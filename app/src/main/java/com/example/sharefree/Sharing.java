package com.example.sharefree;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sharing extends AppCompatActivity {
    RecyclerView rv;
    chatRecyclerView ra;
    String month[]={};
    EditText et;
    ImageButton b;
    List<String> mcount = new ArrayList<String>(Arrays.asList(month));
    View v;
    String ori="";
    WebView rw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing);
        et=findViewById(R.id.editTextTextPersonName);
        b=findViewById(R.id.imageButton);
        //rv=findViewById(R.id.recyclerView);
        //rv.setLayoutManager(new LinearLayoutManager(getApplicationContext() ));
        //rw= findViewById(R.id.tt1);
        //String u = rw.getOriginalUrl();

        //rw.getSettings().setJavaScriptEnabled(true);

        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        WebView wv=findViewById(R.id.webView);
        rw=findViewById(R.id.webView);
        TextView tv=new TextView(this);
        LinearLayout layout2 = new LinearLayout(this);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        //tv.setText(et.getText().toString());
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tv.setBackgroundColor(0xffffdbdb);
        tv.setGravity(Gravity.RIGHT);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TextView tv=new TextView(this);
                //LinearLayout layout2 = new LinearLayout(this);

                //layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                //layout2.setOrientation(LinearLayout.VERTICAL);
                //tv.setText(et.getText().toString());
                //wv.addView(tv);
                new GetContacts(view).execute();

            }
        });
        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et.setText("");
            }
        });
        et.requestFocus();
        et.setText("");

    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        View v;
        public GetContacts(View view) {
            this.v=view;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            final String mimeType = "text/html";
            final String encoding = "UTF-8";
            rw.setBackgroundColor(Color.TRANSPARENT);
            ori=ori+"<div  style=\"display: inline-block; float: right; margin-left: auto; border: 2px solid #dedede;border-color: #ccc; width: fit-content; background-color: #ddd; border-radius: 5px; \">\n" +
                    "  <p>"+et.getText().toString()+"</p>\n" +
                    "  <span class=\"time-left\">11:01</span>\n" +
                    "</div>";
            String html="<body style=\" background-color: black; height: fit-content; background-color: rgba(10,10,10,0.5); \">" +
                    "<div style=\"flex-direction: column;display: flex; bottom: 0px; position: relative; \">"+ori+"</div>"+"</body>";
            //String html = "<h8>" + title + "</h8>" + "<img src=" + img + " width=\"50\" height=\"50\" style=\"float:right\" />";
            rw.loadDataWithBaseURL("", html, mimeType, encoding, "");
            et.setText("");
            super.onPostExecute(unused);
        }
    }
}