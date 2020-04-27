package com.example.firebasestorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText edtTUserName;
    private EditText edtTPassword;
    private Button btnLoging;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        /*Inflate*/
        edtTUserName = findViewById(R.id.edtTUserName);
        edtTPassword = findViewById(R.id.edtTPassword);
        btnLoging = findViewById(R.id.btnLoging);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void conectarse(View v) {
        String email, password;
        email = edtTUserName.getText().toString();
        password = edtTPassword.getText().toString();
        if (validacionDeCampos(email, password))
        {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });

        }
    }

    public void registrarse(View v)
    {
        Intent intent = new Intent(MainActivity.this, Registro.class);
        startActivity(intent);
    }

    public boolean validacionDeCampos(String email, String password) {
        boolean retorno = true;

        if (TextUtils.isEmpty(email)) {
            edtTUserName.setError("Se requiere este campo.");
            retorno = false;
        } else {
            edtTUserName.setError(null);
        }
        if (TextUtils.isEmpty(password)) {
            edtTPassword.setError("Se requiere este campo.");
            retorno = false;
        } else {
            edtTPassword.setError(null);
        }
        return retorno;
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(MainActivity.this, BienvenidaUsuario.class);
            intent.putExtra("usuarioActula", user);
            startActivity(intent);
        } else {
            edtTUserName.setText("");
            edtTPassword.setText("");
        }
    }
}
