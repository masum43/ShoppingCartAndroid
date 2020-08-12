package com.maces.ecommerce.skcashandcarry.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.maces.ecommerce.skcashandcarry.Interfaces.CheckEmail;
import com.maces.ecommerce.skcashandcarry.Model.ServiceGenerator;
import com.maces.ecommerce.skcashandcarry.R;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {
    protected TextInputEditText tvEmail;
    protected Button Check_Email;
    protected LinearLayout checkEmailLayout,newPasswordLayout;
    protected ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        tvEmail=(TextInputEditText)findViewById(R.id.tvEmail);
        Check_Email=(Button)findViewById(R.id.btn_CheckEmail);
        progressDialog=new ProgressDialog(ForgotPassword.this);
        progressDialog.setMessage(getResources().getString(R.string.validating_Email));
        progressDialog.setCancelable(false);
        checkEmailLayout=(LinearLayout)findViewById(R.id.layout_forgot);
        newPasswordLayout=(LinearLayout)findViewById(R.id.layout_newpass);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#000000\">" +getResources().getString(R.string.forgot) + "</font>")));
        newPasswordLayout.setVisibility(View.GONE);
        checkEmailLayout.setVisibility(View.VISIBLE);

        Check_Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvEmail.getText().toString().equals("")|| tvEmail.getText().toString().length()<1|| TextUtils.isEmpty(tvEmail.getText().toString()))
                {
                    tvEmail.setError(getResources().getString(R.string.enter_email));
                    tvEmail.setFocusable(true);
                }
                else if(!isValidEmailId(tvEmail.getText().toString()))
                {
                    tvEmail.setError(getResources().getString(R.string.enter_correct_email));
                    tvEmail.setFocusable(true);
                }

                else
                {

                    Validate_Email();
                }
            }
        });



    }

    private void Validate_Email()
    {
        progressDialog.show();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("email", tvEmail.getText().toString());
        CheckEmail jsonPostService = ServiceGenerator.createService(CheckEmail.class, "https://skcc.luqmansoftwares.com/api/");
        Call<JsonObject> call = jsonPostService.postRawJSON(paramObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try{
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        if(response.body().get("message").getAsString().equals("We have emailed your password reset link.Plese check your email's inbox"))
                        {
                            Toast.makeText(ForgotPassword.this, getResources().getString(R.string.check), Toast.LENGTH_LONG).show(); // do something with that
                            finish();
                        }

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(ForgotPassword.this, getResources().getString(R.string.email_not_exist), Toast.LENGTH_LONG).show(); // do something with that
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
}
