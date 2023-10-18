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

import feny.business.alrannahstorage.Objects.Items;
import feny.business.alrannahstorage.Objects.Storage;
import feny.business.alrannahstorage.R;
import feny.business.alrannahstorage.adapters.dialogs.AddItemDialog;
import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.models.Item;

public class StorageAdaper extends RecyclerView.Adapter<StorageAdaper.ViewHolder> {

    private final ArrayList<Storage> localDataSet;
    private final Context context;
    private boolean extract;

    public StorageAdaper(ArrayList<Storage> localDataSet, Context context, boolean extract) {
        this.localDataSet = localDataSet;
        this.context = context;
        this.extract =extract;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name,quantity;
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
        View view = LayoutInflater.from(context)
                .inflate(R.layout.storage_list, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Item item = Items.getItemByID(localDataSet.get(position).getItemID());
        viewHolder.name.setText(Objects.requireNonNull(item).getName());
        viewHolder.quantity.setText(localDataSet.get(position).getQuantity() + "  " + item.getUnit());
        int _position = position;
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(extract){
                    AddItemDialog customDialog = new AddItemDialog(context,localDataSet.get(_position).getStorageID(),extract, Data.getBranchId(),0);
                    customDialog.show();
                }
                else {
                    AddItemDialog customDialog = new AddItemDialog(context,localDataSet.get(_position).getStorageID(),extract,Data.getBranchId(),Data.getBranchId());
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
