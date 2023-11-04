package feny.business.alrannahstorage.database;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import feny.business.alrannahstorage.Objects.Branches;
import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.models.custom.Branch;
import feny.business.alrannahstorage.models.custom.Pages;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FetchStorage extends AsyncTask<String, Void, String> {
    private static final String API_URL = Data.BASE_URL("storage"); // Replace with your script URL
    Context context;
    boolean force = false;


    public FetchStorage(Pages context,boolean force) {
        this.context = context;

        execute();
    }

    @Override
    protected String doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();

        // Construct a JSON object with username and password

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
        if(Branches.getSize()>0) {
            try {
                jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    int storageID = jsonObject.getInt("storage_id");
                    int branchID = jsonObject.getInt("id");
                    int itemID;
                    if (!jsonObject.isNull("item_id")) {
                        itemID = jsonObject.getInt("item_id");
                    } else {
                        itemID = -1; // Set a default value or handle it as needed
                    }
                    int itemTypeID;
                    if (!jsonObject.isNull("item_type_id")) {
                        itemTypeID = jsonObject.getInt("item_type_id");
                    } else {
                        itemTypeID = -1; // Set a default value or handle it as needed
                    }
                    int quantity = jsonObject.getInt("quantity");
                    Branch branch = Branches.getBranchByID(branchID);

                    if (branch != null) {
                        if (branch.getStorageByID(storageID) == null) {
                            branch.addItems(storageID,branchID,itemID,itemTypeID,quantity);
                        }
                        else if (branch.getStorageByID(storageID).getQuantity() != quantity){
                            branch.getStorageByID(storageID).setQuantity(quantity);
                        }
                    }
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }if (onDataChangedListener != null) {
            onDataChangedListener.onDataChanged(force);
        }


    }


    public interface OnDataChangedListener {
        void onDataChanged(boolean force);
    }
    private static OnDataChangedListener onDataChangedListener;
    public static void setOnDataChangedListener(OnDataChangedListener listener) {
        onDataChangedListener = listener;
    }
}