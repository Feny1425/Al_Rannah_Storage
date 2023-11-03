package feny.business.alrannahstorage.Objects;

import java.util.ArrayList;

import feny.business.alrannahstorage.models.custom.Item;
import feny.business.alrannahstorage.models.custom.ItemType;
import feny.business.alrannahstorage.models.custom.Recipe;
import feny.business.alrannahstorage.models.custom.RecipeItem;

public class Items {
    private static ArrayList<Item> items = new ArrayList<>();
    private static ArrayList<ItemType> itemTypes = new ArrayList<>();
    private static ArrayList<Recipe> recipes = new ArrayList<>();
    private static ArrayList<RecipeItem> recipeItems = new ArrayList<>();


    //region add objects
    // add item
    public static void addItem(Item item) {
        for(Item _item : items){
            if(item.getId() == _item.getId()){
                return;
            }
        }
        items.add(item);
    }

    // add item type
    public static void addItemType(ItemType itemType) {
        for (ItemType _itemType : itemTypes){
            if(itemType.getId() == _itemType.getId()){
                return;
            }
        }
        itemTypes.add(itemType);
    }

    // add recipe of item type
    public static void addRecipe(Recipe recipe) {
        for(Recipe _recipe : recipes){
            if(recipe.getRecipe_id() == _recipe.getRecipe_id()){
                return;
            }
        }
        recipes.add(recipe);
    }
    // add item of the recipe
    public static void addRecipeItem(RecipeItem recipeItem) {
        for(RecipeItem _recipe : recipeItems){
            if(recipeItem.getRecipe_item_id() == _recipe.getRecipe_item_id()){
                return;
            }
        }
        recipeItems.add(recipeItem);
    }
    //endregion

    //region reset
    public static void reset() {
        items = new ArrayList<>();
        itemTypes = new ArrayList<>();
    }
    //endregion

    //region items
    //get item by its item id
    public static Item getItemByID(int id) {
        for (Item item : items) {
            if (item.getId() == id) {
                return item;
            }
        }
        return new Item("",-1,"",0);
    }

    //get all items
    public static ArrayList<Item> getItems() {
        return items;
    }
    //endregion

    //region item types
    //get item type by its item type id
    public static ItemType getItemTypeByID(int id) {
        for (ItemType itemType : itemTypes) {
            if (itemType.getId() == id) {
                return itemType;
            }
        }
        return null;
    }

    //get all item types
    public static ArrayList<ItemType> getItemTypes() {
        return itemTypes;
    }
    //endregion

    //region recipes
    //get recipe by its recipe id
    public static Recipe getRecipeByID(int id){
        for (Recipe recipe : recipes){
            if(recipe.getRecipe_id() == id){
                return recipe;
            }
        }
        return null;
    }
    //get all recipes
    public static ArrayList<Recipe> getRecipes(){
        return recipes;
    }
    //endregion

    //region recipe items
    //get recipe item by its recipe item id
    public static RecipeItem getRecipeItemsByID(int id){
        for (RecipeItem recipe : recipeItems){
            if(recipe.getRecipe_item_id() == id){
                return recipe;
            }
        }
        return null;
    }
    //get all recipe items
    public static ArrayList<RecipeItem> getRecipeItems(){
        return recipeItems;
    }
    //get all recipe items by recipe id
    public static ArrayList<RecipeItem> getRecipeItemsByRecipeID(int id){
        ArrayList<RecipeItem> _recipeItems = new ArrayList<>();
        for (RecipeItem recipe : recipeItems){
            if(recipe.getRecipe_id() == id){
                _recipeItems.add(recipe);
            }
        }
        return _recipeItems;
    }
    //endregion

}