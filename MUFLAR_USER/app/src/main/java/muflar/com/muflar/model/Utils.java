package muflar.com.muflar.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.google.android.gms.maps.model.MapStyleOptions;

import muflar.com.muflar.R;
import muflar.com.muflar.helper.StatusBarUtil;

/**
 * Created by Prince on 8/15/2018.
 */

public class Utils {
    public static int       MAP_ZOOM_SIZE = 17;

    public static String    MAY_STYLE = "map_style";
    public static String    BUS_ROUTE = "bus_route";
    public static int       MAP_STYLE_NORMAL = 0;
    public static int       MAP_STYLE_BLUE = 1;
    public static int       MAP_STYLE_BLACK = 2;

    public static int LINE_WIDTH = 10;
    public static float MARKER_WIDTH = 0.5f;
    public static float MARKER_HEIGHT = 0.5f;
    public static int BUSSTOP_MARKER_IMAGE_WIDTH = 32;
    public static int BUSSTOP_MARKER_IMAGE_HEIGHT = 32;
    public static int MARKER_IMAGE_WIDTH = 48;
    public static int MARKER_IMAGE_HEIGHT = 48;

    public  static final int USER_PATH = 21;
    public static final int FROMTO_PATH = 22;
    public static final int BUSFROM_PATH = 23;
    public static final int BUSSTOPS_PATH = 24;

    public static MapStyleOptions getMapStyle(Activity activity, int style) {
        MapStyleOptions styleOptions;

        switch (style) {
            case 0:
                styleOptions = MapStyleOptions.loadRawResourceStyle(activity, R.raw.mapstyle_normal);
                break;
            case 1:
                styleOptions = MapStyleOptions.loadRawResourceStyle(activity,R.raw.mapstyle_blue);
                break;
            case 2:
                styleOptions = MapStyleOptions.loadRawResourceStyle(activity,R.raw.mapstyle_black);
                break;
            default:
                styleOptions = MapStyleOptions.loadRawResourceStyle(activity,R.raw.mapstyle_normal);
                break;
        }

        return styleOptions;
    }

    public static String PREF_NAME = "MUFLAR_USER_PREF";

    public static void setIntPref(Context context, String key, int val) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Utils.PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(key, val);
        editor.commit();
    }

    public static int getIntPref(Context context, String key) {
        int val = 0;

        SharedPreferences pref = context.getSharedPreferences(Utils.PREF_NAME, Context.MODE_PRIVATE);
        val = pref.getInt(key, 0);

        return val;
    }

    public static Bitmap resizeMarkerIconImage(int path, Context context){
        int height = Utils.MARKER_IMAGE_HEIGHT;
        int width = Utils.MARKER_IMAGE_WIDTH;

        BitmapDrawable bitmapdraw=(BitmapDrawable)context.getResources().getDrawable(path);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMaker = Bitmap.createScaledBitmap(b, width, height, false);

        return  smallMaker;
    }

    public static Bitmap resizeMarkerIconImageForBusstop(int path, Context context){
        int height = Utils.BUSSTOP_MARKER_IMAGE_HEIGHT;
        int width = Utils.BUSSTOP_MARKER_IMAGE_WIDTH;

        BitmapDrawable bitmapdraw=(BitmapDrawable)context.getResources().getDrawable(path);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMaker = Bitmap.createScaledBitmap(b, width, height, false);

        return  smallMaker;
    }
}
