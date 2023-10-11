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
    String name;
    String Location;
    Storage storage;
    int permission;


    public int getPermission() {
        return permission;
    }

    public Branch(String name, String location, int permission) {
        this.name = name;
        Location = location;
        this.storage = new Storage();
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
