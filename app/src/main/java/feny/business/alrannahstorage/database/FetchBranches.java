package feny.business.alrannahstorage.database;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import feny.business.alrannahstorage.Objects.Branches;
import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.models.custom.Branch;
import feny.business.alrannahstorage.models.custom.Pages;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FetchBranches extends Thread {
    private static final String API_URL = Data.BASE_URL("branches"); // Replace with your script URL
    Pages context;
    String username;

    public FetchBranches(Pages context, String user) {
        this.context = context;
        this.username = user;
    }
    private class Fetch extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();

            // Construct a JSON object with username and password
// Build the URL with query parameters
            HttpUrl.Builder urlBuilder = HttpUrl.parse(API_URL).newBuilder();
            urlBuilder.addQueryParameter("action", username); // Add the "action" query parameter
            String url = urlBuilder.build().toString();

            Request request = new Request.Builder().url(url) // Use the constructed URL
                    .get() // Specifies that this is a GET request
                    .addHeader("Content-Type", "application/json").build();


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
                    if (!permission.equals(Data.getAdminPermssion())) {
                        Branch branch = new Branch(name, id, permission, location, user);
                        branches.add(branch);
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            new FetchStorage(context,false);
            Branches.setBranches(branches, context);
            context.refresh();

            if (onDataChangedListener != null) {
                onDataChangedListener.onDataChanged();
            }


        }

    }

    @Override
    public void run() {
        super.run();
        Fetch fetch = new Fetch();
        fetch.execute();
    }

    public interface OnDataChangedListener {
        void onDataChanged();
    }

    private static OnDataChangedListener onDataChangedListener;

    public static void setOnDataChangedListener(OnDataChangedListener listener) {
        onDataChangedListener = listener;
    }
}