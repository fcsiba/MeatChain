package com.android.codeaire.meatchain.activities.scanner;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.codeaire.meatchain.HelperClasses.Constants;
import com.android.codeaire.meatchain.Models.Product;
import com.android.codeaire.meatchain.R;
import com.android.codeaire.meatchain.activities.BaseClasses.BaseActivity;
import com.android.codeaire.meatchain.activities.productInfoScreens.ProductInfoActivity;
import com.android.codeaire.meatchain.activities.rolesBasedScreens.ButcherForm;
import com.android.codeaire.meatchain.activities.rolesBasedScreens.DistributionForm;
import com.android.codeaire.meatchain.activities.rolesBasedScreens.PackagingForm;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.google.android.gms.common.util.CollectionUtils.listOf;

public class ScannerActivity extends BaseActivity {

    // XML Views
    ZXingScannerView qrCodeScanner;
    Button scanQR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        qrCodeScanner = findViewById(R.id.qrCodeScanner);
        setScannerProperties();

        scanQR = findViewById(R.id.scanQR);
        scanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScanWrapper();
            }
        });

        startScanWrapper();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startScanWrapper();
    }

    // Set Qr code scanner basic properties.
    private void setScannerProperties() {
        qrCodeScanner.setFormats(listOf(BarcodeFormat.QR_CODE));
        qrCodeScanner.setAutoFocus(true);
        qrCodeScanner.setLaserColor(R.color.colorAccent);
        qrCodeScanner.setMaskColor(R.color.colorAccent);
        if (Build.MANUFACTURER.equalsIgnoreCase("HUAWEI"))
            qrCodeScanner.setAspectTolerance(0.5f);
    }

    public void startScanWrapper() {
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1 && !checkIfAlreadyHaveCameraPermission()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                showSettingsDialog();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 104);
            }
        } else {
            startScan();
        }
    }

    private void startScan(){
        qrCodeScanner.startCamera();
        qrCodeScanner.setResultHandler(new ZXingScannerView.ResultHandler() {
            @Override
            public void handleResult(Result rawResult) {
                // todo: handle result here
                if (rawResult != null) {
                    Constants.myProductId = rawResult.getText();
                    //Toast.makeText(ScannerActivity.this, rawResult.getText(), Toast.LENGTH_LONG).show();
                    showToast("Scanned successfully\nRetrieving data...");
                    listInit();
                }
            }
        });

        scanQR.setText("Restart Scanning");
    }

    private void listInit() {
        //checking Internet connection
        if (!isNetworkAvailable()) {
            showInternetError();
            return;
        }

        showFullScreenLoader();

        LoadData();
    }

    private void LoadData() {
        FirebaseDatabase.getInstance().getReference(Constants.PRODUCTS_NODE).child(Constants.myProductId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hideFullScreenLoader();
                Product productDisplay = dataSnapshot.getValue(Product.class);

                if(productDisplay == null || productDisplay.getProductId() == null || productDisplay.getProductId().isEmpty()){
                    showToast("No Data Found, please try again");
                    return;
                }

                Constants.myProduct = productDisplay;
                //todo: check product status here and open different forms
                int productStatus = Constants.myProduct.getProcessStatus();
                int userRole = Constants.myUser.getRole();

                if(productStatus == 0 && userRole == 1){
                    startActivity(new Intent(ScannerActivity.this, ButcherForm.class));
                }else if(productStatus == 1 && userRole == 2){
                    startActivity(new Intent(ScannerActivity.this, PackagingForm.class));
                }else if(productStatus == 2 && userRole == 3){
                    startActivity(new Intent(ScannerActivity.this, DistributionForm.class));
                }else{
                    startActivity(new Intent(ScannerActivity.this, ProductInfoActivity.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideFullScreenLoader();
                showToast("No Data Found, please try again");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeScanner.stopCamera();
    }


//****************************************Permission RECORD_AUDIO Handler Start *********************************************************//
    private boolean checkIfAlreadyHaveCameraPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    //open settings in android phone for users so they can explicitly provide permissions.
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("\"Meet Chain\" needs to access camera");
        builder.setMessage("Allow Meet Chain to access your camera to scan QR code");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 104);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 104:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted
                    startScan();
                } else {
                    //permission not granted.
                    Toast.makeText(this, "Camera access permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
