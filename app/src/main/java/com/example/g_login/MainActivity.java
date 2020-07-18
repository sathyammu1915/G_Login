package com.example.g_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
SignInButton btn;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
String gmail,gname;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        btn=findViewById(R.id.sign_in_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
       // updateUI(currentUser);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Constant.gmail=account.getEmail();
                Constant.gname=account.getDisplayName();
               // Log.d(this, "firebaseAuthWithGoogle:" + account.getId());
               // Toast.makeText(this, "firebaseAuthWithGoogle"+account.getEmail(), Toast.LENGTH_LONG).show();
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
               // Log.w(TAG, "Google sign in failed", e);
                //Toast.makeText(this, "Google sign in failed"+e, Toast.LENGTH_SHORT).show();
                // [START_EXCLUDE]
              //  updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        // [START_EXCLUDE silent]
       // showProgressBar();
        // [END_EXCLUDE]
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            profile();
                           // Toast.makeText(MainActivity.this, "signInWithCredential:success", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                           // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                           // Log.w(TAG, "signInWithCredential:failure", task.getException());
                          //  Toast.makeText(MainActivity.this, "signInWithCredential:failure"+task.getException(), Toast.LENGTH_SHORT).show();
                            //Snackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                           // updateUI(null);
                        }

                        // [START_EXCLUDE]
                       // hideProgressBar();
                        // [END_EXCLUDE]
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void profile(){
        Intent intent=new Intent(this,Profile.class);
       // intent.putExtra("gmail",gmail);
        //intent.putExtra("gname",gname);
        startActivity(intent);
    }

}