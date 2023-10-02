package feny.business.alrannahstorage.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import feny.business.alrannahstorage.R;
import feny.business.alrannahstorage.models.Branch;

public class BranchesAdaper extends RecyclerView.Adapter<BranchesAdaper.ViewHolder> {

    private ArrayList<Branch> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name,location;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            name = (TextView) view.findViewById(R.id.branch_name);
            location = (TextView) view.findViewById(R.id.branch_location);
        }

        public TextView getName() {
            return name;
        }
        public TextView getLocation() {
            return location;
        }
    }

    public BranchesAdaper(ArrayList<Branch> branches) {
        localDataSet = branches;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.branches_list, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.name.setText(localDataSet.get(position).getName());
        viewHolder.location.setText(localDataSet.get(position).getLocation());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}
