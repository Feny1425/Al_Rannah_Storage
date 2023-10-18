package feny.business.alrannahstorage.adapters.dialogs;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
        import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
        import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
        import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Objects;

import feny.business.alrannahstorage.Objects.Branches;
import feny.business.alrannahstorage.Objects.Items;
import feny.business.alrannahstorage.Objects.Storage;
import feny.business.alrannahstorage.R;
import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.database.UpdateStorageFromServer;
import feny.business.alrannahstorage.models.Item;
import feny.business.alrannahstorage.models.ItemType;
import feny.business.alrannahstorage.models.Pages;

public class AddItemDialog extends Dialog {

    private TextView dialogTitle;
    private EditText quantity;
    private Spinner items;
    private Button save,cancel;
    private int selection = -1;
    private int storageID;
    private Context context;
    private String[] itemsList;
    private boolean extract;
    private int exportB;
    private int importB;

    public AddItemDialog(Context context, int storageID,boolean extract,int exportB, int importB) {
        super(context); this.context=context; this.storageID = storageID;
        this.extract = extract;
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_dialog);
        dialogTitle = findViewById(R.id.label);
        dialogTitle.setText((extract?"إخراج ":"إدخال ")+Branches.getStorageByID(storageID).getItem().getName()+(extract?" من المخزن":" إلى المخزن"));
        quantity = findViewById(R.id.quantity);
        items = findViewById(R.id.types);
        save = findViewById(R.id.save_dilg);
        cancel = findViewById(R.id.cancel_dilg);
        itemsList = new String[Items.getItemTypes().size()-1];
        if(!extract){
            items.setVisibility(View.GONE);
        }
        for (ItemType itemType : Items.getItemTypes()){
            int index = Items.getItemTypes().indexOf(itemType);
            if(index != 0)
            itemsList[index-1]=itemType.getType();
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
                    if(quantity.getText().toString().equals("") || Integer.parseInt(quantity.getText().toString())==0){
                        quantity.setError("لم يتم إدخال كمية");
                    }
                    else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setTitle(extract?"إخراج كمية":"إضافة كمية");
                        alert.setMessage((extract?"متأكد بأنك تريد إخراج\n":"متأكد بأنك تريد أن تضيف\n")+Integer.parseInt(quantity.getText().toString())+ " " + Branches.getStorageByID(storageID).getItem().getUnit() + " " + Branches.getStorageByID(storageID).getItem().getName());
                        alert.setPositiveButton(Data.YES, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Storage storage = Objects.requireNonNull(Branches.getStorageByID(storageID));
                                int newQuantity = extract?storage.getQuantity()-Integer.parseInt(quantity.getText().toString()):storage.getQuantity()+Integer.parseInt(quantity.getText().toString());
                                if(extract & newQuantity<0){
                                    quantity.setError("الكمية غير متوفرة في المخزون");
                                }
                                else {
                                    new UpdateStorageFromServer((Pages) context, String.valueOf(storage.getStorageID()), String.valueOf(newQuantity), String.valueOf(Data.getBranchId()),
                                            storageID,
                                            Integer.parseInt(quantity.getText().toString()),
                                            newQuantity,
                                            storage.getQuantity(),
                                            !extract,
                                            storage.getBranchID(),
                                            storage.getBranchID());
                                    if(extract) {
                                        for (Storage _storage : Branches.getBranchByID(storage.getBranchID()).getStorage()) {
                                            if (_storage.getState() == selection + 1 && _storage.getItem() == storage.getItem()) {
                                                newQuantity = _storage.getQuantity() + Integer.parseInt(quantity.getText().toString());
                                                new UpdateStorageFromServer((Pages) context, String.valueOf(_storage.getStorageID()), String.valueOf(newQuantity), String.valueOf(Data.getBranchId()),
                                                        _storage.getStorageID(),
                                                        Integer.parseInt(quantity.getText().toString()),
                                                        newQuantity,
                                                        _storage.getQuantity(),
                                                        true,
                                                        _storage.getBranchID(),
                                                        _storage.getBranchID());
                                                break;
                                            }
                                        }
                                    }

                                    dismiss();
                                }
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

        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

}

