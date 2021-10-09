package com.muflar_driver.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.muflar_driver.R;


/**
 * Created by CISS31 on 4/3/2018.
 */

public class Splash_Layout extends AppCompatActivity implements AnimationListener{
    Animation animFadein;
    ImageView imageView_logo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        // load the animation
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        imageView_logo=(ImageView)findViewById(R.id.image_logo);
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);

        // set animation listener
        animFadein.setAnimationListener(this);

        imageView_logo.startAnimation(animFadein);

        Thread back = new Thread() {
            public void run() {
                try {
                    sleep(1 * 1000);

                        Intent intent =new Intent(Splash_Layout.this,Demo_Activity.class);
                        startActivity(intent);
                        openActivityFromRight();
                        finish();

                } catch (Exception ex) {
                    // TODO: handle exception
                }

            }
        };
        back.start();

    }
    protected void openActivityFromRight() {
        //overridePendingTransition(R.anim.slide_left_out, R.anim.slide_left_in);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }
    @Override
    public void onAnimationEnd(Animation animation) {
        // Take any action after completing the animation

        // check for fade in animation
        if (animation == animFadein) {

           /* Toast.makeText(getApplicationContext(), "Animation Stopped",
                    Toast.LENGTH_SHORT).show();*/
        }

    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub

    }
}
