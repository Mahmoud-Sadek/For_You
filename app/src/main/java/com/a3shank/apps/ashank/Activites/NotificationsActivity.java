package com.a3shank.apps.ashank.Activites;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.a3shank.apps.ashank.Adapters.OffersAdapter;
import com.a3shank.apps.ashank.Application_config.myApplication;
import com.a3shank.apps.ashank.R;
import com.a3shank.apps.ashank.models.Advertise;
import com.a3shank.apps.ashank.models.ConstantsAshank;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    static RecyclerView mClientsRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.animation1, R.anim.animation2);
        setContentView(R.layout.activity_notifications);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mClientsRecyclerView = (RecyclerView) findViewById(R.id.recycleView_items_offers);
        mClientsRecyclerView.setHasFixedSize(true);
        //Set RecyclerView type according to intent value
        mClientsRecyclerView.setLayoutManager(new GridLayoutManager(NotificationsActivity.this, 1));
        getOffers();
    }

    public void getOffers() {
        Query qmyDatabase = myApplication.getDatabaseReference().child(ConstantsAshank.FIREBASE_ADVERTIS);
        qmyDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, String> url_maps = new HashMap<String, String>();
                HashMap<String, Advertise> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, Advertise>>() {
                });
                if (results != null) {
                    List<Advertise> advertiseList = new ArrayList<Advertise>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Advertise advertise = postSnapshot.getValue(Advertise.class);
//                    url_maps.put(advertise.getKey(), advertise.getUrl());
                    advertiseList.add(advertise);
                }
                    OffersAdapter clientsAdapter = new OffersAdapter(getBaseContext(), advertiseList);
                    mClientsRecyclerView.setAdapter(clientsAdapter);
//                for (String name : url_maps.keySet()) {
//                    TextSliderView textSliderView = new TextSliderView(getBaseContext());
//                    // initialize a SliderLayout
//                    textSliderView
//                            .description(name)
//                            .image(url_maps.get(name))
//                            .setScaleType(BaseSliderView.ScaleType.Fit)
//                            .setOnSliderClickListener(MainActivity.this);
//
//                    //add your extra information
//                    textSliderView.bundle(new Bundle());
//                    textSliderView.getBundle()
//                            .putString("extra", name);
//                    mDemoSlider.addSlider(textSliderView);
//                }
                } else {
                    Toast.makeText(NotificationsActivity.this, "No Data Loaded", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
