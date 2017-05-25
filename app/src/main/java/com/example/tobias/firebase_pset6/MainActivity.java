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

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    String TAG = "this is a tag";
    String email;
    String password;

    boolean logged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = "new@app.com";
        password = "wachtwoord";

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
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

    public void createUser(View view){
        EditText emailET = (EditText) findViewById(R.id.etname);
        EditText passET = (EditText) findViewById(R.id.etpass);

        email = emailET.getText().toString();
        password = passET.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Authentication failed",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Authentication successful",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    public void fastLogIn(View view){
        EditText emailET = (EditText) findViewById(R.id.etname);
        EditText passET = (EditText) findViewById(R.id.etpass);

        emailET.setText("a@b.com");
        passET.setText("1234567890");

        logIn(view);

    }

    public void logIn(View view){
        EditText emailET = (EditText) findViewById(R.id.etname);
        EditText passET = (EditText) findViewById(R.id.etpass);

        email = emailET.getText().toString();
        password = passET.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Logged in with " + email,
                                    Toast.LENGTH_SHORT).show();
                            logged = true;
                        }
                    }
                });
        if(logged){
//            Intent intent = new Intent(this, LoadActivity.class);
            Intent intent = new Intent(this, SocialActivity.class);
            intent.putExtra("user", email);
            startActivity(intent);
        }
    }

    public void fastLogIn2(View view){
        EditText emailET = (EditText) findViewById(R.id.etname);
        EditText passET = (EditText) findViewById(R.id.etpass);

        emailET.setText("piet@yahoo.nl");
        passET.setText("1234567890");

        logIn(view);
    }
    public void fastLogIn3(View view){
        EditText emailET = (EditText) findViewById(R.id.etname);
        EditText passET = (EditText) findViewById(R.id.etpass);

        emailET.setText("tob@tobi.tg");
        passET.setText("1234567890");

        logIn(view);
    }

}
