package com.ahmed.ahmed.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ahmed.ahmed.NavMainActivity;
import com.ahmed.ahmed.R;
import com.ahmed.ahmed.databinding.ActivityVerfiyPhoneBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class verfiyPhoneActivity extends AppCompatActivity {
    ActivityVerfiyPhoneBinding binding;
    String phonenumber;
    String getBackEndOTP;
    String otpid;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken forceResending;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityVerfiyPhoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();


        phonenumber=getIntent().getStringExtra("phoneNo");
        getBackEndOTP=getIntent().getStringExtra("backendOTP");

        binding.verifyPhoneKeepGoingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.verifyPhoneCodeEt.getText().toString().isEmpty()) {
                    String enterCodeOTP=binding.verifyPhoneCodeEt.getText().toString();
                    initiateotp(phonenumber);

                    if (getBackEndOTP!=null){
                        showProgress();
                        binding.verifyPhoneKeepGoingBtn.setVisibility(View.INVISIBLE);

                        PhoneAuthCredential phoneAuthCredential =PhoneAuthProvider.getCredential(getBackEndOTP,enterCodeOTP);
                       signInWithPhoneAuthCredential(phoneAuthCredential);

                        String code = binding.verifyPhoneCodeEt.getText().toString();

                        if (code.isEmpty() || code.length() < 6) {
                            binding.verifyPhoneCodeEt.setError("Wrong OTP...");
                            binding.verifyPhoneCodeEt.requestFocus();
                            return;
                        }
                              showProgress();


                    }
                    else {
                        Toast.makeText(verfiyPhoneActivity.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();

                    }

                    Toast.makeText(verfiyPhoneActivity.this, "Verify code", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(verfiyPhoneActivity.this, "Enter the Phone Number Correct", Toast.LENGTH_SHORT).show();
                }
            }
        });
        NumberOPTmove();


        binding.verifyPhoneResendCodeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initiateotp(phonenumber);

            }
        });












    }

    private void verifyCode(String codeByUser) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(getBackEndOTP, codeByUser);
        signInWithPhoneAuthCredential(credential);

    }

    private void NumberOPTmove(){
        binding.verifyPhoneCodeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }




    private void initiateotp(String phoneNumber)
    {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+97" + phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)
                        .setForceResendingToken(forceResending)// Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Log.d("TAG", "onVerificationFailed: " + e.getMessage());
                                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                    // Invalid request
                                } else if (e instanceof FirebaseTooManyRequestsException) {
                                    // The SMS quota for the project has been exceeded
                                }
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                forceResending = forceResendingToken;
                                phonenumber=phoneNumber;
                                otpid=s;



                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);     // OnVerificationStateChangedCallbacks

    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            hideProgress();
                            binding.verifyPhoneKeepGoingBtn.setVisibility(View.VISIBLE);
                            Intent intent=new Intent(getApplicationContext(),NavMainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(getApplicationContext(),"Signin Code Error",Toast.LENGTH_LONG).show();
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

}