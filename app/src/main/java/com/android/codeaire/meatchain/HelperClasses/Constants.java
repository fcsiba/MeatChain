package com.android.codeaire.meatchain.HelperClasses;

import com.android.codeaire.meatchain.Models.Product;
import com.android.codeaire.meatchain.Models.UserInfo;

/**
 * Created by Asif Ali on 20-Feb-18.
 */

//This class includes Constants and Global Variables.
public class Constants {
    //CONSTANTS
    public static String USER_NODE = "users/";
    public static String PRODUCTS_NODE = "products/";
    public static String ABOUT_NODE = "about/";
    public static String POLICY_NODE = "policy/";

    public static UserInfo myUser;
    public static Product myProduct;

    // Used only for displaying product into MyProductsDetailActivity
    public static Product mySingleProduct;

    // used for scanner activity
    public static String myProductId = "";



    // only used To UserDetail Activity
    public static String UID = "";
    public static String UTime = "";
}