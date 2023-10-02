package feny.business.alrannahstorage.Objects;

import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;

import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.data.PushPullData;
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

    public static void addBranch(String name, String location, int pass, SharedPreferences sharedPreferences){
        branches.add(new Branch(name,location,pass,pass));
        PushPullData pushPullData = new PushPullData(sharedPreferences);
        pushPullData.saveMemory();
    }
    public static void deleteBranch(int position,SharedPreferences sharedPreferences){
        branches.remove(position);
        PushPullData pushPullData = new PushPullData(sharedPreferences);
        pushPullData.saveMemory();
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
        else if (!branches.isEmpty()){
            for (int i = 0; i < branches.size();i++){
                if(branches.get(i).getPassword() == pass){
                    return branches.get(i).getPermission();
                }
            }
        }
        return -1;
    }

}
