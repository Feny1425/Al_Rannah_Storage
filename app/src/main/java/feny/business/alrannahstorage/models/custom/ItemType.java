package feny.business.alrannahstorage.models.custom;

import feny.business.alrannahstorage.Objects.Items;

public class ItemType {
    private final int id;
    private final String name;
    private final String unit;
    private final boolean canBeSold;


    public ItemType(int id, String name,String unit, int canBeSold) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.canBeSold = canBeSold==1;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public boolean isCanBeSold() {
        return canBeSold;
    }

    public Recipe getRecipe(){
        for (Recipe recipe : Items.getRecipes()){
            if(recipe.getItem_type_id() == id){
                return recipe;
            }
        }
        return null;
    }
    public boolean CanBeCooked(){
        if(getRecipe() == null){
            return false;
        }
        else {
            return getRecipe().CanBeCooked();
        }
    }

}