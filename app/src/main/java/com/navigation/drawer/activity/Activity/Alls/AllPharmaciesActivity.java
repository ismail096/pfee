package com.navigation.drawer.activity.Activity.Alls;

import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.navigation.drawer.activity.Activity.BaseActivity;
import com.navigation.drawer.activity.Activity.Main2Activity;
import com.navigation.drawer.activity.Activity.Profiles.PharmacieProfil;
import com.navigation.drawer.activity.Blocs.pharmacieBloc;
import com.navigation.drawer.activity.Classes.ListPharmacie;
import com.navigation.drawer.activity.Classes.Pharmacie;
import com.navigation.drawer.activity.R;

import java.util.HashMap;
import java.util.Vector;

public class AllPharmaciesActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    HashMap<Button,Pharmacie> list = new HashMap<Button,Pharmacie>();
    ScrollView scroll  = null ;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        Main2Activity.historique.add("pharmacie");
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_all_pharmacies, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);
        ImageView b = (ImageView) findViewById(R.id.backP);
        b.setOnClickListener(this);


        Vector<String> quartier = new Vector<String>();

        for(int i=0;i<ListPharmacie.pharmacieList.size();i++){
            if(!quartier.contains(ListPharmacie.get(i).getSecteur().toLowerCase()))
                quartier.add(ListPharmacie.get(i).getSecteur().toLowerCase());
        }

        int n = quartier.size();
        String[] arraySpinner = new String[n+1];
        arraySpinner[0] = "Choisissez un quartier";
        for(int i=0;i<n;i++){
            arraySpinner[i+1] = quartier.get(i);
        }
        Spinner s = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner) {

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                convertView = super.getDropDownView(position, convertView,
                        parent);

                convertView.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams p = convertView.getLayoutParams();
                p.height = 100; // set the height
                convertView.setLayoutParams(p);

                return convertView;
            }
        };
        adapter.setDropDownViewResource(R.layout.my_spinner_textview);
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getClass().getSimpleName().equals("ImageView")) {
            openActivity(0);
        }
        else {
            pharmacieBloc gb = (pharmacieBloc) v;
            PharmacieProfil.currentPharmacie = gb.pharmacie ;
            Intent intent = new Intent(this,PharmacieProfil.class);
            startActivity(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position != 0) {
            String quartier = parent.getSelectedItem().toString();

            GridLayout myPharmacies = (GridLayout) findViewById(R.id.PharmaciesAll);

            myPharmacies.removeAllViews();

            Vector<Pharmacie> currentVector = new Vector<Pharmacie>();
            for (int i = 0; i < ListPharmacie.pharmacieList.size() - 1; i++) {
                Pharmacie pharmacie = ListPharmacie.pharmacieList.get(i);
                if (pharmacie.getSecteur().toLowerCase().equals(quartier)) {
                    currentVector.add(pharmacie);
                }
            }
            myPharmacies.setRowCount(currentVector.size()+1);


            Point size = new Point();
            getWindowManager().getDefaultDisplay().getSize(size);
            int screenWidth = size.x;
            int screenHeight = size.y;
            int halfScreenWidth = (int)(screenWidth *0.5);
            int quarterScreenWidth = (int)(halfScreenWidth * 0.5);

            for(int i=0;i<currentVector.size();i++){
                Pharmacie pharmacie = currentVector.get(i);
                GridLayout.LayoutParams param =new GridLayout.LayoutParams();
                param.height = GridLayout.LayoutParams.WRAP_CONTENT;
                param.width = halfScreenWidth - 80;
                param.setMargins(10,10,10,10);
                param.setGravity(Gravity.CENTER);
                pharmacieBloc gb = new pharmacieBloc(getApplicationContext(),pharmacie);
                gb.setOnClickListener(this);
                gb.setLayoutParams (param);

                myPharmacies.addView(gb);
            }

            scroll = (ScrollView) findViewById(R.id.scrollPharmacies);
            scroll.setVisibility(View.VISIBLE);
        }
        else {
            scroll = (ScrollView) findViewById(R.id.scrollPharmacies);
            scroll.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
