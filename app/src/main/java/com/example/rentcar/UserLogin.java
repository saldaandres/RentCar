package com.example.rentcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    EditText editName;
    EditText editPassword;
    Button btnAdd;
    Button btnSearch;
    Button btnLogIn;
    Button btnList;
    String idUserFound;
    String passwordActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        getSupportActionBar().hide();

        editEmail = findViewById(R.id.editTextEmail);
        editName = findViewById(R.id.editTextName);
        editPassword = findViewById(R.id.editTextPassword);
        btnAdd = findViewById(R.id.buttonAdd);
        btnSearch = findViewById(R.id.buttonSearch);
        btnLogIn = findViewById(R.id.buttonLogIn);
        btnList = findViewById(R.id.buttonListar);

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ListUsers.class));
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                String name = editName.getText().toString();
                String password = editPassword.getText().toString();

                if (email.isEmpty() || name.isEmpty() || password.isEmpty()) {
                    Toast.makeText(UserLogin.this, "Debe rellenar todos los datos", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                // buscando al usuario
                db.collection("users")
                        .whereEqualTo("username", email)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().isEmpty()) {
                                        // crear el nuevo usuario
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("username", email);
                                        user.put("name", name);
                                        user.put("password", password);
                                        
                                        //lo mandamos a la base de datos
                                        db.collection("users")
                                                .add(user)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Toast.makeText(getApplicationContext(),"Usuario creado correctamente con id: "+documentReference.getId(),Toast.LENGTH_SHORT).show();
                                                        //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getApplicationContext(), "Error al crear el usuario: "+e, Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(UserLogin.this, "Usuario ya existe", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString();
                if (name.isEmpty()) {
                    Toast.makeText(UserLogin.this, "Debes escribir el nombre del usuario para buscarlos", Toast.LENGTH_SHORT).show();
                    return;
                }

                db.collection("users")
                        .whereEqualTo("name", name)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (!task.getResult().isEmpty()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            idUserFound = document.getId();
                                            editEmail.setText(document.getString("username"));
                                        }
                                    } else {
                                        Toast.makeText(UserLogin.this, "El usuario no existe", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                String name = editName.getText().toString();
                String password = editPassword.getText().toString();

                db.collection("users")
                        .whereEqualTo("name", name)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().isEmpty()) {
                                        Toast.makeText(UserLogin.this, "Usuario no existe", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        idUserFound = document.getId();
                                        passwordActual = document.getString("password");
                                    }

                                    if (!password.equals(passwordActual)) {
                                        Toast.makeText(UserLogin.this, "Contraseña invalida", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    Intent intent = new Intent(UserLogin.this, userLogIn2.class);
                                    intent.putExtra("username", email);
                                    intent.putExtra("name", name);
                                    intent.putExtra("password", password);
                                    intent.putExtra("idUser", idUserFound);
                                    Toast.makeText(UserLogin.this, "Redireccionando a tu Pagina Principal", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UserLogin.this, "Algo salió mal", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}