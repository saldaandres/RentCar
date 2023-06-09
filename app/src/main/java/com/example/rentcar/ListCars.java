package com.example.rentcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListCars extends AppCompatActivity {
    RecyclerView carRecycler;
    ArrayList<Car> cars;
    FirebaseFirestore database = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cars);
        getSupportActionBar().hide();
        carRecycler = findViewById(R.id.recyclerViewUserList);
        cars = new ArrayList<>();
        carRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        carRecycler.setHasFixedSize(true);

        // cargar los datos de firestore
        loadCars();
    }

    private void loadCars() {
        database.collection("cars")
                .whereEqualTo("state", 0)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Car carro= document.toObject(Car.class);
                                cars.add(carro);
                            }
                            CardAdapter adaptador = new CardAdapter(cars);
                            carRecycler.setAdapter(adaptador);
                            return;
                        }
                        Log.w("", "Error recuperando los documentos.", task.getException());
                    }
                });
    }

}