package com.android.codeaire.meatchain.activities.productInfoScreens;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.codeaire.meatchain.HelperClasses.Constants;
import com.android.codeaire.meatchain.Models.UserInfo;
import com.android.codeaire.meatchain.R;
import com.android.codeaire.meatchain.activities.BaseClasses.BaseActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class UserDetail extends BaseActivity {

    //XML Views
    private EditText UserNameField, UserEmailField, EntryDateField, RoleField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        UserNameField = findViewById(R.id.UserNameField);
        UserEmailField = findViewById(R.id.UserEmailField);
        RoleField = findViewById(R.id.RoleField);
        EntryDateField = findViewById(R.id.EntryDateField);

        listInit();
    }

    private void listInit() {
        showFullScreenLoader();
        //checking Internet connection
        if (!isNetworkAvailable()) {
            showInternetError();
            onBackPressed();
            return;
        }

        FirebaseDatabase.getInstance().getReference(Constants.USER_NODE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hideFullScreenLoader();

                boolean found = false;
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){
                        UserInfo mUser = dataSnapshot2.getValue(UserInfo.class);
                        if(mUser == null || mUser.getEmail() == null || mUser.getEmail().isEmpty()){
                            continue;
                        }

                        if(mUser.getUserId().equals(Constants.UID)){
                            found = true;

                            UserNameField.setText(mUser.getName());
                            UserEmailField.setText(mUser.getEmail());

                            int Role = mUser.getRole();
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

                            // Today Date Info
                            Calendar mCalendar = Calendar.getInstance();
                            mCalendar.setTimeInMillis(Long.parseLong(Constants.UTime));
                            int todayDay = mCalendar.get(Calendar.DAY_OF_MONTH);
                            int todayMonth = mCalendar.get(Calendar.MONTH);
                            int todayYear = mCalendar.get(Calendar.YEAR);
                            EntryDateField.setText(todayDay + "-" + todayMonth + "-" + todayYear);

                            return;
                        }
                    }
                }

                if(!found){
                    showApiOnFailureError();
                    hideFullScreenLoader();
                    onBackPressed();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showApiOnFailureError();
                hideFullScreenLoader();
                onBackPressed();
            }
        });


    }

    public void GoBack(View view) {
        onBackPressed();
    }
}
