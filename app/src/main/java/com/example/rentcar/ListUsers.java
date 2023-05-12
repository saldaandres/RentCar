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

public class ListUsers extends AppCompatActivity {
    RecyclerView userRecycler;
    ArrayList<User> users;
    FirebaseFirestore database = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);
        getSupportActionBar().hide();
        userRecycler = findViewById(R.id.recyclerViewUserList);
        users = new ArrayList<>();
        userRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        userRecycler.setHasFixedSize(true);

        // cargar los datos de firestore
        loadUsers();
    }

    private void loadUsers() {
        database.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User usuario = document.toObject(User.class);
                                users.add(usuario);
                            }
                            UserAdapter adaptador = new UserAdapter(users);
                            userRecycler.setAdapter(adaptador);
                            return;
                        }
                        Log.w("", "Error recuperando los documentos.", task.getException());
                    }
                });
    }

}