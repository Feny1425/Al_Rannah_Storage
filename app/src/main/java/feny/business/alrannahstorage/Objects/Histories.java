package feny.business.alrannahstorage.Objects;


import static feny.business.alrannahstorage.data.Data.ClosedOperations.CHARITY;
import static feny.business.alrannahstorage.data.Data.ClosedOperations.EXCHANGE_TO;
import static feny.business.alrannahstorage.data.Data.ClosedOperations.EXPIRE;
import static feny.business.alrannahstorage.data.Data.ClosedOperations.EXPORT;
import static feny.business.alrannahstorage.data.Data.ClosedOperations.IMPORT;
import static feny.business.alrannahstorage.data.Data.ClosedOperations.RATION;
import static feny.business.alrannahstorage.data.Data.ClosedOperations.SELL;
import static feny.business.alrannahstorage.data.Data.ClosedOperations.SPOILED;
import static feny.business.alrannahstorage.data.Languages.Arabic.EN_TO_AR;

import android.util.Pair;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.database.UpdateStorage;
import feny.business.alrannahstorage.models.custom.Branch;
import feny.business.alrannahstorage.models.custom.History;
import feny.business.alrannahstorage.models.custom.Item;
import feny.business.alrannahstorage.models.custom.Pages;
import feny.business.alrannahstorage.models.custom.ReportItem;
import feny.business.alrannahstorage.models.custom.Storage;
import feny.business.alrannahstorage.models.grouping.Five;

public class Histories {
    private static ArrayList<History> histories = new ArrayList<>();

    public static void reset() {
        histories = new ArrayList<>();
    }

    public static void AddHistory(int id, int storage_id, int added, String date, int quantity, int new_quantity, int old_quantity, int branch_import_id, int branch_export_id, int closed, String operation) {
        histories.add(new History(id, storage_id, (added == 1), date, quantity, new_quantity, old_quantity, branch_import_id, branch_export_id, closed, operation));
    }

    public static History getHistoryByID(int id) {
        for (History history : histories) {
            if (history.getId() == id) return history;
        }
        return null;
    }

    public static Set<History> getHistoryByBranchID(int id) {
        Set<History> histories = new HashSet<>();
        for (History history : Histories.histories) {
            if (history.getStorage().getBranchID() == id) histories.add(history);
        }
        return histories;
    }

    public static ArrayList<History> getNonFinishedOperationsByBranchID(int id) {
        ArrayList<History> histories = new ArrayList<>();
        for (History history : getNonFinishedOperations()) {
            if (history.getBranch_import_id() == id) {
                histories.add(history);
            }
        }
        return histories;
    }

    public static ArrayList<Storage> getAllNonFinishedOperationsByBranchIDAsStorage(int id) {
        ArrayList<Storage> storages = new ArrayList<>();
        for (History history : getNonFinishedOperationsByBranchID(id)) {
            storages.add(new Storage(history.getBranch_import_id(), id, history.getStorage().getItemID(), history.getStorage().getItemTypeID(), history.getQuantity()));

        }
        return storages;
    }

    public static ArrayList<History> getAllHistoryByStorageID(Storage storage, ArrayList<History> histories) {
        ArrayList<History> _histories = new ArrayList<>();
        for (History history : histories) {
            if (history.getStorage().getItem() == storage.getItem()) {
                _histories.add(history);
            }
        }
        return _histories;
    }

    public static ArrayList<History> getAllHistoryByStorageID(int id, ArrayList<History> histories) {
        ArrayList<History> _histories = new ArrayList<>();
        for (History history : histories) {
            if (history.getStorage_id() == id) {
                _histories.add(history);
            }
        }
        return _histories;
    }

    public static Set<Item> getAllHistoryItems(ArrayList<History> histories) {
        Set<Item> items = new HashSet<>();
        for (History history : histories) {
            items.add(history.getItem());
        }
        return items;
    }

    public static ArrayList<History> getAllImportByID(int id) {
        ArrayList<String> salts = new ArrayList<>();
        ArrayList<History> _histories = new ArrayList<>();
        for (History history : histories) {
            if (!salts.contains(history.getOperation())) {
                if (history.getBranch_import_id() == id && history.getBranch_export_id() != history.getBranch_import_id()) {
                    _histories.add(history);
                    salts.add(history.getOperation());
                }
            }
        }
        return _histories;
    }

