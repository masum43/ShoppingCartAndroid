package com.maces.ecommerce.skcashandcarry.Fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.maces.ecommerce.skcashandcarry.Interfaces.CounterCallBack;
import com.maces.ecommerce.skcashandcarry.Interfaces.Update_Password;
import com.maces.ecommerce.skcashandcarry.Model.ProductService;
import com.maces.ecommerce.skcashandcarry.R;
import com.maces.ecommerce.skcashandcarry.View.Login;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class update_password extends Fragment {

    private TextInputEditText old_password, new_password, confirm_password;
    private SharedPreferences prf;
    static String access_token, token_type, price_category;
    private ProgressDialog progressDialog;
    private SharedPreferences pref;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_update_password, container, false);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#000000\">" + getString(R.string.update_password) + "</font>")));
        prf = getActivity().getSharedPreferences("LoginPref", MODE_PRIVATE);
        token_type = prf.getString("token_typeKey", "");
        access_token = prf.getString("access_tokenKey", "");
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.updatingpassword));
        progressDialog.setCancelable(false);
        pref = getActivity().getSharedPreferences("LoginPref", MODE_PRIVATE);

        // This callback will only be called when MyFragment is at least Started
        old_password = (TextInputEditText) root.findViewById(R.id.tvoldPassword);
        new_password = (TextInputEditText) root.findViewById(R.id.tvPassword);
        confirm_password = (TextInputEditText) root.findViewById(R.id.tvConfirmPassword);
        Button btn_Done = (Button) root.findViewById(R.id.btn_Done);
        btn_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Objects.requireNonNull(old_password.getText()).toString().equals("") || TextUtils.isEmpty(old_password.getText().toString()) ||
                        Objects.requireNonNull(new_password.getText()).toString().equals("") || TextUtils.isEmpty(new_password.getText().toString())) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.fill_all), Toast.LENGTH_SHORT).show();
                } else if (old_password.getText().toString().length() < 8 || TextUtils.isEmpty(old_password.getText().toString())) {
                    old_password.setError(getResources().getString(R.string.enter_correct_password));
                    old_password.setFocusable(true);
                } else if (new_password.getText().toString().equals("") || new_password.getText().toString().length() < 8 || TextUtils.isEmpty(new_password.getText().toString())) {
                    new_password.setError(getResources().getString(R.string.enter_correct_password));
                    new_password.setFocusable(true);
                } else if (Objects.requireNonNull(confirm_password.getText()).toString().equals("") || confirm_password.getText().toString().length() < 8 || TextUtils.isEmpty(confirm_password.getText().toString())) {
                    confirm_password.setError(getResources().getString(R.string.enter_correct_password));
                    confirm_password.setFocusable(true);
                } else if (!confirm_password.getText().toString().equals(new_password.getText().toString())) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.password_not_match), Toast.LENGTH_LONG).show();

                } else {
                    Update_Password();
                }

            }
        });


        return root;
    }

    private void Update_Password() {
        progressDialog.show();
        JsonObject paramObject = new JsonObject();
        Update_Password jsonPostService;
        paramObject.addProperty("old_password", Objects.requireNonNull(old_password.getText()).toString());
        paramObject.addProperty("new_password", Objects.requireNonNull(new_password.getText()).toString());
        paramObject.addProperty("new_password_confirmation", Objects.requireNonNull(confirm_password.getText()).toString());
        if (token_type.length() < 1) {
            jsonPostService = ProductService.createService(Update_Password.class, "https://skcc.luqmansoftwares.com/api/auth/", Login.token_type_val + " " + Login.access_token_val);
        } else {
            jsonPostService = ProductService.createService(Update_Password.class, "https://skcc.luqmansoftwares.com/api/auth/", token_type + " " + access_token);
        }

        Call<JsonObject> call = jsonPostService.postRawJSON(paramObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "" + response.body().get("message").getAsString(), Toast.LENGTH_LONG).show(); // do something with that              getActivity().finish();
                        if (response.body().get("message").getAsString().equals("You entered Invalid old password")) {
                            old_password.requestFocus();
                        } else {
                            SharedPreferences.Editor editor = pref.edit();
                            editor.clear();
                            editor.apply();
                            if(getActivity() != null) {
                                getActivity().finish();
                            }
                        }
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                try {
                    Log.e("response-error", call.toString());
                    progressDialog.dismiss();
                } catch (Exception e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        });
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flContent, fragment);
        transaction.commit();
    }
}
