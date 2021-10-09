package com.muflar_driver.http;

import com.google.android.gms.nearby.messages.internal.Update;
import com.muflar_driver.model.LoginResponse;
import com.muflar_driver.model.Mobile;
import com.muflar_driver.model.MobileOtp;
import com.muflar_driver.model.PaymentItem;
import com.muflar_driver.model.Ride;
import com.muflar_driver.model.SignupInfo;
import com.muflar_driver.model.Topup;
import com.muflar_driver.model.UpdateLocationInfo;
import com.muflar_driver.model.UpdateLocationResponse;
import com.muflar_driver.model.UpdateProfileInfo;
import com.muflar_driver.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Prince on 8/7/2018.
 */

public interface APIInterface {
    @Headers("Content-Type: application/json")
    @POST("/driver/createcode")
    Call<Mobile> createCodeWithPN(@Body Mobile body);

    @Headers("Content-Type: application/json")
    @POST("/driver/verifycode")
    Call<User> verifyCodeWithPN(@Body MobileOtp body);

    @Headers("Content-Type: application/json")
    @PUT("/driver/signup/{Mobile}")
    Call<LoginResponse> singupWithUserInfo(@Path("Mobile") String phonenumber, @Body SignupInfo body);

    @GET("/topuphistory")
    Call<List<Topup>> getTopupHistory();

    @GET("/rideHistory")
    Call<List<Ride>> getRideHistory();

//    http://ec2-54-169-53-70.ap-southeast-1.compute.amazonaws.com:5000/transactionWeekly/byBus/S98765432
    @GET("/transactionWeekly/byBus/{busId}")
    Call<List<PaymentItem>> getPaymentHistory(@Path("busId") String busId);

    @Headers("Content-Type: application/json")
    @GET("/driver/signin/{bus_id}/{password}")
    Call<LoginResponse> loginWithBusId(@Path("bus_id") String busId, @Path("password") String password);

    @Headers("Content-Type: application/json")
    @GET("/driver/{bus_id}")
    Call<LoginResponse> getUserProfile(@Path("bus_id") String busId);

    @Headers("Content-Type: application/json")
    @PUT("/driver/{id}")
    Call<User> updateUserProfile(@Path("id") String busId, @Body UpdateProfileInfo body);

    @Headers("Content-Type: application/json")
    @PUT("/driver/updateLocation")
    Call<UpdateLocationResponse> updateSelfLocation(@Body UpdateLocationInfo body);
}
