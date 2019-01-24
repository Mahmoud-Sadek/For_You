package com.a3shank.apps.ashank.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.a3shank.apps.ashank.Activites.AboutUsActivity;
import com.a3shank.apps.ashank.Activites.HistoryActivity;
import com.a3shank.apps.ashank.Activites.LoginActivity;
import com.a3shank.apps.ashank.Activites.MainActivity;
import com.a3shank.apps.ashank.Activites.NotificationsActivity;
import com.a3shank.apps.ashank.Activites.ProfileUserActivity;
import com.a3shank.apps.ashank.Adapters.DrawerAdapter;
import com.a3shank.apps.ashank.Application_config.myApplication;
import com.a3shank.apps.ashank.Fragments.FreeCodeDialogFragment;
import com.a3shank.apps.ashank.R;
import com.a3shank.apps.ashank.models.ConstantsAshank;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import static com.a3shank.apps.ashank.Activites.MainActivity.freeDialog;


/**
 * Created by Mahmoud Sadek on 4/21/2017.
 */

public class Drawer {
    static Context context;
    View v;
    public static Activity activity;
    DatabaseReference myUserDatabase;
    public static FirebaseAuth auth;
    private FlowingDrawer mDrawer;
    public static String activityName = "";

    public Drawer(Context context, View v, Activity activity, String activityName) {
        this.context = context;
        this.v = v;
        this.activity = activity;
        this.activityName = activityName;
        auth = FirebaseAuth.getInstance();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String UID = sp.getString(ConstantsAshank.USER_ID, auth.getCurrentUser().getUid());
        ConstantsAshank.CURRENT_USER_ID = UID;
        myUserDatabase = myApplication.getDatabaseReference().child(ConstantsAshank.FIREBASE_LOCATION_USERS).child(UID);
    }

    public void makeDrawer(){
        if (!MainActivity.largeScreen) {
            mDrawer = (FlowingDrawer) v.findViewById(R.id.drawerlayout);
            mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        }
        v.findViewById(R.id.imageView_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileUserActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        ///////////////////////////////
        RecyclerView mDrawerListView = (RecyclerView) v.findViewById(R.id.drawerList);
        DrawerAdapter drawerAdapter = new DrawerAdapter(context);
        mDrawerListView.setHasFixedSize(true);
        mDrawerListView.setLayoutManager(new GridLayoutManager(context, 1));
        mDrawerListView.setAdapter(drawerAdapter);
       /* DrawerAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 5) {
                    showChangeLangDialog();
                }
                if (position == 4) {
//                    DialogFragment dialog = AboutUsDialogFragment.newInstance();
//                    dialog.show(MainActivity.this.getFragmentManager(), "AboutUsDialogFragment");
                    Intent intent = new Intent(context, AboutUsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                if (position == 1) {
                    Intent intent = new Intent(context, NotificationsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                if (position == 0) {
                    if (!activityName.equals("MainActivity")) {
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }
                }
                if (position == 3) {
                    Intent intent = new Intent(context, HistoryActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                if (position == 2) {

                    if (!activityName.equals("MainActivity")) {
                        freeDialog = true;
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }
                    else {
                        freeDialog();
                    }
//                    FlowingDrawer mDrawer = (FlowingDrawer) v.findViewById(R.id.drawerlayout);
//                    mDrawer.closeMenu();
//                    DialogFragment dialog = FreeCodeDialogFragment.newInstance(context);
//                    dialog.show(activity.getFragmentManager(), "FreeCodeDialogFragment");
                }
                if (position == 6) {
                    auth.signOut();
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    activity.finish();
                }


            }
        });*/

        if (auth.getCurrentUser() != null) {
            myUserDatabase.keepSynced(true);
            myUserDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("profileImage")) {
                        ImageView Profile_imageView = (ImageView) v.findViewById(R.id.imageView_user);
                        String imgUrl = dataSnapshot.child("profileImage").getValue(String.class);
                        Picasso.with(context)
                                .load(imgUrl)
                                .error(R.drawable.profile)         // optional
                                .into(Profile_imageView);
                    }
                    if (dataSnapshot.hasChild("firstName")) {
                        TextView userName = (TextView) v.findViewById(R.id.userName);
                        userName.setText(dataSnapshot.child("firstName").getValue(String.class));
                        ConstantsAshank.CURRENT_USER_NAME = dataSnapshot.child("firstName").getValue(String.class) +
                                dataSnapshot.child("lastName").getValue(String.class);
//                        userName.setTypeface(myTypeface);
                    }
                    if (dataSnapshot.hasChild("email")) {
                        TextView userEmail = (TextView) v.findViewById(R.id.textView_email);
                        userEmail.setText(dataSnapshot.child("email").getValue(String.class));
//                        userEmail.setTypeface(myTypeface);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public static void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.language_dialog, null);
        dialogBuilder.setView(dialogView);

        final Spinner spinner1 = (Spinner) dialogView.findViewById(R.id.spinner1);

        dialogBuilder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                int langpos = spinner1.getSelectedItemPosition();
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
                SharedPreferences.Editor spe = sp.edit();
                switch (langpos) {
                    case 0: //English
                        PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("LANG", "en").commit();
                        spe.putString(ConstantsAshank.LANG, "en").apply();
                        spe.commit();
                        setLangRecreate("en");
                        return;
                    case 1: //Arabic
                        PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("LANG", "ar").commit();
                        spe.putString(ConstantsAshank.LANG, "ar").apply();
                        spe.commit();
                        setLangRecreate("ar");
                        return;
                    default: //By default set to english
                        PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("LANG", "en").commit();
                        spe.putString(ConstantsAshank.LANG, "en").apply();
                        spe.commit();
                        setLangRecreate("en");
                        return;
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }
    public static void setLangRecreate(String langval) {
        Configuration config = context.getResources().getConfiguration();
        Locale locale = new Locale(langval);
        Locale.setDefault(locale);
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        activity.recreate();
    }
}
