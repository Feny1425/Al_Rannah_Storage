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
}
