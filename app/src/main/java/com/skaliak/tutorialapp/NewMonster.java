package com.skaliak.tutorialapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.skaliak.MonSightingClient;
import com.skaliak.MonSightingClient.*;

import java.util.List;


public class NewMonster extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_monster);
    }


    //begin actionbar stuff
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_monster, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //end actionbar stuff


    //save button handler
    public void saveMonster(View view) {
        Log.d("newmonster", "save button handler");
        Monster m = new Monster();
        EditText name = (EditText) findViewById(R.id.new_mon_name);
        m.name = name.getText().toString();
        EditText desc = (EditText) findViewById(R.id.new_mon_desc);
        m.description = desc.getText().toString();

        new saveMonAsync().execute(m);
    }

    public void cancel(View view) {
        //this should be the equivalent of hitting the "up" action bar button
        //or the back button
        NavUtils.navigateUpFromSameTask(this);
    }

    class saveMonAsync extends AsyncTask<Monster, Void, Monster> {

        //take this out?
        Context theContext;

        protected Monster doInBackground(Monster... m) {

            Log.d("newmonster", "doinbackground");
            MonSightingClient.MonSpottingApi client = MonSightingClient.GetClient(false);
            return client.new_monster(m[0]);
        }

        protected void onPostExecute(Monster m) {
            Log.d("newmonster", "onpostexecute");

            //TODO how to make this more noticible, or hide the kbd automatically?
            Toast.makeText(getApplicationContext(), "saved", Toast.LENGTH_LONG).show();
        }
    }
}
