package com.example.admin.homebusiness;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Objects;

public class MyProfile extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 1;
    private EditText n,e,c;
    private Button done;
    private ImageView pro;
    private Uri uriProfileImage;
    //firebase variables
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private String name = "abdfg",email,contact;
    //to check for user modifications
    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            done.setEnabled(true);
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        //ask the user to login if not already
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            startActivity(new Intent(MyProfile.this, Sign_In.class));
            finishAffinity();
        }

        //firebase initialize
        databaseReference = FirebaseDatabase.getInstance().getReference("UserData");

        done = findViewById(R.id.btn_done);
        n = findViewById(R.id.profile_name);
        e = findViewById(R.id.profile_email);
        c = findViewById(R.id.profile_no);
        pro = findViewById(R.id.profile_pic);

        pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i,"Select profile pic"),CHOOSE_IMAGE);
            }
        });
        //to read user data from firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               UserData u = new UserData();
               u = dataSnapshot.child(user.getUid()).getValue(UserData.class);
               if(u!=null){
                   Toast.makeText(getApplicationContext(),"u is not null",Toast.LENGTH_SHORT).show();

               }
               name = (String) dataSnapshot.child("UserData").child(user.getUid()).child("name").getValue();
               Toast.makeText(getApplicationContext(),"Name: "+name,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        n.setText(name);
        e.setText(email);
        c.setText(contact);

        //setup the touch listener
        n.setOnTouchListener(touchListener);
        e.setOnTouchListener(touchListener);
        c.setOnTouchListener(touchListener);
        pro.setOnTouchListener(touchListener);


    }

    //for choosing profile pic

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null){
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uriProfileImage);
                pro.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
