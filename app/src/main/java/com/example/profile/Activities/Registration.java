package com.example.profile.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.profile.MyData;
import com.example.profile.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;

import java.util.Objects;

public class Registration extends AppCompatActivity {

    private ImageView imageView;
    private Uri photouri,imgpath;
    private EditText name,email,pass,cofig_pass,mobile,licence;
    private ProgressBar progressBar;
    private Button button;
    Bitmap path;
   MyData myData;
    private FirebaseAuth firebaseAuth;
    SignInButton signInButton;
  GoogleSignInClient mGoogleSignInClient;
  private int RC_SIGN_IN=200;


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        int content=getResources().getConfiguration().orientation;
        if(content== Configuration.ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }


        imageView=findViewById(R.id.imageView);
        name=findViewById(R.id.RegName);
        email=findViewById(R.id.RegEmail);
        pass=findViewById(R.id.Regpass);
        cofig_pass=findViewById(R.id.Config_pass);
        mobile=findViewById(R.id.mobile);
        licence=findViewById(R.id.LicenceNo);
        progressBar=findViewById(R.id.progressBar);
        button=findViewById(R.id.RegButton);
        signInButton=findViewById(R.id.googlebtn);
         myData=new MyData();
        firebaseAuth=FirebaseAuth.getInstance();


        progressBar.setVisibility(View.INVISIBLE);

// Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             openGallery();

            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                button.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                final String Name=name.getText().toString().trim();
                final String Email=email.getText().toString().trim();
                final String Pass=pass.getText().toString().trim();
                final String Config=cofig_pass.getText().toString().trim();
                PhoneNumberUtils.formatNumber(mobile.getText().toString());
                final String Mobile=mobile.getText().toString().trim();
                final String Licence=licence.getText().toString().trim();





           if(Name.isEmpty() || Email.isEmpty() || Pass.isEmpty() || Config.isEmpty() || Mobile.isEmpty() || Licence.isEmpty() || photouri==null )
                {
                   if(photouri==null)
                   {
                       ShowMessage("Please Select Profile Photo");
                       progressBar.setVisibility(View.INVISIBLE);
                       button.setVisibility(View.VISIBLE);
                   }
                   else
                   {
                    ShowMessage("Please Verify all fields");

                    progressBar.setVisibility(View.INVISIBLE);
                    button.setVisibility(View.VISIBLE);
                }

                }
                else if(!Config.equals(Pass))
                {
                    ShowMessage("Enter Same Password");
                    button.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else if((Mobile.length())!=10){
               Toast.makeText(Registration.this, "Enter 10 Digit Mobile Number....", Toast.LENGTH_SHORT).show();
               progressBar.setVisibility(View.INVISIBLE);
               button.setVisibility(View.VISIBLE);
                }
                else
                {
                    CreateAccount(Name,Email,Pass);
                }

            }
        });

    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
//                firebaseAuthWithGoogle(account.getIdToken());
//            } catch (ApiException e) {
//                // Google Sign In failed, update UI appropriately
//                Log.w(TAG, "Google sign in failed", e);
//                // ...
//            }
//        }
//    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
      //  updateUI(currentUser);
    }
    private void ShowMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void CreateAccount(final String name, String Email, String Pasword) {

        firebaseAuth.createUserWithEmailAndPassword(Email,Pasword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {

                            ShowMessage("Account Created Successfully.....");
                            DatabaseReference mStorageRef= FirebaseDatabase.getInstance().getReference().child("user");
                            myData.setEmail(email.getText().toString().trim());
                            myData.setPassword(pass.getText().toString().trim());
                            myData.setMobile_NO(mobile.getText().toString().trim());
                            myData.setLicence(licence.getText().toString().trim());
                            DatabaseReference mystore=mStorageRef.child("data");
                            mystore.push().setValue(myData);
                            Toast.makeText(Registration.this, "Data Inserted In DataBase", Toast.LENGTH_SHORT).show();

                                updateUserInfo(name,photouri,firebaseAuth.getCurrentUser());

                        }
                        else
                        {
                            ShowMessage("Failed To Create Account.."+task.getException().getMessage());
                            button.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });

    }



    private void updateUserInfo(final String name, Uri photouri, final FirebaseUser currentUser) {
        ShowMessage("Plase Wait Account is Loading");
        try {
            StorageReference mStore = FirebaseStorage.getInstance().getReference().child("User_Photos");

            final StorageReference imgpath = mStore.child(Objects.requireNonNull(photouri.getLastPathSegment()));
            imgpath.putFile(photouri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imgpath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            UserProfileChangeRequest userProfchange = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .setPhotoUri(uri)
                                    .build();

                            currentUser.updateProfile(userProfchange)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            ShowMessage("Regitration Completed...");


                                            UpdateUI();
                                        }
                                    });


                        }

                    });
                }
            });

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void UpdateUI() {

        Intent intent=new Intent(getApplicationContext(),HomeNavigate.class);

        startActivity(intent);
        finish();

    }



    private void openGallery() {

        Intent galleryintent=new Intent(Intent.ACTION_GET_CONTENT);
        galleryintent.setType("image/*");
        startActivityForResult(galleryintent,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK && requestCode==1 && data !=null)
        {

            photouri=data.getData();
            imageView.setImageURI(photouri);


        }


    }

    public void onClick(View view) {
        Intent intent=new Intent(getApplicationContext(),Login.class);
        startActivity(intent);
        finish();
    }
}
