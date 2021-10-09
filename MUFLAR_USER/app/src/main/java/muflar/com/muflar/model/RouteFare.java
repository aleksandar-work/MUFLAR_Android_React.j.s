package muflar.com.muflar.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Prince on 8/8/2018.
 */

public class RouteFare {
    @SerializedName("id")
    public String id;
    @SerializedName("ids")
    public String ids;
    @SerializedName("BusRoute")
    public String busRoute;
    @SerializedName("From_route")
    public String fromRoute;
    @SerializedName("To_route")
    public String toRoute;
    @SerializedName("Fare")
    public String fare;

    public String driveTime;
}
