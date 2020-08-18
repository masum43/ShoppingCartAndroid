package com.maces.ecommerce.skcashandcarry;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;

public class MySharedPref {


   // Storing data into SharedPreferences
public static void putToken(Context context, String value)
{
    try {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("access_token", value);
            myEdit.apply();
    }
    catch (Exception e)
    {
        Toast.makeText(context, ""+e.toString(), Toast.LENGTH_SHORT).show();
    }

}

    public static String getToken(Context context)
    {
        SharedPreferences sh = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String s1 = sh.getString("access_token", "");

        return s1;

    }

    public static void putTokenType(Context context, String value)
    {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString("access_token_type", value);
            myEdit.apply();
        }
        catch (Exception e)
        {
            Toast.makeText(context, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    public static String getTokenType(Context context)
    {
        SharedPreferences sh = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String s1 = sh.getString("access_token_type", "");

        return s1;

    }

    public static void putCityName(Context context, String value)
    {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString("city_name", value);
            myEdit.apply();
        }
        catch (Exception e)
        {
            Toast.makeText(context, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }

    }


    public static String getCityName(Context context)
    {
        SharedPreferences sh = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String s1 = sh.getString("city_name", "");

        return s1;

    }


    public static void putCityId(Context context, int value)
    {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putInt("city_id", value);
            myEdit.apply();
        }
        catch (Exception e)
        {
            Toast.makeText(context, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }

    }


    public static int getCityId(Context context)
    {
        SharedPreferences sh = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        int s1 = sh.getInt("city_id", 0);

        return s1;

    }

    public static int isIvExist(Context context, String tag)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String iv = sharedPreferences.getString(tag, null);

        if(iv != null) {
            // do some thing
            return 0;
        }
        else return 1;
    }



    public static void clearPref(Context context)
    {
        SharedPreferences preferences =context.getSharedPreferences("loginModelPrefs",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        SharedPreferences preferences2 =context.getSharedPreferences("sDataLoginPref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = preferences2.edit();
        editor2.clear();
        editor2.apply();
    }

}





