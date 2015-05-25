package ch.unige.carron8.bachelor.models;

/**
 * Represents the active user's account
 * Created by Blaise on 30.04.2015.
 */
public class Account {
    private int mId;
    private String mName;
    private String mSurname;
    private String mOffice;

    public Account() {
    }

    public Account(String name, String surname, String office) {
        this.mName = name;
        this.mSurname = surname;
        this.mOffice = office;
        this.mId = 0; //Local id before sending account to the server
    }

    public void updateAccount(String name, String surname, String office) {
        this.mName = name;
        this.mSurname = surname;
        this.mOffice = office;
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

    @Override
    public String toString() {
        return "Account{" +
                "id=" + mId +
                ", mName='" + mName + '\'' +
                ", mSurname='" + mSurname + '\'' +
                ", mOffice='" + mOffice + '\'' +
                '}';
    }
}
