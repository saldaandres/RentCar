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

public class RentCar extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText editRenta;
    EditText editCorreo;
    EditText editPlaca;
    EditText editFecha;
    Button saveRent;
    Button retornar;
    String idCarFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_car);
        getSupportActionBar().hide();

        editRenta = findViewById(R.id.editTextRent);
        editCorreo = findViewById(R.id.editTextUserName);
        editPlaca = findViewById(R.id.editTextPlate);
        editFecha = findViewById(R.id.editTextRentDate);
        saveRent = findViewById(R.id.buttonSave);
        retornar = findViewById(R.id.buttonRetornar);
        editCorreo.setEnabled(false);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        editCorreo.setText(username);

        saveRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fechaRenta = editFecha.getText().toString();
                String placa = editPlaca.getText().toString();
                String stringNumeroRenta = editRenta.getText().toString();

                if (fechaRenta.isEmpty() || placa.isEmpty() || stringNumeroRenta.isEmpty()) {
                    Toast.makeText(RentCar.this, "Debe ingresar todos los datos", Toast.LENGTH_SHORT).show();
                    return;
                }

                int numeroRenta = Integer.parseInt(stringNumeroRenta);

                // buscamos que la placa existe
                db.collection("cars")
                        .whereEqualTo("platenumber", placa)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().isEmpty()) {
                                        // aqui el carro no existe
                                        Toast.makeText(RentCar.this, "Esa placa no existe", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        // aqui verificamos el estado
                                        if (document.getString("state").equalsIgnoreCase("disponible")) {
                                            idCarFound = document.getId();
                                            // buscamos el numero de renta
                                            db.collection("rents")
                                                    .whereEqualTo("rentnumber", numeroRenta)
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                
                                                                // verifica si el numero de renta ya existe
                                                                if (task.getResult().isEmpty()) {

                                                                    // creamos la nueva renta porque el numero no existía
                                                                    Map<String, Object> nuevaRenta = new HashMap<>();
                                                                    nuevaRenta.put("rentnumber", numeroRenta);
                                                                    nuevaRenta.put("username", username);
                                                                    nuevaRenta.put("platenumber", placa);
                                                                    nuevaRenta.put("rentdate", fechaRenta);

                                                                    // la mandamos a la base de datos
                                                                    db.collection("rents")
                                                                            .add(nuevaRenta)
                                                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                @Override
                                                                                public void onSuccess(DocumentReference documentReference) {
                                                                                    Toast.makeText(RentCar.this, "Renta creada con exito. ID : " + documentReference.getId() , Toast.LENGTH_SHORT).show();
                                                                                    db.collection("cars")
                                                                                            .document(idCarFound)
                                                                                            .update("state", "No disponible")
                                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void unused) {
                                                                                                    Toast.makeText(RentCar.this, "Carro actualizado con exito", Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                                                @Override
                                                                                                public void onFailure(@NonNull Exception e) {
                                                                                                    Toast.makeText(RentCar.this, "No pudimos actualizar los datos del carro", Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                            });
                                                                                }
                                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    Toast.makeText(RentCar.this, "No se pudo grabar la renta", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            });
                                                                } else {
                                                                    Toast.makeText(RentCar.this, "Ese numero de renta ya existe. Escoja otro.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        }
                                                    });
                                        } else {
                                            Toast.makeText(RentCar.this, "El carro no está disponible", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        });
            }
        });

        retornar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}