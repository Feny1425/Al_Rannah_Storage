package feny.business.alrannahstorage.Objects;

import java.util.ArrayList;

import feny.business.alrannahstorage.models.Item;
import feny.business.alrannahstorage.models.ItemType;

public class Storage {
    private ArrayList<Item> items = new ArrayList<>();

    public void addItem(Item item){
        items.add(item);
    }
    public void editItem(int position, Item item){
        items.set(position,item);
    }
    public void editItem(int position, String name,String unit, int quantity, ItemType type){
        Item item = items.get(position);
        item.setName(name);
        item.setUnit(unit);
        item.setQuantity(quantity);
        item.setType(type);
        items.set(position, item);
    }
    public void increaseItem(int position, int add){
        items.get(position).increaseQuantity(add);
    }

    public void decreaseItem(int position, int subtract){
        items.get(position).increaseQuantity(subtract);
    }

    public ArrayList<Item> getItems() {
        return items;
    }
}
