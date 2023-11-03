package feny.business.alrannahstorage.models.custom;

public class Item {
    private final String name;
    private final int id;
    private final String unit;
    private final boolean food;// import , extract, import export | 0, 1, 2

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getUnit() {
        return unit;
    }

    public boolean isFood() {
        return food;
    }

    public Item(String name, int id, String unit, int food) {
        this.name = name;
        this.id = id;
        this.unit = unit;
        this.food = food == 1;
    }
}