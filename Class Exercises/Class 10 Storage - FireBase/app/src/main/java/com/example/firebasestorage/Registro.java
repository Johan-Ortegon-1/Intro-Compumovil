package com.example.firebasestorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.view.View;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Registro extends AppCompatActivity
{
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private final static String PATH_USER = "user/";

    private FirebaseAuth mAuth;
    private EditText edtTNameRegistro;
    private EditText edtTPasswordRegistro;
    private EditText edtTFavoriteAuthor;
    private Button btnRegistrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        database = FirebaseDatabase.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mAuth = FirebaseAuth.getInstance();
        /*Inflate*/
        edtTNameRegistro = findViewById(R.id.edtTNombreregistro);
        edtTPasswordRegistro = findViewById(R.id.edtTPasswordRegistro);
        edtTFavoriteAuthor = findViewById(R.id.edtTFavoriteAuthor);
        btnRegistrarse = findViewById(R.id.btnRegister);


        // Write a message to the database
        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("TAG", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
        */
    }


    public void registrarse(View view) {
        final String email = edtTNameRegistro.getText().toString();
        final String password = edtTPasswordRegistro.getText().toString();
        final String autorFavorito = edtTFavoriteAuthor.getText().toString();
        if(validacionDeCampos(email,password,autorFavorito))
        {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "createUserWithEmail:success");
                                Usuario nuevousuario = crearUsuarioPrueba(email,password,autorFavorito);
                                FirebaseUser user = mAuth.getCurrentUser();
                                nuevousuario.setuID(user.getUid());
                                ref =  database.getReference(PATH_USER + user.getUid());
                                ref.setValue(nuevousuario);
                                updateUI(user);
                                Toast.makeText(Registro.this, "Se ha almacenado un nuevo usuario.",Toast.LENGTH_SHORT).show();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(Registro.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(Registro.this, BienvenidaUsuario.class);
            intent.putExtra("usuarioActula", user);
            startActivity(intent);
        } else {
            edtTNameRegistro.setText("");
            edtTPasswordRegistro.setText("");
        }
    }

    public boolean validacionDeCampos(String email, String password, String autor) {
        boolean retorno = true;

        if (TextUtils.isEmpty(email)) {
            edtTNameRegistro.setError("Se requiere este campo.");
            retorno = false;
        } else {
            edtTNameRegistro.setError(null);
        }
        if (TextUtils.isEmpty(password)) {
            edtTPasswordRegistro.setError("Se requiere este campo.");
            retorno = false;
        } else {
            edtTPasswordRegistro.setError(null);
        }
        if (TextUtils.isEmpty(autor)) {
            edtTFavoriteAuthor.setError("Se requiere este campo.");
            retorno = false;
        } else {
            edtTFavoriteAuthor.setError(null);
        }
        return retorno;
    }

    public Usuario crearUsuarioPrueba(String email, String password, String autorFavorito)
    {
        ArrayList<Libro> libros = new ArrayList<>();
        Usuario nuevousuario = new Usuario("",email,password,autorFavorito, null, 0.0, 0.0);

        Libro l1 = new Libro("El resplando", "Stephen King");
        Libro l2 = new Libro("IT", "Stephen King");
        Libro l3 = new Libro("Cementerio de animales", "Stephen King");
        Libro l4 = new Libro("La torre oscura", "Stephen King");

        libros.add(l1);
        libros.add(l2);
        libros.add(l3);
        libros.add(l4);

        nuevousuario.setLibrosLeidos(libros);

        return nuevousuario;
    }
}
