package com.maces.ecommerce.skcashandcarry.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.maces.ecommerce.skcashandcarry.Model.Categories_Class;
import com.maces.ecommerce.skcashandcarry.R;
import com.maces.ecommerce.skcashandcarry.View.Category_Product;

import java.util.ArrayList;

public class Categories_Adapter extends RecyclerView.Adapter<Categories_Adapter.ViewHolder> {

    private Context context;
    private ArrayList<Categories_Class> nevigation_models;

    public Categories_Adapter(Context context, ArrayList<Categories_Class> nevigation_models) {
        this.context = context;
        this.nevigation_models = nevigation_models;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_categoriescontent,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Picasso.get().load(nevigation_models.get(position).getCategory_image()).resize(80,80).placeholder(R.drawable.place).into(holder.category);
        holder.category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disablefor1sec(view);
              //  Toast.makeText(context, ""+nevigation_models.get(position).getName(), Toast.LENGTH_SHORT).show();
                Intent newIntent=new Intent(context, Category_Product.class);
                newIntent.putExtra("id",nevigation_models.get(position).getId());
                newIntent.putExtra("name",nevigation_models.get(position).getName());
                context.startActivity(newIntent);
            }
        });
    }

    @Override
    public int getItemCount() {return nevigation_models.size();}

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView category;
       // LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.layout_category);
         //   linearLayout = itemView.findViewById(R.id.layout);

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
