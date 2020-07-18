package com.example.g_login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Profile<signInResult1> extends AppCompatActivity {
Button signout;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
Intent i=getIntent();
String gmail,gname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]
//gmail=i.getStringExtra("gmail");
//gname=i.getStringExtra("gname");
        Toast.makeText(this, "name"+Constant.gmail+Constant.gname, Toast.LENGTH_LONG).show();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        signout=findViewById(R.id.button);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
signOut();
            }
        });
      //  Toast.makeText(this, "email"+, Toast.LENGTH_SHORT).show();
    }



    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //updateUI(null);
                       gotoMainActivity();
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account=result.getSignInAccount();
           // userName.setText(account.getDisplayName());
           // userEmail.setText(account.getEmail());
           // userId.setText(account.getId());
            Toast.makeText(this, ""+account.getDisplayName()+account.getEmail(), Toast.LENGTH_LONG).show();
            try{
               // Glide.with(this).load(account.getPhotoUrl()).into(profileImage);
            }catch (NullPointerException e){
                //Toast.makeText(getApplicationContext(),"image not found",Toast.LENGTH_LONG).show();
            }

        }else{
            gotoMainActivity();
        }
    }

    private void gotoMainActivity() {
        Intent intent=new Intent(Profile.this,MainActivity.class);
        startActivity(intent);
    }

}