package com.example.sharefree;

import android.os.Looper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;

public class Clientclass extends Thread{

    Socket socket;
    String hostadd;
    //SendReceive sendReceive;
    private InputStream inputStream;
    private OutputStream outputStream;
    MainActivity mainActivity;

    public Clientclass(InetAddress hostAddress){
        hostadd=hostAddress.getHostAddress();
        socket=new Socket();
    }


    public void write(byte[] bytes){
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void run() {
        super.run();

        try {
            socket.connect(new InetSocketAddress(hostadd,8888),500);
            inputStream=socket.getInputStream();
            outputStream=socket.getOutputStream();
            //sendReceive=new SendReceive(socket);
           // sendReceive.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

        ExecutorService executorService= Executors.newSingleThreadExecutor();
        Handler handler=new Handler(Looper.getMainLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                byte[] buffer=new byte[1024];
                int bytes;
                while(socket!=null){
                    try {
                        bytes=inputStream.read(buffer);
                        if(bytes>0){
                            int finalBytes=bytes;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    String tempmsg=new String(buffer,0,finalBytes);
                                    //mainActivity.read_msgbox.setText(tempmsg);

                                }
                            });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }
        });
    }

}
