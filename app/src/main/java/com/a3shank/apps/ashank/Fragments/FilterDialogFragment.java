package com.a3shank.apps.ashank.Fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.a3shank.apps.ashank.Activites.ListItemsActivity;
import com.a3shank.apps.ashank.Application_config.myApplication;
import com.a3shank.apps.ashank.R;
import com.a3shank.apps.ashank.models.ConstantsAshank;

import static com.a3shank.apps.ashank.Activites.ListItemsActivity.categorie;

/**
 * Created by Mahmoud on 6/19/2016.
 */
public class FilterDialogFragment extends DialogFragment {

RadioButton filter_all, filter_new;
    /**
     * Public static constructor that creates fragment and
     * passes a bundle with data into it when adapter is created
     */
    public static FilterDialogFragment newInstance() {
        FilterDialogFragment addListDialogFragment = new FilterDialogFragment();
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
        View rootView = inflater.inflate(R.layout.dialog_filter, null);
        Typeface myTypeface = Typeface.createFromAsset(getActivity().getAssets(), "arabic2.otf");
        filter_all = (RadioButton) rootView.findViewById(R.id.filter_all);
        filter_all.setTypeface(myTypeface);
//        filter_near = (RadioButton) rootView.findViewById(R.id.filter_near);
        filter_new = (RadioButton) rootView.findViewById(R.id.filter_new);
        filter_new.setTypeface(myTypeface);
        filter_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListItemsActivity.myClientsDatabase = myApplication.getDatabaseReference().child(ConstantsAshank.FIREBASE_LOCATION_CLIENTS).orderByChild("catagorie").startAt(categorie);
                ListItemsActivity.recyclering(ListItemsActivity.myClientsDatabase,"");
                ListItemsActivity.adapter.notifyDataSetChanged();
            }
        });
        filter_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListItemsActivity.myClientsDatabase = myApplication.getDatabaseReference().child(ConstantsAshank.FIREBASE_LOCATION_CLIENTS).orderByChild("timestampCreated");
                ListItemsActivity.recyclering(ListItemsActivity.myClientsDatabase,"");
                ListItemsActivity.adapter.notifyDataSetChanged();
            }
        });

        /**
         * Call  when user taps "Done" keyboard action
         */
//        mEditTextListName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
//                if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
//                    addShoppingList();
//                }
//                return true;
//            }
//        });

        /* Inflate and set the layout for the dialog */
        /* Pass null as the parent view because its going in the dialog layout*/
        builder.setView(rootView)
                /* Add action buttons */
                .setPositiveButton("Apply Filter", new DialogInterface.OnClickListener() {
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


            /* Close the dialog fragment */
        FilterDialogFragment.this.getDialog().cancel();

    }

}
