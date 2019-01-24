package com.a3shank.apps.ashank.Fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.a3shank.apps.ashank.Activites.DecriptionsActivity;
import com.a3shank.apps.ashank.Application_config.myApplication;
import com.a3shank.apps.ashank.R;
import com.a3shank.apps.ashank.models.Client;
import com.a3shank.apps.ashank.models.ConstantsAshank;
import com.a3shank.apps.ashank.utils.Drawer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import static com.a3shank.apps.ashank.Activites.MainActivity.freeCode;

/**
 * Created by Mahmoud Sadek on 2/7/2017.
 */

public class FreeCodeDialogFragment extends DialogFragment {
    static Context mContext;
    /**
     * Public static constructor that creates fragment and
     * passes a bundle with data into it when adapter is created
     */
    DatabaseReference myClientDatabase, mFreeCodesDatabase, myClientHistoryDatabase;
    EditText textFreeCode;

    public static FreeCodeDialogFragment newInstance(Context context) {
        mContext = context;
        FreeCodeDialogFragment addListDialogFragment = new FreeCodeDialogFragment();
        Bundle bundle = new Bundle();
        addListDialogFragment.setArguments(bundle);
        return addListDialogFragment;
    }

    /**
     * Initialize instance variables with data from bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Open the keyboard automatically when the dialog fragment is opened
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.freecode_dialog, null);

        Typeface myTypeface = Typeface.createFromAsset(getActivity().getAssets(), "arabic2.otf");
        textFreeCode = (EditText) rootView.findViewById(R.id.txt_free_code);
        textFreeCode.setTypeface(myTypeface);
        myDatabase = myApplication.getDatabaseReference();
        myCodesDatabase = myDatabase.child(ConstantsAshank.FIREBASE_LOCATION_FREECODES);
        /**
         * Call  when user taps "Done" keyboard action
         */

        /* Inflate and set the layout for the dialog */
        /* Pass null as the parent view because its going in the dialog layout*/
        textFreeCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Dialog d = getDialog();
                if (d instanceof AlertDialog) {
                    AlertDialog dialog = (AlertDialog) d;
                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    if (s.length() < 5){
                        positiveButton.setEnabled(false);
                    }else {
                        positiveButton.setEnabled(true);
                    }

                }
            }
        });
        builder.setView(rootView)
                /* Add action buttons */
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        addShoppingList();
                    }
                });

        return builder.create();
    }

    /**
     * Add new active list
     */
    ProgressDialog progressDialog;

    public void addShoppingList() {
        String code = textFreeCode.getText().toString().trim();
        if (!code.equals("")) {
            DecriptionsActivity.Code = code;
            checkCode(code);

        } else {
            Toast.makeText(mContext, "Please Enter Code To Enjoy Free", Toast.LENGTH_SHORT).show();
        }

//        if () {

    }

    public static Query myCodesDatabase;
    private DatabaseReference myDatabase;

    private void checkCode(final String code) {

//        if (Drawer.activityName.equals("MainActivity")){
//            freeCode();
//        }
//        getActivity().findViewById(R.id.content).setBackgroundResource(R.drawable.background_main);

        myCodesDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, String> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, String>>() {
                });
                if (results != null) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String freeCode = postSnapshot.getValue(String.class);
                        if (freeCode.equals(code)) {
//                            getActivity().findViewById(R.id.content).setBackgroundResource(R.drawable.zena);
                            if (Drawer.activityName.equals("MainActivity")){
                                freeCode();
                                return;
                            }
//                            getActivity().findViewById(R.id.content).setBackgroundResource(R.drawable.celebrate);
                        }

                    }

                        Toast.makeText(mContext, "Please Enter Valid Code To Enjoy Free", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(mContext, "No Data Loaded", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}