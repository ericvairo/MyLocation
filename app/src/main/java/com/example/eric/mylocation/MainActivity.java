package com.example.eric.mylocation;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;


public class MainActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private TextView tvCoordinate;
    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvCoordinate = (TextView) findViewById(R.id.tv_coordinate);

        callConnection();
    }

    private synchronized void callConnection(){

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

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


    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("LOG","onConnectionSuspended(" + i + ")");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("LOG","onConnectionFailed("+connectionResult+")");
    }
}
