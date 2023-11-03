package feny.business.alrannahstorage.database;
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import feny.business.alrannahstorage.Objects.Items;
import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.models.custom.Item;
import feny.business.alrannahstorage.models.custom.ItemType;
import feny.business.alrannahstorage.models.custom.Pages;
import feny.business.alrannahstorage.models.custom.Recipe;
import feny.business.alrannahstorage.models.custom.RecipeItem;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FetchItemsClass extends Thread {
    private static final String API_URL = Data.BASE_URL("fetch_items_class");
    Pages context;
    String action;

    public FetchItemsClass(Pages context, String action) {
        this.context = context;
        this.action = action;
    }
    private class Fetch extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse(API_URL).newBuilder();
            urlBuilder.addQueryParameter("action", "all_data"); // Request all data
            String url = urlBuilder.build().toString();

            Request request = new Request.Builder()
                    .url(url)
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
            try {
                JSONObject jsonResponse = new JSONObject(result);
                if (jsonResponse.has("data")) {
                    JSONObject data = jsonResponse.getJSONObject("data");
                    handleData(data);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void handleData(JSONObject data) throws JSONException {
            if (data.has("items")) {
                JSONArray itemsArray = data.getJSONArray("items");
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject jsonObject = itemsArray.getJSONObject(i);
                    Items.addItem(itemFromJSON(jsonObject));
                }
            }

            if (data.has("item_types")) {
                JSONArray itemTypesArray = data.getJSONArray("item_types");
                for (int i = 0; i < itemTypesArray.length(); i++) {
                    JSONObject jsonObject = itemTypesArray.getJSONObject(i);
                    Items.addItemType(itemTypeFromJSON(jsonObject));
                }
            }

            if (data.has("recipes")) {
                JSONArray recipesArray = data.getJSONArray("recipes");
                for (int i = 0; i < recipesArray.length(); i++) {
                    JSONObject jsonObject = recipesArray.getJSONObject(i);
                    Items.addRecipe(recipeFromJSON(jsonObject));
                }
            }

            if (data.has("recipe_items")) {
                JSONArray recipeItemsArray = data.getJSONArray("recipe_items");
                for (int i = 0; i < recipeItemsArray.length(); i++) {
                    JSONObject jsonObject = recipeItemsArray.getJSONObject(i);
                    Items.addRecipeItem(recipeItemFromJSON(jsonObject));
                }
            }
        }
    }
    // Your methods for converting JSON to objects (itemFromJSON, itemTypeFromJSON, etc.) remain the same
    private RecipeItem recipeItemFromJSON(JSONObject jsonObject) throws JSONException {
        // Implement your logic to convert JSON to RecipeItem object
        // Example:
        int ID = jsonObject.getInt("recipe_items_id");
        int recipe_id = jsonObject.getInt("recipe_id");
        int item_id = jsonObject.getInt("item_id");
        int quantity = jsonObject.getInt("quantity");
        return new RecipeItem(ID, recipe_id, item_id, quantity);
    }

    private Recipe recipeFromJSON(JSONObject jsonObject) throws JSONException {
        // Implement your logic to convert JSON to Recipe object
        // Example:
        int ID = jsonObject.getInt("recipe_id");
        int itemTypeID = jsonObject.getInt("item_type_id");
        int quantity = jsonObject.getInt("quantity");
        String description = jsonObject.getString("description");
        return new Recipe(ID, itemTypeID, description, quantity);
    }

    private ItemType itemTypeFromJSON(JSONObject jsonObject) throws JSONException {
        // Implement your logic to convert JSON to ItemType object
        // Example:
        int ID = jsonObject.getInt("id");
        int sold = jsonObject.getInt("can_be_sold");
        String name = jsonObject.getString("type_name");
        String unit = jsonObject.getString("unit");
        return new ItemType(ID, name, unit, sold);
    }

    private Item itemFromJSON(JSONObject jsonObject) throws JSONException {
        // Implement your logic to convert JSON to Item object
        // Example:
        int ID = jsonObject.getInt("id");
        int food = jsonObject.getInt("food");
        String unit = jsonObject.getString("unit");
        String name = jsonObject.getString("name");
        return new Item(name, ID, unit, food);
    }

    @Override
    public void run() {
        super.run();
        Fetch fetch = new Fetch();
        fetch.execute();
    }
}