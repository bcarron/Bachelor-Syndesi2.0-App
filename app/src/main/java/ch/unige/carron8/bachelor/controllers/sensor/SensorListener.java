package ch.unige.carron8.bachelor.controllers.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;

import ch.unige.carron8.bachelor.controllers.network.SendDataTask;
import ch.unige.carron8.bachelor.models.PreferenceKey;

/**
 * Listens to sensors and prepares an AsyncTask to send data to the server and update the user interface.
 * Created by Blaise on 27.05.2015.
 */
public class SensorListener implements SensorEventListener {
    private Context mAppContext;
    private SensorManager mSensorManager;
    private int mSensedData;
    private PowerManager.WakeLock mWakeLock;

    public SensorListener(Context appContext, SensorManager sensorManager){
        this.mAppContext = appContext;
        this.mSensorManager = sensorManager;
        this.mSensedData = 0;
        this.mWakeLock = ((PowerManager) appContext.getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "WakeLock");
        this.mWakeLock.acquire();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("SENSOR", "Accuracy changed: " + accuracy);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(PreferenceManager.getDefaultSharedPreferences(mAppContext).getBoolean(PreferenceKey.PREF_SENSOR_PERM.toString(), false)){
            AsyncTask sendData = new SendDataTask(mAppContext);
            sendData.execute(new SensorEvent[]{sensorEvent});
        }
        if(sensorEvent.sensor.getType() != Sensor.TYPE_PROXIMITY) {
            mSensorManager.unregisterListener(this);
            this.mWakeLock.release();
        }
        //Trick to get data from proximity sensor
        else{
            if(mSensedData < 2){
                mSensedData++;
            }else{
                mSensorManager.unregisterListener(this);
                this.mWakeLock.release();
            }
        }
    }
}
