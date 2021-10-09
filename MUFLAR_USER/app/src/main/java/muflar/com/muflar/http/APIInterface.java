package muflar.com.muflar.http;

import org.json.JSONObject;

import java.util.List;

import muflar.com.muflar.model.Bus;
import muflar.com.muflar.model.BusPositionResponse;
import muflar.com.muflar.model.BusRoute;
import muflar.com.muflar.model.BusStop;
import muflar.com.muflar.model.ETAInfo;
import muflar.com.muflar.model.ETAResponse;
import muflar.com.muflar.model.Mobile;
import muflar.com.muflar.model.MobileOtp;
import muflar.com.muflar.model.Ride;
import muflar.com.muflar.model.RouteFare;
import muflar.com.muflar.model.SocialSignupInfo;
import muflar.com.muflar.model.Topup;
import muflar.com.muflar.model.UpdateProfileInfo;
import muflar.com.muflar.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Prince on 8/5/2018.
 */

public interface APIInterface {

    @Headers("Content-Type: application/json")
    @POST("/users/createcode")
    Call<Mobile> createCodeWithPN(@Body Mobile body);

    @Headers("Content-Type: application/json")
    @POST("/users/verifycode")
    Call<User> verifyCodeWithPN(@Body MobileOtp body);

    @Headers("Content-Type: application/json")
    @PUT("/users/signup/{Mobile}")
    Call<User> singupWithUserInfo(@Path("Mobile") String phonenumber, @Body User body);

    @Headers("Content-Type: application/json")
    @PUT("/users/{id}")
    Call<User> updateUserProfile(@Path("id") String id, @Body UpdateProfileInfo body);

    @Headers("Content-Type: application/json")
    @GET("/users/signin/{cardnumber}/{password}")
    Call<User> loginWithCardno(@Path("cardnumber") String cardno, @Path("password") String password);

    @Headers("Content-Type: application/json")
    @POST("/users/signupBySocial")
    Call<User> singupWithSocialInfo(@Body SocialSignupInfo body);

    @Headers("Content-Type: application/json")
    @GET("/users/{id}")
    Call<User> getUserProfile(@Path("id") String id);

    @GET("/busroute")
    Call<List<BusRoute>> getBusRouteList();

    @GET("/busstops")
    Call<List<BusStop>> getBusStopList();

    @GET("/busstops/{busroute}")
    Call<List<BusStop>> getBusStopListByRoute(@Path("busroute") String busRoute);

    @GET("/topuphistory")
    Call<List<Topup>> getTopupHistory();

    @GET("/rideHistory")
    Call<List<Ride>> getRideHistory();

    @GET("routefare/{from}/{to}/FromTo")
    Call<List<RouteFare>> getRouteFare(@Path("from") String from, @Path("to") String to);

    @GET("/bus")
    Call<List<Bus>> getBusList();

    @GET("/busstops/stops/{stop}")
    Call<List<BusRoute>> getBusRouteListByStop(@Path("stop") String stop);

    @Headers("Content-Type: application/json")
    @POST("/busService/getETA")
    Call<ETAResponse> getETA(@Body ETAInfo body);

    @GET("/busroute/{busId}")
    Call<List<BusPositionResponse>> getBusPosition(@Path("busId") String busId);
}
