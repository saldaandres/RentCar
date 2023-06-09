package com.example.rentcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class UserLogin extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText editEmail;
    EditText editPassword;
    Button btnRegister;
    Button btnLogIn;
    Button btnForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        getSupportActionBar().hide();

        editEmail = findViewById(R.id.editTextEmail);
        editPassword = findViewById(R.id.editTextPassword);
        btnRegister = findViewById(R.id.buttonAdd);
        btnLogIn = findViewById(R.id.buttonLogIn);
        btnForgotPassword = findViewById(R.id.buttonForgotPassword);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), userRegister.class));
            }
        });

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), OlvidoContrasena.class));
            }
        });

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty())  {
                    Toast.makeText(UserLogin.this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                db.collection("users")
                        .whereEqualTo("email", email)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().isEmpty()) {
                                        Toast.makeText(UserLogin.this, "Este usuario no existe", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        User usuarioEncontrado = document.toObject(User.class);
                                        String passwordUsuarioEncontrado = usuarioEncontrado.getPassword();
                                        if (!password.equals(passwordUsuarioEncontrado)){
                                            Toast.makeText(UserLogin.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        int role = usuarioEncontrado.getRole();
                                        if (role == 0) {
                                            Intent intentUser = new Intent(getApplicationContext(), RentCar.class);
                                            intentUser.putExtra("email", usuarioEncontrado.getEmail());
                                            startActivity(intentUser);
                                            return;
                                        }
                                        startActivity(new Intent(getApplicationContext(), CrudCar.class));
                                    }
                                }
                            }
                        });

            }
        });

    }
}