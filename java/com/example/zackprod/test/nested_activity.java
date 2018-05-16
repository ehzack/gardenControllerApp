package com.example.zackprod.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class nested_activity extends Fragment {
    Button global, pompe, libre;
    ConstraintLayout A;

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_nested_activity, container, false);


        global = (Button) rootView.findViewById(R.id.global);
        pompe = (Button) rootView.findViewById(R.id.pompe);
        libre = (Button) rootView.findViewById(R.id.libre);
        Fragment fragment = new Historique();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.zizo, fragment);
        ft.commit();
        global.setBackgroundColor(Color.parseColor("#7FFF4081"));
        pompe.setBackgroundColor(Color.parseColor("#FF4081"));

        libre.setBackgroundColor(Color.parseColor("#FF4081"));
Toast.makeText(getContext(),"les 30 dernier enregistrement ",Toast.LENGTH_LONG).show();




        global.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new Historique();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.zizo, fragment);
                ft.commit();
                global.setBackgroundColor(Color.parseColor("#7FFF4081"));
                pompe.setBackgroundColor(Color.parseColor("#FF4081"));

                libre.setBackgroundColor(Color.parseColor("#FF4081"));


            }
        });
        pompe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new HistoriquePompe();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.zizo, fragment);
                ft.commit();


                global.setBackgroundColor(Color.parseColor("#FF4081"));
                pompe.setBackgroundColor(Color.parseColor("#7FFF4081"));

                libre.setBackgroundColor(Color.parseColor("#FF4081"));

            }
        });

        libre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new HistoriqueLibre();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.zizo, fragment);
                ft.commit();
                global.setBackgroundColor(Color.parseColor("#FF4081"));
                pompe.setBackgroundColor(Color.parseColor("#FF4081"));

                libre.setBackgroundColor(Color.parseColor("#7FFF4081"));

            }
        });


        return rootView;
    }


    @Override
    public void onStop() {
        super.onStop();
        Toast.makeText(getContext(),"onstop",Toast.LENGTH_LONG).show();
      //  getActivity().getFragmentManager().popBackStack();

    }
}
