package muflar.com.muflar.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Prince on 8/6/2018.
 */

public class ETAResponse {
    @SerializedName("BUSROUTE")
    public String busRoute;
    @SerializedName("FARE")
    public String fare;
    @SerializedName("ETA")
    public String eta;
    @SerializedName("BUS_ID")
    public String busID;
}
