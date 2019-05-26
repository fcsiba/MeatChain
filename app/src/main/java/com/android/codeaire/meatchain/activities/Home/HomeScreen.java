package com.android.codeaire.meatchain.activities.Home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.codeaire.meatchain.HelperClasses.AccountInfo;
import com.android.codeaire.meatchain.HelperClasses.Constants;
import com.android.codeaire.meatchain.R;
import com.android.codeaire.meatchain.activities.AboutUsActivty;
import com.android.codeaire.meatchain.activities.BaseClasses.BaseActivity;
import com.android.codeaire.meatchain.activities.HelpSupportActivty;
import com.android.codeaire.meatchain.activities.Profile.UserProfileActivity;
import com.android.codeaire.meatchain.activities.ViewProductsActivity;
import com.android.codeaire.meatchain.activities.authentication.LoginActivity;
import com.android.codeaire.meatchain.activities.rolesBasedScreens.FarmerForm;
import com.android.codeaire.meatchain.activities.scanner.ScannerActivity;
import com.google.firebase.auth.FirebaseAuth;

public class HomeScreen extends BaseActivity {

    // Xml View
    TextView userNameTxt, ViewDetailsButton;
    RelativeLayout AddProductBtn, ScanQRBtn, ViewProductsBtn, helpSupportBtn, AboutUsBtn, LogOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_farmer);

        initVariables();
        initClickListeners();

        if(Constants.myUser.getRole() == 0){
            ScanQRBtn.setVisibility(View.GONE);
        }else if(Constants.myUser.getRole() == 4){
            AddProductBtn.setVisibility(View.GONE);
            ViewProductsBtn.setVisibility(View.GONE);
        }else{
            AddProductBtn.setVisibility(View.GONE);
        }
    }

    private void initVariables() {
        userNameTxt = findViewById(R.id.userNameTxt);
        ViewDetailsButton = findViewById(R.id.ViewDetailsButton);
        AddProductBtn = findViewById(R.id.AddProductBtn);
        ScanQRBtn = findViewById(R.id.ScanQRBtn);
        ViewProductsBtn = findViewById(R.id.ViewProductsBtn);
        helpSupportBtn = findViewById(R.id.helpSupportBtn);
        AboutUsBtn = findViewById(R.id.AboutUsBtn);
        LogOutBtn = findViewById(R.id.LogOutBtn);

        userNameTxt.setText("Welcome, "  + Constants.myUser.getName());
    }

    private void initClickListeners() {
        AddProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, FarmerForm.class));
            }
        });

        ScanQRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, ScannerActivity.class));
            }
        });

        ViewProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, ViewProductsActivity.class));
            }
        });

        helpSupportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, HelpSupportActivty.class));
            }
        });

        AboutUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, AboutUsActivty.class));
            }
        });

        LogOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showFullScreenLoader();
                    FirebaseAuth.getInstance().signOut();
                    AccountInfo.ClearAccountInfo(HomeScreen.this);
                    Constants.myUser = null;
                    Constants.myProduct = null;
                    startActivity(new Intent(HomeScreen.this, LoginActivity.class));
                }catch (Exception e){
                    hideFullScreenLoader();
                    AccountInfo.ClearAccountInfo(HomeScreen.this);
                    Constants.myUser = null;
                    Constants.myProduct = null;
                    startActivity(new Intent(HomeScreen.this, LoginActivity.class));
                }
            }
        });


        ViewDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, UserProfileActivity.class));
            }
        });
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
