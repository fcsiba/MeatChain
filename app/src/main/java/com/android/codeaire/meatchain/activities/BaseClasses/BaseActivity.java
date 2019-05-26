package com.android.codeaire.meatchain.activities.BaseClasses;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.codeaire.meatchain.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public abstract class BaseActivity extends AppCompatActivity {

    // Return true if Internet is connected
    public boolean isNetworkAvailable() {
        try{
            ConnectivityManager connectivityManager = (ConnectivityManager)  this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return (activeNetworkInfo != null) && (activeNetworkInfo.isConnected());
        } catch (Exception e){
            System.out.println("BaseActivity : isNetworkAvailable : " + e);
            return false;
        }
    }


    //Close Android built-in keyboard if Open
    public void hideSoftKeyboardIfOpen(Activity activity) {
        try{
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e){
            System.out.println("BaseActivity : hideSoftKeyboardIfOpen : " + e);
        }
    }


    // Display simple SnackBar Message "No Internet connection!"
    public void showInternetError(){
        try{
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content), getResources().getString(R.string.NoInternetTextWithOutRetry), Snackbar.LENGTH_SHORT);
            snackbar.show();
        } catch (Exception e){
            System.out.println("BaseActivity: showInternetError : " + e);
        }
    }


    // Display simple SnackBar Message for those Api call that falls into onFailure.
    public void showApiOnFailureError(){
        showToast(getResources().getString(R.string.ApiOnFailure));
    }

    private boolean isListenerAttachedToLoader = false;
    public void showFullScreenLoader(){
        findViewById(R.id.blurLayer).setVisibility(View.VISIBLE);

        if(!isListenerAttachedToLoader){
            isListenerAttachedToLoader = true;
            findViewById(R.id.blurLayer).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Do nothing to disable UI clicks
                }
            });
        }
    }

    public void hideFullScreenLoader(){
        findViewById(R.id.blurLayer).setVisibility(View.GONE);
    }

    public void showToast(String msg){
        Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
