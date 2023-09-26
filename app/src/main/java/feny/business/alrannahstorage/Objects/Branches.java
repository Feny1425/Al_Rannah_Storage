package feny.business.alrannahstorage.Objects;

import java.util.ArrayList;

import feny.business.alrannahstorage.models.Branch;

public class Branches {
    private static ArrayList<Branch> branches = new ArrayList<>();
    public static void addBranch(String name, String location){
        branches.add(new Branch(name,location));
    }
    public static Branch getBranch(int position){
        return branches.get(position);
    }
    public static int getSize(){
        return branches.size();
    }

}
