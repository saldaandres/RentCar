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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class CrudCar extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText editPlaca;
    EditText editMarca;
    EditText editEstado;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_car);
        getSupportActionBar().hide();

        editPlaca = findViewById(R.id.editTextPlate);
        editMarca = findViewById(R.id.editTextBrand);
        editEstado = findViewById(R.id.editTextState);
        btnSave = findViewById(R.id.buttonSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placa = editPlaca.getText().toString();
                String marca = editMarca.getText().toString();
                String estado = editEstado.getText().toString();

                // chequear que los datos no estén vacios
                if (placa.isEmpty() || marca.isEmpty() || estado.isEmpty()) {
                    Toast.makeText(CrudCar.this, "Debes rellenar todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // chequear que el dato ESTADO sea un valor aceptado
                if (!estado.equalsIgnoreCase("disponible") && !estado.equalsIgnoreCase("no disponible")) {
                    Toast.makeText(CrudCar.this, "El estado debe ser 'disponible' o 'no disponible'", Toast.LENGTH_SHORT).show();
                    return;
                }

                // buscar si el carro ya existe
                db.collection("cars")
                        .whereEqualTo("platenumber", placa)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().isEmpty()) {
                                        // aqui podemos añadir el carro
                                        Map<String, Object> nuevoCarro = new HashMap<>();
                                        nuevoCarro.put("brand", marca);
                                        nuevoCarro.put("platenumber", placa);
                                        nuevoCarro.put("state", estado);

                                        db.collection("cars")
                                                .add(nuevoCarro)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Toast.makeText(CrudCar.this, "Vehículo añadido", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(CrudCar.this, "No hemos podido añadir el vehículo", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(CrudCar.this, "Esta placa ya existe en la base de datos", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });


    }
}