package ch.unige.carron8.bachelor.controllers;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Listens to sensors and prepares an AsyncTask to send data to the server and update the user interface.
 * Created by Blaise on 27.05.2015.
 */
public class SensorListener implements SensorEventListener {
    private Context mContext;
    private SensorManager mSensorManager;

    public SensorListener(Context context, SensorManager sensorManager){
        this.mContext = context;
        this.mSensorManager = sensorManager;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("SENSOR", "Accuracy changed: " + accuracy);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        AsyncTask sendData = new SendDataTask(mContext);
        sendData.execute(new SensorEvent[]{sensorEvent});
        mSensorManager.unregisterListener(this);
    }
}
