package com.example.localizacioncontactosalmacenamiento;

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
import android.widget.Toast;

public class Contacto extends AppCompatActivity {
    static final int CONTACTS = 1;
    private ListView lVContactos;
    private ContactsCursor mContactsAdapter;
    private String[] mProjection;
    private Cursor mCursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);
        mContactsAdapter = new ContactsCursor(this, null, 0);
        lVContactos = findViewById(R.id.lVContactos);
        lVContactos.setAdapter(mContactsAdapter);
        requestPermisision(this, Manifest.permission.READ_CONTACTS,
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
                    llenarListView();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Necesitaba eso para funcionar :_v", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void llenarListView()
    {
        mProjection = new String[]{
                ContactsContract.Profile._ID, ContactsContract.Profile.DISPLAY_NAME_PRIMARY,
        };
        mCursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, mProjection,
                null, null, null);
        mContactsAdapter.changeCursor(mCursor);
    }
}
