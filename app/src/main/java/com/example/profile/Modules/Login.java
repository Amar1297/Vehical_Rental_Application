package com.example.profile.Modules;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.profile.Activities.HomeNavigate;
import com.example.profile.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText email, pass;
    Button login;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        int content=getResources().getConfiguration().orientation;

        if(content== Configuration.ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        email = findViewById(R.id.loginEmail);
        pass = findViewById(R.id.loginpass);
        login = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBar);
        firebaseAuth = FirebaseAuth.getInstance();

        progressBar.setVisibility(View.INVISIBLE);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn();
            }
        });

    }

    private void SignIn() {
  try {
      login.setVisibility(View.INVISIBLE);
      progressBar.setVisibility(View.VISIBLE);

      final String Email = email.getText().toString().trim();
      final String Pass = pass.getText().toString().trim();

      if (Email.isEmpty() || Pass.isEmpty()) {
          ShowMessage("Empty Fields");
          progressBar.setVisibility(View.INVISIBLE);
          login.setVisibility(View.VISIBLE);

      } else {
          SignInAccount(Email, Pass);
      }
  }catch (Exception e){
      ShowMessage(e.toString());
  }
    }

    private void SignInAccount(String email, String pass) {
try {
    firebaseAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.VISIBLE);
                        login.setVisibility(View.INVISIBLE);
                        UpdateUI();
                    } else {
                        String str = task.getException().getMessage();
                        ShowMessage(str);
                        progressBar.setVisibility(View.INVISIBLE);
                        login.setVisibility(View.VISIBLE);
                    }
                }
            });
}catch (Exception e){
    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
}

    }

    private void UpdateUI() {

        ShowMessage("Please Wait Account is Loading...");
        Intent intent=new Intent(getApplicationContext(), HomeNavigate.class);
        startActivity(intent);
        finish();


    }



    private void ShowMessage(String massage) {
        Toast.makeText(this, massage, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {

            Intent intent = new Intent(getApplicationContext(), HomeNavigate.class);
            startActivity(intent);
            finish();
        }
    }catch (Exception e){
            ShowMessage(e.toString());
        }
    }

    public void onClick(View view) {
        Intent intent=new Intent(getApplicationContext(), Registration.class);
        startActivity(intent);
        finish();
    }
}


