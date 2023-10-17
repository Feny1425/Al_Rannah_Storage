package feny.business.alrannahstorage.adapters.dialogs;
import android.app.AlertDialog;
import android.app.Dialog;
        import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
        import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
        import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Objects;

import feny.business.alrannahstorage.Objects.Branches;
import feny.business.alrannahstorage.Objects.Items;
import feny.business.alrannahstorage.Objects.Storage;
import feny.business.alrannahstorage.R;
import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.database.AddHistoryHttpRequest;
import feny.business.alrannahstorage.database.UpdateStorageFromServer;
import feny.business.alrannahstorage.models.Item;
import feny.business.alrannahstorage.models.Pages;

public class AddItemDialog extends Dialog {

    private TextView dialogTitle;
    private EditText quantity;
    private Spinner items;
    private Button save,cancel;
    private int selection = -1;
    private Context context;
    private String[] itemsList;

    public AddItemDialog(Context context) {
        super(context); this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_dialog);
        quantity = findViewById(R.id.quantity);
        items = findViewById(R.id.types);
        save = findViewById(R.id.save_dilg);
        cancel = findViewById(R.id.cancel_dilg);
        itemsList = new String[Items.getItems().size()];
        for(Item item : Items.getItems()){
            itemsList[Items.getItems().indexOf(item)] = item.getName()+"/"+item.getUnit();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.text, itemsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        items.setAdapter(adapter);

        items.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selection = adapterView.getSelectedItemPosition();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click here
                if(selection > -1){
                    if(quantity.getText().toString().equals("")){
                        quantity.setError("لم يتم إدخال كمية");
                    }
                    else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setTitle("إضافة كمية");
                        alert.setMessage("متأكد بأنك تريد أن تضيف\n"+quantity.getText().toString()+ " " + Items.getItems().get(selection).getUnit() + " " + Items.getItems().get(selection).getName());
                        alert.setPositiveButton(Data.YES, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Storage storage = Objects.requireNonNull(Branches.getBranchByID(Data.getBranchId())).getStorageByPosition(selection);
                                int newQuantity = storage.getQuantity()+Integer.parseInt(quantity.getText().toString());

                                new UpdateStorageFromServer((Pages) context,String.valueOf(storage.getStorageID()),String.valueOf(newQuantity),String.valueOf(Data.getBranchId()),
                                        storage.getStorageID(),
                                        Integer.parseInt(quantity.getText().toString()),
                                        newQuantity,
                                        storage.getQuantity(),
                                        true,
                                        storage.getBranchID(),
                                        storage.getBranchID());

                                dismiss();
                            }
                        });
                        alert.setNegativeButton(Data.CANCEL, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        alert.show();
                    }
                }
                else {
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

}

