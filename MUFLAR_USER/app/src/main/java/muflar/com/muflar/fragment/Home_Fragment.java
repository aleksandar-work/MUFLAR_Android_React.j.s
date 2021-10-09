package muflar.com.muflar.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;
import java.util.List;

import muflar.com.muflar.Manifest;
import muflar.com.muflar.R;
import muflar.com.muflar.activity.MainActivity;
import muflar.com.muflar.http.APIClient;
import muflar.com.muflar.http.APIInterface;
import muflar.com.muflar.model.Bus;
import muflar.com.muflar.model.BusRoute;
import muflar.com.muflar.model.BusStop;
import muflar.com.muflar.model.Utils;
import muflar.com.muflar.model.RouteFare;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by CISS31 on 4/4/2018.
 */

public class Home_Fragment extends Fragment implements OnMapReadyCallback{
    MapView mMapView;
    String currancy,service,bank,add,state;
    Button button_search;
    EditText etFrom, etTo;
    FragmentManager fragmentManager;

    APIInterface apiInterface;
    List<RouteFare> routeFareList;
    pl.droidsonroids.gif.GifImageView loadingDlg;

    Button button_changeMapStyle;

    public static List<BusStop> busStopList;
    public static List<Bus>     busList;
    public static List<BusRoute>    busRouteList;

    private GoogleMap mGoogleMap;
    public static final int         REQ_PERMISSION = 100;
    private String                  LOG_TAG = "Home";

    PowerMenu mapStyleMenu;
    ArrayList<PowerMenuItem> styleList = new ArrayList<>();

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                drawMarker(location);
                mLocationManager.removeUpdates(mLocationListener);
            } else {
                Log.d("Home", "Location data is null.");
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

    private LocationManager mLocationManager;

    @SuppressLint("MissingPermission")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.home_layout,container,false);
        fragmentManager=getActivity().getSupportFragmentManager();
