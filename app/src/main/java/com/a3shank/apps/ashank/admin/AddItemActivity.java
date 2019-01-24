package com.a3shank.apps.ashank.admin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.a3shank.apps.ashank.Adapters.ItemsAdapter;
import com.a3shank.apps.ashank.Application_config.myApplication;
import com.a3shank.apps.ashank.R;
import com.a3shank.apps.ashank.models.Client;
import com.a3shank.apps.ashank.models.ConstantsAshank;
import com.a3shank.apps.ashank.models.ItemsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class AddItemActivity extends AppCompatActivity {

    DatabaseReference mItemsClientDatabase;
    private EditText inputItemName, inputDescription;
    Spinner clientsSpiner;
    List<Client> clients;
    int GALARY_REQUEST1 = 1;
    Uri image_uri = null;
    ImageView imgDesription;
    ProgressDialog progressDialog;
    StorageReference mystorage;
    public static Query myClientsDatabase;
    Client myClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        mItemsClientDatabase = FirebaseDatabase.getInstance().getReference();
        mystorage = FirebaseStorage.getInstance().getReference();
        inputItemName = (EditText) findViewById(R.id.txt_item_name);
        inputDescription = (EditText) findViewById(R.id.txt_item_description);
        imgDesription = (ImageView) findViewById(R.id.img_itemdesc);
        progressDialog = new ProgressDialog(AddItemActivity.this);
        progressDialog.setCancelable(false);
        imgDesription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galaryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galaryIntent.setType("image/*");
                startActivityForResult(galaryIntent, GALARY_REQUEST1);
            }
        });
        Button addClientBtn = (Button) findViewById(R.id.btnAddItem);
        addClientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemClientList();
            }
        });

        myClientsDatabase = myApplication.getDatabaseReference().child(ConstantsAshank.FIREBASE_LOCATION_CLIENTS);

        clientsSpiner = (Spinner) findViewById(R.id.spinnerClients);
        recyclering(myClientsDatabase);
        clientsSpiner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                myClient = clients.get(i);
            }
        });


    }

    List<String> clientList;

    public void recyclering(Query qmyDatabase) {

        qmyDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Client> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, Client>>() {
                });
                if (results != null) {
                    clientList = new ArrayList<String>();
                    clients = new ArrayList<Client>();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Client client = postSnapshot.getValue(Client.class);
                        clientList.add(client.getFirstName());
                        clients.add(client);
                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, clientList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    clientsSpiner.setAdapter(dataAdapter);
                    myClient = clients.get(0);
                } else {
                    Toast.makeText(getBaseContext(), "No Data Loaded", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Add new active list
     */


    public void addItemClientList() {
        if (!Validate()) {
            return;
        }
        progressDialog.setTitle("Add Client ...");
        progressDialog.show();
        String itemName = inputItemName.getText().toString().trim();
        String description = inputDescription.getText().toString().trim();
        AddItemClientInFirebase(itemName, description);


    }

    public boolean Validate() {
        boolean valid = true;

        String itemName = inputItemName.getText().toString().trim();
        String description = inputDescription.getText().toString().trim();


        if (itemName.isEmpty() || itemName.length() < 3) {
            inputItemName.setError("at least 3 characters");
            valid = false;
        } else {
            inputItemName.setError(null);
        }
        if (description.isEmpty()) {
            inputDescription.setError("enter a Description");
            valid = false;
        } else {
            inputDescription.setError(null);
        }
        if (myClient == null) {
            Toast.makeText(this, "No client Choosed", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            inputDescription.setError(null);
        }
        return valid;
    }


    private void AddItemClientInFirebase(final String itemName, final String description) {

        StorageReference filepath = mystorage.child("companyProfileImages").child(RandomName());
        filepath.putFile(image_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    Uri downloaduri = task.getResult().getDownloadUrl();
                    AddItemData(itemName, description, downloaduri.toString());
                }
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i("Progress", String.format("onProgress: %5.2f MB transferred",
                        taskSnapshot.getBytesTransferred() / 1024.0 / 1024.0));
            }
        });


    }

    private void AddItemData(String itemName, String description, String imgUrl) {
        DatabaseReference itemsclient_database = mItemsClientDatabase.child(ConstantsAshank.FIREBASE_LOCATION_ITEMS_CLIENTS).child(myClient.getClientId());
        String itemsclientId = itemsclient_database.push().getKey();
        ItemsClient itemsClient = new ItemsClient(itemName, description, myClient.getClientId(), imgUrl);
        itemsclient_database.child(itemsclientId).setValue(itemsClient)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(getBaseContext(), "New item Add Successfully", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        } else {
                            Toast.makeText(getBaseContext(), "Error : not add ", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALARY_REQUEST1 && resultCode == Activity.RESULT_OK) {
            progressDialog.setTitle("Loading Image ...");
            progressDialog.show();
            image_uri = data.getData();
            imgDesription.setImageURI(image_uri);
            progressDialog.dismiss();
        }

    }

    private String RandomName() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        int length = random.nextInt(20);
        char temp;
        for (int i = 0; i < length; i++) {
            temp = (char) (random.nextInt(96) + 32);
            stringBuilder.append(temp);
        }
        return stringBuilder.toString();
    }

}
