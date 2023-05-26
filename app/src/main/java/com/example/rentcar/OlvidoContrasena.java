package com.example.rentcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class OlvidoContrasena extends AppCompatActivity {
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    EditText editEmail;
    EditText editReserveWord;
    EditText editNewPassword;
    Button btnSaveChanges;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvido_contrasena);
        getSupportActionBar().hide();
        editEmail = findViewById(R.id.editTextEmail);
        editReserveWord = findViewById(R.id.editTextReserveWord);
        editNewPassword = findViewById(R.id.editTextNuevaContraseña);
        btnSaveChanges = findViewById(R.id.buttonSaveChanges);
        btnLogin = findViewById(R.id.buttonLogIn);

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                String reserveWord = editReserveWord.getText().toString();
                String newPassword = editNewPassword.getText().toString();

                if (email.isEmpty() || reserveWord.isEmpty() || newPassword.isEmpty()) {
                    Toast.makeText(OlvidoContrasena.this, "Tiene que llenar todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // buscamos si el usuario existe
                database.collection("users")
                        .whereEqualTo("email", email)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().isEmpty()) {
                                        Toast.makeText(OlvidoContrasena.this, "Usuario no existe. Debes registrarte", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    for (QueryDocumentSnapshot document: task.getResult()) {
                                        String idUsuarioEncontrado = document.getId();
                                        User usuarioEncontrado = document.toObject(User.class);
                                        if (!reserveWord.equals(usuarioEncontrado.getReserveword())){
                                            Toast.makeText(OlvidoContrasena.this, "Ingresaste la palabra incorrecta", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        // actualizamos la contraseña
                                        database.collection("users")
                                                .document(idUsuarioEncontrado)
                                                .update("password", newPassword)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(OlvidoContrasena.this, "Contraseña actualizada", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(OlvidoContrasena.this, "No pudimos actualizar tu contraseña", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }
                            }
                        });
            }
        });

    }
}