package com.example.profile.Activities.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.profile.Activities.Adapter;
import com.example.profile.Activities.HomeNavigate;
import com.example.profile.R;
import com.example.profile.VehicleData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private SearchView searchView=null;
    private SearchView.OnQueryTextListener queryTextListener;
    private RecyclerView recyclerView;
    private StitchAppClient stitchAppClient;
    Adapter adapter;
    List<VehicleData> mydata;
    Spinner spinner;
    String[] names={"Bike","Car"};


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
          View root = inflater.inflate(R.layout.fragment_home, container, false);

        this.stitchAppClient= Stitch.getAppClient("mongodemo-gopcv");
        recyclerView=root.findViewById(R.id.recycle);
        spinner=root.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);



        ArrayAdapter aa=new ArrayAdapter(root.getContext(),android.R.layout.simple_spinner_item,names);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_item);

        spinner.setAdapter(aa);


mydata=new ArrayList<>();

 Getdata();




        return root;
    }



    private void Getdata() {

try {
    final RemoteMongoClient mongoClient = stitchAppClient.getServiceClient(RemoteMongoClient.factory, "Mongodb");

    RemoteMongoCollection<Document> coll = mongoClient.getDatabase("Bike").getCollection("Info");
    RemoteMongoCollection<Document> coll2 = mongoClient.getDatabase("Car").getCollection("Info");


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


                    VehicleData data = new VehicleData();

                        data.setName(item.getString("bikename"));
                        data.setNumber(item.getString("bikenumber"));
                        data.setModel(item.getString("bikemodel"));
                        data.setYear(item.getString("bikeyear"));
                        data.setEmail(item.getString("mail"));
                        data.setMobile(item.getString("mobile"));
                        data.setImg(item.getString("bikeimg"));

                    mydata.add(data);


                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter=new Adapter(getContext(),mydata);
                recyclerView.setAdapter(adapter);

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


                    VehicleData data = new VehicleData();

                    data.setName(item.getString("carname"));
                    data.setNumber(item.getString("carnumber"));
                    data.setModel(item.getString("carmodel"));
                    data.setYear(item.getString("caryear"));
                    data.setEmail(item.getString("mail"));
                    data.setMobile(item.getString("mobile"));
                    data.setImg(item.getString("carimg"));

                    mydata.add(data);

                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter=new Adapter(getContext(),mydata);
                recyclerView.setAdapter(adapter);

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
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.home_navigate, menu);


        MenuItem searchitem=menu.findItem(R.id.action_search);
        SearchView searchView=new SearchView(((HomeNavigate)getContext()).getSupportActionBar().getThemedContext());

        searchitem.setActionView(searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });



        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(names[position]=="Bike")
        {

           Bikedata();

        }else if(names[position]=="Car")
        {

            CarData();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void CarData() {

        try {
            final RemoteMongoClient mongoClient = stitchAppClient.getServiceClient(RemoteMongoClient.factory, "Mongodb");
            RemoteMongoCollection<Document> coll2 = mongoClient.getDatabase("Car").getCollection("Info");

            Document filterDoc = new Document()
                    .append("_id", new Document().append("$exists", true));
            RemoteFindIterable findResults2 = coll2
                    .find(filterDoc)
                    .projection(new Document().append("_id", 0))
                    .sort(new Document().append("name", 1));
            Task<List<Document>> itemsTask2 = findResults2.into(new ArrayList<Document>());

            itemsTask2.addOnCompleteListener(new OnCompleteListener<List<Document>>() {
                @Override
                public void onComplete(@NonNull Task<List<Document>> task) {
                    if (task.isSuccessful()) {
                        List<Document> items = task.getResult();

                        for (Document item : items) {


                            VehicleData data = new VehicleData();

                            data.setName(item.getString("carname"));
                            data.setNumber(item.getString("carnumber"));
                            data.setModel(item.getString("carmodel"));
                            data.setYear(item.getString("caryear"));
                            data.setEmail(item.getString("mail"));
                            data.setMobile(item.getString("mobile"));
                            data.setImg(item.getString("carimg"));

                            mydata.add(data);

                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        adapter=new Adapter(getContext(),mydata);
                        recyclerView.setAdapter(adapter);

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
    private void Bikedata() {

        try {
            final RemoteMongoClient mongoClient = stitchAppClient.getServiceClient(RemoteMongoClient.factory, "Mongodb");

            RemoteMongoCollection<Document> coll = mongoClient.getDatabase("Bike").getCollection("Info");


            Document filterDoc = new Document()
                    .append("_id", new Document().append("$exists", true));
            RemoteFindIterable findResults = coll
                    .find(filterDoc)
                    .projection(new Document().append("_id", 0))
                    .sort(new Document().append("name", 1));


            Task<List<Document>> itemsTask = findResults.into(new ArrayList<Document>());

            itemsTask.addOnCompleteListener(new OnCompleteListener<List<Document>>() {
                @Override
                public void onComplete(@NonNull Task<List<Document>> task) {
                    if (task.isSuccessful()) {
                        List<Document> items = task.getResult();

                        for (Document item : items) {


                            VehicleData data = new VehicleData();

                            data.setName(item.getString("bikename"));
                            data.setNumber(item.getString("bikenumber"));
                            data.setModel(item.getString("bikemodel"));
                            data.setYear(item.getString("bikeyear"));
                            data.setEmail(item.getString("mail"));
                            data.setMobile(item.getString("mobile"));
                            data.setImg(item.getString("bikeimg"));

                            mydata.add(data);


                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        adapter=new Adapter(getContext(),mydata);
                        recyclerView.setAdapter(adapter);

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



