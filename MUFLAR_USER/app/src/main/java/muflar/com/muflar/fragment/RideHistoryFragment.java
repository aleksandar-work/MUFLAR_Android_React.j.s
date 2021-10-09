package muflar.com.muflar.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.List;

import muflar.com.muflar.R;
import muflar.com.muflar.helper.OnBackPressedListener;
import muflar.com.muflar.http.APIClient;
import muflar.com.muflar.http.APIInterface;
import muflar.com.muflar.model.Ride;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RideHistoryFragment extends Fragment implements OnBackPressedListener{
    FragmentManager fragmentManager;
    ListView listView;
    CustomAdapter customAdapter;

    List<Ride> rideList;
    APIInterface apiInterface;
    pl.droidsonroids.gif.GifImageView loadingDlg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ride_history, container, false);
        fragmentManager=getActivity().getSupportFragmentManager();
        listView=(ListView)view.findViewById(R.id.list_ride);
        loadingDlg = view.findViewById(R.id.loading_view);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        loadRideHistory();

        return view;
    }

    private void loadRideHistory() {
        loadingDlg.setVisibility(View.VISIBLE);

        Call<List<Ride>> call = apiInterface.getRideHistory();
        call.enqueue(new Callback<List<Ride>>() {
            @Override
            public void onResponse(Call<List<Ride>> call, Response<List<Ride>> response) {
                loadingDlg.setVisibility(View.GONE);
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<List<Ride>> call, Throwable t) {
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

    private void generateDataList(List<Ride> rideList) {
        this.rideList = rideList;

        customAdapter = new CustomAdapter();

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
            if(rideList == null)
                return 0;

            return rideList.size();
        }

        @Override
        public Object getItem(int position) {
            return rideList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_ride_history, parent, false);
                viewHolder = new ViewHolder();

                viewHolder.entryTV = (TextView) convertView.findViewById(R.id.entry_tv);
                viewHolder.exitTV = (TextView) convertView.findViewById(R.id.exit_tv);
                viewHolder.busRouteTV = (TextView) convertView.findViewById(R.id.busroute_tv);
                viewHolder.busIdTV = (TextView) convertView.findViewById(R.id.busid_tv);
                viewHolder.fareChargeTV = (TextView) convertView.findViewById(R.id.fare_charge_tv);
                viewHolder.travelDateTV = (TextView) convertView.findViewById(R.id.travel_date_tv);
                viewHolder.entryTimeTV = (TextView) convertView.findViewById(R.id.entry_time_tv);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Ride item = rideList.get(position);
            if(item != null) {
                viewHolder.entryTV.setText(item.entry);
                viewHolder.exitTV.setText(item.exit);
                viewHolder.busRouteTV.setText(item.busRoute);
                viewHolder.busIdTV.setText(item.busID);
                viewHolder.fareChargeTV.setText(item.fareCharged);
                viewHolder.travelDateTV.setText(item.travelDate);
                viewHolder.entryTimeTV.setText(item.entryTime);
            }

            return convertView;
        }

        private class ViewHolder {
            public TextView entryTV;
            public TextView exitTV;
            public TextView busRouteTV;
            public TextView busIdTV;
            public TextView fareChargeTV;
            public TextView travelDateTV;
            public TextView entryTimeTV;

        }
    }
}
