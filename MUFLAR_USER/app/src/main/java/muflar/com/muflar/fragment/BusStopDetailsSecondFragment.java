package muflar.com.muflar.fragment;

import android.content.Context;
import android.content.pm.PackageManager;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
import com.google.android.gms.maps.model.PolylineOptions;
import com.tuyenmonkey.mkloader.MKLoader;

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
import muflar.com.muflar.model.BusRoute;
import muflar.com.muflar.model.BusStop;
import muflar.com.muflar.model.ETAInfo;
import muflar.com.muflar.model.ETAResponse;
import muflar.com.muflar.model.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusStopDetailsSecondFragment extends Fragment implements OnBackPressedListener, OnMapReadyCallback {
    MapView mMapView;
    GoogleMap mGoogleMap;
    FragmentManager fragmentManager;
    Button button_1, button_2, button_3, button_4, button_5, button_6;
    TextView textView_cancel;
    TextView busRouteTitleTV;
    ListView listView;

    public static final int REQ_PERMISSION = 100;
    private static final String BACK_STACK_ROOT_TAG = "root_fragment";

    List<BusRoute> busRouteList;
    APIInterface apiInterface;
    pl.droidsonroids.gif.GifImageView loadingDlg;

    BusStop mBusStop;
    public String mBusStopName = "";

    Location mUserLocation;
//    List<BusStop> mBusStopList;

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                mUserLocation = location;
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
        View view = inflater.inflate(R.layout.fragment_bus_stop_second, container, false);

        fragmentManager = getActivity().getSupportFragmentManager();
        listView = (ListView) view.findViewById(R.id.list_stopdetail);
        loadingDlg = view.findViewById(R.id.loading_view);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        mBusStopName = mBusStop.stop;
        loadBusRoutesByStop();

        mMapView = (MapView) view.findViewById(R.id.map_bus_route);

        textView_cancel = (TextView) view.findViewById(R.id.cancel);
        busRouteTitleTV = (TextView) view.findViewById(R.id.bus_route_title_tv);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);

        textView_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.frame_layout, new Home_Fragment()).commit();
            }
        });

        busRouteTitleTV.setText(mBusStopName + " Bus Stop Details");

        return view;
    }


    @Override
    public void onBackPressed() {
        fragmentManager.beginTransaction().replace(R.id.frame_layout, new BusStopDetailsFragment()).commit();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;

//        MapStyleOptions styleOptions = MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.mapstyle_normal);
//        mGoogleMap.setMapStyle(styleOptions);

        int mapStyle = Utils.getIntPref(getActivity(), Utils.MAY_STYLE);
        changeMapStyle(mapStyle);

        if (checkPermission()) {
//            mGoogleMap.setMyLocationEnabled(false);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        } else
            askPermission();

        //mGoogleMap.setMyLocationEnabled(true);

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
//                    drawRoute();
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
            Log.d("Home", String.format("getCurrentLocation(%f, %f)", location.getLatitude(), location.getLongitude()));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng((double) location.getLatitude(), (double) location.getLongitude()), Utils.MAP_ZOOM_SIZE));
            drawMarker(location);
            mUserLocation = location;
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
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.user)));

