package ch.unige.carron8.bachelor.views;

import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.unige.carron8.bachelor.R;
import ch.unige.carron8.bachelor.controllers.account.AccountController;
import ch.unige.carron8.bachelor.controllers.sensor.SensorController;
import ch.unige.carron8.bachelor.controllers.ui.UIReceiver;
import ch.unige.carron8.bachelor.models.BroadcastTypes;
import ch.unige.carron8.bachelor.models.PreferenceKeys;

/**
 * Displays the sensors readings and the server status.
 * Created by Blaise on 27.04.2015.
 */
public class MainActivity extends AppCompatActivity {
    private UIReceiver uiReceiver;
    private List<HashMap<String, String>> sensorsListData;
    SimpleAdapter sensorsListadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set default settings
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        //Set the layout
        setContentView(R.layout.activity_main);
        //Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Set the sensor list
        ListView sensorsList = (ListView) findViewById(R.id.sensor_list);
        sensorsListData = new ArrayList<HashMap<String, String>>();
        sensorsListadapter = new SimpleAdapter(this, sensorsListData, R.layout.sensor_display, new String[] {"sensor_name", "sensor_data"}, new int[] {R.id.sensor_name, R.id.sensor_data});
        sensorsList.setAdapter(sensorsListadapter);

        //Detect if an account is set
        if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(PreferenceKeys.PREF_SAVED_ACCOUNT.toString(), "").equals("")) {
            //Create account
            Intent intent = new Intent(this, AccountSetup.class);
            startActivity(intent);
        } else {
            //Creates the broadcast receiver that updates the UI
            uiReceiver = new UIReceiver(this);
            //Set the preferences listener
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).registerOnSharedPreferenceChangeListener(SensorController.getInstance(this));
            Log.d("ACCOUNT", AccountController.getInstance(getApplicationContext()).getGSON());
        }
    }

    public void removeSensors(){
        sensorsListData.clear();
        sensorsListadapter.notifyDataSetChanged();
    }

    public void addSensor(HashMap<String, String> sensorData){
        Boolean sensorExist = false;
        for(HashMap<String, String> data : sensorsListData){
            if(data.get("sensor_name").equals(sensorData.get("sensor_name"))){
                data.put("sensor_data",sensorData.get("sensor_data"));
                sensorExist = true;
            }
        }
        if(!sensorExist){
            sensorsListData.add(sensorData);
        }
        sensorsListadapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu clicks
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }else if(id == R.id.action_controller){
            startActivity(new Intent(this, ControllerActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Life cycle management
    @Override
    protected void onResume() {
        super.onResume();
        //Reset the context on the sensor controller
        SensorController.getInstance(this).setmContext(this);
        //Register the Broadcast listener
        IntentFilter filter = new IntentFilter(String.valueOf(Sensor.TYPE_LIGHT));
        filter.addAction(String.valueOf(Sensor.TYPE_AMBIENT_TEMPERATURE));
        filter.addAction(String.valueOf(Sensor.TYPE_PRESSURE));
        filter.addAction(String.valueOf(Sensor.TYPE_RELATIVE_HUMIDITY));
        filter.addAction(String.valueOf(Sensor.TYPE_PROXIMITY));
        filter.addAction(BroadcastTypes.BCAST_TYPE_SERVER_STATUS.toString());
        LocalBroadcastManager.getInstance(this).registerReceiver(uiReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Unregister the Broadcast listener
        LocalBroadcastManager.getInstance(this).unregisterReceiver(uiReceiver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(String.valueOf(R.id.sensors_status), ((TextView) findViewById(R.id.sensors_status)).getText().toString());
        outState.putString(String.valueOf(R.id.server_display_status), ((TextView) findViewById(R.id.server_display_status)).getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        ((TextView) findViewById(R.id.sensors_status)).setText(savedInstanceState.getString(String.valueOf(R.id.sensors_status)));
        ((TextView) findViewById(R.id.server_display_status)).setText(savedInstanceState.getString(String.valueOf(R.id.server_display_status)));
    }
}
