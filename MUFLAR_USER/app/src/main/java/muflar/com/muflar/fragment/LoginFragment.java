package muflar.com.muflar.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import muflar.com.muflar.Manifest;
import muflar.com.muflar.R;
import muflar.com.muflar.activity.MainActivity;
import muflar.com.muflar.activity.SignupActivity;
import muflar.com.muflar.http.APIClient;
import muflar.com.muflar.http.APIInterface;
import muflar.com.muflar.model.SocialSignupInfo;
import muflar.com.muflar.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.annotation.ElementType.PACKAGE;
import static muflar.com.muflar.fragment.BusRouteDetailsSecondFragment.REQ_PERMISSION;

/**
 * Created by Prince on 8/14/2018.
 */

public class LoginFragment extends Fragment
{
    String TAG = "LoingFragment";

    View form_login, imglogo, label_signup, darkoverlay, signupOptLayout, forgotPwdLayout;
    ImageView google_login, facebook_login, linkedin_login;
    KenBurnsView kbv;

    APIInterface apiInterface;

    String SHARED_PREFERENCES_Muflar = "Muflar";
    SharedPreferences sharedPref; // sharedpreference var
    SharedPreferences.Editor editor;

    int GOOGLE_SIGN_IN = 11; // google var
    GoogleSignInClient mGoogleSignInClient;

    CallbackManager callbackManager; // facebook var
    private static final String PROFILE = "public_profile";

    String linkedinToken;

    private DisplayMetrics dm; // google location var

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_login, container, false);
        dm=getResources().getDisplayMetrics();
        form_login=v.findViewById(R.id.form_login);
        imglogo=v.findViewById(R.id.fragmentloginLogo);
        kbv=(KenBurnsView) v.findViewById(R.id.fragmentloginKenBurnsView1);
        darkoverlay=v.findViewById(R.id.fragmentloginView1);
        label_signup=v.findViewById(R.id.label_signup);
        signupOptLayout = v.findViewById(R.id.layout_signup_option);
        forgotPwdLayout = v.findViewById(R.id.forgot_pwd_tv);
        google_login = v.findViewById(R.id.mail_btn);
        facebook_login = v.findViewById(R.id.facebook_btn);
        linkedin_login = v.findViewById(R.id.linkedin_btn);

        google_login.setOnClickListener(googleLoginClickListener);
        facebook_login.setOnClickListener(facebookLoginClickListener);
        linkedin_login.setOnClickListener(linkedinLoginClickListener);

        label_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SignupActivity.class);
                getActivity().startActivity(intent);
            }
        });

        apiInterface = APIClient.getClient().create(APIInterface.class);

         sharedPref =  getActivity().getApplicationContext().getSharedPreferences(SHARED_PREFERENCES_Muflar, Context.MODE_PRIVATE);
         editor = sharedPref.edit();

        askPermission();
        googleSigninConfigure();
        configureFacebookLogin();
        getPackageHash();

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        RandomTransitionGenerator generator = new RandomTransitionGenerator(20000, new AccelerateDecelerateInterpolator());
        kbv.setTransitionGenerator(generator);
//        imglogo.animate().setStartDelay(2000).setDuration(2000).alpha(1).start();
//        darkoverlay.animate().setStartDelay(2000).setDuration(2000).alpha(0.0f).start();
//        label_signup.animate().setStartDelay(3000).setDuration(2000).alpha(1).start();
//        signupOptLayout.animate().setStartDelay(3000).setDuration(2000).alpha(1).start();
////        forgotPwdLayout.animate().setStartDelay(6000).setDuration(2000).alpha(1).start();
//        form_login.animate().translationY(dm.heightPixels).setStartDelay(0).setDuration(0).start();
//        form_login.animate().translationY(0).setDuration(1500).alpha(1).setStartDelay(3000).start();

        imglogo.animate().setStartDelay(5000).setDuration(2000).alpha(1).start();
        darkoverlay.animate().setStartDelay(5000).setDuration(2000).alpha(0.0f).start();
        label_signup.animate().setStartDelay(6000).setDuration(2000).alpha(1).start();
        signupOptLayout.animate().setStartDelay(6000).setDuration(2000).alpha(1).start();
