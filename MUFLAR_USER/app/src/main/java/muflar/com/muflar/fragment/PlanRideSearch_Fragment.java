package muflar.com.muflar.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
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
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
import com.google.android.gms.maps.model.PolylineOptions;

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
import muflar.com.muflar.helper.StatusBarUtil;
import muflar.com.muflar.model.BusStop;
import muflar.com.muflar.model.Utils;
import muflar.com.muflar.model.RouteFare;

public class PlanRideSearch_Fragment extends Fragment implements OnMapReadyCallback,OnBackPressedListener {
    MapView mMapView;
    GoogleMap mGoogleMap;
    FragmentManager fragmentManager;
    Button button_one,button_two,button__three,button_four,button_five,button_six;
    TextView textView_cancel;
    List<RouteFare> routeFareList;
    ListView listView;
    CustomAdapter customAdapter;

    public static final int         REQ_PERMISSION = 100;
    private String                  LOG_TAG = "Home";

    ArrayList mFromToStopPoints = new ArrayList();

    String      from, to;
    BusStop     fromStop, toStop;

    List<List<HashMap<String, String>>> mFromToPathData;

    String      driveTime;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_plan,container,false);

            fragmentManager=getActivity().getSupportFragmentManager();
            listView=(ListView)view.findViewById(R.id.list_plain_ride);

        customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);
        final Context ctx = listView.getContext();
        int resId = R.anim.layout_animation_from_bottom;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(ctx, resId);
        listView.setLayoutAnimation(animation);

        mMapView = (MapView)view. findViewById(R.id.maproute_plan);
        textView_cancel=(TextView)view.findViewById(R.id.cancel_confirm_ride);

        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);

        textView_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                fragmentManager.beginTransaction().replace(R.id.frame_layout,new Home_Fragment()).commit();
                fragmentManager.popBackStack();
            }
        });

        getFromToStopCoordinate();

        return view;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap =googleMap;

//        MapStyleOptions styleOptions=MapStyleOptions.loadRawResourceStyle(getActivity(),R.raw.mapstyle_normal);
//        mGoogleMap.setMapStyle(styleOptions);

        int mapStyle = Utils.getIntPref(getActivity(), Utils.MAY_STYLE);
        changeMapStyle(mapStyle);

        mGoogleMap.setMyLocationEnabled (true);

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
                if(checkPermission()) {
                    mGoogleMap.setMyLocationEnabled(true);
                    mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
                    mGoogleMap.getUiSettings().setAllGesturesEnabled(true);

                    getCurrentLocation();
                    drawRoute();
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
            Log.d("Home", String.format("getCurrentLocation(%f, %f)", location.getLatitude(), location.getLongitude()));
//            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng((double)location.getLatitude(), (double)location.getLongitude()), 30));
            drawMarker(location);
        }
    }

    private void drawMarker(Location location) {
        if (mGoogleMap != null) {
            mGoogleMap.clear();

            LatLng gps = new LatLng(location.getLatitude(), location.getLongitude());
            mGoogleMap.addMarker(new MarkerOptions()
                    .position(gps)
                    .title("Current Position")
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                    .anchor(Utils.MARKER_WIDTH, Utils.MARKER_WIDTH)
                    .icon(BitmapDescriptorFactory.fromBitmap(Utils.resizeMarkerIconImage(R.drawable.current_user, getActivity()))));

            drawRoutePath();
        }
    }

    private void drawRoute() {
        if(mFromToStopPoints.size() < 2)
            return;

        LatLng origin = (LatLng) mFromToStopPoints.get(0);
        LatLng dest = (LatLng) mFromToStopPoints.get(1);

        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(origin, Utils.MAP_ZOOM_SIZE));

        MarkerOptions fromOptions = new MarkerOptions();
        fromOptions.position(origin);
        fromOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        // Add new marker to the Google Map Android API V2
        mGoogleMap.addMarker(fromOptions);

        MarkerOptions toOptions = new MarkerOptions();
        toOptions.position(dest);
        toOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));

        mGoogleMap.addMarker(toOptions);

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(origin, dest);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

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

            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);

        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);

                JSONObject jLeg = parser.jLegs.getJSONObject(0);
                if(jLeg != null) {
                    JSONObject duration = jLeg.getJSONObject("duration");
                    if(duration != null) {
                        driveTime = duration.getString("text");

                        for (int i = 0; i < routeFareList.size(); i++) {
                            RouteFare item = routeFareList.get(i);
                            item.driveTime = driveTime;
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                customAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            if(result == null)
                return;

            mFromToPathData = result;
            drawRoutePath();
        }
    }

    private void drawRoutePath() {
        if(mFromToPathData == null)
            return;

        ArrayList points = null;
        PolylineOptions lineOptions = null;

        for (int i = 0; i < mFromToPathData.size(); i++) {
            points = new ArrayList();
            lineOptions = new PolylineOptions();

            List<HashMap<String, String>> path = mFromToPathData.get(i);

            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }

            lineOptions.addAll(points);
            lineOptions.width(Utils.LINE_WIDTH);
            lineOptions.color( getResources().getColor(R.color.from_to_color));
            lineOptions.geodesic(true);
        }

        if(lineOptions == null)
            return;

        LatLng origin = (LatLng) mFromToStopPoints.get(0);
        LatLng dest = (LatLng) mFromToStopPoints.get(1);

        mGoogleMap.addMarker(new MarkerOptions()
                .position(origin)
                .title("From")
                .anchor(Utils.MARKER_WIDTH, Utils.MARKER_WIDTH)
                .icon(BitmapDescriptorFactory.fromBitmap(Utils.resizeMarkerIconImage(R.drawable.from, getActivity()))));
//                .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        mGoogleMap.addMarker(new MarkerOptions()
                .position(dest)
                .title("To")
                .anchor(Utils.MARKER_WIDTH, Utils.MARKER_WIDTH)
                .icon(BitmapDescriptorFactory.fromBitmap(Utils.resizeMarkerIconImage(R.drawable.to, getActivity()))));
