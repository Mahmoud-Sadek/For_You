package com.a3shank.apps.ashank.Activites;

import android.Manifest;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Rect;
import android.location.Location;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.a3shank.apps.ashank.Adapters.ItemsAdapter;
import com.a3shank.apps.ashank.Application_config.myApplication;
import com.a3shank.apps.ashank.Fragments.FilterDialogFragment;
import com.a3shank.apps.ashank.R;
import com.a3shank.apps.ashank.models.Client;
import com.a3shank.apps.ashank.models.ConstantsAshank;
import com.a3shank.apps.ashank.utils.Drawer;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.a3shank.apps.ashank.Activites.MainActivity.freeDialog;

public class ListItemsActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        NavigationView.OnNavigationItemSelectedListener {

    public static String categorie = "";
    public static String category_name = "";
    private static RecyclerView newsRecyclerView;
    private DatabaseReference myDatabase;
    public static Query myClientsDatabase;
    private FirebaseAuth myAuth;
    static Context activity;
    static String post_id;
    public static ProgressBar progressBar;
    private GoogleApiClient mGoogleApiClient;
    public static Location mLastLocation;
    //    FirebaseRecyclerAdapter<Client, ListItemsActivity.postViewHolder> firebaseRecyclerAdapter;
    MaterialSearchView searchView;
    static List<Client> clientList;
    public static ItemsAdapter adapter;
    //    Typeface myTypeface;
    public static int categorie_photo;
    FloatingActionButton fabOutFreeCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.animation1, R.anim.animation2);
        setContentView(R.layout.activity_list_items);
//        myTypeface = Typeface.createFromAsset(getBaseContext().getAssets(), "arabic2.otf");
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(categorie);
        initCollapsingToolbar();
        activity = ListItemsActivity.this;
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
        myDatabase = myApplication.getDatabaseReference();
        myClientsDatabase = myDatabase.child(ConstantsAshank.FIREBASE_LOCATION_CLIENTS).orderByChild("catagorie").equalTo(category_name);

        myClientsDatabase.keepSynced(true);
        myAuth = FirebaseAuth.getInstance();

        View v = findViewById(R.id.drawerlayout);
        Drawer drawer = new Drawer(getBaseContext(), v, ListItemsActivity.this, "");
        drawer.makeDrawer();

        newsRecyclerView = (RecyclerView) findViewById(R.id.recycleView_ItemsSecond);
//        newsRecyclerView.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
//        linearLayoutManager.setReverseLayout(true);
//        newsRecyclerView.setLayoutManager(linearLayoutManager);
//        linearLayoutManager.setStackFromEnd(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        newsRecyclerView.setLayoutManager(mLayoutManager);
        newsRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        newsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(100);
        itemAnimator.setRemoveDuration(100);
        newsRecyclerView.setItemAnimator(itemAnimator);
        adapter = new ItemsAdapter(getBaseContext(), null);
        recyclering(myClientsDatabase, "");
//        newsRecyclerView.setAdapter(firebaseRecyclerAdapter);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
//        setSearch();
        if (ConstantsAshank.FREECODE) {
            findViewById(R.id.content).setBackgroundResource(R.drawable.celebrate);
        }
        progressBar = (ProgressBar) findViewById(R.id.loading_spinner);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
//        searchView.setHintTextColor(R.color.colorPrimary);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                recyclering(myClientsDatabase, query);
                adapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                recyclering(myClientsDatabase, query);
                adapter.notifyDataSetChanged();
                return true;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
            }

            @Override
            public void onSearchViewClosed() {
            }
        });
        searchView.setVoiceSearch(true);

    }

    public static void recyclering(Query qmyDatabase, final String search) {

        qmyDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Client> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, Client>>() {
                });
                if (results != null) {
                    clientList = new ArrayList<Client>();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Client client = postSnapshot.getValue(Client.class);
                        if (!search.equals("")) {
                            if (client.getFirstName().contains(search) || client.getLastName().contains(search) ||
                                    client.getDesc().contains(search) || client.getEmail().contains(search)
                                    || client.getResume().contains(search)) {
                                clientList.add(client);
                            }
                        } else {
                            clientList.add(client);
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                    adapter = new ItemsAdapter(activity, clientList);
                    newsRecyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(activity, "No Data Loaded", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ConstantsAshank.FREECODE) {
            findViewById(R.id.content).setBackgroundResource(R.drawable.celebrate);
            fabOutFreeCode.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            adapter.notifyDataSetChanged();
//            Toast.makeText(activity, String.valueOf(mLastLocation.getLatitude()), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.items_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        searchView.setVoiceSearch(true);
        searchView.setCursorDrawable(R.drawable.custom_cursor);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        if (id == R.id.action_filter) {
//            DialogFragment dialog = FilterDialogFragment.newInstance();
//            dialog.show(ListItemsActivity.this.getFragmentManager(), "FilterDialogFragment");
//        }
        if (id == R.id.action_search) {
            findViewById(R.id.search_view).setVisibility(View.VISIBLE);
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    public void initCollapsingToolbar() {
        try {
            Glide.with(this).load(categorie_photo).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView cat_name = (TextView) findViewById(R.id.catagory_name);
        cat_name.setText(categorie);
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
//        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            boolean isShow = false;
//            int scrollRange = -1;
//
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                if (scrollRange == -1) {
//                    scrollRange = appBarLayout.getTotalScrollRange();
//                }
//                if (scrollRange + verticalOffset == 0) {
//                    collapsingToolbar.setTitle(categorie);
//                    isShow = true;
//                } else if (isShow) {
//                    collapsingToolbar.setTitle(" ");
//                    isShow = false;
//                }
//            }
//        });
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

}
