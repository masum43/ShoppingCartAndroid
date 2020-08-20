package com.maces.ecommerce.skcashandcarry.Fragments;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.maces.ecommerce.skcashandcarry.Adapter.Categories_Adapter;
import com.maces.ecommerce.skcashandcarry.Adapter.Category_ProductAdapter;
import com.maces.ecommerce.skcashandcarry.Adapter.PaginationScrollListener;
import com.maces.ecommerce.skcashandcarry.Adapter.SearchAdapter;
import com.maces.ecommerce.skcashandcarry.Adapter.SliderAdapter;
import com.maces.ecommerce.skcashandcarry.Adapter._PaginationAdapter;
import com.maces.ecommerce.skcashandcarry.Constant;
import com.maces.ecommerce.skcashandcarry.Converter;
import com.maces.ecommerce.skcashandcarry.Interfaces.Fetch_Categories;
import com.maces.ecommerce.skcashandcarry.Interfaces.Fetch_Categories_Products;
import com.maces.ecommerce.skcashandcarry.Interfaces.Fetch_Slider_Images;
import com.maces.ecommerce.skcashandcarry.Interfaces.Get_UserInfor;
import com.maces.ecommerce.skcashandcarry.Interfaces.OnBackPressed;
import com.maces.ecommerce.skcashandcarry.Interfaces.Sliders;
import com.maces.ecommerce.skcashandcarry.Interfaces.Webservices;
import com.maces.ecommerce.skcashandcarry.LockableNestedScrollView;
import com.maces.ecommerce.skcashandcarry.MainActivity;
import com.maces.ecommerce.skcashandcarry.Model.Cart_Class;
import com.maces.ecommerce.skcashandcarry.Model.Categories_Class;
import com.maces.ecommerce.skcashandcarry.Model.Movie;
import com.maces.ecommerce.skcashandcarry.Model.ProductImage;
import com.maces.ecommerce.skcashandcarry.Model.ProductModel;
import com.maces.ecommerce.skcashandcarry.Model.ProductService;
import com.maces.ecommerce.skcashandcarry.Model.SliderItem;
import com.maces.ecommerce.skcashandcarry.MySharedPref;
import com.maces.ecommerce.skcashandcarry.R;
import com.maces.ecommerce.skcashandcarry.View.CartActivity;
import com.maces.ecommerce.skcashandcarry.View.Home;
import com.maces.ecommerce.skcashandcarry.View.Login;
import com.maces.ecommerce.skcashandcarry.View.Product_Detail;
import com.maces.ecommerce.skcashandcarry.View.Splash;
import com.maces.ecommerce.skcashandcarry.View.order_placed;
import com.maces.ecommerce.skcashandcarry.api_volley.Search;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import fr.ganfra.materialspinner.MaterialSpinner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.content.Context.MODE_PRIVATE;
import static com.maces.ecommerce.skcashandcarry.Adapter._PaginationAdapter.cartModels;

public class Product_Home extends Fragment implements _PaginationAdapter.CallBackUs, _PaginationAdapter.HomeCallBack, OnBackPressed, SearchAdapter.HomeCallBack {

    public static ArrayList<ProductModel> arrayList = new ArrayList<>();
    public static int cart_count = 0;
    //   public static ArrayList<ProductImage> cartModels = new ArrayList<>();
    private LinearLayout slider, category_title, categoryRecycler;
    private String name, brand, price, brand_id, category_id, description, p1, p2, p3, p4, p5, weight, size, product_image;
    private RecyclerView productRecyclerView;
    protected ProgressDialog progressDialog;
    public static String access_token, token_type, price_category;
    private SharedPreferences prf, shapref;
    private SliderView sliderView;
    protected SliderAdapter adapter;
    int n = 2;
    private ArrayList<Movie> country;
    private ArrayList<String> listcountry;
    private TextView tv_no;
    private List<SliderItem> sliderItemList;
    private SharedPreferences sharedPreferences;
    private List<Cart_Class> arrPackageData;
    private EditText searchAutoComplete;
    private ArrayList<Categories_Class> categories_classArrayList;
    private RecyclerView recyclerView_Category;
    private ProgressBar progressBar;
    private int nn = 1;

