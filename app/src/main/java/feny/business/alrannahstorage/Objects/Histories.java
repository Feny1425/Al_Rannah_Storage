package feny.business.alrannahstorage.Objects;


import android.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import feny.business.alrannahstorage.models.Branch;
import feny.business.alrannahstorage.models.History;
import feny.business.alrannahstorage.models.Item;
import feny.business.alrannahstorage.models.ReportItem;

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
    public static Set<History> getHistoryByBranchID(int id){
        Set<History> histories = new HashSet<>();
        for(History history : Histories.histories){
            if(history.getStorage().getBranchID() == id) histories.add(history);
        }
        return histories;
    }

    public static ArrayList<History> getNonFinishedOperationsByID(int id){
        ArrayList<History> histories = new ArrayList<>();
        for(History history : getNonFinishedOperations()){
            if(history.getBranch_import_id() == id){
                histories.add(history);
            }
        }
        return histories;
    }

    public static ArrayList<History> getAllHistoryByStorageID(Storage storage,ArrayList<History> histories){
        ArrayList<History> _histories = new ArrayList<>();
        for(History history : histories){
            if(history.getStorage().getItem() == storage.getItem()){
                _histories.add(history);
            }
        }
        return _histories;
    }
    public static ArrayList<History> getAllHistoryByStorageID(int id,ArrayList<History> histories){
        ArrayList<History> _histories = new ArrayList<>();
        for(History history : histories){
            if(history.getStorage_id() == id){
                _histories.add(history);
            }
        }
        return _histories;
    }
    public static Set<Item> getAllHistoryItems(ArrayList<History> histories){
        Set<Item> items = new HashSet<>();
        for(History history : histories){
            items.add(history.getItem());
        }
        return items;
    }

    public static ArrayList<History> getAllImportByID(int id){
        ArrayList<String> salts = new ArrayList<>();
        ArrayList<History> _histories = new ArrayList<>();
        for(History history : histories){
            if(!salts.contains(history.getOperation())) {
                if (history.getBranch_import_id() == id && history.getBranch_export_id() != history.getBranch_import_id()) {
                    _histories.add(history);
                    salts.add(history.getOperation());
                }
            }
        }
        return _histories;
    }
    public static ArrayList<History> getAllExportByID(int id){
        ArrayList<String> salts = new ArrayList<>();
        ArrayList<History> _histories = new ArrayList<>();
        for(History history : histories){
            if(!salts.contains(history.getOperation())) {
                if (history.getBranch_export_id() == id && history.getBranch_export_id() != history.getBranch_import_id()) {
                    _histories.add(history);
                    salts.add(history.getOperation());
                }
            }
        }
        return _histories;
    }

    public static ArrayList<History> getNonFinishedOperations(){
        ArrayList<History> non_finished_operations = new ArrayList<>();
        ArrayList<History> import_operations = new ArrayList<>();
        ArrayList<History> export_operations = new ArrayList<>();
        for(History history : histories){
            if(history.getBranch_export_id() != history.getBranch_import_id()){
                if(history.getClosed() > 0){
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

    public static Pair<Vector<String>,Vector<String>> getReport(Branch branch, Pair<int[], int[]> pair){
        ReportItem reportItem = new ReportItem(pair);
        ArrayList<History> histories = new ArrayList<>(getHistoryByBranchID(branch.getId()));
        histories.sort(Comparator.comparing(History::getDate));
        for(History history : histories){
            reportItem.addItem(history.getItem().getId());
        }
        for(History history : histories){
            String date = history.getDate();

            for(ReportItem.Item item : reportItem.getItems()){
                if(history.getItem().getId() == item.getItemID()){
                    item.addItemType(history.getItemType().getId(),
                            history.getOld_quantity(),
                            history.getNew_quantity(),
                            history.getQuantity(),
                            date,
                            history.getClosed(),
                            history.getBranch_import_id(),
                            history.getBranch_export_id());
                }
            }
        }
        return new Pair<>(reportItem.getItemsS(),reportItem.getItemsShort());
    }


}