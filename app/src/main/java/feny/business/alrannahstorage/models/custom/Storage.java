package feny.business.alrannahstorage.models.custom;

import feny.business.alrannahstorage.Objects.Branches;
import feny.business.alrannahstorage.Objects.Items;
import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.models.interfaces.ListItem;

public class Storage implements ListItem {
    int storageID;
    int branchID;
    int itemID;
    int itemTypeID;
    int quantity = 0;
    boolean Imported = false;
    boolean Extracted = false;
    int otherStorage = -1;
    boolean close = false;
    boolean isRecipe = false;
    String salted = "";

    public void setRecipe(boolean recipe) {
        isRecipe = recipe;
    }

    public boolean isRecipe() {
        return isRecipe;
    }

    public boolean isFood(){
        if(getItem() == null){
            return false;
        }
        return getItem().isFood();
    }
    public String getSalted() {
        return salted;
    }

    public void setSalted(String salted) {
        this.salted = salted;
    }

    public boolean isExtracted() {
        return Extracted;
    }

    public void setExtracted(boolean extracted) {
        Extracted = extracted;
    }

    public boolean isImported() {
        return Imported;
    }

    public void setImported(boolean imported) {
        Imported = imported;
    }

    public boolean isClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    public Storage getOtherStorage() {
        return Branches.getStorageByID(otherStorage);
    }

    public void setOtherStorage(int otherStorage) {
        this.otherStorage = otherStorage;
    }

    public Storage getExportedToStorage(){
        return Branches.getStorageByID(otherStorage);
    }

    public Item getItem(){
        return Items.getItemByID(itemID);
    }
    public boolean isItem(){
        return getItem().getId()!=-1;
    }

    public ItemType getItemType(){
        return Items.getItemTypeByID(itemTypeID);
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String getName() {
        if(itemID == -1){
            return getItemType().getName();
        }
        else {
            return getItem().getName();
        }
    }

    @Override
    public String getUnit() {
        if(itemID == -1){
            return getItemType().getUnit();
        }
        else {
            return getItem().getUnit();
        }
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public int getPlace() {
        if(itemID == -1){
            return Data.Places.RECIPE;
        }
        else {
            Item item = getItem();
            if(item.isFood()){
                return Data.Places.FOOD;
            }
            else return Data.Places.NON_FOOD;
        }
    }

    public int getStorageID() {
        return storageID;
    }

    public int getBranchID() {
        return branchID;
    }
    public Branch getBranch() {
        return Branches.getBranchByID(branchID);
    }

    public int getItemID() {
        return itemID;
    }

    public int getItemTypeID() {
        return itemTypeID;
    }

    public boolean canBeSold(){
        if(isItem()){return false;}
        else return getItemType().isCanBeSold();
    }

    public Storage(int storageID, int branchID, int itemID, int itemTypeID, int quantity) {
        this.storageID = storageID;
        this.branchID = branchID;
        this.itemID = itemID;
        this.itemTypeID = itemTypeID;
        this.quantity = quantity;
    }

}