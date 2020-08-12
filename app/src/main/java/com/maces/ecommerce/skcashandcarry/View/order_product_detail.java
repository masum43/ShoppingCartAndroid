package com.maces.ecommerce.skcashandcarry.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.maces.ecommerce.skcashandcarry.Adapter.order_detail_adapter;
import com.maces.ecommerce.skcashandcarry.Interfaces.getdetailoforder;
import com.maces.ecommerce.skcashandcarry.Model.Order_Model;
import com.maces.ecommerce.skcashandcarry.Model.ProductService;
import com.maces.ecommerce.skcashandcarry.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class order_product_detail extends AppCompatActivity {

    RecyclerView recycler_view_orderdetail;
    private List<Order_Model> order_modelList;
    protected Order_Model order_model;
    protected ProgressDialog progressDialog;
    static String access_token, token_type, price_category;
    public static final String CartPref = "CartPref";
    public static final String Cart_Value = "cart_value";
    SharedPreferences prf;
    SimpleDateFormat simpleDateFormat1,simpleDateFormat2;
    TextView order_number,tvDate;
    public static View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_product_detail);
        order_number = findViewById(R.id.order_number);
        tvDate =findViewById(R.id.tvDate);
         view = getCurrentFocus();
//        closeKeyboard();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tvDate.getWindowToken(), 0);
        progressDialog=new ProgressDialog(order_product_detail.this);
        progressDialog.setCancelable(false);
        simpleDateFormat1=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        simpleDateFormat2=new SimpleDateFormat("dd-MM-YYYY");
        progressDialog.setMessage(getResources().getString(R.string.lo));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#000000\">" + getResources().getString(R.string.order_detail) + "</font>")));
        prf = getSharedPreferences("LoginPref", MODE_PRIVATE);

        if( prf.getString("token_typeKey", "").length()>0) {
            token_type = prf.getString("token_typeKey", "");
            access_token = prf.getString("access_tokenKey", "");
        }
        else
        {
            token_type = Login.token_type_val;
            access_token = Login.access_token_val;
        }
        recycler_view_orderdetail=(RecyclerView)findViewById(R.id.recycler_view_orderdetail);
        order_modelList=new ArrayList<>();
        httprequest();

    }
    private void getdetail()
    {
        getdetailoforder jsonPostService;
        progressDialog.show();
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Accept","application/json");
        headers.put("Authorization", token_type+" "+access_token);
        JsonObject jsonObject= new JsonObject();
        if(token_type.toString().length()<1) {
            jsonPostService = ProductService.createService(getdetailoforder.class, "https://skcc.luqmansoftwares.com/api/auth/order/"+getIntent().getExtras().getString("id")+"/",Login.token_type_val+" "+Login.access_token_val);
        }
        else {
            jsonPostService = ProductService.createService(getdetailoforder.class, "https://skcc.luqmansoftwares.com/api/auth/order/"+getIntent().getExtras().getString("id")+"/",token_type+" "+access_token);
        }

        Call<JsonObject> call = jsonPostService.postRawJSON();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try{
                    if (response.isSuccessful()) {
                        parse_data(response.body().toString());
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(order_product_detail.this,"Error:"+response.errorBody(), Toast.LENGTH_LONG).show(); // do something with that
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
    public void parse_data(String response) {

        String Status, Price, Date, image_url, Quantity, Method, id,ProductName;
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonarray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                Order_Model order_model=new Order_Model();

                Price=jsonobject.getString("product_price");
                order_model.setPrice(Price);
                Date=parseDate(jsonobject.getString("created_at"),simpleDateFormat1,simpleDateFormat2);
                order_model.setDate(Date);
                tvDate.setText(Date);
                Quantity=jsonobject.getString("quantity");
                order_model.setQuantity(Quantity);
                image_url=jsonobject.getString("product_price");
                order_model.setPrice(Price);
                id=jsonobject.getString("quantity");
                order_model.setQuantity(Quantity);
                ProductName=jsonobject.getString("product_price");
                order_model.setPrice(Price);
                order_modelList.add(order_model);

            }
            recycler_view_orderdetail.setAdapter(new order_detail_adapter(order_product_detail.this,order_modelList));
            recycler_view_orderdetail.setLayoutManager(new LinearLayoutManager(order_product_detail.this,LinearLayoutManager.VERTICAL,false));
            progressDialog.dismiss();
            closeKeyboard();
        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }

    }
    public static String parseDate(String inputDateString, SimpleDateFormat inputDateFormat, SimpleDateFormat outputDateFormat) {
        Date date = null;
        String outputDateString = null;
        try {
            date = inputDateFormat.parse(inputDateString);
            outputDateString = outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDateString;
    }

    private void httprequest()
    {
        int val=Integer.parseInt(getIntent().getExtras().getString("id"));
        progressDialog.show();


        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, "http://www.skcc.luqmansoftwares.com/api/auth/order/"+val, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String Respon, Price, Date, image_url, Quantity, Method, id,ProductName;
                try {
                    JSONArray jsonObject= new JSONArray(response);

                    for (int i = 0; i < jsonObject.length(); i++) {
                        JSONObject jsonobject = jsonObject.getJSONObject(i);
                        Order_Model order_model=new Order_Model();

                        Price=jsonobject.getString("product_price");
                        order_model.setPrice(Price);
                        id=jsonobject.getString("order_id");
                        order_number.setText(getResources().getString(R.string.order_id)+" "+id);

                        Quantity=jsonobject.getString("quantity");
                        order_model.setQuantity(Quantity);
                        Date=parseDate(jsonobject.getString("created_at"),simpleDateFormat1,simpleDateFormat2);
                        order_model.setDate(Date);
                        tvDate.setText(Date);
                        if(jsonobject.getString("product").length()<0)
                        {
                            order_modelList.add(order_model);
                        }
                        else {
                            Respon = jsonobject.getString("product");
                            JSONObject JSONResponse = new JSONObject(Respon);
                            image_url = JSONResponse.getString("product_image");
                            order_model.setImage_url("https://skcc.luqmansoftwares.com/uploads/products/" + image_url);
                            ProductName = JSONResponse.getString("name");
                            order_model.setProductName(ProductName);
                            order_modelList.add(order_model);
                        }
                    }
                    recycler_view_orderdetail.setAdapter(new order_detail_adapter(order_product_detail.this,order_modelList));
                    recycler_view_orderdetail.setLayoutManager(new LinearLayoutManager(order_product_detail.this,LinearLayoutManager.VERTICAL,false));
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(order_product_detail.this, ""+error.toString(), Toast.LENGTH_SHORT).show();

            }
        }){
            /** Passing some request headers* */
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Accept","application/json");
                headers.put("Authorization",token_type+" "+access_token);
                headers.put("Content-Type","application/json");

                return headers;
            }
        };;

        queue.add(jsonObjReq);
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
    public void closeKeyboard() {

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