//        forgotPwdLayout.animate().setStartDelay(6000).setDuration(2000).alpha(1).start();
        form_login.animate().translationY(dm.heightPixels).setStartDelay(0).setDuration(0).start();
        form_login.animate().translationY(0).setDuration(1500).alpha(1).setStartDelay(6000).start();
    }


    private void startMainActivity(){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    // -------------------------- google signin part ----------------------------------------//
    private void googleSigninConfigure() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

         mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.w(TAG, "googleSignInResult:success account=" + account.getDisplayName().toString());

//            editor.putString("profile_email", account.getEmail());
//            editor.putString("profile_fullname", account.getDisplayName());
//            editor.putString("profile_firstname", account.getGivenName());
//            editor.putString("profile_lastname", account.getFamilyName());
//            editor.putString("profile_token", account.getIdToken());
//            editor.putString("profile_photourl", account.getPhotoUrl().toString());
//            editor.commit();

            SocialSignupInfo socialSignupInfo = new SocialSignupInfo();
            socialSignupInfo.email = account.getEmail();
            socialSignupInfo.firstName = account.getGivenName();
            socialSignupInfo.lastName = account.getFamilyName();
            socialSignupInfo.signUp_Type = "google";
            socialSignupInfo.addressLine1 = "";
            socialSignupInfo.addressLine2 = "";
            socialSignupInfo.addressLine3 = "";
            socialSignupInfo.password = "";
            socialSignupInfo.age = "";
            socialSignupInfo.gender = "";
            socialSignupInfo.accountNumber = 0;
            socialSignupInfo.cardNumber = 0;
            socialSignupInfo.mobile = "";
            socialSignupInfo.city = "";
            socialSignupInfo.state = "";
            socialSignupInfo.fareType = "";
            socialSignupInfo.firstNameAC = "";
            socialSignupInfo.lastNameAC = "";
            socialSignupInfo.IFSC = 0;
            socialSignupInfo.isOTP = "";
            socialSignupInfo.isEVP = "";
            socialSignupInfo.maritalStatus = "";
            socialSignupInfo.profession = "";
            socialSignupInfo.updatedDate = "";
            socialSignupInfo.created_Date = "";
            socialSignupInfo.pincode = 0;

            signupWithSocialInfo(socialSignupInfo);

        } catch (ApiException e) {
            Log.w(TAG, "googleSignInResult:failed code=" + e.getStatusCode());
        }
    }
    // ----------------------------------------------------------------------------------------//

    // --------------------------------  facebook login part ----------------------------------//

    private void configureFacebookLogin(){
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                Toast.makeText(getActivity(), "facebook login success", Toast.LENGTH_LONG).show();
                getProfileWithToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity(), "facebok login cancel", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getActivity(), "facebook login error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getProfileWithToken(final com.facebook.AccessToken token){
        GraphRequest request = GraphRequest.newMeRequest(
                token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {

//                            if(object.has("email")) editor.putString("profile_email", object.getString("email"));
//                            if(object.has("name")) editor.putString("profile_fullname", object.getString("name"));
//                            if(object.has("first_name")) editor.putString("profile_firstname", object.getString("first_name"));
//                            if(object.has("last_name")) editor.putString("profile_lastname", object.getString("last_name"));
//                            if(object.has("gender")) editor.putString("profile_gender", object.getString("gender"));
//                            if(object.has("birthday")) editor.putString("profile_birthday", object.getString("birthday"));
//                            editor.putString("profile_token", String.valueOf(token));
//                            if(object.has("profile_pic")) editor.putString("profile_photourl", object.getString("profile_pic"));
//                            editor.commit();

                            SocialSignupInfo socialSignupInfo = new SocialSignupInfo();
                            socialSignupInfo.email = object.getString("email");
                            socialSignupInfo.firstName = object.getString("first_name");
                            socialSignupInfo.lastName = object.getString("last_name");
                            socialSignupInfo.gender = object.getString("gender");
                            socialSignupInfo.signUp_Type = "facebook";

                            signupWithSocialInfo(socialSignupInfo);

                            //                LoginManager.getInstance().logOut();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, first_name, last_name, email, birthday, gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void facebookLogin(){
        LoginManager.getInstance().logInWithReadPermissions(LoginFragment.this ,Arrays.asList(PROFILE));
    }

    // ----------------------------------------------------------------------------------------//

    // --------------------------------  linkedin login part--------------------------------------//
    private void linkedInLogin(){
        LISessionManager.getInstance(getActivity().getApplicationContext()).init(getActivity(), buildScope(), new AuthListener() {
                    @Override
                    public void onAuthSuccess() {
                        linkedinToken = LISessionManager.getInstance(getActivity().getApplicationContext())
                                .getSession().getAccessToken().toString();

                        getUserProfile();

                    }

                    @Override
                    public void onAuthError(LIAuthError error) {

                        Toast.makeText(getActivity().getApplicationContext(), "failed "
                                        + error.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }, true);
    }

    // set the permission to retrieve basic information of User's linkedIn account
    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    private void getUserProfile(){
//        String host = "api.linkedin.com"; // linkedin var
//        String profileUrl = "https://" + host + "/v1/people/~:" +
//                "(email-address,formatted-name,phone-numbers,public-profile-url,picture-url,picture-urls::(original))";
        String profileUrl = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name)";
        APIHelper apiHelper = APIHelper.getInstance(getActivity().getApplicationContext());
        apiHelper.getRequest(getActivity(), profileUrl, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                setUserProfile(result.getResponseDataAsJson());
            }
            @Override
            public void onApiError(LIApiError error) {
                // ((TextView) findViewById(R.id.error)).setText(error.toString());
            }
        });
    }

    private void setUserProfile(JSONObject response){
        try {
//            editor.putString("profile_email", response.get("emailAddress").toString());
//            editor.putString("profile_fullname", response.get("formattedName").toString());
//            editor.putString("profile_firstname", response.get("first_name").toString());
//            editor.putString("profile_lastname", response.get("last_name").toString());
//            editor.putString("profile_token", linkedinToken);
//            editor.putString("profile_photourl", response.get("pictureUrl").toString());
//            editor.commit();

            SocialSignupInfo socialSignupInfo = new SocialSignupInfo();
            socialSignupInfo.email = response.getString("emailAddress");
            socialSignupInfo.firstName = response.getString("first_name");
            socialSignupInfo.lastName = response.getString("last_name");
            socialSignupInfo.signUp_Type = "linkedin";

            signupWithSocialInfo(socialSignupInfo);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getPackageHash() {
        try {

            @SuppressLint("PackageManagerGetSignatures") PackageInfo info = getActivity().getPackageManager().getPackageInfo(
                    "muflar.com.muflar",//give your package name here
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                Log.v(TAG, "Hash  : " + Base64.encodeToString(md.digest(), Base64.NO_WRAP));//Key hash is printing in Log
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            Log.d(TAG, e.getMessage(), e);
        }

    }

    // ----------------------------------------------------------------------------------------//

    private void signupWithSocialInfo(SocialSignupInfo socialSignupInfo) {

        Call<User> call = apiInterface.singupWithSocialInfo(socialSignupInfo);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User body = response.body();

                if (body == null) {
                    if (response.code() == 404) {
                        new MaterialDialog.Builder(getContext())
                                .title("Error")
                                .content("Connection Failed.")
                                .positiveText("Ok")
                                .show();

                        return;
                    }
                }

                Gson gson = new Gson();
                String json = gson.toJson(body);
                editor.putString("user", json);
                editor.commit();

                Toast.makeText(getActivity().getApplicationContext(), "Your signup is success!", Toast.LENGTH_LONG).show();
                startMainActivity();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getActivity(), "Your signup is failed!", Toast.LENGTH_LONG).show();
                return;
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data); // facebook
        LISessionManager.getInstance(getActivity().getApplicationContext()).onActivityResult(getActivity(), requestCode, resultCode, data); // linkedin
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN) { // google
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        }
    }


    private ImageView.OnClickListener googleLoginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            googleSignIn();
        }
    };

    private ImageView.OnClickListener facebookLoginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            facebookLogin();
        }
    };

    private ImageView.OnClickListener linkedinLoginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            linkedInLogin();
        }
    };

    // Check for permission to access Location
    private boolean checkPermission() {
        Log.d("Home", "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED );
    }

    // Asks for permission
    private void askPermission() {
        Log.d("Home", "askPermission()");
        ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, REQ_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("Home", "onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Home_Fragment.REQ_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted


                } else {
                    // Permission denied

                }
                break;

        }
    }
}