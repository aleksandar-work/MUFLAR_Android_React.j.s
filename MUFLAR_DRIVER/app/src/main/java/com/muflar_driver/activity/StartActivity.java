package com.muflar_driver.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import java.util.List;
import com.muflar_driver.R;
import com.muflar_driver.fragment.LoginFragment;
import com.muflar_driver.helper.AlarmManagerBroadcastReceiver;
import com.muflar_driver.helper.StatusBarUtil;
import com.muflar_driver.http.APIClient;
import com.muflar_driver.http.APIInterface;
import com.muflar_driver.model.LoginResponse;
import com.muflar_driver.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prince on 8/14/2018.
 */

public class StartActivity extends AppCompatActivity {
    Fragment frag_login;
    ProgressBar pbar;
    View button_login, button_label,button_icon,ic_menu1,ic_menu2;
    private DisplayMetrics dm;

    boolean     isLogin = false;

    APIInterface apiInterface;

    String SHARED_PREFERENCES_Muflar = "Muflar";
    SharedPreferences sharedPref; // sharedpreference var
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        pbar= findViewById(R.id.mainProgressBar1);
        button_icon=findViewById(R.id.button_icon);
        button_label=findViewById(R.id.button_label);

        dm=getResources().getDisplayMetrics();
        button_login=findViewById(R.id.button_login);
        button_login.setTag(0);
        pbar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        StatusBarUtil.immersive(this);

        frag_login=new LoginFragment();

        apiInterface = APIClient.getClient().create(APIInterface.class);

        sharedPref =  getApplicationContext().getSharedPreferences(SHARED_PREFERENCES_Muflar, Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, frag_login).commit();

        button_login.animate().translationX(dm.widthPixels+button_login.getMeasuredWidth()).setDuration(0).setStartDelay(0).start();
        button_login.animate().translationX(0).setStartDelay(6500).setDuration(1500).setInterpolator(new OvershootInterpolator()).start();

