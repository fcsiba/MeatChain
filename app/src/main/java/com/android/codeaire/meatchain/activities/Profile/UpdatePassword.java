package com.android.codeaire.meatchain.activities.Profile;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.codeaire.meatchain.HelperClasses.AccountInfo;
import com.android.codeaire.meatchain.HelperClasses.Constants;
import com.android.codeaire.meatchain.R;
import com.android.codeaire.meatchain.activities.BaseClasses.BaseActivity;
import com.android.codeaire.meatchain.activities.productInfoScreens.ProductInfoActivity;
import com.android.codeaire.meatchain.activities.rolesBasedScreens.DistributionForm;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class UpdatePassword extends BaseActivity {

    //XML Views
    private EditText UserEmailField,PassField, ConfirmPassField, oldPassField;
    private Button loginBtn;

    //other variables
    private static final String TAG = UpdatePassword.class.getSimpleName();

    // Flags
    private boolean isNextButtonEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        initVariables();
        initClickListeners();
    }


    private void initVariables() {
        UserEmailField = findViewById(R.id.UserEmailField);
        PassField = findViewById(R.id.PassField);
        ConfirmPassField = findViewById(R.id.ConfirmPassField);
        loginBtn = findViewById(R.id.loginBtn);
        oldPassField = findViewById(R.id.oldPassField);

        UserEmailField.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
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

        oldPassField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handleButtonState();
                oldPassField.getBackground().clearColorFilter();
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

                String UserName = UserEmailField.getText().toString();
                String oldpass = oldPassField.getText().toString();
                final String Password = PassField.getText().toString();
                String ConfirmPassword = ConfirmPassField.getText().toString();

                if(Password.equals(ConfirmPassword)){

                    showFullScreenLoader();

                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential credential = EmailAuthProvider.getCredential(UserName, oldpass);
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.updatePassword(Password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    showToast("Password Successfully updated");

                                                    Constants.myUser.setPassword(Password);

                                                    // save credentials
                                                    AccountInfo.userEmail = Constants.myUser.getEmail();
                                                    AccountInfo.userPass = Password;
                                                    AccountInfo.saveAccountInfoIntoSharedPref(UpdatePassword.this);

                                                    onBackPressed();
                                                } else {
                                                    showToast(task.getException().getMessage() + " password not updated");
                                                    hideFullScreenLoader();
                                                }
                                            }
                                        });
                                    } else {
                                        showToast(task.getException().getMessage() + " Fail to authorized User");
                                        hideFullScreenLoader();
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
        String oldpass = oldPassField.getText().toString();
        String Password = PassField.getText().toString();
        String ConfirmPassword = ConfirmPassField.getText().toString();

        return !Email.isEmpty() && !Password.isEmpty() && !ConfirmPassword.isEmpty() && !oldpass.isEmpty();
    }

    public void GoBack(View view) {
        onBackPressed();
    }
}
