package com.navigation.drawer.activity.Activity.Profiles;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.navigation.drawer.activity.Activity.Alls.AllMedecinsActivity;
import com.navigation.drawer.activity.Activity.Alls.AllPharmaciesActivity;
import com.navigation.drawer.activity.Activity.BaseActivity;
import com.navigation.drawer.activity.Activity.FavorisActivity;
import com.navigation.drawer.activity.Activity.SearchActivity;
import com.navigation.drawer.activity.JSONParser;
import com.navigation.drawer.activity.Activity.Main2Activity;
import com.navigation.drawer.activity.Activity.MapActivity;
import com.navigation.drawer.activity.Classes.MyGPS;
import com.navigation.drawer.activity.Classes.Pharmacie;
import com.navigation.drawer.activity.R;

import static android.R.attr.id;

public class PharmacieProfil extends BaseActivity implements View.OnClickListener{
    public String currentTel = null ;
    public boolean loginVerifed = false ;
    public static MyGPS result = null ;
    public static Pharmacie currentPharmacie = null ;
    public boolean isFavoris = false ;
    ImageView fav = null ;
    ProgressDialog pDialog;

    public ImageView bt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_pharmacie_profil, frameLayout);

        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);


        TextView pharmacie = (TextView) findViewById(R.id.PharmacieName);
        TextView pharmacien = (TextView) findViewById(R.id.pharmacienP);
        ImageView tel = (ImageView) findViewById(R.id.telp);
        TextView secteur = (TextView) findViewById(R.id.SecteurP);
        TextView Adresse = (TextView) findViewById(R.id.AdresseP);
        TextView telM = (TextView) findViewById(R.id.telpM);
        ImageView back = (ImageView) findViewById(R.id.retourpp);

        ///////////////////////
        bt1=(ImageView)findViewById(R.id.telp);
        //////////////////////////////
        back.setOnClickListener(this);

        pharmacie.setText("Pharmacie : "+currentPharmacie.getPharmacie());
        pharmacien.setText(currentPharmacie.getPharmacien());
        currentTel = currentPharmacie.getTel();
        telM.setText(currentTel);
        tel.setOnClickListener(this);
        secteur.setText(currentPharmacie.getSecteur());
        Adresse.setText(currentPharmacie.getAdresse());

        fav = (ImageView) findViewById(R.id.fav_phar);
        if(FavorisActivity.lf.contains(currentPharmacie)){
            isFavoris = true ;
            fav.setImageDrawable(getResources().getDrawable(R.drawable.fav));
        }
        else {
            isFavoris = false ;
            fav.setImageDrawable(getResources().getDrawable(R.drawable.notfav));
        }
        fav.setOnClickListener(this);


        ImageView localisation = (ImageView) findViewById(R.id.localPharmacie);
        localisation.setOnClickListener(this);

        lancer();
    }



    public void lancer() {
        bt1 = (ImageView) findViewById(R.id.telp);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + currentTel));
                    startActivity(callIntent);
                } catch (SecurityException err) {
                    System.out.println(err);
                }
            }
        });
    }



    @Override
    public void onClick(View v) {
        if(v.getClass().getSimpleName().equals("Button")) {
            Button b = (Button) v;
            if (b.getText().equals("Retour")) {


            }

        }
        else if (v.getClass().getSimpleName().equals("ImageView")) {
            ImageView curr = (ImageView) v;
            if(curr.getId()==R.id.localPharmacie || curr.getId()==R.id.retourpp) {

                if(curr.getId()==R.id.localPharmacie){
                    new MyTask().execute();
                }
                if(curr.getId()==R.id.retourpp){
                    int search = Main2Activity.historique.lastIndexOf("search");
                    int favoris = Main2Activity.historique.lastIndexOf("favoris");
                    int pharmacies = Main2Activity.historique.lastIndexOf("pharmacie");
                    int garde = Main2Activity.historique.lastIndexOf("garde");

                    int m = Math.max(Math.max(search,garde),Math.max(favoris,pharmacies));

                    if(m==search)
                        openActivity(0);
                    if(m==pharmacies)
                        openActivity(2);
                    if(m==favoris)
                        openActivity(5);
                    if(m==garde)
                        openActivity(1);
                }
            }
            else{
                if(isFavoris){
                    isFavoris = false ;
                    fav.setImageDrawable(getResources().getDrawable(R.drawable.notfav));
                    Main2Activity.removeFav("Pharmacie",currentPharmacie.getPharmacie());
                }
                else{
                    isFavoris = true ;
                    fav.setImageDrawable(getResources().getDrawable(R.drawable.fav));
                    Main2Activity.insertFav("Pharmacie",currentPharmacie.getPharmacie());
                }
            }
        }
    }
    public class MyTask extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PharmacieProfil.this);
            pDialog.setMessage("Please Wait");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.getWindow().setGravity(Gravity.BOTTOM);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            if(currentPharmacie.getLocalisation().getLongitude()!=0){

                Location loc = new Location("");
                loc.setLatitude(currentPharmacie.getLocalisation().getLaltitude());
                loc.setLongitude(currentPharmacie.getLocalisation().getLongitude());

                MapActivity.searchedLocation = loc;
                MapActivity.currentObject = currentPharmacie;


                loginVerifed = true;
                return null ;
            }

            MyGPS t = JSONParser.getGPS(currentPharmacie.getPharmacie() + " PHARMACIE");
            if(t != null){
                currentPharmacie.setLocalisation(t);

                Location loc = new Location("");
                loc.setLatitude(currentPharmacie.getLocalisation().getLaltitude());
                loc.setLongitude(currentPharmacie.getLocalisation().getLongitude());

                MapActivity.searchedLocation = loc;
                MapActivity.currentObject = currentPharmacie;

                loginVerifed = true;

                return null ;
            }
            t = JSONParser.getGPS(currentPharmacie.getAdresse() + " PHARMACIE");
            if(t != null){
                currentPharmacie.setLocalisation(t);

                Location loc = new Location("");
                loc.setLatitude(currentPharmacie.getLocalisation().getLaltitude());
                loc.setLongitude(currentPharmacie.getLocalisation().getLongitude());

                MapActivity.searchedLocation = loc;
                MapActivity.currentObject = currentPharmacie;


                loginVerifed = true;
                return null ;
            }
            t = JSONParser.getGPS(currentPharmacie.getSecteur() + " PHARMACIE");
            if(t != null){
                currentPharmacie.setLocalisation(t);
                Location loc = new Location("");
                loc.setLatitude(currentPharmacie.getLocalisation().getLaltitude());
                loc.setLongitude(currentPharmacie.getLocalisation().getLongitude());

                MapActivity.searchedLocation = loc;
                MapActivity.currentObject = currentPharmacie;

                loginVerifed = true;
                return null ;
            }
            Toast.makeText(PharmacieProfil.this, "not available", Toast.LENGTH_SHORT).show();
            loginVerifed = true;
            return  null ;
        }

        @Override
        protected void onPostExecute(String s) {
            if(loginVerifed == true)
            {
                pDialog.dismiss();
                startActivity(new Intent(PharmacieProfil.this, MapActivity.class));
                finish();
            }
        }
    }
}
