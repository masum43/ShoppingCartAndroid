package com.maces.ecommerce.skcashandcarry.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.maces.ecommerce.skcashandcarry.R;

import java.util.Locale;

public class Splash extends AppCompatActivity {

    Handler handler;
    SharedPreferences sharedPreferences_Language;

    protected SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferences_Language = getSharedPreferences("Language", MODE_PRIVATE);
        String Lang = sharedPreferences_Language.getString("Selected", "");
        {
            assert Lang != null;
            if (Lang.equals("en")) {
                setAppLocal("en");
            } else {
                setAppLocal("es");
            }
            try {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            } catch (Exception ignored) {

            }

            ProgressBar progressBar =  findViewById(R.id.progress);
            Sprite doubleBounce = new Circle();
            progressBar.setIndeterminateDrawable(doubleBounce);
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(Splash.this, Login.class));
                }
            }, 2500);

        }

    }
    private void setAppLocal(String Language)
    {
        Resources resources=getResources();
        DisplayMetrics displayMetrics=resources.getDisplayMetrics();
        Configuration configuration=resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(new Locale(Language.toLowerCase()));
        }
        else
        {
            configuration.locale=new Locale(Language.toLowerCase());
        }
        resources.updateConfiguration(configuration, displayMetrics);

        SharedPreferences.Editor editor = sharedPreferences_Language.edit();
        editor.putString("Selected",Language);
        editor.commit();
    }
}