package feny.business.alrannahstorage.models;

import java.util.Objects;

import feny.business.alrannahstorage.Objects.Branches;
import feny.business.alrannahstorage.Objects.Storage;

public class History {
    int id;
    int storage_id;
    boolean added;
    String date;
    int quantity;
    int new_quantity;
    int old_quantity;
    int branch_import_id;
    int branch_export_id;
    int closed;
    String operation;

    public History(int id, int storage_id, boolean added, String date,
                   int quantity, int new_quantity, int old_quantity,
                   int branch_import_id, int branch_export_id, int closed, String operation) {
        this.id = id;
        this.storage_id = storage_id;
        this.added = added;
        this.date = date;
        this.quantity = quantity;
        this.new_quantity = new_quantity;
        this.old_quantity = old_quantity;
        this.branch_import_id = branch_import_id;
        this.branch_export_id = branch_export_id;
        this.closed = closed;
        this.operation = operation;
    }

    public Storage getStorage(){
        return Branches.getStorageByID(storage_id);
    }
    public Branch getImportBranch(){
        return Branches.getBranchByID(branch_import_id);
    }
    public Branch getExportBranch(){
        return Branches.getBranchByID(branch_export_id);
    }

    public String getOperation() {
        return operation;
    }

    public int getId() {
        return id;
    }

    public int getStorage_id() {
        return storage_id;
    }

    public boolean isAdded() {
        return added;
    }

    public String getDate() {
        return date;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getNew_quantity() {
        return new_quantity;
    }

    public int getOld_quantity() {
        return old_quantity;
    }

    public int getBranch_import_id() {
        return branch_import_id;
    }

    public int getBranch_export_id() {
        return branch_export_id;
    }

    public int getClosed() {
        return closed;
    }
    public Item getItem(){
        return Objects.requireNonNull(Branches.getStorageByID(storage_id)).getItem();
    }
    public ItemType getItemType(){
        return Objects.requireNonNull(Branches.getStorageByID(storage_id)).getStateType();
    }
}
