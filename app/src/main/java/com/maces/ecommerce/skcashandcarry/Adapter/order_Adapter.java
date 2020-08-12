package com.maces.ecommerce.skcashandcarry.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.maces.ecommerce.skcashandcarry.Model.Order_Model;
import com.maces.ecommerce.skcashandcarry.R;

import java.util.List;
import com.maces.ecommerce.skcashandcarry.View.order_product_detail;

public class order_Adapter extends RecyclerView.Adapter<order_Adapter.ViewHolder> {

    private Context context;
    private List<Order_Model> nevigation_models;

    public order_Adapter(Context context, List<Order_Model> nevigation_models) {
        this.context = context;
        this.nevigation_models = nevigation_models;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(nevigation_models.get(position).getMethod().trim().equals("Test"))
        {

        }
        else {
            holder.tvorderby.setText(""+nevigation_models.get(position).getId());
            holder.tvbill.setText(nevigation_models.get(position).getPrice());
            holder.tvquantity.setText(nevigation_models.get(position).getQuantity());
            holder.tvquantity.setAllCaps(true);
            holder.tvdetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent new_Intent=new Intent(context,order_product_detail.class);
                    new_Intent.putExtra("id",nevigation_models.get(position).getId());
                    context.startActivity(new_Intent);

                }
            });
        }

    }

    @Override
    public int getItemCount() {return nevigation_models.size();}

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView product_image;
        TextView tvorderby,tvbill,tvquantity;
        Button tvdetail;
        LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            tvorderby = itemView.findViewById(R.id.tvorderby);
            tvbill = itemView.findViewById(R.id.tvbill);
            tvquantity = itemView.findViewById(R.id.tvquantity);
            tvdetail = itemView.findViewById(R.id.tvdetail);

        }
    }
}
