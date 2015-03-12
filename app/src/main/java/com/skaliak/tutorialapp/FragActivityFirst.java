package com.skaliak.tutorialapp;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.TextView;


public class FragActivityFirst extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag_activity_first);
        if (savedInstanceState == null) {

            TextFragment frag = new TextFragment();
            Bundle args = new Bundle();
            args.putString("text", "holy poo!");
            frag.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, frag)
                    .commit();

            View v = frag.getView();
            if (v != null) {
                TextView tv = (TextView) v.findViewById(R.id.the_text);
                tv.setText("this is a different way");
            }
        }

        Log.d("FragActivityFirst", "End of onCreate");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_frag_activity_first, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
}
