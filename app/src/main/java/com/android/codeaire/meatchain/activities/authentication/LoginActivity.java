package com.android.codeaire.meatchain.activities.authentication;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.codeaire.meatchain.HelperClasses.AccountInfo;
import com.android.codeaire.meatchain.HelperClasses.Constants;
import com.android.codeaire.meatchain.Models.UserInfo;
import com.android.codeaire.meatchain.R;
import com.android.codeaire.meatchain.activities.BaseClasses.BaseActivity;
import com.android.codeaire.meatchain.activities.Home.HomeScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends BaseActivity {

    //XML Views
    private EditText UserEmailField,PassField;
    private Button loginBtn;
    TextView SignUpBtn, forgotPassBtn;

    //other variables
    private static final String TAG = LoginActivity.class.getSimpleName();

    // Flags
    private boolean isNextButtonEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initVariables();
        initClickListeners();


        if(!AccountInfo.userEmail.isEmpty() && !AccountInfo.userPass.isEmpty()){
            UserEmailField.setText(AccountInfo.userEmail);
            PassField.setText(AccountInfo.userPass);
            onClickLoginButton();
        }

    }


    private void initVariables() {
        UserEmailField = findViewById(R.id.UserEmailField);
        PassField = findViewById(R.id.PassField);

        loginBtn = findViewById(R.id.loginBtn);
        SignUpBtn = findViewById(R.id.SignUpBtn);
        forgotPassBtn = findViewById(R.id.forgotPassBtn);
    }


    private void initClickListeners() {
        UserEmailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handleButtonState();
                UserEmailField.getBackground().clearColorFilter();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        PassField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handleButtonState();
                PassField.getBackground().clearColorFilter();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickLoginButton();
            }
        });

        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                //finish();
            }
        });

        forgotPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
                //finish();
            }
        });
    }

    private void onClickLoginButton() {
        if(!isNextButtonEnabled){
            return;
        }

        if(!isNetworkAvailable()){
            showInternetError();
            return;
        }

        final String Email = UserEmailField.getText().toString();
        final String Password = PassField.getText().toString();

        showFullScreenLoader();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(Email,Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseDatabase.getInstance().getReference(Constants.USER_NODE).child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                        Constants.myUser = dataSnapshot1.getValue(UserInfo.class);
                                    }

                                    if(Constants.myUser == null || Constants.myUser.getEmail() == null || Constants.myUser.getEmail().isEmpty()){
                                        Constants.myUser = null;
                                        hideFullScreenLoader();
                                        showToast("Something went wrong");
                                        System.out.println("error getting user data , sign out force");
                                        return;
                                    }

                                    showToast("Login Successful");

                                    // save credentials
                                    AccountInfo.userEmail = Email;
                                    AccountInfo.userPass = Password;
                                    AccountInfo.saveAccountInfoIntoSharedPref(LoginActivity.this);

                                    startNextActivity();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    hideFullScreenLoader();
                                    showToast("Something went wrong");
                                    System.out.println("error getting user data , sign out force");
                                }
                            });
                        }
                        else{
                            hideFullScreenLoader();
                            UserEmailField.getBackground().setColorFilter(getResources().getColor(R.color.bt_error_red), PorterDuff.Mode.SRC_ATOP);
                            PassField.getBackground().setColorFilter(getResources().getColor(R.color.bt_error_red), PorterDuff.Mode.SRC_ATOP);
                            showToast(task.getException().getMessage());
                        }
                    }
                });
    }

    private void startNextActivity() {
        /*if(Constants.myUser.getRole() == 0){
            startActivity(new Intent(LoginActivity.this, FarmerForm.class));
        }else{
            startActivity(new Intent(LoginActivity.this, ScannerActivity.class));
        }*/

        startActivity(new Intent(LoginActivity.this, HomeScreen.class));
        finishAffinity();
    }

    private void handleButtonState(){
        if(isAllFieldNotNull()){
            isNextButtonEnabled = true;
            loginBtn.setTextColor(getResources().getColor(R.color.ButtonEnabled));
            loginBtn.setBackground(getResources().getDrawable(R.drawable.bg_btn_enabled));
        }else{
            isNextButtonEnabled = false;
            loginBtn.setTextColor(getResources().getColor(R.color.ButtonDisabled));
            loginBtn.setBackground(getResources().getDrawable(R.drawable.bg_btn_disabled));
        }
    }

    private boolean isAllFieldNotNull(){
        String Email = UserEmailField.getText().toString();
        String Password = PassField.getText().toString();

        return !Email.isEmpty() && !Password.isEmpty();
    }

    int exit_flag = 0;
    @Override
    public void onBackPressed() {
        exit_flag++;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                exit_flag--;
            }
        }, 4000);
        if(exit_flag == 2) {
            finishAffinity();
        }
        else {
            Toast.makeText(this,"Press again to exit",Toast.LENGTH_LONG).show();
        }
    }
}