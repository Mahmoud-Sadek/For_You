package com.a3shank.apps.ashank.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.a3shank.apps.ashank.R;
import com.a3shank.apps.ashank.models.Advertise;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Mahmoud Sadek on 3/19/2017.
 */

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.postViewHolder> {

    private List<Advertise> model;
    Context mContext;
    private int lastPosition = -1;


    public OffersAdapter(Context mContext, List<Advertise> model) {
        this.model = model;
        this.mContext = mContext;
    }

    @Override
    public postViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_offer, parent, false);

        return new postViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(postViewHolder viewHolder, final int position) {

        setAnimation(viewHolder.myView, position);
        viewHolder.advTxt.setText(model.get(position).getKey());
        Picasso.with(mContext).load(model.get(position).getUrl()).error(R.drawable.ashank).into(viewHolder.advImg);
        viewHolder.myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent n = new Intent(mContext, ProfileClientActivity.class);
//                n.addFlags(FLAG_ACTIVITY_NEW_TASK);
//                mContext.startActivity(n);
            }
        });
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated

        if (position > lastPosition) {

            Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.slide_down : R.anim.slide_up);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }

    }

    class postViewHolder extends RecyclerView.ViewHolder {
        View myView;
        ImageView advImg;
        TextView advTxt;
        public postViewHolder(View itemView) {
            super(itemView);
            myView = itemView;
            advImg = (ImageView) myView.findViewById(R.id.adv_img);
            advTxt = (TextView) myView.findViewById(R.id.adv_txt);
        }
    }
}