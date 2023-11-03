package feny.business.alrannahstorage.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import feny.business.alrannahstorage.R;
import feny.business.alrannahstorage.adapters.dialogs.AddItemDialog;
import feny.business.alrannahstorage.models.custom.Pages;
import feny.business.alrannahstorage.models.custom.Storage;

public class ItemListAdaper extends RecyclerView.Adapter<ItemListAdaper.ViewHolder> {

    private ArrayList<Storage> localDataSet;
    private final Pages context;

    public ItemListAdaper(ArrayList<Storage> localDataSet, Pages context) {
        this.localDataSet = localDataSet;
        this.context = context;
    }

    public void setLocalDataSet(ArrayList<Storage> localDataSet) {
        this.localDataSet = localDataSet;
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
        viewHolder.name.setText(localDataSet.get(position).getName());
        viewHolder.quantity.setText((localDataSet.get(position).isRecipe()?localDataSet.get(position).getItemType().getRecipe().HowManyCanBeCooked()+"":localDataSet.get(position).getQuantity()) + "  " + localDataSet.get(position).getUnit());
        int _position = position;
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddItemDialog customDialog = new AddItemDialog(context, localDataSet.get(_position));
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