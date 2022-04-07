package com.example.sharefree;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class wifipeerReceiver<WiFiDirectActivity> extends BroadcastReceiver {
    private WifiP2pManager manager;
    private Channel channel;
    private mainPage activity;
    PeerListListener myPeerListListener;
    Serverclass serverclass;
    Clientclass clientclass;
    boolean isHost;
    public wifipeerReceiver(WifiP2pManager manager, Channel channel,
                            mainPage activity) {
        super();
        this.manager = manager;
        this.channel = channel;
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Determine if Wifi P2P mode is enabled or not, alert
            // the Activity.
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                activity.setIsWifiP2pEnabled(true);
            } else {
                activity.setIsWifiP2pEnabled(false);
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            if (manager != null) {
                if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                manager.requestPeers(channel, activity.peerListListener);
            }

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            Toast.makeText(activity.getApplicationContext(), "receiver CONNECTION INFO CALLED", Toast.LENGTH_SHORT).show();

            //Intent i=new Intent(activity.getApplicationContext(),Sharing.class);
            //activity.getApplicationContext().startActivity(i);

            manager.requestConnectionInfo(channel,new WifiP2pManager.ConnectionInfoListener() {
                @Override
                public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {

                    final InetAddress groupOwnerAddress = wifiP2pInfo.groupOwnerAddress;

                    if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner) {
                        //ConnectionStatus.setText("Host");
                        Toast.makeText(activity.getApplicationContext(), "server CONNECTION INFO CALLED", Toast.LENGTH_SHORT).show();

                        //isHost=true;
                        //serverclass=new Serverclass();
                        //serverclass.start();
                        //new udpFirst().execute();
                    } else if (wifiP2pInfo.groupFormed) {
                        Toast.makeText(activity.getApplicationContext(), " client CONNECTION INFO CALLED", Toast.LENGTH_SHORT).show();
                        //ConnectionStatus.setText("Client");
                        //isHost=false;
                        //clientclass=new Clientclass(groupOwnerAddress);
                        //clientclass.start();
                        //new udpSecond(activity.getApplicationContext()).execute();


                    }
                }
            });


        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {


        }
    }




    public class udpFirst extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String messageStr="Hello Android! how are you";
                int server_port = 6668;
                // TODO: fill in UDP Client IP, 255 is for broadcast. You can use 255.255.255.255 also.

                InetAddress local = InetAddress.getByName("192.168.43.101");

                //DatagramPacket p = new DatagramPacket(messageStr.getBytes(), messageStr.length(), local,server_port);
                //Toast.makeText(activity.getApplicationContext(), " UDP server about to:send", Toast.LENGTH_SHORT).show();

               // Log.i("UDP server about to: ", "send");
                //DatagramSocket s = new DatagramSocket();
                //s.send(p);
                //s.close();
                Toast.makeText(activity.getApplicationContext(), " ***** UDP server:Done sending", Toast.LENGTH_SHORT).show();

                //Log.i("***** UDP server: ", "Done sending");
            } catch ( UnknownHostException e ) {
                Log.i("***** UDP server has: ", "UnknownHostException");
            } catch (IOException e){
                Log.i("UDP server has Exc", "e: " + e);
            }
            return null;
        }
    }

    public class udpSecond extends AsyncTask<Void,Void,Void> {
        Context context;

        udpSecond(Context c){
            context = c;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            Log.i("***** UDP client: ", "starting");
            final String text;
            int server_port = 6668;
            byte[] message = new byte[2048];
            DatagramPacket p = new DatagramPacket(message, message.length);
                /*
                DatagramSocket s = new DatagramSocket(server_port);
                Toast.makeText(activity.getApplicationContext(), " ***** UDP client:about to wait to receive", Toast.LENGTH_SHORT).show();
                //Log.i("***** UDP client: ", "about to wait to receive");
                s.receive(p); // blocks until something is received
                //Log.i("***** UDP client: ", "received");
                Toast.makeText(activity.getApplicationContext(), " ***** UDP client: received", Toast.LENGTH_SHORT).show();

                text = new String(message, 0, p.getLength());
                Toast.makeText(activity.getApplicationContext(), " *UDP client message:  "+text, Toast.LENGTH_SHORT).show();
                //Log.i("*UDP client message: ", text);

                Handler handler =  new Handler(context.getMainLooper());
                handler.post( new Runnable(){
                    public void run(){
                        Toast.makeText(context, text,Toast.LENGTH_LONG).show();
                    }
                });

                Log.d("Udp tutorial","message:" + text);
                s.close();*/
            return null;
        }
    }


}