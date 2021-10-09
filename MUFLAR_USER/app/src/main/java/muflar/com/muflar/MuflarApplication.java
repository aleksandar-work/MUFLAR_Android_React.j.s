package muflar.com.muflar;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import muflar.com.muflar.helper.FontsOverride;

/**
 * Created by Olga on 8/4/2018.
 */

public class MuflarApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
//        FontsOverride.setDefaultFont(this, "DEFAULT", "avenir_light_35.ttf");
//        FontsOverride.setDefaultFont(this, "MONOSPACE", "avenir_light_35.ttf");
//        FontsOverride.setDefaultFont(this, "SERIF", "avenir_light_35.ttf");
//        FontsOverride.setDefaultFont(this, "SANS_SERIF", "avenir_light_35.ttf");
    }
}
