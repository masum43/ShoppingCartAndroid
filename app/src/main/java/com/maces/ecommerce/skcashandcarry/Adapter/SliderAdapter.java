package com.maces.ecommerce.skcashandcarry.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.maces.ecommerce.skcashandcarry.Constant;
import com.maces.ecommerce.skcashandcarry.OfferDetails;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;
import com.maces.ecommerce.skcashandcarry.Model.SliderItem;
import com.maces.ecommerce.skcashandcarry.R;

import java.util.List;

public class SliderAdapter extends
        SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private Context context;
    private List<SliderItem> mSliderItems;

    public SliderAdapter(Context context,List<SliderItem> sliderItemList) {
        this.context = context;
        this.mSliderItems=sliderItemList;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        final SliderItem sliderItem = mSliderItems.get(position);


        Picasso.get().load(Constant.sliderImageBase+sliderItem.getImageUrl()).into(viewHolder.imageViewBackground);

        viewHolder.offerDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, OfferDetails.class);
                i.putExtra("id",String.valueOf(sliderItem.getId()));
                i.putExtra("title",sliderItem.getTitle());
                i.putExtra("price",sliderItem.getPrice());
                i.putExtra("desc",sliderItem.getDescription());
                i.putExtra("url",Constant.sliderImageBase+sliderItem.getImageUrl());
                context.startActivity(i);
            }
        });

        viewHolder.imageViewBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, OfferDetails.class);
                i.putExtra("id",String.valueOf(sliderItem.getId()));
                i.putExtra("title",sliderItem.getTitle());
                i.putExtra("price",sliderItem.getPrice());
                i.putExtra("desc",sliderItem.getDescription());
                i.putExtra("url",Constant.sliderImageBase+sliderItem.getImageUrl());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        ImageView imageGifContainer;
        TextView textViewDescription,tvPrice,tvTitle;
        Button offerDetailsBtn;

        SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            offerDetailsBtn = itemView.findViewById(R.id.offerDetailsId);
            this.itemView = itemView;
        }
    }

}