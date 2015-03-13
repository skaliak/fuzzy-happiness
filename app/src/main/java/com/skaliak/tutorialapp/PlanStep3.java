package com.skaliak.tutorialapp;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.skaliak.MonSightingClient;
import com.skaliak.MonSightingClient.*;

import java.util.List;

//TODO everything in here should be moved into an actionbar activity with a listview (like 3b)
//or just convert this to an actionbaractivity...
public class PlanStep3 extends ListActivity {

    private List<Monster> monsterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("***** ps3", "in onCreate, before executing async task..");

        DataSinglet data = DataSinglet.getInstance();
        if (data.hasList()) {
            Log.d("***** ps3", "using existing list from datasinglet");
            monsterList = data.getMonsterList();
            MonArrayAdapter adapter = new MonArrayAdapter(this, monsterList);
            setListAdapter(adapter);
            data.setSubscriber(adapter);
        }
        else new getMonAsync().execute(this);

    }

    //TODO this can probably be removed
    @Override
    protected void onResume() {
        Log.d("planstep3", "onresume");
        super.onResume();
        DataSinglet data = DataSinglet.getInstance();
        if (data.wasListChanged()) {
            MonArrayAdapter a = (MonArrayAdapter) getListAdapter();
            Log.d("planstep3", "list has this many monsters on resume: " + a.monsters.size());
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        //super.onListItemClick(l, v, position, id);
        DataSinglet.getInstance().select(position);
        Intent intent = new Intent(this, MonDetailView.class);

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
            monsterList = monList;
            Log.d("planstep3", "list has this many monsters: " + monList.size());
            DataSinglet data = DataSinglet.getInstance();
            data.setMonsterList(monsterList);

            MonArrayAdapter adapter = new MonArrayAdapter(this.theContext, monsterList);
            setListAdapter(adapter);
            data.setSubscriber(adapter);
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
            img.setImageResource(R.drawable.mmm_icon);

            tv.setText(monsters.get(position).name);

            return monView;
        }
    }
}


