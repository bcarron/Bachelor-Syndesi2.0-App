package ch.unige.carron8.bachelor.controllers.account;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import ch.unige.carron8.bachelor.controllers.network.RESTService;
import ch.unige.carron8.bachelor.models.Account;
import ch.unige.carron8.bachelor.models.PreferenceKey;
import ch.unige.carron8.bachelor.models.SensorData;

/**
 * Manages account information on the app and the server in a singleton controller
 * and provides easy methods to convert to and from JSON
 * Created by Blaise on 05.05.2015.
 */
public class AccountController {
    private static AccountController mInstance;
    private Context mAppContext;
    private Gson mGson;
    private SharedPreferences mAccountPref;
    private SharedPreferences.Editor mAccountPrefEditor;


    public AccountController(Context context) {
        this.mAppContext = context;
        this.mGson = new Gson();
        this.mAccountPref = PreferenceManager.getDefaultSharedPreferences(mAppContext);
        this.mAccountPrefEditor = mAccountPref.edit();
    }

    public static synchronized AccountController getInstance(Context appContext) {
        if (mInstance == null) {
            mInstance = new AccountController(appContext);
        }
        return mInstance;
    }

    public void updateAccount() {
        RESTService.getInstance(mAppContext).updateAccount(this.getJSON());
    }

    public void createAccount(Account account) {
        this.setAccount(account);
        RESTService.getInstance(mAppContext).createAccount(this.getJSON());
    }

    public void saveAccount(Account account) {
        this.setAccount(account);
        RESTService.getInstance(mAppContext).updateAccount(this.getJSON());
    }

    public JSONObject formatDataJSON(Float data, String dataType) {
        SensorData sensorData = new SensorData(this.getAccount().getmId(), data, dataType);
        JSONObject dataJSON = null;
        try {
            dataJSON = new JSONObject(mGson.toJson(sensorData));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataJSON;
    }

    public Account getAccount() {
        return mGson.fromJson(mAccountPref.getString(PreferenceKey.PREF_SAVED_ACCOUNT.toString(), ""), Account.class);
    }

    public String getGSON() {
        return mAccountPref.getString(PreferenceKey.PREF_SAVED_ACCOUNT.toString(), "");
    }

    public JSONObject getJSON() {
        JSONObject JSONaccount = null;
        try {
            JSONaccount = new JSONObject(mAccountPref.getString(PreferenceKey.PREF_SAVED_ACCOUNT.toString(), ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return JSONaccount;
    }

    public void setAccount(Account account) {
        mAccountPrefEditor.putString(PreferenceKey.PREF_SAVED_ACCOUNT.toString(), mGson.toJson(account));
        mAccountPrefEditor.commit();
    }
}
