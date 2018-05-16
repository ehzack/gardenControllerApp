package com.example.zackprod.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import me.itangqi.waveloadingview.WaveLoadingView;


public class Login extends AppCompatActivity {
    String log;
    String pass;
    EditText login;
    EditText password;
    Intent A;
    int i = 0;
    boolean lancer = false;
    public static String URLSERVER = null;


    String resultat = "";

    String nom = "";
    String prenom = "";
    String id = "";
    Button go;
    String oldlog;
    String oldpass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
        go = (Button) findViewById(R.id.go);


        final FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        URLSERVER = mFirebaseRemoteConfig.getString("URLServer");

        mFirebaseRemoteConfig.fetch(30)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(getApplicationContext(), "Fetch Succeeded",Toast.LENGTH_SHORT).show();
                            URLSERVER = mFirebaseRemoteConfig.getString("URLServer");

                            // After config data is successfully fetched, it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                        }
                    }

                });


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Message");
        myRef.addValueEventListener(new ValueEventListener() {

            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String a = dataSnapshot.getValue(String.class);
                //  Toast.makeText(getApplicationContext(),a,Toast.LENGTH_LONG).show();


            }


            @Override
            public void onCancelled(DatabaseError error) {

                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        SharedPreferences sp1 = this.getSharedPreferences("Login", MODE_PRIVATE);

         oldlog = sp1.getString("username", "");
         oldpass = sp1.getString("password", "zakaria") ;



        if (oldlog != null && oldpass != null) {

            loginveri test = new loginveri(oldlog, oldpass);
            test.execute();


        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void login(View view) throws Throwable {
        log = login.getText().toString();
        pass = password.getText().toString();
        if ((log.length() > 0) && (pass.length() > 0) && lancer == false) {

            if (ConnexionInternet.isConnectedInternet(Login.this)) {
                loginveri test = new loginveri(log, pass);
                test.execute();
                lancer = true;


            } else {
                Toast.makeText(this, "Verifie Votre Connexion SVP", Toast.LENGTH_LONG).show();
                lancer = false;
            }
        }
    }


    /*************************************************************************************************/

    public class loginveri extends AsyncTask<String, String, String> {
boolean flag=false;
        String user = null;
        String motd = null;
        String reponse = null;

        JSONObject ja;
        URL url;
        HttpURLConnection httpURLConnection;
        String lien;
        InputStream inputStream;
        BufferedReader bufferead;
        String data = "";
        String line = "";

        loginveri(String A, String B) {
            user = A;
            motd = B;
        }


        @Override
        protected String doInBackground(String... strings) {
            if (ConnexionInternet.isConnectedInternet(Login.this)) {
                    flag=true;
                try {

                    lien = "http://" + getString(R.string.URL) + "/sensor/android/login.php?login=" + user + "&password=" + motd;

                    url = new URL(lien);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    inputStream = httpURLConnection.getInputStream();
                    bufferead = new BufferedReader(new InputStreamReader(inputStream));


                    while (line != null) {
                        line = bufferead.readLine();
                        data = data + line;
                    }

                    //   bufferead.close();


                } catch (MalformedURLException e) {
                } catch (IOException e) {


                }
            }
                return null;
            }


        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (flag) {
                try {
                    JSONObject ja = new JSONObject(data);

                    String reponse = (String) ja.getString("resultat");


                    if (reponse.equals("1")) {

                        String id = (String) ja.getString("id");
                        String nom = (String) ja.getString("nom");
                        String prenom = (String) ja.getString("prenom");


                        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
                        SharedPreferences.Editor Ed = sp.edit();
                        Ed.putString("username", user);
                        Ed.putString("password", pass);

                      //  Ed.apply();
                        Ed.commit();


                        Intent A = new Intent(getApplicationContext(), liste.class);
                        Bundle B = new Bundle();
                        B.putString("id", id);
                        B.putString("nom", nom);
                        B.putString("prenom", prenom);


                        A.putExtras(B);
                        startActivity(A);
                        finish();


                    } else {

                        Toast.makeText(getApplicationContext(), "Login Or Password Incorrecte", Toast.LENGTH_LONG).show();
                        password.setText("");
                        login.setText("");
                        log = null;
                        pass = null;
                        lancer = false;


                    }


                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Data null", Toast.LENGTH_LONG).show();
                }
            }else
            {
                Toast.makeText(getApplicationContext(), "Verifie votre connexion", Toast.LENGTH_LONG).show();

            }
        }

    }


    class updatee extends Thread {
        boolean flag = false;


        int i = 0;

        public void run() {

            while (i < 100) {


                i = i + 1;
                try {
                    Thread.sleep(4);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // mWaveLoadingView.setTopTitle(String.valueOf(i)+"%");
                            // mWaveLoadingView.setProgressValue(i);
                        }
                    });
                } catch (Exception ex) {


                }
            }


        }
    }

}
