package feny.business.alrannahstorage.Objects;

import java.util.ArrayList;

import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.models.Branch;

public class Branches {
    private static ArrayList<Branch> branches = new ArrayList<>();
    public static void addBranch(String name, String location,int pass){
        branches.add(new Branch(name,location,pass,branches.size()+1));
    }
    public static Branch getBranch(int position){
        return branches.get(position);
    }
    public static int getSize(){
        return branches.size();
    }

    public static int getPermissionByPassword(int pass){
        if(pass == Data.getPassword()){
            return 0;
        }
        else {
            for (int i = 0; i < branches.size();i++){
                if(branches.get(i).getPassword() == pass){
                    return branches.get(i).getPermission();
                }
            }
            return -1;
        }
    }

}
