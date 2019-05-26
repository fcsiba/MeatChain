package com.android.codeaire.meatchain.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.codeaire.meatchain.HelperClasses.Constants;
import com.android.codeaire.meatchain.R;
import com.android.codeaire.meatchain.activities.BaseClasses.BaseActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HelpSupportActivty extends BaseActivity {

    TextView DataText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_support_activty);

        DataText = findViewById(R.id.DataText);

        getData();
    }


    public void getData(){
        if(!isNetworkAvailable()){
            showToast(getResources().getString(R.string.NoInternetTextWithOutRetry));
            onBackPressed();
            return;
        }

        showFullScreenLoader();
        FirebaseDatabase.getInstance().getReference(Constants.POLICY_NODE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hideFullScreenLoader();
                String data = dataSnapshot.getValue(String.class);

                if(data == null || data.isEmpty()){
                    showToast("No Data Found, please try again");
                    onBackPressed();
                    return;
                }

                // Display Data here
                DataText.setText(data);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideFullScreenLoader();
                Snackbar snackbar = Snackbar
                        .make(findViewById(android.R.id.content), getResources().getString(R.string.ApiOnFailure), Snackbar.LENGTH_INDEFINITE)
                        .setAction("Retry!", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getData();
                            }
                        });
                snackbar.show();
            }
        });
    }

    public void GoBack(View view) {
        onBackPressed();
    }
}
