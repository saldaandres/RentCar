package com.example.rentcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentResultListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RentCar extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Spinner spinner;
    Button btnFechaInicial;
    Button btnFechaFinal;
    Button btnGuardar;
    Button btnReturnCar;
    //Button btnListCars;
    TextView tvRentNumber;
    String placaSeleccionada;
    ArrayList<String> placasDisponibles = new ArrayList<>();
    ArrayAdapter<String> adapter;
    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_car);
        getSupportActionBar().hide();

        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        btnFechaInicial = findViewById(R.id.buttonFechaInicial);
        btnGuardar = findViewById(R.id.buttonGuardar);
        btnFechaFinal = findViewById(R.id.buttonFechaFinal);
        tvRentNumber = findViewById(R.id.textViewRentNumber);
        //btnListCars = findViewById(R.id.buttonListar);
        final long[] rentNumber = new long[1];
        email = getIntent().getStringExtra("email");
        btnReturnCar = findViewById(R.id.buttonReturnCar);

        btnReturnCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReturnCar.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        // cargamos los datos del spinner
        db.collection("cars")
                .whereEqualTo("state", 0)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                Toast.makeText(RentCar.this, "No hay carros disponibles", Toast.LENGTH_SHORT).show();
                            }
                            for (QueryDocumentSnapshot document: task.getResult()) {
                                String placa = document.getString("platenumber");
                                placasDisponibles.add(placa);
                            }
                            adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, placasDisponibles);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(adapter);
                        }
                    }
                });



        // calendario fecha inicial
        btnFechaInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment picker = new DatePickerFragment();
                picker.show(getSupportFragmentManager(), "fechaInicial");
            }
        });
        getSupportFragmentManager()
                .setFragmentResultListener("1", this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        String resultado = result.getString("inicial");
                        btnFechaInicial.setText(resultado);
                    }
                });

        // calendario fecha final
        btnFechaFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment picker = new DatePickerFragment();
                picker.show(getSupportFragmentManager(), "fechaFinal");
            }
        });
        getSupportFragmentManager()
                .setFragmentResultListener("2", this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        String resultado = result.getString("final");
                        btnFechaFinal.setText(resultado);
                    }
                });

        // cargar el numero de rentnumber
        db.collection("autoincrementar")
                .document("numeros")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            rentNumber[0] = documentSnapshot.getLong("rentnumber");
                            rentNumber[0]++;
                            tvRentNumber.setText(String.valueOf(rentNumber[0]));
                        }
                    }
                });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (placaSeleccionada == null) {
                    Toast.makeText(RentCar.this, "Debes seleccionar una placa", Toast.LENGTH_SHORT).show();
                    return;
                }

                String fechaInicialString = btnFechaInicial.getText().toString();
                String fechaFinalString = btnFechaFinal.getText().toString();
                
                if (fechaInicialString.equals("Fecha Inicial") || fechaFinalString.equals("Fecha Final")) {
                    Toast.makeText(RentCar.this, "Tienes que escoger las fechas", Toast.LENGTH_SHORT).show();
                    return;
                }

                LocalDate fechaInicial = LocalDate.parse(fechaInicialString);
                LocalDate fechaFinal = LocalDate.parse(fechaFinalString);
                
                if (fechaInicial.isBefore(LocalDate.now())) {
                    Toast.makeText(RentCar.this, "La fecha inicial no puede ser anterior a hoy", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (fechaFinal.isBefore(fechaInicial)) {
                    Toast.makeText(RentCar.this, "La fecha final no puede ser antes de la inicial", Toast.LENGTH_SHORT).show();
                    return;
                }

                // crear la nueva renta
                Rent nuevaRenta = new Rent(rentNumber[0], email, placaSeleccionada, fechaInicialString, fechaFinalString, 0);
                db.collection("rents")
                        .add(nuevaRenta)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(RentCar.this, "Se ha creado la renta con el ID " + documentReference.getId(), Toast.LENGTH_SHORT).show();

                                // incrementamos el numero de renta
                                db.collection("autoincrementar")
                                        .document("numeros")
                                        .update("rentnumber", FieldValue.increment(1));

                                // quitamos la placa de la lista de disponibles
                                adapter.remove(placaSeleccionada);
                                if (adapter.isEmpty()) {
                                    btnGuardar.setEnabled(false);
                                }

                                // actualizamos en pantalla el numero de renta
                                db.collection("autoincrementar")
                                        .document("numeros")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot documentSnapshot = task.getResult();
                                                    rentNumber[0] = documentSnapshot.getLong("rentnumber");
                                                    rentNumber[0]++;
                                                    tvRentNumber.setText(String.valueOf(rentNumber[0]));
                                                }
                                            }
                                        });

                                // buscamos el carro para actualizarlo
                                db.collection("cars")
                                        .whereEqualTo("platenumber", placaSeleccionada)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document: task.getResult()) {
                                                        // actualizamos el carro a no disponible
                                                        String idCarro = document.getId();
                                                        db.collection("cars")
                                                                .document(idCarro)
                                                                .update("state", FieldValue.increment(1));
                                                    }
                                                }
                                            }
                                        });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RentCar.this, "No se pudo crear la renta", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        placaSeleccionada = parent.getItemAtPosition(position).toString();
        Toast.makeText(this, placaSeleccionada, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "No hay carros disponibles", Toast.LENGTH_SHORT).show();
    }
}