package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Contacto extends AppCompatActivity {
    static final int CONTACTS = 1;
    public ListView lVContactos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);
        requestPermisision(this, Manifest.permission.READ_CONTACTS,
                "Lo necesito, deja de negarte y damelo >:v!!",CONTACTS);
        lVContactos = findViewById(R.id.lVContactos);
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
        else
        {
            llenarListView();
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

                }
                else
                {
                    llenarListView();
                }
            }
        }
    }

    public void llenarListView()
    {
        String[] mProjection = new String[]{
                ContactsContract.Profile._ID, ContactsContract.Profile.DISPLAY_NAME_PRIMARY
        };
        Cursor mCursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, mProjection,
                null, null, null);
    }
}
