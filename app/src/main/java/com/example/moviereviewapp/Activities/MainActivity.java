package com.example.moviereviewapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moviereviewapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button toSearchPage = findViewById(R.id.toSearchPage);

        toSearchPage.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });
      Button aaa=findViewById(R.id.button2);
      aaa.setOnClickListener(v->{
          Intent intentq=new Intent(MainActivity.this,LoginActivity.class);
          startActivity(intentq);
      });

      Button bbb=findViewById(R.id.button3);
      bbb.setOnClickListener(v->{
          Intent intentw=new Intent(MainActivity.this,SplashScreen.class);
          startActivity(intentw);
      });

      Button ccc=findViewById(R.id.button4);
      ccc.setOnClickListener(v->{
          Intent intentw=new Intent(MainActivity.this,UserProfileActivity.class);
          startActivity(intentw);
      });
    }
}