package com.a3shank.apps.ashank.Activites;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.a3shank.apps.ashank.R;
import com.a3shank.apps.ashank.models.ConstantsAshank;
import com.a3shank.apps.ashank.models.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Face";
    private FirebaseAuth myAuth;
    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    //    private ProgressBar progressBar;
    private TextView btnLogin;
    private DatabaseReference myUserDatabase;
    ProgressDialog progressDialog;
//    Typeface myTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getBaseContext());
        //Get Firebase auth instance
        overridePendingTransition(R.anim.animation1, R.anim.animation2);
        setContentView(R.layout.activity_login_customize);
        facebook();
//        myTypeface = Typeface.createFromAsset(getBaseContext().getAssets(), "arabic2.otf");
        auth = FirebaseAuth.getInstance();
        vedio();
        register();
//        DialogFragment dialog = FreeCodeDialogFragment.newInstance(getBaseContext());
//        dialog.show(LoginActivity.this.getFragmentManager(), "FreeCodeDialogFragment");
        myUserDatabase = FirebaseDatabase.getInstance().getReference();
        inputEmail = (EditText) findViewById(R.id.email_login);
        inputPassword = (EditText) findViewById(R.id.pswrd_login);
//        inputEmail.setTypeface(myTypeface);
//        inputPassword.setTypeface(myTypeface);
//        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnLogin = (TextView) findViewById(R.id.sin);
//        btnLogin.setTypeface(myTypeface);
//        TextView skip = (TextView) findViewById(R.id.back);
//        skip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this, MainActivity.class));
//            }
//        });
        myAuth = FirebaseAuth.getInstance();
        authenticatinChecker();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLogin();
            }
        });
        printKeyHash();
