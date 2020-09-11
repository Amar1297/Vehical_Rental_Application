package com.example.profile.Activities.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.profile.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mongodb.client.MongoClient;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;

import org.bson.Document;


public class ProfileFragment extends Fragment {
    private StitchAppClient client=null;
    private RemoteMongoClient mongoClient;
    private RemoteMongoCollection<Document> coll;
    private MongoClient mobileClient;
 FirebaseAuth firebaseAuth;
    FirebaseUser user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
         View root = inflater.inflate(R.layout.fragment_profile, container, false);
      firebaseAuth=FirebaseAuth.getInstance();
      user=firebaseAuth.getCurrentUser();

         final ImageView imageView=root.findViewById(R.id.imageView);
       final TextView name=root.findViewById(R.id.RegName);
        final TextView  email=root.findViewById(R.id.RegEmail);
        final TextView  pass=root.findViewById(R.id.Regpass);
        final TextView  mobile=root.findViewById(R.id.mobile);
        final TextView  licence=root.findViewById(R.id.LicenceNo);
try {

    name.setText(user.getDisplayName());


    Glide.with(this).load(user.getPhotoUrl()).into(imageView);


    DatabaseReference mStorageRef = FirebaseDatabase.getInstance().getReference().child("user");
    mStorageRef.child("data").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
           for(DataSnapshot ds:dataSnapshot.getChildren())
           {

               String mail=ds.child("email").getValue().toString();
               String password=ds.child("password").getValue().toString();
               String number=ds.child("mobile_NO").getValue().toString();
               String lice=ds.child("licence").getValue().toString();
               String n=user.getEmail();
               if(mail.equals(n)){
               email.setText(mail);
               pass.setText(password);
               mobile.setText(number);
               licence.setText(lice);}
           }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getContext(), databaseError.toString(), Toast.LENGTH_SHORT).show();
        }
    });
}catch (Exception e)
{
    Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
}
        return root;
    }

}
