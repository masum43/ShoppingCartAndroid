package com.maces.ecommerce.skcashandcarry.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.maces.ecommerce.skcashandcarry.Interfaces.Register_User;
import com.maces.ecommerce.skcashandcarry.Model.ServiceGenerator;
import com.maces.ecommerce.skcashandcarry.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {
    protected TextView tv_Login;
    protected TextInputEditText tvFullname,tvEmail,tvPassowrd,tvrepassword,tvPhhone,tvBusiness,tvaddress;
    protected Button btn_Register;
    protected ProgressDialog progressDialog;
    protected ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar;
        actionBar=getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);

      getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#000000\">" + getResources().getString(R.string.register) + "</font>")));
        //Getting Edit Text ID'S
        tvFullname=(TextInputEditText)findViewById(R.id.tvFullName);
        tvEmail=(TextInputEditText)findViewById(R.id.tvEmail);
        tvPassowrd=(TextInputEditText)findViewById(R.id.tv_Password);
        tvrepassword=(TextInputEditText)findViewById(R.id.tvrepassword);
        tvPhhone=(TextInputEditText)findViewById(R.id.tvPhone);
        tvBusiness=(TextInputEditText)findViewById(R.id.tvBusiness);
        tvaddress=(TextInputEditText)findViewById(R.id.tvaddress);
        progressDialog=new ProgressDialog(Register.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Creating User....");
        tv_Login=(TextView)findViewById(R.id.tvLogin);
        btn_Register=(Button)findViewById(R.id.btn_Register);
        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tvFullname.getText().toString().equals("")|| TextUtils.isEmpty(tvFullname.getText().toString()) ||tvEmail.getText().toString().equals("")|| tvEmail.getText().toString().length()<1|| TextUtils.isEmpty(tvEmail.getText().toString())||
                        tvPassowrd.getText().toString().equals("")||  TextUtils.isEmpty(tvPassowrd.getText().toString())||tvrepassword.getText().toString().equals("")|| TextUtils.isEmpty(tvrepassword.getText().toString())
                || tvBusiness.getText().toString().equals("")|| TextUtils.isEmpty(tvBusiness.getText().toString())||tvaddress.getText().toString().equals("")|| TextUtils.isEmpty(tvaddress.getText().toString()) ) {

                    Toast.makeText(Register.this, getResources().getString(R.string.fill_all), Toast.LENGTH_SHORT).show();
                }
                else if(tvFullname.getText().toString().equals("")|| TextUtils.isEmpty(tvFullname.getText().toString()))
                {
                    tvFullname.setError(getResources().getString(R.string.fullname));
                    tvFullname.setFocusable(true);
                }
                else if(tvFullname.getText().toString().length()<3)
                {
                    tvFullname.setError(getResources().getString(R.string.full_name_correct));
                    tvFullname.setFocusable(true);
                }
                else if(tvEmail.getText().toString().equals("")|| tvEmail.getText().toString().length()<1|| TextUtils.isEmpty(tvEmail.getText().toString()))
                {
                    tvEmail.setError(getResources().getString(R.string.enter_email));
                    tvEmail.setFocusable(true);
                }
                else if(!isValidEmailId(tvEmail.getText().toString()))
                {
                    tvEmail.setError(getResources().getString(R.string.enter_correct_email));
                    tvEmail.setFocusable(true);
                }
                else if(tvPassowrd.getText().toString().equals("")||  TextUtils.isEmpty(tvPassowrd.getText().toString()))
                {
                    tvPassowrd.setError(getResources().getString(R.string.enter_passsword));
                    tvPassowrd.setFocusable(true);
                }
                else if(tvPassowrd.getText().toString().length()<8)
                {
                    tvPassowrd.setError(getResources().getString(R.string.enter_correct_password));
                    tvPassowrd.setFocusable(true);
                }
                else if(tvrepassword.getText().toString().equals("")|| TextUtils.isEmpty(tvrepassword.getText().toString()))
                {
                    tvrepassword.setError(getResources().getString(R.string.repassword));
                    tvrepassword.setFocusable(true);
                }
                else if(tvrepassword.getText().toString().length()<8)
                {
                    tvrepassword.setError(getResources().getString(R.string.enter_correct_password));
                    tvrepassword.setFocusable(true);
                }
                else if(!tvPassowrd.getText().toString().equals(tvrepassword.getText().toString()))
                {
                    tvPassowrd.setError(getResources().getString(R.string.password_not_match));
                    tvPassowrd.setFocusable(true);

                }
                else if(tvPhhone.getText().toString().length()<9 || tvPhhone.getText().toString().length()>9)
                {
                    tvPhhone.setError(getResources().getString(R.string.correct_mobile_number));
                    tvPhhone.setFocusable(true);
                }

                else if(tvBusiness.getText().toString().equals("")|| TextUtils.isEmpty(tvBusiness.getText().toString()))
                {
                    tvBusiness.setError(getResources().getString(R.string.post_name));
                    tvBusiness.setFocusable(true);
                }
                else if(tvBusiness.getText().toString().length()<5)
                {
                    tvBusiness.setError(getResources().getString(R.string.correct_post));
                    tvBusiness.setFocusable(true);
                } else if(tvBusiness.getText().toString().length()>5)
                {
                    tvBusiness.setError(getResources().getString(R.string.correct_post));
                    tvBusiness.setFocusable(true);
                }
                else if(tvaddress.getText().toString().equals("")|| TextUtils.isEmpty(tvaddress.getText().toString()))
                {
                    tvaddress.setError(getResources().getString(R.string.enter_address));
                    tvaddress.setFocusable(true);
                }else if(tvaddress.getText().toString().length()<6)
                {
                    tvaddress.setError(getResources().getString(R.string.correct_address));
                    tvaddress.setFocusable(true);
                }
                else {
//                    if(validateMobile(tvPhhone.getText().toString()))
//                    {
                        progressDialog.show();
                        Register_User();
//                    }
//                    else {
//                        Toast.makeText(Register.this, ""+getResources().getString(R.string.valid_phone), Toast.LENGTH_SHORT).show();
//                    }


                }
            }
        });

        tv_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login_intent=new Intent(getApplicationContext(),Login.class);

                startActivity(login_intent);
            }
        });
    }

    private void Register_User()
    {
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("name", tvFullname.getText().toString());
        paramObject.addProperty("email", tvEmail.getText().toString());
        paramObject.addProperty("password", tvPassowrd.getText().toString());
        paramObject.addProperty("password_confirmation", tvrepassword.getText().toString());
        paramObject.addProperty("mobile_number", tvPhhone.getText().toString());
        paramObject.addProperty("business_name","" );
        paramObject.addProperty("city", 1);
        paramObject.addProperty("post_code", tvBusiness.getText().toString());
        paramObject.addProperty("address", tvaddress.getText().toString());

        Register_User jsonPostService = ServiceGenerator.createService(Register_User.class, "https://skcc.luqmansoftwares.com/api/auth/");
        Call<JsonObject> call = jsonPostService.postRawJSON(paramObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try{
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(Register.this, ""+response.body().get("message"), Toast.LENGTH_LONG).show(); // do something with that
                        Intent newIntent=new Intent(Register.this,Login.class);
                        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(newIntent);

                    } else {
                        progressDialog.dismiss();
                        if(response.code()== 422)
                        {
                            Toast.makeText(Register.this, ""+getResources().getString(R.string.emailalready), Toast.LENGTH_LONG).show(); // do something with that
                        }

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
    private boolean validateMobile(String mobilenumber ) {
        String pattern = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";
        Matcher m = null;
        Pattern r = Pattern.compile(pattern);
        if (!mobilenumber.toString().isEmpty()) {
            m = r.matcher(mobilenumber.toString().trim());
        }
        assert m != null;
        if (m.find()) {
            return true;
        } else {
return false;
        }
    }

}