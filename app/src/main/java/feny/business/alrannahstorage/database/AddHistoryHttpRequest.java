package feny.business.alrannahstorage.database;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import feny.business.alrannahstorage.activities.AdminActivity;
import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.data.JsonMaker;
import feny.business.alrannahstorage.models.Pages;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddHistoryHttpRequest extends AsyncTask<String, Void, String> {
    private static final String API_URL = Data.BASE_URL("add_history"); // Replace with your script URL
    Pages context;

    public AddHistoryHttpRequest(Context context, int id, int quantity, int new_quantity, int old_quantity, boolean add, int importB, int exportB) {
        this.context = (Pages) context;
        execute(String.valueOf(id),
                String.valueOf(quantity),
                String.valueOf(new_quantity),
                String.valueOf(old_quantity),
                String.valueOf(add?1:0),
                String.valueOf(importB),
                String.valueOf(exportB)
        );
    }


    @Override
    protected String doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        // Construct a JSON object with username and password
        JsonMaker jsonMaker = new JsonMaker();
        int i = 0;
        String id = params[i++];
        String qunt = params[i++];
        String newquant = params[i++];

        String oldquant = params[i++];
        String add = params[i++];
        String importq = params[i++];

        String export = params[i++];

        jsonMaker.addItem("id",id);
        jsonMaker.addItem("quantity",qunt);
        jsonMaker.addItem("new_quantity",newquant);
        jsonMaker.addItem("old_quantity",oldquant);
        jsonMaker.addItem("add",add);
        jsonMaker.addItem("import",importq);
        jsonMaker.addItem("export",export);

        String json = jsonMaker.getIntJson();
        RequestBody body = RequestBody.create(JSON,json );

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
    }
}