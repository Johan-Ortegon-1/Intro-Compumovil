package com.example.basiclocalization;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class Gps extends AppCompatActivity {
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private TextView txtVLon;
    private TextView txtVLat;
    private TextView txtVAlt;
    static final int LOCATION_REQUEST = 1, REQUEST_CHECK_SETTINGS = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        txtVLon = findViewById(R.id.txtVLongitud);
        txtVLat = findViewById(R.id.txtVLatitud);
        txtVAlt = findViewById(R.id.txtVAltitud);
        mLocationRequest = createLocationRequest();
        mLocationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult)
            {
                Location location = locationResult.getLastLocation();
                Log.i("LOCATION","Location update in the callback: " + location);
                if(location != null)
                {
                    txtVLon.setText("Longitud: " + String.valueOf(location.getLongitude()));
                    txtVLat.setText("Latitud: " + String.valueOf(location.getLatitude()));
                    txtVAlt.setText("Altura: " + String.valueOf(location.getAltitude()));
                }
            }
        };

        if(requestPermisision(this, Manifest.permission.ACCESS_FINE_LOCATION, "Es necesario acceder a su localizacion", LOCATION_REQUEST))
        {
            usarGps();
        }
    }

    private void startLocationUpdates()
    {
        if(ContextCompat.checkSelfPermission(this ,Manifest.permission. ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED)
        {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
            SettingsClient client = LocationServices.getSettingsClient(this);
            Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

            task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                @Override
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    startLocationUpdates();
                }
            });

            task.addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch(statusCode)
                    {
                        case CommonStatusCodes.RESOLUTION_REQUIRED:
                            try{
                                ResolvableApiException resolvable = (ResolvableApiException) e;
                                resolvable.startResolutionForResult(Gps.this, REQUEST_CHECK_SETTINGS);
                            }catch(IntentSender.SendIntentException sendEx)
                            {
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            });
        }
    }

    private void stopLocationUpdates()
    {
        if(mFusedLocationClient != null)
        {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    protected void onActivityResult (int requestCode , int resultCode , Intent data)
    {
        switch (requestCode)
        {
            case REQUEST_CHECK_SETTINGS :
            {
                if (resultCode == RESULT_OK )
                {
                    startLocationUpdates(); //Se encendi ó la localización!!!
                }
                else
                {
                    Toast.makeText(this,"Sinacceso a localización , hardware deshabilitado !",Toast. LENGTH_LONG ).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        startLocationUpdates();
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        stopLocationUpdates();
    }

    private LocationRequest createLocationRequest()
    {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    private boolean requestPermisision(Activity contexto, String permiso, String justificacion, int codigo)
    {
        if(ContextCompat.checkSelfPermission(contexto, permiso)!= PackageManager.PERMISSION_GRANTED)
        {
            //Justification of the permission
            if(ActivityCompat.shouldShowRequestPermissionRationale(contexto,permiso))
            {
                Toast.makeText(getApplicationContext(),justificacion, Toast.LENGTH_LONG).show();
            }
            ActivityCompat.requestPermissions(contexto, new String[]{permiso},codigo);
            return false;
        }
        else
            return true;
    }

    public void onRequestPermissionsResult(int reqCode, String permisos[], int[] grantResul)
    {
        switch (reqCode)
        {
            case LOCATION_REQUEST:
            {
                if(grantResul.length > 0 && grantResul[0] == PackageManager.PERMISSION_GRANTED)
                {
                    usarGps();
                }
                break;
            }
        }
    }

    public void usarGps()
    {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this ,
                new OnSuccessListener<Location>()
                {
                    @Override
                    public void onSuccess(Location location)
                    {
                        Log. i ("LOCATION","onSuccess location");
                        if (location != null )
                        {
                            txtVLon.setText("Longitud:" + location.getLongitude());
                            txtVLat.setText("Latitud:" + location.getLatitude());
                            txtVAlt.setText("Altitud:" + location.getAltitude());
                            Log. i (" LOCATION ", "Longitud:" + location.getLongitude());
                            Log. i (" LOCATION ", "Latitud:" + location.getLatitude());
                        }
                        else
                        {
                            txtVLon.setText("Something goes wrong" );
                            txtVLat.setText("Something goes wrong");
                            txtVAlt.setText("Something goes wrong");
                        }
                    }
                });
    }

}