//        MainActivity.toolbar.setVisibility(View.GONE);

        mMapView = (MapView)view. findViewById(R.id.map);
        button_search=(Button)view.findViewById(R.id.btn_search);
        etFrom = (EditText)view.findViewById(R.id.et_from);
        etTo = (EditText)view.findViewById(R.id.et_to);
        loadingDlg = view.findViewById(R.id.loading_view);
        button_changeMapStyle = view.findViewById(R.id.button_change_map_style);

        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from = etFrom.getEditableText().toString();
                String to = etTo.getEditableText().toString();

                if(from == null || from.length() == 0) {
                    new MaterialDialog.Builder(getContext())
                            .title("Warning")
                            .content("Please input 'From' field.")
                            .positiveText("Ok")
                            .show();

                    return;
                }

                if(to == null || to.length() == 0) {
                    new MaterialDialog.Builder(getContext())
                            .title("Warning")
                            .content("Please input 'To' field.")
                            .positiveText("Ok")
                            .show();

                    return;
                }

                search(from, to);
            }
        });

        //
        initMapStyleMenu();
        //

        if(busStopList == null)
            loadBusStop();

        return view;
    }

    private void initMapStyleMenu() {
        styleList.clear();
        styleList.add(new PowerMenuItem("Normal", false));
        styleList.add(new PowerMenuItem("Blue", false));
        styleList.add(new PowerMenuItem("Black", false));

        OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
            @Override
            public void onItemClick(int position, PowerMenuItem item) {
                changeMapStyle(position);
                mapStyleMenu.setSelectedPosition(position); // change selected item
                Utils.setIntPref(getActivity(), Utils.MAY_STYLE, position);
                mapStyleMenu.dismiss();
            }
        };

        int mapStyle = Utils.getIntPref(getActivity(), Utils.MAY_STYLE);

        mapStyleMenu = new PowerMenu.Builder(getActivity())
                .addItemList(styleList)
                .setAnimation(MenuAnimation.ELASTIC_CENTER) // Animation start point (TOP | LEFT)
                .setMenuRadius(10f)
                .setMenuShadow(10f)
                .setTextColor(getActivity().getResources().getColor(android.R.color.black))
                .setSelectedTextColor(Color.WHITE)
                .setMenuColor(Color.WHITE)
                .setSelectedMenuColor(getActivity().getResources().getColor(R.color.colorPrimary))
                .setOnMenuItemClickListener(onMenuItemClickListener)
                .build();
        mapStyleMenu.setSelectedPosition(mapStyle);

        button_changeMapStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapStyleMenu.showAtCenter(button_changeMapStyle);
            }
        });
    }

    private void changeMapStyle(int style) {
        MapStyleOptions styleOptions = Utils.getMapStyle(getActivity(), style);
        mGoogleMap.setMapStyle(styleOptions);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;

        int mapStyle = Utils.getIntPref(getActivity(), Utils.MAY_STYLE);
        changeMapStyle(mapStyle);

        if(checkPermission())
            mGoogleMap.setMyLocationEnabled(true);
        else
            askPermission();

//        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        initMap();
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    // Check for permission to access Location
    private boolean checkPermission() {
        Log.d("Home", "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED );
    }

    // Asks for permission
    private void askPermission() {
        Log.d("Home", "askPermission()");
        ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, REQ_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("Home", "onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Home_Fragment.REQ_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    if (checkPermission())
                        mGoogleMap.setMyLocationEnabled(true);

                } else {
                    // Permission denied

                }
                break;

        }
    }

    private void initMap() {
        int googlePlayStatus = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (googlePlayStatus != ConnectionResult.SUCCESS) {
            GooglePlayServicesUtil.getErrorDialog(googlePlayStatus, getActivity(), -1).show();
        } else {
            if (mGoogleMap != null) {
                if(checkPermission()) {
                    mGoogleMap.setMyLocationEnabled(true);
                    mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
                    mGoogleMap.getUiSettings().setAllGesturesEnabled(true);

                    getCurrentLocation();
                }
                else
                    askPermission();
            }
        }
    }

    private void getCurrentLocation() {
        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location location = null;
        if (!(isGPSEnabled || isNetworkEnabled))
            Snackbar.make(mMapView, "Please enable GPS or Network provider.", Snackbar.LENGTH_INDEFINITE).show();
        else {
            if (isNetworkEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        MainActivity.LOCATION_UPDATE_MIN_TIME, MainActivity.LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (isGPSEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        MainActivity.LOCATION_UPDATE_MIN_TIME, MainActivity.LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        if (location != null) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng((double)location.getLatitude(), (double)location.getLongitude()), Utils.MAP_ZOOM_SIZE));
            drawMarker(location);
        }
    }

    private void drawMarker(Location location) {
        if (mGoogleMap != null) {
            mGoogleMap.clear();
            LatLng gps = new LatLng(location.getLatitude(), location.getLongitude());
            mGoogleMap.addMarker(new MarkerOptions()
                    .position(gps)
                    .anchor(0.5f, 0.5f)
                    .title("Current Position")
                    .icon(BitmapDescriptorFactory.fromBitmap(Utils.resizeMarkerIconImage(R.drawable.current_user, getActivity()))));

//            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 12));
        }
    }

    private void search(String from, String to) {
        loadingDlg.setVisibility(View.VISIBLE);

        Call<List<RouteFare>> call = apiInterface.getRouteFare(from, to);
        call.enqueue(new Callback<List<RouteFare>>() {
            @Override
            public void onResponse(Call<List<RouteFare>> call, Response<List<RouteFare>> response) {
                loadingDlg.setVisibility(View.GONE);
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<List<RouteFare>> call, Throwable t) {
                loadingDlg.setVisibility(View.GONE);
                call.cancel();

                new MaterialDialog.Builder(getContext())
                        .title("Error")
                        .content("Connection Failed.")
                        .positiveText("Ok")
                        .show();
            }
        });
    }

    private static final String BACK_STACK_ROOT_TAG = "root_fragment";

    private void generateDataList(List<RouteFare> routeFareList) {
        this.routeFareList = routeFareList;

        PlanRideSearch_Fragment fragment = new PlanRideSearch_Fragment();
        fragment.routeFareList = this.routeFareList;
        fragment.from = etFrom.getEditableText().toString();
        fragment.to = etTo.getEditableText().toString();

        fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).addToBackStack(BACK_STACK_ROOT_TAG).commit();
    }

    private void loadBusStop() {
        loadingDlg.setVisibility(View.VISIBLE);

        Call<List<BusStop>> call = apiInterface.getBusStopList();
        call.enqueue(new Callback<List<BusStop>>() {
            @Override
            public void onResponse(Call<List<BusStop>> call, Response<List<BusStop>> response) {
                loadingDlg.setVisibility(View.GONE);
                busStopList = response.body();

//                loadBusList();
                loadBusRoute();
            }

            @Override
            public void onFailure(Call<List<BusStop>> call, Throwable t) {
                loadingDlg.setVisibility(View.GONE);
                call.cancel();

                new MaterialDialog.Builder(getContext())
                        .title("Error")
                        .content("Connection Failed.")
                        .positiveText("Ok")
                        .show();

                return;
            }
        });
    }

    private void loadBusList() {
        loadingDlg.setVisibility(View.VISIBLE);

        Call<List<Bus>> call = apiInterface.getBusList();
        call.enqueue(new Callback<List<Bus>>() {
            @Override
            public void onResponse(Call<List<Bus>> call, Response<List<Bus>> response) {
                loadingDlg.setVisibility(View.GONE);
                busList = response.body();

//                loadBusRoute();
            }

            @Override
            public void onFailure(Call<List<Bus>> call, Throwable t) {
                loadingDlg.setVisibility(View.GONE);
                call.cancel();

                new MaterialDialog.Builder(getContext())
                        .title("Error")
                        .content("Connection Failed.")
                        .positiveText("Ok")
                        .show();

                return;
            }
        });
    }

    private void loadBusRoute() {
        loadingDlg.setVisibility(View.VISIBLE);

        Call<List<BusRoute>> call = apiInterface.getBusRouteList();
        call.enqueue(new Callback<List<BusRoute>>() {
            @Override
            public void onResponse(Call<List<BusRoute>> call, Response<List<BusRoute>> response) {
                loadingDlg.setVisibility(View.GONE);
                busRouteList = response.body();
            }

            @Override
            public void onFailure(Call<List<BusRoute>> call, Throwable t) {
                loadingDlg.setVisibility(View.GONE);
                call.cancel();

                new MaterialDialog.Builder(getContext())
                        .title("Error")
                        .content("Connection Failed.")
                        .positiveText("Ok")
                        .show();

                return;
            }
        });
    }
}
