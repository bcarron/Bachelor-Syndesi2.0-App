package ch.unige.carron8.bachelor.controllers.network;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorEvent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import ch.unige.carron8.bachelor.controllers.sensor.SensorList;
import ch.unige.carron8.bachelor.models.BroadcastTypes;
import ch.unige.carron8.bachelor.views.ControllerActivity;

/**
 * Sends data to the server and fire broadcast intents to update the user interface
 * Created by Blaise on 30.04.2015.
 */
public class fetchDevicesTask extends AsyncTask<Void, Void, Void> {
    private Context mContext;

    public fetchDevicesTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        final ArrayList<String> bulbsNID = new ArrayList<>();
        //Get devices
        final String url = "http://129.194.70.52:8111/ero2proxy/service/type/xml_rspec";

        StringRequest jsonRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("HTTP", response);

                        try {
                            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                            Document document = documentBuilder.parse(new InputSource(new StringReader(response)));

                            NodeList nl = document.getElementsByTagName("gpio");
                            for(int i = 0; i < nl.getLength(); i++){
                                Node n = nl.item(i);
                                Element e = (Element) n.getFirstChild();
                                String device = e.getAttribute("name");
                                if(device.contains("bulb")){
                                    bulbsNID.add(device.substring(device.indexOf("NID: "),device.length()));
                                }
                            }
                            ((ControllerActivity)mContext).addLights(bulbsNID);
                        } catch (ParserConfigurationException e) {
                            e.printStackTrace();
                        } catch (SAXException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("HTTP", "Error connecting to server address " + url);
            }
        });

        RequestQueue mRequestQueue = Volley.newRequestQueue(mContext);
        mRequestQueue.add(jsonRequest);

        return null;
    }
}