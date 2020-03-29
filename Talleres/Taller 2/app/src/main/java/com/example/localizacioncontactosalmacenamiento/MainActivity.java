package com.example.localizacioncontactosalmacenamiento;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void activarCamara(View v)
    {
        Intent intent = new Intent(MainActivity.this, Camara.class);
        startActivity(intent);
    }

    public void activarContacto(View v)
    {
        Intent intent = new Intent(MainActivity.this, Contacto.class);
        startActivity(intent);
    }

    public void activarMapa(View v)
    {
        Intent intent = new Intent(MainActivity.this, Mapa.class);
        startActivity(intent);
    }

}
