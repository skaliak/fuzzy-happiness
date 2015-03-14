package com.skaliak.tutorialapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.skaliak.MonSightingClient;
import com.skaliak.MonSightingClient.*;

import java.util.List;


public class NewMonster extends ActionBarActivity {

    private Monster currentMonster;
    private boolean editing;
    DataSinglet data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_monster);
        data = DataSinglet.getInstance();

        String action = getIntent().getAction();
        if (action != null && action == Intent.ACTION_EDIT){
            Log.d("***** newmonster", "edit action specified");

            TextView caption = (TextView) findViewById(R.id.new_edit_caption);
            caption.setText("editing monster");

            setTitle("edit monster");

            currentMonster = data.getSelected();
            editing = true;
            populateForm();
        } else {
            editing = false;
            setTitle("new monster");
        }
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

    public void populateForm() {
        //take values from monster being edited and put them in the form
        EditText name = (EditText) findViewById(R.id.new_mon_name);
        EditText desc = (EditText) findViewById(R.id.new_mon_desc);
        EditText mon_url = (EditText) findViewById(R.id.new_mon_url);

        name.setText(currentMonster.name);
        desc.setText(currentMonster.description);
        mon_url.setText(currentMonster.image_url);
    }

    //save button handler
    public void saveMonster(View view) {
        Log.d("***** newmonster", "save button handler");
        Monster m;
        if (editing)
            m = currentMonster;
        else
            m = new Monster();
        EditText name = (EditText) findViewById(R.id.new_mon_name);
        m.name = name.getText().toString();
        EditText desc = (EditText) findViewById(R.id.new_mon_desc);
        m.description = desc.getText().toString();
        EditText mon_url = (EditText) findViewById(R.id.new_mon_url);
        m.image_url = mon_url.getText().toString();

        new saveMonAsync().execute(m);
        NavUtils.navigateUpFromSameTask(this);
    }

    public void cancel(View view) {
        //this should be the equivalent of hitting the "up" action bar button
        //or the back button
        NavUtils.navigateUpFromSameTask(this);
    }

    //AsyncTask<Params, Progress, Result>
    class saveMonAsync extends AsyncTask<Monster, Void, Monster> {

        //take this out?
        Context theContext;

        protected Monster doInBackground(Monster... m) {
            Log.d("***** newmonster", "doinbackground");
            MonSightingClient.MonSpottingApi client = MonSightingClient.GetClient(false);

            if (editing) {
                //this is a bit sloppy...
                client.update_monster(m[0].encoded_key, m[0]);
                return m[0];
            }
            return client.new_monster(m[0]);
        }

        protected void onPostExecute(Monster m) {
            Log.d("***** newmonster", "onpostexecute");
            if (!editing)
                data.addMonster(m);
            //does the toast show in the right place?
            Toast t = Toast.makeText(getApplicationContext(), "saved", Toast.LENGTH_LONG);
            t.setGravity(Gravity.TOP, 0, 100);
            t.show();
        }
    }
}
