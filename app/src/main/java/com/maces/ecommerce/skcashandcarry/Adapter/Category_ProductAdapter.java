package com.maces.ecommerce.skcashandcarry.Adapter;

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
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.maces.ecommerce.skcashandcarry.View.Product_Detail;
import com.squareup.picasso.Picasso;
import com.maces.ecommerce.skcashandcarry.Model.ProductImage;
import com.maces.ecommerce.skcashandcarry.Model.ProductModel;
import com.maces.ecommerce.skcashandcarry.R;
import com.maces.ecommerce.skcashandcarry.View.CartActivity;
import com.maces.ecommerce.skcashandcarry.View.Category_Product;
import com.maces.ecommerce.skcashandcarry.View.Home;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.maces.ecommerce.skcashandcarry.Adapter._PaginationAdapter.cartModels;

public class Category_ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private SharedPreferences cart_class;
    private Context context;
    private List<ProductModel> movieList;
    private static final int LOADING = 0;
    private static final int ITEM = 1;
    public static ProductImage cartModel;
    final int[] cartCounter = {1};//{(arrayListImage.get(position).getStocks())};
    int qty = 1;
    public static HomeCallBack homeCallBack;
    public static ArrayList<ProductImage> cartModels_Category = new ArrayList<ProductImage>();
    private boolean isLoadingAdded = false;

    public Category_ProductAdapter(Context context, HomeCallBack mCallBackus) {
        this.context = context;
        movieList = new LinkedList<>();
        homeCallBack = mCallBackus;
        cart_class = context.getSharedPreferences("Cart", MODE_PRIVATE);
    }

    public void setMovieList(List<ProductModel> movieList) {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.product_content, parent, false);
                viewHolder = new MovieViewHolder(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingViewHolder(viewLoading);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {

        ProductModel movie = movieList.get(i);
        switch (getItemViewType(i)) {
            case ITEM:
                MovieViewHolder movieViewHolder = (MovieViewHolder) holder;
                movieViewHolder.productName.setText(movieList.get(i).getName());

                movieViewHolder.tvProduct_Brand.setText(movieList.get(i).getBrand());


                if(movieList.get(i).getWeight().equals("null"))
                {
                    movieViewHolder.tvSize.setText("");
                }
                else
                {
                    movieViewHolder.tvSize.setText(movieList.get(i).getWeight());
                }

                movieViewHolder.tvProduct_Price.setText("€ "+movieList.get(i).getPrice());
                Picasso.get().load(movieList.get(i).getProduct_image()).into(movieViewHolder.icon);
                movieViewHolder.layout_product.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        disablefor1sec(view);
                        Intent product_intent=new Intent(context, Product_Detail.class);
                        product_intent.putExtra("Name",movieList.get(i).getName());
                        product_intent.putExtra("Description",movieList.get(i).getDescription());
                        product_intent.putExtra("Brand",movieList.get(i).getBrand());
                        product_intent.putExtra("Price",movieList.get(i).getPrice());
                        product_intent.putExtra("Weight",movieList.get(i).getWeight());
                        product_intent.putExtra("Size",movieList.get(i).getSize());
                        product_intent.putExtra("image",movieList.get(i).getProduct_image());
                        product_intent.putExtra("product_id",movieList.get(i).getId());
                        context.startActivity(product_intent);
                    }
                });
                movieViewHolder.tvProduct_addtoCart.setOnClickListener(new View.OnClickListener() {
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
                                if (!TextUtils.isEmpty(s)){
                                    qty = Integer.parseInt(s.toString());
                                }

                            }
                        });

                        final int[] cartCounter = {0};//{(arrayListImage.get(position).getStocks())};
                        if (TextUtils.isEmpty(quantity.getText().toString())) {
                            qty = Integer.parseInt(quantity.getText().toString());
                        } else {
                            qty = 1;
                        }
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
                        });     viewCartDialog.setOnClickListener(new View.OnClickListener() {
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
                                    cartModel.setProductPrice(movieList.get(i).getPrice());
                                    cartModel.setProductName(movieList.get(i).getName());
                                    cartModel.setId(movieList.get(i).getId());
                                    cartModel.setProductImage(movieList.get(i).getProduct_image());
                                    cartModel.setTotalCash(qty * Double.parseDouble(movieList.get(i).getPrice()));
                                    Log.d("pos", String.valueOf(i));

                                    cartModels.add(cartModel);

                                    // from these lines of code we update badge count value
                                    Home.cart_count = 0;
                                    Category_Product.cart_count = 0;

                                    for (int i = 0; i < cartModels.size(); i++) {
                                        for (int j = i + 1; j < cartModels.size(); j++) {
                                            if (cartModels.get(i).getProductImage().equals(cartModels.get(j).getProductImage())) {
                                                cartModels.get(i).setProductQuantity(cartModels.get(j).getProductQuantity());
                                                cartModels.get(i).setId(cartModels.get(j).getId());
                                                cartModels.get(i).setTotalCash(cartModels.get(j).getTotalCash());
                                                cartModels.get(i).setProductName(cartModels.get(j).getProductName());
                                                //          cartModels.get(i).setImageIdSlide(cartModels.get(j).getImageIdSlide());
                                                cartModels.remove(j);
                                                j--;
                                                Log.d("remove", String.valueOf(cartModels.size()));

                                            }
                                        }
                                    }

                                    Gson gson = new Gson();
                                    String jsonn = gson.toJson(cartModels);
                                    SharedPreferences.Editor editorr = cart_class.edit();
                                    editorr.putString("Cart", jsonn);
                                    editorr.apply();

                                    Home.cart_count = cartModels.size();
                                    Category_Product.cart_count = cartModels.size();
                                    Log.e("cat_", String.valueOf(cartModels.size()));
                                    // from this interface method calling we show the updated value of cart cout in badge
                                    homeCallBack.updateCartCount(context);
                                    dialog.dismiss();
                                }
                            }
                        });


                    }
                });
                break;

            case LOADING:
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return movieList == null ? 0 : movieList.size();
    }

    public interface CallBackUs {
        void addCartItemView();
    }
    // this interface creates for call the invalidateoptionmenu() for refresh the menu item
    public interface HomeCallBack {
        boolean onCreateOptionsMenu(Menu menu);
        void updateCartCount(Context context);
    }
    @Override
    public int getItemViewType(int position) {
        return (position == movieList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new ProductModel());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movieList.size() - 1;
        ProductModel result = getItem(position);

        if (result != null) {
            movieList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(ProductModel movie) {
        movieList.add(movie);
        notifyItemInserted(movieList.size() - 1);
    }

    public void addAll(List<ProductModel> moveResults) {
        for (ProductModel result : moveResults) {
            add(result);
        }
    }

    private ProductModel getItem(int position) {
        return movieList.get(position);
    }


    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView productName;
        TextView tvProduct_Title;
        TextView tvProduct_Brand;
        TextView tvProduct_Price;
        TextView tvProduct_addtoCart;
        TextView tvSize;
        CardView layout_product;

        MovieViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.tvTitle);
            tvProduct_Brand = itemView.findViewById(R.id.tvBrand);
            tvProduct_Price = itemView.findViewById(R.id.tvPrice);
            tvProduct_addtoCart = itemView.findViewById(R.id.tvaddtoCart);
            tvSize = itemView.findViewById(R.id.tvSize);

            icon = itemView.findViewById(R.id.icon);
            layout_product=itemView.findViewById(R.id.layout_product);
        }

    }
    private int getIndex(ProductModel playlist) {
        for (int i = 0; i < cartModels_Category.size(); i++) {
            if (cartModels_Category.get(i).getProductName().equals(playlist.getName())) {
                return i;
            }
        }
        return -1;
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar progressBar;

        LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar =  itemView.findViewById(R.id.loadmore_progress);

        }
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
}