    public static ArrayList<History> getAllExportByID(int id) {
        ArrayList<String> salts = new ArrayList<>();
        ArrayList<History> _histories = new ArrayList<>();
        for (History history : histories) {
            if (!salts.contains(history.getOperation())) {
                if (history.getBranch_export_id() == id && history.getBranch_export_id() != history.getBranch_import_id()) {
                    _histories.add(history);
                    salts.add(history.getOperation());
                }
            }
        }
        return _histories;
    }

    public static ArrayList<History> getNonFinishedOperations() {
        ArrayList<History> non_finished_operations = new ArrayList<>();
        ArrayList<History> import_operations = new ArrayList<>();
        ArrayList<History> export_operations = new ArrayList<>();
        for (History history : histories) {
            if (history.getBranch_export_id() != history.getBranch_import_id()) {
                if (history.getClosed() == IMPORT) {
                    import_operations.add(history);
                } else if (history.getClosed() == EXPORT) {
                    export_operations.add(history);
                }
            }
        }
        for (History export : export_operations) {
            boolean non = true;
            for (History importO : import_operations) {
                if (export.getOperation().equals(importO.getOperation())) {
                    non = false;
                }
            }
            if (non) {
                non_finished_operations.add(export);
            }
        }
        return non_finished_operations;
    }

    public static void getNonClosedOperationsByBranch(Pages context) {
        ArrayList<History> extractStorage = new ArrayList<>();
        ArrayList<History> close_storage = new ArrayList<>();
        ArrayList<String> expired_storage = new ArrayList<>();
        for (History history : histories) {
            if (history.getBranch_export_id() == history.getBranch_import_id() && history.getBranch_export_id() == Data.getBranchId()) {
                if (history.getClosed() == EXCHANGE_TO) {
                    extractStorage.add(history);
                } else if (history.getClosed() == SELL || history.getClosed() == CHARITY || history.getClosed() == RATION || history.getClosed() == SPOILED || history.getClosed() == EXPORT) {
                    close_storage.add(history);
                } else if (history.getClosed() == EXPIRE) {
                    expired_storage.add(history.getOperation());
                }
            }
        }
        Vector<Integer> quantity = new Vector<>();
        Vector<Integer> IDs = new Vector<>();

        for (History extract : close_storage) {
            int extractStorageID = extract.getStorage_id();
            int extractQuantity = extract.getQuantity();

            if (IDs.contains(extractStorageID)) {
                int pos = IDs.indexOf(extractStorageID);
                quantity.set(pos, quantity.get(pos) + extractQuantity);
            } else {
                IDs.add(extractStorageID);
                quantity.add(extractQuantity);
            }
        }

        for (History close : extractStorage) {
            if (expired_storage.contains(close.getOperation())) {
                continue;
            }
            int closeStorageID = close.getStorage_id();
            int closeQuantity = close.getQuantity();

            if (IDs.contains(closeStorageID)) {
                int indexOfId = IDs.indexOf(closeStorageID);
                int currentQuantity = quantity.get(indexOfId);

                if (currentQuantity >= closeQuantity) {
                    int newQuantity = currentQuantity - closeQuantity;
                    quantity.set(indexOfId, newQuantity);

                } else {
                    // Check if the storage item has expired
                    int newQuantity = closeQuantity - currentQuantity;
                    quantity.set(indexOfId, newQuantity);
                    if (close.getStorage().getItemType().expired(close.getDate())) {
                        // Do the update if there's enough quantity and the item has expired
                        new UpdateStorage(context, String.valueOf(Data.getBranchId()), close.getStorage().getStorageID(), newQuantity, close.getStorage().getQuantity() - newQuantity, close.getStorage().getQuantity(), false, Data.getBranchId(), Data.getBranchId(), EXPIRE, close.getOperation());
                        expired_storage.add(close.getOperation());
                    }
                }
            }
        }
    }

    public static Pair<Vector<String>, Vector<String>> getReport(Branch branch, Pair<int[], int[]> pair) {
        ReportItem reportItem = new ReportItem(pair);
        ArrayList<History> histories = new ArrayList<>(getHistoryByBranchID(branch.getId()));
        histories.sort(Comparator.comparing(History::getDate));
        for (History history : histories) {
            reportItem.addItem(history.getItem().getId());
        }
        for (History history : histories) {
            String date = history.getDate();

            for (ReportItem.Item item : reportItem.getItems()) {
                /*if(history.getItem().getId() == item.getItemID()){
                    item.addItemType(history.getItemType().getId(),
                            history.getOld_quantity(),
                            history.getNew_quantity(),
                            history.getQuantity(),
                            date,
                            history.getClosed(),
                            history.getBranch_import_id(),
                            history.getBranch_export_id());
                }*/
            }
        }
        return new Pair<>(reportItem.getItemsS(), reportItem.getItemsShort());
    }

