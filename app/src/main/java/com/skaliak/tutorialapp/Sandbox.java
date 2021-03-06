package com.skaliak.tutorialapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;


public class Sandbox extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sandbox);

        NumberPicker np = (NumberPicker) findViewById(R.id.numberPicker);
        np.setMaxValue(100);
        np.setMinValue(0);

        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        String msg = pref.getString("my_secret_string", "empty");
        if (msg != null)
            Log.d("***** sandbox", "msg was " + msg);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("my_secret_string", "heyyyoooo!");

        editor.apply();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sandbox, menu);
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

    public void testSomething(View view) {
//        new AlertDialog.Builder(this)
//                //.setTitle("Titel")
//                .setMessage("Do you really want to whatever?")
//                //.setIcon(android.R.drawable.ic_dialog_alert)
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        Toast.makeText(Sandbox.this, "Yaay", Toast.LENGTH_SHORT).show();
//                    }})
//                .setNegativeButton(android.R.string.no, null).show();

        NumberPicker p = (NumberPicker) findViewById(R.id.numberPicker);
        int i = p.getValue();

        Toast t = Toast.makeText(getApplicationContext(), "boo!", Toast.LENGTH_LONG);
        t.setGravity(Gravity.TOP, 0, i);
        t.show();
    }
}
