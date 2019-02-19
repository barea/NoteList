package com.example.barea.notelist.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtils {

    private static SharedPreferences getSharedPrefernce(Context context){
        return context.getSharedPreferences("APP_PREF", Context.MODE_PRIVATE);
    }

    public static void storeApiKey(Context context, String apiKey){
        SharedPreferences.Editor editor = getSharedPrefernce(context).edit();
        editor.putString("API_KEY", apiKey );
        editor.commit();
    }
    public static String getApiKey(Context context){
        return getSharedPrefernce(context).getString("API_KEY", null);
    }

}
