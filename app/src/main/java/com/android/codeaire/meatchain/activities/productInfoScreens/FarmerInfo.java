package com.android.codeaire.meatchain.activities.productInfoScreens;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.codeaire.meatchain.HelperClasses.Constants;
import com.android.codeaire.meatchain.Models.Product;
import com.android.codeaire.meatchain.R;
import com.android.codeaire.meatchain.activities.BaseClasses.BaseActivity;
import com.android.codeaire.meatchain.activities.DisplayQRCodeActivity;
import com.android.codeaire.meatchain.activities.Home.HomeScreen;
import com.android.codeaire.meatchain.activities.rolesBasedScreens.FarmerForm;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FarmerInfo extends BaseActivity {

    // XML Views
    TextView valueType, valueGender, valueAge, valueWeight, ViewDetails0Button;
    Button BtnViewQR, BtnAddAnother;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_info);

        if(Constants.myProduct == null || Constants.myProduct.getProductId().isEmpty()){
            showToast("No Data Found, please try again");
            onBackPressed();
            return;
        }

        initVariables();
        initClickListeners();
        listInit();
    }

    private void initVariables() {
        valueType = findViewById(R.id.valueType);
        valueGender = findViewById(R.id.valueGender);
        valueAge = findViewById(R.id.valueAge);
        valueWeight = findViewById(R.id.valueWeight);
        BtnViewQR = findViewById(R.id.BtnViewQR);
        BtnAddAnother = findViewById(R.id.BtnAddAnother);
        ViewDetails0Button = findViewById(R.id.ViewDetails0Button);
    }

    public void initClickListeners(){

        BtnViewQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FarmerInfo.this, DisplayQRCodeActivity.class));
            }
        });

        BtnAddAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo : change here
                startActivity(new Intent(FarmerInfo.this, FarmerForm.class));
                finish();
            }
        });

        ViewDetails0Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.UID = Constants.myProduct.getInfo0Id();
                Constants.UTime = Constants.myProduct.getInfo0Time();
                startActivity(new Intent(FarmerInfo.this, UserDetail.class));
            }
        });
    }

    private void listInit() {
        showFullScreenLoader();

        //checking Internet connection
        if (!isNetworkAvailable()) {
            hideFullScreenLoader();
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content), getResources().getString(R.string.NoInternetTextWithRetry), Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry!", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listInit();
                        }
                    });
            snackbar.show();
            return;
        }

        LoadData();
    }

    private void LoadData() {

        FirebaseDatabase.getInstance().getReference(Constants.PRODUCTS_NODE).child(Constants.myProduct.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hideFullScreenLoader();
                Product productDisplay = dataSnapshot.getValue(Product.class);

                if(productDisplay == null || productDisplay.getProductId() == null){
                    showToast("No Data Found, please try again");
                    onBackPressed();
                    return;
                }

                // Display Data here
                valueType.setText(productDisplay.getType());
                valueGender.setText(productDisplay.getSex());
                valueAge.setText(productDisplay.getAge());
                valueWeight.setText(productDisplay.getWeight());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideFullScreenLoader();
                Snackbar snackbar = Snackbar
                        .make(findViewById(android.R.id.content), getResources().getString(R.string.ApiOnFailure), Snackbar.LENGTH_INDEFINITE)
                        .setAction("Retry!", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                listInit();
                            }
                        });
                snackbar.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FarmerInfo.this, HomeScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
