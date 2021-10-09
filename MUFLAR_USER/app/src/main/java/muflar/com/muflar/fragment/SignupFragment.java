package muflar.com.muflar.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.lamudi.phonefield.PhoneInputLayout;
import com.redmadrobot.inputmask.MaskedTextChangedListener;
import com.redmadrobot.inputmask.PolyMaskTextChangedListener;

import java.util.ArrayList;
import java.util.List;

import muflar.com.muflar.R;
import muflar.com.muflar.activity.MainActivity;
import muflar.com.muflar.http.APIClient;
import muflar.com.muflar.http.APIInterface;
import muflar.com.muflar.model.Mobile;
import muflar.com.muflar.model.MobileOtp;
import muflar.com.muflar.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Olga on 8/16/2018.
 */

public class SignupFragment extends Fragment {
    PercentRelativeLayout rootLayout;
    PercentRelativeLayout putPhoneNoLayout;
    PercentRelativeLayout putOTPLayout;
    PercentRelativeLayout putCardLayout;
    PercentRelativeLayout putPasswordLayout;

    RelativeLayout btnSendOTP;
    RelativeLayout btnVerifyOTP;
    RelativeLayout btnSendCard;
    RelativeLayout btnSendPassword;

    PhoneInputLayout etPhoneNumber;
    com.jkb.vcedittext.VerificationCodeEditText etOTPcode;
    EditText etCardNumber;
    EditText etPassword;
    EditText etConfirmPassword;
    ImageView btnBack;

    List<String> affineFormats;
    MaskedTextChangedListener listener;

    APIInterface apiInterface;

    String phonenumber;
    int isOTP;
    int cardnumber;
    String password;
    String confirmPassword;

