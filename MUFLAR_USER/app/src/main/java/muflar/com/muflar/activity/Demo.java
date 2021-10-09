package muflar.com.muflar.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import muflar.com.muflar.R;

public class Demo   extends AppCompatActivity implements  Animation.AnimationListener   {


    LinearLayout  linearLayout_login,linearLayout_password,linearLayout_signup,linearLayout_signup_password,linearLayout_signup_card,linearLayout_otp,linearLayout_intro;



    //======================================login activity============================================================================

    Button btn_login,btn_signup;
    Animation slideLeft, slideRight, slideTop,slideleftout,slideleftin,slide_up,slideleftin1,slideleftin2,slideleftin3,slideleftin4,slideleftin5,slidebottomup;
    ImageView imageView_login;
    LinearLayout linearLayout_buttons, linearLayout_somedia_buttons;
    EditText editText_phoneno;

    //==============================================signup activity============================================================
    Button btn_signup_layout;
    ImageView imageView_signup;
    EditText editText_signup_phone;
    Intent slideactivity;
    Bundle bndlanimation;
    //================================================OTP======================================================
    Button btn_verify;
    TextView textView_otp;

    ImageView imageView_otp;
    LinearLayout linearLayout_otp_number;


    //=============================================signup card activity==================================================

    Button btn_signupcard;
    EditText editText_signup_card;
    ImageView imageView_logo;





    //===========================================loginpassword activity=============================================================
    Button btn_loginnext;

    ImageView imageView_loginpassword;
    EditText editText_loginpasss_phoneno;
    TextView forgetpassword;




//=============================================signup password activity======================================

    Button signup_passnext;
    ImageView imageView_password;
    EditText editText_pass,editText_conf_pass;


    //=============================================================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        ActionBar actionBar=getSupportActionBar();
//        actionBar.hide();

        setContentView(R.layout.activity_demo);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION
        };

        if(!hasPermissions(Demo.this, PERMISSIONS)){
            ActivityCompat.requestPermissions((Activity) Demo.this, PERMISSIONS, PERMISSION_ALL);
        }




        slidebottomup= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slidefrombottom);
        slidebottomup.setAnimationListener(this);



        slideRight = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.right_in);
        slideRight.setAnimationListener(this);


        slideleftin=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_in);
        slideleftin.setAnimationListener(this);



        slideleftin1=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_in1);
        slideleftin1.setAnimationListener(this);


        slideleftin2=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_in2);
        slideleftin2.setAnimationListener(this);


        slideleftin3=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_in3);
        slideleftin3.setAnimationListener(this);


        slideleftin4=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_in4);
        slideleftin4.setAnimationListener(this);


        slideleftin5=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_in5);
        slideleftin5.setAnimationListener(this);



        slideleftout=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.left_out);
        slideleftout.setAnimationListener(this);


        slide_up=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up);
        slide_up.setAnimationListener(this);



        //=====================================login screen id's==============================================


        linearLayout_login=(LinearLayout)findViewById(R.id.login);
        linearLayout_buttons=(LinearLayout)findViewById(R.id.login_buttons);
        linearLayout_somedia_buttons=(LinearLayout)findViewById(R.id.login_somedia_buttons);
        imageView_login=(ImageView)findViewById(R.id.login_logo);
        editText_phoneno=(EditText)findViewById(R.id.login_phoneno);
        btn_login=(Button)findViewById(R.id.btn_login);
        btn_signup=(Button)findViewById(R.id.btn_signup);




//=====================================login_password screen id's==============================================

        linearLayout_password=(LinearLayout)findViewById(R.id.login_password);
        btn_loginnext=(Button)findViewById(R.id.btn_loginnext);
        imageView_loginpassword=(ImageView)findViewById(R.id.password_image);
        editText_loginpasss_phoneno=(EditText)findViewById(R.id.password);
        forgetpassword=(TextView) findViewById(R.id.forgetpassword);


//=====================================signup screen id's==============================================




        linearLayout_signup=(LinearLayout)findViewById(R.id.signup);
        btn_signup_layout=(Button)findViewById(R.id.btn_signupotp);
        imageView_signup=(ImageView)findViewById(R.id.sign_image);
        editText_signup_phone=(EditText)findViewById(R.id.sign_phoneno_);



        //=====================================signup_password screen id's==============================================

        linearLayout_signup_password=(LinearLayout)findViewById(R.id.signup_password);
        signup_passnext=(Button)findViewById(R.id.signup_passnext);
        imageView_password=(ImageView)findViewById(R.id.password_logo);
        editText_pass=(EditText) findViewById(R.id.pass_edit);
        editText_conf_pass=(EditText)findViewById(R.id.confrim_pass);


//=====================================signup_card screen id's==============================================

        linearLayout_signup_card=(LinearLayout)findViewById(R.id.signup_card);
        editText_signup_card=(EditText)findViewById(R.id.card_edit);
        imageView_logo=(ImageView)findViewById(R.id.card_logo);
        btn_signupcard=(Button)findViewById(R.id.btn_signupcard);

