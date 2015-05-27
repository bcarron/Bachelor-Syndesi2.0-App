package ch.unige.carron8.bachelor.controllers;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import ch.unige.carron8.bachelor.models.BroadcastTypes;

/**
 * Sends data to the server and fire broadcast intents to update the user interface
 * Created by Blaise on 30.04.2015.
 */
public class SendDataTask extends AsyncTask<SensorEvent, Void, SensorEvent> {
    private Context mContext;

    public SendDataTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected SensorEvent doInBackground(SensorEvent... params) {
        SensorEvent event = params[0];
        Float data = event.values[0];

        //Send data to server
        RESTService.getInstance(mContext).sendData(data, SensorController.getStringType(event.sensor.getType()));

        //Send broadcast to update the UI if the app is active
        /*Intent localIntent = null;
        if(event.sensor.getType() == Sensor.TYPE_LIGHT){
            localIntent = new Intent(BroadcastTypes.BCAST_TYPE_SENSOR_LIGHT.toString());
        } else if(event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE){
            localIntent = new Intent(BroadcastTypes.BCAST_TYPE_SENSOR_TEMP.toString());
        }else{
            Log.d("SENSOR","ERROR");
        }*/
        Intent localIntent = new Intent(String.valueOf(event.sensor.getType()));
        localIntent.putExtra(BroadcastTypes.BCAST_EXTRA_SENSOR_DATA.toString(), data);
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(mContext);
        broadcastManager.sendBroadcast(localIntent);

        return event;
    }

}