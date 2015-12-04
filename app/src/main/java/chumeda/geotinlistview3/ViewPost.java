package chumeda.geotinlistview3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chu on 12/3/15.
 */
public class ViewPost extends AppCompatActivity implements View.OnClickListener {
    private TextView Ttitle;
    private TextView Tdescription;
    private TextView TstartDate;
    private TextView TstartTime;
    private TextView TendDate;
    private TextView TendTime;
    private TextView Tlocation;

    private Button buttonEdit;
    private Button buttonViewAllPosts;
    private Button buttonMapView;

    private String id;
    private String title;
    private String description;
    private String latitude;
    private String longitude;
    private String dateStart;
    private String dateEnd;
    private String timeStart;
    private String timeEnd;

    private String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        Intent intent = getIntent();

        id = intent.getStringExtra(Config.POST_ID);

        Ttitle = (TextView) findViewById(R.id.postTitle);
        Tdescription = (TextView) findViewById(R.id.postDescription);
        TstartDate = (TextView) findViewById(R.id.postStartDate);
        TstartTime = (TextView) findViewById(R.id.postEndDate);
        TendDate = (TextView) findViewById(R.id.postStartTime);
        TendTime = (TextView) findViewById(R.id.postEndTime);
        Tlocation = (TextView) findViewById(R.id.postLocation);
        buttonEdit = (Button) findViewById(R.id.EditPost);
        buttonViewAllPosts = (Button) findViewById(R.id.viewAllPosts);
        buttonMapView = (Button) findViewById(R.id.mapViewFromPost);

        buttonEdit.setOnClickListener(this);
        buttonViewAllPosts.setOnClickListener(this);
        buttonMapView.setOnClickListener(this);

        getJSON();
    }

    public void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewPost.this, "Fetching Data...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showPost();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_POST_ID, id);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Config.URL_GET_POST, hashMap);
                return s;
            }
        }

        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showPost() {
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(JSON_STRING);
            Log.d("test", JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            JSONObject jo = result.getJSONObject(0);
            id = jo.getString(Config.TAG_ID);
            title = jo.getString(Config.TAG_TITLE);
            description = jo.getString(Config.TAG_DESCRIPTION);
            longitude = jo.getString(Config.TAG_LONGITUDE);
            latitude = jo.getString(Config.TAG_LATITUDE);
            dateStart = jo.getString(Config.TAG_DATE_START);
            dateEnd = jo.getString(Config.TAG_DATE_END);
            timeStart = jo.getString(Config.TAG_TIME_START);
            timeEnd = jo.getString(Config.TAG_TIME_END);

            Ttitle.setText("Title: " + title);
            Tdescription.setText("Description: " + description);
            TstartDate.setText("Start Date: " + dateStart);
            TstartTime.setText("Start Time: " + timeStart);
            TendDate.setText("End Date: " + dateEnd);
            TendTime.setText("End Time: " + timeEnd);
            Tlocation.setText("Location (longitude,latitude): (" + latitude + ", " + longitude + ")");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == buttonEdit) {
            Intent intent = new Intent(this, UpdatePost.class);
            intent.putExtra(Config.POST_ID,id);
            intent.putExtra(Config.POST_DESCRIPTION,description);
            intent.putExtra(Config.POST_DATE_START,dateStart);
            intent.putExtra(Config.POST_DATE_END,dateEnd);
            intent.putExtra(Config.POST_TIME_START,timeStart);
            intent.putExtra(Config.POST_TIME_END,timeEnd);
            intent.putExtra(Config.POST_LONGITUDE,longitude);
            intent.putExtra(Config.POST_LATITUDE,latitude);
            Log.d("test", id);
            startActivity(intent);
        }

        if (v == buttonViewAllPosts) {
            Intent intentViewPosts = new Intent(this, ViewAllPosts.class);
            startActivity(intentViewPosts);
        }

        if (v == buttonMapView) {
            Intent intentMapView = new Intent(this, MapView.class);
            startActivity(intentMapView);
        }
    }
}
