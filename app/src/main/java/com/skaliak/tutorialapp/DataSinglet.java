package com.skaliak.tutorialapp;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

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
    private boolean list_changed;
    private ArrayAdapter subscriber;

    public void setMonsterList(List<Monster> incoming_list){
        Log.d("datasinglet", "setting monsterlist");
        if (monsterList != null)
            Log.d("datasinglet", "monsterlist was not null!");
        monsterList = incoming_list;
        selected = 0;
        list_changed = false;
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

    public void setSubscriber(ArrayAdapter a){
        subscriber = a;
    }

    public boolean wasListChanged() {
        return list_changed;
    }

    public boolean hasList() {
        return monsterList != null;
    }

    public List<Monster> getMonsterList() {
        list_changed = false;
        return monsterList;
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
        Log.d("datasinglet", "adding monster");
    }

    public void removeSelected() {
        Log.d("datasinglet", "removing selected monster");
        monsterList.remove(selected);
        selected = 0;
        list_changed = true;
        if (subscriber != null) {
            Log.d("datasinglet", "notifying subscribed adapter");
            subscriber.notifyDataSetChanged();
        }
    }
}
