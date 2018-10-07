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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class CreateAccount extends AppCompatActivity {

    private EditText name,email,pass,num,add;
    private Button create;
    //firebase variables
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering user and storing data..");

        //initialize firebase variables
        mAuth = FirebaseAuth.getInstance();
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("UserData");

        email = (EditText) findViewById(R.id.e_email);
        pass = (EditText) findViewById(R.id.e_pass);
        name = findViewById(R.id.e_name);
        num = findViewById(R.id.e_number);
        add = findViewById(R.id.e_address);
        create = (Button) findViewById(R.id.btn_create_account);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ValidEntry()){
                    progressDialog.show();
                    final String e = email.getText().toString();
                    String p = pass.getText().toString();
                    final String address = add.getText().toString();
                    final String number = num.getText().toString();
                    final String n = name.getText().toString();
                    mAuth.createUserWithEmailAndPassword(e,p)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if(task.isSuccessful()){
                                        storeData(n,e,number,address);

                                        Toast.makeText(getApplicationContext(),"User Registered",Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(CreateAccount.this,MyProfile.class);
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
    //store user data in firebase
    private void storeData(String n, String e, String number, String address) {
        String key = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("UserData/"+key);
        UserData userData = new UserData(n,e,number,address);
        databaseReference.push().setValue(userData);
    }

    private boolean ValidEntry() {
        String e = email.getText().toString();
        String p = pass.getText().toString();
        String address = add.getText().toString();
        String number = num.getText().toString();
        String n = name.getText().toString();
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
        if(TextUtils.isEmpty(address)){
            add.setError("Enter Valid Address");
            add.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(n)){
            name.setError("Enter Valid name");
            name.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(number) || num.length()!=10){
            num.setError("Enter Valid Contact number");
            num.requestFocus();
            return false;
        }
        return true;
    }
}
