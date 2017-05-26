package com.example.tobias.firebase_pset6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/*
 * In this activity the user can log in or go to the sign up activity to create an account.
 * When the user is logged in the app will send you to the most important activity.
 * This app has for now two simple login buttons for usability during testing.
 */

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    String userEmail;
    String password;

    boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("signed in", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("not signed in", "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

//    When sign up is pressed, go to sign up page
    public void createUser(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

//    Log in by filling in email and password (both must be filled in)
//    Afterwards go to the main page or social page of the app
    public void logIn(View view){
        EditText emailET = (EditText) findViewById(R.id.etname);
        EditText passET = (EditText) findViewById(R.id.etpass);

        userEmail = emailET.getText().toString();
        password = passET.getText().toString();
        if(userEmail.length() > 0 && password.length() > 0) {

            mAuth.signInWithEmailAndPassword(userEmail, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("signed in", "signInWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Log.w("not signed in", "signInWithEmail:failed", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Logged in with " + userEmail,
                                        Toast.LENGTH_SHORT).show();
                                loggedIn = true;
                            }
                        }
                    });
            if (loggedIn) {
                Intent toSocialIntent = new Intent(this, SocialActivity.class);
                toSocialIntent.putExtra("email", userEmail);
                startActivity(toSocialIntent);
                finish();
            }
        }
    }

//    Easy log in button for testing. This is temporary and only for the demo.
    public void fastLogIn2(View view){
        EditText emailET = (EditText) findViewById(R.id.etname);
        EditText passET = (EditText) findViewById(R.id.etpass);
        emailET.setText("piet@yahoo.nl");
        passET.setText("1234567890");

        logIn(view);
    }
//    Easy log in button for testing. This is temporary and only for the demo.
    public void fastLogIn3(View view){
        EditText emailET = (EditText) findViewById(R.id.etname);
        EditText passET = (EditText) findViewById(R.id.etpass);
        emailET.setText("tob@tobi.tg");
        passET.setText("1234567890");

        logIn(view);
    }

}
