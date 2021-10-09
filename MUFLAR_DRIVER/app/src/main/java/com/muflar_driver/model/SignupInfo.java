package com.muflar_driver.model;

        import com.google.gson.annotations.SerializedName;

/**
 * Created by Prince on 8/6/2018.
 */

public class SignupInfo {
    @SerializedName("Bus_ID")
    public String bus_ID;
    @SerializedName("BusRoute")
    public String busRoute;
    @SerializedName("Created_Date")
    public String created_Date;
    @SerializedName("Password")
    public String password;
}
