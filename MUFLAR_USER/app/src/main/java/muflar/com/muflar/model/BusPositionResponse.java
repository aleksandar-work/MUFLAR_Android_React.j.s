package muflar.com.muflar.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Prince on 8/6/2018.
 */

public class BusPositionResponse {
    @SerializedName("id")
    public String id;
    @SerializedName("BusRoute")
    public String busRoute;
    @SerializedName("Bus_ID")
    public String busID;
    @SerializedName("Zone")
    public String zone;
    @SerializedName("CutLat")
    public String cutLat;
    @SerializedName("CurLong")
    public String curLong;
    @SerializedName("CurGPS_Location_1")
    public String curGPSLocation1;
    @SerializedName("CurGPS_Location_2")
    public String curGPSLocation2;
    @SerializedName("CurETA")
    public String curETA;
}
