package muflar.com.muflar.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.List;

import muflar.com.muflar.R;
import muflar.com.muflar.helper.OnBackPressedListener;
import muflar.com.muflar.http.APIClient;
import muflar.com.muflar.http.APIInterface;
import muflar.com.muflar.model.BusStop;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusStopDetailsFragment extends Fragment implements OnBackPressedListener {
    MapView mapFragment;
    GoogleMap googleMap;
    FragmentManager fragmentManager;
    Button btn_busStop;
    ListView listView;

    List<BusStop> busStopList;
    APIInterface apiInterface;
    pl.droidsonroids.gif.GifImageView loadingDlg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_bus_stop,container,false);

        fragmentManager=getActivity().getSupportFragmentManager();

        btn_busStop = (Button)view.findViewById(R.id.btn_busStop);
        listView=(ListView)view.findViewById(R.id.list_stop);
        loadingDlg = view.findViewById(R.id.loading_view);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        loadBusStop();

        btn_busStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction().replace(R.id.frame_layout,new BusStopDetailsSecondFragment()).commit();
            }
        });

        return view;
    }

    private void loadBusStop() {
        loadingDlg.setVisibility(View.VISIBLE);

        Call<List<BusStop>> call = apiInterface.getBusStopList();
        call.enqueue(new Callback<List<BusStop>>() {
            @Override
            public void onResponse(Call<List<BusStop>> call, Response<List<BusStop>> response) {
                loadingDlg.setVisibility(View.GONE);
                generateDataList(response.body());
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

    private void generateDataList(List<BusStop> busStopList) {
        this.busStopList = busStopList;

        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);
        final Context ctx = listView.getContext();
        int resId = R.anim.layout_animation_from_bottom;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(ctx, resId);
        listView.setLayoutAnimation(animation);
    }

    @Override
    public void onBackPressed() {
        fragmentManager.beginTransaction().replace(R.id.frame_layout,new Home_Fragment()).commit();
    }
    private class CustomAdapter extends BaseAdapter {
        View v;

        @Override
        public int getCount() {
            return busStopList.size();
        }

        @Override
        public Object getItem(int position) {
            return busStopList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            try {
                LayoutInflater linf = getActivity().getLayoutInflater();
                v = linf.inflate(R.layout.item_bus_route, parent, false);

                final TextView textView_band_name = (TextView) v.findViewById(R.id.route_34b);

                textView_band_name.setText(busStopList.get(position).stop);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        BusStopDetailsSecondFragment fragment = new BusStopDetailsSecondFragment();
                        fragment.mBusStop = busStopList.get(position);

                        fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit();
                    }
                });

            } catch (Exception e) {

            }
            return v;
        }
    }
}
