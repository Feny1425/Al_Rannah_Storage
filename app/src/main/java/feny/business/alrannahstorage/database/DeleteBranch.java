package feny.business.alrannahstorage.database;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import feny.business.alrannahstorage.activities.AdminActivity;
import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.data.JsonMaker;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DeleteBranch extends AsyncTask<String, Void, String> {
    private static final String API_URL = Data.BASE_URL("delete_branch"); // Replace with your script URL
    AdminActivity context;

    public DeleteBranch(Context context, String user, String permission) {
        this.context = (AdminActivity) context;
        execute(user,permission);
    }

    @Override
    protected String doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String username = params[0];
        String permission = params[1];

        // Construct a JSON object with username and password
        JsonMaker js = new JsonMaker();
        js.addItem("user",username);
        js.addItem("permission",permission);

        RequestBody body = RequestBody.create(JSON, js.getJson());

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
        FetchBranches fetchBranches = new FetchBranches(context, Data.getUSER());
        fetchBranches.run();
        Toast.makeText(context,"تم الحذف بنجاح", Toast.LENGTH_LONG).show();
    }
}