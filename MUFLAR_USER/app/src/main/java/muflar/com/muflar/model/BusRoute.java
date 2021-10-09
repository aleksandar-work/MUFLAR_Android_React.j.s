package muflar.com.muflar.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Prince on 8/5/2018.
 */

public class BusRoute {
    @SerializedName("id")
    public String id;
    @SerializedName("BusRoute")
    public String busRoute;
    @SerializedName("Bus_ID")
    public String busId;
    @SerializedName("Zone")
    public String zone;

    public BusRoute(String id, String busRoute, String busId, String zone) {
        this.id = id;
        this.busRoute = busRoute;
        this.busId = busId;
        this.zone = zone;
    }
}
