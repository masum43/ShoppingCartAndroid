package com.maces.ecommerce.skcashandcarry.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maces.ecommerce.skcashandcarry.Adapter.Category_ProductAdapter;
import com.maces.ecommerce.skcashandcarry.Adapter.PaginationAdapter;
import com.maces.ecommerce.skcashandcarry.Adapter.PaginationScrollListener;
import com.maces.ecommerce.skcashandcarry.Adapter.SliderAdapter;
import com.maces.ecommerce.skcashandcarry.Converter;
import com.maces.ecommerce.skcashandcarry.Fragments.Product_Home;
import com.maces.ecommerce.skcashandcarry.Interfaces.Fetch_Categories_Products;
import com.maces.ecommerce.skcashandcarry.Interfaces.OnBackPressed;
import com.maces.ecommerce.skcashandcarry.Model.ProductImage;
import com.maces.ecommerce.skcashandcarry.Model.ProductModel;
import com.maces.ecommerce.skcashandcarry.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Category_Product extends AppCompatActivity implements Category_ProductAdapter.CallBackUs, Category_ProductAdapter.HomeCallBack, OnBackPressed {
    public static int cart_count = 0;
    String name, brand, price, brand_id, category_id, description, p1, p2, p3, p4, p5, Selecetd_Category, weight, size, product_image;
    RecyclerView productRecyclerView;
    //   protected ProgressDialog progressDialog;
    static String access_token, token_type, price_category;
    public static final String CartPref = "CartPref";
    LinearLayout layout_1, layout_2;
    int nn = 2;
    public static int selected_id = 0;
    protected SliderAdapter adapter;
    private Category_ProductAdapter paginationAdapter;
    private ProgressBar progressBar;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private SharedPreferences prf, shapref;
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_product);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        shapref = getSharedPreferences("Cart", MODE_PRIVATE);

    //    updateCartCount(Category_Product.this);

        layout_2 = findViewById(R.id.layout_2);
        layout_1 = findViewById(R.id.layout_1);
        selected_id = Objects.requireNonNull(getIntent().getExtras()).getInt("id");
        Selecetd_Category = getIntent().getExtras().getString("name");
        price_category = Product_Home.price_category;
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#000000\">" + Selecetd_Category + "</font>")));
        progressBar = findViewById(R.id.progressbar);
        //     progressDialog = new ProgressDialog(Category_Product.this);
        //    progressDialog.setMessage(getResources().getString(R.string.Fetching));
        //    progressDialog.show();
        productRecyclerView = findViewById(R.id.grid_products);

        check_cout();

   //     cart_count=Product_Home.cart_count;

        Log.e("yoyo_",""+cart_count);

     //   cart_count = PaginationAdapter.cartModels.size();
        loadFirstPage();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        paginationAdapter = new Category_ProductAdapter(Category_Product.this, this);
        productRecyclerView.setLayoutManager(linearLayoutManager);
        productRecyclerView.setAdapter(paginationAdapter);
        productRecyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                loadNextPage();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

    }

    private void check_cout() {
        Gson gson = new Gson();
        String json = shapref.getString("Cart", "");
        if (json.isEmpty()) {
            //    Toast.makeText(Product_Detail.this,"There is something error",Toast.LENGTH_LONG).show();
        } else {
            Type type = new TypeToken<ArrayList<ProductImage>>() {
            }.getType();
            Category_ProductAdapter.cartModels_Category = gson.fromJson(json, type);
        }
        cart_count = Category_ProductAdapter.cartModels_Category.size();
        Category_Product.this.invalidateOptionsMenu();
    }

    private void loadNextPage() {
        Log.d("cat_page", String.valueOf(currentPage));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://skcc.luqmansoftwares.com/api/category/" + selected_id + "/?page=" + currentPage)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        Fetch_Categories_Products api = retrofit.create(Fetch_Categories_Products.class);
        Call<String> call = api.getProducts("");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                paginationAdapter.removeLoadingFooter();
                isLoading = false;
                int id;
                List<ProductModel> results = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    JSONArray jsonarray = jsonObject.getJSONArray("data");
                    if (jsonarray.length() > 1) {
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

                            if (price_category.equals("normal")) {
                                price = jsonobject.getString("price");
                                product_class.setPrice(price);
                            } else if (price_category.equals("p1")) {
                                if (jsonobject.getString("p1").equals("null")) {
                                    price = jsonobject.getString("price");
                                    product_class.setPrice(price);
                                } else {
                                    price = jsonobject.getString("p1");
                                    product_class.setPrice(price);
                                }

                            } else if (price_category.equals("p2")) {
                                if (jsonobject.getString("p2").equals("null")) {
                                    price = jsonobject.getString("price");
                                    product_class.setPrice(price);
                                } else {
                                    price = jsonobject.getString("p2");
                                    product_class.setPrice(price);
                                }
                            } else if (price_category.equals("p3")) {
                                if (jsonobject.getString("p3").equals("null")) {
                                    price = jsonobject.getString("price");
                                    product_class.setPrice(price);
                                } else {
                                    price = jsonobject.getString("p3");
                                    product_class.setPrice(price);
                                }


                            } else if (price_category.equals("p4")) {
                                if (jsonobject.getString("p4").equals("null")) {
                                    price = jsonobject.getString("price");
                                    product_class.setPrice(price);
                                } else {
                                    price = jsonobject.getString("p4");
                                    product_class.setPrice(price);
                                }


                            } else if (price_category.equals("p5")) {
                                if (jsonobject.getString("p5").equals("null")) {
                                    price = jsonobject.getString("price");
                                    product_class.setPrice(price);
                                } else {
                                    price = jsonobject.getString("p5");
                                    product_class.setPrice(price);
                                }
                            }
                            brand_id = jsonobject.getString("brand_id");
                            if (brand_id != "null") {
                                brand = jsonobject.getJSONObject("brand").getString("name");
                                product_class.setBrand(brand);
                            } else {
                                product_class.setBrand("");
                            }
                            product_image = jsonobject.getString("product_image");
                            product_class.setProduct_image("https://skcc.luqmansoftwares.com/uploads/products/" + product_image);
                            results.add(product_class);
                        }
                        paginationAdapter.addAll(results);
                        if (currentPage != TOTAL_PAGES) {
                            paginationAdapter.addLoadingFooter();
                        } else {
                            isLastPage = true;
                        }
                        nn++;
                    } else {
                        Toast.makeText(getApplicationContext(), "No More Product", Toast.LENGTH_SHORT);
                        isLastPage = true;
                        //                  progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //            progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                progressBar.setVisibility(View.GONE);
                //      progressDialog.dismiss();

            }
        });
    }


    private void loadFirstPage() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://skcc.luqmansoftwares.com/api/category/" + selected_id + "/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        Fetch_Categories_Products api = retrofit.create(Fetch_Categories_Products.class);


        Call<String> call = api.getProducts("");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.body().length() > 1) {
                    int id;
                    List<ProductModel> results = new ArrayList<>();
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
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
                            weight = jsonobject.getString("weight");
                            product_class.setWeight(weight);
                            size = jsonobject.getString("size");
                            product_class.setSize(size);

                            if (price_category.equals("normal")) {
                                price = jsonobject.getString("price");
                                product_class.setPrice(price);
                            } else if (price_category.equals("p1")) {
                                if (jsonobject.getString("p1").equals("null")) {
                                    price = jsonobject.getString("price");
                                    product_class.setPrice(price);
                                } else {
                                    price = jsonobject.getString("p1");
                                    product_class.setPrice(price);
                                }

                            } else if (price_category.equals("p2")) {
                                if (jsonobject.getString("p2").equals("null")) {
                                    price = jsonobject.getString("price");
                                    product_class.setPrice(price);
                                } else {
                                    price = jsonobject.getString("p2");
                                    product_class.setPrice(price);
                                }

                            } else if (price_category.equals("p3")) {
                                if (jsonobject.getString("p3").equals("null")) {
                                    price = jsonobject.getString("price");
                                    product_class.setPrice(price);
                                } else {
                                    price = jsonobject.getString("p3");
                                    product_class.setPrice(price);
                                }


                            } else if (price_category.equals("p4")) {
                                if (jsonobject.getString("p4").equals("null")) {
                                    price = jsonobject.getString("price");
                                    product_class.setPrice(price);
                                } else {
                                    price = jsonobject.getString("p4");
                                    product_class.setPrice(price);
                                }


                            } else if (price_category.equals("p5")) {
                                if (jsonobject.getString("p5").equals("null")) {
                                    price = jsonobject.getString("price");
                                    product_class.setPrice(price);
                                } else {
                                    price = jsonobject.getString("p5");
                                    product_class.setPrice(price);
                                }
                            }

                            brand_id = jsonobject.getString("brand_id");
                            if (brand_id != "null") {
                                brand = jsonobject.getJSONObject("brand").getString("name");
                                product_class.setBrand(brand);
                            } else {
                                product_class.setBrand("");
                            }

                            product_image = jsonobject.getString("product_image");
                            product_class.setProduct_image("https://skcc.luqmansoftwares.com/uploads/products/" + product_image);
                            results.add(product_class);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (results.size()<=0){
                        layout_2.setVisibility(View.VISIBLE);
                        layout_1.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                    }else {
                        layout_2.setVisibility(View.GONE);
                        layout_1.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        paginationAdapter.addAll(results);
                    }
                    if (currentPage <= TOTAL_PAGES) paginationAdapter.addLoadingFooter();
                    else isLastPage = true;
                    nn++;
                    //        progressDialog.dismiss();

                } else {
                    Toast.makeText(Category_Product.this, getResources().getString(R.string.nomore), Toast.LENGTH_SHORT);
                    //      progressDialog.dismiss();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressBar.setVisibility(View.GONE);

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.cart_action);
        menuItem.setIcon(Converter.convertLayoutToImage(Category_Product.this, cart_count, R.drawable.ic_shopping_cart_white_24dp));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.cart_action:
                if (cart_count < 1) {
                    Toast.makeText(Category_Product.this, getResources().getString(R.string.noitem), Toast.LENGTH_SHORT).show();
                    cart_count = 0;
                } else {
                    startActivity(new Intent(Category_Product.this, CartActivity.class));
                }
                break;
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void addCartItemView() {
        //addItemToCartMethod();

    }

    @Override
    public void updateCartCount(Context context) {
        Category_Product.this.invalidateOptionsMenu();
    }

    private static void disablefor1sec(final View v) {
        try {
            v.setEnabled(false);
            v.setAlpha((float) 0.8);
            v.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        v.setEnabled(true);
                        v.setAlpha((float) 1.0);
                    } catch (Exception e) {
                        Log.d("disablefor1sec", " Exception while un hiding the view : " + e.getMessage());
                    }
                }
            }, 2000);
        } catch (Exception e) {
            Log.d("disablefor1sec", " Exception while hiding the view : " + e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        check_cout();
    }
}
