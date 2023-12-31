package feny.business.alrannahstorage.database;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import feny.business.alrannahstorage.Objects.Histories;
import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.models.custom.Pages;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FetchHistory extends AsyncTask<String, Void, String> {
    private static final String API_URL = Data.BASE_URL("fetch_history"); // Replace with your script URL
    Pages context;

    public FetchHistory(Pages context) {
        this.context = context;
        execute();
    }

    @Override
    protected String doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(API_URL)
                .get()
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
        Histories.reset();
        try {
            jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int id = jsonObject.getInt("id");
                int storage_id = jsonObject.getInt("storage_id");
                int added = jsonObject.getInt("added");
                String date = jsonObject.getString("date");
                int quantity = jsonObject.getInt("quantity");
                int new_quantity = jsonObject.getInt("new_quantity");
                int old_quantity = jsonObject.getInt("old_quantity");
                int branch_import_id = jsonObject.getInt("import");
                int branch_export_id = jsonObject.getInt("export");
                int closed = jsonObject.getInt("closed");
                String operation = jsonObject.getString("operation");

                Histories.AddHistory(id,storage_id,added,date,
                        quantity,new_quantity,old_quantity,branch_import_id,
                        branch_export_id,closed,operation);
                //context.refresh();
            }
            Histories.getNonClosedOperationsByBranch(context);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        FetchBranches fetchBranches = new FetchBranches(context, Data.getUSER());
        fetchBranches.run();
    }
}