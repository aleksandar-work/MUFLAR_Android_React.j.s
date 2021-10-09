package com.muflar_driver.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import com.muflar_driver.R;
import com.muflar_driver.activity.MainActivity;
import com.muflar_driver.activity.SignupActivity;

/**
 * Created by Prince on 8/14/2018.
 */

public class LoginFragment extends Fragment
{
    String TAG = "LoingFragment";

    View form_login, imglogo, label_signup, darkoverlay, signupOptLayout, forgotPwdLayout;
    ImageView google_login, facebook_login, linkedin_login;
    KenBurnsView kbv;

    String SHARED_PREFERENCES_Muflar = "Muflar";
    SharedPreferences sharedPref; // sharedpreference var
    SharedPreferences.Editor editor;

    private static final String PROFILE = "public_profile";

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


        label_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SignupActivity.class);
                getActivity().startActivity(intent);
            }
        });

         sharedPref =  getActivity().getApplicationContext().getSharedPreferences(SHARED_PREFERENCES_Muflar, Context.MODE_PRIVATE);
         editor = sharedPref.edit();

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        RandomTransitionGenerator generator = new RandomTransitionGenerator(20000, new AccelerateDecelerateInterpolator());
        kbv.setTransitionGenerator(generator);

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

}