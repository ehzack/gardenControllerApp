package com.example.zackprod.test;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class liste extends AppCompatActivity {
    ListView T;
    public static int drap = -1;

    public static String idclient;
    public static String data = "";
    public static String line = "";
    public static String dataparsin = "";
    public static ArrayList<String> temp = new ArrayList<String>();

    public static ArrayList<String> hum = new ArrayList<String>();

    public static ArrayList<String> humsol = new ArrayList<String>();

    public static ArrayList<String> id = new ArrayList<String>();
    public String s;
    public static ArrayList<String> nom_plante = new ArrayList<String>();

    public static ArrayList<String> ip = new ArrayList<String>();
    public static ArrayList<String> city = new ArrayList<String>();

    public static ArrayList<String> photo = new ArrayList<String>();
    public static ArrayList<String> watering_time = new ArrayList<String>();
    public static ArrayList<String> withtime = new ArrayList<String>();
    public static ArrayList<String> lat = new ArrayList<String>();
    public static ArrayList<String> lon = new ArrayList<String>();


    public static ArrayList<Bitmap> timg = new ArrayList<Bitmap>();
    boolean flagpicture = false;

    boolean loading = false;
    String color[] = {"circle", "cirle2", "cirle3", "cirle4", "cirle5", "cirle6"};
    TextView img;
    TextView cl;
    TextView nmplante;
    public static int taille;
    // public static ArrayList<Bitmap>timg=new ArrayList<>();
    String nom, prenom;
    View view;
    Drawable mDrawable;


    //  ProgressBar simpleProgressBar;
    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste);


        T = (ListView) findViewById(R.id.listeview);
        cl = (TextView) findViewById(R.id.barclient);
        Bundle info = getIntent().getExtras();
        nom = info.getString("nom");
        prenom = info.getString("prenom");
        idclient = info.getString("id");
        mDrawable = getResources().getDrawable(R.drawable.circle);
        cl.setText(prenom + " " + nom);



        parseliste test = new parseliste();
        test.execute();


        T.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bitmap test = null;
                Intent A = new Intent(liste.this, SensorInformation.class);

                Bundle B = new Bundle();
                B.putString("idclient", idclient);
                B.putString("nomclient", nom);
                B.putString("prenomclient", prenom);


                B.putString("temp", temp.get(i));
                B.putString("hum", hum.get(i));
                B.putString("humsol", humsol.get(i));
                B.putString("nomplante", nom_plante.get(i));
                B.putString("ip", ip.get(i));
                B.putString("id", id.get(i));
                B.putString("city", city.get(i));
                B.putString("time", watering_time.get(i));
                B.putString("with_time", withtime.get(i));
                B.putString("lat", lat.get(i));
                B.putString("lon", lon.get(i));
                // ByteArrayOutputStream stream = new ByteArrayOutputStream();
                //test=timg.get(i);
                //test.compress(Bitmap.CompressFormat.PNG, 100, stream);
                // byte[] byteArray = stream.toByteArray();
                // B.putByteArray("photo",byteArray);


                A.putExtras(B);
                startActivity(A);


            }
        });


    }

    public void onStop() {
        super.onStop();


    }

    @Override
    public void onResume() {
        super.onResume();
        // simpleProgressBar.setVisibility(View.GONE);

        if (ConnexionInternet.isConnectedInternet(liste.this)) {

            nom_plante.clear();
            temp.clear();

            hum.clear();

            humsol.clear();

            id.clear();

            ip.clear();
            city.clear();

            photo.clear();
            watering_time.clear();
            withtime.clear();
            lat.clear();
            lon.clear();
            parseliste test = new parseliste();
            test.execute();

        }

    }

    /*************************************************************************************/


    /**********************************************************************************/
    class Customadapter extends BaseAdapter {


        @Override
        public int getCount() {
            return taille;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            int id = getResources().getIdentifier(color[i % 5], "drawable", getPackageName());
            view = getLayoutInflater().inflate(R.layout.listeplante, null);
            nmplante = (TextView) view.findViewById(R.id.txt);
            img = (TextView) view.findViewById(R.id.imgvw);
            img.setBackgroundResource(id);

            img.setText(city.get(i).substring(0, 1));
            nmplante.setText(nom_plante.get(i));
            return view;
        }

        @Nullable
        @Override
        public CharSequence[] getAutofillOptions() {
            return new CharSequence[0];
        }
    }


    class parseliste extends AsyncTask<String, String, String> {

        String data = "";
        String line = "";
        String dataparsin = "";
        int drapo = -1;

        String TEMP;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (ConnexionInternet.isConnectedInternet(liste.this)) {

                Customadapter customadapter = new Customadapter();
                T.setAdapter(customadapter);
                //   simpleProgressBar.setVisibility(View.GONE);
            }
        }

        int verification() {
            return drapo;
        }

        @Override
        protected String doInBackground(String... strings) {
            if (ConnexionInternet.isConnectedInternet(liste.this)) {
                URL url = null;
                try {
                    url = new URL("http://" + getString(R.string.URL) + "/sensor/android/paramettre.php?id=" + liste.idclient);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                HttpURLConnection httpURLConnection = null;
                try {
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                InputStream inputStream = null;
                try {
                    inputStream = httpURLConnection.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BufferedReader bufferead = new BufferedReader(new InputStreamReader(inputStream));

                while (line != null) {
                    try {
                        line = bufferead.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    data = data + line;
                }
                JSONArray ta = null;
                try {
                    ta = new JSONArray(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject ja = null;

                liste.taille = ta.length();
                for (int i = 0; i < ta.length(); i++) {

                    try {
                        JSONObject C = ta.getJSONObject(i);
                        liste.temp.add((String) C.getString("temperature"));


                        liste.hum.add((String) C.getString("humidite"));

                        liste.humsol.add((String) C.getString("humidite_sol"));


                        liste.id.add((String) C.getString("idplante"));


                        liste.nom_plante.add((String) C.getString("nom_plante"));

                        liste.ip.add((String) C.getString("ip"));
                        liste.city.add((String) C.getString("city"));
                        liste.photo.add((String) C.getString("photo"));
                        liste.watering_time.add((String) C.getString("time"));
                        liste.withtime.add((String) C.getString("withtime"));
                        liste.lat.add((String) C.getString("lat"));
                        liste.lon.add((String) C.getString("lon"));


                    } catch (JSONException e) {
                    }

                }
                drapo = 1;
            }
            return null;
        }


    }

    class donwloadpicture extends AsyncTask<String, String, String> {
        Bitmap timgg = null;
        int i;

        @Override
        protected String doInBackground(String... strings) {

            for (i = 0; i < taille; i++) {
                try {
                    InputStream srt = new java.net.URL("http://zackprodserver.ddns.net/sensor/image/" + photo.get(i)).openStream();
                    timgg = BitmapFactory.decodeStream(srt);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                liste.timg.add(timgg);

                timgg = null;
            }
            flagpicture = true;

            return null;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuuser, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.deconnexion) {

            SharedPreferences sp1 = getApplication().getSharedPreferences("Login", MODE_PRIVATE);
            sp1.edit().clear().commit();
            finish();

        }

        return true;
    }


}

