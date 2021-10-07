package mad.todo.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import mad.todo.HomeScreenActivity;
import mad.todo.R;


public class LoginActivity extends AppCompatActivity {
 private static final int RC_SIGN_IN = 100;
 private static final String TAG = "login";
 GoogleSignInOptions gso;
 public static GoogleSignInClient mGoogleSignInClient;
 SignInButton signInButton;
 public static FirebaseAuth mAuth;

 @Override
 protected void onCreate(Bundle savedInstanceState) {
  Log.d("TAG", "onCreate: Called");
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_login);

  signInButton = findViewById(R.id.sign_in_button);

  // Configure Google Sign In
  gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
    .requestIdToken(getString(R.string.web_client_id))
    .requestEmail()
    .build();
  mAuth = FirebaseAuth.getInstance();
  mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

  signInButton.setOnClickListener(v -> {
   Intent signInIntent = mGoogleSignInClient.getSignInIntent();
   startActivityForResult(signInIntent, RC_SIGN_IN);

  });
 }


 @Override
 public void onActivityResult(int requestCode, int resultCode, Intent data) {
  Log.d("TAG", "onActivityResult: Called");
  super.onActivityResult(requestCode, resultCode, data);

  // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
  if (requestCode == RC_SIGN_IN) {
   Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
   try {
    // Google Sign In was successful, authenticate with Firebase
    GoogleSignInAccount account = task.getResult(ApiException.class);
    Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
    firebaseAuthWithGoogle(account.getIdToken());
   } catch (ApiException e) {
    // Google Sign In failed, update UI appropriately
    Log.w(TAG, "Google sign in failed", e);
   }
  }
 }


 private void firebaseAuthWithGoogle(String idToken) {
  Log.d("TAG", "firebaseAuthWithGoogle: Called");
  AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
  mAuth.signInWithCredential(credential)
    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
     @Override
     public void onComplete(@NonNull Task<AuthResult> task) {
      if (task.isSuccessful()) {
       // Sign in success, update UI with the signed-in user's information
       Log.d(TAG, "signInWithCredential:success");
       FirebaseUser user = mAuth.getCurrentUser();
       Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
       startActivity(intent);
       finish();
      } else {
       // If sign in fails, display a message to the user.
       Log.w(TAG, "signInWithCredential:failure", task.getException());
       //updateUI(null);
      }
     }
    });
 }


 @Override
 public void onStart() {
  Log.d("TAG", "onStart: Called");
  super.onStart();
  // Check if user is signed in (non-null) and update UI accordingly.
  FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

  try {
   Log.d("TAG", "onStart: user = " + user.toString());
  } catch (Exception e) {
   Log.d("TAG", "onStart: user is null");
  }
  if (user != null) {
   Intent intent = new Intent(this, HomeScreenActivity.class);
   startActivity(intent);
   finish();
  }


 }

}