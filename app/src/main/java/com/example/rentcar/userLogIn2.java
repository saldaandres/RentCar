package com.example.rentcar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

public class userLogIn2 extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText editEmail;
    EditText editName;
    EditText editPassword;
    Button btnEdit, btnDelete, btnRent;

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
    }
}
