package com.muflar_driver.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.muflar_driver.model.PaymentItem;
import com.muflar_driver.model.Topup;
import com.tuyenmonkey.mkloader.MKLoader;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Paymenthistory_fragment extends Fragment {

    MKLoader loadingDlg;
    APIInterface apiInterface;

    CustomAdapter customAdapter;
    List<PaymentItem>   paymentHistoryList;
    ListView listView;

    String mBusId;

    String[] monthName = {"January", "February",
            "March", "April", "May", "June", "July",
            "August", "September", "October", "November",
            "December"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_paymenthistory_fragment, container, false);

        loadingDlg = view.findViewById(R.id.loading_dlg);
        listView = view.findViewById(R.id.list_payment_history);

        mBusId = "S98765432";

        apiInterface = APIClient.getClient().create(APIInterface.class);
        loadPaymentHistory(mBusId);

        return view;
    }

    private void loadPaymentHistory(String busId) {
        //loadingDlg.setVisibility(View.VISIBLE);

        Call<List<PaymentItem>> call = apiInterface.getPaymentHistory(busId);
        call.enqueue(new Callback<List<PaymentItem>>() {
            @Override
            public void onResponse(Call<List<PaymentItem>> call, Response<List<PaymentItem>> response) {
                //loadingDlg.setVisibility(View.GONE);
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<List<PaymentItem>> call, Throwable t) {
               //loadingDlg.setVisibility(View.GONE);
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

    private void generateDataList(List<PaymentItem> list) {
        paymentHistoryList = list;
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
            return paymentHistoryList.size();
        }

        @Override
        public Object getItem(int position) {
            return paymentHistoryList.size();
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Paymenthistory_fragment.ViewHolder viewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_payment_history_item, parent, false);
                viewHolder = new Paymenthistory_fragment.ViewHolder();

                viewHolder.topUpAmountTV = (TextView) convertView.findViewById(R.id.topup_tv);
                viewHolder.amountTV = (TextView) convertView.findViewById(R.id.amount_tv);
                viewHolder.rideAmountTV = (TextView) convertView.findViewById(R.id.ride_amount_tv);
                viewHolder.totalAmountTV = (TextView) convertView.findViewById(R.id.total_amount_tv);
                viewHolder.weekTV = (TextView) convertView.findViewById(R.id.week_tv);
                viewHolder.monthTV = (TextView) convertView.findViewById(R.id.month_tv);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (Paymenthistory_fragment.ViewHolder) convertView.getTag();
            }

            PaymentItem item = paymentHistoryList.get(position);
            String preMonth = "";
            if (position>1){
                 PaymentItem preItem = paymentHistoryList.get(position - 1);
                 Calendar cal = getCalendarOfDate(item.DateStart);
                 int wk = cal.get(Calendar.WEEK_OF_MONTH);
                 String month = monthName[cal.get(Calendar.MONTH)];
            }
            if(item != null) {
                Calendar cal = getCalendarOfDate(item.DateStart);
                int wk = cal.get(Calendar.WEEK_OF_MONTH);
                String month = monthName[cal.get(Calendar.MONTH)];

                viewHolder.topUpAmountTV.setText(item.TopUpAmount);
                viewHolder.amountTV.setText(item.MFCAmount);
                viewHolder.rideAmountTV.setText(item.RideAmount);
                viewHolder.totalAmountTV.setText(item.TopUpAmount);
                viewHolder.weekTV.setText("week" + wk);
                viewHolder.monthTV.setText(month);

                if (!preMonth.equals(month)) {
                    viewHolder.monthTV.setVisibility(View.VISIBLE);
                }
            }

            return convertView;
        }
    }

    private class ViewHolder {
        public TextView topUpAmountTV;
        public TextView rideAmountTV;
        public TextView amountTV;
        public TextView totalAmountTV;
        public TextView weekTV;
        public TextView monthTV;
    }

    private Calendar getCalendarOfDate(String dateStr){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setMinimalDaysInFirstWeek(1);

        return cal;
    }
}
