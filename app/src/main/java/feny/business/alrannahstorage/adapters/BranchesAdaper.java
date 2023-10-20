package feny.business.alrannahstorage.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;

import feny.business.alrannahstorage.Objects.Branches;
import feny.business.alrannahstorage.R;
import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.models.Branch;

public class BranchesAdaper extends RecyclerView.Adapter<BranchesAdaper.ViewHolder> {

    private final ArrayList<Branch> localDataSet;
    private final Context context;

    public BranchesAdaper(ArrayList<Branch> localDataSet, Context context) {
        this.localDataSet = localDataSet;
        this.context = context;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name,location,pass;
        private final ImageView delete,details;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            name = view.findViewById(R.id.branch_name);
            pass = view.findViewById(R.id.branch_pass);
            location = view.findViewById(R.id.branch_location);
            details = view.findViewById(R.id.more_branch_details);
            delete = view.findViewById(R.id.delete_branch);

        }

        public TextView getName() {
            return name;
        }
    }


    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(context)
                .inflate(R.layout.branches_list, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.name.setText(localDataSet.get(position).getName());
        viewHolder.pass.setText("رمز الفرع : "+localDataSet.get(position).getPermission());
        viewHolder.location.setText(localDataSet.get(position).getLocation());
        int _position = position;
        viewHolder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] pass = new String[1];
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("حذف الفرع");
                alert.setMessage("متأكد بقرارك لحذف الفرع؟");
                alert.setPositiveButton(Data.YES, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface d, int which) {
                        // continue with delete
                        final EditText input = new EditText(context);
                        input.setInputType(InputType.TYPE_CLASS_NUMBER);


                        alert.setTitle("تأكيد حذف الفرع");
                        alert.setMessage("أنت على بعد خطوتين لحذف الفرع, هل انت متأكد أنك تريد الحذف؟");
                        alert.setPositiveButton(Data.YES, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dd, int which) {
                                // continue with delete
                                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                alert.setTitle("التأكيد النهائي");
                                alert.setView(input);
                                alert.setMessage("لن تستطيع استرجاع الفرع بعد هذا التأكيد الأخير, \nللتأكيد اكتب رمز الفرع");
                                alert.setNegativeButton(Data.CANCEL, (dialog, which1) ->{});
                                alert.setPositiveButton(Data.YES, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        pass[0] = input.getText().toString();
                                        if(pass[0].equals(String.valueOf(localDataSet.get(_position).getPermission()))){
                                           Branches.deleteBranch(context,localDataSet.get(_position).getUser(),localDataSet.get(_position).getPermission());

                                        }
                                        else {
                                            Toast.makeText(context,"الرمز غير صحيح", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                                alert.show();
                            }
                        });
                        alert.show();
                    }
                });
                alert.setNegativeButton(Data.CANCEL, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // close dialog
                        dialog.cancel();
                    }
                });
                alert.show();

            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}