//            drawRoutePath();
        }
    }

    private void loadBusRoutesByStop() {
        loadingDlg.setVisibility(View.VISIBLE);

        Call<List<BusRoute>> call = apiInterface.getBusRouteListByStop(mBusStopName);
        call.enqueue(new Callback<List<BusRoute>>() {
            @Override
            public void onResponse(Call<List<BusRoute>> call, Response<List<BusRoute>> response) {
                loadingDlg.setVisibility(View.GONE);
                generateDataList(response.body());
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

    private void generateDataList(List<BusRoute> busRouteList) {
        this.busRouteList = busRouteList;

        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);
        final Context ctx = listView.getContext();
        int resId = R.anim.layout_animation_from_bottom;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(ctx, resId);
        listView.setLayoutAnimation(animation);

//        drawBusRoutes();
        drawUserToBusstopPath();
    }

    private class CustomAdapter extends BaseAdapter {
        View v;

        @Override
        public int getCount() {
            return busRouteList.size();
        }

        @Override
        public Object getItem(int position) {
            return busRouteList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            try {

                LayoutInflater linf = getActivity().getLayoutInflater();
                v = linf.inflate(R.layout.item_stope_route, parent, false);

                final TextView textView_band_name = (TextView) v.findViewById(R.id.stop_name);
                final TextView tv_eta = (TextView) v.findViewById(R.id.tv_eta);
                final Button button = (Button) v.findViewById(R.id.bt_1);

                BusRoute item = busRouteList.get(position);
                calculateETAWithOrigin(mBusStopName, tv_eta);

                textView_band_name.setText(item.busRoute);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ConfirmRide_fragment fragment = new ConfirmRide_fragment();
                        fragment.busStop = mBusStop;
                        fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).addToBackStack(BACK_STACK_ROOT_TAG).commit();
                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        fragmentManager.beginTransaction().replace(R.id.frame_layout, new ConfirmRide_fragment()).addToBackStack(BACK_STACK_ROOT_TAG).commit();
                    }
                });

            } catch (Exception e) {

            }
            return v;
        }

    }

    private void calculateETAWithOrigin(String origin, final TextView etaTV){

        ETAInfo body = new ETAInfo();
        body.origin = origin;
        body.destination = "";

        Call<ETAResponse> call = apiInterface.getETA(body);
        call.enqueue(new Callback<ETAResponse>() {
            @Override
            public void onResponse(Call<ETAResponse> call, Response<ETAResponse> response) {
                ETAResponse body = response.body();
                if (body.eta != null) etaTV.setText(body.eta);
            }

            @Override
            public void onFailure(Call<ETAResponse> call, Throwable t) {
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

    private void drawBusRoutes() {
        if(this.busRouteList == null)
            return;

        for (int i = 0; i < this.busRouteList.size(); i++) {
            getBusStopsWithRoute(busRouteList.get(i));
        }
    }

    private void getBusStopsWithRoute(BusRoute busRoute) {

        Call<List<BusStop>> call = apiInterface.getBusStopListByRoute(busRoute.busRoute);
        call.enqueue(new Callback<List<BusStop>>() {
            @Override
            public void onResponse(Call<List<BusStop>> call, Response<List<BusStop>> response) {
//                mBusStopList = response.body();
                drawBusRouteWithBusStops(response.body());
            }

            @Override
            public void onFailure(Call<List<BusStop>> call, Throwable t) {
                new MaterialDialog.Builder(getContext())
                        .title("Error")
                        .content("Connection Failed.")
                        .positiveText("Ok")
                        .show();

                return;
            }
        });
    }

    private void drawBusRouteWithBusStops(List<BusStop> busStopList) {
        if(busStopList == null)
            return;

        BusStop fromStop = busStopList.get(0);
        BusStop toStop = busStopList.get(busStopList.size() - 1);

        LatLng origin = new LatLng(Double.parseDouble(fromStop.lat), Double.parseDouble(fromStop.longtitude));
        LatLng dest = new LatLng(Double.parseDouble(toStop.lat), Double.parseDouble(toStop.longtitude));

        String url = getDirectionsUrl(origin, dest);

        Log.d("Home", url);

        BusStopDetailsSecondFragment.DownloadTask downloadTask = new BusStopDetailsSecondFragment.DownloadTask(origin, dest);

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }


    private void drawUserToBusstopPath(){
//        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location location = mUserLocation;
        if (location == null)
            return;

        LatLng origin = new LatLng(location.getLatitude(), location.getLongitude());
        LatLng  dest = new LatLng(Double.valueOf(mBusStop.lat), Double.valueOf(mBusStop.longtitude));

        String url = getDirectionsUrl(origin, dest);

        BusStopDetailsSecondFragment.DownloadTask downloadTask = new BusStopDetailsSecondFragment.DownloadTask(origin, dest);

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
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

            BusStopDetailsSecondFragment.ParserTask parserTask = new BusStopDetailsSecondFragment.ParserTask(from, to);
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
            lineOptions.color( getResources().getColor(R.color.user_tracking_color));
            lineOptions.geodesic(true);
        }

        if(lineOptions == null)
            return;

//        mGoogleMap.addMarker(new MarkerOptions()
//                        .position(origin)
//                        .anchor(Utils.MARKER_WIDTH, Utils.MARKER_WIDTH)
//                /*.title("From")*/)
//                .setIcon(BitmapDescriptorFactory.fromBitmap(Utils.resizeMarkerIconImage(R.drawable.current_user, getActivity())));

        mGoogleMap.addMarker(new MarkerOptions()
                        .position(dest)
                        .anchor(Utils.MARKER_WIDTH, Utils.MARKER_WIDTH)
                /*.title("To")*/)
                .setIcon(BitmapDescriptorFactory.fromBitmap(Utils.resizeMarkerIconImage(R.drawable.bus_stop, getActivity())));

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

}
