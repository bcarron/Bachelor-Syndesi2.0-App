package ch.unige.carron8.bachelor.models;

/**
 * Holds the data relative to a value coming from a sensor for a given account
 * Created by Blaise on 30.04.2015.
 */
public class SensorData {
    private String mAccountId;
    private float mData;
    private String mDataType;

    public SensorData(String accountId, float data, String dataType) {
        this.mAccountId = accountId;
        this.mData = data;
        this.mDataType = dataType;
    }

    public String getmAccountId() {
        return mAccountId;
    }

    public void setmAccountId(String mAccountId) {
        this.mAccountId = mAccountId;
    }

    public float getmData() {
        return mData;
    }

    public void setmData(float mData) {
        this.mData = mData;
    }

    public String getmDataType() {
        return mDataType;
    }

    public void setmDataType(String mDataType) {
        this.mDataType = mDataType;
    }

    @Override
    public String toString() {
        return "SensorData{" +
                "mAccountId=" + mAccountId +
                ", mData=" + mData +
                ", mDataType=" + mDataType +
                '}';
    }
}
