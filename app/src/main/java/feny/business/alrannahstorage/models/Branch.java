package feny.business.alrannahstorage.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import feny.business.alrannahstorage.Objects.Storage;
import feny.business.alrannahstorage.data.Data;

public class Branch {
    private final String name;
    private final int id;
    private final String permission;
    private final String location;
    private final String user;

    public int getStorageSize() {
        return storages.size();
    }

    private ArrayList<Storage> storages = new ArrayList<>();

    public Storage getStorageByID(int id){
        for(Storage storage : storages){
            if(storage.getStorageID() == id){
                return storage;
            }
            else return null;
        }
        return null;
    }
    public Storage getStorageByPosition(int id){
        return (storages.get(id));
    }

    public void addItems(int storageID,int branchID,int itemID,int quantity,int state){
        storages.add(new Storage(storageID,branchID,itemID,quantity,state));
    }

    public Branch(String name, int id, String permission, String location, String user) {
        this.name = name;
        this.id = id;
        this.permission = permission;
        this.location = location;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getPermission() {
        return permission;
    }

    public String getLocation() {
        return location;
    }

    public String getUser() {
        return user;
    }

    public void resetStorage() {
        storages = new ArrayList<>();
    }

    public ArrayList<Storage> getStorage() {
        return storages;
    }
}
