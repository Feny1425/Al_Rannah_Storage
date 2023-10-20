package feny.business.alrannahstorage.Objects;


import android.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;

import feny.business.alrannahstorage.models.Branch;
import feny.business.alrannahstorage.models.History;
import feny.business.alrannahstorage.models.Item;

public class Histories {
    private static ArrayList<History> histories = new ArrayList<>();
    public static void reset(){
        histories = new ArrayList<>();
    }
    public static void AddHistory(int id, int storage_id, int added, String date,
                                 int quantity, int new_quantity, int old_quantity,
                                 int branch_import_id, int branch_export_id, int closed,
                                  String operation){
        histories.add(new History(id,storage_id,(added==1),
                date,quantity,new_quantity,old_quantity,
                branch_import_id,branch_export_id,closed,
                operation));
    }
    public static History getHistoryByID(int id){
        for(History history : histories){
            if(history.getId() == id) return history;
        }
        return null;
    }
    public static ArrayList<History> getNonFinishedOperations(){
        ArrayList<History> non_finished_operations = new ArrayList<>();
        ArrayList<History> import_operations = new ArrayList<>();
        ArrayList<History> export_operations = new ArrayList<>();
        for(History history : histories){
            if(history.getBranch_export_id() != history.getBranch_import_id()){
                if(history.getClosed() == 0){
                    import_operations.add(history);
                }
                else if(history.getClosed() == -1) {
                    export_operations.add(history);
                }
            }
        }
        for (History export : export_operations){
            boolean non = true;
            for(History importO : import_operations){
                if(export.getOperation().equals(importO.getOperation())){
                    non = false;
                }
            }
            if(non) {
                non_finished_operations.add(export);
            }
        }
        return non_finished_operations;
    }

}
