package com.a3shank.apps.ashank.Activites;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.a3shank.apps.ashank.Application_config.myApplication;
import com.a3shank.apps.ashank.R;
import com.a3shank.apps.ashank.models.ConstantsAshank;
import com.a3shank.apps.ashank.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileUserActivity extends AppCompatActivity {

    User model;
    DatabaseReference myUserDatabase;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.animation1, R.anim.animation2);
        setContentView(R.layout.activity_profile_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String UID = sp.getString(ConstantsAshank.USER_ID, auth.getCurrentUser().getUid());
        myUserDatabase = myApplication.getDatabaseReference().child(ConstantsAshank.FIREBASE_LOCATION_USERS).child(UID);


        if (auth.getCurrentUser() != null) {
            myUserDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    model = dataSnapshot.getValue(User.class);
                    if (model!=null) {
//                        Typeface myTypeface = Typeface.createFromAsset(getBaseContext().getAssets(), "arabic2.otf");
                        ImageView profile = (ImageView) findViewById(R.id.userImg);
                        TextView userName = (TextView) findViewById(R.id.userName);
//                        userName.setTypeface(myTypeface);
                        TextView userEmail = (TextView) findViewById(R.id.user_mail);
//                        userEmail.setTypeface(myTypeface);
//                        TextView userBirth = (TextView) findViewById(R.id.birthdate_txt);
//                        userBirth.setTypeface(myTypeface);
//                        TextView userGender = (TextView) findViewById(R.id.gender_txt);
//                        userGender.setTypeface(myTypeface);
                        userName.setText(model.getFirstName()+" "+ model.getLastName());
//                        userBirth.setText(model.getBirthday());
                        userEmail.setText(model.getEmail());
//                        userGender.setText(model.getGender());
                        Picasso.with(ProfileUserActivity.this)
                                .load(model.getProfileImage())
                                .error(R.drawable.person_icon)         // optional
                                .into(profile);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

}
