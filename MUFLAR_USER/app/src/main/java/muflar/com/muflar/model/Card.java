package muflar.com.muflar.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Prince on 8/5/2018.
 */

public class Card {
    @SerializedName("id")
    public String id;
    @SerializedName("UserID")
    public String userID;
    @SerializedName("Owner")
    public String owner;
    @SerializedName("Isblock")
    public String isblock;
    @SerializedName("ValidFrom")
    public String validFrom;
    @SerializedName("ValidTo")
    public String validTo;
    @SerializedName("Balance")
    public String balance;
    @SerializedName("CardNumber")
    public String cardNumber;
}
