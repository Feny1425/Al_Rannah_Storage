package feny.business.alrannahstorage.Objects;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import feny.business.alrannahstorage.activities.AdminActivity;
import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.data.PushPullData;
import feny.business.alrannahstorage.database.AddBranchHttpRequest;
import feny.business.alrannahstorage.database.DeleteBranchHttpRequest;
import feny.business.alrannahstorage.models.Branch;
import feny.business.alrannahstorage.models.Pages;

public class Branches {
    private static ArrayList<Branch> branches = new ArrayList<>();
    public static void setBranches(ArrayList<Branch> branches, Pages pages){
        Branches.branches = new ArrayList<>();
        Branches.branches.addAll(branches==null?new ArrayList<>():branches);
        pages.refresh();
    }

    public static ArrayList<Branch> getBranches() {
        return branches;
    }

    public static void addBranch(Context context,String name, String location, int permission, String username){
        new AddBranchHttpRequest(context,String.valueOf(permission),name,location,username);

    }
    public static void deleteBranch(Context context, String username, String permission){
        new DeleteBranchHttpRequest(context,username,permission);
    }
    public static Branch getBranch(int position){
        return branches.get(position);
    }
    public static Branch getBranchByPermission(String permission){
        for (int i = 0; i < getSize();i++){
            if(getBranch(i).getPermission().equals(permission)){
                return getBranch(i);
            }
        }
        return null;
    }public static Branch getBranchByID(int id){
        for (int i = 0; i < getSize();i++){
            if(getBranch(i).getId() == id){
                return getBranch(i);
            }
        }
        return null;
    }
    public static int getSize(){
        return branches.size();
    }

    public static String getPermissionByPassword(String pass){
        if(pass == Data.getAdminPermssion()){
            return "0";
        }
        else if (!branches.isEmpty()){
            for (int i = 0; i < branches.size();i++){
                if(branches.get(i).getPermission().equals(pass)){
                    return branches.get(i).getPermission();
                }
            }
        }
        return "-1";
    }

}
