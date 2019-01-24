package com.a3shank.apps.ashank.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.a3shank.apps.ashank.Activites.DecriptionsActivity;
import com.a3shank.apps.ashank.Activites.ListItemsActivity;
import com.a3shank.apps.ashank.R;
import com.a3shank.apps.ashank.models.Client;
import com.a3shank.apps.ashank.models.ConstantsAshank;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Sadokey on 12/20/2016.
 */

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.postViewHolder> {

    private List<Client> model;
    Context mContext;
    private int lastPosition = -1;
    String activity;
//    Typeface myTypeface;
    public ItemsAdapter(Context mContext, List<Client> model) {
        this.model = model;
        this.mContext = mContext;
        this.activity = activity;
//        myTypeface = Typeface.createFromAsset(mContext.getAssets(), "arabic2.otf");
    }

    @Override
    public postViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_clients_card, parent, false);

        return new postViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(postViewHolder viewHolder, final int position) {
        /*MainModel item = mainModels.get(position);
        Bitmap image = BitmapFactory.decodeResource(mContext.getResources(),
                item.getPhoto());
        holder.imageView.setImageBitmap(image);
        setAnimation(holder.itemsView, position);
        holder.itemsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.equals("main")) {
                    CategoriesActivity.categorie_num = position;
                    Intent n = new Intent(mContext, CategoriesActivity.class);
                    n.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(n);

                } else {
                    ListItemsActivity.categorie = ConstantsAshank.MAIN_CATEGORIES[position];
                    Intent n = new Intent(mContext, ListItemsActivity.class);
                    n.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(n);
                }

            }
        });*/
        setAnimation(viewHolder.myView, position);
        viewHolder.setName(model.get(position).getFirstName() + " " + model.get(position).getLastName());
//        viewHolder.setResume(model.get(position).getDesc());
//        viewHolder.setEmail(model.get(position).getEmail());
        viewHolder.setProfileImage(model.get(position).getProfileImage(),position);
//        viewHolder.setSeeMore(model.get(position));
        if (ListItemsActivity.mLastLocation != null) {
            viewHolder.setDistance(model.get(position).getLat(), model.get(position).getLang());
        }
//        viewHolder.setDate(model.get(position).getTimestampCreatedLong());
//        if (ConstantsAshank.FREECODE) {
//            viewHolder.setFreeCodeNum(model.get(position).getActiveCodeNum());
//        }
        viewHolder.myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DecriptionsActivity.class);
                DecriptionsActivity.model = model.get(position);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
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
            TextView txt_title = (TextView) myView.findViewById(R.id.txt_clientName);
            txt_title.setText(Title);
//            txt_title.setTypeface(myTypeface);
        }
//
//        void setResume(String Desc) {
//            TextView txt_resume = (TextView) myView.findViewById(R.id.txt_item_description);
//            txt_resume.setText(Desc);
//            txt_resume.setTypeface(myTypeface);
//        }

//        void setEmail(String Desc) {
//            TextView txt_Email = (TextView) myView.findViewById(R.id.txt_item_email);
//            txt_Email.setText(Desc);
//        }

        void setProfileImage(String profileImage, final int position) {
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
//
//        void setDate(long date) {
//            TextView txt_Date = (TextView) myView.findViewById(R.id.txt_item_date);
//            txt_Date.setText(ConstantsAshank.SIMPLE_DATE_FORMAT.format(
//                    new Date(date)));
//        }

        public void setDistance(double lat, double lang) {
            TextView txt_Date = (TextView) myView.findViewById(R.id.txt_item_distance);
            Location latLng = new Location("client");
            latLng.setLatitude(lat);
            latLng.setLongitude(lang);
            int n = (int) ListItemsActivity.mLastLocation.distanceTo(latLng);
            txt_Date.setText((int) ListItemsActivity.mLastLocation.distanceTo(latLng) + "M");
//            txt_Date.setTypeface(myTypeface);
        }

//        public void setFreeCodeNum(int freeCodeNum) {
//            TextView txt_code = (TextView) myView.findViewById(R.id.txt_item_code);
//            txt_code.setVisibility(View.VISIBLE);
//            txt_code.setText(freeCodeNum);
//            txt_code.setTypeface(myTypeface);
//        }

    }
}
