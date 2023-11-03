package feny.business.alrannahstorage.models.custom;

import java.util.ArrayList;

import feny.business.alrannahstorage.Objects.Branches;
import feny.business.alrannahstorage.Objects.Items;

public class Recipe {
    private final int recipe_id;
    private final int item_type_id;
    private final String description;
    private final int quantity;

    public int getQuantity() {
        return quantity;
    }

    public Recipe(int recipe_id, int item_type_id, String description, int quantity) {
        this.recipe_id = recipe_id;
        this.item_type_id = item_type_id;
        this.description = description;
        this.quantity = quantity;
    }

    public int getRecipe_id() {
        return recipe_id;
    }

    public int getItem_type_id() {
        return item_type_id;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<RecipeItem> getRecipeItems(){
        return Items.getRecipeItemsByRecipeID(recipe_id);
    }

    public boolean CanBeCooked(){
        for(RecipeItem recipeItem : getRecipeItems()){
            Storage storage = Branches.getStorageByItemID(recipeItem.getItem_id());
            if(storage == null){
                return false;
            }
            if(storage.getQuantity() < recipeItem.getQuantity()){
                return false;
            }
        }
        return true;
    }
    public int HowManyCanBeCooked(){
        int low = -1;
        if(CanBeCooked()){
            for(RecipeItem recipeItem : getRecipeItems()){
                Storage storage = Branches.getStorageByItemID(recipeItem.getItem_id());
                int qunat = storage.getQuantity()/recipeItem.getQuantity();
                if(low == -1){
                    low = qunat;
                }
                else if(qunat < low){
                    low = qunat;
                }
            }
        }
        else {
            return 0;
        }
        return low*quantity;
    }
}