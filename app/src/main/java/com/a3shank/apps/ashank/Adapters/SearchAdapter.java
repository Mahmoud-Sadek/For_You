package com.a3shank.apps.ashank.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.a3shank.apps.ashank.Activites.DecriptionsActivity;
import com.a3shank.apps.ashank.R;
import com.a3shank.apps.ashank.models.Client;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Mahmoud Sadek on 7/7/2017.
 */

public class SearchAdapter extends BaseAdapter {

    private List<Client> model;
    Context mContext;
    private int lastPosition = -1;
    String activity;

    //    Typeface myTypeface;
    public SearchAdapter(Context mContext, List<Client> model) {
        this.model = model;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return model.size();
    }

    @Override
    public Object getItem(int i) {
        return model.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_clients_card, viewGroup, false);
        }
        setProfileImage(model.get(i).getProfileImage(), i, view);
        setName(model.get(i).getFirstName() + " " + model.get(i).getLastName(), view);
        return view;
    }

    void setName(String Title, View myView) {
        TextView txt_title = (TextView) myView.findViewById(R.id.txt_clientName);
        txt_title.setText(Title);
    }

    void setProfileImage(String profileImage, final int position, View myView) {
        ImageView img_profile = (ImageView) myView.findViewById(R.id.img_client);
        Picasso.with(mContext)
                .load(profileImage)
                .error(R.drawable.searh_btn)         // optional
                .into(img_profile);
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DecriptionsActivity.class);
                DecriptionsActivity.model = model.get(position);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

    }
}
