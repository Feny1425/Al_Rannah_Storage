package feny.business.alrannahstorage.Objects;

import java.util.ArrayList;

import feny.business.alrannahstorage.models.Item;
import feny.business.alrannahstorage.models.ItemType;

public class Items {
    private static ArrayList<Item> items = new ArrayList<>();
    private static ArrayList<ItemType> itemTypes = new ArrayList<>();

    public static ArrayList<ItemType> getItemTypes() {
        return itemTypes;
    }

    public static void addItem(String name, int id, String unit){
        items.add(new Item(name,id,unit));
    }
    public static void addItemType(String type, int id){
        itemTypes.add(new ItemType(id,type));
    }
    public static void reset(){
        items = new ArrayList<>();
        itemTypes = new ArrayList<>();
    }
    public static Item getItemByID(int id){
        for(Item item : items){
            if(item.getId() == id){
                return item;
            }
        }
        return null;
    }
    public static int getItemTypeID(String type){
        for (ItemType itemType : itemTypes){
            if(itemType.getType().equals(type)) return itemType.getId();
        }
        return 0;
    }

    public static ArrayList<Item> getItems() {
        return items;
    }

    public static ItemType getItemTypesByID(int state) {
        for(ItemType item : itemTypes){
            if(item.getId() == state){
                return item;
            }
        }
        return null;
    }
}
