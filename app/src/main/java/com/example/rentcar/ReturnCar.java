package com.example.rentcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentResultListener;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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

public class ReturnCar extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    Spinner spinnerRetorno;
    TextView textViewPlaca;
    Button btnFechaRetorno;
    Button btnGuardar;
    ArrayList<Long> rentasAbiertas = new ArrayList<>();
    ArrayAdapter<Long> adapter;
    Long rentaSeleccionada;
    String email;
    LocalDate fechaInicial;
    LocalDate fechaRetorno;
    long returnnumber;
    Map<Long, Rent> rentas= new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_car);
        getSupportActionBar().hide();
        textViewPlaca = findViewById(R.id.textViewPlateNumber);
        btnFechaRetorno = findViewById(R.id.buttonFechaRetorno);
        btnGuardar = findViewById(R.id.buttonGuardar);
        spinnerRetorno = findViewById(R.id.spinnerRetorno);
        spinnerRetorno.setOnItemSelectedListener(this);
        email = getIntent().getStringExtra("email");

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.isEmpty()) {
                    Toast.makeText(ReturnCar.this, "No hay rentas para devolver", Toast.LENGTH_SHORT).show();
                    return;
                }

                String stringFechaRetorno = btnFechaRetorno.getText().toString();
                if (stringFechaRetorno.equals("Fecha Retorno")) {
                    Toast.makeText(ReturnCar.this, "Elige la fecha de retorno", Toast.LENGTH_SHORT).show();
                    return;
                }

                fechaRetorno = LocalDate.parse(btnFechaRetorno.getText().toString());
                if (fechaRetorno.isBefore(fechaInicial)) {
                    Toast.makeText(ReturnCar.this, "Fecha de retorno debe ser superior a la inicial", Toast.LENGTH_SHORT).show();
                    return;
                }

                // cargamos el return number
                database.collection("autoincrementar")
                        .document("numeros")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    returnnumber = documentSnapshot.getLong("returnnumber");

                                    // creamos el nuevo retorno
                                    Return nuevoRetorno = new Return(returnnumber, rentaSeleccionada, stringFechaRetorno);
                                    database.collection("returncars")
                                            .add(nuevoRetorno)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Toast.makeText(ReturnCar.this, "Se ha retornado el carro", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                    // incrementamos el numero de retorno
                                    database.collection("autoincrementar")
                                            .document("numeros")
                                            .update("returnnumber", FieldValue.increment(1))
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            returnnumber = returnnumber +1;
                                                        }
                                                    });
                                    // la sacamos de la lista actual
                                    adapter.remove(rentaSeleccionada);

                                    // la sacamos de la base de datos
                                    database.collection("rents")
                                            .whereEqualTo("rentNumber", rentaSeleccionada)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                            String idRenta = documentSnapshot.getId();
                                                            database.collection("rents")
                                                                    .document(idRenta)
                                                                    .update("status", FieldValue.increment(1));
                                                        }
                                                    }
                                                }
                                            });

                                    // actualizamos el carro
                                    database.collection("cars")
                                            .whereEqualTo("platenumber", rentas.get(rentaSeleccionada).getPlateNumber())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful())   {
                                                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                            String idCarro = documentSnapshot.getId();
                                                            database.collection("cars")
                                                                    .document(idCarro)
                                                                    .update("state", FieldValue.increment(-1));
                                                        }
                                                    }
                                                }
                                            });

                                }
                            }
                        });


            }
        });

        btnFechaRetorno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment picker = new DatePickerFragment();
                picker.show(getSupportFragmentManager(), "fechaRetorno");
            }
        });

        getSupportFragmentManager()
                .setFragmentResultListener("3", this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        String resultado = result.getString("retorno");
                        btnFechaRetorno.setText(resultado);
                    }
                });

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
                                rentasAbiertas.add(rentNumber);
                                Rent renta = document.toObject(Rent.class);
                                rentas.put(rentNumber, renta);
                            }
                            adapter = new ArrayAdapter<Long>(getApplicationContext(), android.R.layout.simple_spinner_item, rentasAbiertas);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerRetorno.setAdapter(adapter);
                        }
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        rentaSeleccionada =  (long) parent.getItemAtPosition(position);
        textViewPlaca.setText(rentas.get(rentaSeleccionada).getPlateNumber());
        fechaInicial = LocalDate.parse(rentas.get(rentaSeleccionada).getInitialDate());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}