package muflar.com.muflar.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Prince on 8/6/2018.
 */

public class UpdateProfileInfo {
//    @SerializedName("id")
//    public Number id;
    @SerializedName("UserID")
    public String userID;
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
    public Number pincode;
    @SerializedName("Updated_Date")
    public String updatedDate;
    @SerializedName("CardNumber")
    public String cardNumber;
    @SerializedName("Age")
    public String age;
    @SerializedName("Gender")
    public String gender;
    @SerializedName("MaritalStatus")
    public String maritalStatus;
    @SerializedName("Profession")
    public String profession;
    @SerializedName("Name")
    public String name_;
    @SerializedName("FirstNameAC")
    public String firstNameAC;
    @SerializedName("LastNameAC")
    public String lastNameAC;
    @SerializedName("Account_Number")
    public Number accountNumber;
    @SerializedName("IFSC")
    public Number IFSC;
    @SerializedName("AddressLine1")
    public String addressLine1;
    @SerializedName("AddressLine2")
    public String addressLine2;
    @SerializedName("AddressLine3")
    public String addressLine3;
}
