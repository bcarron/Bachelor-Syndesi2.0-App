package ch.unige.carron8.bachelor.models;

import java.util.ArrayList;

/**
 * Represents the active user's account
 * Created by Blaise on 30.04.2015.
 */
public class Account {
    private int mId;
    private String mName;
    private String mSurname;
    private String mOffice;
    private int mTargetLight;
    private int mTargetTemp;
    private ArrayList<String> mAvailableSensors;

    public Account(String name, String surname, String office, int targetLight, int targetTemp) {
        this.mName = name;
        this.mSurname = surname;
        this.mOffice = office;
        this.mTargetLight = targetLight;
        this.mTargetTemp = targetTemp;
        this.mId = 0; //Local id before sending account to the server
        mAvailableSensors = new ArrayList<String>();
    }

    public void updateAccount(String name, String surname, String office, int targetLight, int targetTemp) {
        this.mName = name;
        this.mSurname = surname;
        this.mOffice = office;
        this.mTargetLight = targetLight;
        this.mTargetTemp = targetTemp;
    }

    public int getmId() { return mId; }

    public void setmId(int mId) { this.mId = mId; }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmSurname() {
        return mSurname;
    }

    public void setmSurname(String mSurname) {
        this.mSurname = mSurname;
    }

    public String getmOffice() {
        return mOffice;
    }

    public void setmOffice(String mOffice) {
        this.mOffice = mOffice;
    }

    public int getmTargetLight() {
        return mTargetLight;
    }

    public void setmTargetLight(int mTargetLight) {
        this.mTargetLight = mTargetLight;
    }

    public int getmTargetTemp() {
        return mTargetTemp;
    }

    public void setmTargetTemp(int mTargetTemp) {
        this.mTargetTemp = mTargetTemp;
    }

    public ArrayList<String> getmAvailableSensors() {
        return mAvailableSensors;
    }

    public void setmAvailableSensors(ArrayList<String> mAvailableSensors) {
        this.mAvailableSensors = mAvailableSensors;
    }
}
