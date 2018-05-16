package com.example.zackprod.test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ServiceCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.example.zackprod.test.SensorInformation.nomplante;


@SuppressLint("ValidFragment")
public class information extends Fragment implements OnMapReadyCallback {
    public static TextView hum;
    public static TextView humsol;

    public static TextView temp;
    TextView client, ipplante;
public static Context contexte;
    private GoogleMap googleMap;
    float lat, lon;
    private GoogleMap mMap;
    public MapView mapview;
    public static TextView etatpompe;
    public static ImageView picetat;
    public static int i=0;

    String city;
    LatLngBounds.Builder builder = new LatLngBounds.Builder();


    public information(String A, String B, String C) {
        lat = Float.parseFloat(A);
        lon = Float.parseFloat(B);
        city = C;


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_information, container, false);
        hum = (TextView) v.findViewById(R.id.hum2);
        FragmentActivity mycontext = (FragmentActivity) getActivity();
        temp = (TextView) v.findViewById(R.id.temp);
        humsol = (TextView) v.findViewById(R.id.humsol);
        client = (TextView) v.findViewById(R.id.plante);
        etatpompe=(TextView) v.findViewById(R.id.pomp);
        picetat=(ImageView)v.findViewById(R.id.picpmp);
        SensorInformation S = (SensorInformation) getActivity();
        client.setText(S.idplante + " - " + S.nomplante + " / " + S.city);
        lat = Float.parseFloat(S.lat);
        lon = Float.parseFloat(S.lon);
        getActivity().setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        contexte=getContext();
        i=0;



        if(parsing.startservice==false)
        {
            parsing.startservice=true;
            Intent intent=new Intent(getActivity(),parsing.class);
            getActivity().startService(intent);

        }

        mapview = (MapView) v.findViewById(R.id.map);
        mapview.onCreate(savedInstanceState);

        mapview.getMapAsync(this);


        return v;
    }

    public void onPause() {
        super.onPause();
        parsing.startservice=false;


    }

    public void onStop() {
        super.onStop();
        parsing.startservice=false;


    }

    public void onStart() {
        super.onStart();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(this.getActivity());

        if (googleMap != null) {
            mMap = googleMap;

            // Add a marker in Sydney and move the camera
            LatLng sydney = new LatLng(lat, lon);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));
            Marker marker = mMap.addMarker(new MarkerOptions().position(sydney).title(city));
            builder.include(marker.getPosition());


        }
    }

    @Override
    public void onResume() {
        mapview.onResume();
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        super.onDestroy();


        mapview.onDestroy();
    }
    public static void alert() {
        if(i<1) {
            AlertDialog.Builder alert = new AlertDialog.Builder(contexte);
            alert.setTitle("Plante  non connecter");
            alert.setMessage("Plante " + nomplante + " n'est pas joignable ");
            alert.show();
            parsing.startservice = false;
            i++;
        }
    }

}


