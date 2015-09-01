package ch.unige.carron8.bachelor.controllers.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import java.util.HashMap;

import ch.unige.carron8.bachelor.R;
import ch.unige.carron8.bachelor.controllers.sensor.SensorList;
import ch.unige.carron8.bachelor.models.BroadcastType;
import ch.unige.carron8.bachelor.models.SensorData;
import ch.unige.carron8.bachelor.views.MainActivity;

/**
 * Updates the user interface by receiving broadcasts
 * Created by Blaise on 01.05.2015.
 */
public class UIReceiver extends BroadcastReceiver {
    private Activity mActivity;

    public UIReceiver(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(BroadcastType.BCAST_TYPE_SERVER_STATUS.toString())) {
            String response = intent.getStringExtra(BroadcastType.BCAST_EXTRA_SERVER_RESPONSE.toString());
            TextView server = (TextView) mActivity.findViewById(R.id.server_display_status);
            server.setText(response);
        }else if (intent.getAction().equals(BroadcastType.BCAST_TYPE_CONTROLLER_STATUS.toString())) {
            String response = intent.getStringExtra(BroadcastType.BCAST_EXTRA_SERVER_RESPONSE.toString());
            TextView server = (TextView) mActivity.findViewById(R.id.controller_display_status);
            server.setText(response);
        }else{
            Float data = intent.getFloatExtra(BroadcastType.BCAST_EXTRA_SENSOR_DATA.toString(), 0);
            ((MainActivity)mActivity).addSensor(new SensorData("", data, intent.getAction()));
        }
    }
}
