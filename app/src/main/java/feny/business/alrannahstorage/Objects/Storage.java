package feny.business.alrannahstorage.Objects;

import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import feny.business.alrannahstorage.activities.AdminActivity;
import feny.business.alrannahstorage.activities.LoginActivity;
import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.data.PushPullData;
import feny.business.alrannahstorage.models.Item;
import feny.business.alrannahstorage.models.ItemType;

public class Storage {
    int storageID;
    int branchID;
    int itemID;
    int quantity = 0;
    int state;

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
}
