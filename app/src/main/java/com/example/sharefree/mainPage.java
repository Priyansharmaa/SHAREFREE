package com.example.sharefree;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.SubMenu;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharefree.databinding.ActivityMainPageBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class mainPage extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainPageBinding binding;
    TextView t1,t2;
    ImageView i1;
    RecyclerView rv;
    RecyclerAdapter ra;
    private WifiP2pManager.Channel channel;
    private WifiP2pManager manager;
    public static final String TAG = "wifidirectdemo";
    private boolean isWifiP2pEnabled = false;
    private boolean retryChannel = false;
    private final IntentFilter intentFilter = new IntentFilter();
    private BroadcastReceiver receiver = null;
    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    //String month[] = {"JANUARARY", "FEBURARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER", "JANUARARY", "FEBURARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
    WifiP2pManager.PeerListListener peerListListener;
    WifiP2pDevice device;
    Menu m;
    String[] month={"jan"};
    int index;
    WifiP2pDevice[] deviceArray;
    Serverclass serverclass;
    Clientclass clientclass;
    boolean isHost;


    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMainPage.toolbar);
        binding.appBarMainPage.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        //navigationView=findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        ShapeableImageView shapeableImageView = (ShapeableImageView) findViewById(R.id.shapeimgview);

        // Obtém a referência da view de cabeçalho
        View headerView = navigationView.getHeaderView(0);

        // Obtém a referência do nome do usuário e altera seu nome
        TextView tt11 = (TextView) headerView.findViewById(R.id.tv1);
        ImageView ii11=(ImageView) headerView.findViewById(R.id.imm);
        //i1=findViewById(R.id.imageView);
        //t1=findViewById(R.id.tv1);
        //t2=findViewById(R.id.tv2);



        File file = new File(getApplicationContext().getFilesDir(), "userdetails1.json");

        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        try {
            line = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (line != null) {
            stringBuilder.append(line).append("\n");
            try {
                line = bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String responce = stringBuilder.toString();


        try {
            JSONObject jsonObject  = new JSONObject(responce);
            t1=findViewById(R.id.tv1);
            tt11.setText(jsonObject.getString("firstname").toString()+jsonObject.getString("lastname").toString());
            Toast.makeText(mainPage.this, getApplicationContext().getFilesDir()+"/dp.jpg", Toast.LENGTH_SHORT).show();

            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),Uri.parse(getApplicationContext().getFilesDir()+"/dp.jpg") );
            //Drawable res = shapeableImageView.getDrawable();
            //ii11.setImageDrawable(res);
            ii11.setImageBitmap(bitmap);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_page);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);

        channel = manager.initialize(this, getMainLooper(), null);

        receiver = new wifipeerReceiver<>(manager, channel, this);
        rv=findViewById(R.id.recyclerView);
        //month[0]="hello";
        //SubMenu topChannelMenu = m.addSubMenu("NEW");
        //topChannelMenu = m.addSubMenu("RECENT ADD");
        //m.add(0,0,0,month[0]);


        if (ContextCompat.checkSelfPermission(mainPage.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(mainPage.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(mainPage.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else{
                ActivityCompat.requestPermissions(mainPage.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(mainPage.this, "discover success", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(int i) {
                Toast.makeText(mainPage.this, "discover failed", Toast.LENGTH_SHORT).show();

            }
        });

        peerListListener = new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peerList) {
                if (!peerList.getDeviceList().equals(peers)) {
                    peers.clear();
                    peers.addAll(peerList.getDeviceList());
                    Toast.makeText(getApplicationContext(), "peer listener called", Toast.LENGTH_SHORT).show();
                    month = new String[peerList.getDeviceList().size()];
                    deviceArray = new WifiP2pDevice[peerList.getDeviceList().size()];
                    index = 0;
                    deviceArray=peerList.getDeviceList().toArray(new WifiP2pDevice[0]);
                    for (WifiP2pDevice device : peerList.getDeviceList()) {
                        month[index] = device.deviceName;
                        index++;
                    }
                    //month[0]="hello";
                    //topChannelMenu.add(0,index,0,month[index]);
                    rv.setLayoutManager(new LinearLayoutManager(getApplicationContext() ));
                    ra=new RecyclerAdapter(mainPage.this ,month,deviceArray,manager,channel);
                    rv.setAdapter(ra);

                }

                if (peers.size() == 0) {
                    Toast.makeText(getApplicationContext(), "No Device Found", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        };



        WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
            @Override
            public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
                Toast.makeText(getApplicationContext(), "CONNECTION INFO CALLED", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_page, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_page);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(mainPage.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                        Snackbar.make(getWindow().getDecorView().getRootView(),R.string.location,Snackbar.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        receiver = new wifipeerReceiver<>(manager, channel, this);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }


}