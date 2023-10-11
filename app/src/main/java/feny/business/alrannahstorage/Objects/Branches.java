package feny.business.alrannahstorage.Objects;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.data.PushPullData;
import feny.business.alrannahstorage.database.AddBranchHttpRequest;
import feny.business.alrannahstorage.models.Branch;

public class Branches {
    private static ArrayList<Branch> branches = new ArrayList<>();

    public static void setBranches(ArrayList<Branch> branches){
        Branches.branches = new ArrayList<>();
        Branches.branches.addAll(branches==null?new ArrayList<>():branches);
    }

    public static ArrayList<Branch> getBranches() {
        return branches;
    }

    public static void addBranch(Context context,String name, String location, int permission, SharedPreferences sharedPreferences,String username){
        new AddBranchHttpRequest(context,String.valueOf(permission),name,location,username);
        branches.add(new Branch(name,location,permission));
    }
    public static void deleteBranch(int position,SharedPreferences sharedPreferences){
        branches.remove(position);
        PushPullData pushPullData = new PushPullData(sharedPreferences);
        pushPullData.saveMemory();
    }
    public static Branch getBranch(int position){
        return branches.get(position);
    }
    public static Branch getBranchByPermission(int permission){
        for (int i = 0; i < getSize();i++){
            if(getBranch(i).getPermission()==permission){
                return getBranch(i);
            }
        }
        return null;
    }
    public static int getSize(){
        return branches.size();
    }

    public static int getPermissionByPassword(int pass){
        if(pass == Data.getAdminPermssion()){
            return 0;
        }
        else if (!branches.isEmpty()){
            for (int i = 0; i < branches.size();i++){
                if(branches.get(i).getPermission() == pass){
                    return branches.get(i).getPermission();
                }
            }
        }
        return -1;
    }

}
