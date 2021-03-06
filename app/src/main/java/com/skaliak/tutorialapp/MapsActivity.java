package com.skaliak.tutorialapp;

import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.skaliak.MonSightingClient;
import com.skaliak.MonSightingClient.*;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class MapsActivity extends ActionBarActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Monster currentMonster;
    private List<Sighting> sightings;
    private MonSpottingApi client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ab);
        client = MonSightingClient.GetClient(false);

        currentMonster = DataSinglet.getInstance().getSelected();
        if (currentMonster == null)
            Log.d("***** MapsActivity", "null currentmonster in oncreate");
        else
            setTitle(currentMonster.name + " sightings");

        //do this last, you dumbass
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ab, menu);
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

        if (id == R.id.new_sighting) {
            newSighting();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void newSighting() {
        //sighting marker is created in current map center (camera target)
        LatLng pos = mMap.getCameraPosition().target;
        Date now = new Date();
        Sighting sighting = new Sighting();

        sighting.lat = pos.latitude;
        sighting.lng = pos.longitude;
        sighting.timestamp = now;

        new PostSightingAsync().execute(sighting);
        addMarker(sighting);
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        Log.d("***** MapsActivity", "in setupmap");
        mMap.setMyLocationEnabled(true);
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        //add all markers here (execute async task)

        //TODO (low) probably safe to take some null checks out...
        if (currentMonster != null) {
            String key = currentMonster.encoded_key;
            if (key != null) {
                Log.d("***** MapsActivity", "getting sightings of " + currentMonster.name);
                new GetSightingsAsync().execute(currentMonster.encoded_key);
            } else {
                Log.d("***** MapsActivity", "null key from currentmonster, can't lookup sightings");
            }
        } else {
            Log.d("***** MapsActivity", "currentmonster was null in setupmap??");
        }
    }

    public LatLng addMarker(MonSightingClient.Sighting s){
        Log.d("***** MapsActivity", "adding a marker for a sighting");
        LatLng loc = new LatLng(s.lat, s.lng);
        MarkerOptions opt = new MarkerOptions()
                .position(loc)
                .title(s.timestamp.toString());
        mMap.addMarker(opt);

        return loc;
    }

    private class PostSightingAsync extends AsyncTask<Sighting, Void, Void> {
        @Override
        protected Void doInBackground(Sighting... s) {
            //TODO add some error checking here?
            String key = currentMonster.encoded_key;

            //uncomment next line to enable retrofit debugging
            //client = MonSightingClient.GetClient(true);
            client.new_sighting(key, s[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getApplicationContext(), "saved", Toast.LENGTH_LONG).show();
        }
    }

    private class GetSightingsAsync extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... keys) {
            sightings = client.sightings_of_monster(keys[0]);
            if (sightings != null)
                Log.d("***** MapsActivity", "got this many sightings: " + sightings.size());
            else
                Log.d("***** MapsActivity", "no sightings returned");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //default location (PDX)
            LatLng loc = new LatLng(45.5, -122.5);

            if (sightings != null) {
                LatLngBounds.Builder builder = LatLngBounds.builder();
                //add marker for each sighting
                for (Sighting s : sightings) {
                    loc = addMarker(s);
                    builder = builder.include(loc);
                }
                LatLngBounds bounds = builder.build();

                //center map on sightings
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
            }
        }
    }
}
