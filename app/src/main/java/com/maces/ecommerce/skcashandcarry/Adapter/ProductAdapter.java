package com.maces.ecommerce.skcashandcarry.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.maces.ecommerce.skcashandcarry.Model.ProductImage;
import com.maces.ecommerce.skcashandcarry.Model.ProductModel;
import com.maces.ecommerce.skcashandcarry.R;
import com.maces.ecommerce.skcashandcarry.View.CartActivity;
import com.maces.ecommerce.skcashandcarry.View.Home;
import com.maces.ecommerce.skcashandcarry.View.Product_Detail;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> implements Filterable {

    private List<ProductModel> productsArray;
    public static ArrayList<ProductImage> cartModels = new ArrayList<ProductImage>();
    public static ProductImage cartModel;
    private List<ProductModel> productListFiltered;
    private Context context;

    private CallBackUs mCallBackus;
    public static HomeCallBack homeCallBack;
    private SharedPreferences sharedPreferences;
    private List<ProductModel> dictionaryWords;
    private List<ProductModel> filteredList;

    public ProductAdapter(List<ProductModel> filteredList, List<ProductModel> dictionaryWords, Context context, HomeCallBack mCallBackus) {
        this.filteredList = filteredList;
        this.dictionaryWords = dictionaryWords;
        this.context = context;
        this.homeCallBack = mCallBackus;
        this.productsArray = filteredList;
        sharedPreferences = context.getSharedPreferences("HomeItem", MODE_PRIVATE);


    }

    public ProductAdapter() {

    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_content, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductAdapter.ViewHolder viewHolder, final int i) {
        //viewHolder.productImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.bag));
        viewHolder.productName.setText(productsArray.get(i).getName());
        viewHolder.tvProduct_Brand.setText(productsArray.get(i).getBrand());
        viewHolder.tvProduct_Price.setText("€ " + productsArray.get(i).getPrice());
        Picasso.get().load(productsArray.get(i).getProduct_image()).resize(55, 90).into(viewHolder.icon);

        viewHolder.layout_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disablefor1sec(view);
                Intent product_intent = new Intent(context, Product_Detail.class);
                product_intent.putExtra("Name", productsArray.get(i).getName());
                product_intent.putExtra("Description", productsArray.get(i).getDescription());
                product_intent.putExtra("Brand", productsArray.get(i).getBrand());
                product_intent.putExtra("Price", productsArray.get(i).getPrice());
                product_intent.putExtra("Weight", productsArray.get(i).getWeight());
                product_intent.putExtra("Size", productsArray.get(i).getSize());
                product_intent.putExtra("image", productsArray.get(i).getProduct_image());
                product_intent.putExtra("product_id", productsArray.get(i).getId());
                context.startActivity(product_intent);
                Toast.makeText(context, "Product Adapter ", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.tvProduct_addtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText quantity;
                if (cartModels.size() > 0) {
                    int index = getIndex(productsArray.get(i));

                    if (index > -1) {
                        Toast.makeText(context, context.getResources().getString(R.string.already), Toast.LENGTH_SHORT).show();
                    } else {
                        final Dialog dialog = new Dialog(context);
                        // Include dialog.xml file
                        dialog.setContentView(R.layout.dialog_item_quantity_update);
                        // Set dialog title
                        dialog.setTitle("Custom Dialog");

                        final ImageView cartDecrement = dialog.findViewById(R.id.cart_decrement);
                        ImageView cartIncrement = dialog.findViewById(R.id.cart_increment);
                        TextView updateQtyDialog = dialog.findViewById(R.id.update_quantity_dialog);
                        TextView viewCartDialog = dialog.findViewById(R.id.view_cart_button_dialog);
                        quantity = dialog.findViewById(R.id.cart_product_quantity_tv);
                        cartModel.setProductQuantity(Integer.parseInt((quantity.getText().toString())));
                        cartIncrement.setVisibility(View.INVISIBLE);
                        cartDecrement.setVisibility(View.INVISIBLE);
                        quantity.setEnabled(false);
                        final int[] cartCounter = {0};//{(arrayListImage.get(position).getStocks())};
                        cartDecrement.setEnabled(false);
                        cartDecrement.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (cartCounter[0] == 1) {
                                    Toast.makeText(context, context.getResources().getString(R.string.cannot), Toast.LENGTH_SHORT).show();
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
                                } else {
                                    cartCounter[0] += 1;
                                    //  Toast.makeText(context, String.valueOf(cartCounter[0]) + "", Toast.LENGTH_SHORT).show();

                                    // from these line of code we add items in cart
                                    cartModel = new ProductImage();
                                    cartModel.setId(productsArray.get(i).getId());
                                    cartModel.setProductName(productsArray.get(i).getName());
                                    cartModel.setProductQuantity((cartCounter[0]));
                                    cartModel.setProductPrice(productsArray.get(i).getPrice());
                                    cartModel.setProductImage(productsArray.get(i).getProduct_image());
                                    double Val = Double.parseDouble(productsArray.get(i).getPrice());
                                    Double FinalP = (Double) (Val * cartCounter[0]);
                                    cartModel.setTotalCash(FinalP);
                                    Log.d("pos", String.valueOf(i));

                                    cartModels.add(cartModel);
                                    Gson gson = new Gson();
                                    String json = gson.toJson(cartModels);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("HomeItem", json);
                                    editor.apply();
//
                                    // from these lines of code we update badge count value
                                    Home.cart_count = 0;
                                    for (int i = 0; i < cartModels.size(); i++) {
                                        for (int j = i + 1; j < cartModels.size(); j++) {
                                            if (cartModels.get(i).getProductImage().equals(cartModels.get(j).getProductImage())) {
                                                cartModels.get(i).setProductQuantity(cartModels.get(j).getProductQuantity());
                                                cartModels.get(i).setTotalCash(cartModels.get(j).getTotalCash());
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
                } else {
                    final Dialog dialog = new Dialog(context);
                    // Include dialog.xml file
                    dialog.setContentView(R.layout.dialog_item_quantity_update);
                    // Set dialog title
                    dialog.setTitle("Custom Dialog");
                    final ImageView cartDecrement = dialog.findViewById(R.id.cart_decrement);
                    ImageView cartIncrement = dialog.findViewById(R.id.cart_increment);
                    TextView updateQtyDialog = dialog.findViewById(R.id.update_quantity_dialog);
                    TextView viewCartDialog = dialog.findViewById(R.id.view_cart_button_dialog);
                    quantity = dialog.findViewById(R.id.cart_product_quantity_tv);
                    quantity.setText(String.valueOf(1));
                    cartIncrement.setVisibility(View.GONE);
                    cartDecrement.setVisibility(View.GONE);
                    quantity.setEnabled(false);
                    final int[] cartCounter = {0};//{(arrayListImage.get(position).getStocks())};
                    cartDecrement.setEnabled(false);
                    cartDecrement.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (cartCounter[0] == 1) {
                                Toast.makeText(context, context.getResources().getString(R.string.cannot), Toast.LENGTH_SHORT).show();
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
                            } else {
                                cartCounter[0] += 1;
                                cartModel = new ProductImage();
                                cartModel.setId(productsArray.get(i).getId());
                                cartModel.setProductName(productsArray.get(i).getName());
                                cartModel.setProductQuantity((cartCounter[0]));
                                cartModel.setProductPrice(productsArray.get(i).getPrice());
                                cartModel.setProductImage(productsArray.get(i).getProduct_image());
                                double Val = Double.parseDouble(productsArray.get(i).getPrice());
                                Double FinalP = (Double) (Val * cartCounter[0]);
                                cartModel.setTotalCash(FinalP);
                                Log.d("pos", String.valueOf(i));

                                cartModels.add(cartModel);
                                Gson gson = new Gson();
                                String json = gson.toJson(cartModels);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("HomeItem", json);
                                editor.apply();
//
                                // from these lines of code we update badge count value
                                Home.cart_count = 0;
                                for (int i = 0; i < cartModels.size(); i++) {
                                    for (int j = i + 1; j < cartModels.size(); j++) {
                                        if (cartModels.get(i).getProductImage().equals(cartModels.get(j).getProductImage())) {
                                            cartModels.get(i).setProductQuantity(cartModels.get(j).getProductQuantity());
                                            cartModels.get(i).setTotalCash(cartModels.get(j).getTotalCash());
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
            }
        });
    }

    @Override
    public int getItemCount() {

        if (filteredList != null) {
            return filteredList.size();
        } else {
            return 0;
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredList = dictionaryWords;
                } else {
                    List<ProductModel> filteredList_Product = new ArrayList<>();
                    for (ProductModel movie : dictionaryWords) {
                        if (movie.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList_Product.add(movie);
                        }
                    }
                    filteredList = filteredList_Product;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredList = (ArrayList<ProductModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView productName;
        TextView tvProduct_Title;
        TextView tvProduct_Brand;
        TextView tvProduct_Price;
        TextView tvProduct_addtoCart;
        LinearLayout layout_product;

        ViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.tvTitle);
            tvProduct_Brand = itemView.findViewById(R.id.tvBrand);
            tvProduct_Price = itemView.findViewById(R.id.tvPrice);
            tvProduct_addtoCart = itemView.findViewById(R.id.tvaddtoCart);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            layout_product =  itemView.findViewById(R.id.layout_product);
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
