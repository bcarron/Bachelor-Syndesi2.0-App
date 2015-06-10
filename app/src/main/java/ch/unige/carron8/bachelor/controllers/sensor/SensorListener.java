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
    private Context mContext;
    private SensorManager mSensorManager;
    private int sensedData;
    private PowerManager.WakeLock mWakeLock;

    public SensorListener(Context context, SensorManager sensorManager){
        this.mContext = context;
        this.mSensorManager = sensorManager;
        this.sensedData = 0;
        this.mWakeLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "WakeLock");
        this.mWakeLock.acquire();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("SENSOR", "Accuracy changed: " + accuracy);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean(PreferenceKey.PREF_SENSOR_PERM.toString(), false)){
            AsyncTask sendData = new SendDataTask(mContext);
            sendData.execute(new SensorEvent[]{sensorEvent});
        }
        if(sensorEvent.sensor.getType() != Sensor.TYPE_PROXIMITY) {
            mSensorManager.unregisterListener(this);
            this.mWakeLock.release();
        }
        //Trick to get data from proximity sensor
        else{
            if(sensedData < 2){
                sensedData++;
            }else{
                mSensorManager.unregisterListener(this);
                this.mWakeLock.release();
            }
        }
    }
}
