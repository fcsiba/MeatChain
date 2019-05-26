package com.android.codeaire.meatchain.activities.authentication;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.codeaire.meatchain.HelperClasses.AccountInfo;
import com.android.codeaire.meatchain.HelperClasses.Constants;
import com.android.codeaire.meatchain.Models.UserInfo;
import com.android.codeaire.meatchain.R;
import com.android.codeaire.meatchain.activities.BaseClasses.BaseActivity;
import com.android.codeaire.meatchain.activities.Home.HomeScreen;
import com.android.codeaire.meatchain.activities.rolesBasedScreens.FarmerForm;
import com.android.codeaire.meatchain.activities.scanner.ScannerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends BaseActivity {

    //XML Views
    private EditText UserNameField, UserEmailField, PassField, ConfirmPassField;
    private Button loginBtn;
    Spinner rolesSpinner;

    //other variables
    private static final String TAG = LoginActivity.class.getSimpleName();

    // Flags
    private boolean isNextButtonEnabled = false;

    private int selectedRole = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initVariables();
        initClickListeners();
    }


    private void initVariables() {
        UserNameField = findViewById(R.id.UserNameField);
        UserEmailField = findViewById(R.id.UserEmailField);
        PassField = findViewById(R.id.PassField);
        ConfirmPassField = findViewById(R.id.ConfirmPassField);
        loginBtn = findViewById(R.id.loginBtn);

        //init Spinner
        rolesSpinner = findViewById(R.id.rolesSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.roles_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        rolesSpinner.setAdapter(adapter);
        // Apply listener
        rolesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                //showToast("Selected position : " + position);
                selectedRole = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void initClickListeners() {
        UserNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handleButtonState();
                UserNameField.getBackground().clearColorFilter();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

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


        ConfirmPassField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handleButtonState();
                ConfirmPassField.getBackground().clearColorFilter();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isNextButtonEnabled){
                    return;
                }

                if(!isNetworkAvailable()){
                    showInternetError();
                    return;
                }

                final String Name = UserNameField.getText().toString();
                final String Email = UserEmailField.getText().toString();
                final String Password = PassField.getText().toString();
                String ConfirmPassword = ConfirmPassField.getText().toString();
                final int Role = selectedRole;

                if(Password.equals(ConfirmPassword)){
                    showFullScreenLoader();
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(Email,Password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        // pushing user data in user node
                                        DatabaseReference UsersDataBaseReference = FirebaseDatabase.getInstance().getReference(Constants.USER_NODE).child(FirebaseAuth.getInstance().getUid());
                                        String childId = UsersDataBaseReference.push().getKey();
                                        UserInfo mUserData = new UserInfo(childId, Name, Email, Password, Role);
                                        Constants.myUser = mUserData;
                                        UsersDataBaseReference.child(childId).setValue(mUserData);

                                        showToast("User successfully created");

                                        // save credentials
                                        AccountInfo.userEmail = Email;
                                        AccountInfo.userPass = Password;
                                        AccountInfo.saveAccountInfoIntoSharedPref(SignUpActivity.this);

                                        startNextActivity();
                                    }
                                    else{
                                        hideFullScreenLoader();
                                        showToast(task.getException().getMessage() + " Fail to create User");
                                    }
                                }
                            });
                }else{
                    PassField.getBackground().setColorFilter(getResources().getColor(R.color.bt_error_red), PorterDuff.Mode.SRC_ATOP);
                    ConfirmPassField.getBackground().setColorFilter(getResources().getColor(R.color.bt_error_red), PorterDuff.Mode.SRC_ATOP);
                    showToast("Password Does not match");
                }
            }
        });
    }

    private void startNextActivity() {
        /*if(Constants.myUser.getRole() == 0){
            startActivity(new Intent(SignUpActivity.this, FarmerForm.class));
        }else{
            startActivity(new Intent(SignUpActivity.this, ScannerActivity.class));
        }*/

        startActivity(new Intent(SignUpActivity.this, HomeScreen.class));
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
        String Name = UserNameField.getText().toString();
        String Email = UserEmailField.getText().toString();
        String Password = PassField.getText().toString();
        String ConfirmPassword = ConfirmPassField.getText().toString();

        return !Name.isEmpty() && !Email.isEmpty() && !Password.isEmpty() && !ConfirmPassword.isEmpty();
    }
}
