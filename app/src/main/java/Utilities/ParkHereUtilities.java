package Utilities;

import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mdewantara on 7/20/17.
 */

public class ParkHereUtilities {

    public byte[] base64Decode(String imageBytes) {
        return Base64.decode(imageBytes, Base64.DEFAULT);
    }

    public JSONArray convertToArray(String data) throws JSONException {
        JSONArray arrayData = new JSONArray(data);

        return arrayData;
    }

    public JSONObject convertToJson(String data) throws JSONException {
        JSONObject object = new JSONObject(data);

        return object;
    }
}