//        facebook();
    }

    FirebaseAuth.AuthStateListener mAuthStateListener;

    private void authenticatinChecker() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(LoginActivity.this, "u are in", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        myAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            myAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    private void mLogin() {

        String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Login Account...");
        progressDialog.setCancelable(false);
        progressDialog.show();
//        progressBar.setVisibility(View.VISIBLE);

        //authenticate user
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
//                        progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (password.length() < 6) {
                                inputPassword.setError("less than 6");
                            } else {
                                Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();
                        } else {
                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor spe = sp.edit();
                            spe.putString(ConstantsAshank.USER_ID, myAuth.getCurrentUser().getUid()).apply();
//                            spe.putString(Constants.USER_TYPE, Constants.USERS_TOURIST).apply();
                            spe.commit();
                            progressDialog.dismiss();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

    }

    /////////////////////// video//////////////////////

    private void vedio() {
        //Displays a video file.
        getWindow().setFormat(PixelFormat.UNKNOWN);
        VideoView mVideoView = (VideoView) findViewById(R.id.videoview);
        String uriPath = "android.resource://" + getPackageName() + "/" + R.raw.login_video;
        Uri uri = Uri.parse(uriPath);
        mVideoView.setVideoURI(uri);
        mVideoView.requestFocus();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        mVideoView.start();
    }

    ////////////////////////////// register //////////////////////////////////
    int DatePiker_id = 999;
    DatePicker datePicker;
    Calendar calendar;
    int year, month, day;
    private EditText inputSEmail, inputSPassword, inputSconfPassword, inputSFirstName, inputSLastName;
    private RadioButton radioMale, radioFemale;
    TextView txtbirth, btnSignUp;
    String gender = "", birthdate;
    int MAP_REQUEST = 2, GALARY_REQUEST1 = 1;
    ImageView imgProfile;
    StorageReference mystorage;
    Uri image_uri = Uri.parse("");
    //    private ProgressBar progressBar;
    private DatabaseReference mySUserDatabase;
    ProgressDialog progressDialogS;

    private void register() {
//        txtbirth.setTypeface(myTypeface);
        btnSignUp = (TextView) findViewById(R.id.sign_up_button);
//        btnSignUp.setTypeface(myTypeface);
        inputSEmail = (EditText) findViewById(R.id.email);
//        inputSEmail.setTypeface(myTypeface);
        inputSPassword = (EditText) findViewById(R.id.pswrd);
//        inputSPassword.setTypeface(myTypeface);
        inputSconfPassword = (EditText) findViewById(R.id.cnpswrd);
//        inputSconfPassword.setTypeface(myTypeface);
        inputSFirstName = (EditText) findViewById(R.id.fname);
//        inputSFirstName.setTypeface(myTypeface);
        inputSLastName = (EditText) findViewById(R.id.lname);
//        inputSLastName.setTypeface(myTypeface);
        radioMale = (RadioButton) findViewById(R.id.male);
//        radioMale.setTypeface(myTypeface);
        radioFemale = (RadioButton) findViewById(R.id.female);
//        radioFemale.setTypeface(myTypeface);

        imgProfile = (ImageView) findViewById(R.id.imageView_profileCompany);
        radioMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "Male";
            }
        });
        radioFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "Female";
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
//        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //Get Firebase auth instance
        myAuth = FirebaseAuth.getInstance();
        mySUserDatabase = FirebaseDatabase.getInstance().getReference();
        mystorage = FirebaseStorage.getInstance().getReference();

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        txtbirth = (TextView) findViewById(R.id.birth);
        txtbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DatePiker_id);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sinup();
            }
        });
    }

    private void sinup() {
        final String email = inputSEmail.getText().toString().trim();
        String password = inputSPassword.getText().toString().trim();
        final String firstName = inputSFirstName.getText().toString().trim();
        final String lastName = inputSLastName.getText().toString().trim();
        if (!validate()) {
            return;
        }
//        progressBar.setVisibility(View.VISIBLE);

        progressDialogS = new ProgressDialog(LoginActivity.this);
        progressDialogS.setIndeterminate(true);
        progressDialogS.setMessage("Creating Account...");
        progressDialogS.setCancelable(false);
        progressDialogS.show();
        //create user
        myAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    completeRegister(firstName, lastName, email);
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor spe = sp.edit();
                    spe.putString(ConstantsAshank.USER_ID, myAuth.getCurrentUser().getUid()).apply();
                    spe.commit();

                } else {
                    Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
//        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DatePiker_id) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Toast.makeText(LoginActivity.this, day + "/" + month + "/" + year, Toast.LENGTH_SHORT).show();
            txtbirth.setText(day + "/" + month + "/" + year);
        }
    };

    public boolean validate() {
        boolean valid = true;

        String firstName = inputSFirstName.getText().toString().trim();
        String lastName = inputSLastName.getText().toString().trim();
        String email = inputSEmail.getText().toString().trim();
        String password = inputSPassword.getText().toString().trim();
        String repassword = inputSconfPassword.getText().toString().trim();
        birthdate = txtbirth.getText().toString().trim();

        if (firstName.isEmpty() || firstName.length() < 3) {
            inputSFirstName.setError("at least 3 characters");
            Toast.makeText(LoginActivity.this, "First Name is not right", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            inputSFirstName.setError(null);
        }
        if (lastName.isEmpty() || lastName.length() < 3) {
            inputSLastName.setError("at least 3 characters");
            Toast.makeText(LoginActivity.this, "Last Name is not right", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            inputSLastName.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputSEmail.setError("enter a valid email address");
            Toast.makeText(LoginActivity.this, "Email is not right", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            inputSEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 7 || password.length() > 14) {
            inputSPassword.setError("between 7 and 14 alphanumeric characters");
            Toast.makeText(LoginActivity.this, "Password is not right", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            inputSPassword.setError(null);
        }
        if (!repassword.equals(password)) {
            inputSPassword.setError("password not match!");
            Toast.makeText(LoginActivity.this, "password not match", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            inputSPassword.setError(null);
        }
        if (image_uri.getPath().equals("")) {
            Toast.makeText(LoginActivity.this, "Choose Your Image First", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (gender.equals("")) {
            Toast.makeText(LoginActivity.this, "Choose Your Gender First", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (birthdate.equals("Birthday")) {
            Toast.makeText(LoginActivity.this, "Choose Your Birthdate First", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        return valid;
    }

    private void completeRegister(final String firstName, final String lastName, final String email) {

        StorageReference filepath = mystorage.child("UsersProfileImages").child(RandomName());
        filepath.putFile(image_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    Uri downloaduri = task.getResult().getDownloadUrl();
                    AddUserData(firstName, lastName, email, gender, downloaduri.toString(), txtbirth.getText().toString());
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

    private void AddUserData(String firstName, String lastName, String email, String gender, String photo, String birthdate) {
        User user = new User(firstName, lastName, email, gender, photo, birthdate);
        String user_id = myAuth.getCurrentUser().getUid();
        DatabaseReference user_database = mySUserDatabase.child(ConstantsAshank.FIREBASE_LOCATION_USERS).child(user_id);
        user_database.setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor spe = sp.edit();
                            spe.putString(ConstantsAshank.USER_ID, myAuth.getCurrentUser().getUid()).apply();
                            spe.commit();
                            Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                            progressDialogS.dismiss();
                            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(mainIntent);
                            finish();
                        } else {
                            progressDialogS.dismiss();
                            Toast.makeText(LoginActivity.this, "Error : not add ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALARY_REQUEST1 && resultCode == Activity.RESULT_OK) {
            image_uri = data.getData();
            imgProfile.setImageURI(image_uri);
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
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

    //facebook


    private CallbackManager mCallbackManager;

    private void facebook() {

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.button_facebook_login);
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile", "user_birthday"));
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                progressDialogS = new ProgressDialog(LoginActivity.this);
                progressDialogS.setIndeterminate(true);
                progressDialogS.setMessage("Please Wait...");
                progressDialogS.setCancelable(false);
                progressDialogS.show();
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                progressDialogS.dismiss();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });
    }

    private void handleFacebookAccessToken(final AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        myAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
//                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        Toast.makeText(LoginActivity.this, "Signing in with your acount",
                                Toast.LENGTH_SHORT).show();
//                        AddUserDataFace(task.getResult().getUser().getDisplayName(), "-", task.getResult().getUser().getEmail(), "-", task.getResult().getUser().getPhotoUrl() + "", "-");
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            AddUserData(task.getResult().getUser().getDisplayName(), "-", task.getResult().getUser().getEmail(), "-", task.getResult().getUser().getPhotoUrl() + "", "-");
                        }

                        // ...
                    }
                });
    }

    private void printKeyHash() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.a3shank.apps.ashank",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.d("KeyHash:", e.toString());
        }
    }
}
