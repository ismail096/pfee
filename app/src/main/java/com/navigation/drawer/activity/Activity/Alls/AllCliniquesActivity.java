package com.navigation.drawer.activity.Activity.Alls;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.navigation.drawer.activity.Activity.BaseActivity;
import com.navigation.drawer.activity.Activity.Main2Activity;
import com.navigation.drawer.activity.Activity.Profiles.CliniqueProfil;
import com.navigation.drawer.activity.Blocs.CliniqueBloc;
import com.navigation.drawer.activity.Classes.Clinique;
import com.navigation.drawer.activity.Classes.ListCliniques;
import com.navigation.drawer.activity.R;

import java.util.Vector;

public class AllCliniquesActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Main2Activity.historique.add("clinique");
        getLayoutInflater().inflate(R.layout.activity_all_cliniques, frameLayout);

        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);
        ImageView b = (ImageView) findViewById(R.id.backC);
        b.setOnClickListener(this);



        String[] arraySpinner = new String[4];
        arraySpinner[0] = "Choisissez une categorie";
        arraySpinner[1] = "Publique";
        arraySpinner[2] = "Semi publique";
        arraySpinner[3] = "Privé";

        Spinner s = (Spinner) findViewById(R.id.spinnerC);

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
            CliniqueBloc gb = (CliniqueBloc) v;
            CliniqueProfil.currentClinique = gb.clinique ;
            Intent intent = new Intent(this,CliniqueProfil.class);
            startActivity(intent);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position != 0) {


            GridLayout myCliniques = (GridLayout) findViewById(R.id.CliniqueAll);

            myCliniques.removeAllViews();

            Vector<Clinique> list = new Vector<Clinique>();

            String categ = parent.getSelectedItem().toString();
            for(int i = 0; i< ListCliniques.myList.size(); i++){
                if(ListCliniques.myList.get(i).getCategorie().equals(categ)){
                    list.add(ListCliniques.myList.get(i));
                }
            }

            myCliniques.setRowCount((int)(list.size()/2)+1);
            myCliniques.setColumnCount(2);


            Point size = new Point();
            getWindowManager().getDefaultDisplay().getSize(size);
            int screenWidth = size.x;
            int screenHeight = size.y;
            int halfScreenWidth = (int)(screenWidth *0.5);
            int quarterScreenWidth = (int)(halfScreenWidth * 0.5);

            for(int i=0;i<list.size();i++){
                Clinique clinique = list.get(i);
                GridLayout.LayoutParams param =new GridLayout.LayoutParams();
                param.height = GridLayout.LayoutParams.WRAP_CONTENT;
                param.width = halfScreenWidth - 80;
                param.setMargins(10,10,10,10);
                param.setGravity(Gravity.CENTER);
                CliniqueBloc gb = new CliniqueBloc(getApplicationContext(),clinique);
                gb.setOnClickListener(this);
                gb.setLayoutParams (param);

                myCliniques.addView(gb);
            }

            ScrollView scroll = (ScrollView) findViewById(R.id.scrollClinique);
            scroll.setVisibility(View.VISIBLE);
        }
        else {
            ScrollView scroll = (ScrollView) findViewById(R.id.scrollClinique);
            scroll.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
