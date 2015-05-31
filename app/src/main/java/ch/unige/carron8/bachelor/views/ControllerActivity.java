package ch.unige.carron8.bachelor.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;

import ch.unige.carron8.bachelor.R;
import ch.unige.carron8.bachelor.controllers.network.fetchDevicesTask;

/**
 * Created by Blaise on 31.05.2015.
 */
public class ControllerActivity extends AppCompatActivity{
    private ArrayList<HashMap<String, String>> lightsListData;
    SimpleAdapter lightsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set the layout
        setContentView(R.layout.activity_controller);
        //Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Set the sensor list
        ListView lightsList = (ListView) findViewById(R.id.lights_list);
        lightsListData = new ArrayList<>();
        lightsListAdapter = new SimpleAdapter(this, lightsListData, R.layout.sensor_display, new String[] {"lights_name", "lights_status"}, new int[] {R.id.sensor_name, R.id.sensor_data});
        lightsList.setAdapter(lightsListAdapter);

        new fetchDevicesTask(this).execute();
    }

    public void addLights(ArrayList<String> bulbsNID){
        for(String s : bulbsNID){
            HashMap<String, String> bulb = new HashMap<>();
            bulb.put("lights_name",s);
            bulb.put("lights_status","ON");
            lightsListData.add(bulb);
        }
        lightsListAdapter.notifyDataSetChanged();
        ((TextView) findViewById(R.id.lights_status)).setText("");
    }
}
