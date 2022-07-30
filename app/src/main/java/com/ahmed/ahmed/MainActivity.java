package com.ahmed.ahmed;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ahmed.ahmed.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
        ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
             setContentView(binding.getRoot());




    }
}