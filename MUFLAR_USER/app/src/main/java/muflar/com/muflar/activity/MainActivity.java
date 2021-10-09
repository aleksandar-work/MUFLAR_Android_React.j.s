package muflar.com.muflar.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import muflar.com.muflar.R;
import muflar.com.muflar.fragment.BusRouteDetailsFragment;
import muflar.com.muflar.fragment.BusStopDetailsFragment;
import muflar.com.muflar.fragment.EditProfileFragment;
import muflar.com.muflar.fragment.RideHistoryFragment;
import muflar.com.muflar.fragment.Home_Fragment;
import muflar.com.muflar.fragment.TopUpHistoryFragment;
import muflar.com.muflar.helper.OnBackPressedListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";

    View rootLayout;

    Button btnLogout;

    private int revealX;
    private int revealY;

    FragmentManager fragmentManager;
    public static  Toolbar toolbar;
    ImageView img_drawer;

    String[] drawer_item = new String[]{"Plan ride","Bus route details","Bus stop details","Top-up history","Ride history","Edit profile"};
    ListView list_drawer;
    DrawerAdapter drawerAdapter;

    public static int LOCATION_UPDATE_MIN_TIME = 30000;
    public static int LOCATION_UPDATE_MIN_DISTANCE = 5000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //=============================animation for reveal========================================

        final Intent intent = getIntent();

        rootLayout = findViewById(R.id.rootlayout);


//        if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
//                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) &&
//                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)) {
//            rootLayout.setVisibility(View.INVISIBLE);
//
//            revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0);
//            revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);
//
//
//            ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
//            if (viewTreeObserver.isAlive()) {
//                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        revealActivity(revealX, revealY);
//
//                        rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//
//                    }
//                });
//            }
//        } else {
//            rootLayout.setVisibility(View.VISIBLE);
//
//        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        fragmentManager=getSupportFragmentManager();


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        img_drawer=(ImageView)findViewById(R.id.drawer);
        list_drawer = (ListView)findViewById(R.id.list_drawer);


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



        drawerAdapter = new DrawerAdapter(MainActivity.this,drawer_item);
        list_drawer.setAdapter(drawerAdapter);


        list_drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                if (drawer_item[i].equals("Plan ride"))
                {
                    fragmentManager.beginTransaction().replace(R.id.frame_layout,new Home_Fragment()).commit();
                    openActivityFromRight();
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);

                }

                else if(drawer_item[i].equals("Bus route details"))
                {
                    fragmentManager.beginTransaction().replace(R.id.frame_layout,new BusRouteDetailsFragment()).commit();
                    openActivityFromRight();
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                }

                else if(drawer_item[i].equals("Bus stop details"))

                {
                    fragmentManager.beginTransaction().replace(R.id.frame_layout,new BusStopDetailsFragment()).commit();
                    openActivityFromRight();
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                }

                else if(drawer_item[i].equals("Top-up history"))

                {
                    fragmentManager.beginTransaction().replace(R.id.frame_layout,new TopUpHistoryFragment()).commit();
                    openActivityFromRight();
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                }

                else if(drawer_item[i].equals("Ride history"))

                {

                    fragmentManager.beginTransaction().replace(R.id.frame_layout,new RideHistoryFragment()).commit();
                    openActivityFromRight();
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                }

                else if(drawer_item[i].equals("Edit profile"))

                {

                    fragmentManager.beginTransaction().replace(R.id.frame_layout,new EditProfileFragment()).commit();
                    openActivityFromRight();
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

            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()));

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
        else {

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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        if (id == R.id.plan_ride) {
            fragmentManager.beginTransaction().replace(R.id.frame_layout,new Home_Fragment()).commit();
        } else if (id == R.id.bus_route) {
            fragmentManager.beginTransaction().replace(R.id.frame_layout,new BusRouteDetailsFragment()).commit();
        } else if (id == R.id.bus_stop) {
            fragmentManager.beginTransaction().replace(R.id.frame_layout,new BusStopDetailsFragment()).commit();
        } else if (id == R.id.top_up) {
            fragmentManager.beginTransaction().replace(R.id.frame_layout,new TopUpHistoryFragment()).commit();
        }  else if (id == R.id.ride_history) {
            fragmentManager.beginTransaction().replace(R.id.frame_layout,new RideHistoryFragment()).commit();
        }  else if (id == R.id.edit_profile) {
            fragmentManager.beginTransaction().replace(R.id.frame_layout,new EditProfileFragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    protected void openActivityFromRight() {
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

}
