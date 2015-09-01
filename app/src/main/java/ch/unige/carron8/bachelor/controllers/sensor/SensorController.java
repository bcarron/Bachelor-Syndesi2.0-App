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
import ch.unige.carron8.bachelor.models.PreferenceKey;
import ch.unige.carron8.bachelor.views.MainActivity;

/**
 * Manages sensors and reacts to settings changes to adapt the sensors in a singleton controller.
 * Created by Blaise on 01.05.2015.
 */
public class SensorController implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static SensorController mInstance;
    private Activity mActivity;
    private ArrayList<PendingIntent> mSensorsLauncher;
    private AlarmManager mAlarmManager;
    private ArrayList<String> mAvailableSensors;

    public SensorController(Activity activity) {
        this.mActivity = activity;

        //Get all sensors
        this.getSensorLaunchers();

        //Get the alarm manager
        mAlarmManager = (AlarmManager) mActivity.getSystemService(Context.ALARM_SERVICE);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        if (sharedPreferences.getBoolean(PreferenceKey.PREF_SENSOR_PERM.toString(), false)) {
            this.enableSensors();
        } else {
            this.disableSensors();
        }
        if (sharedPreferences.getString(PreferenceKey.PREF_SERVER_URL.toString(), "").equals("")) {
            ((TextView) mActivity.findViewById(R.id.server_display_status)).setText(R.string.connection_no_server_set);
        }
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PreferenceKey.PREF_SERVER_URL.toString())) {
            Log.d("PREF", "Server changed");
            final TextView connection = (TextView) mActivity.findViewById(R.id.server_display_status);
            String server_url = PreferenceManager.getDefaultSharedPreferences(mActivity).getString(PreferenceKey.PREF_SERVER_URL.toString(), "");

            if (server_url.equals("")) {
                connection.setText(R.string.connection_no_server_set);
            } else {
                AccountController.getInstance(mActivity).updateAccount();
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

    public static synchronized SensorController getInstance(Activity activity) {
        if (mInstance == null) {
            mInstance = new SensorController(activity);
        }
        return mInstance;
    }

    public void enableSensors() {
        ((TextView) mActivity.findViewById(R.id.sensors_status)).setText("");
        //Set Alarm to launch the listener
        AccountController.getInstance(mActivity).updateAccount();
        for(PendingIntent sensorLauncher : mSensorsLauncher){
            mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(mActivity).getString(PreferenceKey.PREF_SENSOR_RATE.toString(), "30")) * 1000, sensorLauncher);
        }
    }

    public void disableSensors() {
        ((TextView) mActivity.findViewById(R.id.sensors_status)).setText(R.string.sensors_disabled);
        ((TextView) mActivity.findViewById(R.id.server_display_status)).setText(R.string.connection_no_data);
        ((MainActivity)mActivity).removeSensors();
        //Disable alarms
        for(PendingIntent sensorLauncher : mSensorsLauncher){
            mAlarmManager.cancel(sensorLauncher);
        }
    }

    public void getSensorLaunchers(){
        mSensorsLauncher = new ArrayList<PendingIntent>();
        SensorManager sensorManager = (SensorManager) mActivity.getSystemService(Context.SENSOR_SERVICE);
        mAvailableSensors = new ArrayList<>();
        for(Integer sensorType : SensorList.sensorUsed){
            if (sensorManager.getDefaultSensor(sensorType) != null){
                Intent sensorIntent = new Intent(mActivity, SensorService.class);
                sensorIntent.setAction(String.valueOf(sensorType));
                mSensorsLauncher.add(PendingIntent.getService(mActivity, 0, sensorIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                mAvailableSensors.add(SensorList.getStringType(sensorType));
            }
        }
    }

    public ArrayList<String> getmAvailableSensors() {
        return mAvailableSensors;
    }

    public void setmAvailableSensors(ArrayList<String> mAvailableSensors) {
        this.mAvailableSensors = mAvailableSensors;
    }

    public void setmActivity(Activity activity) {
        this.mActivity = activity;
    }
}
