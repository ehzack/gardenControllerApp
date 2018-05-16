package com.example.zackprod.test;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import static com.example.zackprod.test.SensorInformation.nomplante;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        if (intent.getAction().equalsIgnoreCase("com.exemple.Zackprod")) {

         if(bundle.getBoolean("flag"))  {
            information.temp.setText(bundle.getString("temp") + "°c");
            information.hum.setText(bundle.getString("hum") + "%");
            information.humsol.setText(bundle.getString("humsol") + "%");


            if (bundle.getInt("work") == 1) {
                int id = context.getResources().getIdentifier("staricon", "drawable", context.getPackageName());

                information.picetat.setImageResource(id);
                information.etatpompe.setText("Pompe est démarrer");


                if (work.notifwork == false) {
                    work w = new work();
                    w.notify(context, "La pompe est démarrer", 123);
                    work.notifnotwork = false;
                    work.notifwork = true;
                }
            } else {
                int id = context.getResources().getIdentifier("stopicon", "drawable", context.getPackageName());
                information.picetat.setImageResource(id);

                information.etatpompe.setText("Pompe est arreter");


                if (work.notifnotwork == false) {
                    work w = new work();
                    w.notify(context, "la pompe est arreter", 123);
                    work.notifwork = false;
                    work.notifnotwork = true;
                }

            }
        }else{

             information.alert();

         }
    }
    }
}
