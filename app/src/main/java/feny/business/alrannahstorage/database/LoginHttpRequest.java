package feny.business.alrannahstorage.database;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import feny.business.alrannahstorage.activities.LoginActivity;
import feny.business.alrannahstorage.data.Data;
import okhttp3.*;

public class LoginHttpRequest extends AsyncTask<String, Void, String> {
    private static final String API_URL = Data.BASE_URL("login"); // Replace with your script URL
    LoginActivity context;

    public LoginHttpRequest(Context context, String user, String pass) {
        this.context = (LoginActivity) context;
        execute(user,pass);
    }

    @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            String username = params[0];
            String password = params[1];

            // Construct a JSON object with username and password
            String json = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";

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
            context.login(result);
        }
    }