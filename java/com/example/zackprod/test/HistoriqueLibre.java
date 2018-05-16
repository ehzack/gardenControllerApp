package com.example.zackprod.test;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
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


public class HistoriqueLibre extends Fragment {
    EditText datedebut, datefin;
    ListView listefree;
    public ArrayList<String> temp = new ArrayList<String>();
    public ArrayList<String> hum = new ArrayList<String>();
    public ArrayList<String> humsol = new ArrayList<String>();
    public ArrayList<String> DateTime = new ArrayList<String>();
    public ArrayList<String> work = new ArrayList<String>();
    public boolean flag = false;
    public static String DATEDEBUT, TIMEDEBUT;
    public static String DATEFIN, TIMEFIN;
    int size;

    String requestDEBUT, requestFIN;
    SensorInformation Ac;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_historique_libre, container, false);
        datedebut = (EditText) v.findViewById(R.id.Datedebut);
        datefin = (EditText) v.findViewById(R.id.Datefin);
        listefree = (ListView) v.findViewById(R.id.listelibre);
        datedebut.setShowSoftInputOnFocus(false);
        datefin.setShowSoftInputOnFocus(false);
        Ac = (SensorInformation) getActivity();

        datedebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Date Debut");
                alert.setMessage("selection la Date");
                final CalendarView input = new CalendarView(getContext());
                alert.setView(input);
                input.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

                    @Override
                    public void onSelectedDayChange(CalendarView view, int year, int month,
                                                    int dayOfMonth) {
                        DATEDEBUT = year + "-" + (month + 1) + "-" + dayOfMonth;

                        DialogFragment newFragment = new TimePickerFragment();
                        newFragment.show(getActivity().getFragmentManager(), "timePicker");
                    }
                });

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        requestDEBUT = DATEDEBUT + " " + TIMEDEBUT;

                        Toast.makeText(getContext(), requestDEBUT, Toast.LENGTH_LONG).show();
                        datedebut.setText(requestDEBUT);

                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                alert.show();


            }
        });


        datefin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Date Fin");
                alert.setMessage("selection la Date");
                final CalendarView input = new CalendarView(getContext());
                alert.setView(input);
                input.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

                    @Override
                    public void onSelectedDayChange(CalendarView view, int year, int month,
                                                    int dayOfMonth) {
                        DATEFIN = year + "-" + (month + 1) + "-" + dayOfMonth;

                        DialogFragment newFragment = new TimePickerFragment2();
                        newFragment.show(getActivity().getFragmentManager(), "timePicker");
                    }
                });

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        requestFIN = DATEFIN + " " + TIMEFIN;

                        Toast.makeText(getContext(), requestFIN, Toast.LENGTH_LONG).show();
                        datefin.setText(requestFIN);
                        if (requestDEBUT.length() > 2) {
                            get_Historique_libre start = new get_Historique_libre();
                            start.execute();
                        } else {
                            Toast.makeText(getContext(), "Date Debut invalide", Toast.LENGTH_LONG).show();
                        }

                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                alert.show();


            }
        });


        return v;
    }


    public class get_Historique_libre extends AsyncTask<String, String, String> {
        String data = "";
        String line = "";
        JSONObject ja;


        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {
            temp.clear();
            hum.clear();
            humsol.clear();
            DateTime.clear();
            work.clear();

            try {

                String lien = "http://" + getString(R.string.URL) + "/sensor/historique/libre.php?id=" + Ac.idplante + "&&datedebut=" + requestDEBUT + "&datefin=" + requestFIN;
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

            if (flag) {
                size = ta.length();
                for (int i = 0; i < ta.length(); i++) {

                    try {
                        JSONObject C = ta.getJSONObject(i);
                        temp.add((String) C.getString("temp"));


                        hum.add((String) C.getString("hum"));

                        humsol.add((String) C.getString("humsol"));
                        DateTime.add((String) C.getString("time"));
                        work.add((String) C.getString("pompe"));


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
                listefree.setAdapter(new Customadapter(getActivity()));


            } else {

                Toast.makeText(getContext(), "Aucun enregistrement dans ce date", Toast.LENGTH_LONG).show();
            }
        }
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
            int id;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.listehistoriquelibre, viewGroup, false);


            TextView temphist = (TextView) row.findViewById(R.id.temp);
            TextView humhist = (TextView) row.findViewById(R.id.hum);
            TextView humsolhist = (TextView) row.findViewById(R.id.humsol);
            TextView timehist = (TextView) row.findViewById(R.id.time);
            ImageView image = (ImageView) row.findViewById(R.id.imagehist);


            temphist.setText(temp.get(i));
            humhist.setText(hum.get(i));

            humsolhist.setText(humsol.get(i));

            timehist.setText(DateTime.get(i));
            if (work.get(i).equals("1")) {
                id = getContext().getResources().getIdentifier("staricon", "drawable", getContext().getPackageName());

            } else {
                id = getContext().getResources().getIdentifier("stopicon", "drawable", getContext().getPackageName());

            }
            image.setImageResource(id);


            return row;

        }

        @Nullable
        @Override
        public CharSequence[] getAutofillOptions() {
            return new CharSequence[0];
        }
    }


}