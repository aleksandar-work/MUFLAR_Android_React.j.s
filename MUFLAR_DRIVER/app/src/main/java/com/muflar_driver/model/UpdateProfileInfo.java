package com.muflar_driver.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Prince on 8/6/2018.
 */

public class UpdateProfileInfo {
    @SerializedName("id")
    public Number id;
    @SerializedName("Bus_ID")
    public String bus_ID;
    @SerializedName("FirstName")
    public String firstName;
    @SerializedName("LastName")
    public String lastName;
    @SerializedName("Email")
    public String email;
    @SerializedName("Mobile")
    public String mobile;
    @SerializedName("City")
    public String city;
    @SerializedName("State")
    public String state;
    @SerializedName("Pincode")
    public String pincode;
    @SerializedName("Age")
    public Number age;
    @SerializedName("Updated_Date")
    public String updatedDate;
    @SerializedName("Account_Number")
    public String accountNumber;
    @SerializedName("IFSC")
    public String IFSC;
    @SerializedName("AddressLine1")
    public String addressLine1;
    @SerializedName("AddressLine2")
    public String addressLine2;
    @SerializedName("AddressLine3")
    public String addressLine3;
}
