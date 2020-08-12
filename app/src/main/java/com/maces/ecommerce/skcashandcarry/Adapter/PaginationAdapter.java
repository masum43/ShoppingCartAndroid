package com.maces.ecommerce.skcashandcarry.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
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

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.maces.ecommerce.skcashandcarry.Model.ProductImage;
import com.maces.ecommerce.skcashandcarry.Model.ProductModel;
import com.maces.ecommerce.skcashandcarry.R;
import com.maces.ecommerce.skcashandcarry.View.CartActivity;
import com.maces.ecommerce.skcashandcarry.View.Home;
import com.maces.ecommerce.skcashandcarry.View.Product_Detail;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static ArrayList<ProductModel> productsArray;
    public static ProductImage cartModel;
    public static ArrayList<ProductImage> cartModels = new ArrayList<ProductImage>();

    Context context;
    private CallBackUs mCallBackus;
    private HomeCallBack homeCallBack;

    private List<ProductModel> movieList;
    private static final int LOADING = 0;
    private static final int ITEM = 1;
    private final int[] cartCounter = {1};//{(arrayListImage.get(position).getStocks())};
    private List<ProductModel> dictionaryWords;
    private List<ProductModel> filteredList;
    private ArrayList<String> names;
    SharedPreferences sharedPreferences;
    private boolean isLoadingAdded = false;
    private List<ProductModel> exampleListFull;

    public PaginationAdapter(Context context, HomeCallBack mCallBackus) {
        this.context = context;
        exampleListFull = new ArrayList<>();
        this.movieList = filteredList;
        movieList = new LinkedList<>();
        this.homeCallBack = mCallBackus;

        sharedPreferences = context.getSharedPreferences("HomeItem", MODE_PRIVATE);


    }

    public PaginationAdapter() {

    }


    public void setMovieList(List<ProductModel> movieList) {
        this.movieList = movieList;
    }


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
    public int getItemCount() {

        return movieList == null ? 0 : movieList.size();


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {

        ProductModel movie = movieList.get(i);
        switch (getItemViewType(i)) {
            case ITEM:
                MovieViewHolder movieViewHolder = (MovieViewHolder) holder;
                movieViewHolder.productName.setText(movieList.get(i).getName());
                movieViewHolder.tvProduct_Brand.setText(movieList.get(i).getBrand());
                if (movieList.get(i).getWeight().equals("null")) {
                    movieViewHolder.tvSize.setText("");
                } else {
                    movieViewHolder.tvSize.setText(movieList.get(i).getWeight());
                }

                movieViewHolder.tvProduct_Price.setText("€ " + movieList.get(i).getPrice());
                movieViewHolder.layout_product.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        disablefor1sec(view);
                        Intent product_intent = new Intent(context, Product_Detail.class);
                        product_intent.putExtra("Name", movieList.get(i).getName());
                        product_intent.putExtra("Description", movieList.get(i).getDescription());
                        product_intent.putExtra("Brand", movieList.get(i).getBrand());
                        product_intent.putExtra("Price", movieList.get(i).getPrice());
                        product_intent.putExtra("Weight", movieList.get(i).getWeight());
                        product_intent.putExtra("Size", movieList.get(i).getSize());
                        product_intent.putExtra("image", movieList.get(i).getProduct_image());
                        product_intent.putExtra("product_id", movieList.get(i).getId());
                        context.startActivity(product_intent);
                    }
                });
                Picasso.get().load(movieList.get(i).getProduct_image()).into(movieViewHolder.icon);
                movieViewHolder.tvProduct_addtoCart.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        disablefor1sec(view);
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
                        quantity.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                        quantity.requestFocus();
                        quantity.setText(String.valueOf(1));
                        quantity.setSelection(quantity.getText().length());
                        quantity.setOnKeyListener(new View.OnKeyListener() {
                            @Override
                            public boolean onKey(View v, int keyCode, KeyEvent event) {
                                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                                if (keyCode == KeyEvent.KEYCODE_DEL) {
                                    if (quantity.getText().toString().equals("") || quantity.getText().toString().equals(" ")) {

                                        quantity.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                                        quantity.setText(String.valueOf(1));
                                    } else if (Integer.parseInt(quantity.getText().toString()) == 1) {
                                        quantity.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                                        quantity.setText(String.valueOf(1));

                                    }
                                }
                                return false;
                            }
                        });

                        quantity.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                // TODO Auto-generated method stub
                                InputMethodManager keyboard = (InputMethodManager)
                                        context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                keyboard.showSoftInput(quantity, 0);
                            }
                        }, 200); //use
                        // {(arrayListImage.get(position).getStocks())};
                        cartDecrement.setEnabled(false);
                        cartDecrement.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (Integer.parseInt(quantity.getText().toString()) == 1) {
                                    Toast.makeText(context, "cant add less than 0", Toast.LENGTH_SHORT);
                                } else {
                                    cartCounter[0] -= 1;
                                    quantity.setText(String.valueOf(cartCounter[0]));
                                }

                            }
                        });
                        cartIncrement.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cartDecrement.setEnabled(true);
                                cartCounter[0] += 1;
                                quantity.setText(String.valueOf(cartCounter[0]));


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

                                if (quantity.getText().toString().equals("")) {
                                    Toast.makeText(context, "" + context.getResources().getString(R.string.cannot), Toast.LENGTH_SHORT).show();
                                    quantity.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                                    quantity.requestFocus();
                                    quantity.setText(String.valueOf(1));
                                    quantity.setSelection(quantity.getText().length());
                                }

                                if (Integer.parseInt((quantity.getText().toString())) > 5000) {
                                    Toast.makeText(context, "" + context.getResources().getString(R.string.maxquantity), Toast.LENGTH_SHORT).show();
                                    quantity.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                                    quantity.requestFocus();
                                    quantity.setSelection(quantity.getText().length());
                                } else if (Integer.parseInt((quantity.getText().toString())) == 0) {
                                    Toast.makeText(context, "" + context.getResources().getString(R.string.cannot), Toast.LENGTH_SHORT).show();
                                    quantity.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                                    quantity.requestFocus();
                                    quantity.setText(String.valueOf(1));
                                    quantity.setSelection(quantity.getText().length());
                                } else if (quantity.getText().toString().length() < 1) {
                                    Toast.makeText(context, "" + context.getResources().getString(R.string.cannot), Toast.LENGTH_SHORT).show();
                                    quantity.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                                    quantity.requestFocus();
                                    quantity.setText(String.valueOf(1));
                                    quantity.setSelection(quantity.getText().length());
                                }
                                //Toast.makeText(context, String.valueOf(cartCounter[0]) + "", Toast.LENGTH_SHORT).show();

                                else {
                                    // from these line of code we add items in cart
                                    cartModel = new ProductImage();
                                    cartModel.setProductQuantity(Integer.parseInt((quantity.getText().toString())));
                                    cartModel.setProductPrice(movieList.get(i).getPrice());
                                    cartModel.setProductName(movieList.get(i).getName());
                                    cartModel.setId(movieList.get(i).getId());
                                    cartModel.setProductImage(movieList.get(i).getProduct_image());
                                    int q = Integer.parseInt((quantity.getText().toString()));
                                    cartModel.setTotalCash(q *
                                            Double.parseDouble(movieList.get(i).getPrice()));
                                    Log.d("pos", String.valueOf(i));

                                    cartModels.add(cartModel);

//
                                    // from these lines of code we update badge count value
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


    public interface CallBackUs {
        void addCartItemView();
    }

    // this interface creates for call the invalidateoptionmenu() for refresh the menu item
    public interface HomeCallBack {
        boolean onCreateOptionsMenu(Menu menu);

        void updateCartCount(Context context);
    }

    public void filterList(List<ProductModel> filteredList) {
        movieList = filteredList;
        notifyDataSetChanged();
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


    public void filterList(ArrayList<ProductModel> filteredList) {
        movieList = filteredList;
        notifyDataSetChanged();
    }


    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView productName;
        TextView tvProduct_Title;
        TextView tvProduct_Brand;
        TextView tvProduct_Price;
        TextView tvProduct_addtoCart, tvSize;
        LinearLayout layout_product;

        MovieViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.tvTitle);
            tvProduct_Brand = itemView.findViewById(R.id.tvBrand);
            tvProduct_Price = itemView.findViewById(R.id.tvPrice);
            tvProduct_addtoCart = itemView.findViewById(R.id.tvaddtoCart);
            tvSize = itemView.findViewById(R.id.tvSize);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            layout_product = (LinearLayout) itemView.findViewById(R.id.layout_product);
        }

    }

    private int getIndex(ProductModel playlist) {
        for (int i = 0; i < cartModels.size(); i++) {
            if (cartModels.get(i).getProductName().equals(playlist.getName())) {
                return i;
            }
        }
        return -1;
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar progressBar;

        LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.loadmore_progress);
            progressBar.setVisibility(View.GONE);
        }
    }

    InputFilter filter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (Character.isWhitespace(source.charAt(i))) {
                    return "1";
                }
            }
            return null;
        }

    };

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
