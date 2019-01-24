package com.a3shank.apps.ashank.Activites;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.a3shank.apps.ashank.Adapters.MainListAdapter;
import com.a3shank.apps.ashank.Application_config.myApplication;
import com.a3shank.apps.ashank.Fragments.FreeCodeDialogFragment;
import com.a3shank.apps.ashank.R;
import com.a3shank.apps.ashank.models.Advertise;
import com.a3shank.apps.ashank.models.ConstantsAshank;
import com.a3shank.apps.ashank.models.MainModel;
import com.a3shank.apps.ashank.utils.Drawer;
import com.a3shank.apps.ashank.utils.NotificationUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.DrawableBanner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.views.BannerSlider;

import static com.a3shank.apps.ashank.models.ConstantsAshank.langCheck1;
import static com.a3shank.apps.ashank.utils.CheckNetwork.isConnected;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
//        , BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    public static boolean freeDialog = false;
    private FirebaseAuth auth;
    DatabaseReference myUserDatabase;
    private static FlowingDrawer mDrawer;
    static View content;
    static Context mContext;
    static FragmentManager fragmentManager;
    //    static SliderLayout mDemoSlider;
    static FloatingActionButton fabOutFreeCode;
    public static boolean largeScreen;
    BroadcastReceiver mRegistrationBroadcastReceiver;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.animation1, R.anim.animation2);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        content = findViewById(R.id.content);
        fabOutFreeCode = (FloatingActionButton) findViewById(R.id.fab_out_free_code);
        fabOutFreeCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outFreeCode();
            }
        });
        mContext = getBaseContext();
        fragmentManager = getFragmentManager();
        ///////////////
        setNotification();
        checkScreeen();
        View v = findViewById(R.id.drawerlayout);
        Drawer drawer = new Drawer(getBaseContext(), v, MainActivity.this, "MainActivity");
        drawer.makeDrawer();
        if (!largeScreen) {
            mDrawer = (FlowingDrawer) findViewById(R.id.drawerlayout);
            mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        }
        setupToolbar();
        toolbaSetAction();

//        EnableGPSIfPossible();
/////////////////user&lang//////////////
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String UID = sp.getString(ConstantsAshank.USER_ID, auth.getCurrentUser().getUid());
        ConstantsAshank.CURRENT_USER_ID = UID;
        myUserDatabase = myApplication.getDatabaseReference().child(ConstantsAshank.FIREBASE_LOCATION_USERS).child(UID);
        String lang = sp.getString(ConstantsAshank.LANG, "en");
        if (langCheck1) {
            langCheck1 = false;
            if (lang.equals("en")) {
                SharedPreferences.Editor spe = sp.edit();
                spe.putString(ConstantsAshank.LANG, lang).apply();
                spe.commit();
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "en").commit();
                setLangRecreate("en");
            } else {
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "ar").commit();
                setLangRecreate("ar");
            }
        }
        ///////////////////////////////
        int imgs[] = {R.drawable.adv, R.drawable.adv, R.drawable.preview, R.drawable.free_code};
        DemoSliderM(imgs);
        setMainList();

    }

    private void toolbaSetAction() {
        findViewById(R.id.search_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), SearchActivity.class));
            }
        });
        findViewById(R.id.profilrImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), ProfileUserActivity.class));
            }
        });
    }

    private void setNotification() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(ConstantsAshank.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(ConstantsAshank.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(ConstantsAshank.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

//                    txtMessage.setText(message);
                }
            }

        };

        displayFirebaseRegId();
    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(ConstantsAshank.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        Log.e("Firebase", "Firebase reg id: " + regId);

//        if (!TextUtils.isEmpty(regId))
//            txtRegId.setText("Firebase Reg Id: " + regId);
//        else
//            txtRegId.setText("Firebase Reg Id is not received yet!");
    }


    private void checkScreeen() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
