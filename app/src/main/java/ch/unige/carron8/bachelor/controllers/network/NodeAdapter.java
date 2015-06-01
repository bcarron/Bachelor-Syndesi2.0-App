package ch.unige.carron8.bachelor.controllers.network;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ch.unige.carron8.bachelor.R;
import ch.unige.carron8.bachelor.models.DeviceNode;

/**
 * Created by Blaise on 01.06.2015.
 */
public class NodeAdapter extends ArrayAdapter<DeviceNode>{
    private final Context mContext;
    private final ArrayList<DeviceNode> mNodesList;

    public NodeAdapter(Context context, ArrayList<DeviceNode> nodesList) {
        super(context, R.layout.node_display, nodesList);
        this.mContext = context;
        this.mNodesList = nodesList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View nodeView = inflater.inflate(R.layout.node_display, parent, false);
        TextView label = (TextView) nodeView.findViewById(R.id.node_label);
        TextView status = (TextView) nodeView.findViewById(R.id.node_status);
        ImageView image = (ImageView) nodeView.findViewById(R.id.node_icon);

        DeviceNode node = mNodesList.get(position);
        label.setText(node.getmNID());
        status.setText(node.getmStatus());
        image.setImageResource(node.getmType().getIcon(node.getmStatus()));

        return nodeView;
    }
}
