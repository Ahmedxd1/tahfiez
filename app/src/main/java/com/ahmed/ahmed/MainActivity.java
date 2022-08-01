package com.ahmed.ahmed;

import static com.ahmed.ahmed.ui.login.LoginActivity.CURRENT_USER;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ahmed.ahmed.constants.Constants;
import com.ahmed.ahmed.databinding.ActivityMainBinding;
import com.ahmed.ahmed.model.User;
import com.ahmed.ahmed.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
        ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fireStore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
             setContentView(binding.getRoot());
        fireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        User currentUser=getCurrentUser();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAuth.getCurrentUser()!=null ){

                    Intent intent=new Intent(getApplicationContext(),NavMainActivity.class);
                    startActivity(intent);
                    finish();


                }else{
                    Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
//                if (mAuth.getCurrentUser()==null){
//                    Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
//                    startActivity(intent);
//                    finish();
//                }


            }
        },5000);




    }

    public void logoutCurrentUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CURRENT_USER, null);
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
                Toast.makeText(MainActivity.this, "Profile Do Not Exists", Toast.LENGTH_SHORT).show();
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