package com.example.serasel.eagle.Activities;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.serasel.eagle.Classes.Student;
import com.example.serasel.eagle.Utilities.FirebaseCaller;
import com.example.serasel.eagle.Utilities.SharedPrefManager;
import com.example.serasel.eagle.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private EditText et_login_username, et_login_password;
    private CardView btn_login_button;
    private LinearLayout login_background;
    private AnimationDrawable animationDrawable;
    private DatabaseReference userRef;
    private Intent navIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        checkIfLoggedin();
    }

    //interfaces in this app are used to get parameters/values in asynchronous functions (Retrieving Firebase Data)
    public interface FirebaseLoginCallback {
        void onLoginCallback(Student student);
    }

    private void checkIfLoggedin() {
        if(SharedPrefManager.getInstance(getApplicationContext()).getKeyUserId() != null){
            proceedLogin();
        }
    }

    private void initViews() {
        et_login_username = findViewById(R.id.et_login_username);
        et_login_password = findViewById(R.id.et_login_password);

        btn_login_button = findViewById(R.id.btn_login_button);
        btn_login_button.setOnClickListener(new LoginUser());

        login_background = findViewById(R.id.login_background);
        login_background.setBackgroundResource(R.drawable.videoback);

        //starts video looping in the background
        animationDrawable = (AnimationDrawable) login_background.getBackground();
        animationDrawable.start();

        userRef = FirebaseDatabase.getInstance().getReference().child("Students");
        navIntent = new Intent(LoginActivity.this, NavigationActivity.class);

    }

    private void proceedLogin(){
        navIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(navIntent);
        finish();
    }

    private class LoginUser implements OnClickListener {
        private String id;
        private String password;

        @Override
        public void onClick(View v) {
            //get text from editext of user and password fields
            id = et_login_username.getText().toString();
            password = et_login_password.getText().toString();
            userRef = (DatabaseReference) userRef.child(id);

            //parameters are context and database reference
            new FirebaseCaller(getApplicationContext(), userRef).verifyUser(id, password,
                    new FirebaseLoginCallback() {
                        @Override
                        public void onLoginCallback(Student student) {
                            //verifyUser gets
                            SharedPrefManager.getInstance(getApplicationContext())
                                    .userLogin(student.getStu_id(), student.getStu_name(),
                                            student.getStu_course());
                            proceedLogin();
                        }
            });

        }
    }
}

