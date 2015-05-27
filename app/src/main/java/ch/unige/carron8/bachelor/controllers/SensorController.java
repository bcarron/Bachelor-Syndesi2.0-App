package ch.unige.carron8.bachelor.controllers;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

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
    private ArrayList<PendingIntent> sensorsLauncher;
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
                this.disableSensors();
                this.startSensors();
                Log.d("PREF", "Sensor polling rate changed");
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

        //Get all sensors
        this.getSensorLaunchers();

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

    public void startSensors() {
        ((TextView) mContext.findViewById(R.id.sensors_status)).setText("");
        //Set Alarm to launch the listener
        AccountController.getInstance(mContext).updateAccount();
        for(PendingIntent sensorLauncher : sensorsLauncher){
            mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(mContext).getString(PreferenceKeys.PREF_SENSOR_RATE.toString(), "30")) * 1000, sensorLauncher);
        }
    }

    public void disableSensors() {
        ((TextView) mContext.findViewById(R.id.sensors_status)).setText(R.string.sensors_disabled);
        ((TextView) mContext.findViewById(R.id.server_display_status)).setText(R.string.connection_no_data);
        //Disable alarms
        for(PendingIntent sensorLauncher : sensorsLauncher){
            mAlarmManager.cancel(sensorLauncher);
        }
    }

    public void getSensorLaunchers(){
        sensorsLauncher = new ArrayList<PendingIntent>();
        SensorManager sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null){
            Intent sensorIntent = new Intent(mContext, SensorService.class);
            sensorIntent.setAction(String.valueOf(Sensor.TYPE_LIGHT));
            sensorsLauncher.add(PendingIntent.getService(mContext, 0, sensorIntent, PendingIntent.FLAG_UPDATE_CURRENT));
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null){
            Intent sensorIntent = new Intent(mContext, SensorService.class);
            sensorIntent.setAction(String.valueOf(Sensor.TYPE_AMBIENT_TEMPERATURE));
            sensorsLauncher.add(PendingIntent.getService(mContext, 1, sensorIntent, PendingIntent.FLAG_UPDATE_CURRENT));
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) != null){
            Intent sensorIntent = new Intent(mContext, SensorService.class);
            sensorIntent.setAction(String.valueOf(Sensor.TYPE_PRESSURE));
            sensorsLauncher.add(PendingIntent.getService(mContext, 2, sensorIntent, PendingIntent.FLAG_UPDATE_CURRENT));
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY) != null){
            Intent sensorIntent = new Intent(mContext, SensorService.class);
            sensorIntent.setAction(String.valueOf(Sensor.TYPE_RELATIVE_HUMIDITY));
            sensorsLauncher.add(PendingIntent.getService(mContext, 3, sensorIntent, PendingIntent.FLAG_UPDATE_CURRENT));
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null){
            Intent sensorIntent = new Intent(mContext, SensorService.class);
            sensorIntent.setAction(String.valueOf(Sensor.TYPE_PROXIMITY));
            sensorsLauncher.add(PendingIntent.getService(mContext, 4, sensorIntent, PendingIntent.FLAG_UPDATE_CURRENT));
        }
    }

    public static String getStringType(int sensorType){
        String stringType;
        switch (sensorType){
            case Sensor.TYPE_LIGHT: stringType = "LIGHT"; break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE: stringType = "TEMPERATURE"; break;
            case Sensor.TYPE_PRESSURE: stringType = "PRESSURE"; break;
            case Sensor.TYPE_RELATIVE_HUMIDITY: stringType = "HUMIDITY"; break;
            case Sensor.TYPE_PROXIMITY: stringType = "PROXIMITY"; break;
            default: stringType = "Undefined"; break;
        }
        return stringType;
    }

    public static String getStringUnit(int sensorType){
        String stringType;
        switch (sensorType){
            case Sensor.TYPE_LIGHT: stringType = "lx"; break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE: stringType = "°C"; break;
            case Sensor.TYPE_PRESSURE: stringType = "hPa"; break;
            case Sensor.TYPE_RELATIVE_HUMIDITY: stringType = "%"; break;
            case Sensor.TYPE_PROXIMITY: stringType = "cm"; break;
            default: stringType = "Undefined"; break;
        }
        return stringType;
    }

    public void setmContext(Activity context) {
        this.mContext = context;
    }
}
