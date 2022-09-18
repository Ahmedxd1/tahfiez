package com.ahmed.ahmed.ui.Centers;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ahmed.ahmed.NavMainActivity;
import com.ahmed.ahmed.R;
import com.ahmed.ahmed.adapters.NameSctionSpinnerAdapter;
import com.ahmed.ahmed.adapters.UserNameSpinnerAdapter;
import com.ahmed.ahmed.databinding.ActivityAddCenterBinding;
import com.ahmed.ahmed.model.Section;
import com.ahmed.ahmed.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddCenterActivity extends AppCompatActivity {
    ActivityAddCenterBinding binding;
    private FirebaseFirestore fireStore;
    private FirebaseStorage storage;
    StorageReference storageReference;
    private String uuid;
    private FirebaseAuth mAuth;
    NameSctionSpinnerAdapter sectionsSpinnerAdapter;
    UserNameSpinnerAdapter userNameSpinnerAdapter;
    String photoUrl;
    private List<Section> sections=new ArrayList<>();
    private List<User> users=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCenterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fireStore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();

        storageReference = storage.getReference();
        setSupportActionBar(binding.centerToolbar);
        binding.collapsingToolbarLayout.setTitle(getString(R.string.add_center));

        showData();
        showManger();






        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (photoUrl != null) {
                    uploadImage();
                    Log.d("ttt", "onComplete: Uploaded Image");

                } else {
                    String nameCenter = binding.addCenterActivityNameEt.getText().toString();
                    int section = (int) binding.addCenterActivitySection.getSelectedItemId();
                    String address = binding.addCenterActivityCenterAddressEt.getText().toString();
                    String numberCycles = binding.addCenterActivityMixcyclesEt.getText().toString();
                    int userManger = (int) binding.centerManger.getSelectedItemId();

                    DocumentReference documentReference = fireStore.collection("centers").document();
                    Map<String, Object> center = new HashMap<>();
                    center.put("NameCenter", nameCenter);
                    center.put("Section", section);
                    center.put("Address", address);
                    center.put("NumberCycles", numberCycles);
                    center.put("userManger", userManger);
                    documentReference.set(center).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("ttt", "onComplete: " + task.isSuccessful());
                                Intent intent = new Intent(getApplicationContext(), NavMainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("ttt", "onFailure: " + e.getMessage());
                        }
                    });

                    Log.d("ttt", "onComplete: errror");
                }


            }


        });


        ActivityResultLauncher<String> arl = registerForActivityResult(
                new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            binding.centerImage.setImageURI(result);
                            photoUrl = getPhotoPath(result);
                        }
                    }
                });

        ActivityResultLauncher<String[]> launcher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
                    @Override
                    public void onActivityResult(Map<String, Boolean> result) {
                        if (Boolean.TRUE.equals(result.get(Manifest.permission.READ_EXTERNAL_STORAGE)) &&
                                Boolean.TRUE.equals(result.get(Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
                            arl.launch("image/*");
                        }
                    }
                }
        );


        binding.centerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.launch(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE});
            }
        });


    }

    private void uploadImage() {
        Log.d("ttt", "uploadImage: ");

        Uri uri = Uri.fromFile(new File(photoUrl));

        Log.d("ttt", "uri " + (uri == null));
        if (uri != null) {
            ProgressDialog progressDialog = new ProgressDialog(AddCenterActivity.this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("centers").child(uri.getLastPathSegment());
            storageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    Log.d("ttt", "upload image success");
                    if (task.isSuccessful()) {
                        Log.d("ttt", "upload image success 22");
                        photoUrl = task.getResult().getStorage().toString();
                        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                photoUrl = task.getResult().toString();

                                if (task.isSuccessful()) {
                                    String nameCenter = binding.addCenterActivityNameEt.getText().toString();
                                    int section = (int) binding.addCenterActivitySection.getSelectedItemId();
                                    String address = binding.addCenterActivityCenterAddressEt.getText().toString();
                                    String numberCycles = binding.addCenterActivityMixcyclesEt.getText().toString();
                                    int userManger = (int) binding.centerManger.getSelectedItemId();
                                    uuid = mAuth.getCurrentUser().getUid();

                                    DocumentReference documentReference = fireStore.collection("centers").document(uuid);
                                    Map<String, Object> center = new HashMap<>();
                                    center.put("NameCenter", nameCenter);
                                    center.put("Section", section);
                                    center.put("Address", address);
                                    center.put("NumberCycles", numberCycles);
                                    center.put("userManger", userManger);
                                    center.put("ImageCenter", photoUrl);
                                    documentReference.set(center).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Intent intent = new Intent(getApplicationContext(), NavMainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("ttt", "onFailure: ");
                                        }
                                    });

                                    Log.d("TAG", "onComplete: " + task.getResult().toString());
                                }


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("ttt", "onFailure:get download url " + e.getMessage());

                            }
                        });
                        progressDialog.dismiss();
                        Toast.makeText(AddCenterActivity.this, "تم رفع الصورة  ", Toast.LENGTH_SHORT).show();
                        Log.d("AHM", "onComplete: Uploaded Image");

                    }

                }
            });
        }
    }

    private String getPhotoPath(Uri imageUri) {

        String photoPath = null;
        Bitmap bitmap = null;
        String android_id = Settings.Secure.getString(getBaseContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getBaseContext().getContentResolver(), imageUri);
            File dir = getBaseContext().getExternalFilesDir(Environment.DIRECTORY_DCIM);
            File myFile = new File(dir.getPath() + "/Centers" + android_id + ".png");

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



    public void showData(){

        FirebaseFirestore.getInstance().collection("Section").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){
                    Section section=  queryDocumentSnapshot.toObject(Section.class);
                    sections.add(section);
                    sectionsSpinnerAdapter=new NameSctionSpinnerAdapter(sections);
                    binding.addCenterActivitySection.setAdapter(sectionsSpinnerAdapter);


                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("ttt", "onComplete:Failure "+sections);

            }
        });



    }


    public void showManger(){
        FirebaseFirestore.getInstance().collection("users").whereEqualTo("userType","0"). get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){
                    User user =  queryDocumentSnapshot.toObject(User.class);
                    users.add(user);
                    Log.e("ttt", "onComplete: "+user);
                    Log.e("ttt", "onComplete: "+users);
                    userNameSpinnerAdapter=new UserNameSpinnerAdapter(users);
                    binding.centerManger.setAdapter(userNameSpinnerAdapter);


                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("ttt", "onComplete:Failure "+users);

            }
        });



    }

}