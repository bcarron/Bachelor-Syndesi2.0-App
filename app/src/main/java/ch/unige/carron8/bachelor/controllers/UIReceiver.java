package ch.unige.carron8.bachelor.controllers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.util.Log;
import android.widget.TextView;

import ch.unige.carron8.bachelor.R;
import ch.unige.carron8.bachelor.models.BroadcastTypes;

/**
 * Updates the user interface by receiving broadcasts
 * Created by Blaise on 01.05.2015.
 */
public class UIReceiver extends BroadcastReceiver {
    private Activity mContext;

    public UIReceiver(Activity context) {
        mContext = context;
    }

    //TODO: Update the data of multiple sensors
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(String.valueOf(Sensor.TYPE_LIGHT))) {
            Float data = intent.getFloatExtra(BroadcastTypes.BCAST_EXTRA_SENSOR_DATA.toString(), 0);
            TextView light_data = (TextView) mContext.findViewById(R.id.sensors_display_light_data);
            TextView display = (TextView) mContext.findViewById(R.id.sensors_display_light);
            light_data.setText(String.valueOf(data) + " lx");
            display.setText(R.string.sensors_light_enabled);
        }else if(intent.getAction().equals(String.valueOf(Sensor.TYPE_AMBIENT_TEMPERATURE))){
            Float data = intent.getFloatExtra(BroadcastTypes.BCAST_EXTRA_SENSOR_DATA.toString(), 0);
            TextView temp_data = (TextView) mContext.findViewById(R.id.sensors_display_temp_data);
            TextView display = (TextView) mContext.findViewById(R.id.sensors_display_temp);
            temp_data.setText(String.valueOf(data) + " °C");
            display.setText(R.string.sensors_light_enabled);
        }else if (intent.getAction().equals(BroadcastTypes.BCAST_TYPE_SERVER_STATUS.toString())) {
            String response = intent.getStringExtra(BroadcastTypes.BCAST_EXTRA_SERVER_RESPONSE.toString());
            TextView server = (TextView) mContext.findViewById(R.id.server_display_status);
            server.setText(response);
        }
    }
}
