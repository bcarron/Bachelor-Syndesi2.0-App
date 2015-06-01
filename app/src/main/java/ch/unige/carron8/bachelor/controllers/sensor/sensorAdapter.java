package ch.unige.carron8.bachelor.controllers.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ch.unige.carron8.bachelor.R;
import ch.unige.carron8.bachelor.models.SensorData;

/**
 * Created by Blaise on 02.06.2015.
 */
public class sensorAdapter extends ArrayAdapter<SensorData> {
private final Context mContext;
private final ArrayList<SensorData> mSensorsList;

public sensorAdapter(Context context, ArrayList<SensorData> sensorsList) {
        super(context, R.layout.node_display, sensorsList);
        this.mContext = context;
        this.mSensorsList = sensorsList;
        }

@Override
public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View nodeView = inflater.inflate(R.layout.sensor_display, parent, false);
        TextView label = (TextView) nodeView.findViewById(R.id.sensor_label);
        TextView status = (TextView) nodeView.findViewById(R.id.sensor_data);
        ImageView image = (ImageView) nodeView.findViewById(R.id.sensor_icon);

        SensorData sensor = mSensorsList.get(position);
        label.setText(SensorList.getStringType(Integer.parseInt(sensor.getmDataType())));
        status.setText(String.valueOf(sensor.getmData()));
        image.setImageResource(SensorList.getIcon(Integer.parseInt(sensor.getmDataType())));

        return nodeView;
        }
}
