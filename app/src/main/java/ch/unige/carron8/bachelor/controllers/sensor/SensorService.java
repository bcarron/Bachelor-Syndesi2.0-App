package ch.unige.carron8.bachelor.controllers.sensor;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.PowerManager;

/**
 * Prepares a sensor listener for the right sensor.
 * Created by Blaise on 30.04.2015.
 */
public class SensorService extends IntentService {
    private SensorManager sensorManager;

    public SensorService() {
        super("SensorListener");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        //Get a wakelock to keep the system on while sending the data
        PowerManager.WakeLock wakeLock = ((PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "WakeLock");
        wakeLock.acquire();
        sensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        //Get the sensor from the workIntent
        Sensor sensor = sensorManager.getDefaultSensor(Integer.parseInt(workIntent.getAction()));
        //Register a listener to read data from the sensor
        sensorManager.registerListener(new SensorListener(getApplicationContext(), sensorManager), sensor, SensorManager.SENSOR_DELAY_NORMAL);
        wakeLock.acquire();
    }
}
