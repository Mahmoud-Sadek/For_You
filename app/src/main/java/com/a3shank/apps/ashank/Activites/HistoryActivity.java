package com.a3shank.apps.ashank.Activites;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.a3shank.apps.ashank.Application_config.myApplication;
import com.a3shank.apps.ashank.R;
import com.a3shank.apps.ashank.models.ConstantsAshank;
import com.a3shank.apps.ashank.models.History;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView newsRecyclerView;
    private DatabaseReference myDatabase;
    Query myClientsDatabase;
    private FirebaseAuth myAuth;
    static Context activity;
    static String post_id;
    ProgressDialog progressDialog;
    FirebaseRecyclerAdapter<History, HistoryActivity.postViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.animation1, R.anim.animation2);
        setContentView(R.layout.activity_history);
        myDatabase = myApplication.getDatabaseReference();
        myClientsDatabase = myDatabase.child(ConstantsAshank.FIREBASE_LOCATION_HISTORY).child(ConstantsAshank.CURRENT_USER_ID);
        myClientsDatabase.keepSynced(true);
        myAuth = FirebaseAuth.getInstance();

        newsRecyclerView = (RecyclerView) findViewById(R.id.recycleView_history);
        newsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        linearLayoutManager.setReverseLayout(true);
        newsRecyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(100);
        itemAnimator.setRemoveDuration(100);
        newsRecyclerView.setItemAnimator(itemAnimator);
        recyclering(myClientsDatabase);
    }

    private void recyclering(Query qmyDatabase) {
        progressDialog = new ProgressDialog(HistoryActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<History, HistoryActivity.postViewHolder>
                (History.class, R.layout.card_history, HistoryActivity.postViewHolder.class, qmyDatabase) {
            @Override
            protected void populateViewHolder(final HistoryActivity.postViewHolder viewHolder, final History model, final int position) {
                post_id = getRef(position).getKey();
                viewHolder.setCode(model.getCode());
                progressDialog.dismiss();
            }
        };
        newsRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class postViewHolder extends RecyclerView.ViewHolder {
        View myView;
        FirebaseAuth myAuth;

        public postViewHolder(View itemView) {
            super(itemView);
            myView = itemView;
            myAuth = FirebaseAuth.getInstance();
        }

        void setCode(String Title) {
            TextView txt_title = (TextView) myView.findViewById(R.id.code_history);
            txt_title.setText(Title);
        }

    }


}
