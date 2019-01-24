package com.a3shank.apps.ashank.Activites;

import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.a3shank.apps.ashank.Adapters.ItemsAdapter;
import com.a3shank.apps.ashank.Adapters.SearchAdapter;
import com.a3shank.apps.ashank.Application_config.myApplication;
import com.a3shank.apps.ashank.R;
import com.a3shank.apps.ashank.models.Client;
import com.a3shank.apps.ashank.models.ConstantsAshank;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    MaterialSearchView searchView;
    public static Query myClientsDatabase;
    private DatabaseReference myDatabase;
    public static ItemsAdapter adapter;
    static List<Client> clientList;
    private static RecyclerView searchRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(true);
        searchView.setHintTextColor(R.color.colorPrimary);
        searchView.setCursorDrawable(R.drawable.custom_cursor);

        myDatabase = myApplication.getDatabaseReference();
        myClientsDatabase = myDatabase.child(ConstantsAshank.FIREBASE_LOCATION_CLIENTS);
        myClientsDatabase.keepSynced(true);
        searchRecyclerView = (RecyclerView) findViewById(R.id.recycleView_ItemsSecond);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        searchRecyclerView.setLayoutManager(mLayoutManager);
        searchRecyclerView.addItemDecoration(new SearchActivity.GridSpacingItemDecoration(2, dpToPx(10), true));
        searchRecyclerView.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(100);
        itemAnimator.setRemoveDuration(100);
        searchRecyclerView.setItemAnimator(itemAnimator);
        adapter = new ItemsAdapter(getBaseContext(), null);

        recyclering(myClientsDatabase, "");

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                recyclering(myClientsDatabase, query);
                adapter.notifyDataSetChanged();searchView.hideKeyboard(searchView);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                recyclering(myClientsDatabase, query);
                adapter.notifyDataSetChanged();
                searchView.focusSearch(View.FOCUS_UP);
                return true;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
            }

            @Override
            public void onSearchViewClosed() {
                finish();
            }
        });
    }

    public void recyclering(Query qmyDatabase, final String search) {

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
                        } else clientList.add(client);
                    }
                    adapter = new ItemsAdapter(SearchActivity.this, clientList);
                    searchRecyclerView.setAdapter(adapter);
                    searchView.setAdapter(new SearchAdapter(getBaseContext(), clientList));
                } else {
                    Toast.makeText(SearchActivity.this, "No Data Loaded", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.items_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        searchView.setVoiceSearch(true);
        searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.showSearch();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_search) {
            findViewById(R.id.search_view).setVisibility(View.VISIBLE);
        }
        return super.onOptionsItemSelected(item);
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
}
