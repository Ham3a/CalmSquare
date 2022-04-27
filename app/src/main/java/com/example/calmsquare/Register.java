package com.example.calmsquare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText mFullName, mEmail,mPassword;
    Button mRegisterBtn, verifybtn;
    TextView mLoginBtn, verifymsg;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    FirebaseFirestore fStore;

    String UserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName =findViewById(R.id.edit_FullName);
        mEmail =findViewById(R.id.edit_email);
        mPassword =findViewById(R.id.edit_password);
        mRegisterBtn =findViewById(R.id.RegisterBtn);
        mLoginBtn =findViewById(R.id.linktext2);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        verifybtn = findViewById(R.id.verifybtn);
        verifymsg = findViewById(R.id.verifymsg);

        final FirebaseUser user = fAuth.getCurrentUser();



        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String fullname = mFullName.getText().toString();

                //Email needs to be entered
                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required.");
                    return;
                }
                //Password needs to be entered
                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Password is required.");
                    return;
                }

                //Password needs to be more than 6 characters long
                if (password.length() < 6 ){
                    mPassword.setError("Password must be 6 or more characters.");
                    return;
                }


                //Register user to firebase
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            //Email verification by sending link
                            FirebaseUser user = fAuth.getCurrentUser();
                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Register.this, "Verification Email has been sent.", Toast.LENGTH_SHORT).show();

                                }
                            }) . addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: Verification email not sent" + e.getMessage());
                                    
                                }
                            });

                            Toast.makeText(Register.this, "User Created.", Toast.LENGTH_SHORT).show();

                            //Document and collection for User data to be stored inside.
                            UserID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("Users").document(UserID);
                            Map<String,Object> User = new HashMap<>();
                            User.put("fName", fullname);
                            User.put("email", email);
                            documentReference.set(User).addOnSuccessListener((OnSuccessListener) (aVoid) -> {

                                Log.d(TAG, "onSuccess: User Profile is created for  " + UserID);
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure" + e.toString());
                                }
                            });

                            if (!user.isEmailVerified()){
                                verifymsg.setVisibility(View.VISIBLE);
                                verifybtn.setVisibility(View.VISIBLE);

                                verifybtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(view.getContext(), "Verification Email has been sent.", Toast.LENGTH_SHORT).show();

                                            }
                                        }) . addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d(TAG, "onFailure: Verification email not sent" + e.getMessage());

                                            }
                                        });
                                    }
                                });
                            }

                            if (user.isEmailVerified()){
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            }

                        }else {
                            Toast.makeText(Register.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();


                        }
                    }
                });

            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

    }
}