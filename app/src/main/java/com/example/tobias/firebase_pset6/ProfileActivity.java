package com.example.tobias.firebase_pset6;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
 * In this activity the user can send messages to another user.
 * The user is able to see what messages he send and what the other user send.
 * The order of the messages is chronological.
 * The messages are saved in two paths in Firebase, the path of person A to person B and vice versa.
 */

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth authTest;
    private DatabaseReference mDatabase;
    private static final String firebaseAccounts = "message";

    ListView viewList;

    ArrayAdapter adapter;

    String user;
    String profilePerson;
    String mail;
    String userMail;
    String messageDatabase = "loading";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Get data from previous intent and set profile name
        Bundle extras = getIntent().getExtras();
        user = extras.getString("email");
        profilePerson = extras.getString("profile");
//        messageDatabase = extras.getString("chatData");
        TextView boredField = (TextView) findViewById(R.id.friendEmail);
        boredField.setText(profilePerson);

        // Make emails readable for firebase paths
        mail = profilePerson;
        mail = mail.replace("@","");
        mail = mail.replace(".","");
        userMail = user;
        userMail = userMail.replace("@","");
        userMail = userMail.replace(".","");

        authTest = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        getMessageData();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

//     Send private message to person on whose profile you are
    public void sendMessage(View view){
        EditText messageField = (EditText) findViewById(R.id.etmessage);
        String message = messageField.getText().toString();
        getMessageData();
        if(message.length() > 0) {
            fireBaseSend(message);
        }
        messageField.setText("");
    }

//    Get and update database with send message
    public void fireBaseSend(String message){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        String messageToSend = message + "***" + user;
        String updatedFirebase = messageToSend+"$$$"+messageDatabase;
        DatabaseReference myRef = database.getReference(firebaseAccounts);
        myRef.child(userMail).child(mail).setValue(updatedFirebase);
        myRef.child(mail).child(userMail).setValue(updatedFirebase);
        messageDatabase = updatedFirebase;

        if(messageDatabase.length() > 0){
            createListView();
            adapter.notifyDataSetChanged();
        }
    }

//    Get the current database from firebase with all relevent messages
    public void getMessageData(){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messageDatabase = dataSnapshot.child(firebaseAccounts).child(mail).child(userMail).getValue(String.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "DATABASE ERROR", Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase.addValueEventListener(postListener);
        if (messageDatabase == null){
            messageDatabase = "";
        }
    }

//    Create ListView to show send and received messages
//    These messages should be split on all different messages
//    They should also be split on a different "split sign" as the second part indicates
//    who had send the message
    public void createListView(){
        String[] messages = messageDatabase.split("\\$\\$\\$");
        ArrayList<String> messagesList = new ArrayList<>(Arrays.asList(messages));
        adapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_list_item_1,
                        messagesList) {

                    // All items in list are given a color to indicate who had send the message
                    // by checking if the second substring links to the user or the other person
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        View view = super.getView(position, convertView, parent);
                        TextView text = (TextView) view.findViewById(android.R.id.text1);

                        String[] textWithId = text.getText().toString().split("\\*\\*\\*");
                        text.setText(textWithId[0]);
                        if (textWithId[1].equals(user)) {
                            text.setTextColor(getResources().getColor(R.color.black));
                            text.setBackgroundColor(getResources().getColor(R.color.red));
                        } else {
                            text.setTextColor(getResources().getColor(R.color.red));
                            text.setBackgroundColor(getResources().getColor(R.color.black));
                        }
                        text.setTextSize(30);

                        return view;
                    }
                };

        viewList = (ListView) findViewById(R.id.messageList);
        assert viewList != null;
        viewList.setAdapter(adapter);
    }

//    Load the database with messages
//    Must be done to enable user to send messages
    public void loadMessages(View view){
        getMessageData();
        if (messageDatabase.length() > 7) {
            createListView();
            adapter.notifyDataSetChanged();
        }
        Button sendButton = (Button) findViewById(R.id.button2);
        sendButton.setEnabled(true);
    }



}
