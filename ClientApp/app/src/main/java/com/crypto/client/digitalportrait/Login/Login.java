package com.crypto.client.digitalportrait.Login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.crypto.client.digitalportrait.DrawerMain;
import com.crypto.client.digitalportrait.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import static com.crypto.client.digitalportrait.Utilities.Reference.*;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Login";
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth firebaseAuth;

    private GoogleSignInClient googleSignInClient;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.btn_sign_in).setOnClickListener(this);
        findViewById(R.id.btn_sign_out).setOnClickListener(this);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        try {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                sendToAnActivity(user);
            }
            //Maybe update the GUI
            super.onStart();
        } catch (Exception e) {
            Log.e(TAG, "onStart: ", e);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        hide();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w(TAG, "onActivityResult: ", e);
                //Maybe update the GUI again
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle: " + account.getId());
        show();
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Log.i(TAG, "onComplete: " + user.getEmail());
                            sendToAnActivity(user);
                        } else {
                            Log.w(TAG, "signInCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Auth failed", Snackbar.LENGTH_LONG).show();
                            //Update GUI
                        }
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        firebaseAuth.signOut();
        googleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Update GUI
                        Toast.makeText(Login.this, "Sign out", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void sendToAnActivity(FirebaseUser user) {
        Log.d(TAG, "sendToAnActivity() called with: user = [" + user + "]");
        Intent intent = new Intent(Login.this, DrawerMain.class);
        intent.putExtra(USER_NAME, user.getDisplayName());
        intent.putExtra(EMAIL, user.getEmail());
        String photo;
        try {
            photo = user.getPhotoUrl().toString();
        } catch (NullPointerException e) {
            Log.e(TAG, "sendToAnActivity: ", e);
            photo = "empty";
        }
        intent.putExtra(IMG_PROFILE, photo);
        startActivity(intent);
    }

    public void show() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.setIndeterminate(true);
        }

        progressDialog.show();
    }

    public void hide() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_sign_in)
            signIn();
        else if (i == R.id.btn_sign_out)
            signOut();
    }
}