    private _PaginationAdapter _paginationAdapter;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = PAGE_START;

    private LinearLayout mLinearLayout;
    private ArrayList<String> allCityList = new ArrayList<>();
    private ArrayList<Movie> searchAl = new ArrayList<>();

    //as
    private LockableNestedScrollView nestedScrollView;
    RecyclerView searchRv;
    public static boolean lastPagReached = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_product_home, container, false);
        sliderView = root.findViewById(R.id.imageSlider);
        slider = root.findViewById(R.id.layout_slider);
        category_title = root.findViewById(R.id.layout_category_title);
        categoryRecycler = root.findViewById(R.id.layout_categoryRecycler);
        mLinearLayout = root.findViewById(R.id.linearLayout_focus);
        progressBar = root.findViewById(R.id.progressbar);

        //as
        nestedScrollView = root.findViewById(R.id.scroll_view);
        searchRv = root.findViewById(R.id.search_products);

        sliderItemList = new ArrayList<>();
        tv_no = root.findViewById(R.id.tv_no);
        country = new ArrayList<>();
        listcountry = new ArrayList<>();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle((Html.fromHtml("<font color=\"#000000\">" + getString(R.string.menu_home) + "</font>")));
        //  progressBar = root.findViewById(R.id.progressbar);
        progressDialog = new ProgressDialog(getActivity());
        shapref = getActivity().getSharedPreferences("Cart", MODE_PRIVATE);
        progressDialog.setMessage(getResources().getString(R.string.Fetching));
        progressDialog.show();
        sharedPreferences = getActivity().getSharedPreferences("Cart", MODE_PRIVATE);
        arrPackageData = new ArrayList<>();
        prf = getActivity().getSharedPreferences("LoginPref", MODE_PRIVATE);
        if (Objects.requireNonNull(prf.getString("token_typeKey", "")).length() > 0) {
            token_type = prf.getString("token_typeKey", "");
            access_token = prf.getString("access_tokenKey", "");
        } else {
            token_type = Login.token_type_val;
            access_token = Login.access_token_val;
        }
        categories_classArrayList = new ArrayList<>();
        recyclerView_Category = root.findViewById(R.id.category_Recycler);
        Fetch_Categories();

        Get_Userinfo();

        Fetch_Slider();
        Gson gson = new Gson();
        String json = shapref.getString("Cart", "");
        if (json.isEmpty()) {
            //    Toast.makeText(Product_Detail.this,"There is something error",Toast.LENGTH_LONG).show();
        } else {
            Type type = new TypeToken<ArrayList<ProductImage>>() {
            }.getType();
            _PaginationAdapter.cartModels = gson.fromJson(json, type);
        }
        Home.cart_count = _PaginationAdapter.cartModels.size();

        progressBar = root.findViewById(R.id.progressbar);
        productRecyclerView = root.findViewById(R.id.grid_products);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        _paginationAdapter = new _PaginationAdapter(getActivity(), this);
        productRecyclerView.setLayoutManager(linearLayoutManager);
        productRecyclerView.setAdapter(_paginationAdapter);

//        productRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                LinearLayoutManager layoutManager=LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
//                int totalItemCount = layoutManager.getItemCount();
//                int lastVisible = layoutManager.findLastVisibleItemPosition();
//
//                boolean endHasBeenReached = lastVisible + 5 >= totalItemCount;
//                if (totalItemCount > 0 && endHasBeenReached) {
//                    //you have reached to the bottom of your recycler view
//                    currentPage += 1;
//                    loadNextPage();
//                }
//            }
//        });





        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())
                {
                    //if (currentPage<TOTAL_PAGES)
                    //{
                    Log.d("last=page", String.valueOf(lastPagReached));
                    if (!lastPagReached)
                    {
                        loadNextPage();
                        Log.d("PPP",String.valueOf(currentPage));
                    }

                    //}
                    //else progressBar.setVisibility(View.GONE);

                }
            }
        });


       /* productRecyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                loadNextPage();
                Log.d("LOAD","Yes"+String.valueOf(currentPage));
            }
            @Override
            public boolean isLastPage() {
                progressBar.setVisibility(View.GONE);
                return isLastPage;
            }
            @Override
            public boolean isLoading() {
                progressBar.setVisibility(View.VISIBLE);
                return isLoading;
            }
        });*/


        //  productAdapter = new ProductAdapter(arrayList, getActivity(), this);
        searchAutoComplete = root.findViewById(R.id.searchAutoComplete);


        searchAutoComplete.setMaxWidth(Integer.MAX_VALUE);
        searchAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                searchAutoComplete.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cancel_black_24dp, 0);
