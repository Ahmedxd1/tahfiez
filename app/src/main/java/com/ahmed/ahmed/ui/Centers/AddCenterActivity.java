package com.ahmed.ahmed.ui.Centers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ahmed.ahmed.R;
import com.ahmed.ahmed.databinding.ActivityAddCenterBinding;


public class AddCenterActivity extends AppCompatActivity {
    ActivityAddCenterBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCenterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.centerToolbar);
        binding.collapsingToolbarLayout.setTitle(getString(R.string.add_center));


    }
}