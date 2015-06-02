package ch.unige.carron8.bachelor.views;

import android.content.IntentFilter;
import android.hardware.Sensor;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;

import ch.unige.carron8.bachelor.R;
import ch.unige.carron8.bachelor.controllers.network.NodeAdapter;
import ch.unige.carron8.bachelor.controllers.network.RESTService;
import ch.unige.carron8.bachelor.controllers.ui.UIReceiver;
import ch.unige.carron8.bachelor.models.BroadcastType;
import ch.unige.carron8.bachelor.models.DeviceNode;

/**
 * Created by Blaise on 31.05.2015.
 */
public class ControllerActivity extends AppCompatActivity{
    private UIReceiver uiReceiver;
    private ArrayList<DeviceNode> mNodeList;
    NodeAdapter nodeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set the layout
        setContentView(R.layout.activity_controller);
        //Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Creates the broadcast receiver that updates the UI
        uiReceiver = new UIReceiver(this);
        //Set the sensor list
        ListView lightsList = (ListView) findViewById(R.id.nodes_list);
        mNodeList = new ArrayList<>();
        nodeAdapter = new NodeAdapter(this, mNodeList);
        lightsList.setAdapter(nodeAdapter);
    }

    public void addNodes(ArrayList<DeviceNode> nodeList){
        Log.d("XML",String.valueOf(nodeList.size()));
        mNodeList.addAll(nodeList);
        nodeAdapter.notifyDataSetChanged();
        ((TextView) findViewById(R.id.controller_display_status)).setText("");
    }

    //Life cycle management
    @Override
    protected void onResume() {
        super.onResume();
        //List nodes
        RESTService.getInstance(this).fetchNodes();
        //Reset the context on the REST service
        RESTService.getInstance(this).setmContext(this);
        //Register the Broadcast listener
        IntentFilter filter = new IntentFilter(String.valueOf(BroadcastType.BCAST_TYPE_CONTROLLER_STATUS));
        LocalBroadcastManager.getInstance(this).registerReceiver(uiReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Unregister the Broadcast listener
        LocalBroadcastManager.getInstance(this).unregisterReceiver(uiReceiver);
    }
}
