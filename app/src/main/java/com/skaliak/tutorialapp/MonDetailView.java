package com.skaliak.tutorialapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;
import com.skaliak.MonSightingClient;
import com.skaliak.MonSightingClient.*;

import java.net.URL;


public class MonDetailView extends ActionBarActivity {

    private MonSpottingApi client;
    private Monster currentMon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        client = MonSightingClient.GetClient(false);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_detail_view);

        currentMon = DataSinglet.getInstance().getSelected();
        MonSightingClient.Monster m = currentMon;

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

        if(created_by != null && !created_by.isEmpty())
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

        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.edit_mon:
                editMonster();
                return false;
            case R.id.view_sightings:
                viewSightings();
                return false;
            case R.id.del_mon:
                deleteMonsterWithConf();
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteMonster() {
        //make async call to delete monster
        //remove monster from local list (done by callback)
        //then show toast message
        //then navigate UP
        Log.d("mondetailview", "attempting to delete monster async");
        GenericCallback cb = new DelMonCallback(this);
        String key = currentMon.encoded_key;
        client.delete_monster_async(key, cb);

        //will this work??
        NavUtils.navigateUpFromSameTask(this);
    }

    public void deleteMonsterWithConf() {
        new AlertDialog.Builder(this)
                .setMessage("Delete this Monster?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteMonster();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void editMonster() {
        //TODO start new activity or switch this one into edit mode somehow?
        Log.d("mondetailview", "edit clicked");

    }

    public void viewSightings() {
        //start new activity to view sightings on a map
        Log.d("mondetailview", "sightings clicked");
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
