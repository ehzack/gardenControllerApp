package com.example.zackprod.test;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

import static com.example.zackprod.test.SensorInformation.idplante;

/**
 * Created by zackprod on 13/01/18.
 */

public class parsing extends IntentService {
    char data[] = new char[105];
    String line = "";
    String dataparsin = "";
    String temp = "18";
    String hum = "22";
    String humsol = "40";
    String Ptemp = "20";
    String Phum = "30";
    String Phumsol = "60";
    String withTime = "1";
    String time = "20";
    String work = "";
    public static boolean startservice = false;
    int i = 0;
    String response = "";
    char message;
    String URL = "";
    boolean flag=true;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public parsing() {
        super("ZackProd");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {


        while (startservice) {
            String data = "", line = "";

            try {

                String lien = "http://" + getString(R.string.URL) + "/sensor/socket/info.json" + idplante;
                URL url = new URL(lien);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferead = new BufferedReader(new InputStreamReader(inputStream));


                while (line != null) {
                    line = bufferead.readLine();
                    data = data + line;
                }


            } catch (MalformedURLException e) {
                flag=false;
            } catch (IOException e) {
                flag=false;

            } catch (Exception e) {
                flag=false;


            }
if(flag) {
    try {
        JSONObject ja = new JSONObject(data);


        try {
            String test = (String) ja.getString("hum");

            hum = (String) ja.getString("hum");
            temp = (String) ja.getString("temp");
            humsol = (String) ja.getString("humsol");
            work = (String) ja.getString("wateringstart");
            SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);
            SharedPreferences.Editor Ed = sp.edit();
            Ed.putString("temp", temp);
            Ed.putString("hum", hum);
            Ed.putString("humsol", humsol);
            Ed.putString("work", work);


        } catch (JSONException e) {
            SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);
            temp = sp.getString("temp", "18");
            hum = sp.getString("hum", "23");

            humsol = sp.getString("humsol", "40");
            work = sp.getString("work", "0");

        }

        int a = 0;
        if (work.equals("1")) {
            a = 1;
        }


        Intent send = new Intent();

        send.setAction("com.exemple.Zackprod");

        send.putExtra("temp", temp);

        send.putExtra("hum", hum);

        send.putExtra("humsol", humsol);
        send.putExtra("work", a);
        send.putExtra("flag", flag);


        sendBroadcast(send);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    } catch (JSONException e) {
        e.printStackTrace();
    }
}else {
    Intent send = new Intent();

    send.setAction("com.exemple.Zackprod");

    send.putExtra("flag", flag);


    sendBroadcast(send);
}
        }
    }
}




