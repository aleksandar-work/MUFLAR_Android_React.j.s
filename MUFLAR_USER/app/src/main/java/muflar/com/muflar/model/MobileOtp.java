package muflar.com.muflar.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Prince on 8/6/2018.
 */

public class MobileOtp {
    @SerializedName("Mobile")
    public String Mobile;
    @SerializedName("isOTP")
    public Number isOTP;
}
