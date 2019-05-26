package com.android.codeaire.meatchain.activities.Profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.codeaire.meatchain.HelperClasses.AccountInfo;
import com.android.codeaire.meatchain.HelperClasses.Constants;
import com.android.codeaire.meatchain.R;

public class UserProfileActivity extends AppCompatActivity {

    //XML Views
    private EditText UserNameField, UserEmailField, PassField, RoleField;
    private Button ChangePassBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        UserNameField = findViewById(R.id.UserNameField);
        UserEmailField = findViewById(R.id.UserEmailField);
        PassField = findViewById(R.id.PassField);
        RoleField = findViewById(R.id.RoleField);
        ChangePassBtn = findViewById(R.id.ChangePassBtn);

        updateInfo();

       ChangePassBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                startActivity(new Intent(UserProfileActivity.this, UpdatePassword.class));
           }
       });
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateInfo();
    }

    public void updateInfo(){
        UserNameField.setText(Constants.myUser.getName());
        UserEmailField.setText(AccountInfo.userEmail);
        PassField.setText(AccountInfo.userPass);

        int Role = Constants.myUser.getRole();
        if(Role == 0){
            RoleField.setText("Farmer");
        }else if(Role == 1){
            RoleField.setText("Butcher");
        }else if(Role == 2){
            RoleField.setText("Packaging");
        }else if(Role == 3){
            RoleField.setText("Distributor");
        }else if(Role == 4){
            RoleField.setText("Customer");
        }else{
            RoleField.setText("User");
        }
    }

    public void GoBack(View view) {
        onBackPressed();
    }
}