//                slider.setVisibility(View.GONE);
//                categoryRecycler.setVisibility(View.GONE);
//                category_title.setVisibility(View.GONE);
                if (!s.toString().isEmpty())
                {
                    nestedScrollView.setVisibility(View.GONE);
                    searchRv.setVisibility(View.VISIBLE);
                    filter(s.toString());
                }
                else
                {
                    nestedScrollView.setVisibility(View.VISIBLE);
                    searchRv.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                try {
                    searchAutoComplete.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            final int DRAWABLE_RIGHT = 2;
                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                if (searchAutoComplete.getCompoundDrawables()[2] != null) {
                                    if (event.getRawX() >= (searchAutoComplete.getRight() - searchAutoComplete.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                                        searchAutoComplete.setText("");
                                        searchAutoComplete.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                        searchAutoComplete.clearFocus();
                                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                                        slider.setVisibility(View.VISIBLE);
                                        categoryRecycler.setVisibility(View.VISIBLE);
                                        category_title.setVisibility(View.VISIBLE);
                                        //    loadFirstPage();
                                        return true;
                                    }
                                }

                            }
                            return false;
                        }
                    });
                } catch (Exception ignored) {
                }
            }
        });

        return root;
    }



    private void parseResult(JSONArray jsonArray) {
        for (int i=0;i<jsonArray.length();i++)
        {
            try {
                JSONObject object = jsonArray.getJSONObject(i);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        //do not give the editbox focus automatically when activity starts
        searchAutoComplete.clearFocus();
        mLinearLayout.requestFocus();
    }

    private void Fetch_Categories() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Fetch_Categories.URL)

                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        Fetch_Categories api = retrofit.create(Fetch_Categories.class);
        Call<String> call = api.getCategories();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body());

                        String jsonresponse = response.body();
                        parse_Categories(jsonresponse);

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

    private void parse_Categories(String response) {

        String name, url;
        int id;
        try {
            JSONArray jsonarray = new JSONArray(response);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                Categories_Class categories_class = new Categories_Class();
                id = jsonobject.getInt("id");
                categories_class.setId(id);
                name = jsonobject.getString("name");
                categories_class.setName(name);
                url = jsonobject.getString("category_image");
                categories_class.setCategory_image("https://skcc.luqmansoftwares.com/uploads/categories/" + url);
                categories_classArrayList.add(categories_class);
            }
            recyclerView_Category.setAdapter(new Categories_Adapter(getActivity(), categories_classArrayList));
            recyclerView_Category.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Objects.requireNonNull(getActivity()).getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.cart_action);
        menuItem.setIcon(Converter.convertLayoutToImage(getActivity(), cart_count, R.drawable.ic_shopping_cart_white_24dp));


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
                Toast.makeText(getActivity(), getResources().getString(R.string.noitem), Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(getActivity(), CartActivity.class));
            }
        } else {
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
        Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
    }


    private void Get_Userinfo() {
        Get_UserInfor jsonPostService;
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Accept", "application/json");
        if (token_type.toString().length() < 1) {
            headers.put("Authorization", Login.token_type_val + " " + Login.access_token_val);
        } else {
            headers.put("Authorization", token_type + " " + access_token);
        }
        JsonObject jsonObject = new JsonObject();
        if (token_type.length() < 1) {
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
                        progressDialog.dismiss();
                        assert response.body() != null;
                        price_category = response.body().get("price_category").getAsString();
                        loadFirstPage();

                    } else {
                        progressDialog.dismiss();
                        //   Toast.makeText(getActivity(), "Error:"+response.errorBody(), Toast.LENGTH_LONG).show(); // do something with that
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

    private void Fetch_Slider() {
        progressDialog.show();
        Log.d("token_type",MySharedPref.getTokenType(getContext()));
        Log.d("token_",MySharedPref.getToken(getContext()));

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", MySharedPref.getTokenType(getContext()) + " " + MySharedPref.getToken(getContext()));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Sliders.URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Sliders jsonPostService = retrofit.create(Sliders.class);
        Call<JsonArray> call = jsonPostService.ApiName(headers);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                Log.d("sliders_status", String.valueOf(response));
                if (response.isSuccessful())
                {
                    if (response.body()!=null)
                    {
                        Log.d("sliders_body", response.body().toString());
                        String jsonresponse = response.body().toString();
                        parse_Slider(jsonresponse);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void parse_Slider(String response) {
        String url,price,description,title;
        int id;

        try {
            JSONArray jsonarray = new JSONArray(response);

            Log.d("jsonArray",String.valueOf(jsonarray));


            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);

                //getting
                SliderItem sliderItem = new SliderItem();
                url = jsonobject.getString("banner_image");
                title = jsonobject.getString("title");
                price = jsonobject.getString("price");
                description = jsonobject.getString("description");
                id = jsonobject.getInt("product_id");


                //setting
                sliderItem.setId(id);
                sliderItem.setTitle(title);
                sliderItem.setPrice(price);
                sliderItem.setDescription(description);
                //sliderItem.setImageUrl("https://skcc.luqmansoftwares.com/uploads/products/" + url);
                sliderItem.setImageUrl(url);
                sliderItemList.add(sliderItem);
            }
            adapter = new SliderAdapter(getActivity(), sliderItemList);

            sliderView.setSliderAdapter(adapter);

            sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
            sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
            sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
            sliderView.setIndicatorSelectedColor(Color.WHITE);
            sliderView.setIndicatorUnselectedColor(Color.GRAY);
            sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
            sliderView.startAutoCycle();

        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }

    }

    private void getPrefernceValue() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("CartItem", "");
        assert json != null;
        if (Objects.requireNonNull(json).isEmpty()) {
            //   Toast.makeText(,"There is something error",Toast.LENGTH_LONG).show();
        } else {
            Type type = new TypeToken<ArrayList<Cart_Class>>() {
            }.getType();
            arrPackageData = gson.fromJson(json, type);
            parse(json);
        }
    }

    private void parse(String your_json_string) {
        JSONArray jArr = null;
        int quan = 0;
        try {

            jArr = new JSONArray(your_json_string);
            for (int count = 0; count < jArr.length(); count++) {
                JSONObject obj = jArr.getJSONObject(count);
                quan += obj.getInt("productQuantity");
                //so on
            }
            Product_Detail.cart_count = quan;
            MainActivity.cart_count = quan;
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public List<ProductModel> GetListData() {
        return new ArrayList<>(arrayList);
    }

    @Override
    public void onBackPressed() {
        Objects.requireNonNull(getActivity()).finish();
    }


    private void loadNextPage() {
        productRecyclerView.setNestedScrollingEnabled(false);
        nestedScrollView.setScrollingEnabled(false);
        currentPage++;

        if (currentPage >1 )
        {
            Log.d("HHH","yes");
            progressBar.setVisibility(View.VISIBLE);

//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {


                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://skcc.luqmansoftwares.com/api/fetch-products"+ "/?page=" + currentPage)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .build();

                    Fetch_Categories_Products api = retrofit.create(Fetch_Categories_Products.class);
                    Call<String> call = api.getProducts("");
                    call.enqueue(new Callback<String>() {

                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            _paginationAdapter.removeLoadingFooter();
                            isLoading = false;
                            int id;



                            List<Movie> results = new LinkedList<>();
                            List<Movie> tempresults = new ArrayList<>();
                            //if (currentPage%3 == 0) results.clear();
                                try {
                                JSONObject jsonObject = new JSONObject(response.body().toString());
                                //int to = jsonObject.getInt("to");
                                Log.d("current_page", String.valueOf(currentPage));
                                //Log.d("current_to", String.valueOf(to));

                                    JSONArray jsonarray = jsonObject.getJSONArray("data");
                                    if (jsonarray.length() > 0) {
                                        for (int i = 0; i < jsonarray.length(); i++) {
                                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                                            Movie product_class = new Movie();

                                            id = jsonobject.getInt("id");
                                            product_class.setId(id);
                                            if (jsonobject.getString("name").equals("null"))
                                                name=" ";
                                            else
                                            name = jsonobject.getString("name");
                                            product_class.setName(name);

                                            if(jsonobject.getString("category_id").equals("null"))
                                                category_id ="1";
                                            else
                                                category_id = jsonobject.getString("category_id");
                                            product_class.setCategory_id(category_id);

                                            if (jsonobject.getString("description").equals("null"))
                                                description="Not provided by the author";
                                            else
                                            description = jsonobject.getString("description");
                                            product_class.setDescription(description);
//                                            if (description.equals("null")) {
//                                                product_class.setDescription(" ");
//                                            } else {
//                                                product_class.setDescription(description);
//                                            }

                                            if (jsonobject.getString("weight").equals("null"))
                                                weight = " ";
                                            else
                                            weight = jsonobject.getString("weight");
                                            product_class.setWeight(weight);

//                                            if (weight.equals("null")) {
//                                                product_class.setWeight(" ");
//                                            } else {
//                                                product_class.setWeight(weight);
//                                            }

                                            if (jsonobject.getString("size").equals("null"))
                                            size =" ";
                                            else
                                            size = jsonobject.getString("size");
                                            product_class.setSize(size);
//                                            if (size.equals("null")) {
//                                                product_class.setSize("");
//                                            } else {
//                                                product_class.setSize(size);
//                                            }


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
                                            brand_id = jsonobject.getString("brand_id");
                                            if (!brand_id.equals("null")) {
                                                brand = jsonobject.getJSONObject("brand").getString("name");
                                                product_class.setBrand(brand);
                                            } else {
                                                product_class.setBrand("");
                                            }
                                            product_image = jsonobject.getString("product_image");
                                            product_class.setProduct_image("https://skcc.luqmansoftwares.com/uploads/products/" + product_image);
                                            results.add(product_class);
                                            //searchAl.add(product_class);
                                        }

//                                        _paginationAdapter.addAll(results);
//                                        //if (currentPage != TOTAL_PAGES)
//                                        //  _paginationAdapter.addLoadingFooter();
//                                        //else isLastPage = true;
//                                        //country.addAll(results);
//                                        _paginationAdapter.addLoadingFooter();
//                                        _paginationAdapter.stop_progress();
//                                        progressBar.setVisibility(View.GONE);
//                                        nestedScrollView.setScrollingEnabled(true);
//                                        nn++;


                                        if (results.size()<=0){
                                            progressBar.setVisibility(View.GONE);
                                        }else {
                                            progressBar.setVisibility(View.GONE);
                                            _paginationAdapter.addAll(results);
                                        }
                                        _paginationAdapter.addLoadingFooter();
                                        nestedScrollView.setScrollingEnabled(true);

                                    }
                                    else {
                                        lastPagReached = true;
                                        //_paginationAdapter.stop_progress();
                                        progressBar.setVisibility(View.GONE);
                                        tv_no.setVisibility(View.VISIBLE);
                                        tv_no.setText("No more product found!");
                                        nestedScrollView.setScrollingEnabled(true);
                                        //    Toast.makeText(getActivity(), getResources().getString(R.string.no_pro), Toast.LENGTH_SHORT).show();
                                    }
                                //}
//                                else
//                                {
//                                    lastPagReached = true;
//                                    progressBar.setVisibility(View.GONE);
//                                    tv_no.setVisibility(View.VISIBLE);
//                                    tv_no.setText("No more product found!");
//                                }


                            } catch (JSONException e) {
                                progressBar.setVisibility(View.GONE);
                                e.printStackTrace();
                                Log.d("error_msg_server",e.getMessage());
                                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }



                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            _paginationAdapter.stop_progress();
                            progressBar.setVisibility(View.GONE);
                            Log.d("error_msg",t.getMessage());
                        }
                    });
//                }
//            }, 2500);



            //currentPage += 1;

        }

        else
        {
            _paginationAdapter.stop_progress();
            progressBar.setVisibility(View.GONE);
            tv_no.setVisibility(View.VISIBLE);
            tv_no.setText("No more product to show!!");
        }

    }


    private void loadFirstPage() {
        if (currentPage == 1)
        {
            Log.d("current_page",String.valueOf(currentPage));
            progressBar.setVisibility(View.VISIBLE);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://skcc.luqmansoftwares.com/api/fetch-products"+ "/?page="+currentPage)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();

            Fetch_Categories_Products api = retrofit.create(Fetch_Categories_Products.class);
            Call<String> call = api.getProducts("");
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    int id;
                    List<Movie> results = new ArrayList<>();
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        JSONArray jsonarray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            Movie product_class = new Movie();

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
                    } catch (JSONException e) {
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                    }

                    if (results.size()<=0){
                        progressBar.setVisibility(View.GONE);
                    }else {
                        progressBar.setVisibility(View.GONE);
                        _paginationAdapter.addAll(results);
                        country.addAll(results);
                    }
                    _paginationAdapter.addLoadingFooter();
                    nn++;

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        //currentPage += 1;
        Log.d("CCC",String.valueOf(currentPage));
    }



    private void filter(String text) {
        //_paginationAdapter.removeLoadingFooter();
        searchProduct(getContext(),text);

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



    private void searchProduct(final Context c, final String data)

    {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Constant.searchApi+"?name="+data,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("saerch",response);

                        try {
                            setSearchAdapter(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            protected Map<String,String> getParams() throws AuthFailureError
            {
                Map<String,String> params=new HashMap<String, String>();
                //params.put("name",data);
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(c);
        requestQueue.add(stringRequest);
    }

    private void setSearchAdapter(String response) throws JSONException {

        ArrayList<Movie> newSearchFilteAl = new ArrayList<>();
        newSearchFilteAl.clear();
        JSONArray jsonarray = new JSONArray(response);
        if (jsonarray.length() > 1) {
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                Movie product_class = new Movie();

                int id = jsonobject.getInt("id");
                product_class.setId(id);
                name = jsonobject.getString("name");
                product_class.setName(name);
                category_id = jsonobject.getString("category_id");
                product_class.setCategory_id(category_id);
                description = jsonobject.getString("description");
                if (description.equals("null")) {
                    product_class.setDescription(" ");
                } else {
                    product_class.setDescription(description);
                }

                weight = jsonobject.getString("weight");
                if (weight.equals("null")) {
                    product_class.setWeight(" ");
                } else {
                    product_class.setWeight(weight);
                }

                size = jsonobject.getString("size");
                if (size.equals("null")) {
                    product_class.setSize("");
                } else {
                    product_class.setSize(size);
                }


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
                brand_id = jsonobject.getString("brand_id");
                if (!brand_id.equals("null")) {
                    brand = jsonobject.getJSONObject("brand").getString("name");
                    product_class.setBrand(brand);
                } else {
                    product_class.setBrand("");
                }
                product_image = jsonobject.getString("product_image");
                product_class.setProduct_image("https://skcc.luqmansoftwares.com/uploads/products/" + product_image);
                newSearchFilteAl.add(product_class);
            }

            SearchAdapter searchAdapter;
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            searchAdapter = new SearchAdapter(getActivity(), this);


            searchRv.setLayoutManager(linearLayoutManager);
            searchRv.setAdapter(searchAdapter);

            searchAdapter.addAll(newSearchFilteAl);
        }
    }
}