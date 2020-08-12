package com.maces.ecommerce.skcashandcarry.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.maces.ecommerce.skcashandcarry.Adapter.ProductDetailAdapter;
import com.maces.ecommerce.skcashandcarry.Converter;
import com.maces.ecommerce.skcashandcarry.Model.Cart_Class;
import com.maces.ecommerce.skcashandcarry.Model.ProductModel;
import com.maces.ecommerce.skcashandcarry.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Product_Detail extends AppCompatActivity implements ProductDetailAdapter.CallBackUs, ProductDetailAdapter.HomeCallBack {

    public static int cart_count = 0;
    protected RecyclerView recyclerView_Product;
    public ArrayList<ProductModel> arrayList = new ArrayList<>();
    protected String name, brand, price, weight, size, description, product_image;
    int product_id;
    protected ProductModel productModel;
    protected ProductDetailAdapter productAdapter;
    SharedPreferences sharedPreferences;
    List<Cart_Class> arrPackageData;
    public static int temp_value = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        recyclerView_Product = findViewById(R.id.recyclerView_Product);
        productModel = new ProductModel();
        sharedPreferences = getSharedPreferences("CartItem", MODE_PRIVATE);
        name = Objects.requireNonNull(getIntent().getExtras()).getString("Name");
        weight = getIntent().getExtras().getString("Weight");
        price = getIntent().getExtras().getString("Price");
        size = getIntent().getExtras().getString("Size");
        brand = getIntent().getExtras().getString("Brand");
        product_image = getIntent().getExtras().getString("image");
        description = getIntent().getExtras().getString("Description");
        product_id = getIntent().getExtras().getInt("product_id", 0);
        productModel.setName(name);
        productModel.setPrice(price);
        productModel.setBrand(brand);
        if (description == null) {
            productModel.setDescription(" ");

        } else {
            productModel.setDescription(description);
        }
        productModel.setId(product_id);
        productModel.setWeight(weight);
        productModel.setSize(size);
        productModel.setProduct_image(product_image);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#000000\">" + name + "</font>")));
        arrayList.add(productModel);
        arrPackageData = new ArrayList<>();
        productAdapter = new ProductDetailAdapter(arrayList, Product_Detail.this, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(Product_Detail.this, 1, LinearLayoutManager.VERTICAL, false);
        recyclerView_Product.setLayoutManager(gridLayoutManager);
        recyclerView_Product.setAdapter(productAdapter);
//getPrefernceValue();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.cart_action);
        menuItem.setIcon(Converter.convertLayoutToImage(Product_Detail.this, cart_count, R.drawable.ic_shopping_cart_white_24dp));
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
                    //     Toast.makeText(Product_Detail.this, "there is no item in cart", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(Product_Detail.this, CartActivity.class));
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
        invalidateOptionsMenu();
    }

    @Override
    public void finishIt() {
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        invalidateOptionsMenu();
    }

}