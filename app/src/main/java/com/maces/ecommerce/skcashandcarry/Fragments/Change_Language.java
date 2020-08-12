package com.maces.ecommerce.skcashandcarry.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.maces.ecommerce.skcashandcarry.R;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;


public class Change_Language extends Fragment {
    private Context context;
    private LinearLayout llEnglish, llSpanish;
    private AlertDialog alertDialog;
    private View dialogView;
    Locale locale;
    private Button English, Spanish, EnglishSelected, SpanishSelected;
    private TextView Begin;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_change__language, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#000000\">" + getString(R.string.change_lang) + "</font>")));

        sharedPreferences = getActivity().getSharedPreferences("Language", MODE_PRIVATE);
        context = container.getContext();

        English = (Button) root.findViewById(R.id.english);
        Spanish = (Button) root.findViewById(R.id.spanish);
        EnglishSelected = (Button) root.findViewById(R.id.englishSelected);
        Begin = (TextView) root.findViewById(R.id.begin);
        SpanishSelected = (Button) root.findViewById(R.id.arabicSelected);
        Begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EnglishSelected.getVisibility() == View.VISIBLE) {
                    setAppLocal("en");
                    Toast.makeText(getActivity(), "Language Set to English", Toast.LENGTH_SHORT).show();
                    restartActivity();

                } else {
                    setAppLocal("es");
                    Toast.makeText(getActivity(), "Idioma configurado para español", Toast.LENGTH_SHORT).show();
                    restartActivity();

                }
            }
        });
        String Selected = sharedPreferences.getString("Selected", "");
        if (Selected.equals("en")) {
            EnglishSelected.setVisibility(View.VISIBLE);
            EnglishSelected.setEnabled(false);

        } else if (Selected.equals("es")) {

            SpanishSelected.setVisibility(View.VISIBLE);
            SpanishSelected.setEnabled(false);

        }
        English.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpanishSelected.setVisibility(View.INVISIBLE);
                EnglishSelected.setVisibility(View.VISIBLE);

            }
        });
        Spanish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpanishSelected.setVisibility(View.VISIBLE);
                EnglishSelected.setVisibility(View.INVISIBLE);

            }
        });

        //  dilog();
        return root;
    }

    private void setAppLocal(String Language) {
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(Language.toLowerCase()));
        resources.updateConfiguration(configuration, displayMetrics);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Selected", Language);
        editor.apply();

    }

    private void initView(View root) {
        llEnglish = root.findViewById(R.id.llEnglish);
        llSpanish = root.findViewById(R.id.llSpanish);
        String Selected = sharedPreferences.getString("Selected", "");
        if (Selected.equals("en")) {
            llEnglish.setEnabled(false);
            llSpanish.setEnabled(true);
        } else if (Selected.equals("es")) {
            llSpanish.setEnabled(false);
            llEnglish.setEnabled(true);
        }

        //setUpButton();
    }

    private void dilog() {
        try {
            final androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(context);
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();

            dialogView = inflater.inflate(R.layout.popup_layout, null);
            dialogBuilder.setView(dialogView);
            alertDialog = dialogBuilder.create();
            initView(dialogView);

            llEnglish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAppLocal("en");
                    Toast.makeText(getActivity(), "Language Set to English", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    restartActivity();
                }
            });
            llSpanish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAppLocal("es");
                    Toast.makeText(getActivity(), "Idioma configurado para español", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    restartActivity();
                }
            });

            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        } catch (Exception e) {
            Log.v("ERROR", e.getMessage());
        }
    }

    private void restartActivity() {
        Intent intent = getActivity().getIntent();
        getActivity().finish();
        startActivity(intent);
    }
}
