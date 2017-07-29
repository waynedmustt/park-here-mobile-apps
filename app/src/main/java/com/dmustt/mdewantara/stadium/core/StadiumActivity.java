package com.dmustt.mdewantara.stadium.core;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;

import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dmustt.mdewantara.R;
import com.dmustt.mdewantara.floor.schema.FloorSchema;
import com.dmustt.mdewantara.parkhere.core.DashboardActivity;
import com.dmustt.mdewantara.stadium.service.StadiumService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import Utilities.ParkHereUtilities;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StadiumActivity extends AppCompatActivity {

    private ParkHereUtilities utilities;
    private ArrayList<FloorSchema> floorSchemas;
    private StadiumService stadiumService;
    String baseUrl = "http://103.23.22.8";
    String apiPath = "/park-here/api/";
    String port = "3000";
    int year_x;
    int month_x;
    int day_x;
    String hour_x;
    String setDate = "";
    int paIndex = 0;

    public static final String DMUSTT_TAG = "the_dmustt_message";

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stadium);

        // set calendar now()
        final Calendar calendar = Calendar.getInstance();
        year_x = calendar.get(Calendar.YEAR);
        month_x = calendar.get(Calendar.MONTH);
        day_x = calendar.get(Calendar.DAY_OF_MONTH);

        // call utilities
        utilities = new ParkHereUtilities();

        // call service
        stadiumService = new StadiumService();

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String name = intent.getStringExtra(DashboardActivity.STADIUM_NAME);
        String address = intent.getStringExtra(DashboardActivity.STADIUM_ADDRESS);
        String image = intent.getStringExtra(DashboardActivity.STADIUM_IMAGE);
        Integer id = intent.getIntExtra(DashboardActivity.STADIUM_ID, 0);

        ImageView stadiumImage = (ImageView) findViewById(R.id.image_view_stadium_detail);
        Glide.with(StadiumActivity.this)
                .load(utilities.base64Decode(image))
                .into(stadiumImage);

        TextView stadiumName = (TextView) findViewById(R.id.stadium_detail_title);
        TextView stadiumAddress = (TextView) findViewById(R.id.stadium_detail_address);
        stadiumName.setText(name);

        // set address
        stadiumAddress.setText(address);
        if (address == "null") {
            stadiumAddress.setText("No Address Available");
        }

        // my_child_toolbar is defined in the layout file
        Toolbar myChildToolbar =
                (Toolbar) findViewById(R.id.stadium_detail_toolbar);
        myChildToolbar.setTitle("");
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        floorSchemas = new ArrayList<>();

        loadDataFromServer();
    }

    @Override
    protected Dialog onCreateDialog(int index) {
        paIndex = index;
        return new DatePickerDialog(this, dpickerlistener, year_x, month_x, day_x);
    }

    DatePickerDialog.OnDateSetListener dpickerlistener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            year_x = year;
            month_x = month + 1;
            day_x = day;

            // get complete format time
            final Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
            hour_x = sdf.format(c.getTime());

            // set validation for date format
            String newMonthFormat = month_x > 10 ? "" + month_x : "0" + month_x;
            String newDayFormat = day_x > 10 ? "" + day_x : "0" + day_x;
            setDate = year_x + "-" + newMonthFormat + "-" + newDayFormat + "T" + hour_x + "Z";

            // send data to server
            sendDataToServer();
        }
    };

    private void loadDataFromServer() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(baseUrl + ":" + port + apiPath + "floors")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(StadiumActivity.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();

                try {
                    JSONArray array = new JSONArray(responseData);
                    for (int i=0; i<array.length();i++) {
                        JSONObject object = array.getJSONObject(i);
                        FloorSchema floorSchema = new FloorSchema(object.getInt("id"), object.getString("name"),
                                object.getInt("capacity"), object.getInt("stadiumId"));
                        floorSchemas.add(floorSchema);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Run view-related code back on the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LinearLayout ll = (LinearLayout) findViewById(R.id.floor_layout);

                        // render button for floor availability in stadium
                        for (int i=0;i<floorSchemas.size();i++) {
                            final Button floorButton = new Button(StadiumActivity.this);
                            floorButton.setId(i+1);
                            floorButton.setTag(i);
                            floorButton.setPadding(5, 5, 5, 5);
                            floorButton.setWidth(500);
                            floorButton.setText("Floor " + floorSchemas.get(i).getName());
                            ll.addView(floorButton);

                            final int floorIndex = i;
                            floorButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(StadiumActivity.this);
                                    builder.setTitle("Parking Area");
                                    View parkingAreaView = getLayoutInflater().inflate(R.layout.activity_parking_area, null);

                                    builder.setView(parkingAreaView);
                                    final AlertDialog dialog = builder.create();
                                    dialog.show();

                                    final ArrayList<String> parkingArea = getParkingAreaData(floorSchemas.get(floorIndex).getId());
                                    LinearLayout parkingAreaLl = (LinearLayout) parkingAreaView.findViewById(R.id.parking_area_layout);

                                    for (int i=0;i<parkingArea.size();i++) {
                                        final Button parkingAreaButton = new Button(StadiumActivity.this);
                                        parkingAreaButton.setId(i+1);
                                        parkingAreaButton.setTag(i);
                                        parkingAreaButton.setPadding(5, 5, 5, 5);
                                        parkingAreaButton.setWidth(250);
                                        parkingAreaButton.setText(parkingArea.get(i));
                                        parkingAreaLl.addView(parkingAreaButton);

                                        final int parkingAreaIndex = i;
                                        parkingAreaButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                showDialog(parkingAreaIndex);
                                            }
                                        });

                                    }
                                }
                            });
                        }
                    }
                });
            };
        });
    }

    private void sendDataToServer() {
        OkHttpClient client = new OkHttpClient();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Map<String, String> params = new HashMap<String, String>();
        params.put("booked_at", setDate);
        params.put("parkingAreaId", String.valueOf(floorSchemas.get(paIndex).getId()));
        JSONObject parameter = new JSONObject(params);

        RequestBody formBody = RequestBody.create(JSON, parameter.toString());

        Request request = new Request.Builder()
                .url(baseUrl + ":" + port + apiPath + "bookings")
                .post(formBody)
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();

                // Run view-related code back on the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(StadiumActivity.this, "Parking Area has been booked", Toast.LENGTH_SHORT).show();
                    }
                });
            };
        });
    }

    private ArrayList<String> getParkingAreaData(Integer floorId) {
        ArrayList<String> arrayList = new ArrayList<String>();

        try {
            // force to permit all of connection
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                final String stadiumParkingArea = stadiumService.get(baseUrl + ":" + port + apiPath + "parking-areas");

                for (int i=0;i<utilities.convertToArray(stadiumParkingArea).length();i++) {
                    JSONObject object = utilities.convertToArray(stadiumParkingArea).getJSONObject(i);
                    if (floorId == object.getInt("floorId")) {
                        arrayList.add(object.getString("parking_area_code"));
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayList;
    }

}
