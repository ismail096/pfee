package com.navigation.drawer.activity.Activity.Administation;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.navigation.drawer.activity.Activity.AddProfiles.AddClinique;
import com.navigation.drawer.activity.Activity.BaseActivity;
import com.navigation.drawer.activity.HttpManager;
import com.navigation.drawer.activity.JSONParser;
import com.navigation.drawer.activity.Activity.Main2Activity;
import com.navigation.drawer.activity.Classes.ListPharmacie;
import com.navigation.drawer.activity.Classes.ListSpecialities;
import com.navigation.drawer.activity.R;

import org.json.JSONException;

import java.io.IOException;

public class AdminProfil extends BaseActivity implements View.OnClickListener{

    TextView upP = null , lUpP = null , upM = null , lUpM = null ;
    public boolean isFinished = false ;
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getLayoutInflater().inflate(R.layout.activity_admin_profil, frameLayout);
    mDrawerList.setItemChecked(position, true);
    setTitle(listArray[position]);
    upP = (TextView) findViewById(R.id.updateP);
    lUpP = (TextView) findViewById(R.id.lastUpP);
    upM = (TextView) findViewById(R.id.updateM);
    lUpM = (TextView) findViewById(R.id.lastUpM);

    lUpP.setText("LAST UPDATE = "+ListPharmacie.get(ListPharmacie.pharmacieList.size()-1).getPharmacie());
    lUpM.setText("LAST UPDATE = "+ListSpecialities.get(ListSpecialities.Specialities.size()-1).getName());

    upP.setOnClickListener(this);
    upM.setOnClickListener(this);

}
@Override
public void onClick(View v) {
        if(v.getClass().getSimpleName().equals("TextView")){
            TextView b = (TextView) v;
            if(b.getText().equals("mettre a jours les pharmacies")){
                (new MyTask()).execute("Pharmacie");
            }
            if(b.getText().equals("mettre a jours les medecins")){
                (new MyTask()).execute("Medecin");
            }
        }
    }

    public void addClinique(View view) {
        startActivity(new Intent(AdminProfil.this,AddClinique.class));
    }

    public class MyTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            if(params[0].equals("Pharmacie")) {
                String currentResult = null;
                try {
                    currentResult = HttpManager.getDatas("http://www.pfesmi.tk/setPharmacies.php");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (currentResult != null) {
                    try {
                        ListPharmacie.pharmacieList = JSONParser.JSONPharmacieParser(HttpManager.getData("http://www.pfesmi.tk/getPharmacies.php"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(params[0].equals("Medecin")) {
                String currentResult = null;
                try {
                    currentResult = HttpManager.getDatas("http://www.pfesmi.tk/setMedecins.php");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (currentResult != null) {
                    try {
                        ListSpecialities.Specialities = JSONParser.JSONSpecialitiesPARSER(HttpManager.getData("http://www.pfesmi.tk/getMedecins.php"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            isFinished = true ;
            return  null ;
        }

        @Override
        protected void onPostExecute(String s) {
            if(isFinished){
                refresh();
                finish();
            }
        }
    }
    public void refresh(){
        startActivity(new Intent(this,AdminProfil.class));
    }
}
