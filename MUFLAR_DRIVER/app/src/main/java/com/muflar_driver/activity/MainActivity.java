package com.muflar_driver.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.muflar_driver.R;
import com.muflar_driver.fragment.Adhistory_fragment;
import com.muflar_driver.fragment.EditProfileFragment;
import com.muflar_driver.fragment.Home_Fragment;
import com.muflar_driver.fragment.Paymenthistory_fragment;
import com.muflar_driver.fragment.Ridehistory_fragment;
import com.muflar_driver.fragment.Topuphistory_frament;
import com.muflar_driver.helper.AlarmManagerBroadcastReceiver;
import com.muflar_driver.helper.OnBackPressedListener;
import com.muflar_driver.http.APIClient;
import com.muflar_driver.http.APIInterface;
import com.muflar_driver.model.LoginResponse;
import com.muflar_driver.model.UpdateLocationInfo;
import com.muflar_driver.model.UpdateLocationResponse;
import com.muflar_driver.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static int LOCATION_UPDATE_MIN_TIME = 30000;
    public static int LOCATION_UPDATE_MIN_DISTANCE = 5000;

    APIInterface apiInterface;
    Location mLocation = null;

    String SHARED_PREFERENCES_Muflar = "Muflar";
    SharedPreferences sharedPref; // sharedpreference var
    SharedPreferences.Editor editor;

    FragmentManager fragmentManager;
    public static  Toolbar toolbar;
    ImageView img_drawer;
    //        String[] drawer_item = new String[]{"Feed route","Top-up history","Ride history","Ad history","Payment history","Edit profile"};
    String[] drawer_item = new String[]{"Feed route","Top-up history","Ride history","Payment history","Edit profile"};
    ListView list_drawer;
    DrawerAdapter drawerAdapter;
    Button btnLogout;

    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";
    View rootLayout;

    private int revealX;
    private int revealY;

    private Handler mHandler;
    private LocationManager mLocationManager;

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                mLocationManager.removeUpdates(mLocationListener);
            } else {

            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        sharedPref =  this.getApplicationContext().getSharedPreferences(SHARED_PREFERENCES_Muflar, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        mHandler = new Handler(); // update location periodicly
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread
                getCurrentLocation();

                mHandler.postDelayed(this, 30000);
            }
        };
        mHandler.post(runnableCode);

        btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentManager=getSupportFragmentManager();
        final Intent intent = getIntent();

        rootLayout = findViewById(R.id.rootlayout);

        if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)) {
            rootLayout.setVisibility(View.INVISIBLE);

            revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0);
            revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);

            ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        revealActivity(revealX, revealY);

                        rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    }
                });
            }
        } else {
            rootLayout.setVisibility(View.VISIBLE);

        }

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        img_drawer=(ImageView)findViewById(R.id.drawer);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        img_drawer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                drawer.openDrawer(GravityCompat.START);
            }
        });

        list_drawer = (ListView)findViewById(R.id.list_drawer);

        drawerAdapter = new DrawerAdapter(MainActivity.this,drawer_item);
        list_drawer.setAdapter(drawerAdapter);


        list_drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (drawer_item[i].equals("Feed route"))
                {

                    fragmentManager.beginTransaction().replace(R.id.frame_layout,new Home_Fragment()).commit();
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);


                }

                else if(drawer_item[i].equals("Top-up history"))
                {

                    fragmentManager.beginTransaction().replace(R.id.frame_layout,new Topuphistory_frament()).commit();
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);

                }

                else if(drawer_item[i].equals("Ride history"))
                {

                    fragmentManager.beginTransaction().replace(R.id.frame_layout,new Ridehistory_fragment()).commit();
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);

                }

                else if(drawer_item[i].equals("Ad history"))

                {

                    fragmentManager.beginTransaction().replace(R.id.frame_layout,new Adhistory_fragment()).commit();
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);

                }
                else if(drawer_item[i].equals("Payment history"))

                {

                    fragmentManager.beginTransaction().replace(R.id.frame_layout,new Paymenthistory_fragment()).commit();
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);

                }
                else if(drawer_item[i].equals("Edit profile"))
                {
                    fragmentManager.beginTransaction().replace(R.id.frame_layout,new EditProfileFragment()).commit();
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);

                }

            }
        });

        fragmentManager.beginTransaction().replace(R.id.frame_layout,new Home_Fragment()).commit();

    }
    protected void revealActivity(int x, int y) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            int cx=0;
            int cy=0;

            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight())/* * 1.1*/);

            // create the animator for this view (the start radius is zero)
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, cx, cy, 0, finalRadius);
            circularReveal.setDuration(1000);
            //circularReveal.setInterpolator(new AccelerateInterpolator());

            // make the view visible and start the animation
            rootLayout.setVisibility(View.VISIBLE);
            circularReveal.start();
        } else {

            finish();
        }
    }

    protected void unRevealActivity() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            finish();

        }
        else
        {


            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                    rootLayout, revealX, revealY, finalRadius, 0);

            circularReveal.setDuration(400);
            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    rootLayout.setVisibility(View.INVISIBLE);
                    finish();
                }
            });



            circularReveal.start();

        }
    }


    public class DrawerAdapter extends BaseAdapter {


        Context context;
        String[] drawer_item;


        public DrawerAdapter(Context context,String[] drawer_item)
        {

            this.context = context;
            this.drawer_item = drawer_item;
        }

        @Override
        public int getCount() {
            return drawer_item.length;
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {


            LayoutInflater layoutInflater =  (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view1 = layoutInflater.inflate(R.layout.drawer_item,null);

            TextView textView = (TextView)view1.findViewById(R.id.txt_drawer);

            textView.setText(drawer_item[i]);

            return view1;

        }
    }



    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // super.onBackPressed();
            List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
            if (fragmentList != null) {
                //TODO: Perform your logic to pass back press here
                for (Fragment fragment : fragmentList) {
                    if (fragment instanceof OnBackPressedListener) {
                        ((OnBackPressedListener) fragment).onBackPressed();

                    }
                }
            }
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Feed_route) {

            fragmentManager.beginTransaction().replace(R.id.frame_layout,new Home_Fragment()).commit();
        }
        else if (id == R.id.Top_up_history) {

            fragmentManager.beginTransaction().replace(R.id.frame_layout,new Topuphistory_frament()).commit();
        }
        else if (id == R.id.Ride_history) {

            fragmentManager.beginTransaction().replace(R.id.frame_layout,new Ridehistory_fragment()).commit();
        }
        else if (id == R.id.Ad_history) {

            fragmentManager.beginTransaction().replace(R.id.frame_layout,new Adhistory_fragment()).commit();
        }
        else if (id == R.id.Payment_history) {

            fragmentManager.beginTransaction().replace(R.id.frame_layout,new Paymenthistory_fragment()).commit();
        }
        else if (id == R.id.Edit_profile) {

            fragmentManager.beginTransaction().replace(R.id.frame_layout,new EditProfileFragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getCurrentLocation() {
        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location location = null;
        if (!(isGPSEnabled || isNetworkEnabled)) {

        } else {
            if (isNetworkEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        MainActivity.LOCATION_UPDATE_MIN_TIME, MainActivity.LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                mLocation = location;

                callUpdateLocationAPI();

            }

            if (isGPSEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        MainActivity.LOCATION_UPDATE_MIN_TIME, MainActivity.LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                mLocation = location;

                callUpdateLocationAPI();
            }
        }
        if (location != null) {

        }
    }

    private void callUpdateLocationAPI(){

        Gson gson = new Gson();            // for retrive
        String json = sharedPref.getString("user", "");
        LoginResponse user = gson.fromJson(json, LoginResponse.class);

        UpdateLocationInfo body = new UpdateLocationInfo();
        body.busID = user.bus_ID;
        body.busRoute = user.busRoute;
        body.currentLatitude = mLocation.getLatitude() + "";
        body.CurrentLongitude = mLocation.getLongitude() + "";

        Call<UpdateLocationResponse> call = apiInterface.updateSelfLocation(body);
        call.enqueue(new Callback<UpdateLocationResponse>() {
            @Override
            public void onResponse(Call<UpdateLocationResponse> call, Response<UpdateLocationResponse> response) {
                Toast.makeText(getApplicationContext(), "alarm manager alarm success", Toast.LENGTH_LONG).show();
                UpdateLocationResponse body = response.body();

            }

            @Override
            public void onFailure(Call<UpdateLocationResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "alarm manager alarm failure", Toast.LENGTH_LONG).show();
                int errorCode = 500;
                return;
            }
        });
    }
}
