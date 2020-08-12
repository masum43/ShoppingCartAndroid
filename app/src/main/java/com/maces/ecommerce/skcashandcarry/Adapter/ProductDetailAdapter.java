package com.maces.ecommerce.skcashandcarry.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;
import com.maces.ecommerce.skcashandcarry.Model.ProductImage;
import com.maces.ecommerce.skcashandcarry.Model.ProductModel;
import com.maces.ecommerce.skcashandcarry.R;
import com.maces.ecommerce.skcashandcarry.View.CartActivity;
import com.maces.ecommerce.skcashandcarry.View.Home;
import com.maces.ecommerce.skcashandcarry.View.Product_Detail;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.maces.ecommerce.skcashandcarry.Adapter._PaginationAdapter.cartModels;


public class ProductDetailAdapter extends RecyclerView.Adapter<ProductDetailAdapter.ViewHolder> {
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;
    private int qty = 1;
    private static ArrayList<ProductModel> productsArray;
    private static ArrayList<ProductImage> cartModels_ProductDetail = new ArrayList<ProductImage>();
    private static ProductImage cartModel;
    private Context context;
    private SharedPreferences sharedPreferences;
    private CallBackUs mCallBackus;
    private static HomeCallBack homeCallBack;

    public ProductDetailAdapter(ArrayList<ProductModel> productsArray, Context context, HomeCallBack mCallBackus) {
        ProductDetailAdapter.productsArray = productsArray;
        this.context = context;
        homeCallBack = mCallBackus;
        Product_Detail.cart_count = Home.cart_count;
        sharedPreferences = context.getSharedPreferences("HomeItem", MODE_PRIVATE);
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @NonNull
    @Override
    public ProductDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_detail_content, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductDetailAdapter.ViewHolder viewHolder, final int i) {
        //viewHolder.productImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.bag));

        if (productsArray.get(0).getPrice().equals("null")) {
            viewHolder.tvPrice.setText(" ");
        } else {
            viewHolder.tvPrice.setText("€ " + productsArray.get(i).getPrice());
        }
        if (productsArray.get(i).getWeight().equals("null")) {
            viewHolder.tvWeight.setText(" ");
        } else {
            viewHolder.tvWeight.setText(productsArray.get(i).getWeight());
        }
        if (productsArray.get(i).getSize().equals("null")) {
            viewHolder.tvSize.setText(" ");
        } else {
            viewHolder.tvSize.setText(productsArray.get(i).getSize());

        }
        if (productsArray.get(i).getBrand().equals("null")) {
            viewHolder.tvBrand.setText(" ");
        } else {
            viewHolder.tvBrand.setText(productsArray.get(i).getBrand());
        }
        if (productsArray.get(i).getDescription().equals("null")) {
            viewHolder.tvDescription.setText(" ");
        } else {

            viewHolder.tvDescription.setText(productsArray.get(i).getDescription());

        }

        Picasso.get().load(productsArray.get(i).getProduct_image()).into(viewHolder.cover);

        viewHolder.btn_GoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) context).finish();
            }
        });
        viewHolder.btn_GotoCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(context);
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
                            Toast.makeText(context, "cant add less than 1", Toast.LENGTH_SHORT);
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
                            Toast.makeText(context, "Quantity Less then 5000", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                viewCartDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        context.startActivity(new Intent(context, CartActivity.class));
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
                            cartModel.setProductPrice(productsArray.get(i).getPrice());
                            cartModel.setProductName(productsArray.get(i).getName());
                            cartModel.setId(productsArray.get(i).getId());
                            cartModel.setProductImage(productsArray.get(i).getProduct_image());
                            cartModel.setTotalCash(qty *
                                    Double.parseDouble(productsArray.get(i).getPrice()));
                            Log.d("pos", String.valueOf(i));
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
                            homeCallBack.updateCartCount(context);
                            homeCallBack.finishIt();
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsArray.size();
    }

    public interface CallBackUs {
        void addCartItemView();
    }

    // this interface creates for call the invalidateoptionmenu() for refresh the menu item
    public interface HomeCallBack {
        boolean onCreateOptionsMenu(Menu menu);
        void updateCartCount(Context context);
        void finishIt();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescription, tvBrand, tvPrice, tvWeight, tvSize, tvName;
        Button tvincrement, btn_GoBack, btn_GotoCheckout;

        EditText edit_quantity;
        ZoomageView cover;

        ViewHolder(View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.cover);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvBrand = itemView.findViewById(R.id.tvBrand);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvWeight = itemView.findViewById(R.id.tvWeight);
            tvSize = itemView.findViewById(R.id.tvSize);
            tvName = itemView.findViewById(R.id.tvName);
            btn_GotoCheckout = itemView.findViewById(R.id.btn_GotoCheckout);
            btn_GoBack = itemView.findViewById(R.id.btn_GoBack);
        }
    }

    private int getIndex(ProductModel playlist) {
        for (int i = 0; i < cartModels_ProductDetail.size(); i++) {
            if (cartModels_ProductDetail.get(i).getProductName().equals(playlist.getName())) {
                return i;
            }
        }
        return -1;
    }
}
