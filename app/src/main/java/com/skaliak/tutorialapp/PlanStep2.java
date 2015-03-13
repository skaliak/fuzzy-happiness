package com.skaliak.tutorialapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ListActivity;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.skaliak.MonSightingClient;

import java.util.List;
import java.util.Vector;

//TODO delete this activity
public class PlanStep2 extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("***** ps2", "in onCreate, before executing async task..");

        new getMonAsync().execute(this);

        //what's this do?
        //getListView().setTextFilterEnabled(true);
    }

    class getMonAsync extends AsyncTask<Context, Void, String[]> {

        Context theContext;

        protected String[] doInBackground(Context... c) {
            theContext = c[0];

            MonSightingClient.MonSpottingApi client = MonSightingClient.GetClient(false);
            List<MonSightingClient.Monster> monsterList = client.monsters();
            Vector<String> nameVector = new Vector<String>();

            for(MonSightingClient.Monster m : monsterList) {
                nameVector.add(m.name);
            }

            return nameVector.toArray(new String[0]);
        }

        protected void onPostExecute(String[] nameStrings) {
            Log.d("***** ps2", "in postExecute handler");

            setListAdapter(new ArrayAdapter<String>(this.theContext,
                    android.R.layout.simple_list_item_1, nameStrings));
        }
    }

}
