package com.navigation.drawer.activity.Classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pro on 11/03/2017.
 */

public class ListCliniques {

    public static List<Clinique> myList ;

    public ListCliniques() {
        myList = new ArrayList<Clinique>();
    }

    public static Clinique getByName(String name){
        for(int i=0;i<myList.size();i++){
            Clinique currentClinique = myList.get(i);
                if(currentClinique.getName().equals(name))
                    return currentClinique;
        }
        return null ;
    }

}
