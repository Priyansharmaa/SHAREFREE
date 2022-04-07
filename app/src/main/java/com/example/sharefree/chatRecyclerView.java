package com.example.sharefree;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

public class chatRecyclerView extends RecyclerView.Adapter<chatRecyclerView.holder>{
    String data[];
    Context c;
    String title = "Title";
    String img;
    String body;
    String url;
    String ori="";
    public WifiP2pDevice[] device;
    private WifiP2pManager.Channel channel;
    private WifiP2pManager manager;

    public chatRecyclerView(String data[]) {
        this.data = data;

    }
    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater lf = LayoutInflater.from(parent.getContext());
        View v = lf.inflate(R.layout.chatbox, parent, false);
        chatRecyclerView.holder h = new chatRecyclerView.holder(v);
        return h;
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        WebView rw= holder.tv1;
        /*
        WebView rw= holder.tv1;;
        String u = rw.getOriginalUrl();
        rw.getSettings().setJavaScriptEnabled(true);
        final String mimeType = "text/html";
        final String encoding = "UTF-8";

        String html=
                "<div  style=\"flex-direction: column;display: flex; display: inline-block; float: right; margin-left: auto;border: 2px solid #dedede;border-color: #ccc; width: fit-content;background-color: #ddd;border-radius: 5px;padding: 1px; \">\n" +
                "  <p>"+data[position]+"</p>\n" +
                "  <span class=\"time-left\">11:01</span>\n" ;
        //String html = "<h8>" + title + "</h8>" + "<img src=" + img + " width=\"50\" height=\"50\" style=\"float:right\" />";
        rw.loadDataWithBaseURL("", html, mimeType, encoding, "");*/
        new GetContacts(rw,position).execute();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        WebView rw;
        int position;
        public GetContacts(WebView view,int position) {
            this.rw=view;
            this.position=position;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            //WebView rw= holder.tv1;;
            String u = rw.getOriginalUrl();
            rw.getSettings().setJavaScriptEnabled(true);
            final String mimeType = "text/html";
            final String encoding = "UTF-8";

            String html="<body style=\" background-color: black;\">"+
                    "<div  style=\"flex-direction: column;display: flex; display: inline-block; float: right; margin-left: auto;border: 2px solid #dedede;border-color: #ccc; width: fit-content;background-color: #ddd;border-radius: 5px;padding: 1px; \">\n" +
                            "  <p>"+data[position]+"</p>\n" +
                            "  <span class=\"time-left\">11:01</span>\n" +"</div>\n" +
                    "\n" +
                    "    </body>";
            //String html = "<h8>" + title + "</h8>" + "<img src=" + img + " width=\"50\" height=\"50\" style=\"float:right\" />";
            rw.loadDataWithBaseURL("", html, mimeType, encoding, "");
        }
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class holder extends RecyclerView.ViewHolder {
        WebView tv1;
        CardView ca;

        public holder(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tt1);
        }

    }
}
