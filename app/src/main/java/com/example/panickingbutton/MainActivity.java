package com.example.panickingbutton;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.panickingbutton.dynaview.AktifitasKriket;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private final int RESULT_PICK_CONTACT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_CONTACTS,
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                    },
                    1);
        }

        AppCompatButton panicButton = (AppCompatButton) findViewById(R.id.panic_button);
        if(new OperasiKontak(this).baca() == null || new OperasiKontak(this).baca().size() == 0){
            Toast.makeText(getBaseContext(), "At least one emergency contact must be added.",
                    Toast.LENGTH_LONG).show();
        }
        panicButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_items,menu);
        MenuItem item = menu.getItem(0);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
            aktifitasKriket();
                return true;
            }
        });
        return true;
    }

    private void aktifitasKriket(){
        Intent kriket = new Intent(this, AktifitasKriket.class);
        this.startActivity(kriket);
    }

    @Override
    public void onClick(View v) {
        if(new OperasiKontak(this).baca() == null || new OperasiKontak(this).baca().size() == 0){
            Toast.makeText(getBaseContext(), "At least one emergency contact must be added.",
                    Toast.LENGTH_LONG).show();
        }else{
            this.startActivity(new Intent(this,DirectionActivity.class));
        }
    }
}