package feny.business.alrannahstorage.database;

import android.content.Context;
import android.os.AsyncTask;

import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.data.JsonMaker;
import feny.business.alrannahstorage.models.custom.Pages;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdateStorage extends AsyncTask<String, Void, String> {
    private static final String API_URL = Data.BASE_URL("storage"); // Replace with your script URL
    private final int id;
    private final int quantity;
    private final int newQuantity;
    private final int oldQuantity;
    private boolean add;
    private final int importB;
    private final int exportB;
    private int closed = 0;
    Context context;
    String branchID;
    String salted = "nothing";

    public UpdateStorage(Pages context, String storageID, String branchID,
                         int id, int quantity, int new_quantity, int old_quantity,
                         boolean add, int importB, int exportB, int closed) {
        this.context = context;
        this.branchID = branchID;
        execute(storageID,String.valueOf(new_quantity));
        this.id = id;
        this.quantity = quantity;
        this.newQuantity = new_quantity;
        this.oldQuantity = old_quantity;
        this.add = add;
        this.importB = importB;
        this.exportB = exportB;
        this.closed = closed;

    }
    public UpdateStorage(Pages context, String branchID,
                         int id, int quantity, int new_quantity, int old_quantity,
                         boolean add, int importB, int exportB, int closed, String salted) {
        this.context = context;
        this.branchID = branchID;
        execute(String.valueOf(id),String.valueOf(new_quantity));
        this.id = id;
        this.quantity = quantity;
        this.newQuantity = new_quantity;
        this.oldQuantity = old_quantity;
        this.add = add;
        this.importB = importB;
        this.exportB = exportB;
        this.closed = closed;
        this.salted = salted;

    }

    @Override
    protected String doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String id = params[0];
        String quantity = params[1];

        JsonMaker jsonMaker = new JsonMaker();
        jsonMaker.addItem("id","\""+id+"\"");
        jsonMaker.addItem("quantity",quantity);

        // Construct a JSON object with username and password

        RequestBody body = RequestBody.create(JSON, jsonMaker.getIntJson());

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
        new FetchStorage((Pages) context,true);
        new AddHistory((Pages) context,
                id,
                quantity,
                newQuantity,
                oldQuantity,
                add,
                importB,
                exportB,
                closed,
                (salted.equals("nothing")?Data.getSaltString():salted));
        // This is an example of calling the callback from within your adapter.
    }
}