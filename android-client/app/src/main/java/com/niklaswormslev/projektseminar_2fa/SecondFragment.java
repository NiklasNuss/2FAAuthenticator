package com.niklaswormslev.projektseminar_2fa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

public class SecondFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextInputEditText textInputOTPList = view.findViewById(R.id.textInputOTPList);
        Button saveButton = view.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = textInputOTPList.getText().toString();

                SharedPreferences prefs = getContext().getSharedPreferences("TOTP", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("OTP_LIST", inputText);
                editor.apply();

                Toast.makeText(getContext(), "OTP List saved", Toast.LENGTH_SHORT).show();
            }
        });
    }
}