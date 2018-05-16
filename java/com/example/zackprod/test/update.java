package com.example.zackprod.test;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

public class update extends Fragment {
    TextView id;
    EditText temp, hum, humsol, nom, arrossage;
    Button B;
    CheckBox etatDarr;
    public static SensorInformation S;

    public update() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_update, container, false);
        S = (SensorInformation) getActivity();
        temp = (EditText) rootView.findViewById(R.id.temp);
        B = (Button) rootView.findViewById(R.id.valider);
        hum = (EditText) rootView.findViewById(R.id.hum2);
        nom = (EditText) rootView.findViewById(R.id.nom);
        humsol = (EditText) rootView.findViewById(R.id.humsol);
        arrossage = (EditText) rootView.findViewById(R.id.arrosage);
        ConstraintLayout clickableArea = (ConstraintLayout) rootView.findViewById(R.id.layout);
        etatDarr = (CheckBox) rootView.findViewById(R.id.checkBox);

        temp.setText(S.temp);
        hum.setText(S.hum);
        nom.setText(S.nom);
        humsol.setText(S.humsol);
        if (S.with_time.equals("1")) {
            etatDarr.setChecked(true);
            arrossage.setEnabled(true);
            arrossage.setText(S.time);

        } else {
            etatDarr.setChecked(false);
            arrossage.setEnabled(false);


        }


        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String new_hum, new_nom, new_temp, new_humsol, new_withtime, new_time;
                new_nom = nom.getText().toString();
                new_temp = temp.getText().toString();
                new_hum = hum.getText().toString();
                new_humsol = humsol.getText().toString();
                new_time = arrossage.getText().toString();
                if (etatDarr.isChecked()) {
                    new_withtime = "1";
                } else {
                    new_withtime = "0";
                    new_time = "0";
                }
                if (Integer.valueOf(new_temp) > 100 || Integer.valueOf(new_hum) > 100 || Integer.valueOf(new_humsol) > 100 || Integer.valueOf(new_temp) == 0 || Integer.valueOf(new_hum) == 0 || Integer.valueOf(new_humsol) == 0) {

                    //Toast.makeText(getActivity(), "parrametre invalide ", Toast.LENGTH_LONG).show();


                    temp.setText(S.temp);
                    hum.setText(S.hum);
                    nom.setText(S.nom);
                    humsol.setText(S.humsol);
                    arrossage.setText(S.time);
                    if (S.with_time.equals("1")) {
                        etatDarr.setChecked(true);
                    } else {
                        etatDarr.setChecked(false);

                    }

                } else {


                    updatedata D = new updatedata(S.idplante, new_nom, new_temp, new_hum, new_humsol, new_withtime, new_time);
                    D.execute();

                    S.temp = new_temp;
                    S.hum = new_hum;
                    S.humsol = new_humsol;
                    S.time = new_time;
                    S.with_time = new_withtime;
                    S.nom = new_nom;

                }
            }
        });
        etatDarr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (etatDarr.isChecked()) {
                    arrossage.setText(S.time);

                    arrossage.setEnabled(true);
                } else {
                    arrossage.setText("");

                    arrossage.setEnabled(false);
                }
            }
        });


        return rootView;
    }


    public class updatedata extends AsyncTask<String, String, String> {

        String nom = null;
        String hum = null;
        String temp = null;
        String time = null;
        String with_time = null;
        String humsol = null;

        String reponse = null;
        int flag = -1;

        JSONObject ja;
        URL url;
        HttpURLConnection httpURLConnection;
        String lien;
        InputStream inputStream;
        BufferedReader bufferead;
        String idplante;
        String data = "";
        String line = "";

        updatedata(String id, String A, String B, String C, String D, String E, String F) {
            idplante = id;
            nom = A;
            temp = B;
            hum = C;
            humsol = D;
            with_time = E;
            time = F;
        }

        public int verification_send_data() {
            return flag;
        }


        @Override
        protected String doInBackground(String... strings) {

            try {


                lien = "http://" + getString(R.string.URL) + "/sensor/android/update.php?nom=" + nom + "&idplante=" + idplante + "&temp=" + temp + "&hum=" + hum + "&humsol=" + humsol + "&time=" + time + "&withtime=" + with_time;

                url = new URL(lien);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                inputStream = httpURLConnection.getInputStream();
                bufferead = new BufferedReader(new InputStreamReader(inputStream));


                while (line != null) {
                    line = bufferead.readLine();
                    data = data + line;
                }
                ja = new JSONObject(data);


                reponse = (String) ja.getString("resultat");
                if (reponse.equals("1")) {
                    flag = 1;
                } else {
                    flag = 0;
                }


            } catch (MalformedURLException e) {
                flag = 0;
                e.printStackTrace();
            } catch (IOException e) {
                flag = 0;
                e.printStackTrace();

            } catch (JSONException e) {
                flag = 0;
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (flag == 0) {
                Toast.makeText(getActivity(), "Erreur", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "Valider ^_^", Toast.LENGTH_LONG).show();
/*

 *
               Fragment fragment = new information(S.lat,S.lon,S.city);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.zack, fragment)
                ft.commit();
                S.change_item(R.id.nav_camera);

                */

            }

            Intent A = new Intent(getActivity(), liste.class);
            Bundle B = new Bundle();
            B.putString("id", S.idclient);
            B.putString("nom", S.nomclient);
            B.putString("prenom", S.prenomclient);

            A.putExtras(B);
            startActivity(A);

            getActivity().finish();

        }


    }
}

