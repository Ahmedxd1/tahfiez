package com.ahmed.ahmed;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ahmed.ahmed.databinding.ActivityRegiseterBinding;

public class RegiseterActivity extends AppCompatActivity {
    ActivityRegiseterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegiseterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.registerActivityLoginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

            }
        });
    }
}