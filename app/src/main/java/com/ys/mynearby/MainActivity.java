package com.ys.mynearby;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.ys.interfaces.IMyHandlerListener;
import com.ys.interfaces.WSFetchListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, WSFetchListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, IMyHandlerListener {

    GoogleApiClient mGoogleApiClient;
    PlaceLocation mCurrentLocation;

    TextView textView;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        MyHandler myHandler = MyHandler.getHandler();
        myHandler.setMyHandlerListener(this);

        textView = (TextView) findViewById(R.id.textView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
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
        getMenuInflater().inflate(R.menu.main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_MapView) {
            // Handle the camera action
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_Home) {

            URLFetcher urlFetcher = URLFetcher.getURLFetcher();
            urlFetcher.startFetchURLBackground(urlFetcher.buildURL(this, mCurrentLocation, "atm"), this);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    int DATA_RECEIVED = 1;

    @Override
    public void onWSResponse(String data) {
        Log.d("MNB", data);
//        textView.setText(String.valueOf(data));
//        textView.invalidate();
        Message message = new Message();
        message.what = DATA_RECEIVED;
        message.obj = data;
        MyHandler.getHandler().sendMessage(message);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkPermissionReq();
        return;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Variables.LOCATION_REQ ) {
            boolean isGranted = false;
            for (int i =0; i<permissions.length;i++){
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED){
//                    permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    isGranted = true;
                    break;
                }
            }
            if (isGranted){
                checkPermissionReq();
            }

        }

//        if (requestCode == Variables.LOCATION_REQ && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
////                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
////                startActivityForResult(intent,GPS_ENABLE_REQ);
//                GPSEnableDialog gpsEnableDialog = new GPSEnableDialog(this);
//                gpsEnableDialog.show();
//            }
////
//        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = new PlaceLocation(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
        Log.d("MNB", mCurrentLocation.lat + " " + mCurrentLocation.lng);

        textView.setText(" lat : "+ mCurrentLocation.lat +"  lan : "+mCurrentLocation.lng);

    }


    /*Cutom methods*/
    public void checkPermissionReq() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, Variables.LOCATION_REQ);

        } else {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (location == null) {
                location = new Location("");
                location.setLatitude(0.0);
                location.setLongitude(0.0);
            }

            mCurrentLocation = new PlaceLocation(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));

            textView.setText(" lat : "+ mCurrentLocation.lat +"  lan : "+mCurrentLocation.lng);

            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(5000);
            locationRequest.setFastestInterval(1000);
//            locationRequest.setMaxWaitTime(1000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
        }
    }

    @Override
    public void onHandlerListener(Object obj) {
        textView.setText(String.valueOf(obj));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("MNB","req : "+requestCode+"  res: "+resultCode);
        if (data!=null){
            Log.d("MNB","  data : "+data.getExtras().toString());
        }
    }
}
//    I currently work as an IT recruiter within HR at a large company. I'm planning on eventually starting my own IT recruiting firm. How ethical i...
//        If I join a startup and it fails, what can be plan B for me? Which kind of companies hire such employees?
//        Does a company needs to register under EPF if one of the group companies got registered?
//        I'm 20 years old, I don't want to work under anyone and I am not a business-minded person either. What should I do?
//        If I have been working on a long-term project in a company which is under NDA, what would I explain about it if I apply to another company?
//        How can we set up an employee friendly environment for a startup under 25lac?
//        I belong to OBC but I work for company with package <6lpa.but my husband salary is >6lpa. Can I apply under OBC NON-creamy layer?
//
//        Related Questions
//
//        Is one person company advisable for online startup?
//        How do I choose the decision on where to work? Should I work for a startup or for an established company under a contract?
//        I'm an MCA fresher. I planned to start my career in a new startup company. Will this help me to improve my career?
//        My co-founder is suggesting me to bring our startup under the umbrella of another company he is already part of. Should I agree to that?
//        Am I the only one who hates to work under others?
//        If I go for applying geology at IIT KGP will I get a job only under oil companies? Is the working condition for such jobs harsh?
//        Will any company recruit me?
//        I'm an employee of a startup company but now people leave there's just me and my boss. Is it a sign of a failure startup. Should I leave too?
//        Is it possible for a CA in employment (not having COP) to work under a CA firm as an employee?
//        Is it ok to work in a company where system is under developing stage?


