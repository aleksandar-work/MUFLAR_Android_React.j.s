package muflar.com.muflar.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

/**
 * Created by Prince on 8/6/2018.
 */

public class UserDistanceEvent {

    @SerializedName("jsonData")
    public JSONObject jsonData;
    @SerializedName("isFromUserPath")
    public int drawRoutePath;
}
