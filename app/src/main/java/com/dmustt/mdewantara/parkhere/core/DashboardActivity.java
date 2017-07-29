package com.dmustt.mdewantara.parkhere.core;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dmustt.mdewantara.R;
import com.dmustt.mdewantara.parkhere.service.DashboardService;
import com.dmustt.mdewantara.stadium.schema.StadiumSchema;
import com.dmustt.mdewantara.stadium.core.StadiumActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import Utilities.ParkHereUtilities;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private ArrayList<StadiumSchema> stadiumSchemas;
    private DashboardService dashboardService;
    private ParkHereUtilities utilities;
    private CustomAdapter adapter;
    String baseUrl = "http://103.23.22.8";
    String apiPath = "/park-here/api/";
    String port = "3000";

    OkHttpClient client = new OkHttpClient();

    public static final String STADIUM_NAME = "dmustt.stadium.name";
    public static final String STADIUM_IMAGE = "dmustt.stadium.image";
    public static final String STADIUM_ID = "dmustt.stadium.id";
    public static final String STADIUM_ADDRESS = "dmustt.stadium.address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Recycler view
        recyclerView = (RecyclerView) findViewById(R.id.dashboard_recycler);
        stadiumSchemas = new ArrayList<>();

        dashboardService = new DashboardService();
        utilities = new ParkHereUtilities();

        // load data from server
        loadDataFromServer();
    }

    private void loadDataFromServer() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(baseUrl + ":" + port + apiPath + "stadiums")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(DashboardActivity.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();

                try {
                    JSONArray array = new JSONArray(responseData);
                    for (int i=0; i<array.length();i++) {
                        JSONObject object = array.getJSONObject(i);

                        StadiumSchema stadiumSchema = new StadiumSchema(object.getInt("id"),
                                object.getString("name"), object.getString("address"),
                                getPictureData(object.getInt("id")),
                                getStadiumCapacityData(object.getInt("id")));
                        stadiumSchemas.add(stadiumSchema);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Run view-related code back on the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gridLayoutManager = new GridLayoutManager(DashboardActivity.this, 1);
                        adapter = new CustomAdapter(DashboardActivity.this, stadiumSchemas);
                        recyclerView.setLayoutManager(gridLayoutManager);
                        recyclerView.setAdapter(adapter);
                    }
                });
            };
        });
    }

    private Integer getStadiumCapacityData(Integer stadiumId) {
        Integer data = 0;

        try {
            final String stadiumFloor = dashboardService.get(baseUrl + ":" + port + apiPath + "floors");

            for (int i=0; i<utilities.convertToArray(stadiumFloor).length();i++) {
                JSONObject object = utilities.convertToArray(stadiumFloor).getJSONObject(i);
                if (object.getInt("stadiumId") == stadiumId) {
                    data += object.getInt("capacity");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }

    private String getPictureData(Integer stadiumId) {
        String data = "";

        try {
            final String stadiumPicture = dashboardService.get(baseUrl + ":" + port + apiPath + "stadiums/"
                    + stadiumId + "/picture");

            if (!stadiumPicture.contains("{}")) {
                data = utilities.convertToJson(stadiumPicture).getString("data_picture");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void viewStadium(View view) {
        Intent intent = new Intent(this, StadiumActivity.class);

        // get position of card view when clicked
        int position = (int) view.getTag();

        intent.putExtra(STADIUM_ID, stadiumSchemas.get(position).getId());
        intent.putExtra(STADIUM_NAME, stadiumSchemas.get(position).getName());
        intent.putExtra(STADIUM_IMAGE, stadiumSchemas.get(position).getImage());
        intent.putExtra(STADIUM_ADDRESS, stadiumSchemas.get(position).getAddress());
        startActivity(intent);

    }
}
