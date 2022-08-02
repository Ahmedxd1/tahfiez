package com.ahmed.ahmed.ui.register;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ahmed.ahmed.R;
import com.ahmed.ahmed.databinding.ActivityRegiseterBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class RegiseterActivity extends AppCompatActivity {
    ActivityRegiseterBinding binding;
    private FirebaseAuth mAuth;
    private  FirebaseFirestore fireStore;
    private FirebaseStorage storage;
    StorageReference storageReference;
    private static String uuid;
    Boolean verificationOnProgress = false;
    String profileImagePath;
//    private Uri filePath;
        String photoPath ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegiseterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fireStore = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
         storageReference = storage.getReference();



        ActivityResultLauncher<String> arl = registerForActivityResult(
                new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null){
                            binding.profileImage.setImageURI(result);
                            profileImagePath = getPhotoPath(result);
                        }
                    }
                });

        ActivityResultLauncher<String[]> launcher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
                    @Override
                    public void onActivityResult(Map<String, Boolean> result) {
                        if (Boolean.TRUE.equals(result.get(Manifest.permission.READ_EXTERNAL_STORAGE)) &&
                                Boolean.TRUE.equals(result.get(Manifest.permission.WRITE_EXTERNAL_STORAGE))){
                            arl.launch("image/*");
                        }
                    }
                }
        );


        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.launch(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE ,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE});
            }
        });
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
                String filePath = photoPath;


                if (isPhoneNumberAndPasswordValid(name,phone, password,email)) {
                    setSaveBtnEnable(false);
                    showProgress();

                }

                    Intent intent=new Intent(RegiseterActivity.this, verfiyPhoneActivity.class);
                    intent.putExtra("phoneNo",phone);
                    intent.putExtra("name",name);
                    intent.putExtra("password",password);
                    intent.putExtra("email",email);
                    intent.putExtra("image", filePath);

                    startActivity(intent);

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
        ArrayList<Boolean> isValid = new ArrayList<>();


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
        if (TextUtils.isEmpty(fullName)) {
            binding.registerActivityFullNameEtLayout.setError(getString(R.string.required));
            isValid.add(false);
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            binding.registerActivityPhoneNumberEtLayout.setError(getString(R.string.required));
            isValid.add(false);

        }
        if (TextUtils.isEmpty(password)) {
            binding.registerActivityPasswordEtLayout.setError(getString(R.string.required));
            isValid.add(false);

        }
        if (TextUtils.isEmpty(email)) {
            binding.registerActivityEmailEtLayout.setError(getString(R.string.required));
            isValid.add(false);

        }

        return !isValid.contains(false);
    }
        private String getPhotoPath(Uri imageUri){


            Bitmap bitmap = null;
            String android_id = Settings.Secure.getString(getBaseContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getBaseContext().getContentResolver(), imageUri);
                File dir = getBaseContext().getExternalFilesDir(Environment.DIRECTORY_DCIM);
                File myFile = new File(dir.getPath()+"/Tahfiez" + android_id +".png");

                FileOutputStream fileOutputStream = new FileOutputStream(myFile);

                bitmap.compress(Bitmap.CompressFormat.PNG,
                        100, fileOutputStream);

                fileOutputStream.flush();
                fileOutputStream.close();

                photoPath = myFile.getPath();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return photoPath;

        }



}






