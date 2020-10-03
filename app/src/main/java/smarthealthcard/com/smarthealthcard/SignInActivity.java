package smarthealthcard.com.smarthealthcard;

import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.List;

import smarthealthcard.com.smarthealthcard.Loader.ShowLoader;
import smarthealthcard.com.smarthealthcard.LocalStorage.SaveLocalData;
import smarthealthcard.com.smarthealthcard.Model.UserModel;

public class SignInActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    GoogleSignInClient mGoogleSignInClient;
    ShowLoader showLoader;
    SaveLocalData saveLocalData;
    FirebaseUser user;

    ViewFlipper v_flipper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        int images[] = {R.drawable.login,R.drawable.recordkeep,R.drawable.pic3};

        v_flipper = findViewById(R.id.v_flipper);

        for (int image : images){
            flipperImages(image);
        }
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        showLoader = new ShowLoader(SignInActivity.this);
        saveLocalData = new SaveLocalData(SignInActivity.this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_client_id))
                .requestEmail()
                .build();

        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        //Now we will attach a click listener to the sign_in_button
        //and inside onClick() method we are calling the signIn() method that will open
        //google sign in intent
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getting the google signin intent
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                //starting the activity for result
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    public void flipperImages(int image){
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        v_flipper.addView(imageView);
        v_flipper.setFlipInterval(4000);
        v_flipper.setAutoStart(true);

        //animation
        v_flipper.setInAnimation(this, android.R.anim.slide_in_left);
        v_flipper.setOutAnimation(this, android.R.anim.slide_out_right);

    }

    @Override
    protected void onStart() {
        super.onStart();

        //if the user is already signed in
        //we will close this activity
        //and take the user to profile activity
        if (firebaseAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, HomeActivity.class));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN) {
            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //authenticating with firebase
                fireBaseAuthWithGoogle(account);
            } catch (ApiException e) {
                showLoader.PresentToast(e.getMessage());
            }
        }
    }

    private void fireBaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("", "firebaseAuthWithGoogle:" + acct.getId());
        showLoader.showProgressDialog("Please wait...");
        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        //Now using firebase we are signing in the user here
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("", "signInWithCredential:success");
                            user = firebaseAuth.getCurrentUser();
                            DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
                            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        showLoader.dismissDialog();
                                        startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                                    } else {
                                        UserModel userModel = new UserModel(user.getDisplayName(), user.getPhoneNumber(), user.getEmail(), "" + user.getPhotoUrl());
                                        firebaseFirestore.collection("Users").document(user.getUid()).set(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                showLoader.dismissDialog();
                                                startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                showLoader.dismissDialog();
                                                showLoader.PresentToast(e.getMessage());
                                            }
                                        });
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    showLoader.dismissDialog();
                                    showLoader.PresentToast(e.getMessage());
                                }
                            });
                        } else {
                            showLoader.PresentToast("Authentication failed.");
                        }
                    }
                });
    }
}