//        size.x > size.y ||
        if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE || screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            largeScreen = true;
        } else {
            largeScreen = false;
        }
        fragmentTransaction.commit();

    }


    public static void outFreeCode() {
        ConstantsAshank.FREECODE = false;
        content.setBackgroundResource(android.R.color.transparent);
//        mDemoSlider.setVisibility(View.VISIBLE);
        fabOutFreeCode.setVisibility(View.GONE);
    }

    private void EnableGPSIfPossible() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);
                    }
                });
        builder.setCancelable(false);
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            EnableGPSIfPossible();
        }
    }


    public static void freeCode() {
        ConstantsAshank.FREECODE = true;
        content.setBackgroundResource(R.drawable.celebrate);
//        mDemoSlider.setVisibility(View.GONE);
        fabOutFreeCode.setVisibility(View.VISIBLE);
    }

    public static void freeDialog() {
        if (isConnected(mContext)) {
            if (!largeScreen) {
                mDrawer.closeMenu();
            }
            DialogFragment dialog = FreeCodeDialogFragment.newInstance(mContext);
            dialog.show(fragmentManager, "FreeCodeDialogFragment");
        } else {
            Toast.makeText(mContext, "Check Network Connection!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setMainList() {
        RecyclerView mMainRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_main);
        mMainRecyclerView.setHasFixedSize(true);
        mMainRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        int imgs[] = {R.drawable.c1, R.drawable.c2, R.drawable.c3, R.drawable.c4, R.drawable.c5,
                R.drawable.c6, R.drawable.c7, R.drawable.c8, R.drawable.c9, R.drawable.c11, R.drawable.c10, R.drawable.c12};
        String catName[] = getResources().getStringArray(R.array.categorie_array);
        List<MainModel> mainModels = new ArrayList<MainModel>();
        for (int i = 0; i < imgs.length; i++) {
            mainModels.add(new MainModel(imgs[i], catName[i]));
        }
        MainListAdapter adapter = new MainListAdapter(getBaseContext(), mainModels);
        mMainRecyclerView.setAdapter(adapter);

    }

    protected void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (!MainActivity.largeScreen) {
            toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawer.toggleMenu();
                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        if (!MainActivity.largeScreen) {
            if (mDrawer.isMenuVisible()) {
                mDrawer.closeMenu();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
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

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ConstantsAshank.FREECODE) {
            freeCode();
        }
        if (freeDialog) {
            freeDialog();
            freeDialog = false;
        } else {
            outFreeCode();
        }

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(ConstantsAshank.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(ConstantsAshank.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    public void setLangRecreate(String langval) {
        Configuration config = getBaseContext().getResources().getConfiguration();
        Locale locale = new Locale(langval);
        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        recreate();
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns >= 2 ? noOfColumns : 2;
    }


    private void DemoSliderM(int imgs[]) {
        BannerSlider bannerSlider = (BannerSlider) findViewById(R.id.banner_slider1);
        //add banner using image url
        bannerSlider.addBanner(new RemoteBanner("http://static.egypt.travel/2016/08/IbnTulun.jpg"));
        bannerSlider.addBanner(new RemoteBanner("http://susanshain.com/wp-content/uploads/2012/04/DSC_0496_mini.jpg"));
        bannerSlider.addBanner(new DrawableBanner(R.drawable.preview));

        //add banner using resource drawable
//        bannerSlider.addBanner(new DrawableBanner(R.drawable.yourDrawable));

    }
//        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
//
////        url_maps.put("Women tourism", "http://susanshain.com/wp-content/uploads/2012/04/DSC_0496_mini.jpg");
////        url_maps.put("Mosque", "http://static.egypt.travel/2016/08/IbnTulun.jpg");
//        getAdvertisment();
//
//        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
//        file_maps.put("Ashank", imgs[0]);
//        file_maps.put("عشانك", imgs[1]);
//        file_maps.put("اعلان", imgs[2]);
//        file_maps.put("wow", imgs[3]);
//
//
//        for (String name : file_maps.keySet()) {
//            TextSliderView textSliderView = new TextSliderView(getBaseContext());
//            // initialize a SliderLayout
//            textSliderView
//                    .description(name)
//                    .image(file_maps.get(name))
//                    .setScaleType(BaseSliderView.ScaleType.Fit)
//                    .setOnSliderClickListener(this);
//
//            //add your extra information
//            textSliderView.bundle(new Bundle());
//            textSliderView.getBundle()
//                    .putString("extra", name);
//
//            mDemoSlider.addSlider(textSliderView);
//        }
//        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
//        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
//        mDemoSlider.setDuration(4000);
//        // load the animation
//        BaseAnimationInterface anim = new BaseAnimationInterface() {
//            @Override
//            public void onPrepareCurrentItemLeaveScreen(View current) {
//                // load the animation
//                Animation animFadein = AnimationUtils.loadAnimation(getBaseContext(),
//                        R.anim.animation);
//
//                animFadein.setDuration(2000);
//                current.startAnimation(animFadein);
//            }
//
//            @Override
//            public void onPrepareNextItemShowInScreen(View next) {
//
//            }
//
//            @Override
//            public void onCurrentItemDisappear(View view) {
//
//            }
//
//            @Override
//            public void onNextItemAppear(View view) {
//            }
//        };
//
//        mDemoSlider.setCustomAnimation(anim);
//        mDemoSlider.addOnPageChangeListener(this);
//    }
//
//    @Override
//    public void onSliderClick(BaseSliderView slider) {
//    }
//
//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//    }
//
//    @Override
//    public void onPageSelected(int position) {
//        Log.d("Slider Demo", "" + position);
//    }
//
//    @Override
//    public void onPageScrollStateChanged(int state) {
//    }

    public void getAdvertisment() {
        Query qmyDatabase = myApplication.getDatabaseReference().child(ConstantsAshank.FIREBASE_ADVERTIS);
        qmyDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, String> url_maps = new HashMap<String, String>();
//                HashMap<String, Advertise> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, Advertise>>() {
//                });
//                if (results != null) {
//                    List<Advertise> advertiseList = new ArrayList<Advertise>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Advertise advertise = postSnapshot.getValue(Advertise.class);
//                        detail.setKey(postSnapshot.getKey());
                    url_maps.put(advertise.getKey(), advertise.getUrl());
                }
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
//                } else {
//                    Toast.makeText(MainActivity.this, "No Data Loaded", Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
