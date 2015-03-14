package com.skaliak.tutorialapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class PlanStep3b extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_step3b);

        final String[] monArray = {"one", "two", "three"};

        if(DataSinglet.getInstance().isLoggedIn()) {
            Log.d("***** PlanStep3b", "yes logged in");
            Log.d("***** PlanStep3b", DataSinglet.getInstance().getCookie());
        }

        ListView lv = (ListView) findViewById(R.id.mon_lv_ps3b);
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 ,monArray));

        //same thing as onListItemClick in PlanStep3, which is a ListActivity
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataSinglet.getInstance().select(position);
                Intent intent = new Intent(PlanStep3b.this, MonDetailView.class);

                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_plan_step3b, menu);
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
}
