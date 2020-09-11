package com.example.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Book extends AppCompatActivity {

    private TextView name,number,mobile,Email;
    private ImageView whatsapp,calling;
    ImageView imageView;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        int content=getResources().getConfiguration().orientation;
        if(content== Configuration.ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        name=findViewById(R.id.viewname);
        number=findViewById(R.id.viewnumber);
        mobile=findViewById(R.id.viewmobiler);
        Email=findViewById(R.id.viewemail);
        whatsapp=findViewById(R.id.whatsapplogo);
        calling=findViewById(R.id.call);

        imageView=findViewById(R.id.imgbook);

        Intent intent=getIntent();
        String n=intent.getStringExtra("name");
        String no=intent.getStringExtra("number");
        String mo=intent.getStringExtra("mobile");
        String email=intent.getStringExtra("mail");
        String img=intent.getStringExtra("img");
        name.setText(n);
        number.setText(no);
        Email.setText(email);
        mobile.setText(mo);



        byte[] data = Base64.decode(img, Base64.DEFAULT);
        Bitmap decodedata = BitmapFactory.decodeByteArray(data, 0, data.length);
        imageView.setImageBitmap(decodedata);

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent whatsapplunch=new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:"+
                        ""+ mo + "?body=" + ""));

                whatsapplunch.setPackage("com.whatsapp");
                startActivity(whatsapplunch);
            }
        });

        calling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent dailpad=new Intent(Intent.ACTION_DIAL);
                dailpad.setData(Uri.parse("tel:"+mo));
                startActivity(dailpad);

            }
        });

    }
}
