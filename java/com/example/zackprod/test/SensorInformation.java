package com.example.zackprod.test;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;



public class SensorInformation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static Fragment fragment = null;
    ;
    public static String nom, id, nomplante, city, ip, idplante, temp, hum, humsol, time, with_time, lat, lon, idclient, nomclient, prenomclient;
    ImageView image;
    Bitmap picture;
    boolean S = false;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Bundle info = getIntent().getExtras();


        nomplante = info.getString("nomplante");
        temp = info.getString("temp");
        hum = info.getString("hum");
        humsol = info.getString("humsol");
        nom = info.getString("nomplante");
        time = info.getString("time");
        with_time = info.getString("with_time");
        lat = info.getString("lat");
        lon = info.getString("lon");
        city = info.getString("city");
        ip = info.getString("ip");
        idplante = info.getString("id");
        idclient = info.getString("idclient");
        nomclient = info.getString("nomclient");
        prenomclient = info.getString("prenomclient");
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //byte[] byteArray = info.getByteArray("photo");
        //picture = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        //image.setImageBitmap(picture);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Fragment information = new information(lat, lon, city);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.zack, information);
        ft.commit();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sensor_information, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            SharedPreferences sp1 = getApplication().getSharedPreferences("Login", MODE_PRIVATE);
            sp1.edit().clear().commit();
            finish();
            ActivityCompat.finishAffinity(this);


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int idd = item.getItemId();


        if (idd == R.id.nav_camera) {
            if (fragment instanceof information) {


            } else {
                fragment = new information(lat, lon, city);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.zack, fragment);
                ft.commit();
            }


        } else if (idd == R.id.nav_gallery) {


            fragment = new update();

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.zack, fragment);
            ft.commit();


        } else if (idd == R.id.nav_slideshow) {

            Toast.makeText(getApplicationContext(), " n'y a-t-il aucune photo ^_^ ", Toast.LENGTH_LONG).show();


        } else if (idd == R.id.historiquecomplet) {

                fragment = new nested_activity();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.zack, fragment);
                ft.commit();



        } else if (idd == R.id.nav_send) {
            SharedPreferences sp1 = getApplication().getSharedPreferences("Login", MODE_PRIVATE);
            sp1.edit().clear().commit();
            ActivityCompat.finishAffinity(this);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public void change_item(int id) {
        navigationView.setCheckedItem(id);

    }


}
