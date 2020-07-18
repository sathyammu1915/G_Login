package com.example.g_login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Fb_login extends AppCompatActivity {
LoginButton fb_log;
CallbackManager mCallbackManager;
private FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_login);
        fb_log=findViewById(R.id.fb_login);
        mCallbackManager = CallbackManager.Factory.create();
mauth=FirebaseAuth.getInstance();
fb_log.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
    @Override
    public void onSuccess(LoginResult loginResult) {
        Toast.makeText(Fb_login.this, "user:"+loginResult.getAccessToken(), Toast.LENGTH_SHORT).show();
        handleFacebookAccessToken(loginResult.getAccessToken());
    }

    @Override
    public void onCancel() {
        Toast.makeText(Fb_login.this, "cancel", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(FacebookException error) {
        Toast.makeText(Fb_login.this, "Error", Toast.LENGTH_SHORT).show();
    }
});
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser cur_usr=mauth.getCurrentUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode,resultCode,data);

    }

    private void handleFacebookAccessToken(AccessToken token) {
        //Log.d(TAG, "handleFacebookAccessToken:" + token);
        Toast.makeText(this, "handleFacebookAccessToken"+token, Toast.LENGTH_SHORT).show();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                          //  Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(Fb_login.this, "signInWithCredential:success", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mauth.getCurrentUser();
                           // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                           // Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Fb_login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                           // updateUI(null);
                        }

                        // ...
                    }
                });
    }
    public void signOut() {
        mauth.signOut();
        LoginManager.getInstance().logOut();

       // updateUI(null);
    }
}