package com.muflar_driver.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Prince on 8/27/2018.
 */

public class PaymentItem {
    @SerializedName("id")
    public String id;
    @SerializedName("busId")
    public String busId;
    @SerializedName("AFCAmount")
    public String AFCAmount;
    @SerializedName("MFCAmount")
    public String MFCAmount;
    @SerializedName("MFC-CAmount")
    public String MFC_CAmount;
    @SerializedName("MuflarCommission")
    public String MuflarCommission;
    @SerializedName("TotalAmount")
    public String TotalAmount;
    @SerializedName("RideAmount")
    public String RideAmount;
    @SerializedName("TopUpAmount")
    public String TopUpAmount;
    @SerializedName("DateStart")
    public String DateStart;
    @SerializedName("DateEnd")
    public String DateEnd;
    @SerializedName("Week")
    public String Week;
    @SerializedName("year")
    public String year;
    @SerializedName("transactionStatus")
    public String transactionStatus;
}
