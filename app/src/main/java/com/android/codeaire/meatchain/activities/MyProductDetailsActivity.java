package com.android.codeaire.meatchain.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.codeaire.meatchain.HelperClasses.Constants;
import com.android.codeaire.meatchain.R;
import com.android.codeaire.meatchain.activities.BaseClasses.BaseActivity;
import com.android.codeaire.meatchain.activities.productInfoScreens.UserDetail;

public class MyProductDetailsActivity extends BaseActivity {

    // XML Views
    TextView valueType, valueGender, valueAge, valueWeight;
    TextView ViewDetails0Button, ViewDetails1Button, ViewDetails2Button, ViewDetails3Button;
    TextView valueDateOfSlaughter;
    TextView valueMeatPart, valueMeatType, valuePackagingDate, valueExpiryDate;
    TextView valuePurchasesDate;
    LinearLayout ButcherInfoLayout, PackagingInfoLayout, DistributionInfoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_product_details);

        if(Constants.mySingleProduct == null || Constants.mySingleProduct.getProductId().isEmpty()){
            showToast("No Data Found, please try again");
            onBackPressed();
            return;
        }

        initVariables();
        initClickListeners();
    }

    private void initVariables() {
        valueType = findViewById(R.id.valueType);
        valueGender = findViewById(R.id.valueGender);
        valueAge = findViewById(R.id.valueAge);
        valueWeight = findViewById(R.id.valueWeight);
        valueDateOfSlaughter = findViewById(R.id.valueDateOfSlaughter);
        valueMeatPart = findViewById(R.id.valueMeatPart);
        valueMeatType = findViewById(R.id.valueMeatType);
        valuePackagingDate = findViewById(R.id.valuePackagingDate);
        valueExpiryDate = findViewById(R.id.valueExpiryDate);
        valuePurchasesDate = findViewById(R.id.valuePurchasesDate);

        ViewDetails0Button = findViewById(R.id.ViewDetails0Button);
        ViewDetails1Button = findViewById(R.id.ViewDetails1Button);
        ViewDetails2Button = findViewById(R.id.ViewDetails2Button);
        ViewDetails3Button = findViewById(R.id.ViewDetails3Button);

        ButcherInfoLayout = findViewById(R.id.ButcherInfoLayout);
        PackagingInfoLayout = findViewById(R.id.PackagingInfoLayout);
        DistributionInfoLayout = findViewById(R.id.DistributionInfoLayout);

        // Display Data here
        valueType.setText(Constants.mySingleProduct.getType());
        valueGender.setText(Constants.mySingleProduct.getSex());
        valueAge.setText(Constants.mySingleProduct.getAge());
        valueWeight.setText(Constants.mySingleProduct.getWeight());
        valueDateOfSlaughter.setText(Constants.mySingleProduct.getDateOfSlaughter());
        valueMeatPart.setText(Constants.mySingleProduct.getMeatPart());
        valueMeatType.setText(Constants.mySingleProduct.getMeatType());
        valuePackagingDate.setText(Constants.mySingleProduct.getPackagingDate());
        valueExpiryDate.setText(Constants.mySingleProduct.getExpiryDate());
        valuePurchasesDate.setText(Constants.mySingleProduct.getDateOfReceiving());

        int status = Constants.mySingleProduct.getProcessStatus();
        if(status == 0){
            ButcherInfoLayout.setVisibility(View.GONE);
            PackagingInfoLayout.setVisibility(View.GONE);
            DistributionInfoLayout.setVisibility(View.GONE);
        }else if(status == 1){
            PackagingInfoLayout.setVisibility(View.GONE);
            DistributionInfoLayout.setVisibility(View.GONE);
        }else if(status == 2){
            DistributionInfoLayout.setVisibility(View.GONE);
        }
    }

    public void initClickListeners(){

        ViewDetails0Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.UID = Constants.mySingleProduct.getInfo0Id();
                Constants.UTime = Constants.mySingleProduct.getInfo0Time();
                startActivity(new Intent(MyProductDetailsActivity.this, UserDetail.class));
            }
        });

        ViewDetails1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.UID = Constants.mySingleProduct.getInfo1Id();
                Constants.UTime = Constants.mySingleProduct.getInfo1Time();
                startActivity(new Intent(MyProductDetailsActivity.this, UserDetail.class));
            }
        });

        ViewDetails2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.UID = Constants.mySingleProduct.getInfo2Id();
                Constants.UTime = Constants.mySingleProduct.getInfo2Time();
                startActivity(new Intent(MyProductDetailsActivity.this, UserDetail.class));
            }
        });

        ViewDetails3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.UID = Constants.mySingleProduct.getInfo3Id();
                Constants.UTime = Constants.mySingleProduct.getInfo3Time();
                startActivity(new Intent(MyProductDetailsActivity.this, UserDetail.class));
            }
        });

    }
}
