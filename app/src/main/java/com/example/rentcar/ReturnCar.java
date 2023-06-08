package com.example.rentcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ReturnCar extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    Spinner spinnerRetorno;
    TextView textViewPlaca;
    Button btnFechaRetorno;
    Button btnGuardar;
    ArrayList<String> rentasAbiertas = new ArrayList<>();
    ArrayAdapter<String> adapter;
    Long rentaSeleccionada;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_car);
        textViewPlaca = findViewById(R.id.textViewPlateNumber);
        btnFechaRetorno = findViewById(R.id.buttonFechaRetorno);
        btnGuardar = findViewById(R.id.buttonGuardar);
        spinnerRetorno = findViewById(R.id.spinnerRetorno);
        spinnerRetorno.setOnItemSelectedListener(this);
        email = getIntent().getStringExtra("email");

        database.collection("rents")
                .whereEqualTo("status", 0)
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                Toast.makeText(ReturnCar.this, "Este usuario no ha realizado prestamos", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            for (QueryDocumentSnapshot document: task.getResult()) {
                                Long rentNumber = document.getLong("rentNumber");
                                rentasAbiertas.add(String.valueOf(rentNumber));
                            }
                            adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, rentasAbiertas);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerRetorno.setAdapter(adapter);
                        }
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        rentaSeleccionada = (long) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}