package com.example.rentcar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

public class Rent extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText editRenta;
    EditText editCorreo;
    EditText editPlaca;
    Button btnFecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);
        getSupportActionBar().hide();

        editRenta = findViewById(R.id.editTextRent);
        editCorreo = findViewById(R.id.editTextUserName);
        editPlaca = findViewById(R.id.editTextPlate);
        btnFecha = findViewById(R.id.buttonSave);
    }
}