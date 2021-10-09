package muflar.com.muflar.http;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Prince on 8/5/2018.
 */

public class APIClient {
    private static Retrofit retrofit = null;
    //    public static String BASE_URL = "http://ec2-54-169-53-70.ap-southeast-1.compute.amazonaws.com:5000"; // real old server
//        public static String BASE_URL = "http://54.179.159.226:5000"; // real new server
        public static String BASE_URL = "http://10.0.2.2:5000"; // local server for memu/default
//    public static String BASE_URL = "http://192.168.1.9:5000"; // local server for real device of Debbirla

    public static Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }
}
