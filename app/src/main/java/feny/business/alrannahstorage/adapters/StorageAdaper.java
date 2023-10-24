package feny.business.alrannahstorage.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import feny.business.alrannahstorage.Objects.Branches;
import feny.business.alrannahstorage.Objects.Storage;
import feny.business.alrannahstorage.R;
import feny.business.alrannahstorage.adapters.dialogs.AddItemDialog;
import feny.business.alrannahstorage.models.Branch;
import feny.business.alrannahstorage.models.Item;
import feny.business.alrannahstorage.models.ItemType;

public class StorageAdaper extends RecyclerView.Adapter<StorageAdaper.ViewHolder> {

    private ArrayList<Storage> localDataSet;
    private final Context context;
    private boolean extract, close,Import;
    private ArrayList<Pair<Branch,Pair<Integer,String>>> export;

    public StorageAdaper(ArrayList<Storage> localDataSet, Context context, boolean extract, boolean e2,boolean Import, ArrayList<Pair<Branch,Pair<Integer,String>>> export) {
        this.localDataSet = localDataSet;
        this.context = context;
        this.extract = extract;
        this.close = e2;
        this.Import = Import;
        this.export = export;
    }


    public void setLocalDataSet(ArrayList<Storage> localDataSet) {
        this.localDataSet = localDataSet;
    }

    public void setExport(ArrayList<Pair<Branch, Pair<Integer, String>>> export) {
        this.export = export;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name, quantity;
        private final LinearLayout layout;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            name = view.findViewById(R.id.name);
            quantity = view.findViewById(R.id.quantity);
            layout = view.findViewById(R.id.layout);

        }

    }


    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(context).inflate(R.layout.storage_list, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        ItemType itemType = localDataSet.get(position).getStateType();
        Item item = localDataSet.get(position).getItem();
        viewHolder.name.setText((close||Import) ? Objects.requireNonNull(itemType).getType() : item.getName());
        viewHolder.quantity.setText(localDataSet.get(position).getQuantity() + "  " + item.getUnit());
        int _position = position;
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Item item = localDataSet.get(_position).getItem();
                ItemType itemType = localDataSet.get(_position).getStateType();
                int branchID = localDataSet.get(_position).getBranchID();
                Storage storage = Branches.getStorageByItemItemTypeBranchID(item,itemType ,branchID);
                if(storage != null) {
                    AddItemDialog customDialog = new AddItemDialog(context, storage.getStorageID(), extract, close, Import, (export.size() > 0) ? export.get(_position) : null);
                    customDialog.show();
                }
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}