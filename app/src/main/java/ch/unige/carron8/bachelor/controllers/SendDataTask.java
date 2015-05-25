package ch.unige.carron8.bachelor.controllers;

import android.content.Context;
import android.content.Intent;
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
        Log.d("SENSOR", String.valueOf(data) + " lx");

        //Send data to server
        RESTService.getInstance(mContext).sendData(data, BroadcastTypes.BCAST_TYPE_SENSOR_LIGHT.toString());

        //Send broadcast to update the UI if the app is active
        Intent localIntent = new Intent(BroadcastTypes.BCAST_TYPE_SENSOR_LIGHT.toString());
        localIntent.putExtra(BroadcastTypes.BCAST_EXTRA_SENSOR_DATA.toString(), data);
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(mContext);
        broadcastManager.sendBroadcast(localIntent);

        return event;
    }

}