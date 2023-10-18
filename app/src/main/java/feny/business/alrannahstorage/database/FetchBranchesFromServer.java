package feny.business.alrannahstorage.database;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import feny.business.alrannahstorage.Objects.Branches;
import feny.business.alrannahstorage.activities.AdminActivity;
import feny.business.alrannahstorage.activities.MainActivity;
import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.models.Branch;
import feny.business.alrannahstorage.models.Pages;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FetchBranchesFromServer extends AsyncTask<String, Void, String> {
    private static final String API_URL = Data.BASE_URL("get_branches"); // Replace with your script URL
    Pages context;

    public FetchBranchesFromServer(Pages context, String user) {
        this.context = context;
        execute(user);
    }

    @Override
    protected String doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String username = params[0];

        // Construct a JSON object with username and password
        String json = "{\"user\":\""+username+"\"}";

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
        ArrayList<Branch> branches = new ArrayList<>();
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String name = jsonObject.getString("name");
                int id = jsonObject.getInt("id");
                String permission = jsonObject.getString("permission");
                String location = jsonObject.getString("location");
                String user = jsonObject.getString("user");

                // Create a Branch object and add it to the list
                if(!permission.equals(Data.getAdminPermssion())) {
                    Branch branch = new Branch(name, id, permission, location, user);
                    branches.add(branch);
                    new FetchStorageFromServer(context,String.valueOf(id));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Branches.setBranches(branches, context);
    }
}
