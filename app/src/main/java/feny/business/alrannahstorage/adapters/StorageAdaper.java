package feny.business.alrannahstorage.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Vector;

import feny.business.alrannahstorage.Objects.Items;
import feny.business.alrannahstorage.Objects.Storage;
import feny.business.alrannahstorage.R;
import feny.business.alrannahstorage.adapters.dialogs.AddItemDialog;
import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.models.Item;
import feny.business.alrannahstorage.models.ItemType;

public class StorageAdaper extends RecyclerView.Adapter<StorageAdaper.ViewHolder> {

    private final ArrayList<Storage> localDataSet;
    private final Context context;
    private boolean extract, e2,Import;
    private Vector<Integer> quantities = new Vector<>();
    private Vector<Integer> export = new Vector<>();

    public StorageAdaper(ArrayList<Storage> localDataSet, Context context, boolean extract, boolean e2) {
        this.localDataSet = localDataSet;
        this.context = context;
        this.extract = extract;
        this.e2 = e2;
    }

    public StorageAdaper(ArrayList<Storage> storages, Context context, Vector<Integer> quantities, Vector<Integer> export) {
        this.localDataSet = storages;
        this.context = context;
        this.extract = false;
        this.e2 = false;
        Import = true;
        this.quantities = quantities;
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
        viewHolder.name.setText(e2 ? Objects.requireNonNull(itemType).getType() : item.getName());
        viewHolder.quantity.setText((Import?quantities.get(position):localDataSet.get(position).getQuantity()) + "  " + item.getUnit());
        int _position = position;
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddItemDialog customDialog = new AddItemDialog(context, localDataSet.get(_position).getStorageID(), extract, e2,Import,Import?export.get(_position):Data.getBranchId());
                customDialog.show();
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}