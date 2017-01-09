package com.ys.mynearby;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,LocationListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMyLocationButtonClickListener{

    private GoogleMap mMap;
    Marker currLocationMarker;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    final int LOCATION_REQ = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMaxZoomPreference(22);
        mMap.setMinZoomPreference(12);

        // Add a marker in Sydney and move the camera
        LatLng Pune = new LatLng(18.5204, 73.8567);
        MarkerOptions markerOptions = new MarkerOptions().position(Pune).title("Marker in Pune");
        mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate2 = CameraUpdateFactory.zoomTo(20);
        mMap.moveCamera(cameraUpdate2);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(Pune);
        mMap.animateCamera(cameraUpdate);

        LatLng rahulComPune = new LatLng(18.5089, 73.8121);
        Log.d("MNB",rahulComPune.toString());

        if (mCurrentLocation !=null) {
            Location location = new Location(""); //provider name
            location.setLatitude(mCurrentLocation.getLatitude());
            location.setLongitude(mCurrentLocation.getLongitude());
            showCurrentmarker(location);
        }



//        checkPermissionReq();

//        PolylineOptions polylineOptions = new PolylineOptions();
//        polylineOptions.add(rahulComPune);
//        polylineOptions.add(Pune);
//
//        mMap.addPolyline(polylineOptions);
    }

    @Override
    public void onLocationChanged(Location location) {
        showCurrentmarker(location);
    }

    /*Cutom methods*/
    public void checkPermissionReq(){
        Log.d("MNB","checkPermissionReq");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            String [] permissions = {"android.Manifest.permission.ACCESS_FINE_LOCATION",
                    android.Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions,LOCATION_REQ );
        }else {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (location != null) {
//                mCurrentLocation = new PlaceLocation(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                mMap.setMyLocationEnabled(true );
                mMap.setOnMyLocationButtonClickListener(this);

                showCurrentmarker(location);
            } else {
//                mCurrentLocation = new PlaceLocation("","");
            }

            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(5000);
            locationRequest.setFastestInterval(500);
//            locationRequest.setSmallestDisplacement(1);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
        }
    }

    public void showCurrentmarker(Location location){
        //place marker at current position
        //mGoogleMap.clear();
        Log.d("MNB","Showing current location");
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }
        mMap.setMaxZoomPreference(22);
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_marker2));
        currLocationMarker = mMap.addMarker(markerOptions);

//        mCurrentLocation = new PlaceLocation(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
        mCurrentLocation = location;

        CameraUpdate cameraUpdate2 = CameraUpdateFactory.zoomTo(20);
        mMap.moveCamera(cameraUpdate2);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude()));
        mMap.animateCamera(cameraUpdate);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkPermissionReq();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

//    @Override
//    public boolean onMyLocationButtonClick() {
//        showCurrentmarker(mCurrentLocation);
//        return true;
//    }
}
