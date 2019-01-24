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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.a3shank.apps.ashank.R;
import com.a3shank.apps.ashank.models.Client;
import com.a3shank.apps.ashank.models.ConstantsAshank;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AddClientActivity extends AppCompatActivity {

    DatabaseReference mClientsDatabase;
    StorageReference mystorage;
    private EditText inputEmail, inputFirstName, inputLastName, inputResume, inputShortDesc, inputPhone;
    Spinner inputCatagorie;
    Button btnMap;
    public static double lat = 0, lang = 0;
    ImageView imgProfile;
    static Context mContext;
    static int GALARY_REQUEST1 = 1;
    Uri image_uri = null, image_uri2;
    ProgressDialog progressDialog;
    private String TAGFirebase = "fire";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);
        mClientsDatabase = FirebaseDatabase.getInstance().getReference();
        mystorage = FirebaseStorage.getInstance().getReference();
        mContext = getBaseContext();
        inputEmail = (EditText) findViewById(R.id.email);
        inputFirstName = (EditText) findViewById(R.id.fname);
        inputLastName = (EditText) findViewById(R.id.lname);
        inputResume = (EditText) findViewById(R.id.resume);
        inputShortDesc = (EditText) findViewById(R.id.shortdesc);
        imgProfile = (ImageView) findViewById(R.id.imageView_profileCompany);
        inputPhone = (EditText) findViewById(R.id.phone);
        inputCatagorie = (Spinner) findViewById(R.id.spinner_categ);
        btnMap = (Button) findViewById(R.id.btnMap);
//        imgDesription = (ImageView) findViewById(R.id.img_list);
        progressDialog = new ProgressDialog(AddClientActivity.this);

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMap = new Intent(mContext, ChooseLocationActivity.class);
                startActivity(intentMap);
            }
        });
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent galaryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                Intent galaryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galaryIntent.setType("image/*");
//                galaryIntent.putExtra("crop", "true");
//                galaryIntent.putExtra("aspectX", 0);
//                galaryIntent.putExtra("aspectY", 0);
//                try {
//                    galaryIntent.putExtra("return-data", true);
//                    startActivityForResult(Intent.createChooser(galaryIntent,"Complete action using"),     GALARY_REQUEST);
//
//                } catch (ActivityNotFoundException e) {
//                }
                startActivityForResult(galaryIntent, GALARY_REQUEST1);
            }
        });

