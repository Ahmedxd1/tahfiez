package com.ahmed.ahmed.ui.register;

import static com.google.common.io.Files.getFileExtension;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.ahmed.ahmed.MainActivity;
import com.ahmed.ahmed.NavMainActivity;
import com.ahmed.ahmed.databinding.ActivityVerfiyPhoneBinding;
import com.ahmed.ahmed.model.User;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class verfiyPhoneActivity extends AppCompatActivity {
    ActivityVerfiyPhoneBinding binding;
    String phonenumber;
    String nameIntent;
    String passwordIntent;
    String emailIntent;
    String imageIntent;
    String userTypeIntent;
    private static String vrf_id;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken forceResending;
    int activityState=1;
    public static String VERIFY_ACTIVITY="VERIFY_ACTIVITY";
    private FirebaseFirestore fireStore;
    private static String uuid;
    private FirebaseStorage storage;
    StorageReference storageReference;
    String  photoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityVerfiyPhoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        Intent intent=getIntent();
        if (intent!=null){
            phonenumber=intent.getStringExtra("phoneNo");
            nameIntent=intent.getStringExtra("name");
            emailIntent=intent.getStringExtra("email");
            passwordIntent=intent.getStringExtra("password");
            imageIntent=intent.getStringExtra("image");
            userTypeIntent=intent.getStringExtra("userType");
            Log.d("TAG", "onCreate: "+phonenumber);
            Log.d("TAG", "onCreate: "+nameIntent);
            Log.d("TAG", "onCreate: "+emailIntent);
            Log.d("TAG", "onCreate: "+passwordIntent);
            Log.d("TAG", "onCreate: "+imageIntent);
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
                } else {
                    Toast.makeText(verfiyPhoneActivity.this, "Enter the code or verify Phone Number", Toast.LENGTH_SHORT).show();
                }
            }
        });
        NumberOPTmove();

        binding.verifyPhoneResendCodeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPhoneNumberVerification(phonenumber);

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

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            if (imageIntent!=null) {
                                uploadImage();
                                Log.d("AHM", "onComplete: Uploaded Image");

                            }
                            hideProgress();
                            binding.verifyPhoneKeepGoingBtn.setVisibility(View.VISIBLE);

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
    private  String getFileExtension(Uri imageUri){
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().getType(Uri.parse(imageIntent)));
    }

    private void uploadImage() {
        Log.d("ttt", "uploadImage: ");
        Intent intent=getIntent();
        if (intent!=null){
            imageIntent=intent.getStringExtra("image");
            Log.d("TAG", "onCreate: "+imageIntent);
        }

        Uri uri=Uri.fromFile(new File(imageIntent));

        Log.d("ttt", "uri "+ (uri==null));
        if (uri!=null){
            ProgressDialog progressDialog = new ProgressDialog(verfiyPhoneActivity.this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("images").child(uri.getLastPathSegment());
            storageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    //code الرفع بشتغلش
                    Log.d("ttt", "upload image success");
                    if (task.isSuccessful()){
                        Log.d("ttt", "upload image success 22");
                        photoUrl=task.getResult().getStorage().toString();
                        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                photoUrl=task.getResult().toString();

                                if (task.isSuccessful()){

                                    uuid = mAuth.getCurrentUser().getUid();

                                    DocumentReference documentReference = fireStore.collection("users").document(uuid);
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("fullName", nameIntent);
                                    user.put("phoneNumber", phonenumber);
                                    user.put("email", emailIntent);
                                    user.put("password", passwordIntent);
                                    user.put("image", task.getResult().toString());
                                    user.put("userType",userTypeIntent);
                                    documentReference.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Intent intent = new Intent(getApplicationContext(), NavMainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("ttt", "onFailure: " );
                                        }
                                    });

                                    Log.d("TAG", "onComplete: "+task.getResult().toString());
                                }



                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("ttt", "onFailure:get download url "+e.getMessage() );

                            }
                        });
                        progressDialog.dismiss();
                        Toast.makeText(verfiyPhoneActivity.this, "تم رفع الصورة  ", Toast.LENGTH_SHORT).show();
                        Log.d("AHM", "onComplete: Uploaded Image");

                    }

                }
            });}
        }




}