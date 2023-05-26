package com.example.rentcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class userRegister extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText editEmail;
    EditText editName;
    EditText editPassword;
    EditText editReserveWord;
    RadioButton radioUser;
    RadioButton radioAdmin;
    Button btnAddUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        getSupportActionBar().hide();

        editEmail = findViewById(R.id.editTextEmail);
        editName = findViewById(R.id.editTextName);
        editPassword = findViewById(R.id.editTextPassword);
        editReserveWord = findViewById(R.id.editTextReserveWord);
        radioUser = findViewById(R.id.radioUser);
        radioAdmin = findViewById(R.id.radioAdmin);
        btnAddUser = findViewById(R.id.buttonAdd);

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                String name = editName.getText().toString();
                String password = editPassword.getText().toString();
                String reserveWord = editReserveWord.getText().toString();
                int role = (radioAdmin.isChecked() ? 1 : 0 );

                if (email.isEmpty() || name.isEmpty() || password.isEmpty() || reserveWord.isEmpty()) {
                    Toast.makeText(userRegister.this, "Debes rellenar todos los datos", Toast.LENGTH_SHORT).show();
                    return;
                }

                //verificar que el usuario no está en la base de datos
                db.collection("users")
                        .whereEqualTo("email", email)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (!task.getResult().isEmpty()){
                                        Toast.makeText(userRegister.this, "Este usuario ya existe", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    User nuevoUsuario = new User(name, email, password, role, reserveWord);
                                    // lo mandamos a la base de datos
                                    db.collection("users")
                                            .add(nuevoUsuario)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Toast.makeText(userRegister.this, "Usuario creado con el ID: " + documentReference.getId(), Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(userRegister.this, "No se pudo crear el usuario", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                        });
            }
        });

    }
}
