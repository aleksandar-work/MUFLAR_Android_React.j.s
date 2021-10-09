package com.muflar_driver.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Prince on 8/6/2018.
 */

public class User {
    @SerializedName("id")
    public Number id;
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
    @SerializedName("Bus_ID")
    public String bus_ID;
    @SerializedName("isEVP")
    public String isEVP;
    @SerializedName("isOTP")
    public String isOTP;
    @SerializedName("Password")
    public String password;
    @SerializedName("Created_Date")
    public String createdDate;
    @SerializedName("Updated_Date")
    public String updatedDate;
    @SerializedName("FirstNameAC")
    public String firstNameAC;
    @SerializedName("LastNameAC")
    public String lastNameAC;
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
