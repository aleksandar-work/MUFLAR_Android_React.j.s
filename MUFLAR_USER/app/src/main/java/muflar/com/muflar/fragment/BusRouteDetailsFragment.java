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
import muflar.com.muflar.model.BusRoute;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusRouteDetailsFragment extends Fragment implements OnBackPressedListener {
    MapView mapFragment;
    GoogleMap googleMap;
    FragmentManager fragmentManager;
    Button btn_busRoute;
    CustomAdapter customAdapter;

    ListView listView;

    List<BusRoute> busRouteList;

    APIInterface apiInterface;

    pl.droidsonroids.gif.GifImageView loadingDlg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_bus_route,container,false);

        fragmentManager=getActivity().getSupportFragmentManager();

        btn_busRoute = (Button)view.findViewById(R.id.btn_busRouteDetail);
        listView=(ListView)view.findViewById(R.id.list_busRoute);
        loadingDlg = view.findViewById(R.id.loading_view);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        if(busRouteList == null)
            loadBusRoute();
        else
            generateDataList(busRouteList);

        btn_busRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //fragmentManager.beginTransaction().replace(R.id.frame_layout, new BusRouteDetailsSecondFragment()).commit();
            }
        });

        return view;
    }

    private void loadBusRoute() {
        loadingDlg.setVisibility(View.VISIBLE);

        Call<List<BusRoute>> call = apiInterface.getBusRouteList();
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

        customAdapter = new CustomAdapter();

        listView.setAdapter(customAdapter);

        final Context ctx = listView.getContext();
        int resId = R.anim.layout_animation_from_bottom;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(ctx, resId);
        listView.setLayoutAnimation(animation);
    }

    private void runLayoutAnimation(final ListView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.setAdapter(customAdapter);


        recyclerView.scheduleLayoutAnimation();
    }

    private static final String BACK_STACK_ROOT_TAG = "root_fragment";

    @Override
    public void onBackPressed() {
        fragmentManager.beginTransaction().replace(R.id.frame_layout,new Home_Fragment()).commit();
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
                v = linf.inflate(R.layout.item_bus_route, parent, false);

                final TextView textView_band_name = (TextView) v.findViewById(R.id.route_34b);

                textView_band_name.setText(busRouteList.get(position).busRoute);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        BusRouteDetailsSecondFragment fragment = new BusRouteDetailsSecondFragment();
                        fragment.mBusRoute = busRouteList.get(position);
                        fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).addToBackStack(BACK_STACK_ROOT_TAG).commit();
                    }
                });

            } catch (Exception e) {

            }
            return v;
        }
    }
}
