package com.example.dan.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAggiungiRP=(Button)findViewById(R.id.btnAggiungiRP);
        btnAggiungiRP.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                Intent openPage1 = new Intent(MainActivity.this,AddActivity.class);
                startActivity(openPage1);
            }
        });

        Button btnTrova=(Button)findViewById(R.id.btnTrova);
        btnTrova.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                Intent openPage2 = new Intent(MainActivity.this,FindActivity.class);
                startActivity(openPage2);
            }
        });
    }
}
