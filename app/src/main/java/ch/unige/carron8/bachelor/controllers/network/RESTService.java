package ch.unige.carron8.bachelor.controllers.network;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import ch.unige.carron8.bachelor.R;
import ch.unige.carron8.bachelor.controllers.account.AccountController;
import ch.unige.carron8.bachelor.models.BroadcastType;
import ch.unige.carron8.bachelor.models.NodeDevice;
import ch.unige.carron8.bachelor.models.NodeType;
import ch.unige.carron8.bachelor.models.PreferenceKey;
import ch.unige.carron8.bachelor.views.NodesControllerActivity;

/**
 * Implements a REST service in a singleton class to send data to the server
 * Created by Blaise on 04.05.2015.
 */
public class RESTService {
    private static RESTService mInstance;
    private Context mAppContext;
    private RequestQueue mRequestQueue;
    private AccountController mAccountController;

    public RESTService(Context appContext) {
        mAppContext = appContext;
        mRequestQueue = this.getRequestQueue();
        mAccountController = AccountController.getInstance(mAppContext);
    }

    public static synchronized RESTService getInstance(Context appContext) {
        if (mInstance == null) {
            mInstance = new RESTService(appContext);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mAppContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * Sends data to the server
     *
     * @param data     the data to send
     * @param dataType the type of sensor used to collect the data
     */
    public void sendData(Float data, String dataType) {
        String server_url = PreferenceManager.getDefaultSharedPreferences(mAppContext).getString(PreferenceKey.PREF_SERVER_URL.toString(), "");

        if (!server_url.equals("")) {
            // Instantiate the RequestQueue.
            if (server_url.length() > 7 && !server_url.substring(0, 7).equals("http://")) {
                server_url = "http://" + server_url;
            }
            final String url = server_url + "/crowddata";

            JSONObject dataJSON = mAccountController.formatDataJSON(data, dataType);

            // Request a string response from the provided URL.
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, dataJSON,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("HTTP", response.toString());
                            //Send broadcast to update the UI if the app is active
                            RESTService.sendServerStatusBcast(mAppContext, response.toString());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("HTTP", "Error connecting to server address " + url);
                    //Send broadcast to update the UI if the app is active
                    RESTService.sendServerStatusBcast(mAppContext, mAppContext.getString(R.string.connection_error) + ": " + url);
                }
            });

            mRequestQueue.add(request);
        } else {
            RESTService.sendServerStatusBcast(mAppContext, mAppContext.getString(R.string.connection_no_server_set));
        }
    }

    /**
     * Creates the current user account on the server
     *
     * @param account the account to create
     */
    public void createAccount(JSONObject account) {
        String server_url = PreferenceManager.getDefaultSharedPreferences(mAppContext).getString(PreferenceKey.PREF_SERVER_URL.toString(), "");

        if (!server_url.equals("")) {
            // Instantiate the RequestQueue.
            if (server_url.length() > 7 && !server_url.substring(0, 7).equals("http://")) {
                server_url = "http://" + server_url;
            }
            final String url = server_url + "/crowdusers";

            //Initiate the JSON request
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, account,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("HTTP", response.toString());
                            RESTService.sendServerStatusBcast(mAppContext, response.toString());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("HTTP", "Error connecting to server " + url);
                    RESTService.sendServerStatusBcast(mAppContext, mAppContext.getString(R.string.connection_error) + ": " + url);
                }
            });

            mRequestQueue.add(request);
        } else {
            RESTService.sendServerStatusBcast(mAppContext, mAppContext.getString(R.string.connection_no_server_set));
        }
    }

    /**
     * Updates the current account on the server
     *
     * @param account the account to update
     */
    public void updateAccount(JSONObject account) {
        String server_url = PreferenceManager.getDefaultSharedPreferences(mAppContext).getString(PreferenceKey.PREF_SERVER_URL.toString(), "");

        if (!server_url.equals("")) {
            // Instantiate the RequestQueue.
            if (server_url.length() > 7 && !server_url.substring(0, 7).equals("http://")) {
                server_url = "http://" + server_url;
            }
            final String url = server_url + "/crowdusers";

            //Initiate the JSON request
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, account,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("HTTP", response.toString());
                            RESTService.sendServerStatusBcast(mAppContext, response.toString());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("HTTP", "Error connecting to server " + url);
                    RESTService.sendServerStatusBcast(mAppContext, mAppContext.getString(R.string.connection_error) + ": " + url);
                }
            });

            mRequestQueue.add(request);
        } else {
            RESTService.sendServerStatusBcast(mAppContext, mAppContext.getString(R.string.connection_no_server_set));
        }
    }

    public void fetchNodes() {
        String server_url = PreferenceManager.getDefaultSharedPreferences(mAppContext).getString(PreferenceKey.PREF_SERVER_URL.toString(), "");
        //TESTING URL
        server_url = "http://129.194.70.52:8111/ero2proxy";

        if (!server_url.equals("")) {
            // Instantiate the RequestQueue.
            if (server_url.length() > 7 && !server_url.substring(0, 7).equals("http://")) {
                server_url = "http://" + server_url;
            }
            final String url = server_url + "/service/type/xml_rspec";

            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("HTTP", response);

                    try {
                        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                        Document document = documentBuilder.parse(new InputSource(new StringReader(response)));

                        NodeList nl = document.getElementsByTagName("gpio");
                        for (int i = 0; i < nl.getLength(); i++) {
                            Node n = nl.item(i);
                            Element e = (Element) n.getFirstChild();
                            String device = e.getAttribute("name");
                            NodeType nodeType = NodeType.getType(device);
                            ((NodesControllerActivity) mAppContext).addNode(new NodeDevice(device.substring(device.indexOf("NID: ") + 5, device.length()), nodeType, nodeType.getStatus("default")));
                        }
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
                    RESTService.sendControllerStatusBcast(mAppContext, mAppContext.getString(R.string.connection_error) + ": " + url);
                }
            });

            mRequestQueue.add(request);
        } else {
            RESTService.sendControllerStatusBcast(mAppContext, mAppContext.getString(R.string.connection_no_server_set));
        }
    }

    public void toggleNode(final NodeDevice node) {
        String server_url = PreferenceManager.getDefaultSharedPreferences(mAppContext).getString(PreferenceKey.PREF_SERVER_URL.toString(), "");
        //TESTING URL
        server_url = "http://129.194.70.52:8111/ero2proxy";

        if (!server_url.equals("")) {
            // Instantiate the RequestQueue.
            if (server_url.length() > 7 && !server_url.substring(0, 7).equals("http://")) {
                server_url = "http://" + server_url;
            }
            server_url += "/mediate?service=" + node.getmNID() + "&resource=" + node.getmType() + "&status=" + node.getmType().getToggleStatus(node.getmStatus());

            final String url = server_url;

            StringRequest request = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("HTTP", response);
                            if (response.equals("ERROR")) {
                                RESTService.sendControllerStatusBcast(mAppContext, "Error connecting to the node");
                            } else {
                                ((NodesControllerActivity) mAppContext).addNode(new NodeDevice(node.getmNID(), node.getmType(), NodeType.parseResponse(response)));
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("HTTP", "Error connecting to server address " + url);
                    RESTService.sendControllerStatusBcast(mAppContext, mAppContext.getString(R.string.connection_error) + ": " + url);
                }
            });

            mRequestQueue.add(request);
        } else {
            RESTService.sendControllerStatusBcast(mAppContext, mAppContext.getString(R.string.connection_no_server_set));
        }
    }

    /**
     * Sends the server status in a broadcast to update the user interface
     *
     * @param context the application context
     * @param status  the server status
     */
    public static void sendServerStatusBcast(Context context, String status) {
        //Send broadcast to update the UI if the app is active
        Intent localIntent = new Intent(BroadcastType.BCAST_TYPE_SERVER_STATUS.toString());
        localIntent.putExtra(BroadcastType.BCAST_EXTRA_SERVER_RESPONSE.toString(), status);
        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
    }

    /**
     * Sends the server controller status in a broadcast to update the user interface
     *
     * @param context the application context
     * @param status  the server status
     */
    public static void sendControllerStatusBcast(Context context, String status) {
        //Send broadcast to update the UI if the app is active
        Intent localIntent = new Intent(BroadcastType.BCAST_TYPE_CONTROLLER_STATUS.toString());
        localIntent.putExtra(BroadcastType.BCAST_EXTRA_SERVER_RESPONSE.toString(), status);
        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
    }

    public void setmAppContext(Context appContext) {
        this.mAppContext = appContext;
    }
}
