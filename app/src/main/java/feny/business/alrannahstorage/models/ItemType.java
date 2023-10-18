package feny.business.alrannahstorage.models;

import static feny.business.alrannahstorage.data.Data.ITEM_TYPES;

public class ItemType {
    int id;
    String type;

    public ItemType(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
