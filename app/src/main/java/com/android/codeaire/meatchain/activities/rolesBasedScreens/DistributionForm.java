package com.android.codeaire.meatchain.activities.rolesBasedScreens;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.android.codeaire.meatchain.HelperClasses.Constants;
import com.android.codeaire.meatchain.R;
import com.android.codeaire.meatchain.activities.BaseClasses.BaseActivity;
import com.android.codeaire.meatchain.activities.productInfoScreens.ProductInfoActivity;
import com.android.codeaire.meatchain.activities.scanner.ScannerActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DistributionForm extends BaseActivity {

    // XML Views
    TextView valueType, valueGender, valueAge, valueWeight;
    TextView valueDateOfSlaughter;
    TextView valueMeatPart, valueMeatType, valuePackagingDate, valueExpiryDate;
    EditText FieldReceivingDate;
    private Button submitButton;

    //Class Variables
    private static final String TAG = DistributionForm.class.getSimpleName();
    private int mYear, mMonth, mDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distribution_form);

        if(Constants.myProduct == null || Constants.myProduct.getProductId().isEmpty()){
            showToast("Something went wrong, please try again");
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

        FieldReceivingDate = findViewById(R.id.FieldReceivingDate);
        submitButton = findViewById(R.id.submitButton);

        // Display Product Information
        valueType.setText(Constants.myProduct.getType());
        valueGender.setText(Constants.myProduct.getSex());
        valueAge.setText(Constants.myProduct.getAge());
        valueWeight.setText(Constants.myProduct.getWeight());
        valueDateOfSlaughter.setText(Constants.myProduct.getDateOfSlaughter());
        valueMeatPart.setText(Constants.myProduct.getMeatPart());
        valueMeatType.setText(Constants.myProduct.getMeatType());
        valuePackagingDate.setText(Constants.myProduct.getPackagingDate());
        valueExpiryDate.setText(Constants.myProduct.getExpiryDate());

        // Get Latest Date
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public void initClickListeners(){

        //Opens Date Picker
        FieldReceivingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), datePickerListener, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSubmitButton();
            }
        });
    }

    public void onClickSubmitButton() {
        if (!isNetworkAvailable()) {
            return;
        }

        // Get Values from Fields
        String DOR = FieldReceivingDate.getText().toString();


        // check if any field is empty
        if (!DOR.isEmpty()) {
            // update product data in products node

            Constants.myProduct.setDateOfReceiving(DOR);
            Constants.myProduct.setProcessStatus(3);
            Constants.myProduct.setInfo3Id(Constants.myUser.getUserId());
            Constants.myProduct.setInfo3Time(System.currentTimeMillis() + "");

            FirebaseDatabase.getInstance().getReference(Constants.PRODUCTS_NODE).child(Constants.myProduct.getProductId())
                    .setValue(Constants.myProduct)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast("Data submitted successfully");
                            startActivity(new Intent(DistributionForm.this, ProductInfoActivity.class));
                            finishAffinity();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showToast("SomeThing went wrong, please try again");
                }
            });
        } else {
            showToast("Please fill all field");
        }
    }

    //DoB listener when user selects date from calender and press ok
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            mYear = year;
            mMonth = month;
            mDay = day;

            //Format Date To display
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);
            String formatToDisplay = new SimpleDateFormat("dd-MM-yyyy").format(c.getTime());

            // display formatted date
            FieldReceivingDate.setText(formatToDisplay);
        }
    };
}
