package ch.unige.carron8.bachelor.controllers.sensor;

import android.content.Context;
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
public class SensorAdapter extends ArrayAdapter<SensorData> {
private final Context mAppContext;
private final ArrayList<SensorData> mSensorsList;

public SensorAdapter(Context context, ArrayList<SensorData> sensorsList) {
        super(context, R.layout.node_display, sensorsList);
        this.mAppContext = context;
        this.mSensorsList = sensorsList;
        }

@Override
public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mAppContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View nodeView = inflater.inflate(R.layout.node_display, parent, false);
        TextView label = (TextView) nodeView.findViewById(R.id.node_label);
        TextView status = (TextView) nodeView.findViewById(R.id.node_status);
        ImageView image = (ImageView) nodeView.findViewById(R.id.node_icon);

        SensorData sensor = mSensorsList.get(position);
        label.setText(SensorList.getStringType(Integer.parseInt(sensor.getmDataType())));
        status.setText(String.valueOf(sensor.getmData())+" "+SensorList.getStringUnit(Integer.parseInt(sensor.getmDataType())));
        image.setImageResource(SensorList.getIcon(Integer.parseInt(sensor.getmDataType())));

        return nodeView;
        }
}
