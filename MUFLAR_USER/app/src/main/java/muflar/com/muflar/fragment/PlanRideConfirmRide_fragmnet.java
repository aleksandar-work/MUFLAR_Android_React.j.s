package muflar.com.muflar.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import muflar.com.muflar.Manifest;
import muflar.com.muflar.R;
import muflar.com.muflar.activity.MainActivity;
import muflar.com.muflar.helper.DirectionsJSONParser;
import muflar.com.muflar.helper.OnBackPressedListener;
import muflar.com.muflar.http.APIClient;
import muflar.com.muflar.http.APIInterface;
import muflar.com.muflar.model.BusPositionResponse;
import muflar.com.muflar.model.BusRoute;
import muflar.com.muflar.model.BusStop;
import muflar.com.muflar.model.User;
import muflar.com.muflar.model.UserDistanceEvent;
import muflar.com.muflar.model.Utils;
import muflar.com.muflar.model.RouteFare;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PlanRideConfirmRide_fragmnet extends Fragment implements OnMapReadyCallback, OnBackPressedListener {

    MapView mMapView;
    GoogleMap mGoogleMap;
    FragmentManager fragmentManager;

    TextView textView_cancel;
    TextView routeTV;
    TextView busTV;
    TextView fareTV;
    TextView fromTV;
    TextView toTV;

    TextView userTimeTV;
    TextView userDistanceTV;
    TextView busTimeTV;
    TextView busDistanceTV;

    Button button_changeMapStyle;

    RouteFare routeFare;
    String mBusId;

    public static final int REQ_PERMISSION = 100;
    private String LOG_TAG = "Home";

    ArrayList mFromToStopPoints = new ArrayList();
    LatLng nearBusPosition;

    Marker userMaker;
    private Circle accuracyCircle;

    String from, to;
    BusStop fromStop, toStop;

    Location mUserLocation;
    Marker mDriverMarker;
    Boolean isMarkerRotating = false;
    private float v;
    private Handler mHandler;
    Runnable mRunnableCode;

    List<BusStop> mBusStops;

    List<List<HashMap<String, String>>> mFromToPathData;        // 'From stop' --> 'To stop'
    List<List<HashMap<String, String>>> mUserFromPathData;      // 'From user current location' --> 'From stop'
    List<List<HashMap<String, String>>> mBusFromPathData;      // 'From user current location' --> 'From stop'

    PowerMenu mapStyleMenu;
    ArrayList<PowerMenuItem> styleList = new ArrayList<>();

    APIInterface apiInterface;

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {

                mUserLocation = location;
                drawMarker(location);
                mLocationManager.removeUpdates(mLocationListener);

            } else {
                Log.d("PlanRideConfirm", "Location data is null.");
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plan_ride_confirm_ride_fragmnet_constraint, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();

        EventBus.getDefault().register(this);

        mMapView = (MapView) view.findViewById(R.id.map_planride_route);
        textView_cancel = (TextView) view.findViewById(R.id.plain_ride_cancel);
        routeTV = (TextView) view.findViewById(R.id.route_tv);
        busTV = (TextView) view.findViewById(R.id.bus_tv);
        fareTV = (TextView) view.findViewById(R.id.fare_tv);
        fromTV = view.findViewById(R.id.from_tv);
        toTV = view.findViewById(R.id.to_tv);

        button_changeMapStyle = view.findViewById(R.id.button_change_map_style);

        userTimeTV = (TextView) view.findViewById(R.id.user_time_tv);
        userDistanceTV = (TextView) view.findViewById(R.id.user_distance_tv);
        busTimeTV = (TextView) view.findViewById(R.id.bus_time_tv);
        busDistanceTV = (TextView) view.findViewById(R.id.bus_distance_tv);

        textView_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.frame_layout, new Home_Fragment()).commit();
            }
        });

        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        initMapStyleMenu();

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

    @Subscribe
    public void onEvent(final UserDistanceEvent event) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getAndshowDistanceAnd(event.jsonData, event.drawRoutePath);
            }
        });
    }

    private void setData() {
        if (routeFare == null)
            return;

        routeTV.setText(routeFare.busRoute);
        fareTV.setText(routeFare.fare);
//        userTimeTV.setText(routeFare.driveTime);
        fromTV.setText(routeFare.fromRoute);
        toTV.setText(routeFare.toRoute);

        String busId = getBusId(routeFare.busRoute);
        mBusId = busId;
        busTV.setText(busId);

    }

    private String getBusId(String route) {
        String bustId = "";

        if (Home_Fragment.busRouteList == null)
            return bustId;

        for (int i = 0; i < Home_Fragment.busRouteList.size(); i++) {
            BusRoute item = Home_Fragment.busRouteList.get(i);
            if (item.busRoute.equalsIgnoreCase(route)) {
                bustId = item.busId;
                break;
            }
        }

        return bustId;
    }

    private void loadBusStopByRoute(String busRoute) {
        if (busRoute == null)
            return;

        Call<List<BusStop>> call = apiInterface.getBusStopListByRoute(busRoute);
        call.enqueue(new Callback<List<BusStop>>() {
            @Override
            public void onResponse(Call<List<BusStop>> call, Response<List<BusStop>> response) {
                mBusStops = response.body();
                getFromToStopCoordinate();
//                getNearBusCoordinate();

                drawRoute(Utils.USER_PATH);
                drawRoute(Utils.FROMTO_PATH);

                handlerForGetBusLocation();
            }

            @Override
            public void onFailure(Call<List<BusStop>> call, Throwable t) {
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

    private void drawBusStopsMarker() {
        if (mBusStops == null)
            return;

        Boolean start = false;
        for (int i = 0; i < mBusStops.size() - 1; i++) {
            BusStop busStop = mBusStops.get(i);

            if (busStop.id == toStop.id) {
                start = false;
            }

            if (start) {
                LatLng latLng = new LatLng(Double.parseDouble(busStop.lat), Double.parseDouble(busStop.longtitude));
                mGoogleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .anchor(Utils.MARKER_WIDTH, Utils.MARKER_WIDTH)
                        .title("Bus Stops"))
                        .setIcon(BitmapDescriptorFactory.fromBitmap(Utils.resizeMarkerIconImageForBusstop(R.drawable.bus_stop, this.getActivity())));
            }

            if (busStop.id == fromStop.id) {
                start = true;
            }

        }
    }

    @Override
    public void onBackPressed() {
//        fragmentManager.beginTransaction().replace(R.id.frame_layout, new PlanRideSearch_Fragment()).commit();
        mHandler.removeCallbacks(mRunnableCode);
        fragmentManager.popBackStack();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;

//        MapStyleOptions styleOptions=MapStyleOptions.loadRawResourceStyle(getActivity(),R.raw.mapstyle_normal);
//        mGoogleMap.setMapStyle(styleOptions);

        int mapStyle = Utils.getIntPref(getActivity(), Utils.MAY_STYLE);
        changeMapStyle(mapStyle);

        if (checkPermission()) {
//            mGoogleMap.setMyLocationEnabled(false);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        } else
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
        EventBus.getDefault().unregister(this);
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
        return (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    // Asks for permission
    private void askPermission() {
        Log.d("Home", "askPermission()");
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_PERMISSION);
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

    private void changeMapStyle(int style) {
        MapStyleOptions styleOptions = Utils.getMapStyle(getActivity(), style);
        mGoogleMap.setMapStyle(styleOptions);
    }

    private void initMap() {
        int googlePlayStatus = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (googlePlayStatus != ConnectionResult.SUCCESS) {
            GooglePlayServicesUtil.getErrorDialog(googlePlayStatus, getActivity(), -1).show();
        } else {
            if (mGoogleMap != null) {
                if (checkPermission()) {
                    mGoogleMap.setMyLocationEnabled(true);
                    mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
                    mGoogleMap.getUiSettings().setAllGesturesEnabled(true);

                    getCurrentLocation();
                    setData();
                    loadBusStopByRoute(routeFare.busRoute);

                } else
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
            Log.d("Home", String.format("getCurrentLocation(%f, %f)", location.getLatitude(),
                    location.getLongitude()));
            drawMarker(location);
            mUserLocation = location;
        }
    }

    private void drawMarker(Location location) {
        if (mGoogleMap != null) {
            mGoogleMap.clear();

            LatLng gps = new LatLng(location.getLatitude(), location.getLongitude());
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, Utils.MAP_ZOOM_SIZE));
            if (userMaker == null) {
                userMaker = mGoogleMap.addMarker(new MarkerOptions()
                        .position(gps)
                        .anchor(Utils.MARKER_WIDTH, Utils.MARKER_WIDTH)
                        .title("Current Position")
                        .icon(BitmapDescriptorFactory.fromBitmap(Utils.resizeMarkerIconImage(R.drawable.current_user, getActivity()))));
            } else {
                LatLng latlngPosition = new LatLng(location.getLatitude(), location.getLongitude());
                userMaker.setPosition(latlngPosition);
            }

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            float accuracy = location.getAccuracy();

            if (accuracyCircle != null) {
                accuracyCircle.remove();
            }
            final CircleOptions accuracyCircleOptions = new CircleOptions()
                    .center(new LatLng(latitude, longitude))
                    .radius(accuracy)
                    .fillColor(getResources().getColor(R.color.user_tracking_color))
                    .strokeColor(getResources().getColor(R.color.user_tracking_color))
                    .strokeWidth(2.0f);
            accuracyCircle = mGoogleMap.addCircle(accuracyCircleOptions);

        }
    }

    private void drawRoute(int drawRoutePath) {
        LatLng origin = null, dest = null;
        MarkerOptions fromOptions, toOptions;

        switch (drawRoutePath) {
            case Utils.USER_PATH: {
                Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location == null)
                    return;

                origin = new LatLng(location.getLatitude(), location.getLongitude());
                dest = (LatLng) mFromToStopPoints.get(0);

                break;
            }
            case Utils.FROMTO_PATH: {
                if (mFromToStopPoints.size() < 2)
                    return;

                origin = (LatLng) mFromToStopPoints.get(0);
                dest = (LatLng) mFromToStopPoints.get(1);

                break;
            }
            case Utils.BUSFROM_PATH: {
                origin = nearBusPosition;
                dest = (LatLng) mFromToStopPoints.get(0);

                break;
            }
        }


//        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(origin, Utils.MAP_ZOOM_SIZE));

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(origin, dest, drawRoutePath);

        DownloadTask downloadTask = new DownloadTask(drawRoutePath);

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {
        int drawRoutePath = Utils.USER_PATH;

        public DownloadTask(int drawRoutePath) {
            this.drawRoutePath = drawRoutePath;
        }

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask(drawRoutePath);
            parserTask.execute(result);

        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        int drawRoutePath = Utils.USER_PATH;

        // Parsing the data in non-ui thread
        public ParserTask(int drawRoutePath) {
            this.drawRoutePath = drawRoutePath;
        }

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);

                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jObject);

                UserDistanceEvent userDistanceEvent = new UserDistanceEvent();
                userDistanceEvent.drawRoutePath = drawRoutePath;
                userDistanceEvent.jsonData = jObject;

                EventBus.getDefault().post(userDistanceEvent);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            if (result == null)
                return;

            switch (drawRoutePath) {
                case Utils.USER_PATH: {
                    mUserFromPathData = result;
                    break;
                }
                case Utils.FROMTO_PATH: {
                    mFromToPathData = result;
                    break;
                }
                case Utils.BUSFROM_PATH: {
                    mBusFromPathData = result;
                    break;
                }
            }

            drawRoutePath(drawRoutePath);
        }
    }

    private void drawRoutePath(int drawRoutePath) {
        List<List<HashMap<String, String>>> pathData = null;

        switch (drawRoutePath) {
            case Utils.USER_PATH: {
                pathData = mUserFromPathData;
                Log.v("PlanRideCon", "mUserFromPathData");
                break;
            }
            case Utils.FROMTO_PATH: {
                Log.v("PlanRideCon", "mFromToPathData");
                pathData = mFromToPathData;
                break;
            }
            case Utils.BUSFROM_PATH: {
                Log.v("PlanRideCon", "mBusFromPathData");
                pathData = mBusFromPathData;
                break;
            }
        }

        if (pathData == null)
            return;

        ArrayList points = null;
        PolylineOptions lineOptions = null;

        for (int i = 0; i < pathData.size(); i++) {
            points = new ArrayList();
            lineOptions = new PolylineOptions();

            List<HashMap<String, String>> path = pathData.get(i);
            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }

            lineOptions.addAll(points);
            lineOptions.width(Utils.LINE_WIDTH);

            switch (drawRoutePath) {
                case Utils.USER_PATH: {
                    lineOptions.color(getResources().getColor(R.color.user_track_line));

                    LatLng gps = new LatLng(mUserLocation.getLatitude(), mUserLocation.getLongitude());
                    mGoogleMap.addMarker(new MarkerOptions()
                            .position(gps)
                            .anchor(Utils.MARKER_WIDTH, Utils.MARKER_WIDTH)
                            .title("User"))
                            .setIcon(BitmapDescriptorFactory.fromBitmap(Utils.resizeMarkerIconImage(R.drawable.current_user, getActivity())));

                    break;
                }
                case Utils.FROMTO_PATH: {

                    drawBusStopsMarker();

                    lineOptions.color(getResources().getColor(R.color.from_to_color));

                    LatLng origin = (LatLng) mFromToStopPoints.get(0);
                    LatLng dest = (LatLng) mFromToStopPoints.get(1);

                    mGoogleMap.addMarker(new MarkerOptions()
                            .position(origin)
                            .anchor(Utils.MARKER_WIDTH, Utils.MARKER_WIDTH)
                            .title("From"))
                            .setIcon(BitmapDescriptorFactory.fromBitmap(Utils.resizeMarkerIconImage(R.drawable.from, getActivity())));

                    mGoogleMap.addMarker(new MarkerOptions()
                            .position(dest)
                            .anchor(Utils.MARKER_WIDTH, Utils.MARKER_WIDTH)
                            .title("To"))
                            .setIcon(BitmapDescriptorFactory.fromBitmap(Utils.resizeMarkerIconImage(R.drawable.to, getActivity())));

                    break;
                }
                case Utils.BUSFROM_PATH: {
                    lineOptions.color(getResources().getColor(R.color.bus_tracking_color));

                    LatLng busPosition = nearBusPosition;



                    break;
                }
            }


            lineOptions.geodesic(true);
        }

        if (lineOptions == null)
            return;

        mGoogleMap.addPolyline(lineOptions);
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest, int drawRoutePath) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=walking";

        switch (drawRoutePath) {
            case Utils.USER_PATH: {
                mode = "mode=walking";
                break;
            }
            case Utils.FROMTO_PATH: {
                mode = "mode=driving";
                break;
            }
            case Utils.BUSFROM_PATH: {
                mode = "mode=driving";
                break;
            }
        }

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private void getFromToStopCoordinate() {
        fromStop = getBusStop(routeFare.fromRoute);
        toStop = getBusStop(routeFare.toRoute);

        if (fromStop == null || toStop == null)
            return;

        LatLng fromPoint = new LatLng(Double.parseDouble(fromStop.lat), Double.parseDouble(fromStop.longtitude));
        LatLng toPoint = new LatLng(Double.parseDouble(toStop.lat), Double.parseDouble(toStop.longtitude));

        mFromToStopPoints.add(fromPoint);
        mFromToStopPoints.add(toPoint);
    }

    private void getNearBusCoordinate() {

        Call<List<BusPositionResponse>> call = apiInterface.getBusPosition(mBusId);
        call.enqueue(new Callback<List<BusPositionResponse>>() {
            @Override
            public void onResponse(Call<List<BusPositionResponse>> call, Response<List<BusPositionResponse>> response) {
                List<BusPositionResponse> body = response.body();
                BusPositionResponse busPosition = body.get(body.size() - 1);

//                nearBusPosition = new LatLng(1.3460634545457129, 103.69688879364014);
//                nearBusPosition = new LatLng(1.3451812511344816, 103.69667421691895);
                nearBusPosition = new LatLng(1.3437319162665844, 103.69530092590333);

                //                nearBusPosition = new LatLng(Double.valueOf(busPosition.cutLat), Double.valueOf(busPosition.curLong));
//                LatLng nearBusPosition1 = new LatLng(1.3445712166742203,  103.69633089416504);
//                LatLng nearBusPosition1 = new LatLng(1.344756908498621,  103.69654547088624);
                LatLng nearBusPosition1 = new LatLng(1.3430263539994625,  103.69521509521485);

                mDriverMarker = mGoogleMap.addMarker(new MarkerOptions()
                        .position(nearBusPosition)
                        .anchor(Utils.MARKER_WIDTH, Utils.MARKER_WIDTH)
                        .title("Bus"));
                mDriverMarker.setIcon(BitmapDescriptorFactory.fromBitmap(Utils.resizeMarkerIconImage(R.drawable.bus, getActivity())));

                animateCar(nearBusPosition1);
                drawRoute(Utils.BUSFROM_PATH);
            }
            @Override
            public void onFailure(Call<List<BusPositionResponse>> call, Throwable t) {
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

    private BusStop getBusStop(String stopName) {
        BusStop stop = null;

        if(Home_Fragment.busStopList == null)
            return stop;

        for(int i = 0; i < Home_Fragment.busStopList.size(); i++) {
            stop = Home_Fragment.busStopList.get(i);
            if(stop.stop.equals(stopName))
                break;
        }

        return stop;
    }

    private void getAndshowDistanceAnd(JSONObject jObject, int drawRoutePath){


        switch (drawRoutePath){
            case Utils.USER_PATH: {

                JSONArray routesArray = null;
                String distance = "";
                String duration = "";
                try {
                    routesArray = jObject.getJSONArray("routes");
                    JSONObject routsFirstObject = routesArray.getJSONObject(0);
                    JSONArray legsArray = routsFirstObject.getJSONArray("legs");
                    JSONObject legsFirstObject = legsArray.getJSONObject(0);
                    JSONObject distanceObject = legsFirstObject.getJSONObject("distance");
                    JSONObject durationObject = legsFirstObject.getJSONObject("duration");
                    distance = distanceObject.getString("text").toString();
                    duration = durationObject.getString("text").toString();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                this.userDistanceTV.setText(distance);
                this.userTimeTV.setText(duration);
                break;
            }
            case Utils.FROMTO_PATH: {
                break;
            }
            case Utils.BUSFROM_PATH: {
                JSONArray routesArray = null;
                String distance = "";
                String duration = "";
                try {
                    routesArray = jObject.getJSONArray("routes");
                    JSONObject routsFirstObject = routesArray.getJSONObject(0);
                    JSONArray legsArray = routsFirstObject.getJSONArray("legs");
                    JSONObject legsFirstObject = legsArray.getJSONObject(0);
                    JSONObject distanceObject = legsFirstObject.getJSONObject("distance");
                    JSONObject durationObject = legsFirstObject.getJSONObject("duration");
                    distance = distanceObject.getString("text").toString();
                    duration = durationObject.getString("text").toString();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                this.busDistanceTV.setText(distance);
                this.busTimeTV.setText(duration);

                break;
            }
        }

    }

    private void handlerForGetBusLocation(){
        mHandler = new Handler(); // update location periodicly
        mRunnableCode = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread
                getNearBusCoordinate();

                mHandler.postDelayed(this, 3000);
            }
        };
        mHandler.post(mRunnableCode);
    }

    private void animateCar(final LatLng destination) {
        final LatLng startPosition = mDriverMarker.getPosition();
        final LatLng endPosition = new LatLng(destination.latitude, destination.longitude);

        final float startRotation = mDriverMarker.getRotation();
        final LatLngInterpolatorNew latLngInterpolator = new LatLngInterpolatorNew.LinearFixed();

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(3000); // duration 3 second
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                try {
                    float v = animation.getAnimatedFraction();
                    LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
                    mDriverMarker.setPosition(newPosition);
                    mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                            .target(newPosition)
                            .zoom(15.5f)
                            .build()));

                    mDriverMarker.setRotation(getBearing(startPosition, new LatLng(destination.latitude, destination.longitude)));
                } catch (Exception ex) {
                    //I don't care atm..
                }
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                // if (mMarker != null) {
                // mMarker.remove();
                // }
                // mMarker = googleMap.addMarker(new MarkerOptions().position(endPosition).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car)));

            }
        });
        valueAnimator.start();
    }

    private interface LatLngInterpolatorNew {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class LinearFixed implements LatLngInterpolatorNew {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lngDelta = b.longitude - a.longitude;
                // Take the shortest path across the 180th meridian.
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }
    }

    private float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }

}

