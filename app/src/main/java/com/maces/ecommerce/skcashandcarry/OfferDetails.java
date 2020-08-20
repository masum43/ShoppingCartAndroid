package com.maces.ecommerce.skcashandcarry;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.tntkhang.fullscreenimageview.library.FullScreenImageViewActivity;
import com.maces.ecommerce.skcashandcarry.Adapter.PaginationAdapter;
import com.maces.ecommerce.skcashandcarry.Adapter.ProductDetailAdapter;
import com.maces.ecommerce.skcashandcarry.Model.ProductImage;
import com.maces.ecommerce.skcashandcarry.Model.ProductModel;
import com.maces.ecommerce.skcashandcarry.View.CartActivity;
import com.maces.ecommerce.skcashandcarry.View.Home;
import com.maces.ecommerce.skcashandcarry.View.Product_Detail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.maces.ecommerce.skcashandcarry.Adapter._PaginationAdapter.cartModels;

public class OfferDetails extends AppCompatActivity {
    private ImageView iv;
    private TextView titleTv, priceTv, descTv;
    private int qty = 1;
    private static ProductImage cartModel;
    //private static ArrayList<ProductModel> productsArray;
    String url;
    String title;
    String price;
    String desc;
    String id;
    private static HomeCallBack homeCallBack;
    ArrayList<String> uriString = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);

        //cart_class = context.getSharedPreferences("Cart", MODE_PRIVATE);

        iv = findViewById(R.id.iv);
        titleTv = findViewById(R.id.tv_title);
        priceTv = findViewById(R.id.tv_price);
        descTv = findViewById(R.id.tv_desc);


        id = getIntent().getStringExtra("id");
        url = getIntent().getStringExtra("url");
        title= getIntent().getStringExtra("title");
        price = getIntent().getStringExtra("price");
        desc = getIntent().getStringExtra("desc");

        priceTv.setText(price);
        titleTv.setText(title);
        descTv.setText(desc);
        Picasso.get().load(url).into(iv);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uriString.clear();
                uriString.add(url);

                Intent fullImageIntent = new Intent(OfferDetails.this, FullScreenImageViewActivity.class);
                // uriString is an ArrayList<String> of URI of all images
                fullImageIntent.putExtra(FullScreenImageViewActivity.URI_LIST_DATA, uriString);
                // pos is the position of image will be showned when open
                fullImageIntent.putExtra(FullScreenImageViewActivity.IMAGE_FULL_SCREEN_CURRENT_POS, 0);
                startActivity(fullImageIntent);
            }
        });
    }

    public void cartClick(View view) {
        final Dialog dialog = new Dialog(this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog_item_quantity_update);
        // Set dialog title
        dialog.setTitle("Custom Dialog");
        final ImageView cartDecrement = dialog.findViewById(R.id.cart_decrement);
        ImageView cartIncrement = dialog.findViewById(R.id.cart_increment);

        TextView updateQtyDialog = dialog.findViewById(R.id.update_quantity_dialog);
        TextView viewCartDialog = dialog.findViewById(R.id.view_cart_button_dialog);
        final EditText quantity = dialog.findViewById(R.id.cart_product_quantity_tv);
        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    qty = Integer.parseInt(s.toString());
                }

            }
        });
        if (TextUtils.isEmpty(quantity.getText().toString())) {
            qty = Integer.parseInt(quantity.getText().toString());
        } else {
            qty = 1;
        }


        final int[] cartCounter = {0};//{(arrayListImage.get(position).getStocks())};

        cartDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qty > 1) {
                    qty--;
                    cartCounter[0] -= 1;
                    quantity.setText(String.valueOf(qty));
                } else {
                    Toast.makeText(OfferDetails.this, "cant add less than 1", Toast.LENGTH_SHORT);
                }

            }
        });
        cartIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    cartDecrement.setEnabled(true);
                if (qty < 5000) {
                    qty++;
                    cartCounter[0] += qty;
                    quantity.setText(String.valueOf(qty));
                } else {
                    Toast.makeText(OfferDetails.this, "Quantity Less then 5000", Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewCartDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(OfferDetails.this, CartActivity.class));
            }
        });

        dialog.show();
        updateQtyDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((TextUtils.isEmpty(quantity.getText().toString()) || (qty < 1))) {
                    qty = 1;
                    quantity.setText("" + 1);
                } else {
                    // from these line of code we add items in cart
                    cartModel = new ProductImage();
                    cartModel.setProductQuantity(qty);
                    cartModel.setProductPrice(price);
                    cartModel.setProductName(title);
                    cartModel.setId(Integer.valueOf(id));
                    cartModel.setProductImage(url);
                    cartModel.setTotalCash(qty *
                            Double.parseDouble(price));
                    //Log.d("pos", String.valueOf(i));
                    cartModels.add(cartModel);
                    // from these lines of code we update badge count value
                    Product_Detail.cart_count = 0;
                    Home.cart_count = 0;
                    for (int i = 0; i < cartModels.size(); i++) {
                        for (int j = i + 1; j < cartModels.size(); j++) {
                            if (cartModels.get(i).getProductImage().equals(cartModels.get(j).getProductImage())) {
                                cartModels.get(i).setProductQuantity(cartModels.get(j).getProductQuantity());
                                cartModels.get(i).setTotalCash(cartModels.get(j).getTotalCash());
                                cartModels.get(i).setId(cartModels.get(j).getId());
                                cartModels.get(i).setProductName(cartModels.get(j).getProductName());
                                //          cartModels.get(i).setImageIdSlide(cartModels.get(j).getImageIdSlide());
                                cartModels.remove(j);
                                j--;
                                Log.d("remove", String.valueOf(cartModels.size()));

                            }
                        }
                    }
                    Home.cart_count = cartModels.size();
                    Product_Detail.cart_count = cartModels.size();
                    // from this interface method calling we show the updated value of cart cout in badge
                    //homeCallBack.updateCartCount(OfferDetails.this);
                    //homeCallBack.finishIt();
                    dialog.dismiss();
                    finish();
                }
            }
        });
    }

    public void goBackClick(View view) {
        finish();
    }

    public void backOfferClick(View view) {
        finish();
    }

    public interface HomeCallBack {
        boolean onCreateOptionsMenu(Menu menu);
        void updateCartCount(Context context);
        void finishIt();
    }
}