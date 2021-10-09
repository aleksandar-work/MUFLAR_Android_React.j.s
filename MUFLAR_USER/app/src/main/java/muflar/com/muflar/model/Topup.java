package muflar.com.muflar.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Prince on 8/5/2018.
 */

public class Topup {
    @SerializedName("id")
    public String id;
    @SerializedName("UserID")
    public String userID;
    @SerializedName("CardID")
    public String cardID;
    @SerializedName("BusRoute")
    public String busRoute;
    @SerializedName("BusID")
    public String busID;
    @SerializedName("Amount")
    public String amount;
    @SerializedName("Recharge_Date")
    public String rechargeDate;
    @SerializedName("Recharge_Time")
    public String rechargeTime;
    @SerializedName("CardReaderID")
    public String cardReaderID;
}
