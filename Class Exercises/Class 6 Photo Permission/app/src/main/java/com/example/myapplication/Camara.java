package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Camara extends AppCompatActivity {
    static final int CAMERA = 2, ALAMACENAMIENTO_INTERNO = 3 , REQUEST_IMAGE_CAPTURE = 1, IMAGE_PICKER_REQUEST = 10;
    static boolean accesoCamara = false, accesoAlmacenamieto = false;
    public TextView txtVCamara;
    public ImageView imagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara);
        txtVCamara = findViewById(R.id.textVCamara);
        imagen = findViewById(R.id.imgVImagen);
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
        switch(reqCode)
        {
            case CAMERA:
            {
                if(grantResul.length > 0 && grantResul[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(getApplicationContext(),"Gracias joven :v", Toast.LENGTH_LONG).show();
                    txtVCamara.setText("PERMISO CONCEDIDO");
                    txtVCamara.setTextColor(ContextCompat.getColor(this, R.color.textColor2));
                    tomarFoto();
                }
                else
                {
                    txtVCamara.setText("LO DENEGASTE :,(");
                    txtVCamara.setTextColor(ContextCompat.getColor(this, R.color.textColor));
                }
                break;
            }
            case ALAMACENAMIENTO_INTERNO:
            {
                if(grantResul.length > 0 && grantResul[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(getApplicationContext(),"Gracias joven :v", Toast.LENGTH_LONG).show();
                    txtVCamara.setText("PERMISO CONCEDIDO");
                    txtVCamara.setTextColor(ContextCompat.getColor(this, R.color.textColor2));
                    buscarImagen();
                }
                else
                {
                    txtVCamara.setText("LO DENEGASTE :,(");
                    txtVCamara.setTextColor(ContextCompat.getColor(this, R.color.textColor));
                }
                break;
            }
        }
    }

    public void seleccionarImagen(View v)
    {
        accesoAlmacenamieto = requestPermisision(this, Manifest.permission.READ_EXTERNAL_STORAGE,
                "Requiero acceder al almecenamiento interno",ALAMACENAMIENTO_INTERNO);
        if(accesoAlmacenamieto)
            buscarImagen();
    }
    public void buscarImagen()
    {
        Intent pickImage = new Intent(Intent.ACTION_PICK);
        pickImage.setType("image/*");
        startActivityForResult(pickImage, IMAGE_PICKER_REQUEST);
    }
    public void desplegarCamara(View v)
    {
        accesoCamara = requestPermisision(this, Manifest.permission.CAMERA,
                "Quiero la camarita, deja de negarte y damela >:v!!",CAMERA);
        if(accesoCamara)
            tomarFoto();
    }
    public void tomarFoto()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager())!=null)
        {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode){
            case IMAGE_PICKER_REQUEST:
            {
                if(resultCode == RESULT_OK)
                {
                    try
                    {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selctedImage = BitmapFactory.decodeStream(imageStream);
                        imagen.setImageBitmap(selctedImage);
                    }
                    catch(FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case REQUEST_IMAGE_CAPTURE:
            {
                if(resultCode == RESULT_OK)
                {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    imagen.setImageBitmap(imageBitmap);
                }
                break;
            }
        }
//        if(requestCode == Request_Image_Capture && resultCode == RESULT_OK)
//        {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            imagen.setImageBitmap(imageBitmap);
//        }
    }
}
