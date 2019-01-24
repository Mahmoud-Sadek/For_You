package com.a3shank.apps.ashank.Activites;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a3shank.apps.ashank.Application_config.myApplication;
import com.a3shank.apps.ashank.Fragments.FreeCodeDialogFragment;
import com.a3shank.apps.ashank.Fragments.GetCodeDialogFragment;
import com.a3shank.apps.ashank.R;
import com.a3shank.apps.ashank.models.Client;
import com.a3shank.apps.ashank.models.ConstantsAshank;
import com.a3shank.apps.ashank.models.History;
import com.a3shank.apps.ashank.models.ItemsClient;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.zzt.library.BezelImageView;
import com.zzt.library.BooheeScrollView;
import com.zzt.library.BuildLayerLinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ss.com.bannerslider.banners.DrawableBanner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.views.BannerSlider;

import static com.a3shank.apps.ashank.Activites.MainActivity.freeDialog;
import static com.a3shank.apps.ashank.utils.CheckNetwork.isConnected;

public class DecriptionsActivity extends AppCompatActivity implements
//        BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener,
        NavigationView.OnNavigationItemSelectedListener {

    public static String Code = "";
//    private SliderLayout mDemoSlider;
    public static Client model;
//    Typeface myTypeface;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    DatabaseReference myClientDatabase;
    private DatabaseReference myItemsClientDatabase;
    Query qmyItemsClientDatabase;
    FloatingActionButton fabOutFreeCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.animation1, R.anim.animation2);
        setContentView(R.layout.activity_decriptions);

//        myTypeface = Typeface.createFromAsset(getBaseContext().getAssets(), "arabic2.otf");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(model.getFirstName() + " " + model.getLastName());

        DemoSliderM();
        myClientDatabase = myApplication.getDatabaseReference().child(ConstantsAshank.FIREBASE_LOCATION_CLIENTS);
        myItemsClientDatabase = myApplication.getDatabaseReference().child(ConstantsAshank.FIREBASE_LOCATION_ITEMS_CLIENTS);
        qmyItemsClientDatabase = myItemsClientDatabase.orderByKey().equalTo(model.getClientId());

        fabOutFreeCode = (FloatingActionButton) findViewById(R.id.fab_out_free_code);
        fabOutFreeCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                freeDialog = false;
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        TextView date = (TextView) findViewById(R.id.date_txtView);
//        date.setTypeface(myTypeface);
        TextView description = (TextView) findViewById(R.id.description);
//        description.setTypeface(myTypeface);
        ImageView profileImg = (ImageView) findViewById(R.id.profilrImg);
        TextView name = (TextView) findViewById(R.id.name);
//        name.setTypeface(myTypeface);
        name.setText(model.getFirstName() + " " + model.getLastName());
        date.setText(model.getCatagorie());
//        ConstantsAshank.SIMPLE_DATE_FORMAT.format(
//                new Date(model.getTimestampCreatedLong()))
        description.setText(model.getDesc());
        Picasso.with(this)
                .load(model.getProfileImage())
                .error(R.drawable.ashank)         // optional
                .into(profileImg);
        View chatView = findViewById(R.id.chat_view);
        chatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatActivity.model = model;
                startActivity(new Intent(DecriptionsActivity.this, ChatActivity.class));
            }
        });
        View shareView = findViewById(R.id.share_view);
        shareView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "حمل عشانك الان واستمتع بافضل الهدايا والكروت المجانية");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, model.getFirstName() + " " + model.getLastName() + "\n" + model.getProfileImage());
                startActivity(Intent.createChooser(sharingIntent, "3shank"));
            }
        });
        View callView = findViewById(R.id.call_view);
        callView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = model.getPhone();
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + number));
                startActivity(callIntent);
            }
        });
        View mapView = findViewById(R.id.map_view);
        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(getBaseContext(), MapsActivity.class);
                MapsActivity.clientLatLng = new LatLng(model.getLat(), model.getLang());
                MapsActivity.clientName =  model.getFirstName();
                startActivity(n);
            }
        });
        /*View buyView = findViewById(R.id.buy_view);
        buyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = GetCodeDialogFragment.newInstance("order");
                dialog.show(DecriptionsActivity.this.getFragmentManager(), "GetCodeDialogFragment");

            }
        });*/
        NavigationView navigationView1 = (NavigationView) findViewById(R.id.nav_view);

        navigationView1.setNavigationItemSelectedListener(this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        setDrawerData();
        setItemsContent();
    }

    BooheeScrollView booheeScrollView;
    BuildLayerLinearLayout buildLayerLinearLayout;
    int width;
    int height;
    //    List<CardView> cardView;
