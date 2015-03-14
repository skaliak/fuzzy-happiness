package com.skaliak.tutorialapp;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.skaliak.MonSightingClient;
import com.skaliak.MonSightingClient.*;

import java.util.ArrayList;
import java.util.List;



public class PlanStep3 extends ActionBarActivity {
//public class PlanStep3 extends ListActivity {  //

    private List<Monster> monsterList;
    private DataSinglet data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("***** ps3", "in onCreate, before executing async task..");
        setContentView(R.layout.activity_plan_step3b);
        ListView lv = (ListView) findViewById(R.id.mon_lv_ps3b);

        data = DataSinglet.getInstance();
        if (data.hasList()) {
            Log.d("***** ps3", "using existing list from datasinglet");
            monsterList = data.getMonsterList();
            MonArrayAdapter adapter = new MonArrayAdapter(this, monsterList);
            //setListAdapter(adapter);  //
            lv.setAdapter(adapter);
            data.setSubscriber(adapter);
            setupClicks(lv);
        }
        else new getMonAsync().execute(this);

        if (data.isLoggedIn()){
            String user = data.getUser();
            if (data.isFirstLogin())
                Toast.makeText(this, "Logged in as " + user, Toast.LENGTH_SHORT).show();
        }
    }


    private void setupClicks(ListView lv){
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataSinglet.getInstance().select(position);
                Intent intent = new Intent(PlanStep3.this, MonDetailView.class);
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

        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.new_monster:
                newMonIntent();
                return false;
            case R.id.switch_acct:
                switchAcct();
                return false;
        }

        return super.onOptionsItemSelected(item);
    }

    private void switchAcct() {
        //TODO double-check that logOut() does the right thing
        data.logOut();
        //will this show?
        Toast.makeText(getApplicationContext(), "logged out", Toast.LENGTH_LONG).show();
        NavUtils.navigateUpFromSameTask(this);
    }

    private void newMonIntent() {
        Intent intent = new Intent(this, NewMonster.class);
        startActivity(intent);
    }

    class getMonAsync extends AsyncTask<Context, Void, List<Monster>> {

        Context theContext;

        protected List<Monster> doInBackground(Context... c) {
            theContext = c[0];
            MonSightingClient.MonSpottingApi client = MonSightingClient.GetClient(false);
            return client.monsters();
        }

        protected void onPostExecute(List<Monster> monList) {
            Log.d("***** ps3", "in postExecute handler");
            if (monList != null)
                monsterList = monList;
            else
                monsterList = new ArrayList<Monster>();
            Log.d("***** planstep3", "list has this many monsters: " + monsterList.size());
            DataSinglet data = DataSinglet.getInstance();
            data.setMonsterList(monsterList);

            MonArrayAdapter adapter = new MonArrayAdapter(this.theContext, monsterList);
            ListView lv = (ListView) findViewById(R.id.mon_lv_ps3b);
            lv.setAdapter(adapter);
            //setListAdapter(adapter); //
            data.setSubscriber(adapter);
            setupClicks(lv);
        }
    }

    class MonArrayAdapter extends ArrayAdapter<Monster> {

        private Context context;
        private List<Monster> monsters;

        public MonArrayAdapter(Context context, List<Monster> monsters) {
            super(context, R.layout.activity_plan_step3, monsters);
            this.context = context;
            this.monsters = monsters;
            //setNotifyOnChange(true);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View monView = inflater.inflate(R.layout.activity_plan_step3, parent, false);

            TextView tv = (TextView) monView.findViewById(R.id.mon_name);
            ImageView img = (ImageView) monView.findViewById(R.id.mon_icon);

            //TODO use actual image in listview instead of generic icon?
            img.setImageResource(R.drawable.frank_sq);

            tv.setText(monsters.get(position).name);

            return monView;
        }
    }
}


