package com.studio08.ronen.Zivug.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.studio08.ronen.Zivug.BuildConfig;
import com.studio08.ronen.Zivug.R;



public class OAuthActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 11212;
    private static final String TAG = "OAuthActivity";
//    private SignInButton mSignInButton;

    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;
    private GoogleSignInAccount acct;

    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_oauth);

        mAuth = FirebaseAuth.getInstance();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

//        mSignInButton = (SignInButton) findViewById(R.id.google_sign_in_button);
//        mSignInButton.setSize(SignInButton.SIZE_STANDARD);
//        mSignInButton.setScopes(gso.getScopeArray());
//        mSignInButton.setOnClickListener(this);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    updateUI(true);
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    startActivityForResult(
                            // Get an instance of AuthUI based on the default app

                            // https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setProviders(
                                            AuthUI.GOOGLE_PROVIDER)
                                    .setTosUrl("https://superapp.example.com/terms-of-service.html")
                                    .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                                    .build(),
                            RC_SIGN_IN);

                    // TODO: Implement sign-out https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md
                }
            }
        };

//        OptionalPendingResult<GoogleSignInResult> pendingResult =
//                Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
//        if (pendingResult.isDone()) {
//            // There's immediate result available.
//            updateButtonsAndStatusFromSignInResult(pendingResult.get());
//        } else {
//            // There's no immediate result ready, displays some progress indicator and waits for the
//            // async callback.
//            showProgressIndicator();
//            pendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                @Override
//                public void onResult(@NonNull GoogleSignInResult result) {
//                    updateButtonsAndStatusFromSignInResult(result);
//                    hideProgressIndicator();
//                }
//
//            });
//        }


    }

    private void updateButtonsAndStatusFromSignInResult(GoogleSignInResult googleSignInResult) {
        acct = googleSignInResult.getSignInAccount();
        Toast.makeText(this, "Welcome back " + acct.getGivenName(), Toast.LENGTH_SHORT).show();
        signIn();
    }

    private void hideProgressIndicator() {

    }

    private void showProgressIndicator() {
        Toast.makeText(this, "Progress indicator...", Toast.LENGTH_SHORT).show();
    }

    private void signIn() { // 1
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { // 2
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) { // 3
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            acct = result.getSignInAccount();
            // Google Sign In was successful, authenticate with Firebase
            firebaseAuthWithGoogle(acct);
//            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) { // 4
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        updateUI(true);


                        // If sign in fails, display a message to the user.
                        // If sign in succeeds the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(OAuthActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI(false);
                        }
                        // ...
                    }
                });
    }

    private void updateUI(boolean authenticated) {
        if (authenticated) {
            Toast.makeText(this, "Welcome " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();
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

}
