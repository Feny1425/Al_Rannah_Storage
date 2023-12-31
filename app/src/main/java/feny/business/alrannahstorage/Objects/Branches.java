package feny.business.alrannahstorage.Objects;

import android.content.Context;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Vector;

import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.database.AddBranch;
import feny.business.alrannahstorage.database.DeleteBranch;
import feny.business.alrannahstorage.models.custom.Branch;
import feny.business.alrannahstorage.models.custom.Item;
import feny.business.alrannahstorage.models.custom.ItemType;
import feny.business.alrannahstorage.models.custom.Pages;
import feny.business.alrannahstorage.models.custom.Storage;

public class Branches {
    private static ArrayList<Branch> branches = new ArrayList<>();
    public static void setBranches(ArrayList<Branch> branches, Pages pages){
        if(branches != null) {
            if (Branches.branches.size() != branches.size()) {
                if(Branches.branches.size() > branches.size()){
                    Branches.branches.clear();
                    Branches.branches.addAll(branches);
                }else {
                    for (Branch branch : branches) {
                        if (!containBranchByID(branch.getId())) {
                            Branches.branches.add(branch);
                        }
                    }
                }
            }
        }
    }

    public static boolean containBranchByID(int id) {
        if(Branches.branches.size() > 0){
            for (Branch branch : Branches.branches){
                if(branch.getId() == id){
                    return true;
                }
            }
        }
        return false;
    }

    public static Vector<Integer> getAllBranchesIDs(){
        Vector<Integer> IDs = new Vector<>();
        for(Branch branch : branches){
            IDs.add(branch.getId());
        }
        return IDs;
    }

    public static ArrayList<Branch> getBranches() {
        return branches;
    }

    public static void addBranch(Context context,String name, String location, int permission, String username){
        new AddBranch(context,String.valueOf(permission),name,location,username);

    }
    public static void deleteBranch(Context context, String username, String permission){
        new DeleteBranch(context,username,permission);
    }
    public static Branch getBranch(int position){
        return branches.get(position);
    }
    public static Branch getBranchByPermission(String permission){
        for (Branch branch : branches){
            if(branch.getPermission().equals(permission)) return branch;
        }
        return null;
    }public static Branch getBranchByID(int id){
        for (Branch branch : branches){
            if(branch.getId() == id){
                return branch;
            }
        }
        return null;
    }public static Branch getBranchByNameAndLocation(String nameAndLocation){
        for (Branch branch : branches){
            String _nameAndLocation = branch.getName() + " " + branch.getLocation();
            if(nameAndLocation.equals(_nameAndLocation)) return branch;
        }
        return null;
    }public static Branch getBranchByStorageID(int id){
        return getBranchByID(Objects.requireNonNull(getStorageByID(id)).getBranchID());
    }
    public static Storage getStorageByItemItemTypeBranchID(Item item, ItemType itemType, int branchID){
        Branch branch = getBranchByID(branchID);

        for(Storage storage : branch.getStorage()){
            /*if(storage.getItem() == item && storage.getState() == itemType.getId()){
                return storage;
            }*/
        }
        return null;
    }
    public static int getSize(){
        return branches.size();
    }

    public static String getPermissionByPassword(String pass){
        if(Objects.equals(pass, Data.getAdminPermssion())){
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
    public static Storage getStorageByID(int id){
        for (Branch branch : branches){
            for(Storage storage : branch.getStorage()){
                if(storage.getStorageID() == id){
                    return storage;
                }
            }
        }
        return new Storage(-1,branches.get(0).getId(),Items.getItems().get(0).getId(),Items.getItemTypes().get(0).getId(),-1);
    }
    public static Storage getStorageByItemID(int id) {
        Branch branch = getBranchByID(Data.getBranchId());
        for (Storage storage : branch.getStorage()) {
            if (storage.getItem().getId() == id) {
                return storage;
            }

        }
        return null;
    }
    public static boolean checkIfStoragesAreEmpty(){
        for (Branch branch : branches){
            if(branch.getStorage().size() > 0){
                return true;
            }
        }
        return true;
    }


}