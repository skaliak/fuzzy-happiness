package com.skaliak.tutorialapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.skaliak.MonSightingClient;

import java.net.URL;


public class MonDetailView extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_detail_view);

        MonSightingClient.Monster m = DataSinglet.getInstance().getSelected();

//        Intent intent = getIntent();
//        String name = intent.getStringExtra("mon_name");
//        String desc = intent.getStringExtra("mon_desc");
//        String url = intent.getStringExtra("mon_img_url");
        String name = m.name;
        String desc = m.description;
        String url = m.image_url;
        String created_by = m.created_by;

        TextView tv_name = (TextView) findViewById(R.id.mon_detail_name);
        TextView tv_desc = (TextView) findViewById(R.id.mon_desc);
        TextView tv_cb = (TextView) findViewById(R.id.mon_created_by);
        ImageView iv = (ImageView) findViewById(R.id.mon_portrait);

        tv_desc.setText(desc);
        tv_name.setText(name);

        if(! created_by.isEmpty())
            tv_cb.setText("created by " +created_by);

        //this actually works!  use a different placeholder though...
        Ion.with(iv)
                .centerCrop()
                .placeholder(android.R.drawable.ic_menu_slideshow)
                .load(url);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mon_detail_view, menu);
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

        if (id == R.id.edit_mon) {
            editMonster();
            return false;
        }

        if (id == R.id.view_sightings) {
            viewSightings();
            //not sure about these return statements...
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    public void editMonster() {
        //TODO start new activity or switch this one into edit mode somehow?
        Log.d("mondetailview", "edit clicked");

    }

    public void viewSightings() {
        //TODO start new activity to view sightings on a map
        Log.d("mondetailview", "sightings clicked");
    }
}
