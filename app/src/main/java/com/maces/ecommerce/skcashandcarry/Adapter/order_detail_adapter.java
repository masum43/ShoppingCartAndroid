package com.maces.ecommerce.skcashandcarry.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.maces.ecommerce.skcashandcarry.Model.Order_Model;
import com.maces.ecommerce.skcashandcarry.R;

import java.math.BigDecimal;
import java.util.List;

public class order_detail_adapter extends RecyclerView.Adapter<order_detail_adapter.ViewHolder> {

    private Context context;
    private List<Order_Model> nevigation_models;

    public order_detail_adapter(Context context, List<Order_Model> nevigation_models) {
        this.context = context;
        this.nevigation_models = nevigation_models;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.tvPrice.setText("€ " + nevigation_models.get(position).getPrice());
        holder.tvpname.setText(nevigation_models.get(position).getProductName());
        holder.p_quantity.setText(nevigation_models.get(position).getQuantity());
        BigDecimal bdd = new BigDecimal(Double.parseDouble(nevigation_models.get(position).getPrice()) * Double.parseDouble(nevigation_models.get(position).getQuantity()));
        bdd = bdd.setScale(2, BigDecimal.ROUND_HALF_UP);
        holder.tvStatus.setText("€ " + bdd.doubleValue());
        Picasso.get().load(nevigation_models.get(position).getImage_url()).into(holder.product_image);
    }

    @Override
    public int getItemCount() {
        return nevigation_models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView product_image;
        TextView tvpname, p_quantity, tvPrice, tvStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            product_image = itemView.findViewById(R.id.product_image);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            p_quantity = itemView.findViewById(R.id.p_quantity);
            tvpname = itemView.findViewById(R.id.tvpname);
        }
    }
}
