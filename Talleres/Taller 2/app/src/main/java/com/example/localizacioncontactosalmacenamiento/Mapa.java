package com.example.localizacioncontactosalmacenamiento;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOError;
import java.io.IOException;
import java.util.List;


public class Mapa extends FragmentActivity  implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    /*Localization things*/
    private double latitud;
    private double longitud;
    static final int LOCATION_REQUEST = 8;

    /*Light sensors*/
    SensorManager sensorManager;
    Sensor lightSensor;
    SensorEventListener lightSensorListener;

    /*Searching*/
    private EditText edtBuscarDireccion;
    private String direccionBuscar;
    private Geocoder mGeocoder;
    public static final double lowerLeftLatitude = 1.396967;
    public static final double lowerLeftLongitude = -78.903968;
    public static final double upperRightLatitude = 11.983639;
    public static final double upperRigthLongitude = -71.869905;
    static final int RADIUS_OF_EARTH_KM = 6371;
    public Mapa() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //Get current location
        if(requestPermisision(this, Manifest.permission.ACCESS_FINE_LOCATION, "Lo necesita la App", LOCATION_REQUEST))
            usarGps();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Active additional sensors
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(mMap != null)
                {
                    if(event.values[0] < 5000)
                    {
                        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(Mapa.this, R.raw.style_json));
                    }
                    else
                    {
                        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(Mapa.this, R.raw.style_light_json));
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        };

        //Searching
        edtBuscarDireccion = findViewById(R.id.edtBuscarDireccion);
        mGeocoder = new Geocoder(getBaseContext());
        edtBuscarDireccion.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
                    direccionBuscar = edtBuscarDireccion.getText().toString();
                    if(!direccionBuscar.isEmpty())
                    {
                        try
                        {
                            List<Address> direcciones = mGeocoder.getFromLocationName(direccionBuscar, 2,
                                    lowerLeftLatitude, lowerLeftLongitude, upperRightLatitude, upperRigthLongitude);
                            if (direcciones!= null && !direcciones.isEmpty())
                            {
                                Address direccionEncontrada = direcciones.get(0);
                                LatLng posicion = new LatLng(direccionEncontrada.getLatitude(),direccionEncontrada.getLongitude());
                                if(mMap != null)
                                {
                                    MarkerOptions mMarcador = new MarkerOptions();
                                    mMarcador.position(posicion);
                                    mMarcador.title(direccionEncontrada.getAddressLine(0));
                                    mMarcador.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                                    mMap.addMarker(mMarcador);
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicion,14));
                                    double distanciaObjetivo = distance(latitud, longitud, posicion.latitude, posicion.longitude);
                                    Toast.makeText(Mapa.this, "Distancia al nuevo punto: " + distanciaObjetivo + " Km", Toast.LENGTH_LONG).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(getBaseContext(),"La direccion no fue encontrada", Toast.LENGTH_SHORT).show();
                            }
                        }catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
                return false;
            }
        });

    }

    //Lifecycle
    @Override
    protected void onResume()
    {
        super.onResume();
        sensorManager.registerListener(lightSensorListener, lightSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        sensorManager.unregisterListener(lightSensorListener);
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
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this ,
                new OnSuccessListener<Location>()
                {
                    @Override
                    public void onSuccess(Location location)
                    {
                        Log. i ("LOCATION","onSuccess location");
                        if (location != null )
                        {
                            latitud = location.getLatitude();
                            longitud = location.getLongitude();
                            actualizarMapa();
                            //Toast.makeText(getBaseContext(),"We got it!: " + latitud + " , " + longitud,Toast. LENGTH_LONG ).show();
                        }
                        else
                        {
                            Toast.makeText(getBaseContext(),"Something goes wrong!",Toast. LENGTH_LONG ).show();
                        }
                    }
                });
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

        // Add a marker in Bogota and move the camera
        LatLng bogota= new LatLng(latitud, longitud);

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.addMarker(new MarkerOptions().position(bogota).title("Marker in Bogota"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bogota));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title(geoCoderSearchLatLang(latLng)));
                double distanciaObjetivo = distance(latitud, longitud, latLng.latitude, latLng.longitude);
                Toast.makeText(Mapa.this, "Distancia al nuevo punto: " + distanciaObjetivo + " Km", Toast.LENGTH_LONG).show();
            }
        });
    }

    public double distance(double lat1, double long1, double lat2, double long2)
    {
        double latDistance = Math. toRadians (lat1-lat2);
        double lngDistance = Math. toRadians (long1-long2);
        double a = Math. sin(latDistance / 2 ) * Math. sin(latDistance / 2)
                + Math.cos(Math. toRadians (lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lngDistance / 2)*Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double result = RADIUS_OF_EARTH_KM * c;
        return Math.round(result*10000000.0)/10000000.0;
    }

    public String geoCoderSearchLatLang(LatLng direccion)
    {
        try
        {
            List<Address> direcciones = mGeocoder.getFromLocation(direccion.latitude, direccion.longitude, 2);
            if (direcciones!= null && !direcciones.isEmpty())
            {
                Address direccionEncontrada = direcciones.get(0);
                return direccionEncontrada.getAddressLine(0);
            }
            else
            {
                Toast.makeText(getBaseContext(),"La direccion no fue encontrada", Toast.LENGTH_SHORT).show();
            }
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return "";
    }

    public void actualizarMapa()
    {
        if(mMap != null)
        {
            LatLng bogota= new LatLng(latitud, longitud);

            mMap.addMarker(new MarkerOptions().position(bogota).title("localizacion actual"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(bogota));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        }
    }
}


