package com.a3shank.apps.ashank.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.a3shank.apps.ashank.Activites.CategoriesActivity;
import com.a3shank.apps.ashank.R;
import com.a3shank.apps.ashank.models.MainModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Sadokey on 12/20/2016.
 */

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.MyViewHolder> {

    private List<MainModel> mainModels;
    Context mContext;
    private int lastPosition = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;
        View itemsView;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView_item_main);
            textView = (TextView) view.findViewById(R.id.textView_item_main );
            itemsView = view.findViewById(R.id.item_view);
        }

        public void clearAnimation() {
            itemsView.clearAnimation();
        }

    }


    public MainListAdapter(Context mContext, List<MainModel> mainModels) {
        this.mainModels = mainModels;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_first_caegory, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final MainModel item = mainModels.get(holder.getAdapterPosition());
        Picasso.with(mContext).load(item.getPhoto()).into(holder.imageView);
        holder.textView.setText(item.getCategorie());
        setAnimation(holder.itemsView, position);
        holder.itemsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoriesActivity.categorie_num = holder.getAdapterPosition();
                CategoriesActivity.categorie_photo = item.getPhoto();
                CategoriesActivity.categorie_name = item.getCategorie();
                Intent n = new Intent(mContext, CategoriesActivity.class);
                n.addFlags(FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(n);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mainModels.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated

        if (position > lastPosition) {

            Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.slide_down : R.anim.slide_up);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }


    }

    //to solve the problem of fast scroll
    @Override
    public void onViewDetachedFromWindow(MyViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ((MyViewHolder) holder).clearAnimation();
    }

}
