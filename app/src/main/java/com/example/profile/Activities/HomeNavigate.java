package com.example.profile.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.profile.Modules.Login;
import com.example.profile.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;

public class HomeNavigate extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_navigate);
        int content=getResources().getConfiguration().orientation;
        if(content== Configuration.ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final FloatingActionButton fab = findViewById(R.id.fab);
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        
        

        try {
            final StitchAppClient stitchAppClient = Stitch.initializeAppClient("mongodemo-gopcv");

            stitchAppClient.getAuth().loginWithCredential(new AnonymousCredential())
                    .addOnCompleteListener(new OnCompleteListener<StitchUser>() {
                        @Override
                        public void onComplete(@NonNull Task<StitchUser> task) {

                        }
                    });
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Report Any Issue@project.com", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);


        UpdateNavHeader();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_Profile, R.id.nav_myvehicles,R.id.nav_cars,R.id.nav_Bikes,R.id.nav_LogOut)
                .setDrawerLayout(drawer)
                .build();



        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

                int MenuID=destination.getId();
                switch (MenuID)
                {


                    case R.id.nav_LogOut :
                        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
                        firebaseAuth.signOut();
                        Toast.makeText(HomeNavigate.this, "Sign Out", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        finish();
                        break;

                    default:
                        String str=destination.getLabel().toString();
                        Toast.makeText(HomeNavigate.this, str+".......", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });


    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void UpdateNavHeader()
    {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView=navigationView.getHeaderView(0);
        TextView navUser=headerView.findViewById(R.id.nav_username);
        TextView navEmail=headerView.findViewById(R.id.textView);
        ImageView userphoto=headerView.findViewById(R.id.imageView);


        navEmail.setText(user.getEmail());
        navUser.setText(user.getDisplayName());


        Glide.with(this).load(user.getPhotoUrl()).into(userphoto);

    }


}
