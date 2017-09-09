package com.navigation.drawer.activity.Classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pro on 20/02/2017.
 */

public class ListSpecialities {

    public static List<Speciality> Specialities;



    public ListSpecialities() {
        Specialities = new ArrayList<Speciality>();
    }

    public ListSpecialities(ArrayList<Speciality> list) {
        Specialities = list;
    }

    public static Speciality get(int i) {
        if(i<Specialities.size())
            return Specialities.get(i);
        Speciality m = new Speciality();
        m.setName("Not Found");
        return m ;
    }
    public static ArrayList<Speciality> getMyList() {
        return (ArrayList<Speciality>) Specialities;
    }

    public static int get(String name){
        for(int i=0;i<Specialities.size();i++){
            if(get(i).getName().equals(name))
                return i;
        }
        return -1 ;
    }
    public static Medecin getByName(String name){
        for(int i=0;i<Specialities.size()-1;i++){
            for(int j=0;j<Specialities.get(i).getMyList().size()-1;j++){
                Medecin m = Specialities.get(i).get(j);
                if(m.getName().equals(name))
                    return m;
            }
        }
        return null ;
    }
    public void setMyList(ArrayList<Speciality> myList) {
        ListSpecialities.Specialities = myList;
    }
}


