package com.niklaswormslev.projektseminar_2fa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QRScannerUtil {

    private static final String PREFS_NAME = "TOTP";
    private static final String SECRET_KEY = "SECRET_KEY";
    private static final String OCRA_CHALLENGE = "OCRA_CHALLENGE";

    public static void startQRScanner(Fragment fragment) {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(fragment);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Scan a QR code");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    public static void handleActivityResult(Fragment fragment, int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result == null || result.getContents() == null) {
            Toast.makeText(fragment.getContext(), "Scan was cancelled or invalid.", Toast.LENGTH_LONG).show();
            return;
        }

        String contents = result.getContents();
        String secretKey = extractSecretKey(contents);
        String ocraChallenge = extractOCRAChallenge(contents);

        if (secretKey != null) {
            storeValueInPrefs(fragment.getContext(), SECRET_KEY, secretKey);
            Toast.makeText(fragment.getContext(), "Saved Secret Key", Toast.LENGTH_LONG).show();
        } else if (ocraChallenge != null) {
            storeValueInPrefs(fragment.getContext(), OCRA_CHALLENGE, ocraChallenge);
            Toast.makeText(fragment.getContext(), "Saved OCRA Challenge", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(fragment.getContext(), "No valid data found in QR code.", Toast.LENGTH_LONG).show();
        }
    }

    private static void storeValueInPrefs(Context context, String key, String value) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(key, value).apply();
    }

    private static String extractSecretKey(String data) {
        Matcher matcher = Pattern.compile("secret=([^&]+)").matcher(data);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private static String extractOCRAChallenge(String data) {
        Matcher matcher = Pattern.compile("otpauth://ocra/([^+]+)\\+([^+]+)\\+([^+]+)").matcher(data);
        if (matcher.find()) {
            return matcher.group(2);
        }
        return null;
    }
}
