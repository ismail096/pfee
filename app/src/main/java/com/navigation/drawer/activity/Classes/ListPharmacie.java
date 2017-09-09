package com.navigation.drawer.activity.Classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pro on 19/02/2017.
 */

public class ListPharmacie {
    public static List<Pharmacie> pharmacieList ;

    public ListPharmacie(){
        pharmacieList = new ArrayList<Pharmacie>();
    }

    public static Pharmacie get(int i){
        if(pharmacieList.size()>i)
            return pharmacieList.get(i);
        return null;
    }

    public void setPharmacieList(ArrayList<Pharmacie> list){
        pharmacieList = list;
    }

    public static Pharmacie get(String name){
        for(int i=0;i<pharmacieList.size();i++){
            Pharmacie pharmacie = pharmacieList.get(i);
            if(pharmacie.getPharmacie().equals(name))
                return pharmacie;
        }
        return  null ;
    }
}
