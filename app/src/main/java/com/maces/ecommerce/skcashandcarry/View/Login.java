package com.maces.ecommerce.skcashandcarry.View;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.maces.ecommerce.skcashandcarry.Login_User;
import com.maces.ecommerce.skcashandcarry.Model.MyErrorMessage;
import com.maces.ecommerce.skcashandcarry.Model.ServiceGenerator;
import com.maces.ecommerce.skcashandcarry.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    SharedPreferences sharedPreferences_Language;
    protected TextView tvSignup,tvForgot;
    protected Button btn_Login;
    protected TextInputEditText tvEmail,tvPassowrd;
    protected ProgressDialog progressDialog;
    public static final String LoginPref = "LoginPref" ;
    public static final String access_token = "access_tokenKey";
    public static final String token_type = "token_typeKey";
    public static final String expires_at = "expires_atKey";
    protected Intent logintintent;
    protected SharedPreferences sharedpreferences;
    public static String access_token_val,token_type_val,expires_at_val;
    protected String formattedDate;
    protected Date c;
    protected SimpleDateFormat df;
    protected CheckBox checkbox_remember;
    TextView btn_ChangeLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences_Language=getSharedPreferences("Language",MODE_PRIVATE);
        btn_ChangeLang=(TextView)findViewById(R.id.btn_ChangeLang);
        btn_ChangeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedpreferences = getSharedPreferences(LoginPref, Context.MODE_PRIVATE);
                String Lang=sharedPreferences_Language.getString("Selected","");

                if (Lang.equals("en")) {
                    setAppLocal("es");
                    btn_ChangeLang.setText(getResources().getString(R.string.language_english));
                    restartActivity();
                } else {
                    setAppLocal("en");
                    btn_ChangeLang.setText(getResources().getString(R.string.language_spnaish));
                    restartActivity();
                }

            }
        });
        progressDialog=new ProgressDialog(Login.this);
        progressDialog.setMessage(getResources().getString(R.string.pleasewait));
        progressDialog.setCancelable(false);
        logintintent=new Intent(Login.this, Home.class);
        sharedpreferences = getSharedPreferences(LoginPref, Context.MODE_PRIVATE);
        String Lang=sharedPreferences_Language.getString("Selected","");
        {
            if(Lang.equals("en"))
            {
                setAppLocal("en");
                btn_ChangeLang.setText(getResources().getString(R.string.language_english));

            }
            else {
                setAppLocal("es");
                btn_ChangeLang.setText(getResources().getString(R.string.language_spnaish));

            }
        }
        if (sharedpreferences.contains(access_token) && sharedpreferences.contains(token_type) && sharedpreferences.contains(expires_at)) {
            c = Calendar.getInstance().getTime();

            df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
            formattedDate = df.format(c);
            try {
                if(df.parse(df.format(c)).after(df.parse(sharedpreferences.getString("expires_atKey",""))))
                {
                    Toast.makeText(this, "Session Expired", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.clear();
                    editor.commit();
                    Intent   intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(formattedDate.equals(sharedpreferences.getString("expires_atKey",""))) {

                Toast.makeText(this, "Session Expired", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.commit();
                Intent   intent = new Intent(getApplicationContext(), Login.class);

                startActivity(intent);

            }
            else {
                startActivity(logintintent);
            }
        }
        checkbox_remember=(CheckBox)findViewById(R.id.checkbox_remember);
        tvSignup=(TextView)findViewById(R.id.tvSignup);
        tvForgot=(TextView)findViewById(R.id.tvForgot);
        btn_Login=(Button)findViewById(R.id.btn_Login);
        tvEmail=(TextInputEditText)findViewById(R.id.tvEmail);
        tvPassowrd=(TextInputEditText)findViewById(R.id.tvPasssword);


        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,ForgotPassword.class);
                startActivity(intent);
            }
        });
        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tvEmail.getText().toString().equals("")|| TextUtils.isEmpty(tvEmail.getText().toString()) || tvPassowrd.getText().toString().equals("")|| TextUtils.isEmpty(tvPassowrd.getText().toString())) {

                    Toast.makeText(Login.this, getResources().getString(R.string.fill_all), Toast.LENGTH_SHORT).show();

                }
                else if(tvEmail.getText().toString().equals("")|| TextUtils.isEmpty(tvEmail.getText().toString()))
                {
                    tvEmail.setError(getResources().getString(R.string.hint_email));
                    tvEmail.setFocusable(true);
                }
                else if(tvEmail.getText().toString().length()<1)
                {
                    tvEmail.setError(getResources().getString(R.string.enter_email));
                    tvEmail.setFocusable(true);
                }
                else if(!isValidEmailId(tvEmail.getText().toString()))
                {
                    tvEmail.setError(getResources().getString(R.string.enter_correct_email));
                    tvEmail.setFocusable(true);
                }
                else if(tvPassowrd.getText().toString().equals("")|| TextUtils.isEmpty(tvPassowrd.getText().toString()))
                {
                    tvPassowrd.setError(getResources().getString(R.string.enter_passsword));
                    tvPassowrd.setFocusable(true);
                }  else if(tvPassowrd.getText().toString().length()<1)
                {
                    tvPassowrd.setError(getResources().getString(R.string.enter_correct_password));
                    tvPassowrd.setFocusable(true);
                } else if(tvPassowrd.getText().toString().length()<8 )
                {
                    tvPassowrd.setError(getResources().getString(R.string.enter_correct_password));
                    tvPassowrd.setFocusable(true);
                }

                else
                {
                    progressDialog.show();
                    Login_User();
                }
            }
        });

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login_intent=new Intent(getApplicationContext(),Register.class);

                startActivity(login_intent);
            }
        });
    }

    private void Login_User()
    {

        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("email", tvEmail.getText().toString());
        paramObject.addProperty("password", tvPassowrd.getText().toString());
        Login_User jsonPostService = ServiceGenerator.createService(Login_User.class, "https://skcc.luqmansoftwares.com/api/auth/");
        Call<JsonObject> call = jsonPostService.postRawJSON(paramObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try{
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        if(checkbox_remember.isChecked()) {
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(token_type, response.body().get("token_type").getAsString());
                            editor.putString(access_token, response.body().get("access_token").getAsString());
                            editor.putString(expires_at, response.body().get("expires_at").toString());
                            editor.commit();
                            startActivity(logintintent);
                        }
                        else
                        {
                            token_type_val=response.body().get("token_type").getAsString();
                            access_token_val=response.body().get("access_token").getAsString();
                            expires_at_val=response.body().get("expires_at").toString();
                            startActivity(logintintent);

                        }
                    } else {
                        progressDialog.dismiss();
                        Gson gson = new Gson();
                        MyErrorMessage message=gson.fromJson(response.errorBody().charStream(),MyErrorMessage.class);
                        String M=message.getMessage();
                        Toast.makeText(Login.this, ""+M, Toast.LENGTH_LONG).show(); // do something with that


                    }
                }catch (Exception e){
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                try{
                    Log.e("response-error", call.toString());
                    progressDialog.dismiss();
                }catch (Exception e){
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        });
    }
    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(Login.this)
                .setIcon(R.drawable.ic_error)
                .setTitle(getResources().getString(R.string.closeapp))
                .setMessage(getResources().getString(R.string.areyousure))
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            finishAffinity();
                        }

                    }

                })
                .setNegativeButton(getResources().getString(R.string.no), null)
                .show();
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
    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
