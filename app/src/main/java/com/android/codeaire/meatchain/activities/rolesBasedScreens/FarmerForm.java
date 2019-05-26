package com.android.codeaire.meatchain.activities.rolesBasedScreens;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.codeaire.meatchain.HelperClasses.Constants;
import com.android.codeaire.meatchain.Models.Product;
import com.android.codeaire.meatchain.R;
import com.android.codeaire.meatchain.activities.BaseClasses.BaseActivity;
import com.android.codeaire.meatchain.activities.Home.HomeScreen;
import com.android.codeaire.meatchain.activities.productInfoScreens.FarmerInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FarmerForm extends BaseActivity {

    // xml views
    RadioGroup radioGroup_Type, radioGroup_Gender;
    private EditText FieldDOB, FieldWeight;
    private Button submitButton;

    //Class Variables
    private static final String TAG = FarmerForm.class.getSimpleName();
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_form);

        initVariables();

        //Opens Date Picker
        FieldDOB.setOnClickListener(new View.OnClickListener() {
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

    private void initVariables() {
        radioGroup_Type = findViewById(R.id.radioGroup_Type);
        radioGroup_Gender = findViewById(R.id.radioGroup_Gender);
        FieldDOB = findViewById(R.id.FieldDOB);
        FieldWeight = findViewById(R.id.FieldWeight);
        submitButton = findViewById(R.id.submitButton);

        // Get Latest Date
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public void onClickSubmitButton() {
        if (!isNetworkAvailable()) {
            return;
        }

        // Get Values from Fields
        int selectedId = radioGroup_Type.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);
        String Type = String.valueOf(radioButton.getText());

        int selectedId2 = radioGroup_Gender.getCheckedRadioButtonId();
        RadioButton radioButton2 = findViewById(selectedId2);
        String Gender = String.valueOf(radioButton2.getText());

        String DOB = FieldDOB.getText().toString();

        String Weight = FieldWeight.getText().toString();


        // check if any field is empty
        if (!Type.isEmpty() && !Gender.isEmpty() && !DOB.isEmpty() && !Weight.isEmpty()) {
            // pushing product data in products node
            String newId = FirebaseDatabase.getInstance().getReference(Constants.PRODUCTS_NODE).push().getKey();

            final Product product = new Product();
            product.setProductId(newId);
            product.setProcessStatus(0);
            product.setType(Type);
            product.setSex(Gender);
            product.setAge(DOB);
            product.setWeight(Weight);
            product.setInfo0Id(Constants.myUser.getUserId());
            product.setInfo0Time(System.currentTimeMillis() + "");

            FirebaseDatabase.getInstance().getReference(Constants.PRODUCTS_NODE).child(newId)
                    .setValue(product)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast("Data submitted successfully");
                            Constants.myProduct = product;
                            startActivity(new Intent(FarmerForm.this, FarmerInfo.class));
                            finish();

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
            FieldDOB.setText(formatToDisplay);
        }
    };

    public void GoBack(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FarmerForm.this, HomeScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}