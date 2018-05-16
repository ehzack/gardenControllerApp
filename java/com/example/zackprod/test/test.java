package com.example.zackprod.test;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class test extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int i=0 ;i<4;i++)
           {

               Toast.makeText(this,"hello"+i,Toast.LENGTH_LONG).show();
               try {
                   Thread.sleep(5000);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }

           }
        return super.onStartCommand(intent, flags, startId);

    }
}
