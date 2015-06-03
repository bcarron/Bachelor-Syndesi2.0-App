package ch.unige.carron8.bachelor.controllers.sensor;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import ch.unige.carron8.bachelor.R;
import ch.unige.carron8.bachelor.controllers.account.AccountController;
import ch.unige.carron8.bachelor.models.Account;
import ch.unige.carron8.bachelor.models.PreferenceKey;
import ch.unige.carron8.bachelor.views.MainActivity;

/**
 * Manages sensors and reacts to settings changes to adapt the sensors in a singleton controller.
 * Created by Blaise on 01.05.2015.
 */
public class SensorController implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static SensorController mInstance;
    private Activity mContext;
    private ArrayList<PendingIntent> sensorsLauncher;
    private AlarmManager mAlarmManager;

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PreferenceKey.PREF_SERVER_URL.toString())) {
            Log.d("PREF", "Server changed");
            final TextView connection = (TextView) mContext.findViewById(R.id.server_display_status);
            String server_url = PreferenceManager.getDefaultSharedPreferences(mContext).getString(PreferenceKey.PREF_SERVER_URL.toString(), "");

            if (server_url.equals("")) {
                connection.setText(R.string.connection_no_server_set);
            } else {
                AccountController.getInstance(mContext).updateAccount();
                Log.d("PREF", "User account updated");
            }
        }
        if (key.equals(PreferenceKey.PREF_SENSOR_RATE.toString())) {
            if (sharedPreferences.getBoolean(PreferenceKey.PREF_SENSOR_PERM.toString(), false)) {
                this.disableSensors();
                this.enableSensors();
                Log.d("PREF", "Sensor polling rate changed");
            }
        }
        if (key.equals(PreferenceKey.PREF_SENSOR_PERM.toString())) {
            if (sharedPreferences.getBoolean(PreferenceKey.PREF_SENSOR_PERM.toString(), false)) {
                this.enableSensors();
                Log.d("PREF", "Sensors enabled");
            } else {
                this.disableSensors();
                Log.d("PREF", "Sensors disabled");
            }
        }
    }

    public SensorController(Activity context) {
        this.mContext = context;

        //Get all sensors
        this.getSensorLaunchers();

        //Get the alarm manager
        mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        if (sharedPreferences.getBoolean(PreferenceKey.PREF_SENSOR_PERM.toString(), false)) {
            this.enableSensors();
        } else {
            this.disableSensors();
        }
        if (sharedPreferences.getString(PreferenceKey.PREF_SERVER_URL.toString(), "").equals("")) {
            ((TextView) mContext.findViewById(R.id.server_display_status)).setText(R.string.connection_no_server_set);
        }
    }

    public static synchronized SensorController getInstance(Activity context) {
        if (mInstance == null) {
            mInstance = new SensorController(context);
        }
        return mInstance;
    }

    public void enableSensors() {
        ((TextView) mContext.findViewById(R.id.sensors_status)).setText("");
        //Set Alarm to launch the listener
        AccountController.getInstance(mContext).updateAccount();
        for(PendingIntent sensorLauncher : sensorsLauncher){
            mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(mContext).getString(PreferenceKey.PREF_SENSOR_RATE.toString(), "30")) * 1000, sensorLauncher);
        }
    }

    public void disableSensors() {
        ((TextView) mContext.findViewById(R.id.sensors_status)).setText(R.string.sensors_disabled);
        ((TextView) mContext.findViewById(R.id.server_display_status)).setText(R.string.connection_no_data);
        ((MainActivity)mContext).removeSensors();
        //Disable alarms
        for(PendingIntent sensorLauncher : sensorsLauncher){
            mAlarmManager.cancel(sensorLauncher);
        }
    }

    public void getSensorLaunchers(){
        sensorsLauncher = new ArrayList<PendingIntent>();
        SensorManager sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        ArrayList<String> availableSensors = new ArrayList<>();
        for(Integer sensorType : SensorList.sensorUsed){
            if (sensorManager.getDefaultSensor(sensorType) != null){
                Intent sensorIntent = new Intent(mContext, SensorService.class);
                sensorIntent.setAction(String.valueOf(sensorType));
                sensorsLauncher.add(PendingIntent.getService(mContext, 0, sensorIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                availableSensors.add(SensorList.getStringType(sensorType));
            }
        }
        Account account = AccountController.getInstance(mContext).getAccount();
        account.setmAvailableSensors(availableSensors);
        AccountController.getInstance(mContext).saveAccount(account);
    }

    public void setmContext(Activity context) {
        this.mContext = context;
    }
}
