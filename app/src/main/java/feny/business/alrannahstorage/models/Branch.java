package feny.business.alrannahstorage.models;

import feny.business.alrannahstorage.Objects.Storage;

public class Branch {
    String name;
    String Location;
    Storage storage;
    int password;
    int permission;

    public int getPassword() {
        return password;
    }

    public int getPermission() {
        return permission;
    }

    public Branch(String name, String location, int password, int permission) {
        this.name = name;
        Location = location;
        this.storage = new Storage();
        this.password = password;
        this.permission = permission;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }
}
