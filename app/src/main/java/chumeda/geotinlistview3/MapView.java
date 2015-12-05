package chumeda.geotinlistview3;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by chu and ella on 12/2/15.
 */
public class MapView extends FragmentActivity {

    private GoogleMap mMap;
    private LatLng  latLng;

    String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_map_view);
        setUpMapIfNeeded();
        getJSON();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

/*    public void dropMarker(View view) {
        EditText post = (EditText) findViewById(R.id.postContent);
        String postString = post.getText().toString();

        //Get Location Manager object from system service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Get the current location
        Location mylocation = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);

        Log.d("test", mylocation.toString());
        //get latitude of current location
        double latitude = mylocation.getLatitude();

        //get longitude of current location
        double longitude = mylocation.getLongitude();
        Log.d("test","map");
        //Create latlng object of current location
        latLng = new LatLng(latitude,longitude);

        //show current location in google maps
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        //Zoom in google ,ap
        mMap.animateCamera(CameraUpdateFactory.zoomIn());

        //Post marker only if user is within UH
        if(latitude > 21.291918 && latitude < 21.310791 && longitude > -157.821747 && longitude < -157.808540) {
            //add marker to current location
            Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title(postString));
        } else {
            new AlertDialog.Builder(this).setTitle("Oh no!").setMessage("You're outside the bounds of UH Manoa :(").setNeutralButton("Okay",null).show();
        }
    }*/

    public void onPost(View view) {
        Log.d("test", "intent post");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    public void onViewPosts(View view) {
        Intent intent = new Intent(this, ViewAllPosts.class);
        startActivity(intent);
    }

    public void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MapView.this, "Fetching Data...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                //showPost();
                onPopulate();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.URL_GET_ALL);
                return s;
            }
        }

        GetJSON gj = new GetJSON();
        gj.execute();
    }

    public void onPopulate() {
        Log.d("test","onpopulate");
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String title = jo.getString(Config.TAG_TITLE);
                String description = jo.getString(Config.TAG_DESCRIPTION);
                double longitude = jo.getDouble(Config.TAG_LONGITUDE);
                double latitude = jo.getDouble(Config.TAG_LATITUDE);

                putMarker(title, description, latitude, longitude);
            }
        } catch (JSONException e) {

        }
    }

    public void putMarker(String title, String description, double latitude, double longitude) {

        Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(title).snippet(description));
    }

    public void onZoom(View view) {
        if(view.getId() == R.id.Bzoomin) {
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        }
        if(view.getId() == R.id.Bzoomout) {
            mMap.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }

    //set up map type
    public void changeType(View view){
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    private void setUpMapIfNeeded() {
        //do null check to confirm that we have not already instantiated the map
        if(mMap == null) {
            // try to obtain the map from the Support Map Fragment
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            //check if we were successful in obtaining the map
            if(mMap != null) {
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
                        marker.showInfoWindow();
                        return true;
                    }
                });
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        //enable myLocation layer of google map
        mMap.setMyLocationEnabled(true);

        //Get Location Manager object from system service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Get the current location
        Location mylocation = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);

        //get latitude of current location
        double latitude = mylocation.getLatitude();

        //get longitude of current location
        double longitude = mylocation.getLongitude();
        Log.d("test","map");
        //Create latlng object of current location

        LatLng latLng = new LatLng(latitude,longitude);

        latLng = new LatLng(latitude,longitude);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

    }
}
