package feny.business.alrannahstorage.adapters.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import feny.business.alrannahstorage.R;
import feny.business.alrannahstorage.models.custom.RecipeItem;
import feny.business.alrannahstorage.models.custom.Storage;

public class DetailsDialog extends Dialog {

    private TextView dialogTitle,details;
    private Button  cancel;
    private Storage storage;
    private Context context;

    public DetailsDialog(Context context, Storage storage) {
        super(context);
        this.context = context;
        this.storage = storage;

    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.det_dialog);
        dialogTitle = findViewById(R.id.label);
        details = findViewById(R.id.items);

        dialogTitle.setText("الحصة الواحدة من\n"+storage.getItemType().getName()+"\nتحتوي على");

        StringBuilder det = new StringBuilder();
        for(RecipeItem recipeItem : storage.getItemType().getRecipe().getRecipeItems()){
            det.append(recipeItem.getItem().getName())
                    .append(" ")
                    .append(recipeItem.getQuantity())
                    .append(" ")
                    .append(recipeItem.getItem().getUnit())
                    .append("\n");
        }
        details.setText(det.toString());

        cancel = findViewById(R.id.cancel_dilg);
        cancel.setOnClickListener(view -> dismiss());
    }

}