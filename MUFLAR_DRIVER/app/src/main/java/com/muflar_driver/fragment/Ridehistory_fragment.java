package com.muflar_driver.fragment;

import android.content.Context;
import android.net.Uri;
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

import com.muflar_driver.R;
import com.muflar_driver.http.APIClient;
import com.muflar_driver.http.APIInterface;
import com.muflar_driver.model.Ride;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Ridehistory_fragment extends Fragment {

    CustomAdapter customAdapter;
    FragmentManager fragmentManager;

    ListView listView;

    List<Ride> rideList;
    APIInterface apiInterface;
    MKLoader loadingDlg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view= inflater.inflate(R.layout.fragment_ridehistory_fragment, container, false);

        fragmentManager=getActivity().getSupportFragmentManager();

        listView=(ListView)view.findViewById(R.id.list_ridehistory);
        loadingDlg = view.findViewById(R.id.loading_dlg);

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

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setPositiveButton("Ok", null);
                alertDialogBuilder.setTitle("Error");
                alertDialogBuilder.setMessage("Connection failed.");

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
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

    private class CustomAdapter extends BaseAdapter {
        View v;

        @Override
        public int getCount() {
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

                viewHolder.cardIdTV = (TextView) convertView.findViewById(R.id.card_id_tv);
                viewHolder.fareChargeTV = (TextView) convertView.findViewById(R.id.fare_charge_tv);
                viewHolder.travelDateTV = (TextView) convertView.findViewById(R.id.travel_date_tv);
                viewHolder.entryTimeTV = (TextView) convertView.findViewById(R.id.entry_time_tv);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Ride item = rideList.get(position);
            if(item != null) {
                viewHolder.cardIdTV.setText(item.cardID);
                viewHolder.fareChargeTV.setText(item.fareCharged);
                viewHolder.travelDateTV.setText(item.travelDate);
                viewHolder.entryTimeTV.setText(item.entryTime);
            }

            return convertView;
        }

    }

    private class ViewHolder {
        public TextView cardIdTV;
        public TextView fareChargeTV;
        public TextView travelDateTV;
        public TextView entryTimeTV;

    }

}
