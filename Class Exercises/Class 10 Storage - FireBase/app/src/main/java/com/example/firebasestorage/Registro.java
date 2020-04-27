package com.example.firebasestorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class Registro extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText edtTNameRegistro;
    private EditText edtTPasswordRegistro;
    private Button btnRegistrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mAuth = FirebaseAuth.getInstance();
        /*Inflate*/
        edtTNameRegistro = findViewById(R.id.edtTNombreregistro);
        edtTPasswordRegistro = findViewById(R.id.edtTPasswordRegistro);
        btnRegistrarse = findViewById(R.id.btnRegister);
    }

    public void registrarse(View view) {
        String email = edtTNameRegistro.getText().toString();
        String password = edtTPasswordRegistro.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Registro.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
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

}
