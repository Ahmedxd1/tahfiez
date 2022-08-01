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
import com.ahmed.ahmed.ui.register.RegiseterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class verfiyPhoneActivity extends AppCompatActivity {
    ActivityVerfiyPhoneBinding binding;
    String phonenumber,nameIntent,passwordIntent,emailIntent;
    private static String vrf_id;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken forceResending;
    int activityState=1;
    public static String VERIFY_ACTIVITY="VERIFY_ACTIVITY";
    private FirebaseFirestore fireStore;
    private static String uuid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityVerfiyPhoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();

        Intent intent=getIntent();

        if (intent!=null){
            phonenumber=intent.getStringExtra("phoneNo");
            nameIntent=intent.getStringExtra("name");
            emailIntent=intent.getStringExtra("email");
            passwordIntent=intent.getStringExtra("password");
            Log.d("TAG", "onCreate: "+phonenumber);
            Log.d("TAG", "onCreate: "+nameIntent);
            Log.d("TAG", "onCreate: "+emailIntent);
            Log.d("TAG", "onCreate: "+passwordIntent);
            activityState=intent.getIntExtra(VERIFY_ACTIVITY,1);
        }

        sendPhoneNumberVerification(phonenumber);

        binding.verifyPhoneKeepGoingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.verifyPhoneCodeEt.getText().toString().isEmpty()) {
                    String enterCodeOTP = binding.verifyPhoneCodeEt.getText().toString();
                    if (vrf_id != null) {
                        showProgress();
                        setVerifyBtnEnable(false);

                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(vrf_id, enterCodeOTP);
                        signInWithPhoneAuthCredential(phoneAuthCredential);

                        String code = binding.verifyPhoneCodeEt.getText().toString();

                        if (code.isEmpty() || code.length() < 6) {
                            binding.verifyPhoneCodeEt.setError("Wrong OTP...");
                            binding.verifyPhoneCodeEt.requestFocus();
                            showProgress();
                            return;

                        } else {
                            Toast.makeText(verfiyPhoneActivity.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();

                        }
                    }

//                    mAuth.createUserWithEmailAndPassword(emailIntent,passwordIntent).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()){
//                    uuid = mAuth.getCurrentUser().getUid();
//
//                    DocumentReference documentReference = fireStore.collection("users").document(uuid);
//                    Map<String, Object> user = new HashMap<>();
//                    user.put("fullName", nameIntent);
//                    user.put("phoneNumber", phonenumber);
//                    user.put("email", emailIntent);
//                    user.put("password", passwordIntent);
//                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void unused) {
//                            Log.d("TAG", "onSuccess: create user ");
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.d("TAG", "onFailure: Failed to Create User " + e.getMessage());
//                        }
//                    });
//
//                    Toast.makeText(verfiyPhoneActivity.this, "User Registered", Toast.LENGTH_SHORT).show();

//                            }
//                        }
//                    });

                } else {
                    Toast.makeText(verfiyPhoneActivity.this, "Enter the code or verify Phone Number", Toast.LENGTH_SHORT).show();
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

    public void setVerifyBtnEnable(boolean enable){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.verifyPhoneKeepGoingBtn.setEnabled(enable);
            }
        });
    }


    private void NumberOPTmove(){
        binding.verifyPhoneCodeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 6){
                    setVerifyBtnEnable(true);
                }


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
                                vrf_id=s;



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
                            uuid = mAuth.getCurrentUser().getUid();

                            DocumentReference documentReference = fireStore.collection("users").document(uuid);
                            Map<String, Object> user = new HashMap<>();
                            user.put("fullName", nameIntent);
                            user.put("phoneNumber", phonenumber);
                            user.put("email", emailIntent);
                            user.put("password", passwordIntent);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("TAG", "onSuccess: create user ");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG", "onFailure: Failed to Create User " + e.getMessage());
                                }
                            });

                            Toast.makeText(verfiyPhoneActivity.this, "User Registered", Toast.LENGTH_SHORT).show();

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


    public void sendPhoneNumberVerification(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+97" + phoneNumber.trim())       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                          @Override
                                          public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                          }

                                          @Override
                                          public void onVerificationFailed(@NonNull FirebaseException e) {
                                              Log.d("TAG", "onVerificationFailed: " + e.getMessage());
                                          }

                                          @Override
                                          public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                              super.onCodeSent(s, forceResendingToken);
                                              forceResending = forceResendingToken;
                                              vrf_id=s;
                                              phonenumber=phoneNumber;

                                          }

                                      }
                        )          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }




}