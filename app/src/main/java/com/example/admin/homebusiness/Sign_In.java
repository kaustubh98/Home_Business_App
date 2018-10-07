package com.example.admin.homebusiness;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Sign_In extends AppCompatActivity {

    private TextView t;
    private EditText email,pass;
    private Button signin;
    //firebase variables
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__in);

        //firebase Variables
        firebaseAuth = FirebaseAuth.getInstance();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sign in..");
        t = (TextView) findViewById(R.id.text_sign_up);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Sign_In.this,CreateAccount.class));
            }
        });

        email = findViewById(R.id.s_email);
        pass = findViewById(R.id.s_pass);
        signin = findViewById(R.id.btn_sign_in);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ValidEntry()){
                    progressDialog.show();
                    String e = email.getText().toString();
                    String p = pass.getText().toString();
                    firebaseAuth.signInWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Signed In",Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Sign_In.this,MyBusiness.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                            }else {
                                Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                }
            }
        });


    }
    private boolean ValidEntry() {
        String e = email.getText().toString();
        String p = pass.getText().toString();
        if(TextUtils.isEmpty(e) || !Patterns.EMAIL_ADDRESS.matcher(e).matches()){
            email.setError("Enter Valid Email");
            email.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(p) || p.length()<6){
            pass.setError("Enter Valid Password");
            pass.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Sign_In.this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}
