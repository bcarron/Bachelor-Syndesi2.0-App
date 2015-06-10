package ch.unige.carron8.bachelor.controllers.sensor;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.PowerManager;

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
        PowerManager.WakeLock wakeLock = ((PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "WakeLock");
        wakeLock.acquire();
        sensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Integer.parseInt(workIntent.getAction()));
        sensorManager.registerListener(new SensorListener(getApplicationContext(), sensorManager), sensor, SensorManager.SENSOR_DELAY_NORMAL);
        wakeLock.acquire();
    }
}
