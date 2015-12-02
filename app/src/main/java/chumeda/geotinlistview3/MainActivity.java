package chumeda.geotinlistview3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextTitle;
    private EditText editTextDescription;
    private DatePicker datePickerStartDate;
    private DatePicker datePickerEndDate;
    private TimePicker timePickerStartTime;
    private TimePicker timePickerEndTime;


    private Button buttonAdd;
    private Button buttonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing views
        editTextTitle = (EditText) findViewById(R.id.editTextName);
        editTextDescription = (EditText) findViewById(R.id.editTextUserName);
        datePickerStartDate = (DatePicker) findViewById(R.id.datePickerStartDate);
        datePickerEndDate = (DatePicker) findViewById(R.id.datePickerEndDate);
        timePickerStartTime = (TimePicker) findViewById(R.id.timePickerStartTime);
        timePickerEndTime = (TimePicker) findViewById(R.id.timePickerEndTime);

        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonView = (Button) findViewById(R.id.buttonView);

        //setting listeners to buttons
        buttonAdd.setOnClickListener(this);
        buttonView.setOnClickListener(this);
    }

    //adding post
    private void addPost() {

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
        double longitudeNum = 150.000;
        double latitudeNum = 150.000;
        final String longitude = String.valueOf(longitudeNum);
        final String latitude = String.valueOf(latitudeNum);

        Log.d("test",title);
        Log.d("test",description);
        Log.d("test",longitude);
        Log.d("test",latitude);
        Log.d("test",dateStart);
        Log.d("test",dateEnd);
        Log.d("test",timeStart);
        Log.d("test",timeEnd);


        class AddPost extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Adding...", "Waiting...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
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

        AddPost ap = new AddPost();
        ap.execute();
    }

    @Override
    public void onClick(View v) {
        if (v == buttonAdd) {
            addPost();
        }
        if (v == buttonView) {
            Intent intent = new Intent(this, ViewAllPosts.class);
            Log.d("test", "intent test");
            startActivity(intent);
        }
    }
}