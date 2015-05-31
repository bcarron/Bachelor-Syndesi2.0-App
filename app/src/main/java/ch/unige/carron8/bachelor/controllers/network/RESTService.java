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
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import ch.unige.carron8.bachelor.R;
import ch.unige.carron8.bachelor.controllers.account.AccountController;
import ch.unige.carron8.bachelor.models.BroadcastTypes;
import ch.unige.carron8.bachelor.models.PreferenceKeys;

/**
 * Implements a REST service in a singleton class to send data to the server
 * Created by Blaise on 04.05.2015.
 */
public class RESTService {
    private static RESTService mInstance;
    private Context mContext;
    private RequestQueue mRequestQueue;
    private AccountController mAccountController;

    public RESTService(Context context) {
        mContext = context;
        mRequestQueue = this.getRequestQueue();
        mAccountController = AccountController.getInstance(mContext);
    }

    public static synchronized RESTService getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RESTService(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
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
        String server_url = PreferenceManager.getDefaultSharedPreferences(mContext).getString(PreferenceKeys.PREF_SERVER_URL.toString(), "");

        if (!server_url.equals("")) {
            // Instantiate the RequestQueue.
            if (server_url.length() > 7 && !server_url.substring(0, 7).equals("http://")) {
                server_url = "http://" + server_url;
            }
            final String url = server_url + "/crowddata";

            JSONObject dataJSON = mAccountController.formatDataJSON(data, dataType);

            // Request a string response from the provided URL.
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, dataJSON,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("HTTP", response.toString());
                            //Send broadcast to update the UI if the app is active
                            RESTService.sendServerStatusBcast(mContext, "Server Ok");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("HTTP", "Error connecting to server address " + url);
                    //Send broadcast to update the UI if the app is active
                    RESTService.sendServerStatusBcast(mContext, mContext.getString(R.string.connection_error) + ": " + url);
                }
            });

            mRequestQueue.add(jsonRequest);
        } else {
            RESTService.sendServerStatusBcast(mContext, mContext.getString(R.string.connection_no_server_set));
        }
    }

    /**
     * Creates the current user account on the server
     *
     * @param account the account to create
     */
    public void createAccount(JSONObject account) {
        String server_url = PreferenceManager.getDefaultSharedPreferences(mContext).getString(PreferenceKeys.PREF_SERVER_URL.toString(), "");

        if (!server_url.equals("")) {
            // Instantiate the RequestQueue.
            if (server_url.length() > 7 && !server_url.substring(0, 7).equals("http://")) {
                server_url = "http://" + server_url;
            }
            final String url = server_url + "/crowdusers";

            //Initiate the JSON request
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, account,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            mAccountController.setAccountJSON(response);
                            Log.d("HTTP", response.toString());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("HTTP", "Error connecting to server " + url);
                    RESTService.sendServerStatusBcast(mContext, mContext.getString(R.string.connection_error) + ": " + url);
                }
            });

            mRequestQueue.add(jsonRequest);
        } else {
            RESTService.sendServerStatusBcast(mContext, mContext.getString(R.string.connection_no_server_set));
        }
    }

    /**
     * Updates the current account on the server
     *
     * @param account the account to update
     */
    public void updateAccount(JSONObject account) {
        String server_url = PreferenceManager.getDefaultSharedPreferences(mContext).getString(PreferenceKeys.PREF_SERVER_URL.toString(), "");

        if (!server_url.equals("")) {
            // Instantiate the RequestQueue.
            if (server_url.length() > 7 && !server_url.substring(0, 7).equals("http://")) {
                server_url = "http://" + server_url;
            }
            final String url = server_url + "/crowdusers";

            //Initiate the JSON request
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.PUT, url, account,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            mAccountController.setAccountJSON(response);
                            Log.d("HTTP", response.toString());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("HTTP", "Error connecting to server " + url);
                    RESTService.sendServerStatusBcast(mContext, mContext.getString(R.string.connection_error) + ": " + url);
                }
            });

            mRequestQueue.add(jsonRequest);
        } else {
            RESTService.sendServerStatusBcast(mContext, mContext.getString(R.string.connection_no_server_set));
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
        Intent localIntent = new Intent(BroadcastTypes.BCAST_TYPE_SERVER_STATUS.toString());
        localIntent.putExtra(BroadcastTypes.BCAST_EXTRA_SERVER_RESPONSE.toString(), status);
        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
    }
}
