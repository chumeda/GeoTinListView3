package chumeda.geotinlistview3;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chu on 11/29/15.
 */
public class ViewAllPosts extends AppCompatActivity implements ListView.OnItemClickListener, View.OnClickListener {

    private ListView listView;
    private Button refreshButton;
    private Button newPost;
    private Button mapView;

    private String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_posts);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        refreshButton = (Button) findViewById(R.id.refresh);
        newPost = (Button) findViewById(R.id.newPostfromList);
        mapView = (Button) findViewById(R.id.mapViewFromList);
        refreshButton.setOnClickListener(this);
        newPost.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflaterAdd = (LayoutInflater) ViewAllPosts.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupViewAdd = layoutInflaterAdd.inflate(R.layout.popup_add_post, (ViewGroup) findViewById(R.id.popupAddPost));
                final PopupWindow popupWindowAdd = new PopupWindow(popupViewAdd, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                popupWindowAdd.showAtLocation(popupViewAdd, Gravity.CENTER, 0, 0);

                popupWindowAdd.setFocusable(true);
                popupWindowAdd.update();
                Button buttonAdd;
                Button buttonExitAdd;

                //initializing views
                final EditText editTextTitle = (EditText) popupViewAdd.findViewById(R.id.editTextTitle);
                final EditText editTextDescription = (EditText) popupViewAdd.findViewById(R.id.editTextDescription);
                final DatePicker datePickerStartDate = (DatePicker) popupViewAdd.findViewById(R.id.datePickerStartDate);
                final DatePicker datePickerEndDate = (DatePicker) popupViewAdd.findViewById(R.id.datePickerEndDate);
                final TimePicker timePickerStartTime = (TimePicker) popupViewAdd.findViewById(R.id.timePickerStartTime);
                final TimePicker timePickerEndTime = (TimePicker) popupViewAdd.findViewById(R.id.timePickerEndTime);
                Log.d("test", "hello");
                buttonAdd = (Button) popupViewAdd.findViewById(R.id.buttonAdd);

                buttonExitAdd = (Button) popupViewAdd.findViewById(R.id.exitAdd);
                buttonExitAdd.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        popupWindowAdd.dismiss();
                    }
                });

                //setting listeners to buttons
                buttonAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindowAdd.dismiss();
                        final String title = editTextTitle.getText().toString().trim();
                        final String description = editTextDescription.getText().toString().trim();

                        //Dates
                        int monthStart = datePickerStartDate.getMonth();
                        int dayStart = datePickerStartDate.getDayOfMonth();
                        int yearStart = datePickerStartDate.getYear();
                        final String dateStart = String.valueOf(yearStart) + "-" + String.valueOf(monthStart) + "-" + String.valueOf(dayStart);
                        int monthEnd = datePickerEndDate.getMonth();
                        int dayEnd = datePickerEndDate.getDayOfMonth();
                        int yearEnd = datePickerEndDate.getYear();
                        final String dateEnd = String.valueOf(yearEnd) + "-" + String.valueOf(monthEnd) + "-" + String.valueOf(dayEnd);

                        //Times
                        int timePickerStartTimeHour = timePickerStartTime.getCurrentHour();
                        int timePickerStartTimeMin = timePickerStartTime.getCurrentMinute();
                        int timePickerEndTimeHour = timePickerEndTime.getCurrentHour();
                        int timePickerEndTimeMin = timePickerEndTime.getCurrentMinute();
                        final String timeStart = String.valueOf(timePickerStartTimeHour) + ":" + String.valueOf(timePickerStartTimeMin);
                        final String timeEnd = String.valueOf(timePickerEndTimeHour) + ":" + String.valueOf(timePickerEndTimeMin);

                        //Location
                        //Get Location Manager object from system service LOCATION_SERVICE
                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        //Get the current location
                        Location mylocation = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                        //get latitude of current location
                        double latitudeNum = mylocation.getLatitude();
                        //get longitude of current location
                        double longitudeNum = mylocation.getLongitude();
                        //Post marker only if user is within UH
                        if (latitudeNum > 21.291918 && latitudeNum < 21.310791 && longitudeNum > -157.821747 && longitudeNum < -157.808540) {
                            //set longitude and latitude string
                            final String longitude = String.valueOf(longitudeNum);
                            final String latitude = String.valueOf(latitudeNum);
                        } else {
                            longitudeNum = 0;
                            latitudeNum = 0;
                            new AlertDialog.Builder(ViewAllPosts.this).setTitle("Oh no!").setMessage("You're outside the bounds of UH Manoa :(").setNeutralButton("Okay", null).show();
                        }

                        //double longitudeNum = 150.000;
                        //double latitudeNum = 150.000;
                        final String longitude = String.valueOf(longitudeNum);
                        final String latitude = String.valueOf(latitudeNum);

                        Log.d("test", title);
                        Log.d("test", description);
                        Log.d("test", longitude);
                        Log.d("test", latitude);
                        Log.d("test", dateStart);
                        Log.d("test", dateEnd);
                        Log.d("test", timeStart);
                        Log.d("test", timeEnd);


                        class AddPostDo extends AsyncTask<Void, Void, String> {
                            ProgressDialog loading;

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                loading = ProgressDialog.show(chumeda.geotinlistview3.ViewAllPosts.this, "Adding...", "Waiting...", false, false);
                            }

                            @Override
                            protected void onPostExecute(String s) {
                                super.onPostExecute(s);
                                loading.dismiss();
                                Toast.makeText(chumeda.geotinlistview3.ViewAllPosts.this, s, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            protected String doInBackground(Void... v) {
                                HashMap<String, String> params = new HashMap<>();
                                params.put(Config.KEY_POST_TITLE, title);
                                params.put(Config.KEY_POST_DESCRIPTION, description);
                                params.put(Config.KEY_POST_LATITUDE, latitude);
                                params.put(Config.KEY_POST_LONGITUDE, longitude);
                                params.put(Config.KEY_POST_DATE_START, dateStart);
                                params.put(Config.KEY_POST_TIME_START, timeStart);
                                params.put(Config.KEY_POST_DATE_END, dateEnd);
                                params.put(Config.KEY_POST_TIME_END, timeEnd);

                                RequestHandler rh = new RequestHandler();
                                String res = rh.sendPostRequest(Config.URL_ADD, params);
                                return res;
                            }
                        }

                        if (latitudeNum != 0 && longitudeNum != 0) {
                            AddPostDo ap = new AddPostDo();
                            ap.execute();
                        }
                    }
                });
            }
        });
        mapView.setOnClickListener(this);
        getJSON();
    }

    private void showPost() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            Log.d("test", JSON_STRING);
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                final String id = jo.getString(Config.TAG_ID);
                final String title = jo.getString(Config.TAG_TITLE);
                final String description = jo.getString(Config.TAG_DESCRIPTION);
                final String dateStart = jo.getString(Config.KEY_POST_DATE_START);
                final String dateEnd = jo.getString(Config.KEY_POST_DATE_END);
                final String timeStart = jo.getString(Config.KEY_POST_TIME_START);
                final String timeEnd = jo.getString(Config.KEY_POST_TIME_END);

                final double longitude = jo.getDouble(Config.TAG_LONGITUDE);
                final double latitude = jo.getDouble(Config.TAG_LATITUDE);
                final String location = "Location (latitude, longitude): (" + latitude + ", " + longitude + ")";

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        LayoutInflater layoutInflater = (LayoutInflater) ViewAllPosts.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View popupView = layoutInflater.inflate(R.layout.popup, (ViewGroup) findViewById(R.id.Popup));
                        final PopupWindow popupWindow = new PopupWindow(popupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                        TextView titleText = (TextView) popupView.findViewById(R.id.titlePopup);
                        TextView descriptionText = (TextView) popupView.findViewById(R.id.descriptionPopup);
                        TextView dateStartText = (TextView) popupView.findViewById(R.id.dateStartPopup);
                        TextView timeStartText = (TextView) popupView.findViewById(R.id.timeStartPopup);
                        TextView dateEndText = (TextView) popupView.findViewById(R.id.dateEndPopup);
                        TextView timeEndText = (TextView) popupView.findViewById(R.id.timeEndPopup);
                        TextView locationText = (TextView) popupView.findViewById(R.id.locationPopup);

                        titleText.setText("Title: " + title);
                        descriptionText.setText("Description: " + description);
                        dateStartText.setText("Start Date: " + dateStart);
                        dateEndText.setText("End Date: " + dateEnd);
                        timeStartText.setText("Start Time: " + timeStart);
                        timeEndText.setText("End Time: " + timeEnd);
                        locationText.setText("Location (latitude, longitude): (" + latitude + ", " + longitude + ")");

                        Button editButton = (Button) popupView.findViewById(R.id.editPopup);
                        Button exitButton = (Button) popupView.findViewById(R.id.exitPopup);
                        exitButton.setOnClickListener(new Button.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow.dismiss();
                            }
                        });
                        editButton.setOnClickListener(new Button.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                popupWindow.dismiss();
                                LayoutInflater layoutInflaterEdit = (LayoutInflater) ViewAllPosts.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View popupViewEdit = layoutInflaterEdit.inflate(R.layout.activity_update_post, (ViewGroup) findViewById(R.id.EditPost));
                                final PopupWindow popupWindowEdit = new PopupWindow(popupViewEdit, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                                popupWindowEdit.showAtLocation(popupViewEdit, Gravity.CENTER, 0, 0);
                                popupWindowEdit.setFocusable(true);
                                popupWindowEdit.update();

                                EditText editTextIdEdit;
                                final EditText editTextTitleEdit;
                                final EditText editTextDescriptionEdit;
                                final DatePicker datePickerStartDateEdit;
                                final DatePicker datePickerEndDateEdit;
                                final TimePicker timePickerStartTimeEdit;
                                final TimePicker timePickerEndTimeEdit;

                                editTextIdEdit = (EditText) popupViewEdit.findViewById(R.id.editTextId);
                                editTextTitleEdit = (EditText) popupViewEdit.findViewById(R.id.editTextTitle);
                                editTextDescriptionEdit = (EditText) popupViewEdit.findViewById(R.id.editTextDescription);
                                datePickerStartDateEdit = (DatePicker) popupViewEdit.findViewById(R.id.datePickerStartDate);
                                datePickerEndDateEdit = (DatePicker) popupViewEdit.findViewById(R.id.datePickerEndDate);
                                timePickerStartTimeEdit = (TimePicker) popupViewEdit.findViewById(R.id.timePickerStartTime);
                                timePickerEndTimeEdit = (TimePicker) popupViewEdit.findViewById(R.id.timePickerEndTime);

                                Button buttonUpdate;
                                Button buttonDelete;
                                Button buttonExitEdit;

                                buttonUpdate = (Button) popupViewEdit.findViewById(R.id.buttonUpdate);
                                buttonDelete = (Button) popupViewEdit.findViewById(R.id.buttonDelete);
                                buttonExitEdit = (Button) popupViewEdit.findViewById(R.id.exitEdit);

                                editTextIdEdit.setText(id);
                                editTextTitleEdit.setText(title);
                                editTextDescriptionEdit.setText(description);

                                Integer yearStart = Integer.valueOf(dateStart.substring(0, 4));
                                Integer monthStart = Integer.valueOf(dateStart.substring(5, 7));
                                Integer dayStart = Integer.valueOf(dateStart.substring(8));

                                Integer yearEnd = Integer.valueOf(dateEnd.substring(0, 4));
                                Integer monthEnd = Integer.valueOf(dateEnd.substring(5, 7));
                                Integer dayEnd = Integer.valueOf(dateEnd.substring(8));

                                datePickerStartDateEdit.updateDate(yearStart, monthStart, dayStart);
                                datePickerEndDateEdit.updateDate(yearEnd, monthEnd, dayEnd);

                                Integer hourStart = Integer.valueOf(timeStart.substring(0, 2));
                                Integer minStart = Integer.valueOf(timeStart.substring(3, 5));
                                Integer hourEnd = Integer.valueOf(timeEnd.substring(0, 2));
                                Integer minEnd = Integer.valueOf(timeEnd.substring(3, 5));

                                timePickerStartTimeEdit.setCurrentHour(hourStart); //setCurrentHour deprecated API level 23 use setHour
                                timePickerStartTimeEdit.setCurrentMinute(minStart); //setCurrentMinute deprecated API level 23 use setMinute

                                timePickerEndTimeEdit.setCurrentHour(hourEnd);
                                timePickerEndTimeEdit.setCurrentMinute(minEnd);

                                buttonExitEdit.setOnClickListener(new Button.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        popupWindowEdit.dismiss();
                                    }
                                });

                                buttonUpdate.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        final String title = editTextTitleEdit.getText().toString().trim();
                                        final String description = editTextDescriptionEdit.getText().toString().trim();
                                        //Dates
                                        int monthStart = datePickerStartDateEdit.getMonth();
                                        int dayStart = datePickerStartDateEdit.getDayOfMonth();
                                        int yearStart = datePickerStartDateEdit.getYear();
                                        final String dateStart = String.valueOf(yearStart) + "-" + String.valueOf(monthStart) + "-" + String.valueOf(dayStart);
                                        int monthEnd = datePickerEndDateEdit.getMonth();
                                        int dayEnd = datePickerEndDateEdit.getDayOfMonth();
                                        int yearEnd = datePickerEndDateEdit.getYear();
                                        final String dateEnd = String.valueOf(yearEnd) + "-" + String.valueOf(monthEnd) + "-" + String.valueOf(dayEnd);

                                        //Times
                                        int timePickerStartTimeHour = timePickerStartTimeEdit.getCurrentHour();
                                        int timePickerStartTimeMin = timePickerStartTimeEdit.getCurrentMinute();
                                        int timePickerEndTimeHour = timePickerEndTimeEdit.getCurrentHour();
                                        int timePickerEndTimeMin = timePickerEndTimeEdit.getCurrentMinute();
                                        final String timeStart = String.valueOf(timePickerStartTimeHour) + ":" + String.valueOf(timePickerStartTimeMin);
                                        final String timeEnd = String.valueOf(timePickerEndTimeHour) + ":" + String.valueOf(timePickerEndTimeMin);

                                        //Location
                                        final String longitudeEdit = String.valueOf(longitude);
                                        final String latitudeEdit = String.valueOf(latitude);

                                        class UpdatePostPressed extends AsyncTask<Void, Void, String> {
                                            ProgressDialog loading;

                                            @Override
                                            protected void onPreExecute() {
                                                super.onPreExecute();
                                                loading = ProgressDialog.show(chumeda.geotinlistview3.ViewAllPosts.this, "Fetching...", "Waiting...", false, false);
                                            }

                                            @Override
                                            protected void onPostExecute(String s) {
                                                super.onPostExecute(s);
                                                loading.dismiss();
                                                Toast.makeText(chumeda.geotinlistview3.ViewAllPosts.this, s, Toast.LENGTH_LONG).show();
                                            }

                                            @Override
                                            protected String doInBackground(Void... params) {
                                                HashMap<String, String> hashMap = new HashMap<>();
                                                hashMap.put(Config.KEY_POST_ID, id);
                                                hashMap.put(Config.KEY_POST_TITLE, title);
                                                hashMap.put(Config.KEY_POST_DESCRIPTION, description);
                                                hashMap.put(Config.KEY_POST_LATITUDE, latitudeEdit);
                                                hashMap.put(Config.KEY_POST_LONGITUDE, longitudeEdit);
                                                hashMap.put(Config.KEY_POST_DATE_START, dateStart);
                                                hashMap.put(Config.KEY_POST_TIME_START, timeStart);
                                                hashMap.put(Config.KEY_POST_DATE_END, dateEnd);
                                                hashMap.put(Config.KEY_POST_TIME_END, timeEnd);

                                                RequestHandler rh = new RequestHandler();

                                                String s = rh.sendPostRequest(Config.URL_UPDATE_POST, hashMap);

                                                return s;
                                            }
                                        }

                                        UpdatePostPressed up = new UpdatePostPressed();
                                        up.execute();
                                    }
                                });

                                buttonDelete.setOnClickListener(new Button.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewAllPosts.this);
                                        alertDialogBuilder.setMessage("Are you sure you want to delete this post?");

                                        alertDialogBuilder.setPositiveButton("Yes",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface arg0, int arg1) {
                                                        class DeletePost extends AsyncTask<Void, Void, String> {
                                                            ProgressDialog loading;

                                                            @Override
                                                            protected void onPreExecute() {
                                                                super.onPreExecute();
                                                                loading = ProgressDialog.show(ViewAllPosts.this, "Deleting...", "Wait...", false, false);
                                                            }

                                                            @Override
                                                            protected void onPostExecute(String s) {
                                                                super.onPostExecute(s);
                                                                loading.dismiss();
                                                                Toast.makeText(ViewAllPosts.this, s, Toast.LENGTH_LONG).show();
                                                                //startActivity(new Intent(MapView.this, ViewAllPosts.class));
                                                            }

                                                            @Override
                                                            protected String doInBackground(Void... params) {
                                                                HashMap<String, String> hashMap = new HashMap<>();
                                                                hashMap.put(Config.KEY_POST_ID, id);

                                                                RequestHandler rh = new RequestHandler();
                                                                String s = rh.sendPostRequest(Config.URL_DELETE_POST, hashMap);
                                                                Log.d("test", "s " + s);
                                                                return s;
                                                            }
                                                        }

                                                        DeletePost dp = new DeletePost();
                                                        dp.execute();
                                                        popupWindow.dismiss();
                                                    }
                                                });

                                        alertDialogBuilder.setNegativeButton("No",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface arg0, int arg1) {

                                                    }
                                                });

                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();
                                    }
                                });

                                /*popupWindow.dismiss();
                                Intent intent = new Intent(ViewAllPosts.this, UpdatePost.class);
                                intent.putExtra(Config.POST_ID,id);
                                intent.putExtra(Config.POST_DESCRIPTION,description);
                                intent.putExtra(Config.POST_DATE_START,dateStart);
                                intent.putExtra(Config.POST_DATE_END,dateEnd);
                                intent.putExtra(Config.POST_TIME_START,timeStart);
                                intent.putExtra(Config.POST_TIME_END,timeEnd);
                                intent.putExtra(Config.POST_LONGITUDE,longitude);
                                intent.putExtra(Config.POST_LATITUDE, latitude);
                                startActivity(intent);*/
                            }
                        });
                    }
                });
                HashMap<String, String> posts = new HashMap<>();

                posts.put(Config.TAG_TITLE, title);
                posts.put(Config.TAG_DESCRIPTION, description);
                list.add(posts);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                ViewAllPosts.this, list, R.layout.list_item,
                new String[]{Config.TAG_TITLE, Config.TAG_DESCRIPTION},
                new int[]{R.id.id, R.id.name});

        listView.setAdapter(adapter);
    }

    public void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewAllPosts.this, "Fetching Data...", "Wait...", false, false);
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
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.URL_GET_ALL);
                return s;
            }
        }

        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ViewPost.class);
        HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);

        String postId = map.get(Config.TAG_ID).toString();
        intent.putExtra(Config.POST_ID, postId);
        Log.d("test", postId);

        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v == refreshButton) {
            getJSON();
        }
        if (v == newPost) {
            //Intent intentPost = new Intent(this, AddPost.class);
            //startActivity(intentPost);
            addPostPopup();
        }
        if (v == mapView) {
            Intent intentMap = new Intent(this, MapView.class);
            startActivity(intentMap);
        }
    }

    public void addPostPopup() {


    }

}
