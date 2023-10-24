package feny.business.alrannahstorage.Objects;

import feny.business.alrannahstorage.models.Branch;
import feny.business.alrannahstorage.models.Item;
import feny.business.alrannahstorage.models.ItemType;

public class Storage {
    int storageID;
    int branchID;
    int itemID;
    int quantity = 0;
    int state;

    public Branch getBranch(){
        return Branches.getBranchByID(branchID);
    }

    public int getStorageID() {
        return storageID;
    }

    public int getBranchID() {
        return branchID;
    }

    public int getItemID() {
        return itemID;
    }

    public int getQuantity() {
        return quantity;
    }

    public Item getItem(){
        return Items.getItemByID(itemID);
    }
    public ItemType getStateType(){
        return Items.getItemTypesByID(state);
    }

    public int getState() {
        return state;
    }

    public Storage(int storageID, int branchID, int itemID, int quantity, int state) {
        this.storageID = storageID;
        this.branchID = branchID;
        this.itemID = itemID;
        this.quantity = quantity;
        this.state = state;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}