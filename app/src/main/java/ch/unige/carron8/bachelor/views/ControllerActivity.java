package ch.unige.carron8.bachelor.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;

import ch.unige.carron8.bachelor.R;
import ch.unige.carron8.bachelor.controllers.network.FetchNodesTask;
import ch.unige.carron8.bachelor.controllers.network.NodeAdapter;
import ch.unige.carron8.bachelor.models.DeviceNode;

/**
 * Created by Blaise on 31.05.2015.
 */
public class ControllerActivity extends AppCompatActivity{
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
        //Set the sensor list
        ListView lightsList = (ListView) findViewById(R.id.nodes_list);
        mNodeList = new ArrayList<>();
        nodeAdapter = new NodeAdapter(this, mNodeList);
        lightsList.setAdapter(nodeAdapter);

        new FetchNodesTask(this).execute();
    }

    public void addNodes(ArrayList<DeviceNode> nodeList){
        mNodeList.addAll(nodeList);
        nodeAdapter.notifyDataSetChanged();
        ((TextView) findViewById(R.id.nodes_status)).setText("");
    }
}
