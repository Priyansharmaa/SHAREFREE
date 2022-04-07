package com.example.sharefree;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class createProfile extends AppCompatActivity {
    RecyclerView rv;
    chatRecyclerView ra;
    String month[]={""};
    List<String> mcount = new ArrayList<String>(Arrays.asList(month));
    View v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        rv=findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext() ));
        ra=new chatRecyclerView( month);
        rv.setAdapter(ra);

    }
}