package com.niklaswormslev.projektseminar_2fa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.annotation.Nullable;

import com.niklaswormslev.projektseminar_2fa.databinding.FragmentFirstBinding;
import com.niklaswormslev.projektseminar_2fa.QRScannerUtil;
import com.niklaswormslev.projektseminar_2fa.TwoFA;


import com.leinardi.android.speeddial.SpeedDialView;
import com.leinardi.android.speeddial.SpeedDialActionItem;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private Handler handler;
    private Runnable updateTOTPRunnable;

    private void updateSecret() {

    SharedPreferences prefs = getContext().getSharedPreferences("TOTP", Context.MODE_PRIVATE);
    String secretKey = prefs.getString("SECRET_KEY", "No Secret Key Found");

    TextView secretKeyTextView0 = getView().findViewById(R.id.textview_secret);
    secretKeyTextView0.setText(secretKey);
}

 private void updateTOTP() {

    SharedPreferences prefs = getContext().getSharedPreferences("TOTP", Context.MODE_PRIVATE);
    String secretKey = prefs.getString("SECRET_KEY", null);

    TextView secretKeyTextView = getView().findViewById(R.id.textview_totp_code);

    if (secretKey == null || secretKey.isEmpty()) {
        secretKeyTextView.setText("Setup needed");
    } else {

        String totp = TwoFA.getTOTP(secretKey);

        secretKeyTextView.setText(totp);
    }
}


private void updateHOTP() {

    SharedPreferences prefs = getContext().getSharedPreferences("TOTP", Context.MODE_PRIVATE);
    String secretKey = prefs.getString("SECRET_KEY", "No Secret Key Found");

    long counter = prefs.getLong("COUNTER", 1);

    String hotp = TwoFA.generateHOTP(secretKey, counter);

    SharedPreferences.Editor editor = prefs.edit();
    editor.putLong("COUNTER", counter + 1);
    editor.apply();

    TextView secretKeyTextView2 = getView().findViewById(R.id.textview_hotp_code);
    secretKeyTextView2.setText(hotp);

    TextView counterTextView = getView().findViewById(R.id.textview_hotp_counter);
    counterTextView.setText(String.valueOf(counter));
}

private void updateOTPList() {

    SharedPreferences prefs = getContext().getSharedPreferences("TOTP", Context.MODE_PRIVATE);
    String otpList = prefs.getString("OTP_LIST", "No OTP List Found");

    TextView otpListTextView = getView().findViewById(R.id.textview_otplist_code);
    otpListTextView.setText(otpList);
}

private void updateOCRA() {

    SharedPreferences prefs = getContext().getSharedPreferences("TOTP", Context.MODE_PRIVATE);
    String secretKey = prefs.getString("SECRET_KEY", "No Secret Key Found");

    String ocraChallenge = prefs.getString("OCRA_CHALLENGE", "No OCRA Challenge Found");

    String ocra = TwoFA.generateOCRA(secretKey, ocraChallenge);

    TextView secretKeyTextView3 = getView().findViewById(R.id.textview_ocra_code);
    secretKeyTextView3.setText(ocraChallenge + " -> " + ocra);
}

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonOcra.setOnClickListener(v ->
            QRScannerUtil.startQRScanner(FirstFragment.this)
        );

        binding.buttonHotp.setOnClickListener(v -> {
            updateHOTP();
        });

                // Initialize Speed Dial
    SpeedDialView speedDialView = binding.getRoot().findViewById(R.id.speedDial);
    speedDialView.addActionItem(
        new SpeedDialActionItem.Builder(R.id.fab_action1, R.drawable.baseline_qr_code_scanner_24)
            .setLabel("TOTP, HOTP & OCRA")
            .create()
    );
    speedDialView.addActionItem(
        new SpeedDialActionItem.Builder(R.id.fab_action2, R.drawable.baseline_keyboard_24)
            .setLabel("OTP List")
            .create()
    );
    speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
        @Override
        public boolean onActionSelected(SpeedDialActionItem actionItem) {
            if (actionItem.getId() == R.id.fab_action1) {
                
                QRScannerUtil.startQRScanner(FirstFragment.this);
            }
            if (actionItem.getId() == R.id.fab_action2) {

                NavHostFragment.findNavController(FirstFragment.this)
                    .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
            return false;
        }
    });

    updateTOTP();
    updateOTPList();
    updateSecret();


        handler = new Handler(Looper.getMainLooper());
        updateTOTPRunnable = new Runnable() {
            @Override
            public void run() {
                updateTOTP();
                updateSecret();
                handler.postDelayed(this, 30000); // 30 seconds
            }
        };
        handler.post(updateTOTPRunnable);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        QRScannerUtil.handleActivityResult(this, requestCode, resultCode, data);

        updateTOTP();
        updateOCRA();
        updateSecret();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (handler != null && updateTOTPRunnable != null) {
            handler.removeCallbacks(updateTOTPRunnable);
        }
    }

}