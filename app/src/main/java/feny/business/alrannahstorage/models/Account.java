package feny.business.alrannahstorage.models;

public class Account {
    private String name;
    private int permission;

    public Account(String name, int permission) {
        this.name = name;
        this.permission = permission;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }
}