//        imgDesription.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent galaryIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                galaryIntent.setType("image/*");
//                startActivityForResult(galaryIntent, GALARY_REQUEST2);
//            }
//        });

        Button addClientBtn = (Button) findViewById(R.id.btnAddClient);
        addClientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addClientList();
            }
        });


    }

    /**
     * Add new active list
     */


    public void addClientList() {
        if (!Validate()) {
            Toast.makeText(mContext, "الداتا مش صح", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setTitle("Add Client ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String firstName = inputFirstName.getText().toString().trim();
        String lastName = inputLastName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String resume = inputResume.getText().toString().trim();
        String desc = inputShortDesc.getText().toString().trim();
        String phone = inputPhone.getText().toString().trim();
        String catagorie = inputCatagorie.getSelectedItem().toString().trim();
        AddClientInFirebase(firstName, lastName, email, resume, desc, phone, catagorie);

    }

    public boolean Validate() {
        boolean valid = true;

        String firstName = inputFirstName.getText().toString().trim();
        String lastName = inputLastName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String resume = inputResume.getText().toString().trim();
        String phone = inputPhone.getText().toString().trim();
        String catagorie = inputCatagorie.getSelectedItem().toString();
        String desc = inputShortDesc.getText().toString().trim();

        if (firstName.isEmpty() || firstName.length() < 3) {
            inputFirstName.setError("at least 3 characters");
            valid = false;
            Toast.makeText(mContext, "firstName, Error Complete Data", Toast.LENGTH_SHORT).show();
        } else {
            inputFirstName.setError(null);
        }
        if (lastName.isEmpty() || lastName.length() < 3) {
            inputLastName.setError("at least 3 characters");
            Toast.makeText(mContext, "lastName, Error Complete Data", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            inputLastName.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("enter a valid email address");
            Toast.makeText(mContext, "email, Error Complete Data", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            inputEmail.setError(null);
        }
        if (resume.isEmpty()) {
            inputResume.setError("enter a resume");
            Toast.makeText(mContext, "resume,Error Complete Data", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            inputResume.setError(null);
        }
        if (desc.isEmpty()) {
            inputShortDesc.setError("enter a desc");
            Toast.makeText(mContext, "desc,Error Complete Data", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            inputShortDesc.setError(null);
        }
        if (phone.isEmpty()) {
            inputPhone.setError("enter a phone");
            Toast.makeText(mContext, "phone, Error Complete Data", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            inputPhone.setError(null);
        }
        if (catagorie.isEmpty()) {
            Toast.makeText(mContext, "catagorie, Error Complete Data", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
        }
        if (lat == 0 || lang == 0) {
            Toast.makeText(mContext, "lat, Error Complete Data", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
        }

        return valid;
    }


    private void AddClientInFirebase(final String firstName, final String lastName, final String email, final String resume, final String desc, final String phone, final String catagorie) {

        StorageReference filepath = mystorage.child("companyProfileImages").child("photos" + firstName + lastName);
        filepath.putFile(image_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    Uri downloaduri = task.getResult().getDownloadUrl();
//                    String u = mystorage.child("companyProfileImages").child("photos" + firstName + lastName + "/" + image_uri)
//                            .getDownloadUrl().getResult().toString();
                    AddClient(firstName, lastName, email, phone, resume, desc, downloaduri.toString(), catagorie);
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

    private void AddClient(String firstName, String lastName, String email, String phone, String resume, String desc, String profileImage, String catagorie) {
        HashMap<String, Object> timestampJoined = new HashMap<>();
        timestampJoined.put("timestamp", ServerValue.TIMESTAMP);
        DatabaseReference client_database = mClientsDatabase.child(ConstantsAshank.FIREBASE_LOCATION_CLIENTS);
        String clientId = client_database.push().getKey();
        Client client = new Client(firstName, lastName, email, phone, resume, desc, clientId, catagorie, profileImage, lat, lang, 20, timestampJoined);
        client_database.child(clientId).setValue(client)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mContext, "New Client Add Successfully", Toast.LENGTH_SHORT).show();
                               /* Close the dialog fragment */
//                                AddClientsDialogFragments.this.getDialog().cancel();
                        } else {
                            Toast.makeText(mContext, "Error : not add ", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }
//        addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Uri downloaduri = taskSnapshot.getDownloadUrl();
//                AddClientData(firstName,lastName, email, resume, downloaduri.toString());
//            }
//
//        });
//    }


    private void AddClientData(final String firstName, final String lastName, final String email, final String phone, final String
            resume, final String desc, final String profileImage, final String catagorie) {

        StorageReference filepath = mystorage.child("companyProfileImages").child(firstName + lastName);
        filepath.putFile(image_uri2).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    Uri downloaduri = task.getResult().getDownloadUrl();

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALARY_REQUEST1 && resultCode == Activity.RESULT_OK) {
            image_uri = data.getData();
            imgProfile.setImageURI(image_uri);

//                Bundle extras2 = data.getExtras();
//                if (extras2 != null) {
//                    Bitmap photo = extras2.getParcelable("data");
//                    imgProfile.setImageBitmap(photo);
//                }
        }
/*
        if (requestCode == GALARY_REQUEST2) {
            progressDialog.setTitle("Loading Image ...");
            progressDialog.show();
            image_uri2 = data.getData();
            imgDesription.setImageURI(image_uri2);

            progressDialog.dismiss();
//                Bundle extras2 = data.getExtras();
//                if (extras2 != null) {
//                    Bitmap photo = extras2.getParcelable("data");
//                    imgProfile.setImageBitmap(photo);
//                }
        }
*/
    }

}
