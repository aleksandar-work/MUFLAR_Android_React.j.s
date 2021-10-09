package muflar.com.muflar.fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import muflar.com.muflar.model.Bus;
import muflar.com.muflar.model.BusStop;
import muflar.com.muflar.model.Utils;


public class ConfirmRide_fragment extends Fragment implements OnBackPressedListener,OnMapReadyCallback {
    MapView mMapView;
    GoogleMap mGoogleMap;
    FragmentManager fragmentManager;
    TextView textView_cancel;
    TextView busStopTv;
    TextView busIdTv;
    TextView durationTv;

    BusStop busStop;

    public static final int         REQ_PERMISSION = 100;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view=inflater.inflate(R.layout.fragment_confirm_ride_fragment, container, false);
        fragmentManager=getActivity().getSupportFragmentManager();

        mMapView = (MapView)view. findViewById(R.id.map_confirm_route);
        textView_cancel = (TextView)view.findViewById(R.id.text_cancel);
        busStopTv = (TextView) view.findViewById(R.id.bus_stop_tv);
        busIdTv = (TextView) view.findViewById(R.id.bus_id_tv);
        durationTv = (TextView) view.findViewById(R.id.duration_tv);

        textView_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.frame_layout,new Home_Fragment()).commit();
            }
        });

        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);

        setData();

        return view;
    }

    private void setData() {
        if(busStop == null)
            return;

        busStopTv.setText(busStop.busRoute);
    }

    private void drawBusStopMarker(){
        LatLng busStopPoint = new LatLng(Double.parseDouble(busStop.lat), Double.parseDouble(busStop.longtitude));
        mGoogleMap.addMarker(new MarkerOptions()
                .position(busStopPoint)
                .title("")
                .anchor(Utils.MARKER_WIDTH, Utils.MARKER_WIDTH)
                .icon(BitmapDescriptorFactory.fromBitmap(Utils.resizeMarkerIconImage(R.drawable.to, getActivity()))));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap =googleMap;

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
                    .anchor(Utils.MARKER_WIDTH, Utils.MARKER_WIDTH)
                    .title("Current Position")
                    .icon(BitmapDescriptorFactory.fromBitmap(Utils.resizeMarkerIconImage(R.drawable.current_user, getActivity()))));

            if (busStop != null) drawBusStopMarker();

        }
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

//    private void drawBusRouteWithBusStops() {
//        if(busStop == null)
//            return;
//
//            LatLng origin = new LatLng(Double.parseDouble(busStop.lat), Double.parseDouble(busStop.longtitude));
//
//            String url = getDirectionsUrl(origin, origin, false);
//
//            ConfirmRide_fragment.DownloadTask downloadTask = new ConfirmRide_fragment.DownloadTask(origin, origin);
//            downloadTask.execute(url);
//
//    }

    private class DownloadTask extends AsyncTask<String, Void, String> {
        private LatLng from, to;

        public DownloadTask(LatLng from, LatLng to) {
            this.from = from;
            this.to = to;
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

            ConfirmRide_fragment.ParserTask parserTask = new ConfirmRide_fragment.ParserTask(from, to);
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        private LatLng from, to;

        public ParserTask(LatLng from, LatLng to) {
            this.from = from;
            this.to = to;
        }

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            if(result == null)
                return;

            drawRoutePath(from, to, result);
        }
    }

    private void drawRoutePath(LatLng origin, LatLng dest, List<List<HashMap<String, String>>> routes) {
        if(routes == null)
            return;

        ArrayList points = null;
        PolylineOptions lineOptions = null;

        for (int i = 0; i < routes.size(); i++) {
            points = new ArrayList();
            lineOptions = new PolylineOptions();

            List<HashMap<String, String>> path = routes.get(i);

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

        mGoogleMap.addMarker(new MarkerOptions()
                        .position(origin)
                        .anchor(Utils.MARKER_WIDTH, Utils.MARKER_WIDTH)
                /*.title("From")*/)
                .setIcon(BitmapDescriptorFactory.fromBitmap(Utils.resizeMarkerIconImage(R.drawable.from, getActivity())));

        mGoogleMap.addMarker(new MarkerOptions()
                        .position(dest)
                        .anchor(Utils.MARKER_WIDTH, Utils.MARKER_WIDTH)
                /*.title("To")*/)
                .setIcon(BitmapDescriptorFactory.fromBitmap(Utils.resizeMarkerIconImage(R.drawable.to, getActivity())));

        mGoogleMap.addPolyline(lineOptions);
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest, Boolean isFromUserPath) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        if (isFromUserPath) mode = "mode=walking";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }


    @Override
    public void onBackPressed() {
//        fragmentManager.beginTransaction().replace(R.id.frame_layout,new BusRouteDetailsFragment()).commit();
        fragmentManager.popBackStack();
    }


}



