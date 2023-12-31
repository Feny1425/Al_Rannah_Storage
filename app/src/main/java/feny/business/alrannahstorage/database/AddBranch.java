package feny.business.alrannahstorage.database;

import android.content.Context;
import android.os.AsyncTask;

import feny.business.alrannahstorage.activities.AdminActivity;
import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.data.JsonMaker;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddBranch extends AsyncTask<String, Void, String> {
    private static final String API_URL = Data.BASE_URL("branches"); // Replace with your script URL
    AdminActivity context;

    public AddBranch(Context context, String permission, String name, String pass, String username) {
        this.context = (AdminActivity) context;
        execute(permission,name,pass,username);
    }

    @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String permission = params[0];
        String name = params[1];
        String location = params[2];
        String username = params[3];

            // Construct a JSON object with username and password
        JsonMaker jsonMaker = new JsonMaker();
        jsonMaker.addItem("permission",permission);
        jsonMaker.addItem("name",name);
        jsonMaker.addItem("location",location);
        jsonMaker.addItem("username",username);

            RequestBody body = RequestBody.create(JSON, jsonMaker.getJson());

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
            context.addResult(result);
        }
    }