package feny.business.alrannahstorage.models;

public class Item {
   String name;
   int id;
   String unit;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getUnit() {
        return unit;
    }

    public Item(String name, int id, String unit) {
        this.name = name;
        this.id = id;
        this.unit = unit;
    }
}
