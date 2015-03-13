package com.skaliak.tutorialapp;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.skaliak.MonSightingClient;
import com.skaliak.MonSightingClient.*;

import java.util.List;

//TODO delete this activity
public class PlanStep1 extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_step1);

        Log.d("***** ps1", "HaiiiiYoooooo!!!!!");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_plan_step1, menu);
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

        if (id == R.id.action_get_ps1) {
            getMonString();
        }

        return super.onOptionsItemSelected(item);
    }

    public void getMonString() {
        Log.d("***** ps1", "start getMonString");

        new getMonAsync().execute();
    }

    class getMonAsync extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... v) {
            MonSpottingApi client = MonSightingClient.GetClient(false);
            List<Monster> monsterList = client.monsters();
            String nameString = "";

            for(Monster m : monsterList) {
                nameString += "\n" + m.name;
            }

            return nameString;
        }

        protected void onPostExecute(String nameString) {
            Log.d("***** ps1", "in postExecute handler");
            Log.d("***** ps1", nameString);

            TextView tv = (TextView) findViewById(R.id.mon_list_ps1);
            tv.setText(nameString);
        }
    }
}
