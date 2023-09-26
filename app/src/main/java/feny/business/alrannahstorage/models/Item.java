package feny.business.alrannahstorage.models;

public class Item {
    private String name;
    private int quantity;
    private String Unit;
    private ItemType type;

    public Item(String name, int quantity, String unit, ItemType type) {
        this.name = name;
        this.quantity = quantity;
        Unit = unit;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void increaseQuantity(int add){
        this.quantity += add;
    }

    public void decreaseQuantity(int subtract){
        this.quantity -= subtract;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }
}
