package com.example.profile.Activities.ui.bike;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.profile.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;

import org.bson.Document;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;


public class BikeFragment extends Fragment {

   /* private ImageView bikeimg;
    private EditText bikename, bikenumber, bikemodel, bikeyear, bikeinsu;
    private Button bikesubmit;
    private ProgressBar progressBar;

    private StitchAppClient client;
    private RemoteMongoClient mongoClient;
    private RemoteMongoCollection<Document> coll;
    private MongoClient mobileClient;

    Context contex;*/
   private String imageString;
    private  Uri imageuri;
    private ImageView      bikeimg;
    Intent CropIntent;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String mail,mobile;
    private StitchAppClient stitchAppClient=null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.bike_fragment, container, false);

 bikeimg=root.findViewById(R.id.bikeimg);
  final EditText      bikename=root.findViewById(R.id.bikename);
     final EditText   bikenumber=root.findViewById(R.id.bikenumber);
       final EditText bikemodel=root.findViewById(R.id.bikemodel);
      final EditText  bikeyear=root.findViewById(R.id.bikeyear);
       final EditText bikeinsu=root.findViewById(R.id.bikeInsurance);
       final ProgressBar progressBar=root.findViewById(R.id.bikeprogress);
       final Button bikesub=root.findViewById(R.id.bikesubmit);

    progressBar.setVisibility(View.INVISIBLE);
        bikesub.setVisibility(View.VISIBLE);

       try {

           this.stitchAppClient = Stitch.getAppClient("mongodemo-gopcv");


           stitchAppClient.getAuth().loginWithCredential(new AnonymousCredential()).addOnCompleteListener(new OnCompleteListener<StitchUser>() {
               @Override
               public void onComplete(@NonNull Task<StitchUser> task) {
                   //Toast.makeText(getContext(), task.getResult().toString(), Toast.LENGTH_SHORT).show();
               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
               }
           });
       }catch (Exception e)
       {
           Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
       }

        bikeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");

                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, 2);
            }
        });

       GetNameAndNumber();

        bikesub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.INVISIBLE);
                bikesub.setVisibility(View.VISIBLE);

                try {
                  Bitmap bitmap = ((BitmapDrawable) bikeimg.getDrawable()).getBitmap();
                  ByteArrayOutputStream bios = new ByteArrayOutputStream();
                  bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bios);
                  byte[] img = bios.toByteArray();
                  imageString = Base64.encodeToString(img, Base64.DEFAULT);
              }catch (Exception e){
                  Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
              }
                final         String name = bikename.getText().toString();
                final String number = bikenumber.getText().toString();
                final    String model = bikemodel.getText().toString();
                final String year = bikeyear.getText().toString();
                final String insu = bikeinsu.getText().toString();

                 try {
                   if (name.isEmpty() || number.isEmpty() || model.isEmpty() || year.isEmpty() || insu.isEmpty()|| imageuri==null) {

                       if (imageuri == null) {
                           Toast.makeText(v.getContext(), "Select pic", Toast.LENGTH_SHORT).show();
                           progressBar.setVisibility(View.INVISIBLE);
                           bikesub.setVisibility(View.VISIBLE);
                       } else {
                           Toast.makeText(v.getContext(), "Please Enter All Fileds...", Toast.LENGTH_SHORT).show();
                           progressBar.setVisibility(View.INVISIBLE);
                           bikesub.setVisibility(View.VISIBLE);
                       }
                   }
                   else {
                       Add(name,number,model,year,insu,imageString,mail,mobile);

                       progressBar.setVisibility(View.INVISIBLE);
                       bikesub.setVisibility(View.VISIBLE);

                       bikename.setText("");
                       bikenumber.setText("");
                       bikemodel.setText("");
                      bikeyear.setText("");
                   bikeinsu.setText("");
                   bikeimg.setBackground(null);
                   }
                   }catch (Exception e)
               {
                   Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
               }


                        }
        });


        return root;
    }

    private void GetNameAndNumber() {
        firebaseAuth= FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        DatabaseReference mStorageRef = FirebaseDatabase.getInstance().getReference().child("user");
        mStorageRef.child("data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    mail = ds.child("email").getValue().toString();
                    String n=user.getEmail();
                    if(mail.equals(n)) {
                         mail = ds.child("email").getValue().toString();
                         mobile = ds.child("mobile_NO").getValue().toString();

                   }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Add(String name, String number, String model, String year, String insu, String imageString,String mail,String mobile)
    {
        final RemoteMongoClient mongoClient= stitchAppClient.getServiceClient(RemoteMongoClient.factory, "Mongodb");

        RemoteMongoCollection<Document> coll = mongoClient.getDatabase("Bike").getCollection("Info");

        Document namede = new Document()
                .append("bikename", name)
                .append("bikenumber", number)
                .append("bikemodel", model)
                .append("bikeyear", year)
                .append("bikeinsu", insu)
                .append("mail",mail)
                .append("mobile",mobile)
                .append("bikeimg", imageString);


        final Task<RemoteInsertOneResult> insertTask = coll.insertOne(namede);
        insertTask.addOnCompleteListener(new OnCompleteListener<RemoteInsertOneResult>() {
            @Override
            public void onComplete(@com.mongodb.lang.NonNull Task<RemoteInsertOneResult> task) {
                if (task.isSuccessful()) {
                    Log.d("app", String.format("successfully inserted item with id %s",
                            task.getResult().getInsertedId()));
                    Toast.makeText(getContext(), "Sucessfully Inserted Data", Toast.LENGTH_SHORT).show();
                    //Message="successfully Added Data...";

                } else {
                    Log.e("app", "failed to insert document with: ", task.getException());
                    Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                    //Message=task.getException().toString();
                }
            }
        });

    }


    @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      if (requestCode == 0 && resultCode == RESULT_OK) {

          ImageCropFunction();

      } else if (requestCode == 2) {

          if (data != null) {

              imageuri = data.getData();

              ImageCropFunction();

          }
      } else if (requestCode == 1) {

          if (data != null) {

              Bundle bundle = data.getExtras();

              Bitmap bitmap = bundle.getParcelable("data");

              bikeimg.setImageBitmap(bitmap);

          }
      }




  }

    public void ImageCropFunction() {

        // Image Crop Code
        try {
            CropIntent = new Intent("com.android.camera.action.CROP");

            CropIntent.setDataAndType(imageuri, "image/*");

            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", 180);
            CropIntent.putExtra("outputY", 180);
            CropIntent.putExtra("aspectX", 4);
            CropIntent.putExtra("aspectY", 3);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);

            startActivityForResult(CropIntent, 1);

        } catch (ActivityNotFoundException e) {

        }
    }
    //Image Crop Code End Here


}
