package muflar.com.muflar.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Prince on 8/6/2018.
 */

public class Bus {
    @SerializedName("id")
    public String id;
    @SerializedName("Bus_ID")
    public String busID;
    @SerializedName("Owner")
    public String owner;
    @SerializedName("Driver")
    public String driver;
    @SerializedName("Card_Reader_ID1")
    public String cardReaderID1;
    @SerializedName("Card_Reader_ID2")
    public String cardReaderID2;
    @SerializedName("Controller_ID")
    public String controllerID;
    @SerializedName("AdPanelID")
    public String adPanelID;
}
