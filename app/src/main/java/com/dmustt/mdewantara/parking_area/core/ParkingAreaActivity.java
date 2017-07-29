package com.dmustt.mdewantara.parking_area.core;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.dmustt.mdewantara.R;
import com.dmustt.mdewantara.parking_area.schema.ParkingAreaSchema;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ParkingAreaActivity extends AppCompatActivity {

    private ArrayList<ParkingAreaSchema> parkingAreaSchemas;
    String baseUrl = "http://103.23.22.8";
    String apiPath = "/park-here/api/";
    String port = "3000";
    String name = "";
    Integer stadiumId = 0;
    Integer capacity = 0;
    Integer id = 0;

    public static final String DMUSTT_TAG = "the_dmustt_message";

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_area);

        parkingAreaSchemas = new ArrayList<>();

        loadDataFromServer();
    }

    private void loadDataFromServer() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(baseUrl + ":" + port + apiPath + "parking-areas")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(ParkingAreaActivity.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();

                try {
                    JSONArray array = new JSONArray(responseData);
                    for (int i=0; i<array.length();i++) {
                        JSONObject object = array.getJSONObject(i);
                        ParkingAreaSchema parkingAreaSchema = new ParkingAreaSchema(object.getInt("id"),
                                object.getString("parking_area_code"),
                                 object.getInt("floorId"));
                        if (id == object.getInt("floorId")) {
                            parkingAreaSchemas.add(parkingAreaSchema);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Run view-related code back on the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            };
        });
    }


}