        button_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View p1) {
                loginWithBusId();
//                final ValueAnimator va = new ValueAnimator();
//                va.setDuration(1500);
//                va.setInterpolator(new DecelerateInterpolator());
//                va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator p1) {
//                        RelativeLayout.LayoutParams button_login_lp= (RelativeLayout.LayoutParams) button_login.getLayoutParams();
//                        button_login_lp.width=Math.round((float)p1.getAnimatedValue());
//                        button_login.setLayoutParams(button_login_lp);
//                    }
//                });
//
//                reverseStretchLoginButtonAnimationPart(va);// previous animation
//                successAnimationPart();

            }
        });
    }



    @Override
    public void finish() {
        super.finish();

        if(isLogin)
            return;

        overridePendingTransitionExit();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);

        if(isLogin)
            return;

        overridePendingTransitionEnter();
    }


    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        frag_login.onActivityResult(requestCode, resultCode, data);

    }

    private void loginWithBusId(){

        final EditText etBusId = (EditText) frag_login.getView().findViewById(R.id.et_bus_id);
        EditText etPassword = (EditText) frag_login.getView().findViewById(R.id.et_password);

        String busId = etBusId.getText().toString();
        String password = etPassword.getText().toString();

        if (busId.equals("")) {
            Toast.makeText(this, "Please enter Card Number!", Toast.LENGTH_LONG).show();
            return;
        }
        if (password.equals("")) {
            Toast.makeText(this, "Please enter your Password!", Toast.LENGTH_LONG).show();
            return;
        }

        final ValueAnimator va = new ValueAnimator();
        va.setDuration(1500);
        va.setInterpolator(new DecelerateInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @Override
            public void onAnimationUpdate(ValueAnimator p1) {
                RelativeLayout.LayoutParams button_login_lp= (RelativeLayout.LayoutParams) button_login.getLayoutParams();
                button_login_lp.width=Math.round((float)p1.getAnimatedValue());
                button_login.setLayoutParams(button_login_lp);
            }
        });

        reverseStretchLoginButtonAnimationPart(va);// previous animation

        Call<LoginResponse> call = apiInterface.loginWithBusId(busId, password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse body = response.body();
                if (body == null) {
                    if (response.code() == 404) {
                        int errorCode = 404;
                        stretchLoginButtonAnimationPart(va, errorCode);
                        return;
                    }
                }
                Gson gson = new Gson();
                String json = gson.toJson(body);
                editor.putString("user", json);
                editor.commit();

                successAnimationPart();  // after animation
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                int errorCode = 500;
                stretchLoginButtonAnimationPart(va, errorCode);
                return;
            }
        });
    }

    private void successAnimationPart(){
        button_label.animate().setStartDelay(100).setDuration(500).alpha(0).start();
        button_login.animate()
                .setInterpolator(new FastOutSlowInInterpolator())
                .setStartDelay(4000)
                .setDuration(1000)
                .scaleX(30)
                .scaleY(30)
                .setListener(new Animator.AnimatorListener(){
                    @Override
                    public void onAnimationStart(Animator p1) {
                        pbar.animate().setStartDelay(0).setDuration(0).alpha(0).start();
                    }

                    @Override
                    public void onAnimationEnd(Animator p1) {
                        try{
//								getSupportFragmentManager()
//									.beginTransaction()
//									.replace(R.id.frag_container, new EditProfileFragment())
//									.disallowAddToBackStack()
//									.commitAllowingStateLoss();

                            //
                            Toast.makeText(StartActivity.this.getApplicationContext(), "Login is succeessed!", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(StartActivity.this, MainActivity.class);
                            isLogin = true;
                            startActivity(intent);
                            finish();
                            //
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                        button_login.animate().
                                setStartDelay(0).
                                alpha(1).
                                setDuration(1000).
                                scaleX(1).
                                scaleY(1)
                                .x(dm.widthPixels-button_login.getMeasuredWidth()-100)
                                .y(dm.heightPixels-button_login.getMeasuredHeight()-100)
                                .setListener(new Animator.AnimatorListener(){

                                    @Override
                                    public void onAnimationStart(Animator p1) {
                                        // TODO: Implement this method
                                    }

                                    @Override
                                    public void onAnimationEnd(Animator p1) {
                                        button_icon.animate()
                                                .setDuration(0)
                                                .setStartDelay(0)
                                                .rotation(85)
                                                .alpha(1)
                                                .start();

                                        button_icon.animate()
                                                .setDuration(2000)
                                                .setInterpolator(new BounceInterpolator())
                                                .setStartDelay(0)
                                                .rotation(0)
                                                .start();
                                        button_login.setTag(2);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator p1) {
                                        // TODO: Implement this method
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator p1) {
                                        // TODO: Implement this method
                                    }
                                }).start();
                    }

                    @Override
                    public void onAnimationCancel(Animator p1) {
                        // TODO: Implement this method
                    }

                    @Override
                    public void onAnimationRepeat(Animator p1) {
                        // TODO: Implement this method
                    }
                }).start();
    }

    private void reverseStretchLoginButtonAnimationPart(ValueAnimator va){

//        if((int)button_login.getTag()==1){
//            return;
//        }else if((int)button_login.getTag()==2){
//            button_login.animate().x(dm.widthPixels/2).y(dm.heightPixels/2).setInterpolator(new EasingInterpolator(Ease.CUBIC_IN)).setListener(null).setDuration(1000).setStartDelay(0).start();
//            button_login.animate().setStartDelay(600).setDuration(1000).scaleX(40).scaleY(40).setInterpolator(new EasingInterpolator(Ease.CUBIC_IN_OUT)).start();
//            button_icon.animate().alpha(0).rotation(90).setStartDelay(0).setDuration(800).start();
//            return;
//        }
//        button_login.setTag(1);

        va.setFloatValues(button_login.getMeasuredWidth(), button_login.getMeasuredHeight());
        va.start();
        pbar.animate().setStartDelay(300).setDuration(1000).alpha(1).start();
    }

    private void stretchLoginButtonAnimationPart(final ValueAnimator va, final int errorCode){
        button_label.animate().setStartDelay(100).setDuration(500).alpha(0).start();
        button_login.animate()
                .setInterpolator(new ReverseInterpolator())
                .setStartDelay(4000)
                .setDuration(1000)
                .setListener(new Animator.AnimatorListener(){
                    @Override
                    public void onAnimationStart(Animator p1) {
                        pbar.animate().setStartDelay(0).setDuration(0).alpha(0).start();
                    }

                    @Override
                    public void onAnimationEnd(Animator p1) {
                        button_label.animate().setStartDelay(0).setDuration(0).alpha(1).start();

                        va.setInterpolator(new ReverseInterpolator());
                        va.start();

                        switch(errorCode) {
                            case 404: {
                                new MaterialDialog.Builder(StartActivity.this)
                                        .title("Error")
                                        .content("Connection Failed.")
                                        .positiveText("Ok")
                                        .show();
                                return;
                            }
                            case 500: {
                                Toast.makeText(StartActivity.this, "Your Bus ID or Password is not correct!", Toast.LENGTH_LONG).show();
                                return;
                            }
                            default:
                                return;
                        }


                    }

                    @Override
                    public void onAnimationCancel(Animator p1) {
                        // TODO: Implement this method
                    }

                    @Override
                    public void onAnimationRepeat(Animator p1) {
                        // TODO: Implement this method
                    }
                }).start();
    }

    public class ReverseInterpolator implements Interpolator {
        @Override
        public float getInterpolation(float paramFloat) {
            return Math.round(paramFloat -1f);
        }
    }
}
