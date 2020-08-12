package com.maces.ecommerce.skcashandcarry;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.maces.ecommerce.skcashandcarry.Adapter.ProductAdapter;
import com.maces.ecommerce.skcashandcarry.Interfaces.Fetch_Products;
import com.maces.ecommerce.skcashandcarry.Interfaces.Get_UserInfor;
import com.maces.ecommerce.skcashandcarry.Model.ProductModel;
import com.maces.ecommerce.skcashandcarry.Model.ProductService;
import com.maces.ecommerce.skcashandcarry.View.CartActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity implements ProductAdapter.CallBackUs, ProductAdapter.HomeCallBack {

    public static ArrayList<ProductModel> arrayList = new ArrayList<>();
    public static int cart_count = 0;
    ProductAdapter productAdapter;
    String name, brand, price, brand_id, category_id, description, p1, p2, p3, p4, p5, weight, size, product_image;
    RecyclerView productRecyclerView;
    protected ProgressDialog progressDialog;
    static String access_token, token_type, price_category;
    public static final String CartPref = "CartPref";
    public static final String Cart_Value = "cart_value";
    SharedPreferences prf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.Fetching));
        prf = getSharedPreferences("LoginPref", MODE_PRIVATE);
        token_type = prf.getString("token_typeKey", "");
        access_token = prf.getString("access_tokenKey", "");
        Get_Userinfo();
        productRecyclerView = findViewById(R.id.product_recycler_view);
    }

    private void Fetch_Products() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Fetch_Products.URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        Fetch_Products api = retrofit.create(Fetch_Products.class);
        Call<String> call = api.getProducts(1);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body());

                        String jsonresponse = response.body();
                        parse_Products(jsonresponse);

                    } else {
                        progressDialog.dismiss();
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                progressDialog.dismiss();

            }
        });


    }

    public void parse_Products(String response) {
        int id;
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonarray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                ProductModel product_class = new ProductModel();

                id = jsonobject.getInt("id");
                product_class.setId(id);
                name = jsonobject.getString("name");
                product_class.setName(name);
                category_id = jsonobject.getString("category_id");
                product_class.setCategory_id(category_id);
                description = jsonobject.getString("description");
                product_class.setDescription(description);
                weight = jsonobject.getString("weight");
                product_class.setWeight(weight);
                size = jsonobject.getString("size");
                product_class.setSize(size);

                switch (price_category) {
                    case "normal":
                        price = jsonobject.getString("price");
                        product_class.setPrice(price);
                        break;
                    case "p1":
                        if (jsonobject.getString("p1").equals("null")) {
                            price = jsonobject.getString("price");
                            product_class.setPrice(price);
                        } else {
                            price = jsonobject.getString("p1");
                            product_class.setPrice(price);
                        }

                        break;
                    case "p2":
                        if (jsonobject.getString("p2").equals("null")) {
                            price = jsonobject.getString("price");
                            product_class.setPrice(price);
                        } else {
                            price = jsonobject.getString("p2");
                            product_class.setPrice(price);
                        }

                        break;
                    case "p3":
                        if (jsonobject.getString("p3").equals("null")) {
                            price = jsonobject.getString("price");
                            product_class.setPrice(price);
                        } else {
                            price = jsonobject.getString("p3");
                            product_class.setPrice(price);
                        }


                        break;
                    case "p4":
                        if (jsonobject.getString("p4").equals("null")) {
                            price = jsonobject.getString("price");
                            product_class.setPrice(price);
                        } else {
                            price = jsonobject.getString("p4");
                            product_class.setPrice(price);
                        }


                        break;
                    case "p5":
                        if (jsonobject.getString("p5").equals("null")) {
                            price = jsonobject.getString("price");
                            product_class.setPrice(price);
                        } else {
                            price = jsonobject.getString("p5");
                            product_class.setPrice(price);
                        }


                        break;
                }
                brand = jsonobject.getJSONObject("brand").getString("name");
                product_class.setBrand(brand);
                product_image = jsonobject.getString("product_image");
                product_class.setProduct_image("https://skcc.luqmansoftwares.com/uploads/products/" + product_image);

                arrayList.add(product_class);

            }
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false);
            productRecyclerView.setLayoutManager(gridLayoutManager);
            productRecyclerView.setAdapter(productAdapter);
            progressDialog.dismiss();

        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }

    }


    @Override
    public void addCartItemView() {
        //addItemToCartMethod();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.cart_action);
        menuItem.setIcon(Converter.convertLayoutToImage(MainActivity.this, cart_count, R.drawable.ic_shopping_cart_white_24dp));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (item.getItemId() == R.id.cart_action) {
            if (cart_count < 1) {
                Toast.makeText(this, "there is no item in cart", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(this, CartActivity.class));
            }
        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void updateCartCount(Context context) {
        invalidateOptionsMenu();
    }

    @Override
    protected void onStart() {
        super.onStart();
        invalidateOptionsMenu();
    }

    private void Get_Userinfo() {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token_type + " " + access_token);
        JsonObject jsonObject = new JsonObject();
        Get_UserInfor jsonPostService = ProductService.createService(Get_UserInfor.class, "https://skcc.luqmansoftwares.com/api/auth/", token_type + " " + access_token);
        Call<JsonObject> call = jsonPostService.postRawJSON();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        price_category = response.body().get("price_category").getAsString();
                        Fetch_Products();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Error:" + response.errorBody(), Toast.LENGTH_LONG).show(); // do something with that
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

}
