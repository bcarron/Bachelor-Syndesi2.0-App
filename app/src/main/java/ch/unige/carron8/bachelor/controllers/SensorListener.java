package ch.unige.carron8.bachelor.controllers;

import android.app.IntentService;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.util.Log;

import ch.unige.carron8.bachelor.models.BroadcastTypes;

/**
 * Listens to sensors and prepare an AsyncTask to send data to the server and update the user interface.
 * Created by Blaise on 30.04.2015.
 */
public class SensorListener extends IntentService implements SensorEventListener {
    private SensorManager sensorManager;

    public SensorListener() {
        super("SensorListener");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        sensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(workIntent.getIntExtra(BroadcastTypes.BCAST_EXTRA_SENSOR_TYPE.toString(), -1));
        if (sensor != null) {
            Log.d("SENSOR:", sensor.toString());
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("SENSOR", "Accuracy changed: " + accuracy);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        AsyncTask sendData = new SendDataTask(getApplicationContext());
        sendData.execute(new SensorEvent[]{sensorEvent});
        sensorManager.unregisterListener(this);
        this.stopSelf();
    }
}
