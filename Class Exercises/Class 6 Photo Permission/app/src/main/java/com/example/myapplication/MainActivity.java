package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void activarContactos(View v)
    {
        Intent intent = new Intent(MainActivity.this, Contacto.class);
        startActivity(intent);
    }
    public void activarCamara(View v)
    {
        Intent intent = new Intent(MainActivity.this, Camara.class);
        startActivity(intent);
    }
}
