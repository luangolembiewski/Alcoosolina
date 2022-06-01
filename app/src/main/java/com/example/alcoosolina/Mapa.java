package com.example.alcoosolina;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.alcoosolina.directions.Apinterface;
import com.example.alcoosolina.directions.Result;
import com.example.alcoosolina.directions.Route;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.ButtCap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Mapa extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, LocationListener {

    private static final String TAG = "Mapa";
    private GoogleMap mMap;
    private LocationManager locationManager;
    private Geocoder geocoder;
    private Apinterface apiInterface;
    private List<LatLng> polylinelist;
    private PolylineOptions polylineOptions;
    private LatLng origion,dest;
    private boolean navegando;


    EditText textDestino;
    ImageButton btnNavegar;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_mapa);
        control();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        geocoder = new Geocoder(this);

        Retrofit retrofit= new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://maps.googleapis.com/")
                .build();
        apiInterface = retrofit.create(Apinterface.class);
    }

    public void control() {
        textDestino = (EditText) findViewById(R.id.textDestino);
        btnNavegar = (ImageButton) findViewById(R.id.btnNavegar);
    }

    @Override
    public void onResume() {
        super.onResume();

        //Solicita Permissões
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Mapa.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(Mapa.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            ActivityCompat.requestPermissions(Mapa.this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1);
            return;
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50000, 10, this);

    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //escolher melhor provedor
            /*
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            Toast.makeText(getApplicationContext(), "Provider: "+ provider, Toast.LENGTH_LONG).show();
             */
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        //mMap.setTrafficEnabled(true);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    //Clicks
    public void navegar(View v){
        navegando = true;
        origion = new LatLng(mMap.getMyLocation().getLatitude(),mMap.getMyLocation().getLongitude());
        getDirection(origion.latitude+","+origion.longitude,dest.latitude+","+dest.longitude);
    }
    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        if(navegando!=true) {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marcador"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            try {
                List<Address> addresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                String address = addresses.get(0).getAddressLine(0).replace(addresses.get(0).getPostalCode()+", ","");
                textDestino.setText(address);
            }catch (Exception e){
                textDestino.setText("Erro!");
            }
            dest = latLng;
            btnNavegar.setVisibility(View.VISIBLE);
        }

    }

    private void getDirection(String origin, String destination){
        apiInterface.getDirection("driving","less_driving",origin,destination,
                getString(R.string.api_key)
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Result result) {
                        polylinelist = new ArrayList<>();
                        List<Route> routeList = result.getRoutes();
                        for(Route route : routeList){
                            try {
                                String polyline = route.getOverviewPolyline().getPoints();
                                polylinelist.addAll(decodePoly(polyline));
                            }catch (Exception e){
                                Log.i(TAG,"aqui= Rota:"+ route.getOverviewPolyline()+" "+e);
                            }

                        }
                        polylineOptions = new PolylineOptions();
                        polylineOptions.color(ContextCompat.getColor(getApplicationContext(),R.color.purple_500));
                        polylineOptions.width(8);
                        polylineOptions.startCap(new ButtCap());
                        polylineOptions.jointType(JointType.ROUND);
                        polylineOptions.addAll(polylinelist);
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(dest).title("Destino"));
                        mMap.addPolyline(polylineOptions);

                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        builder.include(origion);
                        builder.include(dest);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),100));
                    }

                    @Override
                    public void onError(Throwable ex) {
                        Log.i(TAG, "aqui= " + ex);
                    }
                });
    }





    //comportamentos do mapa
    @Override
    public void onLocationChanged(@NonNull Location location) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
                location.getLongitude()),15.0f));
        if(navegando==true){
            origion = new LatLng(location.getLatitude(),location.getLongitude());
            getDirection(origion.latitude+","+origion.longitude,dest.latitude+","+dest.longitude);
        }
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
        Toast.makeText(getApplicationContext(), "Posição Alterada", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
        Toast.makeText(getApplicationContext(), "O Status do Provider foi alterado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
        Toast.makeText(getApplicationContext(), "GPS Habilitado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
        createNoGpsDialog();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    //Metodo decode string para LatLng
    private List<LatLng> decodePoly(String encoded){
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while(index<len){
            int b, shift=0, result=0;
            do{
                b=encoded.charAt(index++) - 63;
                result |= (b & 0x1f)<<shift;
                shift +=5;
            } while(b>=0x20);
            int dlat = ((result&1)!= 0 ?~(result >>1) : (result >>1));
            lat+=dlat;

            shift = 0;
            result = 0;

            do{
                b = encoded.charAt(index++)-63;
                result |= (b&0x1f) << shift;
                shift +=5;
            } while(b>=0x20);
            int dlng = ((result & 1) != 0 ? ~(result >>1) : (result >> 1));
            lng +=dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

    private void createNoGpsDialog(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Intent callGPSSettingIntent = new Intent(
                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(callGPSSettingIntent);
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Por favor ative seu GPS para usar esse aplicativo.")
                .setPositiveButton("Ativar", dialogClickListener)
                .create();
        builder.show();

    }


}