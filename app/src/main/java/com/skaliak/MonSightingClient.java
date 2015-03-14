package com.skaliak;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.LauncherApps;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.skaliak.tutorialapp.DataSinglet;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
import retrofit.client.Response;
import retrofit.http.*;
import retrofit.converter.GsonConverter;

import java.util.Date;
import java.util.List;


public class MonSightingClient {
    private static final String API_URL = "http://monspotting.appspot.com/api/v1";
    //private static final String API_URL = "http://localhost:8080/api/v1";

    public static class Monster {
        public String name;
        public String description;
        public String image_url;
        public String created_by;
        public String encoded_key;

        public boolean equals(Object obj) {
            if (obj instanceof Monster) {
                Monster m = (Monster)obj;
                return encoded_key.equals(m.encoded_key);
            }
            return false;
        }
    }

    public static class Sighting {
        public Date timestamp;
        public String location;
        public String sighted_by;
        public String notes;
        public String encoded_key;
        public double lat;
        public double lng;

        public boolean equals(Object obj) {
            if (obj instanceof Sighting) {
                Sighting s = (Sighting)obj;
                return encoded_key.equals(s.encoded_key);
            }
            return false;
        }
    }

    public interface MonSpottingApi {
        //get all monsters
        @GET("/Monsters/")
        List<Monster> monsters();

        //get one monster by id (encoded key)
        @GET("/Monsters/{id}")
        Monster monster(@Path("id") String id);

        //get all sightings
        @GET("/Sightings/")
        List<Sighting> sightings();

        //get sighting of a monster (by id)
        @GET("/Monsters/{id}/Sightings/")
        List<Sighting> sightings_of_monster(@Path("id") String id);

        //get sightings in an area
        @GET("/Sightings/")
        List<Sighting> sightings(@Query("ne") String ne, @Query("sw") String sw);

        //create new monster
        @POST("/Monsters/")
        Monster new_monster(@Body Monster mon);

        //create sighting of monster
        @POST("/Monsters/{id}/Sightings/")
        Sighting new_sighting(@Path("id") String id, @Body Sighting s);

        //TODO implement async versions of these:
        //update monster
        @PUT("/Monsters/{id}")
        Response update_monster(@Path("id") String id, @Body Monster m);

        //delete monster
        @DELETE("/Monsters/{id}")
        Response delete_monster(@Path("id") String id);

        //delete monster
        @DELETE("/Monsters/{id}")
        void delete_monster_async(@Path("id") String id, Callback<Void> cb);

        //delete sighting
        //does the api support this?
        @DELETE("/Sightings/{id}")
        Response delete_sighting(@Path("id") String id);

        @DELETE("/Sightings/{id}")
        void delete_sighting(@Path("id") String id, Callback<Void> cb);
    }

    public static class GenericCallback implements Callback<Void> {

        private Context context;

        public GenericCallback(Context context) {
            this.context = context;
        }

        @Override
        public void success(Void aVoid, Response response) {
            Log.d("***** genericcallback", "success");
            Toast toast = Toast.makeText(context, "operation succeeded", Toast.LENGTH_LONG);
            //toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
            postSuccess();
        }

        @Override
        public void failure(RetrofitError error) {
            Log.d("***** genericcallback", "fail");
            Toast toast = Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG);
            //toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        }

        protected void postSuccess() {}
    }

    public static class DelMonCallback extends GenericCallback {
        public DelMonCallback(Context context) {
            super(context);
        }

        @Override
        protected void postSuccess() {
            //delete monster from list
            DataSinglet.getInstance().removeSelected();
        }
    }

    public static MonSpottingApi GetClient(boolean dbg)
    {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestInterceptor.RequestFacade request) {
                //Log.d("api client", "request interceptor");
                DataSinglet ds = DataSinglet.getInstance();
                if (ds.isLoggedIn()) {
                    Log.d("api client", "interceptor says logged in");
                    //Log.d("api client", "data singlet getCookie returns: " + ds.getCookie());
                    //TODO is there a way to use a cookie store to simplify this process?
                    request.addHeader("Cookie", ds.getCookie());
                }
            }
        };

        RestAdapter restAdapter;

        if (dbg) {
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(API_URL)
                    .setRequestInterceptor(requestInterceptor)
                    .setConverter(new GsonConverter(gson))
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setLog(new AndroidLog("retrofit"))
                    .build();
        } else {
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(API_URL)
                    .setRequestInterceptor(requestInterceptor)
                    .setConverter(new GsonConverter(gson))
                    .build();
        }

        MonSpottingApi client = restAdapter.create(MonSpottingApi.class);

        return client;
    }

}