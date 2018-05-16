package com.example.zackprod.test;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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


public class Historique extends Fragment {
    public ArrayList<String> temp = new ArrayList<String>();
    public ArrayList<String> hum = new ArrayList<String>();
    public ArrayList<String> humsol = new ArrayList<String>();
    public ArrayList<String> DateTime = new ArrayList<String>();
    public int size;

    TextView temphist, humhist, humsolhist, timehist;
    ListView T;
    String[] Ssalam = {"salam", "cava"};
    public SensorInformation S;
    public TextView testtttt;
    Calendar cal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_historique, container, false);
        // getActivity().getActionBar().setTitle("Historique");

        S = (SensorInformation) getActivity();
        T = (ListView) rootView.findViewById(R.id.historiqueliste);
        if (ConnexionInternet.isConnectedInternet(getActivity())){
            get_Historique hist = new get_Historique();
            hist.execute();
        }else
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("Connexion");
            alert.setMessage("Connecter vous svp");
            alert.show();
        }
        return rootView;
    }


    class Customadapter extends BaseAdapter {
        Context context;

        Customadapter(Context C) {
            context = C;
        }

        @Override
        public int getCount() {
            return size;
        }

        @Override
        public Object getItem(int i) {
            return DateTime.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.liste_historique, viewGroup, false);


            temphist = (TextView) row.findViewById(R.id.TEMP);
            humhist = (TextView) row.findViewById(R.id.HUM);
            humsolhist = (TextView) row.findViewById(R.id.HUMSOL);
            timehist = (TextView) row.findViewById(R.id.TIME);

            temphist.setText(temp.get(i));
            humhist.setText(hum.get(i));

            humsolhist.setText(humsol.get(i));

            timehist.setText(DateTime.get(i));


            return row;

        }

        @Nullable
        @Override
        public CharSequence[] getAutofillOptions() {
            return new CharSequence[0];
        }
    }


    public class get_Historique extends AsyncTask<String, String, String> {
        String data = "";
        String line = "";
        JSONObject ja;


        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {

            try {

                String lien = "http://" + getString(R.string.URL) + "/sensor/historique/select.php?id=" + S.idplante;
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
            } catch (JSONException e) {
                Toast.makeText(S, "Data to Array", Toast.LENGTH_LONG).show();

            }


            size = ta.length();
            for (int i = 0; i < ta.length(); i++) {

                try {
                    JSONObject C = ta.getJSONObject(i);
                    temp.add((String) C.getString("temp"));


                    hum.add((String) C.getString("hum"));

                    humsol.add((String) C.getString("humsol"));
                    DateTime.add((String) C.getString("time"));


                } catch (JSONException e) {
                    Toast.makeText(S, "erreur Json", Toast.LENGTH_LONG).show();
                }

            }

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            T.setAdapter(new Customadapter(getActivity()));

        }
    }


}