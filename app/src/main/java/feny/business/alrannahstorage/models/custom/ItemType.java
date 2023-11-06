package feny.business.alrannahstorage.models.custom;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import feny.business.alrannahstorage.Objects.Items;

public class ItemType {
    private final int id;
    private final String name;
    private final String unit;
    private final boolean canBeSold;
    private final int expire_by_hours;


    public ItemType(int id, String name, String unit, int canBeSold, int expire_by_hours) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.canBeSold = canBeSold==1;
        this.expire_by_hours = expire_by_hours;
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

    public boolean expired(String dateStr) {
        LocalDateTime baseTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expirationTime = baseTime.plusHours(expire_by_hours);
        boolean ex =currentTime.isAfter(expirationTime);
        return ex;
    }
}