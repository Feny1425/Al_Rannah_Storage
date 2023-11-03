package feny.business.alrannahstorage.models.custom;

public class RecipeItem {
    private final int recipe_item_id;
    private final int recipe_id;
    private final int item_id;
    private final int quantity;

    public RecipeItem(int recipe_item_id, int recipe_id, int item_id, int quantity) {
        this.recipe_item_id = recipe_item_id;
        this.recipe_id = recipe_id;
        this.item_id = item_id;
        this.quantity = quantity;
    }

    public int getRecipe_item_id() {
        return recipe_item_id;
    }

    public int getRecipe_id() {
        return recipe_id;
    }

    public int getItem_id() {
        return item_id;
    }

    public int getQuantity() {
        return quantity;
    }
}