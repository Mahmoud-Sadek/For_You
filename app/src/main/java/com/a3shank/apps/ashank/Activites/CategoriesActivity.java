package com.a3shank.apps.ashank.Activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.a3shank.apps.ashank.Adapters.CategoriesAdapter;
import com.a3shank.apps.ashank.R;
import com.a3shank.apps.ashank.models.Categorys;
import com.a3shank.apps.ashank.models.ConstantsAshank;
import com.a3shank.apps.ashank.utils.Drawer;
import com.bumptech.glide.Glide;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.a3shank.apps.ashank.Activites.MainActivity.freeDialog;
import static com.a3shank.apps.ashank.models.ConstantsAshank.langCheck2;

public class CategoriesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static RecyclerView mCategoriesRecyclerView;
    static Context mContext;
    public static int categorie_num;
    public static int categorie_photo;
    public static String categorie_name;
    public static int pos1;
    int[] imgs;
    private FlowingDrawer mDrawer;
    FloatingActionButton fabOutFreeCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.animation1, R.anim.animation2);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String lang = sp.getString(ConstantsAshank.LANG, "en");
        if (langCheck2) {
            langCheck2 = false;
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
        setContentView(R.layout.activity_categories);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        setTitle(ConstantsAshank.MAIN_CATEGORIES[categorie_num]);
        initCollapsingToolbar();
        mContext = getBaseContext();

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
        if (!MainActivity.largeScreen) {
            mDrawer = (FlowingDrawer) findViewById(R.id.drawerlayout);
            mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        }
        View v = findViewById(R.id.drawerlayout);
        Drawer drawer = new Drawer(getBaseContext(), v, CategoriesActivity.this, "");
        drawer.makeDrawer();
        mCategoriesRecyclerView = (RecyclerView) findViewById(R.id.categories_recyclerview);
        mCategoriesRecyclerView.setHasFixedSize(true);
        //Set RecyclerView type according to intent value
        mCategoriesRecyclerView.setLayoutManager(new GridLayoutManager(CategoriesActivity.this, 1));
        steImages();
        setMainList(imgs);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ConstantsAshank.FREECODE) {
            findViewById(R.id.content).setBackgroundResource(R.drawable.celebrate);
            fabOutFreeCode.setVisibility(View.VISIBLE);
        }
    }

    private void steImages() {
        if (categorie_num == 0) {
            imgs = new int[]{R.drawable.c1_1, R.drawable.c1_2};
        } else if (categorie_num == 1) {
            imgs = new int[]{R.drawable.c2_1, R.drawable.c2_2, R.drawable.c2_3, R.drawable.c2_4};
        } else if (categorie_num == 2) {
            imgs = new int[]{R.drawable.c3_1, R.drawable.c3_2, R.drawable.c3_3};
        } else if (categorie_num == 3) {
            imgs = new int[]{R.drawable.c4};
        } else if (categorie_num == 4) {
            imgs = new int[]{R.drawable.c5};
        } else if (categorie_num == 5) {
            imgs = new int[]{R.drawable.c6_1, R.drawable.c6_2, R.drawable.c6_3, R.drawable.c6_4, R.drawable.c6_5};
        } else if (categorie_num == 6) {
            imgs = new int[]{R.drawable.c6};
        } else if (categorie_num == 7) {
            imgs = new int[]{R.drawable.c8_1, R.drawable.c8_2};
        } else if (categorie_num == 8) {
            imgs = new int[]{R.drawable.c9_1, R.drawable.c9_2};
        } else if (categorie_num == 9) {
            imgs = new int[]{R.drawable.c11};
        } else if (categorie_num == 10) {
            imgs = new int[]{R.drawable.c10};
        } else if (categorie_num == 11) {
            imgs = new int[]{R.drawable.c12};
        }
    }

    public void setMainList(int[] imgs) {
        List<Categorys> categoriesModel = new ArrayList<Categorys>();
        for (int i = 0; i < imgs.length; i++) {
            categoriesModel.add(new Categorys(getResources().getString(ConstantsAshank.MAIN_CATEGORIES[categorie_num][i]), imgs[i], ConstantsAshank.CATEGORIES_NAME[categorie_num][i]));
        }
//            MainListAdapter adapter = new MainListAdapter(mContext, categoriesModel, "");
        CategoriesAdapter adapters = new CategoriesAdapter(mContext, categoriesModel);
        mCategoriesRecyclerView.setAdapter(adapters);
    }

    public void initCollapsingToolbar() {
        try {
            Glide.with(this).load(categorie_photo).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView cat_name = (TextView) findViewById(R.id.catagory_name);
        cat_name.setText(categorie_name);
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(categorie_name);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    public void setLangRecreate(String langval) {
        Configuration config = getBaseContext().getResources().getConfiguration();
        Locale locale = new Locale(langval);
        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        recreate();
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
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
}
