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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class CrudCar extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText editPlaca;
    EditText editMarca;
    EditText editEstado;
    EditText editValue;
    Button btnSave;
    Button btnDelete;
    Button btnSearch;
    Button btnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_car);
        getSupportActionBar().hide();

        editPlaca = findViewById(R.id.editTextPlate);
        editMarca = findViewById(R.id.editTextBrand);
        editEstado = findViewById(R.id.editTextState);
        editValue = findViewById(R.id.editTextDailyValue);
        btnSave = findViewById(R.id.buttonSave);
        btnSearch = findViewById(R.id.buttonBuscar);
        btnDelete = findViewById(R.id.buttonDelete);
        btnEdit = findViewById(R.id.buttonEditar);
        btnEdit.setEnabled(false);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placa = editPlaca.getText().toString();
                String marca = editMarca.getText().toString();
                String stringEstado = editEstado.getText().toString();
                String stringValue = editValue.getText().toString();

                if (placa.isEmpty() || marca.isEmpty() || stringEstado.isEmpty() || stringValue.isEmpty()) {
                    Toast.makeText(CrudCar.this, "Debes rellenar todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // revisamos que el carro no exista
                db.collection("cars")
                        .whereEqualTo("platenumber", placa)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (!task.getResult().isEmpty()) {
                                        Toast.makeText(CrudCar.this, "Este carro ya existe en la base de datos", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    int value = Integer.parseInt(stringValue);
                                    int state = Integer.parseInt(stringEstado);
                                    // creamos el nuevo carro
                                    Car nuevoCarro = new Car(placa, marca, state, value);
                                    db.collection("cars")
                                            .add(nuevoCarro)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Toast.makeText(CrudCar.this, "Carro creado con exito", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(CrudCar.this, "No se pudo crear el nuevo carro", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                        });
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placa = editPlaca.getText().toString();
                if (placa.isEmpty()) {
                    Toast.makeText(CrudCar.this, "Debes ingresar la placa del carro para borrarlo", Toast.LENGTH_SHORT).show();
                    return;
                }
                db.collection("cars")
                        .whereEqualTo("platenumber", placa)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().isEmpty()) {
                                        Toast.makeText(CrudCar.this, "Esa placa no existe", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String idCarro = document.getId();

                                        // borramos el carro
                                        db.collection("cars")
                                                .document(idCarro)
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(CrudCar.this, "El registro ha sido eliminado", Toast.LENGTH_SHORT).show();

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(CrudCar.this, "No se pudo borrar el carro", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }
                            }
                        });
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placa = editPlaca.getText().toString();
                if (placa.isEmpty()) {
                    Toast.makeText(CrudCar.this, "Debes escribir la placa que quieres buscar", Toast.LENGTH_SHORT).show();
                    return;
                }
                db.collection("cars")
                        .whereEqualTo("platenumber", placa)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().isEmpty()) {
                                        Toast.makeText(CrudCar.this, "Este carro no existe en la base de datos", Toast.LENGTH_SHORT).show();
                                        btnEdit.setEnabled(false);
                                        editValue.setText("");
                                        editEstado.setText("");
                                        editMarca.setText("");
                                        return;
                                    }
                                    for (QueryDocumentSnapshot document: task.getResult()) {
                                        Car carroEncontrado = document.toObject(Car.class);
                                        editMarca.setText(carroEncontrado.getBrand());
                                        editEstado.setText(String.valueOf(carroEncontrado.getState()));
                                        editValue.setText(String.valueOf(carroEncontrado.getDailyvalue()));
                                        btnEdit.setEnabled(true);
                                    }
                                }
                            }
                        });
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placa = editPlaca.getText().toString();
                String marca = editMarca.getText().toString();
                String stringEstado = editEstado.getText().toString();
                String stringValue = editValue.getText().toString();

                if (placa.isEmpty() || marca.isEmpty() || stringEstado.isEmpty() || stringValue.isEmpty()) {
                    Toast.makeText(CrudCar.this, "Debes ingresar todos los campos", Toast.LENGTH_SHORT).show();
                }

                db.collection("cars")
                        .whereEqualTo("platenumber", placa)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().isEmpty()) {
                                        Toast.makeText(CrudCar.this, "Esta placa no existe", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String idCarFound = document.getId();
                                        Map<String, Object> actualizaciones = new HashMap<>();
                                        actualizaciones.put("brand", marca);
                                        actualizaciones.put("state", Integer.parseInt(stringEstado));
                                        actualizaciones.put("dailyvalue", Integer.parseInt(stringValue));

                                        // guardamos la actualizacion
                                        db.collection("cars")
                                                .document(idCarFound)
                                                .update(actualizaciones)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(CrudCar.this, "Actualizamos los datos", Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(CrudCar.this, "No pudimos actualizar", Toast.LENGTH_SHORT).show();
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
