package com.example.tobias.firebase_pset6;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth authTest;
    //    private FirebaseAuth.AuthStateListener authStateListener;
    private static final String TAG = "Firebase_bored";
    private DatabaseReference mDatabase;
    private static final String FIREBASE_ACCOUNTS = "message";

    ListView viewList;

    ArrayAdapter adapter;

    String user;
    String profilePerson;
    String mail;
    String uMail;

    String messageDatabase = "loading";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle extras = getIntent().getExtras();
        user = extras.getString("email");
        profilePerson = extras.getString("profile");
        TextView boredET = (TextView) findViewById(R.id.friendEmail);
        boredET.setText(profilePerson);

        mail = profilePerson;
        mail = mail.replace("@","");
        mail = mail.replace(".","");
        uMail = user;
        uMail = uMail.replace("@","");
        uMail = uMail.replace(".","");

        authTest = FirebaseAuth.getInstance();
//        setListener();
        mDatabase = FirebaseDatabase.getInstance().getReference();
//        getBoredPeople();
        getMessageData();
        Toast.makeText(ProfileActivity.this, messageDatabase, Toast.LENGTH_SHORT).show();



    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = authTest.getCurrentUser();
    }

    public void sendMessage(View view){
        EditText messageET = (EditText) findViewById(R.id.etmessage);
        String message = messageET.getText().toString();
        fireBaseSend(message);
        Toast.makeText(ProfileActivity.this, messageDatabase, Toast.LENGTH_SHORT).show();

    }

    public void fireBaseSend(String message){
        getMessageData();
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        String messageToSend = message + "***" + user;
        DatabaseReference myRef = database.getReference(FIREBASE_ACCOUNTS);
        myRef.child(uMail).child(mail).setValue(messageToSend+"$$$"+messageDatabase);
        myRef.child(mail).child(uMail).setValue(messageToSend+"$$$"+messageDatabase);
        getMessageData();

        createListView();
        adapter.notifyDataSetChanged();

    }

    public void getMessageData(){

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messageDatabase = dataSnapshot.child(FIREBASE_ACCOUNTS).child(mail).child(uMail).getValue(String.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "DATABASE ERROR", Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase.addValueEventListener(postListener);
    }

    public void createListView(){
        String[] messages = messageDatabase.split("\\$\\$\\$");
        ArrayList<String> messagesList = new ArrayList<>(Arrays.asList(messages));
//        boredUsersForList.remove(0);
//        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, messagesList);
        adapter =
                new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_list_item_1,
                        messagesList) {

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        View view = super.getView(position, convertView, parent);
                        TextView text = (TextView) view.findViewById(android.R.id.text1);

                        String[] textWithId = text.getText().toString().split("\\*\\*\\*");
                        text.setText(textWithId[0]);
                        if (textWithId[1].equals(user)) {
                            text.setTextColor(Color.BLUE);
                        } else {
                            text.setTextColor(Color.BLACK);
                        }

                        return view;
                    }
                };

        viewList = (ListView) findViewById(R.id.messageList);
        assert viewList != null;
        viewList.setAdapter(adapter);

        AdapterView.OnItemClickListener boredListener = new MessageListener();

        viewList.setOnItemClickListener(boredListener);

    }

    private class MessageListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            TextView titleView = (TextView) view;
            String boredUser = titleView.getText().toString();
        }
    }

}
