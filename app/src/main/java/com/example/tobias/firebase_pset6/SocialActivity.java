package com.example.tobias.firebase_pset6;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class SocialActivity extends AppCompatActivity {

    private FirebaseAuth authTest;
//    private FirebaseAuth.AuthStateListener authStateListener;
    private static final String TAG = "Firebase_bored";
    private DatabaseReference mDatabase;
    String user;
//    String email;
    int incremental = 1;
//    private static final String FIREBASE_BOARD = "message";
    private static final String FIREBASE_USERS = "users";

    String allBoredPeople = "loading$";

    ListView viewList;

    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);

        Bundle extras = getIntent().getExtras();
        user = extras.getString("email");
//        user = extras.getString("username");


        authTest = FirebaseAuth.getInstance();
//        setListener();
        mDatabase = FirebaseDatabase.getInstance().getReference();
//        getBoredPeople();

        createListView();
        adapter.notifyDataSetChanged();
        if(allBoredPeople.contains("loading$")){
            allBoredPeople = allBoredPeople.replace("loading$","");
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = authTest.getCurrentUser();
    }

    public void iAmBored(View view){
        boredButtons(true);
//        myRef2.child("bored").setValue(allBoredPeople+"$"+user);
        createListView();
//        Intent i = new Intent(getApplicationContext(), SocialActivity.class);
////            i.putExtra("title", title);
//        startActivity(i);
//        finish();
        adapter.notifyDataSetChanged();
    }

    public void iAmNotBored(View view){
        boredButtons(false);
//        myRef2.child("bored").setValue(allBoredPeople);
        createListView();
        adapter.notifyDataSetChanged();
    }

    public void boredButtons(boolean boredOrNot){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference(FIREBASE_BOARD);
//        myRef.child(Integer.toString(incremental)).child("status").setValue(user+" not_bored");

        getBoredPeople();
        if(allBoredPeople.contains(user)){
            allBoredPeople = allBoredPeople.replace(user+"$","");
        }

        DatabaseReference myRef2 = database.getReference(FIREBASE_USERS);
        if(boredOrNot) {
            myRef2.child("bored").setValue(user+"$"+allBoredPeople);//+"$"+user);
        } else {
            myRef2.child("bored").setValue(allBoredPeople);

        }

    }

    public void testBored(View view){
        ValueEventListener postListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allBoredPeople = dataSnapshot.child(FIREBASE_USERS).child("bored").getValue(String.class);

//                Toast.makeText(SocialActivity.this, allBoredPeople, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.addValueEventListener(postListener);

        createListView();
        adapter.notifyDataSetChanged();

        Button boredBut = (Button) findViewById(R.id.boredBut);
        Button notBoredBut = (Button) findViewById(R.id.notBoredBut);
        boredBut.setEnabled(true);
        notBoredBut.setEnabled(true);
    }

    public void getBoredPeople(){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allBoredPeople = dataSnapshot.child(FIREBASE_USERS).child("bored").getValue(String.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SocialActivity.this, "DATABASE ERROR", Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase.addValueEventListener(postListener);
    }

    public void createListView(){
        String[] boredUsers = allBoredPeople.split("\\$");
        ArrayList<String> boredUsersForList = new ArrayList<>(Arrays.asList(boredUsers));
//        boredUsersForList.remove(0);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, boredUsersForList);

        viewList = (ListView) findViewById(R.id.boredList);
        assert viewList != null;
        viewList.setAdapter(adapter);

        AdapterView.OnItemClickListener boredListener = new BoredListener();

        viewList.setOnItemClickListener(boredListener);

    }

    private class BoredListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            TextView titleView = (TextView) view;
            String boredUser = titleView.getText().toString();
            Toast.makeText(SocialActivity.this, boredUser, Toast.LENGTH_SHORT).show();
            Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
            profileIntent.putExtra("email", user);
            profileIntent.putExtra("profile", boredUser);
            startActivity(profileIntent);
        }
    }

//    private void setListener(){
//        authStateListener = (FirebaseAuth.AuthStateListener) (firebaseAuth)
//    }
}
