package ch.unige.carron8.bachelor.models;

/**
 * Created by Blaise on 31.05.2015.
 */
public class NodeDevice {
    private String mNID;
    private NodeType mType;
    private String mStatus;

    public NodeDevice(String NID, NodeType type, String status){
        this.mNID = NID;
        this.mType = type;
        this.mStatus = status;
    }

    public String getmNID() {
        return mNID;
    }

    public void setmNID(String mNID) {
        this.mNID = mNID;
    }

    public NodeType getmType() {
        return mType;
    }

    public void setmType(NodeType mType) {
        this.mType = mType;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }
}
