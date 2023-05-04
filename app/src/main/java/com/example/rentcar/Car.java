package com.example.rentcar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

public class Car extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText editPlaca;
    EditText editMarca;
    EditText editEstado;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        getSupportActionBar().hide();

        editPlaca = findViewById(R.id.editTextPlate);
        editMarca = findViewById(R.id.editTextBrand;
        editEstado = findViewById(R.id.editTextState);
        btnSave = findViewById(R.id.buttonSave);
    }
}