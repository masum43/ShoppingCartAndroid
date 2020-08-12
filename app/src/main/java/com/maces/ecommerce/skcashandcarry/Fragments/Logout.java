package com.maces.ecommerce.skcashandcarry.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.maces.ecommerce.skcashandcarry.R;
import com.maces.ecommerce.skcashandcarry.View.Login;

import static android.content.Context.MODE_PRIVATE;


public class Logout extends Fragment {
    SharedPreferences prf;
    Intent intent;
    Button btn_Yes, btn_No;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_logout, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#000000\">" + getString(R.string.logout) + "</font>")));

        prf = getActivity().getSharedPreferences("LoginPref", MODE_PRIVATE);
        intent = new Intent(getActivity(), Login.class);
        btn_Yes = (Button) root.findViewById(R.id.logout_btn);

        btn_Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = prf.edit();
                editor.clear();
                editor.apply();
                startActivity(intent);
            }
        });


        return root;
    }
}
