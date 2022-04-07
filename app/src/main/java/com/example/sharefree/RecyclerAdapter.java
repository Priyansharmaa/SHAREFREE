package com.example.sharefree;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharefree.R;
import com.example.sharefree.httphandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.holder> {
    String data[];
    Context c;
    String title = "Title";
    String img;
    String body;
    String url;
    public WifiP2pDevice[] device;
    private WifiP2pManager.Channel channel;
    private WifiP2pManager manager;

    public RecyclerAdapter(Context c, String data[], WifiP2pDevice[] de, WifiP2pManager m, WifiP2pManager.Channel ch) {
        this.data = data;
        this.c = c;
        this.device = de;
        this.channel = ch;
        this.manager = m;
    }



    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater lf = LayoutInflater.from(parent.getContext());
        View v = lf.inflate(R.layout.singlexml, parent, false);
        holder h = new holder(v);
        return h;
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, @SuppressLint("RecyclerView") int position) {
        //holder.tv.setText("News"+position);
        url = "https://petwear.in/mc2022/news/news_" + (data.length - position - 1) + ".json";
        new GetContacts(holder.tv, url, position).execute();

    }


    private class GetContacts extends AsyncTask<Void, Void, Void> {
        String title = "Title";
        String img;
        String body;
        String url;
        WebView rw;
        int position;

        public GetContacts(WebView wv, String u, int p) {
            this.rw = wv;
            this.url = u;
            this.position = p;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            httphandler sh = new httphandler();
            //Intent it=getActivity().getIntent();
            //url=it.getStringExtra("pos");
            //url = "https://petwear.in/mc2022/news/news_"+getAdapterPosition()+".json";
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    title = jsonObj.getString("title");
                    img = jsonObj.getString("image-url");
                    body = jsonObj.getString("body");

                } catch (final JSONException e) {
                }

            } else {
                title = "title1";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //super.onPostExecute(result);

            String u = rw.getOriginalUrl();
            rw.getSettings().setJavaScriptEnabled(true);
            final String mimeType = "text/html";
            final String encoding = "UTF-8";
            String html = "<div  style=\" border-radius: 5px; margin: 10px 0; \">\n" +
                    "  <h4 ><img src=" + R.drawable.profile48 + " style=\"width:20%; margin-right: 0; float: left;\">\n" +
                    "      " + data[position] + "<p>" +
                    "</div>";
            //String html = "<h8>" + title + "</h8>" + "<img src=" + img + " width=\"50\" height=\"50\" style=\"float:right\" />";
            rw.loadDataWithBaseURL("", html, mimeType, encoding, "");

            super.onPostExecute(result);
        }


    }


    @Override
    public int getItemCount() {
        return data.length;
    }

    public class holder extends RecyclerView.ViewHolder {
        WebView tv;
        CardView ca;
        Serverclass serverclass;
        Clientclass clientclass;
        boolean isHost;

        public holder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tt);
            ca = itemView.findViewById(R.id.card);
            ca.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    WifiP2pConfig config = new WifiP2pConfig();
                    config.deviceAddress = device[getAdapterPosition()].deviceAddress;
                    if (ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    manager.connect(channel, config, new WifiP2pManager.ActionListener() {

                        @Override
                        public void onSuccess() {
                            ViewGroup.LayoutParams params = view.getLayoutParams();
                            params.height = 130;

                            Toast.makeText(c, "device connected", Toast.LENGTH_SHORT).show();
                            //ca.setLayoutParams(new ViewGroup.LayoutParams("match_content","wrap_content"));
                            //Intent i=new Intent(c,Sharing.class);
                            //c.startActivity(i);
                            Intent inn=new Intent(c,Sharing.class);
                            //inn.putExtra("manager", new Sharing());
                            c.startActivity(inn);
                        }

                        @Override
                        public void onFailure(int reason) {
                            Toast.makeText(c, "device not connected", Toast.LENGTH_SHORT).show();                        }

                    });

                }
            });






            WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
                @Override
                public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
                    Toast.makeText(c, "CONNECTION INFO CALLED", Toast.LENGTH_SHORT).show();
                    final InetAddress groupOwnerAddress = wifiP2pInfo.groupOwnerAddress;

                    if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner) {
                        //ConnectionStatus.setText("Host");
                        isHost=true;
                        serverclass=new Serverclass();
                        serverclass.start();
                    } else if (wifiP2pInfo.groupFormed) {
                        //ConnectionStatus.setText("Client");
                        isHost=false;
                        clientclass=new Clientclass(groupOwnerAddress);
                        clientclass.start();

                    }
                }
            };


        }

    }
}
