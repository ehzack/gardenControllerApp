package com.example.zackprod.test;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;


public class HistoriquePompe extends Fragment {
    CalendarView S;
    String DATE = "";

    TextView pompe1, pompe2, humsol2, humsol1, time1, time2;
    ImageView ico1, ico2;


    SensorInformation activity;

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        

        View v = inflater.inflate(R.layout.fragment_historique_pompe, container, false);
        activity = (SensorInformation) getActivity();
        ico1 = v.findViewById(R.id.iconstar);
        ico2 = v.findViewById(R.id.iconstop);



        pompe1 = v.findViewById(R.id.pompdemar);

        pompe2 = v.findViewById(R.id.pompearret);


        humsol2 = v.findViewById(R.id.humsolarret);

        humsol1 = v.findViewById(R.id.humsoldemar);

        time1 = v.findViewById(R.id.timedemar);

        time2 = v.findViewById(R.id.timearret);


        S = (CalendarView) v.findViewById(R.id.calendarView);

        get_hist_pompe hist = new get_hist_pompe();
        hist.execute();
        S.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                Button S = new Button(getContext());
                DATE = year + "-" + (month + 1) + "-" + dayOfMonth;
                get_hist_pompe hist = new get_hist_pompe();
                hist.execute();

            }
        });
        return v;
    }


    public class get_hist_pompe extends AsyncTask<String, String, String> {
        String data = "";
        String line = "";
        JSONObject ja;
        boolean flag = false;
        int size;
        ArrayList<String> humsol = new ArrayList<String>();
        ArrayList<String> time = new ArrayList<String>();
        ArrayList<String> pompe = new ArrayList<String>();

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {

            try {

                String lien = "http://" + getString(R.string.URL) + "/sensor/historique/pompe.php?id=" + activity.idplante + "&date=" + DATE;
                URL url = new URL(lien);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferead = new BufferedReader(new InputStreamReader(inputStream));


                while (line != null) {
                    line = bufferead.readLine();
                    data = data + line;
                }


            } catch (MalformedURLException e) {
            } catch (IOException e) {

            } catch (Exception e) {


            }


            JSONArray ta = null;
            try {
                ta = new JSONArray(data);
                flag = true;
            } catch (JSONException e) {
                flag = false;
            }

            if (flag == true) {

                flag = true;
                size = ta.length();
                for (int i = 0; i < ta.length(); i++) {

                    try {
                        JSONObject C = ta.getJSONObject(i);
                        humsol.add((String) C.getString("humsol"));
                        pompe.add((String) C.getString("pompe"));
                        time.add((String) C.getString("time"));


                    } catch (JSONException e) {
                    }

                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (flag) {
                if (pompe.size() > 1) {

                    if (pompe.get(0).equals("1")) {
                        int id = getContext().getResources().getIdentifier("staricon", "drawable", getContext().getPackageName());
                        pompe1.setText("Pompe est Démarrer");
                        ico1.setImageResource(id);
                        int ids = getContext().getResources().getIdentifier("stopicon", "drawable", getContext().getPackageName());
                        pompe2.setText("Pompe est Arréter");
                        ico2.setImageResource(ids);
                    } else {
                        int id = getContext().getResources().getIdentifier("staricon", "drawable", getContext().getPackageName());
                        pompe1.setText("Pompe est Arréter");
                        int ids = getContext().getResources().getIdentifier("stopicon", "drawable", getContext().getPackageName());
                        pompe2.setText("Pompe est Démarrer");
                        ico2.setImageResource(id);
                        ico1.setImageResource(ids);


                    }


                    time1.setText(time.get(0));
                    time2.setText(time.get(1));
                    humsol1.setText(humsol.get(0));
                    humsol2.setText(humsol.get(1));

                } else {

                    if (pompe.get(0).equals("1")) {
                        int id = getContext().getResources().getIdentifier("staricon", "drawable", getContext().getPackageName());
                        pompe1.setText("Pompe est Démarrer");
                        ico1.setImageResource(id);

                    } else {
                        int id = getContext().getResources().getIdentifier("stopicon", "drawable", getContext().getPackageName());
                        pompe1.setText("Pompe est Arréter");
                        ico1.setImageResource(id);


                    }

                    time1.setText(time.get(0));
                    humsol1.setText(humsol.get(0));
                    ico2.setImageResource(0);
                    time2.setText("");
                    humsol2.setText("");
                    pompe2.setText("");


                }


            } else {
                Toast.makeText(getActivity(), "aucun enregistrement sur ce date", Toast.LENGTH_LONG).show();

                time1.setText("");
                humsol1.setText("");
                ico2.setImageResource(0);
                ico1.setImageResource(0);
                time2.setText("");
                humsol2.setText("");
                pompe2.setText("");
                pompe1.setText("");

            }


        }
    }

    @Override
    public void onStop() {
        super.onStop();

    }
}



