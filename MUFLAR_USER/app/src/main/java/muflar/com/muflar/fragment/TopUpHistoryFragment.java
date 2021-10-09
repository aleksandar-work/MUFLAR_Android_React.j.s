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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.tuyenmonkey.mkloader.MKLoader;

import org.w3c.dom.Text;

import java.util.List;

import muflar.com.muflar.R;
import muflar.com.muflar.activity.Demo;
import muflar.com.muflar.helper.OnBackPressedListener;
import muflar.com.muflar.http.APIClient;
import muflar.com.muflar.http.APIInterface;
import muflar.com.muflar.model.BusRoute;
import muflar.com.muflar.model.Ride;
import muflar.com.muflar.model.Topup;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TopUpHistoryFragment extends Fragment implements OnBackPressedListener {
    FragmentManager fragmentManager;
    ListView listView;
    CustomAdapter customAdapter;

    List<Topup> topupList;

    APIInterface apiInterface;

    pl.droidsonroids.gif.GifImageView loadingDlg;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_topup_history, container, false);
        fragmentManager=getActivity().getSupportFragmentManager();

        listView=(ListView)view.findViewById(R.id.list_topup);
        loadingDlg = view.findViewById(R.id.loading_view);

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

                new MaterialDialog.Builder(getContext())
                        .title("Error")
                        .content("Connection Failed.")
                        .positiveText("Ok")
                        .show();

                return;
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


    @Override
    public void onBackPressed() {
        fragmentManager.beginTransaction().replace(R.id.frame_layout,new Home_Fragment()).commit();
    }
    private class CustomAdapter extends BaseAdapter {
        View v;

        @Override
        public int getCount() {
            if(topupList == null)
                return 0;

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

                viewHolder.busRouteTV = (TextView) convertView.findViewById(R.id.busroute_tv);
                viewHolder.busIdTV = (TextView) convertView.findViewById(R.id.busid_tv);
                viewHolder.amountTV = (TextView) convertView.findViewById(R.id.amount_tv);
                viewHolder.dateTV = (TextView) convertView.findViewById(R.id.date_tv);
                viewHolder.timeTV = (TextView) convertView.findViewById(R.id.time_tv);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Topup item = topupList.get(position);
            if(item != null) {
                viewHolder.busRouteTV.setText(item.busRoute);
                viewHolder.busIdTV.setText(item.busID);
                viewHolder.amountTV.setText(item.amount);
                viewHolder.dateTV.setText(item.rechargeDate);
                viewHolder.timeTV.setText(item.rechargeTime);
            }

            return convertView;
        }

        private class ViewHolder {
            public TextView busRouteTV;
            public TextView busIdTV;
            public TextView amountTV;
            public TextView dateTV;
            public TextView timeTV;

        }
    }
}
