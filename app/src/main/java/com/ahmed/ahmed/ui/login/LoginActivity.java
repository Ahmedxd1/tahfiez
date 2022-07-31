package com.ahmed.ahmed.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ahmed.ahmed.NavMainActivity;
import com.ahmed.ahmed.ui.register.RegiseterActivity;
import com.ahmed.ahmed.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fireStore;
    FirebaseUser firebaseUser;
    String phonenumber;
    private PhoneAuthProvider.ForceResendingToken forceResending;
    String otpid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        phonenumber=binding.loginActivityPhoneNumberEt.getText().toString();
        binding.loginActivityCreateAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getBaseContext(), RegiseterActivity.class);
                startActivity(intent);

            }
        });

                binding.loginActivitySignInBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!binding.loginActivityPhoneNumberEt.getText().toString().trim().isEmpty() &&
                                !binding.loginActivityPasswordEt.getText().toString().isEmpty()) {
                            if ((binding.loginActivityPhoneNumberEt.getText().toString().trim()).length() == 10) {
                                    showProgress();
                                binding.loginActivitySignInBtn.setVisibility(View.INVISIBLE);
//                                initiateotp(phonenumber,LoginActivity.this);
                            } else {
                                Toast.makeText(LoginActivity.this, "Enter the Phone Number Correct", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Enter the Phone Number ", Toast.LENGTH_SHORT).show();

                        }

                        }

                });

                }

    public void showProgress() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.progressBar.setVisibility(View.GONE);
            }
        });

    }



//    private void initiateotp(String phoneNumber, Activity activity)
//    {
//        PhoneAuthOptions options =
//                PhoneAuthOptions.newBuilder(mAuth)
//                        .setPhoneNumber("+97" + phoneNumber.trim())       // Phone number to verify
//                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                        .setActivity(activity)
//                        .setForceResendingToken(forceResending)// Activity (for callback binding)
//                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                            @Override
//                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                                signInWithPhoneAuthCredential(phoneAuthCredential);
//                                hideProgress();
//                                binding.loginActivitySignInBtn.setVisibility(View.VISIBLE);
//                            }
//
//                            @Override
//                            public void onVerificationFailed(@NonNull FirebaseException e) {
//                                Log.d("TAG", "onVerificationFailed: " + e.getMessage());
//                                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
//                                hideProgress();
//                                binding.loginActivitySignInBtn.setVisibility(View.VISIBLE);
//                                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                                super.onCodeSent(s, forceResendingToken);
//                                forceResending = forceResendingToken;
//                                phonenumber=phoneNumber;
//                                otpid=s;
//
//
//                            }
//                        })          // OnVerificationStateChangedCallbacks
//                        .build();
//        PhoneAuthProvider.verifyPhoneNumber(options);     // OnVerificationStateChangedCallbacks
//
//    }






    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
//                            FirebaseUser user = task.getResult().getUser();
                            startActivity(new Intent(getApplicationContext(),NavMainActivity.class));
                            finish();

                        } else {
                            Toast.makeText(getApplicationContext(),"Signin Code Error",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private void setEtTextChangeListeners() {
        binding.loginActivityPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (binding.loginActivityPasswordEtLayout.isErrorEnabled()){
                    binding.loginActivityPasswordEtLayout.setError(null);
                }
            }
        });
        binding.loginActivityPhoneNumberEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (binding.loginActivityPhoneNumberEtLayout.isErrorEnabled()){
                    binding.loginActivityPhoneNumberEtLayout.setError(null);
                }
            }
        });

    }


    }



