package com.ahmed.ahmed.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ahmed.ahmed.NavMainActivity;
import com.ahmed.ahmed.R;
import com.ahmed.ahmed.constants.Constants;
import com.ahmed.ahmed.model.User;
import com.ahmed.ahmed.ui.register.RegiseterActivity;
import com.ahmed.ahmed.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fireStore;
    String phonenumber;
    private PhoneAuthProvider.ForceResendingToken forceResending;
    private static String uuid;
    public static String USERS_COLLECTION="users";
    public static String CURRENT_USER="currentUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        setEtTextChangeListeners();
        binding.loginActivityCreateAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), RegiseterActivity.class);
                startActivity(intent);

            }
        });

        binding.loginActivitySignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        phonenumber = binding.loginActivityPhoneNumberEt.getText().toString();
                        String password = binding.loginActivityPasswordEt.getText().toString();
                        if (TextUtils.isEmpty(phonenumber)) {
                            binding.loginActivityPhoneNumberEtLayout.setError(getString(R.string.enter_phone_number));
                            return;
                        }
                        if (TextUtils.isEmpty(password)) {
                            binding.loginActivityPhoneNumberEtLayout.setError(getString(R.string.enter_password));
//                            Intent intent =new Intent(getApplicationContext(),NavMainActivity.class);
//                            startActivity(intent);
                            return;
                        }
                        showProgress();
                        enableLoginButton(false);
                signInWithPhoneNumberAndPassword(phonenumber, password);



            }

        });

    }

    public void enableLoginButton(boolean enable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.loginActivitySignInBtn.setEnabled(enable);

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


    public void signInWithPhoneNumberAndPassword(String phoneNumber, String password) {

        fireStore.collection(USERS_COLLECTION).whereEqualTo("phoneNumber", phoneNumber).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    User user = null;
                    try {
                        user = task.getResult().getDocuments().get(0).toObject(User.class);

                    } catch (IndexOutOfBoundsException exception) {
                        Log.e("TAG", "onComplete:" + exception.getMessage());
                    }
                    if (user != null) {
                        uuid = user.getId();
                        phonenumber = user.getPhoneNumber();
                        if (password.equals(user.getPassword())) {
                            setCurrentUser(user);
                            Intent intent =new Intent(getApplicationContext(),NavMainActivity.class);
                            startActivity(intent);


                        } else {
                            Toast.makeText(LoginActivity.this, "كلمة المرور غير صحيحة", Toast.LENGTH_SHORT).show();
                            hideProgress();
                            enableLoginButton(true);

                        }


                    } else {
                        Toast.makeText(LoginActivity.this, "المستخدم غير موجود", Toast.LENGTH_SHORT).show();
                        hideProgress();
                        enableLoginButton(true);

                    }
                } else {
                    Toast.makeText(LoginActivity.this, " فشلت عملية تسجيل الدخول", Toast.LENGTH_SHORT).show();
                    hideProgress();
                    enableLoginButton(true);

                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG", "auth onFailure " + e.getMessage());
                hideProgress();

            }
        });

    }

    //    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful())
//                        {
////                            FirebaseUser user = task.getResult().getUser();
//                            startActivity(new Intent(getApplicationContext(),NavMainActivity.class));
//                            finish();
//
//                        } else {
//                            Toast.makeText(getApplicationContext(),"Signin Code Error",Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//    }
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
                if (binding.loginActivityPasswordEtLayout.isErrorEnabled()) {
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
                if (binding.loginActivityPhoneNumberEtLayout.isErrorEnabled()) {
                    binding.loginActivityPhoneNumberEtLayout.setError(null);
                }
            }
        });

    }


    public void setCurrentUser(User user) {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String string = gson.toJson(user);
        editor.putString(CURRENT_USER, string);
        editor.commit();
    }

    public void logoutCurrentUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.CURRENT_USER, null);
        editor.commit();
        editor.clear();
    }

    public User getCurrentUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        String user = sharedPreferences.getString(Constants.CURRENT_USER, null);
        return new Gson().fromJson(user, User.class);
    }
    private void checkUserProfile() {
        DocumentReference docRef = fireStore.collection("users").document(mAuth.getCurrentUser().getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    startActivity(new Intent(getApplicationContext(), NavMainActivity.class));
                    finish();
                    hideProgress();
                } else {
                    //Toast.makeText(Register.this, "Profile Do not Exists.", Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Profile Do Not Exists", Toast.LENGTH_SHORT).show();
            }
        });
    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if(mAuth.getCurrentUser() != null){
//            showProgress();
//            checkUserProfile();
//            hideProgress();
//        }
//    }




}



