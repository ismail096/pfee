package com.navigation.drawer.activity.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.navigation.drawer.activity.HttpManager;
import com.navigation.drawer.activity.JSONParser;
import com.navigation.drawer.activity.Classes.Clinique;
import com.navigation.drawer.activity.Classes.ListCliniques;
import com.navigation.drawer.activity.Classes.ListGarde;
import com.navigation.drawer.activity.Classes.ListPharmacie;
import com.navigation.drawer.activity.Classes.ListSpecialities;
import com.navigation.drawer.activity.Classes.Medecin;
import com.navigation.drawer.activity.Classes.MyGPS;
import com.navigation.drawer.activity.Classes.Pharmacie;
import com.navigation.drawer.activity.Classes.Speciality;
import com.navigation.drawer.activity.R;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Main2Activity extends BaseActivity {
    boolean active = true ;
    public static SQLiteDatabase myDB= null;
    public static boolean isChanged = true ;
    ProgressDialog pDialog;
    public boolean loginVerifed = false ;
    public static Vector<String> historique = new Vector<String> () ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDB = this.openOrCreateDatabase("DatabaseName", MODE_PRIVATE, null);
        createTables();

        new MyTask().execute();

    }

    public void createTable(String TableName,String ... Column) {
        String col = "";
        for(int i=0;i<Column.length;i++) {
            col+=", "+Column[i]+" TEXT";
        }
        myDB.execSQL("CREATE TABLE IF NOT EXISTS "+
                TableName+" (ID INTEGER PRIMARY KEY AUTOINCREMENT "+ col+")"
        );
    }

    public void createTables() {
        createTable("Pharmacie","pharmacien","pharmacie","adresse","secteur","tel","longitude","laltitude");
        createTable("Medecin","name","Adresse","tel","longitude","laltitude","speciality");
        createTable("Clinique","name","categorie","adresse","tel","info");
        myDB.execSQL("CREATE TABLE IF NOT EXISTS FAVORIS (ID INTEGER PRIMARY KEY AUTOINCREMENT  ,TYPE TEXT , KEY TEXT)");
    }

    public static String setSlach(String name){
        String ret = "";
        for(int i=0;i<name.length();i++){
            if(name.charAt(i)=='\''){
                ret+='_';
            }
            else
                ret+=name.charAt(i);
        }
        return ret ;
    }

    public static String SlachBack(String name){
        String ret = "";
        for(int i=0;i<name.length();i++){
            if(name.charAt(i)=='_'){
                ret+='\'';
            }
            else
                ret+=name.charAt(i);
        }
        return ret ;
    }
    public void insertDataClinique(String ... info){

        String values = "";
        for(int i=0;i<info.length;i++) {
            info[i] = setSlach(info[i]);
            if(i==0)
                values+="'"+info[i]+"'";
            else
                values+=",'"+info[i]+"'";
        }

        Cursor c = myDB.rawQuery("SELECT * FROM Clinique WHERE name like \'"+info[0]+"\'",null);
        if(c.getCount() == 0)
            myDB.execSQL("INSERT INTO Clinique "+"(name,categorie,adresse,tel,info)"
                    +"VALUES (\'"+values+"\');");
    }

    public void insertDataPharmacie(String ... val) {

        String values = "";
        for(int i=0;i<val.length;i++) {
            val[i] = setSlach(val[i]);
            if(i==0)
                values+="'"+val[i]+"'";
            else
                values+=",'"+val[i]+"'";
        }
        Cursor c = myDB.rawQuery("SELECT * FROM Pharmacie WHERE pharmacien like \'"+setSlach(val[0])+"\'",null);
        if(c.getCount() == 0)
            myDB.execSQL("INSERT INTO Pharmacie "+"(pharmacien,pharmacie,adresse,secteur,tel,longitude,laltitude)"
                +"VALUES ("+values+");");
    }

    public void insertDataMedecin(String ... val) {

        String values = "";
        for(int i=0;i<val.length;i++) {
            val[i] = setSlach(val[i]);
            if(i==0)
                values+="'"+val[i]+"'";
            else
                values+=",'"+val[i]+"'";
        }
        Cursor c = myDB.rawQuery("SELECT * FROM Medecin WHERE name like \'"+setSlach(val[0])+"\'",null);
        if(c.getCount() == 0)
            myDB.execSQL("INSERT INTO Medecin "+"(name,Adresse,tel,longitude,laltitude,speciality)"
                +"VALUES ("+values+");");
    }

    public static void insertFav(String ... val){
        String values = "";
        for(int i=0;i<val.length;i++) {
            val[i] = setSlach(val[i]);
            if(i==0)
                values+="'"+val[i]+"'";
            else
                values+=",'"+val[i]+"'";
        }

        Cursor c = myDB.rawQuery("SELECT * FROM FAVORIS WHERE TYPE = \'"+val[0]+"\' AND KEY  = \'"+val[1]+"\'",null);
        if(c.getCount()==0)
           myDB.execSQL("INSERT INTO FAVORIS "+"(TYPE,KEY) "
                +"VALUES ("+values+");");
        FavorisActivity.lf = getFavoris();
    }

    public static void removeFav(String ... val){
        String values = "";
        for(int i=0;i<val.length;i++) {
            val[i] = setSlach(val[i]);
            if(i==0)
                values+="'"+val[i]+"'";
            else
                values+=",'"+val[i]+"'";
        }

        myDB.execSQL("DELETE FROM FAVORIS WHERE TYPE = \'"+val[0]+"\' AND KEY  = \'"+val[1]+"\'");
        FavorisActivity.lf = getFavoris();
    }


    public ArrayList<Pharmacie> getPharmacies() {
        ArrayList<Pharmacie> myList = new ArrayList<Pharmacie>() ;
        Cursor c = myDB.rawQuery("SELECT * FROM Pharmacie" , null);
        int PharmacieInd = c.getColumnIndex("pharmacie");
        int pharmacienInd = c.getColumnIndex("pharmacien");
        int adresseInd = c.getColumnIndex("adresse");
        int secteurInd = c.getColumnIndex("secteur");
        int telInd = c.getColumnIndex("tel");
        int longitudeInd = c.getColumnIndex("longitude");
        int laltitudeInd = c.getColumnIndex("laltitude");

        c.moveToFirst();
        if (c != null && c.getCount()!=0) {
            do {
                Pharmacie pharmacie = new Pharmacie();
                pharmacie.setPharmacie(c.getString(PharmacieInd));
                pharmacie.setPharmacien(c.getString(pharmacienInd));
                pharmacie.setAdresse(c.getString(adresseInd));
                pharmacie.setSecteur(c.getString(secteurInd));
                pharmacie.setTel(c.getString(telInd));
                pharmacie.setLocalisation(new MyGPS(Double.parseDouble(c.getString(longitudeInd)),Double.parseDouble(c.getString(laltitudeInd))));
                myList.add(pharmacie);

            }while(c.moveToNext());
        }
        return  myList;
    }

    public static ArrayList<Object> getFavoris() {
        ArrayList<Object> myList = new ArrayList<Object>() ;
        Cursor c = myDB.rawQuery("SELECT * FROM FAVORIS" , null);
        int typeInd = c.getColumnIndex("TYPE");
        int keyInd = c.getColumnIndex("KEY");

        c.moveToFirst();
        if (c != null && c.getCount()!=0) {
            do {
                 String type = c.getString(typeInd);
                String key = c.getString(keyInd);
                if(type.equals("Pharmacie")){
                    key = SlachBack(key);
                    myList.add(ListPharmacie.get(key));
                }
                if(type.equals("Medecin")){
                    key = SlachBack(key);
                    myList.add(ListSpecialities.getByName(key));
                }
                if(type.equals("Clinique")){
                    key = SlachBack(key);
                    myList.add(ListCliniques.getByName(key));
                }
            }while(c.moveToNext());
        }
        return  myList;
    }

    public List<Clinique> getCliniques(){
        List<Clinique> clin = new ArrayList<Clinique>();
        Cursor c = myDB.rawQuery("SELECT * FROM Clinique" , null);

        int nameInd = c.getColumnIndex("name");
        int categorieInd = c.getColumnIndex("categorie");
        int adresseInd = c.getColumnIndex("adresse");
        int telInd = c.getColumnIndex("tel");
        int infoInd = c.getColumnIndex("info");
        c.moveToFirst();
        if (c != null && c.getCount() !=0 ) {
            do {
                Clinique current_clinique = new Clinique();

                current_clinique.setName(c.getString(nameInd));
                current_clinique.setCategorie(c.getString(categorieInd));
                current_clinique.setAdresse(c.getString(adresseInd));
                current_clinique.setTel(c.getString(telInd));
                current_clinique.setInfo(c.getString(infoInd));

                clin.add(current_clinique);

            }while(c.moveToNext());
        }
        return clin ;
    }

    public  ArrayList<Speciality> getMedecins() {
        ArrayList<Speciality> spec = new ArrayList<Speciality>();

        Cursor c = myDB.rawQuery("SELECT * FROM Medecin" , null);
        int nameInd = c.getColumnIndex("name");
        int AdresseInd = c.getColumnIndex("Adresse");
        int telInd = c.getColumnIndex("tel");
        int longitudeInd = c.getColumnIndex("longitude");
        int laltitudeInd = c.getColumnIndex("laltitude");
        int specialityInd = c.getColumnIndex("speciality");

        c.moveToFirst();
        if (c != null && c.getCount() !=0 ) {
            do {
                Medecin medecin = new Medecin();
                medecin.setName(c.getString(nameInd));
                medecin.setAdresse(c.getString(AdresseInd));
                medecin.setTel(c.getString(telInd));
                medecin.setLocalisation(new MyGPS(Double.parseDouble(c.getString(longitudeInd)),Double.parseDouble(c.getString(laltitudeInd))));
                Log.d("haaa -> ",medecin.toString());
                boolean drap = true;
                for(int i=0;drap && i<spec.size();i++) {
                    if(spec.get(i).getName().equals(c.getString(specialityInd))) {
                        spec.get(i).add(medecin);
                        drap = false ;
                    }
                }
                if (drap) {
                    Speciality New = new Speciality();
                    New.setName(c.getString(specialityInd));
                    New.add(medecin);
                    spec.add(New);
                }

            }while(c.moveToNext());
        }
        return  spec;
    }
    private  boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public class MyTask extends AsyncTask<String,String,String>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Main2Activity.this);
            pDialog.setMessage("Please Wait");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {


            if(isNetworkAvailable()) {

                try {

                    ListGarde.pharmacieList = JSONParser.JSONPharmacieParser(HttpManager.getData("http://www.pfesmi.tk/getGardes.php"));
                    Log.d("haaaah ==> ","http://www.pfesmi.tk/getGardes.php");
                    if(ListGarde.pharmacieList != null){
                        for(int i=0;i<ListGarde.pharmacieList.size();i++){
                            Pharmacie currentPharmacie = ListGarde.pharmacieList.get(i);
                            boolean drap = false ;

                            MyGPS t = JSONParser.getGPS(currentPharmacie.getPharmacie() + " PHARMACIE");
                            if(t != null) {
                                currentPharmacie.setLocalisation(t);
                                drap = true ;
                            }
                            if(!drap) {
                                t = JSONParser.getGPS(currentPharmacie.getAdresse() + " PHARMACIE");
                                if (t != null) {
                                    currentPharmacie.setLocalisation(t);
                                    drap = true ;
                                }
                            }
                            if(!drap) {
                                t = JSONParser.getGPS(currentPharmacie.getSecteur() + " PHARMACIE");
                                if (t != null) {
                                    currentPharmacie.setLocalisation(t);
                                }
                            }
                        }
                    }
                    ListPharmacie.pharmacieList = JSONParser.JSONPharmacieParser(HttpManager.getData("http://www.pfesmi.tk/getPharmacies.php"));
                    Log.d("haaaah ==> ","http://www.pfesmi.tk/getPharmacies.php");
                    ListSpecialities.Specialities = JSONParser.JSONSpecialitiesPARSER(HttpManager.getData("http://www.pfesmi.tk/getMedecins.php"));
                    Log.d("haaaah ==> ","http://www.pfesmi.tk/getMedecins.php");
                    ListCliniques.myList = JSONParser.JSONCliniqueParser(HttpManager.getData("http://www.pfesmi.tk/getCliniques.php"));
                    Log.d("haaaah ==> ","http://www.pfesmi.tk/getCliniques.php");


                    if (Main2Activity.isChanged) {
                        for (int i = 0; i < ListPharmacie.pharmacieList.size() - 1 ; i++) {
                            Pharmacie curr = ListPharmacie.get(i);
                            insertDataPharmacie
                                    (curr.getPharmacien(),
                                            curr.getPharmacie(),
                                            curr.getAdresse(),
                                            curr.getSecteur(),
                                            curr.getTel(),
                                            " " + curr.getLocalisation().getLaltitude(),
                                            " " + curr.getLocalisation().getLongitude());
                        }
                        for (int i = 0; i < ListSpecialities.Specialities.size() - 1; i++) {
                            for (int j = 0; j < ListSpecialities.Specialities.get(i).getMyList().size() - 1; j++) {
                                Medecin medecin = ListSpecialities.Specialities.get(i).get(j);
                                try {
                                    insertDataMedecin(medecin.getName()
                                            , medecin.getAdresse()
                                            , medecin.getTel()
                                            , "" + medecin.getLocalisation().getLaltitude()
                                            , "" + medecin.getLocalisation().getLongitude()
                                            , medecin.getSpeciality());
                                } catch (Exception e) {
                                }
                            }
                        }

                        for(int i=0;i<ListCliniques.myList.size();i++){
                            Clinique clinique = ListCliniques.myList.get(i);
                            try{
                                insertDataClinique(clinique.getName()
                                        ,clinique.getCategorie()
                                        ,clinique.getAdresse()
                                        ,clinique.getTel()
                                        ,clinique.getInfo());
                            } catch (Exception e) {
                            }
                        }

                        Main2Activity.isChanged = false;
                    }

                }
                catch (Exception e) {
                    ListPharmacie.pharmacieList = getPharmacies();
                    ListSpecialities.Specialities = getMedecins();
                    ListCliniques.myList = getCliniques();
                }
            }
            else{
                ListPharmacie.pharmacieList = getPharmacies();
                ListSpecialities.Specialities = getMedecins();
                ListCliniques.myList = getCliniques();
            }
            FavorisActivity.lf = getFavoris();
            loginVerifed = true ;
            return null;


        }

        @Override
        protected void onPostExecute(String s) {
            if(loginVerifed){
                pDialog.dismiss();
                openActivity(0);
                finish();
            }
        }
    }
}

