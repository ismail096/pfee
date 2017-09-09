package com.navigation.drawer.activity.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.navigation.drawer.activity.Classes.ListGarde;
import com.navigation.drawer.activity.Classes.Pharmacie;
import com.navigation.drawer.activity.R;


public class SearchActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    CheckBox cPha = null, cCli = null, cMed = null;
    EditText searchField = null;
    Button search = null;
    ImageView addMedecin = null, addClinique = null, addPharmacie = null;
    public SQLiteDatabase myDB = null;
    double userlocatLa;
    double userlocatLn;

    LocationManager locationManager;

    LocationListener locationListener;

    SimpleAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Main2Activity.historique.add("search");

        super.onCreate(savedInstanceState);

        myDB = this.openOrCreateDatabase("DatabaseName", MODE_PRIVATE, null);


        getLayoutInflater().inflate(R.layout.activity_search, frameLayout);

        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);


        searchField = (EditText) findViewById(R.id.editText3);

        cPha = (CheckBox) findViewById(R.id.CheckPharmacies);
        cMed = (CheckBox) findViewById(R.id.CheckMedecins);
        cCli = (CheckBox) findViewById(R.id.CheckCliniques);


        search = (Button) findViewById(R.id.Search);
        search.setOnClickListener(this);

        ImageView Closer = (ImageView) findViewById(R.id.proche);
        Closer.setOnClickListener(this);

        addMedecin = (ImageView) findViewById(R.id.getMedecins);
        addMedecin.setOnClickListener(this);
        addClinique = (ImageView) findViewById(R.id.getCliniques);
        addClinique.setOnClickListener(this);
        addPharmacie = (ImageView) findViewById(R.id.getPharmacies);
        addPharmacie.setOnClickListener(this);



        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

                userlocatLn = location.getLongitude();
                userlocatLa = location.getLatitude();

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }

        };

        // If device is running SDK < 23

        if (Build.VERSION.SDK_INT < 23) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // ask for permission

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


            } else {

                // we have permission!

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            }

        }

    }



    private void DoTask() throws InterruptedException {

        if (isNetworkAvailable()) {

            if (ListGarde.pharmacieList != null) {
                double dis = 1000000000;
                Pharmacie closer = null;
                Location currentLocation = new Location("");
                currentLocation.setLatitude(userlocatLa);
                currentLocation.setLongitude(userlocatLn);
                Location closerLoc   = null ;
                for (int i = 0; i < ListGarde.pharmacieList.size(); i++) {

                    Pharmacie currentPharmacie = ListGarde.pharmacieList.get(i);
                    Location crLoc = new Location("");

                    crLoc.setLongitude(currentPharmacie.getLocalisation().getLongitude());
                    crLoc.setLatitude(currentPharmacie.getLocalisation().getLaltitude());

                    double currDistance = currentLocation.distanceTo(crLoc);


                    if (dis > currDistance) {
                        dis = currDistance;
                        closerLoc = crLoc ;
                        closer = currentPharmacie;
                    }
                }

                MapActivity.currentLocation = currentLocation ;
                MapActivity.searchedLocation = closerLoc;
                MapActivity.currentObject = closer;

                startActivity(new Intent(this, MapActivity.class));
            }
        } else {
            Toast.makeText(getApplicationContext(), "This service is not available in Offline", Toast.LENGTH_SHORT).show();
        }
    }





    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position - 1) {
            case 0:
                openActivity(2);
                break;
            case 1:
                openActivity(1);
                break;
            case 2:
                openActivity(3);
                break;
            case 3:
                openActivity(4);
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if (v.getClass().getSimpleName().equals("Button")) {
            Button b = (Button) v;
            if (b.getText().equals("Search")) {
                if (searchField.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(), "Entrer quelque chose svp", Toast.LENGTH_SHORT).show();
                else {
                    ResultActivity.setVar(searchField.getText().toString(), cPha.isChecked(), cMed.isChecked(), cCli.isChecked(),true);
                    Intent intent = new Intent(this, ResultActivity.class);
                    startActivity(intent);
                }
            }
        }
        if (v.getClass().getSimpleName().equals("ImageView")) {
            ImageView b = (ImageView) v;
            if (b.getId() == R.id.proche) {
                if(isNetworkAvailable()) {
                    try {
                        int s = (int) userlocatLa;
                        if (s == 0)
                            Toast.makeText(getApplicationContext(), "Activez gps et attendez .. ", Toast.LENGTH_SHORT).show();
                        else {
                            DoTask();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Connectez vous a l'internet et redemarrez l'application", Toast.LENGTH_SHORT).show();
                }
            }
            if (b.getId() == R.id.getCliniques) {
                openActivity(4);
            }
            if(b.getId() == R.id.getMedecins){
                openActivity(3);
            }
            if(b.getId() == R.id.getPharmacies){
                openActivity(2);
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        }

    }
}