    public static Pair<String, Vector<Pair<String, Five<Integer, Integer, Integer, Integer, Integer>>>> getReport2(Branch branch, Pair<int[], int[]> pair) {
        ReportItem reportItem = new ReportItem(pair);
        ArrayList<History> histories = new ArrayList<>(getHistoryByBranchID(branch.getId()));
        histories.sort(Comparator.comparing(History::getDate));
        for (History history : histories) {
            reportItem.addItem(history.getItem().getId());
        }
        for (History history : histories) {
            String date = history.getDate();

            for (ReportItem.Item item : reportItem.getItems()) {
                /*if(history.getItem().getId() == item.getItemID()){
                    item.addItemType(history.getItemType().getId(),
                            history.getOld_quantity(),
                            history.getNew_quantity(),
                            history.getQuantity(),
                            date,
                            history.getClosed(),
                            history.getBranch_import_id(),
                            history.getBranch_export_id());
                }*/
            }
        }
        return reportItem.getChartClosed();
    }


    public static List<HistoryTimeAgo> getAllExpiredItemsByBranchID(int id) {
        // Filter the History objects with operation equal to EXPIRE
        List<History> expiredHistories = new ArrayList<>();
        for (History history : getHistoryByBranchID(id)) {
            if (history.getClosed() == EXPIRE) {
                expiredHistories.add(history);
            }
        }

        List<HistoryTimeAgo> historiesWithTimeAgo = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (History history : expiredHistories) {
            LocalDateTime expirationTime = LocalDateTime.parse(history.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            Duration duration = Duration.between(expirationTime, now);
            historiesWithTimeAgo.add(new HistoryTimeAgo(history, duration));
        }

        Collections.sort(historiesWithTimeAgo);

        for (HistoryTimeAgo historyTimeAgo : historiesWithTimeAgo) {
            System.out.println(historyTimeAgo.history.toString() + " - " + historyTimeAgo.getTimeAgo());
        }
        return historiesWithTimeAgo;
    }


    public static class HistoryTimeAgo implements Comparable<HistoryTimeAgo> {
        private History history;
        private Duration duration;

        public HistoryTimeAgo(History history, Duration duration) {
            this.history = history;
            this.duration = duration;
        }

        public Duration getDuration() {
            return duration;
        }

        public String getTimeAgo() {
            // Implement logic to convert duration to a user-friendly time ago format
            // For example, "2 weeks", "1 day and 3 hours", "3 months", etc.
            // You can use Duration values to calculate the desired format.
            Duration duration = this.duration;
            long years = duration.toDays() / 365;
            long months = duration.toDays() / 30;
            long weeks = duration.toDays() / 7;
            long days = duration.toDays();
            long hours = duration.toHours() % 24;
            long minutes = duration.toMinutes() % 60;


            if (years > 1) {
                if(years == 2){
                    return years + EN_TO_AR("two years ago");
                }
                else if (years < 10){
                    return years + EN_TO_AR("years ago");
                }
                else {
                    return years + EN_TO_AR("year ago");
                }
            } else if (months > 1) {
                if(months == 2){
                    return months + EN_TO_AR("two months ago");
                }
                else if (months < 10){
                    return months + EN_TO_AR("months ago");
                }
                else {
                    return months + EN_TO_AR("month ago");
                }
            } else if (weeks > 1) {
                if(weeks == 2){
                    return weeks + EN_TO_AR("two weeks ago");
                }
                else if (weeks < 10){
                    return weeks + EN_TO_AR("weeks ago");
                }
                else {
                    return weeks + EN_TO_AR("week ago");
                }
            } else if (days > 1) {
                if(days == 2){
                    return days + EN_TO_AR("two days ago");
                }
                else if (days < 10){
                    return days + EN_TO_AR("days ago");
                }
                else {
                    return days + EN_TO_AR("day ago");
                }
            } else if (hours > 0) {
                if (minutes > 0) {
                    return hours + EN_TO_AR(" hours and ") + minutes + EN_TO_AR(" minutes ago");
                }
                return hours + EN_TO_AR(" hours ago");
            } else if (minutes > 0) {
                return minutes + EN_TO_AR(" minutes ago");
            } else {
                return EN_TO_AR("just now");
            }
        }

        @Override
        public String toString() {
            return getTimeAgo();
        }

        @Override
        public int compareTo(HistoryTimeAgo other) {
            // Implement comparison logic to sort by expiration time
            // You can compare durations here.
            return this.duration.compareTo(other.duration);
        }
    }


}