//    CardView[] cv;
    BezelImageView[] imageView1;

    private void setItemsContent() {

        booheeScrollView = (BooheeScrollView) findViewById(R.id.horizon);
        buildLayerLinearLayout = (BuildLayerLinearLayout) findViewById(R.id.buildLayer);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = (displayMetrics.widthPixels - 30) / 3;
        height = (int) (width / 340f * 600);
        int viewwidth = 30;

        View view = new View(this);
        view.setLayoutParams(new LinearLayout.LayoutParams(viewwidth, 0));
        buildLayerLinearLayout.addView(view);
        qmyItemsClientDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, ItemsClient> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, ItemsClient>>() {
                });
                if (results != null) {
                    List<ItemsClient> items = new ArrayList<>(results.values());
                    imageView1 = new BezelImageView[items.size()];
                    for (int i = 0; i < items.size(); i++) {


                        imageView1[i] = new BezelImageView(getBaseContext());
                        imageView1[i].setLayoutParams(new LinearLayout.LayoutParams(width, height));
                        buildLayerLinearLayout.addView(imageView1[i]);
                        imageView1[i].setScaleType(ImageView.ScaleType.FIT_XY);
//                        imageView1.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.bg_messages, width, height));
                        imageView1[i].setMaskDrawable(getResources().getDrawable(R.drawable.roundrect));
                        Picasso.with(getBaseContext())
                                .load(items.get(i).getItemPoto())
                                .error(R.drawable.searh_btn)         // optional
                                .into(imageView1[i]);
                        imageView1[i].setContentDescription(items.get(i).getItemName());
                    }
                    booheeScrollView.setChildViews(imageView1);
                } else {
                    Toast.makeText(getBaseContext(), "No Data Loaded", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setDrawerData() {
        ImageView profileImg = (ImageView) findViewById(R.id.imgClient);
        TextView activeCodes = (TextView) findViewById(R.id.txt_activecode);
        TextView resume = (TextView) findViewById(R.id.txt_clientresume);
        activeCodes.setText(model.getActiveCodeNum() + "");
        resume.setText(model.getResume());
        Picasso.with(this)
                .load(model.getProfileImage())
                .error(R.drawable.ashank)         // optional
                .into(profileImg);
        TextView name = (TextView) findViewById(R.id.txt_clientname);
        TextView email = (TextView) findViewById(R.id.txt_clientemail);
        name.setText(model.getFirstName());
        email.setText(model.getEmail());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ConstantsAshank.FREECODE) {
            findViewById(R.id.content_description).setBackgroundResource(R.drawable.celebrate);
            findViewById(R.id.freeCode_btn).setVisibility(View.VISIBLE);
            fabOutFreeCode.setVisibility(View.VISIBLE);
            findViewById(R.id.freeCode_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    DialogFragment dialog = FreeCodeDialogFragment.newInstance(getBaseContext());
//                    dialog.show(DecriptionsActivity.this.getFragmentManager(), "FreeCodeDialogFragment");
                    if (isConnected(getBaseContext())){
                        doeCode();
                    }else {
                        Toast.makeText(DecriptionsActivity.this, "Check Network Connection!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.descreption_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (id == R.id.cv_mnu) {
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            } else {
                drawer.openDrawer(GravityCompat.END);
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    private void DemoSliderM() {

        BannerSlider bannerSlider = (BannerSlider) findViewById(R.id.banner_slider1);
        //add banner using image url
        bannerSlider.addBanner(new RemoteBanner(model.getProfileImage()));
//        bannerSlider.addBanner(new RemoteBanner(model.()));
//        bannerSlider.addBanner(new DrawableBanner(R.drawable.preview));
/*
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);

        HashMap<String, String> url_maps = new HashMap<String, String>();

//        HashMap<String, String> file_maps = new HashMap<String, String>();
        url_maps.put(model.getFirstName(), model.getProfileImage());
//        url_maps.put(model.getLastName(), model.getURL_Photos());
//        url_maps.put(model.getLastName(), model.getURL_Photos());


        for (String name : url_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(getBaseContext());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);
            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(getBaseContext(), slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "" + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
*/
    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Description Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public void doeCode() {
        if (model.getActiveCodeNum() == 0) {
            Toast.makeText(getBaseContext(), "Sorry " + model.getFirstName() + " Not Available Now", Toast.LENGTH_LONG).show();
            return;
        }
        if (Code.equals("")) {
            DialogFragment dialog = FreeCodeDialogFragment.newInstance(getBaseContext());
            dialog.show(DecriptionsActivity.this.getFragmentManager(), "FreeCodeDialogFragment");
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(DecriptionsActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            DatabaseReference myClientHistoryDatabase = myApplication.getDatabaseReference().child(ConstantsAshank.FIREBASE_LOCATION_HISTORY).child(ConstantsAshank.CURRENT_USER_ID);
            History history = new History(Code);
            myClientHistoryDatabase.push().setValue(history)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
                            /* Make a map for the removal */
                                HashMap<String, Object> updatedRemoveItemMap = new HashMap<String, Object>();

        /* Remove the item by passing null */
                                updatedRemoveItemMap.put(DecriptionsActivity.model.getClientId() + "/" + "activeCodeNum", DecriptionsActivity.model.getActiveCodeNum() - 1);
        /* Do the update */
                                myClientDatabase.updateChildren(updatedRemoveItemMap);
                                HashMap<String, Object> removeItemMap = new HashMap<String, Object>();

        /* Remove the item by passing null */
                                removeItemMap.put(DecriptionsActivity.Code + "", null);
        /* Do the update */
                                DatabaseReference myCodesDatabase = myApplication.getDatabaseReference().child(ConstantsAshank.FIREBASE_LOCATION_FREECODES);
                                myCodesDatabase.updateChildren(removeItemMap);
                                progressDialog.cancel();
                                DialogFragment dialog = GetCodeDialogFragment.newInstance("free");
                                dialog.show(DecriptionsActivity.this.getFragmentManager(), "GetCodeDialogFragment");

//                            myClientDatabase.child(ConstantsAshank.CURRENT_USER_ID).child("activeCodeNum").updateChildren(DecriptionsActivity.model.getActiveCodeNum() - 1);

                            } else {
                                Toast.makeText(getBaseContext(), "Error ", Toast.LENGTH_SHORT).show();
                            }
//                                /* Close the dialog fragment */
//                        FreeCodeDialogFragment.this.getDialog().cancel();
                        }
                    });

        }
    }
}