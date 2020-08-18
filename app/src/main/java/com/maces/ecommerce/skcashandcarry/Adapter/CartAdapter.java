package com.maces.ecommerce.skcashandcarry.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.maces.ecommerce.skcashandcarry.Fragments.Product_Home;
import com.maces.ecommerce.skcashandcarry.Interfaces.CounterCallBack;
import com.maces.ecommerce.skcashandcarry.Model.ProductImage;
import com.maces.ecommerce.skcashandcarry.R;
import com.maces.ecommerce.skcashandcarry.View.Home;

import java.math.BigDecimal;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.maces.ecommerce.skcashandcarry.View.CartActivity.grandTotal;
import static com.maces.ecommerce.skcashandcarry.View.CartActivity.grandTotalplus;
import static com.maces.ecommerce.skcashandcarry.View.CartActivity.temparraylist;


/**
 * Created by Deependra Singh Patel on 8/5/19.
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private ArrayList<ProductImage> cartModelArrayList;
    private Context context;
    private SharedPreferences cart_class, home_pref, prf;
    CounterCallBack counterCallBack;

    public CartAdapter(ArrayList<ProductImage> cartModelArrayList, Context context,CounterCallBack counterCallBack) {
        this.context = (Context) context;
        this.counterCallBack=counterCallBack;
        this.cartModelArrayList = cartModelArrayList;
        home_pref = context.getSharedPreferences("HomeItem", MODE_PRIVATE);
        cart_class = context.getSharedPreferences("Cart", MODE_PRIVATE);
    }


    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);
        return new CartAdapter.ViewHolder(v);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(final CartAdapter.ViewHolder holder, final int position) {
        // holder.productCartImage.setImageResource(R.drawable.burger);
        BigDecimal bd = new BigDecimal(cartModelArrayList.get(position).getTotalCash());
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        holder.productCartPrice.setText("€ " + String.valueOf(bd.doubleValue()));
        holder.productCartCode.setText(cartModelArrayList.get(position).getProductName());
        holder.productCartQuantity.setText(String.valueOf(cartModelArrayList.get(position).getProductQuantity()));

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.loader);
        requestOptions.error(R.drawable.loader);
        Log.d("imageurl", String.valueOf(cartModelArrayList.get(position).getProductImage()));
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(cartModelArrayList.get(position).getProductImage()).into(holder.productCartImage);

        //for remove single item in cart and update the total value and list
        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    counterCallBack.CountCheck();
                    if (cartModelArrayList.size() == 1) {
                        cartModelArrayList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, cartModelArrayList.size());
                        grandTotalplus = 0;
                        grandTotal.setText("€ " + String.format("%.2f", grandTotalplus));
                        _PaginationAdapter.cartModels.clear();
                        _PaginationAdapter.cartModels.addAll(temparraylist);
                     //   Product_Home.cartModels.addAll(temparraylist);

                        Gson gson = new Gson();
                        String json = gson.toJson(temparraylist);
                        SharedPreferences.Editor editor = home_pref.edit();
                        editor.putString("HomeItem", json);
                        editor.apply();

                        String jsonn = gson.toJson(temparraylist);
                        SharedPreferences.Editor editorr = cart_class.edit();
                        editorr.putString("Cart", jsonn);
                        editorr.apply();
                        Home.cart_count = temparraylist.size();
                        Product_Home.cart_count = temparraylist.size();

                    }
                    if (cartModelArrayList.size() > 0) {
                        cartModelArrayList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, cartModelArrayList.size());
                        grandTotalplus = 0;
                        for (int i = 0; i < temparraylist.size(); i++) {
                            grandTotalplus = grandTotalplus + temparraylist.get(i).getTotalCash();
                        }

                        Log.e("totalcashthegun", String.valueOf(grandTotalplus));
                        grandTotal.setText("€ " + String.format("%.2f", grandTotalplus));
                        _PaginationAdapter.cartModels.clear();
                        _PaginationAdapter.cartModels.addAll(temparraylist);
                  //      Product_Home.cartModels.addAll(temparraylist);

                        Gson gson = new Gson();
                        String json = gson.toJson(temparraylist);
                        SharedPreferences.Editor editor = home_pref.edit();
                        editor.putString("HomeItem", json);

                        editor.apply();
                        String jsonn = gson.toJson(temparraylist);
                        Log.d("json_cart",json);
                        SharedPreferences.Editor editorr = cart_class.edit();
                        editorr.putString("Cart", jsonn);
                        editorr.apply();
                        Home.cart_count = temparraylist.size();
                        Product_Home.cart_count = temparraylist.size();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        // increment quantity and update quamtity and total cash

        holder.cartIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grandTotalplus = 0;
                int cartUpdateCounter = (cartModelArrayList.get(position).getProductQuantity());
                Log.d("counterthegun", String.valueOf(cartModelArrayList.get(position).getProductQuantity()));
                cartUpdateCounter += 1;
                cartModelArrayList.get(position).setProductQuantity((cartUpdateCounter));
                double product_price = Double.parseDouble(cartModelArrayList.get(position).getProductPrice());
                double cash = (product_price) * (cartUpdateCounter);
                holder.productCartQuantity.setText("" + cartUpdateCounter);
                cartModelArrayList.get(position).setTotalCash(cash);
                BigDecimal bd = new BigDecimal(cash);
                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                holder.productCartPrice.setText("€ " + String.valueOf(bd.doubleValue()));
                for (int i = 0; i < temparraylist.size(); i++) {
                    grandTotalplus = grandTotalplus + temparraylist.get(i).getTotalCash();
                }
                Log.d("totalcashthegun", String.valueOf(grandTotalplus));
                grandTotal.setText("€ " + String.format("%.2f", grandTotalplus));
                _PaginationAdapter.cartModels.clear();
                _PaginationAdapter.cartModels.addAll(temparraylist);
            //    Product_Home.cartModels.addAll(temparraylist);
                Gson gson = new Gson();
                String json = gson.toJson(temparraylist);
                SharedPreferences.Editor editor = home_pref.edit();
                editor.putString("HomeItem", json);
                editor.apply();
                String jsonn = gson.toJson(temparraylist);
                SharedPreferences.Editor editorr = cart_class.edit();
                editorr.putString("Cart", jsonn);
                editorr.apply();
                Home.cart_count = temparraylist.size();
                Product_Home.cart_count = temparraylist.size();
            }
        });

        // decrement quantity and update quamtity and total cash
        holder.cartDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //total_cash=0;
                grandTotalplus = 0;
                int cartUpdateCounter = (cartModelArrayList.get(position).getProductQuantity());
                Log.e("counterthegun", String.valueOf(cartModelArrayList.get(position).getProductQuantity()));

                if (cartUpdateCounter <= 1) {
                    Toast.makeText(context, context.getResources().getString(R.string.cannot), Toast.LENGTH_SHORT).show();
                } else {
                    cartUpdateCounter -= 1;
                    cartModelArrayList.get(position).setProductQuantity((cartUpdateCounter));
                    holder.productCartQuantity.setText(String.valueOf(cartModelArrayList.get(position).getProductQuantity()));
                    double cash = (Double.parseDouble(cartModelArrayList.get(position).getProductPrice()) * (cartModelArrayList.get(position).getProductQuantity()));
                    cartModelArrayList.get(position).setTotalCash(cash);
                    BigDecimal bd = new BigDecimal(cash);
                    bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                    holder.productCartPrice.setText("€ " + bd.doubleValue());
                    for (int i = 0; i < temparraylist.size(); i++) {
                        grandTotalplus = grandTotalplus + temparraylist.get(i).getTotalCash();
                    }
                    Log.d("totalcashthegun", String.valueOf(grandTotalplus));
                    grandTotal.setText("€ " + String.format("%.2f", grandTotalplus));
                    _PaginationAdapter.cartModels.clear();
                    _PaginationAdapter.cartModels.addAll(temparraylist);
                  //  Product_Home.cartModels.addAll(temparraylist);
                    Gson gson = new Gson();
                    String json = gson.toJson(temparraylist);
                    SharedPreferences.Editor editor = home_pref.edit();
                    editor.putString("HomeItem", json);
                    editor.apply();
                    String jsonn = gson.toJson(temparraylist);
                    SharedPreferences.Editor editorr = cart_class.edit();
                    editorr.putString("Cart", jsonn);
                    editorr.apply();
                    Home.cart_count = temparraylist.size();
                    Product_Home.cart_count = temparraylist.size();

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("sizecart", String.valueOf(cartModelArrayList.size()));
        return cartModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productCartImage, cartIncrement, cartDecrement, deleteItem;
        TextView productCartCode, productCartPrice;
        EditText productCartQuantity;

        public ViewHolder(View itemView) {
            super(itemView);
            productCartImage = itemView.findViewById(R.id.list_image_cart);
            deleteItem = itemView.findViewById(R.id.delete_item_from_cart);
            productCartCode = itemView.findViewById(R.id.product_cart_code);
            productCartPrice = itemView.findViewById(R.id.product_cart_price);
            productCartQuantity = itemView.findViewById(R.id.cart_product_quantity_tv);
            cartDecrement = itemView.findViewById(R.id.cart_decrement);
            cartIncrement = itemView.findViewById(R.id.cart_increment);


        }
    }
}
