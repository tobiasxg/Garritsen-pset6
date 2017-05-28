package com.example.tobias.firebase_pset6;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

/*
 * This is the most important part of the app.
 * When a user is bored he can press a button to let the world know that he is bored.
 * This will be put in Firebase so other users can see him standing in the "Bored" list.
 * From this activity you can choose a person who is bored and start a chat with him.
 * This is done by pressing the person in the list after which you will go to the profile activity.
 * Of course someone that is not bored anymore, can press a button to be removed from the list.
 * It is necessary to update the app manually, but this is yet to be fixed to work with real time.
 */

public class SocialActivity extends AppCompatActivity {

    private FirebaseAuth authTest;
    private DatabaseReference mDatabase;
    private static final String firebaseUsers = "users";
    private static final String firebaseAccounts = "message";

    String userEmail;
    String allBoredPeople = "loading$";

    ArrayAdapter adapter;

    ListView viewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);

        Bundle extras = getIntent().getExtras();
        userEmail = extras.getString("email");

        authTest = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        createListView();
        adapter.notifyDataSetChanged();
        if(allBoredPeople.contains("loading$")){
            allBoredPeople = allBoredPeople.replace("loading$","");
        }

        updateListView();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

//    Button for adding bored user to database
    public void iAmBoredButton(View view){
        boredButtons(true);
        updateListView();
    }

//    Button for removing bored user from database
    public void iAmNotBoredButton(View view){
        boredButtons(false);
        updateListView();
    }

//    Code for both removing and adding bored user from database
//    First get from the database the bored users as it is
//    Afterwards either add or remove user, depending on given boolean
    public void boredButtons(boolean boredOrNot){
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        getBoredPeople();
        if(allBoredPeople.contains(userEmail)){
            allBoredPeople = allBoredPeople.replace(userEmail+"$","");
        }

        DatabaseReference myRef = database.getReference(firebaseUsers);
        if(boredOrNot) {
            myRef.child("bored").setValue(userEmail+"$"+allBoredPeople);
            allBoredPeople = userEmail+"$"+allBoredPeople;
        } else {
            myRef.child("bored").setValue(allBoredPeople);
        }
    }

//    Load the database with bored people
//    Must be done to enable user to change database
    public void loadBoredPeopleButton(View view){
        updateListView();

        Button boredButton = (Button) findViewById(R.id.boredBut);
        Button notBoredButton = (Button) findViewById(R.id.notBoredBut);
        boredButton.setEnabled(true);
        notBoredButton.setEnabled(true);
    }

    // Get the variable allBoredPeople to be used everywhere in the class
    public void getBoredPeople(){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allBoredPeople = dataSnapshot.child(firebaseUsers).child("bored").getValue(String.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SocialActivity.this, "DATABASE ERROR", Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase.addValueEventListener(postListener);
    }

//    Create the ListView with all bored users
//    The variable with all bored users must be split to differentiate between users
    public void createListView(){
        String[] boredUsers = allBoredPeople.split("\\$");
        ArrayList<String> boredUsersForList = new ArrayList<>(Arrays.asList(boredUsers));
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, boredUsersForList);

        viewList = (ListView) findViewById(R.id.boredList);
        assert viewList != null;
        viewList.setAdapter(adapter);

        AdapterView.OnItemClickListener boredListener = new BoredListener();

        viewList.setOnItemClickListener(boredListener);
    }

//    Clicking on a bored user will lead to the profile of the user, for further actions
    private class BoredListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            TextView titleView = (TextView) view;
            String boredUser = titleView.getText().toString();
            Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
            profileIntent.putExtra("email", userEmail);
            profileIntent.putExtra("profile", boredUser);
            startActivity(profileIntent);
        }
    }

//    Update the ListView to the most recent found database
    public void updateListView(){
        ValueEventListener postListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allBoredPeople = dataSnapshot.child(firebaseUsers).child("bored").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SocialActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase.addValueEventListener(postListener);
        createListView();
        adapter.notifyDataSetChanged();
    }
}
