package com.maces.ecommerce.skcashandcarry.View;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.maces.ecommerce.skcashandcarry.Adapter.CartAdapter;
import com.maces.ecommerce.skcashandcarry.Fragments.Product_Home;
import com.maces.ecommerce.skcashandcarry.Interfaces.CounterCallBack;
import com.maces.ecommerce.skcashandcarry.Interfaces.Webservices;
import com.maces.ecommerce.skcashandcarry.MainActivity;
import com.maces.ecommerce.skcashandcarry.Model.ProductImage;
import com.maces.ecommerce.skcashandcarry.R;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.maces.ecommerce.skcashandcarry.Adapter._PaginationAdapter.cartModels;


public class CartActivity extends AppCompatActivity implements CounterCallBack {

    public static TextView grandTotal;
    public static double grandTotalplus;
    // create a temp list and add cartitem list
    public static ArrayList<ProductImage> temparraylist;
    RecyclerView cartRecyclerView;
    CartAdapter cartAdapter;
    LinearLayout proceedToBook;
    Context context;
    private Toolbar mToolbar;
    SharedPreferences cart_class, home_pref, prf;
    ProgressDialog progressDialog;
    String token_type, access_token;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        context = this;
        temparraylist = new ArrayList<>();
        proceedToBook = findViewById(R.id.proceed_to_book);
        grandTotal = findViewById(R.id.grand_total_cart);
        home_pref = getSharedPreferences("HomeItem", MODE_PRIVATE);
        cart_class = getSharedPreferences("Cart", MODE_PRIVATE);
        prf = getSharedPreferences("LoginPref", MODE_PRIVATE);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        progressDialog = new ProgressDialog(CartActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.Submitting_Order));
        progressDialog.setCancelable(false);
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        token_type = prf.getString("token_typeKey", "");
        access_token = prf.getString("access_tokenKey", "");
        grandTotal.setText("");
        actionBar.setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        if (getSupportActionBar()!=null)
        Objects.requireNonNull(getSupportActionBar()).setTitle((Html.fromHtml("<font color=\"#000000\">" + getResources().getString(R.string.cart) + "</font>")));


        if (cartModels.size() > 0) {
            grandTotal.setText("");
            for (int i = 0; i < cartModels.size(); i++) {
                for (int j = i + 1; j < cartModels.size(); j++) {
                    if (cartModels.get(i).getProductImage().equals(cartModels.get(j).getProductImage())) {
                        cartModels.get(i).setProductQuantity(cartModels.get(j).getProductQuantity());
                        cartModels.get(i).setProductName(cartModels.get(j).getProductName());
                        cartModels.get(i).setTotalCash(cartModels.get(j).getTotalCash());
                        cartModels.remove(j);
                        j--;
                        Log.e("remove", String.valueOf(cartModels.size()));
                    }
                }
            }
            temparraylist.addAll(cartModels);
            cartModels.clear();
            Log.d("sizecart_11", String.valueOf(temparraylist.size()));
            Log.d("sizecart_22", String.valueOf(cartModels.size()));
            // this code is for get total cash
            for (int i = 0; i < temparraylist.size(); i++) {
                grandTotalplus = grandTotalplus + Double.parseDouble(temparraylist.get(i).getProductPrice()) * (temparraylist.get(i).getProductQuantity());
            }
        }
        else {
            grandTotal.setText("");
            Gson gson = new Gson();
            String json = cart_class.getString("Cart", "");
            if (json.isEmpty()) {
                Toast.makeText(CartActivity.this, getResources().getString(R.string.noitem), Toast.LENGTH_LONG).show();
            } else {
                Type type = new TypeToken<ArrayList<ProductImage>>() {
                }.getType();
                temparraylist = gson.fromJson(json, type);
            }
            for (int i = 0; i < temparraylist.size(); i++) {
                grandTotalplus = grandTotalplus + Double.parseDouble(temparraylist.get(i).getProductPrice()) * (temparraylist.get(i).getProductQuantity());
            }
        }
        BigDecimal bd = new BigDecimal(Double.toString(grandTotalplus));
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        grandTotal.setText("€ " + String.valueOf(bd.doubleValue()));
        cartRecyclerView = findViewById(R.id.recycler_view_cart);
        cartAdapter = new CartAdapter(temparraylist, this,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        cartRecyclerView.setLayoutManager(mLayoutManager);
        cartRecyclerView.setAdapter(cartAdapter);
        proceedToBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Post_JSON();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        grandTotalplus = 0;
        cartModels.clear();
        for (int i = 0; i < temparraylist.size(); i++) {
        }
        cartModels.addAll(temparraylist);
        MainActivity.cart_count = (temparraylist.size());
//                addItemInCart.clear();
        Home.cart_count = temparraylist.size();

        Gson gson = new Gson();
        String json = gson.toJson(cartModels);
        SharedPreferences.Editor editor = home_pref.edit();
        editor.putString("HomeItem", json);
        editor.apply();

        String jsonn = gson.toJson(cartModels);
        SharedPreferences.Editor editorr = cart_class.edit();
        editorr.putString("Cart", jsonn);
        editorr.apply();
        Home.cart_count = temparraylist.size();
        Product_Home.cart_count = temparraylist.size();
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (item.getItemId() == android.R.id.home) {
            grandTotalplus = 0;
            cartModels.clear();

            cartModels.addAll(temparraylist);
            MainActivity.cart_count = (temparraylist.size());
//                addItemInCart.clear();
            Home.cart_count = temparraylist.size();

            Gson gson = new Gson();
            String json = gson.toJson(cartModels);
            SharedPreferences.Editor editor = home_pref.edit();
            editor.putString("HomeItem", json);
            editor.apply();

            String jsonn = gson.toJson(cartModels);
            SharedPreferences.Editor editorr = cart_class.edit();
            editorr.putString("Cart", jsonn);
            editorr.apply();
            Home.cart_count = temparraylist.size();
            Product_Home.cart_count = temparraylist.size();
            finish();
        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }

    public void Post_JSON() {

        if (temparraylist.size() > 0) {
            progressDialog.show();
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Accept", "application/json");
            if (token_type.toString().length() < 1) {
                headers.put("Authorization", Login.token_type_val + " " + Login.access_token_val);
            } else {
                headers.put("Authorization", token_type + " " + access_token);
            }

            JsonObject jsonObject1 = new JsonObject();
            JsonArray jsonArray = new JsonArray();
            jsonObject1.addProperty("notes", getResources().getString(R.string.checkout_text));
            JsonArray product_array = new JsonArray();
            int Quantity;

            for (int i = 0; i < temparraylist.size(); i++) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("product_id", String.valueOf(temparraylist.get(i).getId()));
                Double Val = Double.parseDouble(temparraylist.get(i).getProductPrice());
                Double FinalP = Double.parseDouble(temparraylist.get(i).getProductPrice());
                BigDecimal bd = new BigDecimal(Double.toString(FinalP));
                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                jsonObject.addProperty("price", bd.doubleValue());
                jsonObject.addProperty("quantity", String.valueOf(temparraylist.get(i).getProductQuantity()));

                jsonArray.add(jsonObject);
            }
            jsonObject1.add("order_list", jsonArray);
            Log.e("JSONArray", String.valueOf(jsonArray));
            Log.e("JSON OBject", String.valueOf(jsonObject1));
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Webservices.URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Webservices jsonPostService = retrofit.create(Webservices.class);
            Call<JsonObject> call = jsonPostService.ApiName(headers, jsonObject1);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                    progressDialog.dismiss();
                    if (response.code() == 200) {
                   //     Toast.makeText(CartActivity.this, "" + getResources().getString(R.string.successmessage), Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor3 = home_pref.edit();
                        editor3.clear();
                        editor3.apply();
                        SharedPreferences.Editor editor4 = cart_class.edit();
                        editor4.clear();
                        editor4.apply();
                        temparraylist.clear();
                        cartModels.clear();
                        Home.cart_count = 0;
                        MainActivity.cart_count = 0;
                        //    Product_Home.cartModels.clear();
                        Product_Home.cart_count = 0;
                        progressDialog.dismiss();
                        Intent intent = new Intent(CartActivity.this, order_placed.class);
                        startActivity(intent);
                        CartActivity.this.finish();

                    } else {
                    //    Toast.makeText(CartActivity.this, getResources().getString(R.string.cannotplace), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(CartActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(context, getResources().getString(R.string.noitem), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void CountCheck() {

        if (temparraylist.size() == 1){
            CartActivity.this.finish();
        }
    }
}