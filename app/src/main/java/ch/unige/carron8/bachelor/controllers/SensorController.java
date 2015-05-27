package ch.unige.carron8.bachelor.controllers;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import ch.unige.carron8.bachelor.R;
import ch.unige.carron8.bachelor.models.BroadcastTypes;
import ch.unige.carron8.bachelor.models.PreferenceKeys;

/**
 * Manages sensors and reacts to settings changes to adapt the sensors in a singleton controller.
 * Created by Blaise on 01.05.2015.
 */
public class SensorController implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static SensorController mInstance;
    private Activity mContext;
    private PendingIntent mLightSensorLauncher;
    private PendingIntent mTempSensorLauncher;
    private AlarmManager mAlarmManager;

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PreferenceKeys.PREF_SERVER_URL.toString())) {
            Log.d("PREF", "Server changed");
            final TextView connection = (TextView) mContext.findViewById(R.id.server_display_status);
            String server_url = PreferenceManager.getDefaultSharedPreferences(mContext).getString(PreferenceKeys.PREF_SERVER_URL.toString(), "");

            if (server_url.equals("")) {
                connection.setText(R.string.connection_no_server_set);
            } else {
                AccountController.getInstance(mContext).updateAccount();
                Log.d("PREF", "User account updated");
            }
        }
        if (key.equals(PreferenceKeys.PREF_SENSOR_RATE.toString())) {
            if (sharedPreferences.getBoolean(PreferenceKeys.PREF_LIGHT_PERM.toString(), false)) {
                mAlarmManager.cancel(mLightSensorLauncher);
                Integer rate = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(mContext).getString(PreferenceKeys.PREF_SENSOR_RATE.toString(), "30"));
                mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, rate * 1000, mLightSensorLauncher);
                Log.d("PREF", "Rate changed to: " + rate);
            }
        }
        //TODO: Looking for multiple sensors
        if (key.equals(PreferenceKeys.PREF_LIGHT_PERM.toString())) {
            if (sharedPreferences.getBoolean(PreferenceKeys.PREF_LIGHT_PERM.toString(), false)) {
                this.startSensors();
                Log.d("PREF", "Sensors enabled");
            } else {
                this.disableSensors();
                Log.d("PREF", "Sensors disabled");
            }
        }
    }

    public SensorController(Activity context) {
        this.mContext = context;
        //TODO: Looking for multiple sensors
        //Set up the sensor listener
        Intent mLightSensor = new Intent(mContext, SensorService.class);
        mLightSensor.setAction(BroadcastTypes.BCAST_TYPE_SENSOR_LIGHT.toString());
        mLightSensor.putExtra(BroadcastTypes.BCAST_EXTRA_SENSOR_TYPE.toString(), Sensor.TYPE_LIGHT);
        mLightSensorLauncher = PendingIntent.getService(mContext, 0, mLightSensor, PendingIntent.FLAG_UPDATE_CURRENT);

        //OTHER SENSORS
        Intent mTempSensor = new Intent(mContext, SensorService.class);
        mTempSensor.setAction(BroadcastTypes.BCAST_TYPE_SENSOR_TEMP.toString());
        mTempSensor.putExtra(BroadcastTypes.BCAST_EXTRA_SENSOR_TYPE.toString(), Sensor.TYPE_AMBIENT_TEMPERATURE);
        mTempSensorLauncher = PendingIntent.getService(mContext, 1, mTempSensor, PendingIntent.FLAG_UPDATE_CURRENT);

        //Get the alarm manager
        mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        if (sharedPreferences.getBoolean(PreferenceKeys.PREF_LIGHT_PERM.toString(), false)) {
            this.startSensors();
        } else {
            this.disableSensors();
        }
        if (sharedPreferences.getString(PreferenceKeys.PREF_SERVER_URL.toString(), "").equals("")) {
            ((TextView) mContext.findViewById(R.id.server_display_status)).setText(R.string.connection_no_server_set);
        }
    }

    public static synchronized SensorController getInstance(Activity context) {
        if (mInstance == null) {
            mInstance = new SensorController(context);
        }
        return mInstance;
    }

    //TODO: Starting multiple sensors according to preferences
    public void startSensors() {
        //Set Alarm to launch the listener
        AccountController.getInstance(mContext).updateAccount();
        mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(mContext).getString(PreferenceKeys.PREF_SENSOR_RATE.toString(), "30")) * 1000, mLightSensorLauncher);
        mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(mContext).getString(PreferenceKeys.PREF_SENSOR_RATE.toString(), "30")) * 1000, mTempSensorLauncher);
    }

    //TODO: Disabling multiple sensors according to preferences
    public void disableSensors() {
        ((TextView) mContext.findViewById(R.id.sensors_display_light)).setText(R.string.sensors_light_disabled);
        ((TextView) mContext.findViewById(R.id.sensors_display_light_data)).setText("");
        ((TextView) mContext.findViewById(R.id.server_display_status)).setText(R.string.connection_no_data);
        //Disable alarm
        mAlarmManager.cancel(mLightSensorLauncher);
        mAlarmManager.cancel(mTempSensorLauncher);
    }

    public void setmContext(Activity context) {
        this.mContext = context;
    }
}
