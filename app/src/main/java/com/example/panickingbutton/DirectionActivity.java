package com.example.panickingbutton;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class DirectionActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Polyline currentPolyline;
    private OperasiKontak operasiKontak = new OperasiKontak(this);

    private TextView tvStartAddress, tvEndAddress, tvDuration, tvDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);
        getSupportActionBar().hide();


        // Maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // Set Title bar
        getSupportActionBar().setTitle("Direction Maps API");
        // Inisialisasi Widget
        widgetInit();
        // jalankan method
        lokasiTerakhir();
    }

    private void widgetInit() {
        tvStartAddress = findViewById(R.id.tvStartAddress);
        tvEndAddress = findViewById(R.id.tvEndAddress);
        tvDuration = findViewById(R.id.tvDuration);
        tvDistance = findViewById(R.id.tvDistance);
    }

    private void lokasiTerakhir() {
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Task<Location> lastLocation = this.fusedLocationProviderClient.getLastLocation();
            lastLocation.addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {

                        Location currentLocation = task.getResult();
                        moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 15f);
                        tvStartAddress.setText("Latitude: "+currentLocation.getLatitude());
                        tvEndAddress.setText("Longitude: "+currentLocation.getLongitude());

                        List<Kontak> daftarKontak = operasiKontak.baca();
                        for(Kontak kontak:daftarKontak){
                            operasiKontak.hubungiKontak(kontak.getNomorKontak(),
                                    currentLocation.getLatitude(),currentLocation.getLongitude());
                        }

                        TextView tvDuration = (TextView) findViewById(R.id.tvDuration);
                        tvDuration.setText("Messages have been sent to all emergency contacts.");
                    } else {
                        Toast.makeText(getBaseContext(), "Failed to get your location.",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openWhatsappContact(String number) {
        Uri uri = Uri.parse("smsto:" + number);
        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
        i.putExtra(Intent.EXTRA_TEXT, "Pesan ini dihasilkan komputer.\nTidak perlu dibalas.");
        i.setPackage("com.whatsapp");
        startActivity(i);
    }

    private void moveCamera(LatLng latLng, float zoom) {
        this.mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        lokasiTerakhir();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        this.mMap.setMyLocationEnabled(true);
    }
}
