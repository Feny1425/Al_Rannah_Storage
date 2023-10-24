package feny.business.alrannahstorage.models;

import java.util.ArrayList;

import feny.business.alrannahstorage.Objects.Storage;

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
        //storages = new ArrayList<>();
    }

    public ArrayList<Storage> getStorage() {
        return storages;
    }
    public boolean containStorageByID(int id){
        if(storages.size() > 0){
            for (Storage storage : storages) {
                if(storage.getStorageID() == id){
                    return true;
                }
            }
        }
        return false;
    }

    public void changeQuantityOnly(Storage storage){
        for(Storage _storage : storages){
            if(storage.getItem() == _storage.getItem() &&
            storage.getState() == _storage.getState()){
                _storage.setQuantity(storage.getQuantity());
            }
        }
    }

    public void addItems(Storage storage) {
        storages.add(storage);
    }
    public void setItemByPos(int pos, Storage storage){
        storages.set(pos,storage);
    }
}