package com.example.tobias.firebase_pset6;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoadActivity extends AppCompatActivity {

    private FirebaseAuth authTest;
    private FirebaseAuth.AuthStateListener authStateListener;
    private static final String TAG = "Firebase_bored";
    private DatabaseReference mDatabase;
    String user;
    int incremental = 1;
    private static final String FIREBASE_BOARD = "message";
    private static final String FIREBASE_USERS = "users";

    String allBoredPeople;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        Bundle extras = getIntent().getExtras();
        user = extras.getString("user");

        authTest = FirebaseAuth.getInstance();
//        setListener();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void loadIntent(){

        Intent intent = new Intent(this, SocialActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("boredData", allBoredPeople);
        int test = 1;
        startActivity(intent);
    }

    public void testBored(View view){
        ValueEventListener postListener = new ValueEventListener() {

            String allBoredPeople;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allBoredPeople = dataSnapshot.child(FIREBASE_USERS).child("bored").getValue(String.class);

                Toast.makeText(LoadActivity.this, allBoredPeople, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.addValueEventListener(postListener);
        int test = 1;

//        loadIntent();
    }

}