    String SHARED_PREFERENCES_Muflar = "Muflar";
    SharedPreferences sharedPref; // sharedpreference var
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signup, container, false);

        rootLayout = v.findViewById(R.id.root_layout);
        putPhoneNoLayout = v.findViewById(R.id.layout_put_phone_number);
        putOTPLayout = v.findViewById(R.id.layout_verify_otp);
        putCardLayout = v.findViewById(R.id.layout_put_card);
        putPasswordLayout = v.findViewById(R.id.layout_password);

        btnSendOTP = v.findViewById(R.id.button_send_opt);
        btnVerifyOTP = v.findViewById(R.id.button_verify);
        btnSendCard = v.findViewById(R.id.button_send_card);
        btnSendPassword = v.findViewById(R.id.button_password);

        etPhoneNumber = v.findViewById(R.id.phone_number_et);
        etOTPcode = v.findViewById(R.id.verify_otp_et);
        etCardNumber = v.findViewById(R.id.card_et);
        etPassword = v.findViewById(R.id.password_et);
        etConfirmPassword = v.findViewById(R.id.confirm_password_et);
        btnBack = v.findViewById(R.id.back_button);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        sharedPref =  getActivity().getApplicationContext().getSharedPreferences(SHARED_PREFERENCES_Muflar, Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        initListener();

//        affineFormats = new ArrayList<>();
//        affineFormats.add("+91 ([000]) [000] [000] [00]");
//
//        listener = new PolyMaskTextChangedListener(
//                "+91 ([000]) [000] [00] [00]",
//                affineFormats,
//                etPhoneNumber,
//                new MaskedTextChangedListener.ValueListener() {
//                    @Override
//                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {
//                    }
//                }
//        );
//
//        etPhoneNumber.addTextChangedListener(listener);
//        etPhoneNumber.setOnFocusChangeListener(listener);
//        etPhoneNumber.setHint(listener.placeholder());

        return v;
    }

    private void initListener() {
        btnSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOptResult();
            }
        });

        btnVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOptResult();
            }
        });

        btnSendCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etCardNumber.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter you Card Number!", Toast.LENGTH_LONG).show();
                    return;
                }

                cardnumber = Integer.parseInt(etCardNumber.getText().toString());

                Animation animToLeft = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_to_left);
                putCardLayout.startAnimation(animToLeft);

                putCardLayout.setVisibility(View.GONE);
                putPasswordLayout.setVisibility(View.VISIBLE);

                Animation animFromRight = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_from_right);
                putPasswordLayout.startAnimation(animFromRight);
            }
        });

        btnSendPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = etPassword.getText().toString();
                confirmPassword = etConfirmPassword.getText().toString();

                if (password.equals("")) {
                    Toast.makeText(getActivity(), "Please enter your password", Toast.LENGTH_LONG).show();
                    return;
                }
                if (confirmPassword.equals("")) {
                    Toast.makeText(getActivity(), "Please enter your confirm password", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    new MaterialDialog.Builder(getContext())
                            .title("")
                            .content("Please entry Confirm Password again!")
                            .positiveText("Ok")
                            .show();

                    return;
                } else{
                    signupWithUserInfo();
                }

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (putPhoneNoLayout.getVisibility() == View.VISIBLE) {
                    getActivity().finish();
                } else if (putOTPLayout.getVisibility() == View.VISIBLE) {
                    Animation animFromRight = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_to_right);
                    putOTPLayout.startAnimation(animFromRight);

                    putPhoneNoLayout.setVisibility(View.VISIBLE);
                    putOTPLayout.setVisibility(View.GONE);

                    Animation animToLeft = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_from_left);
                    putPhoneNoLayout.startAnimation(animToLeft);
                } else if (putCardLayout.getVisibility() == View.VISIBLE) {
                    Animation animFromRight = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_to_right);
                    putCardLayout.startAnimation(animFromRight);


                    putOTPLayout.setVisibility(View.VISIBLE);
                    putCardLayout.setVisibility(View.GONE);

                    Animation animToLeft = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_from_left);
                    putOTPLayout.startAnimation(animToLeft);
                } else if (putPasswordLayout.getVisibility() == View.VISIBLE) {
                    Animation animFromRight = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_to_right);
                    putPasswordLayout.startAnimation(animFromRight);


                    putCardLayout.setVisibility(View.VISIBLE);
                    putPasswordLayout.setVisibility(View.GONE);

                    Animation animToLeft = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_from_left);
                    putCardLayout.startAnimation(animToLeft);
                }
            }
        });
    }

    private void sendOptResult() {

//        String str = etPhoneNumber.getText().toString();

        String str = etPhoneNumber.getPhoneNumber().toString();
        if (str.equals("")) {
            Toast.makeText(getActivity(), "Please enter your phone number", Toast.LENGTH_LONG).show();
            return;
        }
        phonenumber = str;

//        String str1 = str.replace("+", "");
//        String str2 = str1.replace("(", "");
//        String str3 = str2.replace(")", "");
//        phonenumber = str3.replace(" ", "").trim();

        Mobile mobile1 = new Mobile();
        mobile1.mobile = phonenumber;

        Call<Mobile> call = apiInterface.createCodeWithPN(mobile1);
        call.enqueue(new Callback<Mobile>() {
            @Override
            public void onResponse(Call<Mobile> call, Response<Mobile> response) {
                Mobile mobile = response.body();

                if (mobile == null) {
                    if (response.code() == 404) {
                        new MaterialDialog.Builder(getContext())
                                .title("Error")
                                .content("Connection Failed.")
                                .positiveText("Ok")
                                .show();

                        return;
                    }
                }

                Animation animToLeft = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_to_left);
                putPhoneNoLayout.startAnimation(animToLeft);

                putPhoneNoLayout.setVisibility(View.GONE);
                putOTPLayout.setVisibility(View.VISIBLE);

                Animation animFromRight = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_from_right);
                putOTPLayout.startAnimation(animFromRight);
            }

            @Override
            public void onFailure(Call<Mobile> call, Throwable t) {

                Toast.makeText(getActivity(), "Your phone is already registered!", Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    private void verifyOptResult() {
        String otpCode = etOTPcode.getText().toString();

        if (otpCode.equals("")) {
            Toast.makeText(getActivity(), "Please enter OTP code!", Toast.LENGTH_LONG).show();
            return;
        } else if (otpCode.length() < 6) {
            Toast.makeText(getActivity(), "Please fill OTP code!", Toast.LENGTH_LONG).show();
            return;
        }
        MobileOtp mobileOtp = new MobileOtp();
        mobileOtp.Mobile = phonenumber;
        isOTP = Integer.parseInt(otpCode);
        mobileOtp.isOTP = isOTP;


        Call<User> call = apiInterface.verifyCodeWithPN(mobileOtp);
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

                Animation animToLeft = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_to_left);
                putOTPLayout.startAnimation(animToLeft);

                putOTPLayout.setVisibility(View.GONE);
                putCardLayout.setVisibility(View.VISIBLE);

                Animation animFromRight = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_from_right);
                putCardLayout.startAnimation(animFromRight);

                Toast.makeText(getActivity(), "Your code is correct!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                Toast.makeText(getActivity(), "Your code is not correct!", Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    private void signupWithUserInfo() {

        User user = new User();
        user.cardNumber = cardnumber;
        user.password = password;
        user.isOTP = isOTP;

        Call<User> call = apiInterface.singupWithUserInfo(phonenumber, user);
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

    private void startMainActivity(){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

}
