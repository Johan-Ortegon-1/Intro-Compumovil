package com.example.firebasestorage;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BienvenidaUsuario extends AppCompatActivity {
    private FirebaseUser user;
    private TextView txtVIdUser;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String textTemp;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida_usuario);
        Intent intent = getIntent();
        user = (FirebaseUser) intent.getParcelableExtra("usuarioActula");
        txtVIdUser = findViewById(R.id.txtVIdUser);
        textTemp = "Id del usuario actual: " + user.getUid();
        txtVIdUser.setText(textTemp);
        mAuth = FirebaseAuth.getInstance();
    }

    public void goMapa(View v)
    {
        Intent intent = new Intent(BienvenidaUsuario.this, LocalizacionesSimultaneas.class);
        intent.putExtra("usuarioActual", user);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_personalizado, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int itemClicked = item.getItemId();
        if(itemClicked == R.id.menuLogout)
        {
            mAuth.signOut();
            Intent intent = new Intent(BienvenidaUsuario.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
