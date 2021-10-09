package muflar.com.muflar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import muflar.com.muflar.R;
import muflar.com.muflar.fragment.SignupFragment;
import muflar.com.muflar.helper.StatusBarUtil;

/**
 * Created by Olga on 8/18/2018.
 */

public class SignupActivity extends AppCompatActivity {

    FrameLayout     containerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        containerLayout = findViewById(R.id.container_layout);

        StatusBarUtil.immersive(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, new SignupFragment()).commit();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransitionExit();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
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
}
