package com.a3shank.apps.ashank.Fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.a3shank.apps.ashank.Activites.DecriptionsActivity;
import com.a3shank.apps.ashank.Activites.MainActivity;
import com.a3shank.apps.ashank.Application_config.myApplication;
import com.a3shank.apps.ashank.R;
import com.a3shank.apps.ashank.models.ConstantsAshank;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Sadokey on 12/30/2016.
 */

public class GetCodeDialogFragment extends DialogFragment {
    /**
     * Public static constructor that creates fragment and
     * passes a bundle with data into it when adapter is created
     */
    DatabaseReference myClientDatabase;
    View textCard;
    ImageView ImageCard;
    static String fre;
    public static GetCodeDialogFragment newInstance(String free) {
        GetCodeDialogFragment addListDialogFragment = new GetCodeDialogFragment();
        Bundle bundle = new Bundle();
        addListDialogFragment.setArguments(bundle);
        fre = free;
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
        View rootView = inflater.inflate(R.layout.dialog_buy, null);
        if (fre.equals("free")){
            rootView.findViewById(R.id.codeImage).setVisibility(View.VISIBLE);
        }else {
            rootView.findViewById(R.id.buytxt_desc).setVisibility(View.VISIBLE);
        }
        myClientDatabase = myApplication.getDatabaseReference().child(ConstantsAshank.FIREBASE_LOCATION_CLIENTS);
        textCard = rootView.findViewById(R.id.buytxt_desc);
        ImageCard = (ImageView) rootView.findViewById(R.id.codeImage);

        /**
         * Call  when user taps "Done" keyboard action
         */

        /* Inflate and set the layout for the dialog */
        /* Pass null as the parent view because its going in the dialog layout*/
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


    public void addShoppingList() {

//        myClientDatabase.child("activeCodeNum").setValue(DecriptionsActivity.model.getActiveCodeNum() - 1);
        textCard.setVisibility(View.GONE);
        ImageCard.setVisibility(View.VISIBLE);
        ConstantsAshank.FREECODE = false;
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
            /* Close the dialog fragment */
//        GetCodeDialogFragment.this.getDialog().cancel();

    }
}