package com.android.codeaire.meatchain.activities.productInfoScreens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.codeaire.meatchain.HelperClasses.Constants;
import com.android.codeaire.meatchain.R;
import com.android.codeaire.meatchain.activities.BaseClasses.BaseActivity;
import com.android.codeaire.meatchain.activities.scanner.ScannerActivity;

public class ProductInfoActivity extends BaseActivity {

    // XML Views
    TextView valueType, valueGender, valueAge, valueWeight;
    TextView ViewDetails0Button, ViewDetails1Button, ViewDetails2Button, ViewDetails3Button;
    TextView valueDateOfSlaughter;
    TextView valueMeatPart, valueMeatType, valuePackagingDate, valueExpiryDate;
    TextView valuePurchasesDate;
    Button BtnOk, BtnScanQR;
    LinearLayout ButcherInfoLayout, PackagingInfoLayout, DistributionInfoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        if(Constants.myProduct == null || Constants.myProduct.getProductId().isEmpty()){
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

        BtnOk = findViewById(R.id.BtnOk);
        BtnScanQR = findViewById(R.id.BtnScanQR);

        ButcherInfoLayout = findViewById(R.id.ButcherInfoLayout);
        PackagingInfoLayout = findViewById(R.id.PackagingInfoLayout);
        DistributionInfoLayout = findViewById(R.id.DistributionInfoLayout);

        // Display Data here
        valueType.setText(Constants.myProduct.getType());
        valueGender.setText(Constants.myProduct.getSex());
        valueAge.setText(Constants.myProduct.getAge());
        valueWeight.setText(Constants.myProduct.getWeight());
        valueDateOfSlaughter.setText(Constants.myProduct.getDateOfSlaughter());
        valueMeatPart.setText(Constants.myProduct.getMeatPart());
        valueMeatType.setText(Constants.myProduct.getMeatType());
        valuePackagingDate.setText(Constants.myProduct.getPackagingDate());
        valueExpiryDate.setText(Constants.myProduct.getExpiryDate());
        valuePurchasesDate.setText(Constants.myProduct.getDateOfReceiving());

        int status = Constants.myProduct.getProcessStatus();
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

        BtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        BtnScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ViewDetails0Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.UID = Constants.myProduct.getInfo0Id();
                Constants.UTime = Constants.myProduct.getInfo0Time();
                startActivity(new Intent(ProductInfoActivity.this, UserDetail.class));
            }
        });

        ViewDetails1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.UID = Constants.myProduct.getInfo1Id();
                Constants.UTime = Constants.myProduct.getInfo1Time();
                startActivity(new Intent(ProductInfoActivity.this, UserDetail.class));
            }
        });

        ViewDetails2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.UID = Constants.myProduct.getInfo2Id();
                Constants.UTime = Constants.myProduct.getInfo2Time();
                startActivity(new Intent(ProductInfoActivity.this, UserDetail.class));
            }
        });

        ViewDetails3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.UID = Constants.myProduct.getInfo3Id();
                Constants.UTime = Constants.myProduct.getInfo3Time();
                startActivity(new Intent(ProductInfoActivity.this, UserDetail.class));
            }
        });

    }
}
