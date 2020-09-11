package com.example.profile.Activities.ui.vehicles;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.profile.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;


public class MyVehicles extends Fragment {

    private StitchAppClient stitchAppClient;
private ListView listView;
List<String> mylist;
FirebaseAuth firebaseAuth;
FirebaseUser user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_myvehicles, container, false);

       listView=root.findViewById(R.id.Listview);
       this.stitchAppClient= Stitch.getAppClient("mongodemo-gopcv");

                        DataGet();




        return root;
    }

    private void DataGet() {
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();

        try {
            final RemoteMongoClient mongoClient = stitchAppClient.getServiceClient(RemoteMongoClient.factory, "Mongodb");

            RemoteMongoCollection<Document> coll = mongoClient.getDatabase("Bike").getCollection("Info");
            RemoteMongoCollection<Document> coll2 = mongoClient.getDatabase("Car").getCollection("Info");
            mylist=new ArrayList<String>();

            Document filterDoc = new Document()
                    .append("_id", new Document().append("$exists", true));
            RemoteFindIterable findResults = coll
                    .find(filterDoc)
                    .projection(new Document().append("_id", 0))
                    .sort(new Document().append("name", 1));
            RemoteFindIterable findResults2 = coll2
                    .find(filterDoc)
                    .projection(new Document().append("_id", 0))
                    .sort(new Document().append("name", 1));


            Task<List<Document>> itemsTask = findResults.into(new ArrayList<Document>());
            Task<List<Document>> itemsTask2 = findResults2.into(new ArrayList<Document>());

            itemsTask.addOnCompleteListener(new OnCompleteListener<List<Document>>() {
                @Override
                public void onComplete(@NonNull Task<List<Document>> task) {
                    if (task.isSuccessful()) {
                        List<Document> items = task.getResult();
                        for (Document item : items) {

                            String email=item.getString("mail");
                           String name=item.getString("bikename");
                           String mail=user.getEmail();

                           if(mail.equals(email)){
                            mylist.add(name);}

                                      }

                        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_list_item_1,android.R.id.text1,mylist);
                        listView.setAdapter(arrayAdapter);



                    } else {
                        Log.e("app", "failed to find documents with: ", task.getException());
                        Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }

                }



            });
            itemsTask2.addOnCompleteListener(new OnCompleteListener<List<Document>>() {
                @Override
                public void onComplete(@NonNull Task<List<Document>> task) {
                    if (task.isSuccessful()) {
                        List<Document> items = task.getResult();
                        for (Document item : items) {

                            String email=item.getString("mail");
                            String name=item.getString("carname");
                            String mail=user.getEmail();

                            if(mail.equals(email)){
                                mylist.add(name);}

                        }

                        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_list_item_1,android.R.id.text1,mylist);
                        listView.setAdapter(arrayAdapter);



                    } else {
                        Log.e("app", "failed to find documents with: ", task.getException());
                        Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }

                }



            });
        }catch (Exception e)
        {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
     }

