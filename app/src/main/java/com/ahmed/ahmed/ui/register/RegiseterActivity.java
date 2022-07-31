package com.ahmed.ahmed.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.ahmed.ahmed.MainActivity;
import com.ahmed.ahmed.NavMainActivity;
import com.ahmed.ahmed.R;
import com.ahmed.ahmed.constants.Constants;
import com.ahmed.ahmed.databinding.ActivityRegiseterBinding;
import com.ahmed.ahmed.model.User;
import com.ahmed.ahmed.ui.login.LoginActivity;
import com.ahmed.ahmed.ui.login.verfiyPhoneActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RegiseterActivity extends AppCompatActivity {
    ActivityRegiseterBinding binding;
    private FirebaseAuth mAuth;
    private  FirebaseFirestore fireStore;
    private String userPhoneNumber;
    private static String uuid;
    String verificationId;
    PhoneAuthCredential credential;
    Boolean verificationOnProgress = false;
    PhoneAuthProvider.ForceResendingToken token;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegiseterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        binding.registerActivityLoginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

            }
        });
        binding.registerActivitySignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!verificationOnProgress){
                String name = binding.registerActivityFullNameEt.getText().toString();
                String phone = binding.registerActivityPhoneNumberEt.getText().toString();
                String password = binding.registerActivityPasswordEt.getText().toString();
                String email = binding.registerActivityEmailEt.getText().toString();


                if (isPhoneNumberAndPasswordValid(name,phone, password,email)) {
                    setSaveBtnEnable(false);
                    showProgress();

                }

                    Intent intent=new Intent(RegiseterActivity.this, verfiyPhoneActivity.class);
                    intent.putExtra("phoneNo",phone);
                    startActivity(intent);
                    finish();
//
//                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()){
//                            uuid=mAuth.getCurrentUser().getUid();
//
//                            DocumentReference documentReference= fireStore.collection("users").document(uuid);
//                            Map<String,Object> user =new HashMap<>();
//                            user.put("FullName",name);
//                            user.put("Phone",phone);
//                            user.put("Email",email);
//                            user.put("Password",password);
//                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void unused) {
//                                    Log.d("TAG", "onSuccess: create user ");
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Log.d("TAG", "onFailure: Failed to Create User " + e.getMessage());
//                                }
//                            });
//
//                            Toast.makeText(RegiseterActivity.this, "User Registered", Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//                });



            }}
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


    public void setSaveBtnEnable(boolean enable){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.registerActivitySignupBtn.setEnabled(enable);

            }
        });
    }



    public boolean isPhoneNumberAndPasswordValid(String fullName,String phoneNumber, String password, String email) {
        ArrayList<Boolean> isValid=new ArrayList<>();


        if (!phoneNumber.startsWith("059")) {
            binding.registerActivityPhoneNumberEtLayout.setError(getString(R.string.msg_phone_number_start_with));
            isValid.add(false);

        }

        if ((password.length() < 6)) {

            binding.registerActivityPasswordEtLayout.setError(getString(R.string.msg_week_passwrod));
            isValid.add(false);
        }
        if (!email.equals(email)) {
            binding.registerActivityEmailEtLayout.setError(getString(R.string.msg_write_cnf_correctly));
            isValid.add(false);
        }
        if(TextUtils.isEmpty(fullName)){
            binding.registerActivityFullNameEtLayout.setError(getString(R.string.required));
            isValid.add(false);
        }

        if (TextUtils.isEmpty(phoneNumber) ) {
            binding.registerActivityPhoneNumberEtLayout.setError(getString(R.string.required));
            isValid.add(false);

        }
        if (TextUtils.isEmpty(password)){
            binding.registerActivityPasswordEtLayout.setError(getString(R.string.required));
            isValid.add(false);

        }
        if (TextUtils.isEmpty(email)){
            binding.registerActivityEmailEtLayout.setError(getString(R.string.required));
            isValid.add(false);

        }

        return !isValid.contains(false);


    }


//       in splash screen
    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
          showProgress();
          checkUserProfile();
        }
    }
//       in splash screen
    private void checkUserProfile() {
        DocumentReference docRef = fireStore.collection("users").document(mAuth.getCurrentUser().getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    startActivity(new Intent(getApplicationContext(),NavMainActivity.class));
                    finish();
                }else {
                    //Toast.makeText(Register.this, "Profile Do not Exists.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegiseterActivity.this, "Profile Do Not Exists", Toast.LENGTH_SHORT).show();
            }
        });
    }


}