package com.maces.ecommerce.skcashandcarry.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.maces.ecommerce.skcashandcarry.Adapter.Category_ProductAdapter;
import com.maces.ecommerce.skcashandcarry.Fragments.Product_Home;
import com.maces.ecommerce.skcashandcarry.MainActivity;
import com.maces.ecommerce.skcashandcarry.R;

import static com.maces.ecommerce.skcashandcarry.Adapter._PaginationAdapter.cartModels;

public class order_placed extends AppCompatActivity {

    Button tvMyOrder;

    @Override
    public void onBackPressed() {
       // startActivity(new Intent(order_placed.this,Home.class));
        order_placed.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);
        tvMyOrder= findViewById(R.id.tvMyOrder);
        tvMyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Home.cart_count=0;
                MainActivity.cart_count=0;
                cartModels.clear();
             //   Product_Home.cartModels.clear();
                Product_Home.cart_count=0;
                Category_Product.cart_count = 0;
                Category_ProductAdapter.cartModels_Category.clear();
       //         startActivity(new Intent(order_placed.this,Home.class));
                order_placed.this.finish();
            }
        });
    }
}
