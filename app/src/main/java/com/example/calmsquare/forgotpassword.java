package com.example.calmsquare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotpassword extends AppCompatActivity {

    private EditText mforgotpassword;
    private Button mforgotpassBtn;
    private TextView mBack_login;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        getSupportActionBar().hide();

        mforgotpassword=findViewById(R.id.forgotpassword);
        mforgotpassBtn=findViewById(R.id.forgotpassBtn);
        mBack_login=findViewById(R.id.Back_login);
        firebaseAuth= FirebaseAuth.getInstance();


        //forgot password go back to login using different (Intent) method.

        mBack_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(forgotpassword.this,Login.class);
                startActivity(intent);
            }
        });

        mforgotpassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail=mforgotpassword.getText().toString().trim();
                if (mail.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter your email first.", Toast.LENGTH_SHORT).show();
                }
                else {
                    //send forgot password link
                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener< Void >() {
                        @Override
                        public void onComplete(@NonNull Task< Void > task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Mail Sent, you can recover password using email.", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(forgotpassword.this,MainActivity.class));
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Unable to fulfil request", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });

    }
}