//=====================================signup_otp screen id's==============================================

        linearLayout_otp=(LinearLayout)findViewById(R.id.signuup_otp);
        btn_verify=(Button)findViewById(R.id.btn_verify);
        textView_otp=(TextView)findViewById(R.id.otp_resend);
        linearLayout_otp_number=(LinearLayout)findViewById(R.id.otp_layout);
        imageView_otp=(ImageView)findViewById(R.id.otp_logo);


//=====================================login screen id's==============================================



        linearLayout_login.setVisibility(View.VISIBLE);


        imageView_login.setVisibility(View.VISIBLE);
        imageView_login.startAnimation(slidebottomup);


        linearLayout_somedia_buttons.setVisibility(View.VISIBLE);
        linearLayout_somedia_buttons.startAnimation(slideleftin4);


        linearLayout_buttons.setVisibility(View.VISIBLE);
        linearLayout_buttons.startAnimation(slideleftin3);


        editText_phoneno.setVisibility(View.VISIBLE);
        editText_phoneno.startAnimation(slideleftin2);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                linearLayout_login.setVisibility(View.GONE);

                linearLayout_password.setVisibility(View.VISIBLE);

                editText_loginpasss_phoneno.setVisibility(View.VISIBLE);
                editText_loginpasss_phoneno.startAnimation(slideleftin2);


                forgetpassword.setVisibility(View.VISIBLE);
                forgetpassword.startAnimation(slideleftin3);


                btn_loginnext.setVisibility(View.VISIBLE);
                btn_loginnext.startAnimation(slideleftin4);


                btn_loginnext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        presentActivity(v);
                    }
                });

            }
        });



        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                linearLayout_login.setVisibility(View.GONE);
                linearLayout_signup.setVisibility(View.VISIBLE);


                editText_signup_phone.setVisibility(View.VISIBLE);
                editText_signup_phone.startAnimation(slideleftin2);


                btn_signup_layout.setVisibility(View.VISIBLE);
                btn_signup_layout.startAnimation(slideleftin3);


                btn_signup_layout.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {


                        linearLayout_login.setVisibility(View.GONE);
                        linearLayout_signup.setVisibility(View.GONE);


                        linearLayout_otp.setVisibility(View.VISIBLE);

                        linearLayout_otp_number.setVisibility(View.VISIBLE);
                        linearLayout_otp_number.startAnimation(slideleftin2);


                        textView_otp.setVisibility(View.VISIBLE);
                        textView_otp.startAnimation(slideleftin3);


                        btn_verify.setVisibility(View.VISIBLE);
                        btn_verify.startAnimation(slideleftin4);


                        btn_verify.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {


                                linearLayout_login.setVisibility(View.GONE);
                                linearLayout_signup.setVisibility(View.GONE);
                                linearLayout_otp.setVisibility(View.GONE);


                                linearLayout_signup_card.setVisibility(View.VISIBLE);

                                editText_signup_card.setVisibility(View.VISIBLE);
                                editText_signup_card.startAnimation(slideleftin2);


                                btn_signupcard.setVisibility(View.VISIBLE);
                                btn_signupcard.startAnimation(slideleftin3);



                                btn_signupcard.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {


                                        linearLayout_login.setVisibility(View.GONE);
                                        linearLayout_signup.setVisibility(View.GONE);
                                        linearLayout_otp.setVisibility(View.GONE);
                                        linearLayout_signup_card.setVisibility(View.GONE);

                                        linearLayout_signup_password.setVisibility(View.VISIBLE);




                                        editText_pass.setVisibility(View.VISIBLE);
                                        editText_pass.startAnimation(slideleftin2);


                                        editText_conf_pass.setVisibility(View.VISIBLE);
                                        editText_conf_pass.startAnimation(slideleftin3);


                                        signup_passnext.setVisibility(View.VISIBLE);
                                        signup_passnext.startAnimation(slideleftin3);


                                        signup_passnext.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                presentActivity(view); //finish();
                                            }
                                        });
                                    }
                                });

                            }
                        });

                    }
                });



                //finish();
            }
        });



    }
    public static boolean hasPermissions(Context context, String... permissions) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void presentActivity(View view) {

        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, view, "transition");
        int centerY = (int) (view.getX() + view.getWidth() / 2);
        int centerX = (int) (view.getY() + view.getHeight() / 2);
        Intent intent = new Intent(Demo.this, MainActivity.class);

        intent.putExtra(MainActivity.EXTRA_CIRCULAR_REVEAL_X, centerX);
        intent.putExtra(MainActivity.EXTRA_CIRCULAR_REVEAL_Y, centerY);

        ActivityCompat.startActivity(this, intent, options.toBundle());
        finish();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}