//                .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        mGoogleMap.addPolyline(lineOptions);
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
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
        fromStop = getBusStop(from);
        toStop = getBusStop(to);

        if(fromStop == null || toStop == null)
            return;

        LatLng fromPoint = new LatLng(Double.parseDouble(fromStop.lat), Double.parseDouble(fromStop.longtitude));
        LatLng toPoint = new LatLng(Double.parseDouble(toStop.lat), Double.parseDouble(toStop.longtitude));

        mFromToStopPoints.add(fromPoint);
        mFromToStopPoints.add(toPoint);
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

    private static final String BACK_STACK_ROOT_TAG = "root_fragment";

    @Override
    public void onBackPressed() {
//        fragmentManager.beginTransaction().replace(R.id.frame_layout,new Home_Fragment()).commit();
        fragmentManager.popBackStack();
    }

    private class CustomAdapter extends BaseAdapter {
        View v;

        @Override
        public int getCount() {
            return routeFareList.size();
        }

        @Override
        public Object getItem(int position) {
            return routeFareList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_plain_route, parent, false);
                viewHolder = new ViewHolder();

                viewHolder.busRouteTV = (TextView) convertView.findViewById(R.id.bus_route_tv);
                viewHolder.routeFareTV = (TextView) convertView.findViewById(R.id.route_fare_tv);
                viewHolder.timeTV = (TextView) convertView.findViewById(R.id.time_tv);
                viewHolder.btnConfirm = (Button) convertView.findViewById(R.id.btn_confirm);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            RouteFare item = routeFareList.get(position);
            if(item != null) {
                viewHolder.busRouteTV.setText(item.busRoute);
                viewHolder.routeFareTV.setText(item.fare);
                viewHolder.timeTV.setText(item.driveTime);

                final PlanRideConfirmRide_fragmnet fragment = new PlanRideConfirmRide_fragmnet();
                fragment.routeFare = item;

                viewHolder.btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_layout, fragment)
                                .addToBackStack(BACK_STACK_ROOT_TAG)
                                .commit();
                    }
                });
            }

            return convertView;
        }

        private class ViewHolder {
            public TextView busRouteTV;
            public TextView routeFareTV;
            public TextView timeTV;
            public Button   btnConfirm;
        }
    }
}
