package com.android.codeaire.meatchain.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.codeaire.meatchain.HelperClasses.Constants;
import com.android.codeaire.meatchain.HelperClasses.QRCodeHelper;
import com.android.codeaire.meatchain.R;
import com.android.codeaire.meatchain.activities.BaseClasses.BaseActivity;
import com.android.codeaire.meatchain.activities.rolesBasedScreens.FarmerForm;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.io.FileOutputStream;

public class DisplayQRCodeActivity extends BaseActivity {

    Bitmap bitmapQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_qrcode);

        if (Constants.myProduct == null || Constants.myProduct.getProductId().isEmpty()) {
            showToast("No Data Found, please try again");
            onBackPressed();
            return;
        }

        ImageView qrCodeImageView = findViewById(R.id.qrCodeImageView);

        String serializeString = Constants.myProduct.getProductId();
        bitmapQR = QRCodeHelper
                .newInstance(this)
                .setContent(serializeString)
                .setErrorCorrectionLevel(ErrorCorrectionLevel.Q)
                .setMargin(2)
                .getQRCOde();
        qrCodeImageView.setImageBitmap(bitmapQR);


        printQRWrapper();
    }

    public void GoBack(View view) {
        onBackPressed();
    }

    public void onClickPrintButton(View view) {
        printQRWrapper();
    }

    private void printQRWrapper() {
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1 && !checkIfAlreadyHaveStoragePermission()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showSettingsDialog();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 104);
            }
        } else {
            printQR();
        }
    }

    private void printQR() {
        try {
            FileOutputStream outStream = null;
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/Meat Chain/QR-Codes");
            dir.mkdirs();
            String fileName = "QR-" + Constants.myProduct.getProductId() + ".jpg";
            File outFile = new File(dir, fileName);

            outStream = new FileOutputStream(outFile);
            bitmapQR.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();

            //Additionally, in order to refresh the gallery and to view the image there:
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(outFile));
            sendBroadcast(intent);

            showToast("QR-Code Successfully Saved");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //****************************************Permission RECORD_AUDIO Handler Start *********************************************************//
    private boolean checkIfAlreadyHaveStoragePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    //open settings in android phone for users so they can explicitly provide permissions.
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("\"Meet Chain\" needs to access storage");
        builder.setMessage("Allow Meet Chain to access your storage to save QR-Code");
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
                    printQR();
                } else {
                    //permission not granted.
                    Toast.makeText(this, "Storage access permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
