package com.navigation.drawer.activity.Activity.Alls;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
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
import com.navigation.drawer.activity.Activity.Profiles.MedecinProfil;
import com.navigation.drawer.activity.Blocs.medecinBloc;
import com.navigation.drawer.activity.Classes.ListSpecialities;
import com.navigation.drawer.activity.Classes.Medecin;
import com.navigation.drawer.activity.Classes.Speciality;
import com.navigation.drawer.activity.R;

import java.util.HashMap;

public class AllMedecinsActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    HashMap<Button,Speciality> list = new HashMap<Button,Speciality>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Main2Activity.historique.add("medecin");
        getLayoutInflater().inflate(R.layout.activity_all_medecins, frameLayout);

        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);
        ImageView b = (ImageView) findViewById(R.id.backM);
        b.setOnClickListener(this);





        int n = ListSpecialities.Specialities.size();
        String[] arraySpinner = new String[n+1];
        arraySpinner[0] = "Choisissez une specialité";
        for(int i=0;i<n;i++){
            arraySpinner[i+1] = ListSpecialities.get(i).getName();
        }
        Spinner s = (Spinner) findViewById(R.id.spinnerM);

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
            medecinBloc gb = (medecinBloc) v;
            MedecinProfil.currentMedecin = gb.medecin ;
            Intent intent = new Intent(this,MedecinProfil.class);
            startActivity(intent);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position != 0) {
            Speciality speciality = null ;
            String spec = parent.getSelectedItem().toString();
            for(int i=0;i<ListSpecialities.Specialities.size();i++){
                if(ListSpecialities.get(i).getName().equals(spec)) {
                    speciality = ListSpecialities.get(i);
                    Log.d("haaa","Speciality found");
                }
            }

            GridLayout myMedecins = (GridLayout) findViewById(R.id.MedecinAll);

            myMedecins.removeAllViews();


            myMedecins.setRowCount(speciality.getMyList().size()/2+1);
            myMedecins.setColumnCount(2);


            Point size = new Point();
            getWindowManager().getDefaultDisplay().getSize(size);
            int screenWidth = size.x;
            int screenHeight = size.y;
            int halfScreenWidth = (int)(screenWidth *0.5);
            int quarterScreenWidth = (int)(halfScreenWidth * 0.5);

            for(int i=0;i<speciality.getMyList().size();i++){
                Medecin medecin = speciality.getMyList().get(i);
                GridLayout.LayoutParams param =new GridLayout.LayoutParams();
                param.height = GridLayout.LayoutParams.WRAP_CONTENT;
                param.width = halfScreenWidth - 80;
                param.setMargins(10,10,10,10);
                param.setGravity(Gravity.CENTER);
                medecinBloc gb = new medecinBloc(getApplicationContext(),medecin);
                gb.setOnClickListener(this);
                gb.setLayoutParams (param);

                myMedecins.addView(gb);
                Log.d("haaa",medecin.getName()+" added");
            }

            ScrollView scroll = (ScrollView) findViewById(R.id.scrollMedecin);
            scroll.setVisibility(View.VISIBLE);
        }
        else {
            ScrollView scroll = (ScrollView) findViewById(R.id.scrollMedecin);
            scroll.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
