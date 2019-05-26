package com.android.codeaire.meatchain.HelperClasses;

import android.content.Context;
import android.content.SharedPreferences;

public class AccountInfo {
    public static String userEmail = "";
    public static String userPass = "";

    public static void saveAccountInfoIntoSharedPref(Context mContext){
        // Init shared pref
        SharedPreferences prefs = mContext.getSharedPreferences("prefFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        // save Account Info values into Shared pref
        prefsEditor.putString("userEmail", AccountInfo.userEmail);
        prefsEditor.putString("userPass", AccountInfo.userPass);
        prefsEditor.apply();
    }

    public static void getAccountInfoFromSharedPref(Context mContext){
        // Init shared pref
        SharedPreferences prefs = mContext.getSharedPreferences("prefFile", Context.MODE_PRIVATE);

        // get Account Info values into Shared pref
        AccountInfo.userEmail = prefs.getString("userEmail" , "");
        AccountInfo.userPass = prefs.getString("userPass" , "");
    }


    public static void ClearAccountInfo(Context mContext){
        userEmail = "";
        userPass = "";

        // Init shared pref
        SharedPreferences prefs = mContext.getSharedPreferences("prefFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        // save Account Info values into Shared pref
        prefsEditor.putString("userEmail", AccountInfo.userEmail);
        prefsEditor.putString("userPass", AccountInfo.userPass);
        prefsEditor.apply();
    }
}