package muflar.com.muflar.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Prince on 8/5/2018.
 */

public class Ride {
    @SerializedName("id")
    public String id;
    @SerializedName("CardID")
    public String cardID;
    @SerializedName("BusRoute")
    public String busRoute;
    @SerializedName("BusID")
    public String busID;
    @SerializedName("Entry")
    public String entry;
    @SerializedName("Exit")
    public String exit;
    @SerializedName("FareCharged")
    public String fareCharged;
    @SerializedName("Travel_Date")
    public String travelDate;
    @SerializedName("Entry_Time")
    public String entryTime;
    @SerializedName("Exit_Time")
    public String exitTime;
    @SerializedName("Entry_CardReaderID")
    public String entryCardReaderID;
    @SerializedName("Exit_CardReaderID")
    public String exitCardReaderID;
    @SerializedName("Fare_Type")
    public String fareType;
}
