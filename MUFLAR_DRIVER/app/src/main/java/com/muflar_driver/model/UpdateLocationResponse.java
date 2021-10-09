package com.muflar_driver.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Prince on 8/6/2018.
 */

public class UpdateLocationResponse {
    @SerializedName("fieldCount")
    public Number fieldCount;
    @SerializedName("affectedRows")
    public Number affectedRows;
    @SerializedName("insertId")
    public Number insertId;
    @SerializedName("serverStatus")
    public Number serverStatus;
    @SerializedName("warningCount")
    public Number warningCount;
    @SerializedName("message")
    public String message;
    @SerializedName("protocol41")
    public Boolean protocol41;
    @SerializedName("changedRows")
    public Number changedRows;
}
