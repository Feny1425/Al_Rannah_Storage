package feny.business.alrannahstorage.database;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import feny.business.alrannahstorage.Objects.Branches;
import feny.business.alrannahstorage.Objects.Items;
import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.models.Pages;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FetchStorageTypesFromServer extends AsyncTask<String, Void, String> {
    private static final String API_URL = Data.BASE_URL("fetch_storage_type"); // Replace with your script URL
    Context context;

    public FetchStorageTypesFromServer(Pages context) {
        this.context = context;
        execute();
    }

    @Override
    protected String doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        // Construct a JSON object with username and password
        String json = "{}";

        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                return "Error: " + response.code();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        // Handle the response here
        // The 'result' contains the response from your PHP script
        //Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int ID = jsonObject.getInt("id");
                String type = jsonObject.getString("type");

                Items.addItemType(type,ID);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
