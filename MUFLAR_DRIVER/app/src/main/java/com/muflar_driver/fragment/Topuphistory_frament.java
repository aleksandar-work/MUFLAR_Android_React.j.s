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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.muflar_driver.R;
import com.muflar_driver.http.APIClient;
import com.muflar_driver.http.APIInterface;
import com.muflar_driver.model.Topup;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Topuphistory_frament extends Fragment {

    CustomAdapter customAdapter;
    FragmentManager fragmentManager;

    ListView listView;

    List<Topup> topupList;

    APIInterface apiInterface;

    MKLoader loadingDlg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View  view= inflater.inflate(R.layout.fragment_topuphistory_frament, container, false);
        fragmentManager=getActivity().getSupportFragmentManager();

        listView=(ListView)view.findViewById(R.id.topup_list);
        loadingDlg = view.findViewById(R.id.loading_dlg);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        loadTopupHistory();

        return view;
    }

    private void loadTopupHistory() {
        loadingDlg.setVisibility(View.VISIBLE);

        Call<List<Topup>> call = apiInterface.getTopupHistory();
        call.enqueue(new Callback<List<Topup>>() {
            @Override
            public void onResponse(Call<List<Topup>> call, Response<List<Topup>> response) {
                loadingDlg.setVisibility(View.GONE);
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<List<Topup>> call, Throwable t) {
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

    private void generateDataList(List<Topup> topupList) {
        this.topupList = topupList;

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
            return topupList.size();
        }

        @Override
        public Object getItem(int position) {
            return topupList.size();
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_top_history, parent, false);
                viewHolder = new ViewHolder();

                viewHolder.cardIdTV = (TextView) convertView.findViewById(R.id.card_id_tv);
                viewHolder.amountTV = (TextView) convertView.findViewById(R.id.amount_tv);
                viewHolder.dateTV = (TextView) convertView.findViewById(R.id.date_tv);
                viewHolder.timeTV = (TextView) convertView.findViewById(R.id.time_tv);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Topup item = topupList.get(position);
            if(item != null) {
                viewHolder.cardIdTV.setText(item.cardID);
                viewHolder.amountTV.setText(item.amount);
                viewHolder.dateTV.setText(item.rechargeDate);
                viewHolder.timeTV.setText(item.rechargeTime);
            }

            return convertView;
        }
    }

    private class ViewHolder {
        public TextView cardIdTV;
        public TextView amountTV;
        public TextView dateTV;
        public TextView timeTV;

    }
}
