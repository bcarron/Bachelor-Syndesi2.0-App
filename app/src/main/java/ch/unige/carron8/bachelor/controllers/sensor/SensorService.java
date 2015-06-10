package ch.unige.carron8.bachelor.controllers.sensor;

import android.app.IntentService;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import ch.unige.carron8.bachelor.controllers.WakeLocker;

/**
 * Prepares sensor listener for the right sensor.
 * Created by Blaise on 30.04.2015.
 */
public class SensorService extends IntentService {
    private SensorManager sensorManager;

    public SensorService() {
        super("SensorListener");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        WakeLocker.acquire(getApplicationContext());
        sensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Integer.parseInt(workIntent.getAction()));
        if (sensor != null) {
            sensorManager.registerListener(new SensorListener(getApplicationContext(), sensorManager), sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        WakeLocker.release();
    }
}
