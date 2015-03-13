package com.skaliak.tutorialapp;

import android.util.Log;

import com.skaliak.MonSightingClient.*;
import java.util.List;

/**
 * Created by skaliak on 3/10/15.
 */
public class DataSinglet {
    //start Singleton Logic
    private static DataSinglet instance = null;
    public static DataSinglet getInstance() {
        if (instance == null)
            instance = new DataSinglet();

        return instance;
    }
    private DataSinglet(){
        //no constructor for you!
    }
    //end Singleton Logic

    private List<Monster> monsterList;
    private boolean logged_in;
    private String cookie;
    private int selected;

    public void setMonsterList(List<Monster> incoming_list){
        monsterList = incoming_list;
        selected = 0;
    }
    public Monster getMonster(int position){
        return monsterList.get(position);
    }
    public boolean isLoggedIn() {
        return logged_in;
    }
    public void logIn(String t) {
        logged_in = true;
        cookie = t;
    }
    public void logOut() {
        logged_in = false;
        monsterList = null;
        selected = 0;
        cookie = null;
    }
    public String getCookie() {
        return cookie;
    }

    public void select(int i) {
        if (i < monsterList.size())
            selected = i;
    }

    public Monster getSelected() {
        Monster m;
        if (monsterList != null && monsterList.size() > 0) {
            m = monsterList.get(selected);
        } else {
            Log.d("DataSinglet", "getselected creating new empty Monster (monsterlist was null or empty?)");
            m = new Monster();
        }

        return m;
    }

    public void addMonster(Monster m) {
        monsterList.add(m);
    }
}
