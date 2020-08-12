package com.maces.ecommerce.skcashandcarry.Fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.maces.ecommerce.skcashandcarry.Adapter.order_Adapter;
import com.maces.ecommerce.skcashandcarry.Interfaces.Fetchorders;
import com.maces.ecommerce.skcashandcarry.Model.Order_Model;
import com.maces.ecommerce.skcashandcarry.Model.ProductService;
import com.maces.ecommerce.skcashandcarry.R;
import com.maces.ecommerce.skcashandcarry.View.Login;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class Order_History extends Fragment {

    RecyclerView recycler_view_order;
    protected ProgressDialog progressDialog;
    static String access_token, token_type, price_category;
    public static final String CartPref = "CartPref";
    public static final String Cart_Value = "cart_value";
    SharedPreferences prf;
    List<Order_Model>order_modelList;
    order_Adapter order_adapter;
    SimpleDateFormat simpleDateFormat1,simpleDateFormat2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_order__history, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#000000\">" + getString(R.string.menu_myorder) + "</font>")));

        recycler_view_order=(RecyclerView)root.findViewById(R.id.recycler_view_order);
        prf = getActivity().getSharedPreferences("LoginPref", MODE_PRIVATE);

        if( prf.getString("token_typeKey", "").length()>0) {
            token_type = prf.getString("token_typeKey", "");
            access_token = prf.getString("access_tokenKey", "");
        }
        else
        {
            token_type = Login.token_type_val;
            access_token = Login.access_token_val;
        }
        simpleDateFormat1=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        simpleDateFormat2=new SimpleDateFormat("dd-MM-YYYY");
        order_modelList=new ArrayList<>();
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.gettingorder));
        GEtOrder();
        return root;
    }

    private void GEtOrder()
    {
        Fetchorders jsonPostService;
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Accept","application/json");
        if(token_type.toString().length()<1) {
            headers.put("Authorization", Login.token_type_val + " " + Login.access_token_val);
        }
        else {
            headers.put("Authorization", token_type + " " + access_token);
        }

        JsonObject jsonObject= new JsonObject();
        if(token_type.toString().length()<1) {
            jsonPostService = ProductService.createService(Fetchorders.class, "https://skcc.luqmansoftwares.com/api/auth/",Login.token_type_val+" "+Login.access_token_val);
        }
        else {
            jsonPostService = ProductService.createService(Fetchorders.class, "https://skcc.luqmansoftwares.com/api/auth/",token_type+" "+access_token);
        }

        Call<JsonObject> call = jsonPostService.postRawJSON();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try{
                    if (response.isSuccessful()) {
                        parse_data(response.body().toString());
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Error:"+response.errorBody(), Toast.LENGTH_LONG).show(); // do something with that
                    }
                }catch (Exception e){
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                try{
                    Log.e("response-error", call.toString());
                    progressDialog.dismiss();
                }catch (Exception e){
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        });
    }
    public void parse_data(String response) {

        String status,method,quantity,price,date,id;
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonarray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                Order_Model order_model=new Order_Model();

                price=jsonobject.getString("amount");
                order_model.setPrice(price);
                quantity=jsonobject.getString("status");
                order_model.setQuantity(quantity);

                date=parseDate(jsonobject.getString("created_at"),simpleDateFormat1,simpleDateFormat2);

                order_model.setDate(date);
                method=jsonobject.getString("notes");
                order_model.setMethod(method);

                id= String.valueOf(jsonobject.getInt("id"));
                order_model.setId(id);

                order_modelList.add(order_model);


            }
            order_adapter=new order_Adapter(getActivity(),order_modelList);
            recycler_view_order.setAdapter(order_adapter);
            recycler_view_order.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
            progressDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }

    }
    public static String parseDate(String inputDateString, SimpleDateFormat inputDateFormat, SimpleDateFormat outputDateFormat) {
        Date date = null;
        String outputDateString = null;
        try {
            date = inputDateFormat.parse(inputDateString);
            outputDateString = outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDateString;
    }

}
