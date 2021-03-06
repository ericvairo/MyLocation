package com.example.eric.mylocation;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;



public class MainActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    private TextView tvCoordinate;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvCoordinate = (TextView) findViewById(R.id.tv_coordinate);

        callConnection();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(mGoogleApiClient !=null && mGoogleApiClient.isConnected()){
            startLocationUpdate();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if(mGoogleApiClient != null){
            stopLocationUpdate();
        }
    }

    private synchronized void callConnection(){

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

    }

    private void initLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdate(){
        initLocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, MainActivity
                .this);
    }

    private void stopLocationUpdate(){

        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,MainActivity.this);
    }


    //LISTENER
    @Override
    public void onConnected(Bundle bundle) {

        Log.i("LOG","onConnected(" +bundle + ")");
        Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(l != null )
        {
            Log.i("LOG", "latitude: "+ l.getLatitude());
            Log.i("LOG", "longitude: " + l.getLongitude());
            tvCoordinate.setText(l.getLatitude()+" | "+l.getLongitude());

        }else{
            tvCoordinate.setText("nao sei | nao sei ");
        }
        startLocationUpdate();


    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("LOG","onConnectionSuspended(" + i + ")");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("LOG","onConnectionFailed("+connectionResult+")");
    }

    @Override
    public void onLocationChanged(Location location) {
        tvCoordinate.setText(Html.fromHtml("Latitude: "+location.getLatitude()+"<br/>"+
                        "Longitude: "+location.getLongitude()+"<br/>"+
                        "Bearing: "+location.getBearing()+"<br/>"+
                        "Altitude: "+location.getAltitude()+"<br/>"+
                        "Speed: "+location.getSpeed()+"<br/>"+
                        "Provider: "+location.getProvider()+"<br/>"+
                        "Accuracy: "+location.getAccuracy()+"<br/>"+
                        "Date: "+ DateFormat.getTimeInstance().format(new Date())+"<br/>"));

    }
}
