package com.muflar_driver.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Prince on 8/6/2018.
 */

public class UpdateLocationInfo {
    @SerializedName("CurrentLatitude")
    public String currentLatitude;
    @SerializedName("CurrentLongitude")
    public String CurrentLongitude;
    @SerializedName("Bus_ID")
    public String busID;
    @SerializedName("BusRoute")
    public String busRoute;
}
