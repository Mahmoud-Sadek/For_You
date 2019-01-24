package com.a3shank.apps.ashank.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.a3shank.apps.ashank.Activites.ListItemsActivity;
import com.a3shank.apps.ashank.R;
import com.a3shank.apps.ashank.models.Categorys;
import com.a3shank.apps.ashank.models.ConstantsAshank;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Mahmoud Sadek on 3/19/2017.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.postViewHolder> {

    private List<Categorys> model;
    Context mContext;

    private int lastPosition = -1;
//    Typeface myTypeface;
    ViewGroup parents;


    public CategoriesAdapter(Context mContext, List<Categorys> model) {
        this.model = model;
        this.mContext = mContext;
//        myTypeface = Typeface.createFromAsset(mContext.getAssets(), "arabic2.otf");
    }

    @Override
    public CategoriesAdapter.postViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        parents = parent;
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_categories, parent, false);

        return new CategoriesAdapter.postViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoriesAdapter.postViewHolder viewHolder, final int position) {

//        getView(position, viewHolder.myView, parents);
        setAnimation(viewHolder.myView, position);
        viewHolder.setName(model.get(position).getItem_name());
        viewHolder.setImage(model.get(position).getImg());
        viewHolder.myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListItemsActivity.categorie_photo = model.get(position).getImg();
                ListItemsActivity.categorie = model.get(position).getItem_name();
                ListItemsActivity.category_name = model.get(position).getName();
                Intent n = new Intent(mContext, ListItemsActivity.class);
                n.addFlags(FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(n);
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
        FirebaseAuth myAuth;

        public postViewHolder(View itemView) {
            super(itemView);
            myView = itemView;
            myAuth = FirebaseAuth.getInstance();
        }


        void setName(String Title) {
            TextView txt_title = (TextView) myView.findViewById(R.id.cat_name);
            txt_title.setText(Title);
//            txt_title.setTypeface(myTypeface);
        }


        void setImage(int profileImage) {
            ImageView img_profile = (ImageView) myView.findViewById(R.id.cat_img);
            Picasso.with(mContext)
                    .load(profileImage)
                    .error(R.drawable.ashank)         // optional
                    .into(img_profile);

        }

    }


}