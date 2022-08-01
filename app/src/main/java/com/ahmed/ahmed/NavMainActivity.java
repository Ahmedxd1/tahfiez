package com.ahmed.ahmed;

import static com.ahmed.ahmed.ui.login.LoginActivity.CURRENT_USER;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.ahmed.ahmed.constants.Constants;
import com.ahmed.ahmed.databinding.ActivityNavMainBinding;
import com.ahmed.ahmed.ui.Settings.SettingsFragment;
import com.ahmed.ahmed.ui.home.HomeFragment;
import com.ahmed.ahmed.ui.login.LoginActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class NavMainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavMainBinding binding;
    public boolean isBackStack=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNavMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarNavMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_centers, R.id.nav_alldata,R.id.nav_messages,R.id.nav_settings)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_nav_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_main, menu);
//        Intent intent=new Intent(getApplicationContext(), SettingsFragment.class);
//        startActivity(intent);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                logoutCurrentUser();
                Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return(true);
        }

        return(super.onOptionsItemSelected(item));
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_nav_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

//    private void replaceFragment (Fragment fragment){
//        String backStateName = fragment.getClass().getName();
//
//        FragmentManager manager = getSupportFragmentManager();
//
//        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);
//        if (!fragmentPopped){ //fragment not in back stack, create it.
//            FragmentTransaction ft = manager.beginTransaction();
//            ft.replace(R.id.homeFragment, fragment);
//            ft.addToBackStack(backStateName);
//            ft.commit();
//        }
//    }
public void logoutCurrentUser() {
    SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(CURRENT_USER, null);
    editor.commit();
    editor.clear();
}
}