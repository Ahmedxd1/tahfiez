package com.ahmed.ahmed.constants;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

public class CommonUtils {

    public static void showSnackbar(View view,String message){
        Snackbar.make(view,message,Snackbar.LENGTH_LONG).show();
    }
    public static void setLocale(String lang, Context context) {
        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        Resources res =context.getResources();
        Configuration conf = res.getConfiguration();
        conf.setLocale(myLocale);
        res.updateConfiguration(conf,res.getDisplayMetrics());
    }


}
