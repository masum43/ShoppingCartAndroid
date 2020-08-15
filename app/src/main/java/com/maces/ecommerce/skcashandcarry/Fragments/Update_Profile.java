package com.maces.ecommerce.skcashandcarry.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.maces.ecommerce.skcashandcarry.Interfaces.Get_UserInfor;
import com.maces.ecommerce.skcashandcarry.Model.MyErrorMessage;
import com.maces.ecommerce.skcashandcarry.Model.ProductService;
import com.maces.ecommerce.skcashandcarry.MySharedPref;
import com.maces.ecommerce.skcashandcarry.R;
import com.maces.ecommerce.skcashandcarry.View.Login;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class Update_Profile extends Fragment implements AdapterView.OnItemSelectedListener{

    private TextInputEditText Fullname, Email, Password, Confirm, PostCode, Business, Address, Phone;
    static String access_token, token_type, price_category;
    private ProgressDialog progressDialog, getProgressDialog;
    private Button btn_Done;
    private SharedPreferences prf;
    private String name, email, business_name, mobile_number, business_address;
    Spinner spinner;
    private String city_name;
    private int city_id;
    private String city_response;
    private TextView cityNameTv;

    private ArrayList<String> allCityList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_update__profile, container, false);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#000000\">" + getString(R.string.menu_updateprofile) + "</font>")));
        prf = getActivity().getSharedPreferences("LoginPref", MODE_PRIVATE);
        token_type = prf.getString("token_typeKey", "");
        access_token = prf.getString("access_tokenKey", "");
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.updating_profile));
        progressDialog.setCancelable(false);
        getProgressDialog = new ProgressDialog(getActivity());
        getProgressDialog.setMessage(getResources().getString(R.string.fetching_data));
        getProgressDialog.setCancelable(false);
        getProgressDialog.show();
        Fullname = root.findViewById(R.id.tvFullName);
        Email = root.findViewById(R.id.tvEmail);
        Business = root.findViewById(R.id.tvBusiness);
        Phone = root.findViewById(R.id.tvPhone);
        Address = root.findViewById(R.id.tvaddress);
        PostCode = root.findViewById(R.id.tvPostCode);
        btn_Done = root.findViewById(R.id.btn_Done);
        cityNameTv=(TextView) root.findViewById(R.id.city_name_tv);


        spinner = (Spinner) root.findViewById(R.id.spinner);
        prepareCityDropDown();

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        //spinner.setPrompt(MySharedPref.getCityName(getContext()));



        Get_Userinfo();
        btn_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Fullname.getText().toString().equals("") || Fullname.getText().toString().length() < 1) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.fullname), Toast.LENGTH_LONG).show();

                } else if (Fullname.getText().toString().length() < 3) {
                    Fullname.setError(getResources().getString(R.string.full_name_correct));
                    Fullname.setFocusable(true);
                } else if (Address.getText().toString().equals("") || Address.getText().toString().length() < 1) {
                    Address.setError(getResources().getString(R.string.enter_address));
                    Address.setFocusable(true);

                } else if (Address.getText().toString().length() < 6) {
                    Address.setError(getResources().getString(R.string.correct_address));
                    Address.setFocusable(true);
                } else if (Business.getText().toString().equals("") || Business.getText().toString().length() < 1) {
                    Business.setError(getResources().getString(R.string.business_name));
                    Business.setFocusable(true);

                } else if (Business.getText().toString().length() < 6) {
                    Business.setError(getResources().getString(R.string.business_correct));
                    Business.setFocusable(true);
                } else if (PostCode.getText().toString().equals("") || TextUtils.isEmpty(PostCode.getText().toString())) {
                    PostCode.setError(getResources().getString(R.string.post_name));
                    PostCode.setFocusable(true);
                } else if (PostCode.getText().toString().length() < 5) {
                    PostCode.setError(getResources().getString(R.string.correct_post));
                    PostCode.setFocusable(true);
                } else if (PostCode.getText().toString().length() > 5) {
                    PostCode.setError(getResources().getString(R.string.correct_post));
                    PostCode.setFocusable(true);
                } else if (Email.getText().toString().equals("") || Email.getText().toString().length() < 1) {
                    Email.setError(getResources().getString(R.string.enter_correct_email));
                    Email.setFocusable(true);

                } else if (!isValidEmailId(Email.getText().toString())) {
                    Email.setError(getResources().getString(R.string.enter_correct_email));
                    Email.setFocusable(true);

                } else if (Phone.getText().toString().equals("") || Phone.getText().toString().length() < 1) {
                    Phone.setError(getResources().getString(R.string.phone));
                    Phone.setFocusable(true);
                } else if (Phone.getText().toString().length() > 9 || Phone.getText().toString().length() < 9) {
                    Phone.setError(getResources().getString(R.string.correct_mobile_number));
                    Phone.setFocusable(true);

                } else {
                    Update_Profile();
                    hideKeyboard(Objects.requireNonNull(getActivity()));
                }

            }
        });
        return root;
    }

    private void Update_Profile() {
        com.maces.ecommerce.skcashandcarry.Interfaces.Update_Profile jsonPostService;
        progressDialog.show();
        JsonObject paramObject = new JsonObject();

        paramObject.addProperty("name", Fullname.getText().toString());
        paramObject.addProperty("email", Email.getText().toString());
        paramObject.addProperty("mobile_number", Phone.getText().toString());
        paramObject.addProperty("business_name", Business.getText().toString());
        paramObject.addProperty("post_code", PostCode.getText().toString().trim());
        paramObject.addProperty("address", Address.getText().toString());
        paramObject.addProperty("city", city_id);

        if (token_type.length() < 1) {
            jsonPostService = ProductService.createService(com.maces.ecommerce.skcashandcarry.Interfaces.Update_Profile.class, "https://skcc.luqmansoftwares.com/api/auth/", Login.token_type_val + " " + Login.access_token_val);
        } else {
            jsonPostService = ProductService.createService(com.maces.ecommerce.skcashandcarry.Interfaces.Update_Profile.class, "https://skcc.luqmansoftwares.com/api/auth/", token_type + " " + access_token);
        }
        Call<JsonObject> call = jsonPostService.postRawJSON(paramObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    String M = "";
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        Gson gson = new Gson();

                        MyErrorMessage message = gson.fromJson(response.body(), MyErrorMessage.class);
                        M = message.getMessage();
                    }
                    if (response.code() == 422) {
                        Toast.makeText(getActivity(), "" + getResources().getString(R.string.emailalready), Toast.LENGTH_LONG).show(); // do something with that
                        progressDialog.dismiss();
                        Email.requestFocus();
                        Email.setError(getResources().getString(R.string.emailalready));
                    } else if (M.equals("Your Profile has been Updated Successfully")) {
                        Toast.makeText(getActivity(), "" + M.toString(), Toast.LENGTH_LONG).show(); // do something with that              getActivity().finish();
                        progressDialog.dismiss();
                        loadFragment(new Product_Home());
                    } else if (M.equals("") || M.equals(TextUtils.isEmpty(M.toString()))) {
                        Toast.makeText(getActivity(), "", Toast.LENGTH_LONG).show(); // do something with that              getActivity().finish();
                        progressDialog.dismiss();
                    } else {
                        progressDialog.dismiss();
                        Gson gson = new Gson();
                        MyErrorMessage message = gson.fromJson(response.errorBody().charStream(), MyErrorMessage.class);
                        M = message.getMessage();
                        Toast.makeText(getActivity(), "" + M, Toast.LENGTH_LONG).show(); // do something with that              getActivity().finish();
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

    private boolean isValidEmailId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    private void Get_Userinfo() {
        Get_UserInfor jsonPostService;
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        if (token_type.toString().length() < 1) {
            headers.put("Authorization", Login.token_type_val + " " + Login.access_token_val);
        } else {
            headers.put("Authorization", token_type + " " + access_token);
        }
        JsonObject jsonObject = new JsonObject();
        if (token_type.toString().length() < 1) {
            jsonPostService = ProductService.createService(Get_UserInfor.class, "https://skcc.luqmansoftwares.com/api/auth/", Login.token_type_val + " " + Login.access_token_val);
        } else {
            jsonPostService = ProductService.createService(Get_UserInfor.class, "https://skcc.luqmansoftwares.com/api/auth/", token_type + " " + access_token);
        }

        Call<JsonObject> call = jsonPostService.postRawJSON();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    if (response.isSuccessful()) {
                        getProgressDialog.dismiss();
                        name = response.body().get("name").getAsString();
                        Fullname.setText("" + name);

                        email = response.body().get("email").getAsString();
                        Email.setText("" + email);

                        mobile_number = response.body().get("mobile_number").getAsString();
                        Phone.setText("" + mobile_number);

                        business_address = response.body().get("address").getAsString();
                        Address.setText("" + business_address);

                        business_address = response.body().get("post_code").getAsString();
                        PostCode.setText("" + business_address);

                        business_name = response.body().get("business_name").getAsString();
                        Business.setText("" + business_name);

                        city_response = response.body().get("city").getAsString();
                        //city_id = Integer.parseInt(city_response);
                        cityNameTv.setText(MySharedPref.getCityName(getContext()));

                    } else {
                        getProgressDialog.dismiss();
                        Toast.makeText(getActivity(), "Error:" + response.errorBody(), Toast.LENGTH_LONG).show(); // do something with that
                    }
                } catch (Exception e) {
                    getProgressDialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                try {
                    Log.e("response-error", call.toString());
                    getProgressDialog.dismiss();
                } catch (Exception e) {
                    getProgressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadFragment(Fragment fragment) {

        FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flContent, fragment);
        transaction.commit();
    }
    private static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void prepareCityDropDown() {
        allCityList.add(MySharedPref.getCityName(getContext()));
        getAllCityList();
        // Spinner element


        //Log.d("city",allCityList.get(0));
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, allCityList);

        // Drop down layout style - list view with radio button
        //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        city_name = parent.getItemAtPosition(position).toString();
        city_id = position;

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + String.valueOf(position), Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private void getAllCityList() {
//        BackgroundApiTask backgroundApiTask = new BackgroundApiTask(context);
//        myDatabaseSource = new MyDatabaseSource(context);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        final String savedata = "postData";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                "https://skcc.luqmansoftwares.com/api/fetch-cities",
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("city_list", String.valueOf(response));

                        try {

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject book_object = response.getJSONObject(i);
                                String name = book_object.getString("name");

                                allCityList.add(name);


                            }

                        } catch (JSONException e) {

                            //Toast.makeText(this, "Server Error", Toast.LENGTH_LONG).show();
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Toast.makeText(getContext(), "Response error...", Toast.LENGTH_SHORT).show();
                // Do something when error occurred
                Log.e("BackgroundApiTask : ", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    return savedata == null ? null : savedata.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    Log.d("Unsprted Encoding", "URLleaveType");
                    return null;
                }
            }

        };
        requestQueue.add(jsonArrayRequest);


    }

}
