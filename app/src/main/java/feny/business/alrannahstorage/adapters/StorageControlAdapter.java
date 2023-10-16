package feny.business.alrannahstorage.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import feny.business.alrannahstorage.R;
import feny.business.alrannahstorage.activities.MainActivity;
import feny.business.alrannahstorage.models.Branch;
import feny.business.alrannahstorage.models.Item;

public class StorageControlAdapter extends RecyclerView.Adapter<StorageControlAdapter.ViewHolder> {

    private ArrayList<Item> localDataSet;
    private Context context;
    private MainActivity main;

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    private Branch branch;

    public StorageControlAdapter(ArrayList<Item> localDataSet, Context context,MainActivity main) {
        this.localDataSet = localDataSet;
        this.context = context;
        this.main = main;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name,quantity;
        private final ImageView inc,dec;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            name = (TextView) view.findViewById(R.id.name);
            quantity = (TextView) view.findViewById(R.id.quantity);
            inc = (ImageView) view.findViewById(R.id.increase);
            dec = (ImageView) view.findViewById(R.id.decrease);

        }

        public TextView getName() {
            return name;
        }

        public TextView getQuantity() {
            return quantity;
        }

        public ImageView getInc() {
            return inc;
        }

        public ImageView getDec() {
            return dec;
        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(context)
                .inflate(R.layout.storage_list_control, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getName().setText(localDataSet.get(position).getName());
        viewHolder.getQuantity().setText(localDataSet.get(position).getQuantity() + " " + localDataSet.get(position).getUnit());
        /*viewHolder.getInc().setOnClickListener(v -> {
            branch.getStorage().increaseItem(position,1);
            main.refresh(context);
        });
        viewHolder.getDec().setOnClickListener(v -> {
            branch.getStorage().decreaseItem(position,1);
            main.refresh(context);
        });*/

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}
