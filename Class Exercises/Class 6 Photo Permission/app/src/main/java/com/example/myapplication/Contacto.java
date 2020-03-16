package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Contacto extends AppCompatActivity {
    static final int CONTACTS = 1;
    public TextView txtVCamara;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);
        txtVCamara = findViewById(R.id.txtVCamara);
        requestPermisision(this, Manifest.permission.WRITE_CONTACTS,
                "Lo necesito, deja de negarte y damelo >:v!!",CONTACTS);
    }
    private void requestPermisision(Activity contexto, String permiso, String justificacion, int codigo)
    {
        if(ContextCompat.checkSelfPermission(contexto, permiso)!= PackageManager.PERMISSION_GRANTED)
        {
            // Shouldweshow anexplanation?
            if(ActivityCompat.shouldShowRequestPermissionRationale(contexto,permiso))
            {
                Toast.makeText(getApplicationContext(),justificacion, Toast.LENGTH_LONG).show();
            }
            ActivityCompat.requestPermissions(contexto, new String[]{permiso},codigo);
        }
    }

    public void onRequestPermissionsResult(int reqCode, String permisos[], int[] grantResul)
    {
        switch(reqCode)
        {
            case CONTACTS:
            {
                if(grantResul.length > 0 && grantResul[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(getApplicationContext(),"Gracias joven :v", Toast.LENGTH_LONG).show();
                    txtVCamara.setText("PERMISO CONCEDIDO");
                    txtVCamara.setTextColor(ContextCompat.getColor(this, R.color.textColor2));
                }
                else
                {
                    txtVCamara.setText("LO DENEGASTE :,(");
                    txtVCamara.setTextColor(ContextCompat.getColor(this, R.color.textColor));
                }
            }
        }
    }

}
