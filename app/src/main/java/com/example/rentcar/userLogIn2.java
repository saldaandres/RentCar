package com.example.rentcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class userLogIn2 extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText editEmail;
    EditText editName;
    EditText editPassword;
    Button btnEdit;
    Button btnDelete;
    Button btnRent;
    ImageButton btnPencil;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_log_in2);
        getSupportActionBar().hide();

        editEmail = findViewById(R.id.editTextEmail);
        editName = findViewById(R.id.editTextName);
        editPassword = findViewById(R.id.editTextPassword);
        btnEdit = findViewById(R.id.buttonEdit);
        btnDelete = findViewById(R.id.buttonDelete);
        btnRent = findViewById(R.id.buttonRentCar);
        btnPencil = findViewById(R.id.imageButtonPencil);
        btnEdit.setEnabled(false);
        editEmail.setEnabled(false);
        editName.setEnabled(false);
        editPassword.setEnabled(false);

        // recuperamos los datos del intent que inicio la actividad
        intent = getIntent();
        String idUser = intent.getStringExtra("idUser");
        String usernameActual = intent.getStringExtra("username");
        String nameActual = intent.getStringExtra("name");
        String passwordActual = intent.getStringExtra("password");

        editEmail.setText(usernameActual);
        editName.setText(nameActual);
        editPassword.setText(passwordActual);

        btnPencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnEdit.setEnabled(true);
                editEmail.setEnabled(true);
                editName.setEnabled(true);
                editPassword.setEnabled(true);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editEmail.getText().toString();
                String name = editName.getText().toString();
                String password = editPassword.getText().toString();

                if (username.isEmpty() || name.isEmpty() || password.isEmpty()) {
                    Toast.makeText(userLogIn2.this, "No puedes editar tu usuario y dejar campos vacios", Toast.LENGTH_SHORT).show();
                    return;
                }

                // si no se cambió el nombre de usuario
                if (username.equals(usernameActual)) {
                    Map<String, Object> datosNuevos = new HashMap<>();
                    datosNuevos.put("name", name);
                    datosNuevos.put("password", password);

                   db.collection("users")
                           .document(idUser)
                           .update(datosNuevos)
                           .addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void unused) {
                                   Toast.makeText(userLogIn2.this, "Datos actualizados", Toast.LENGTH_SHORT).show();
                               }
                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   Toast.makeText(userLogIn2.this, "No pudimos cambiar los datos", Toast.LENGTH_SHORT).show();
                               }
                           });
                   return;
                } else {
                    db.collection("users")
                            .whereEqualTo("username", username)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (!task.getResult().isEmpty()) {
                                            Toast.makeText(userLogIn2.this, "Ese nombre de usuario ya fue tomado", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Map<String, Object> datosNuevos = new HashMap<>();
                                            datosNuevos.put("username", username);
                                            datosNuevos.put("name", name);
                                            datosNuevos.put("password", password);
                                            db.collection("users")
                                                    .document(idUser)
                                                    .update(datosNuevos)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(userLogIn2.this, "Datos actualizados", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(userLogIn2.this, "Error al cambiar los datos", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    }
                                }
                            });
                }

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("users")
                        .document(idUser)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(userLogIn2.this, "Has eliminado tu usuario", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(userLogIn2.this, "No hemos podido borrarte", Toast.LENGTH_SHORT).show();
                            }
                        });
                finish();
            }
        });

        btnRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRentar = new Intent(getApplicationContext(), Rent.class);
                intentRentar.putExtra("username", usernameActual);
                startActivity(intentRentar);
            }
        });

    }
}
