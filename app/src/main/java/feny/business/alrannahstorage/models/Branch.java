package feny.business.alrannahstorage.models;

import feny.business.alrannahstorage.Objects.Storage;

public class Branch {
    String name;
    String Location;
    Storage storage;

    public Branch(String name, String location) {
        this.name = name;
        Location = location;
        this.storage = new Storage();
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
