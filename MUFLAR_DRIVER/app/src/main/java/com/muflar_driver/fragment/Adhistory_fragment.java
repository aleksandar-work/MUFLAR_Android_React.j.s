package com.muflar_driver.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.muflar_driver.R;

public class Adhistory_fragment extends Fragment {


    CustomAdapter customAdapter;
    FragmentManager fragmentManager;

    ListView listView;
    String routes[] = {"ABC123", "DEF456","GHI987","DEB981", "ABC123", "DEF456","GHI987","DEB981","ABC123", "DEF456","GHI987","DEB981"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view =inflater.inflate(R.layout.fragment_adhistory_fragment, container, false);
        fragmentManager=getActivity().getSupportFragmentManager();


        listView=(ListView)view.findViewById(R.id.listad_history);


        customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);


        final Context ctx = listView.getContext();
        int resId = R.anim.layout_animation_from_bottom;


        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(ctx, resId);
        listView.setLayoutAnimation(animation);

        return view;
    }



    private class CustomAdapter extends BaseAdapter {
        View v;

        @Override
        public int getCount() {
            return routes.length;
        }

        @Override
        public Object getItem(int position) {
            return routes.length;
        }

        @Override
        public long getItemId(int position) {


            return position;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            try {



                    LayoutInflater linf = getActivity().getLayoutInflater();
                    v = linf.inflate(R.layout.item_ad_history, parent, false);

                            final TextView textView_band_name = (TextView) v.findViewById(R.id.userid);
                            textView_band_name.setText(routes[position]);


            } catch (Exception e) {

            }
            return v;
        }

    }


}
