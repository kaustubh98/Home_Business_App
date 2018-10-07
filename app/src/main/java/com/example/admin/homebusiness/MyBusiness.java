package com.example.admin.homebusiness;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyBusiness extends AppCompatActivity {

    //firebase variables
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    //activity variables
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_business);

        user = FirebaseAuth.getInstance().getCurrentUser();
        //ask the user to login if not already
        if(user == null)
            startActivity(new Intent(MyBusiness.this,Sign_In.class));

        fab = findViewById(R.id.add_business);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyBusiness.this,Add_Business.class);
                startActivity(i);
            }
        });

    }
}
