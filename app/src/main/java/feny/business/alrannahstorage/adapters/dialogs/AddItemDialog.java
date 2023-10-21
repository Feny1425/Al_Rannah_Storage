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

import java.util.Arrays;
import java.util.Objects;
import java.util.Vector;

import feny.business.alrannahstorage.Objects.Branches;
import feny.business.alrannahstorage.Objects.Items;
import feny.business.alrannahstorage.Objects.Storage;
import feny.business.alrannahstorage.R;
import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.database.UpdateStorageFromServer;
import feny.business.alrannahstorage.models.ItemType;
import feny.business.alrannahstorage.models.Pages;

public class AddItemDialog extends Dialog {

    private TextView dialogTitle;
    private EditText quantity;
    private Spinner items;
    private Button save,cancel;
    private int selection = -1;
    private int storageID,branchImport;
    private Context context;
    private Vector<String> itemsList;
    private boolean extract,close,Import;

    public AddItemDialog(Context context, int storageID, boolean extract, boolean e2, boolean Import,int branchImport) {
        super(context); this.context=context; this.storageID = storageID;
        this.extract = extract; close = e2; this.Import = Import;
        this.branchImport = branchImport;
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean imported = branchImport != Data.getBranchId();
        setContentView(R.layout.add_item_dialog);
        dialogTitle = findViewById(R.id.label);
        dialogTitle.setText(
        (extract?"إخراج ":(imported?"استيراد ":"إدخال "))+Branches.getStorageByID(storageID).getItem().getName()+((extract)?" من المخزن":" إلى المخزن"));
        quantity = findViewById(R.id.quantity);
        items = findViewById(R.id.types);
        save = findViewById(R.id.save_dilg);
        cancel = findViewById(R.id.cancel_dilg);
        itemsList = new Vector<>();
        if(!extract && !close){
            items.setVisibility(View.GONE);
        }
        if(extract){
            for (ItemType itemType : Items.getItemTypes()){
                int index = Items.getItemTypes().indexOf(itemType);
                if(index != 0) {
                    if(itemType.getType().contains(Branches.getStorageByID(storageID).getItem().getName())){
                        itemsList.add(itemType.getType());
                    }
                }
            }
        }else if(close){
            itemsList.addAll(Arrays.asList(Data.getCLOSE()));
            for (Integer id : Branches.getAllBranchesIDs()){
                    if(Data.getBranchId() != id){
                        itemsList.add(Objects.requireNonNull(Branches.getBranchByID(id)).getName() + " " + Objects.requireNonNull(Branches.getBranchByID(id)).getLocation());
                    }
            }
        }
        if(itemsList.size() == 0){
            itemsList.add(Items.getItemTypes().get(1).getType());
        }
        String[] s = new String[itemsList.size()];
        for (int i = 0; i < itemsList.size(); i++){
            s[i] = itemsList.get(i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.text, s);
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
                                int newQuantity = (extract||close)?storage.getQuantity()-Integer.parseInt(quantity.getText().toString()):storage.getQuantity()+Integer.parseInt(quantity.getText().toString());
                                if((extract||close) & newQuantity<0){
                                    quantity.setError("الكمية غير متوفرة في المخزون");
                                }
                                else {
                                    new UpdateStorageFromServer((Pages) context, String.valueOf(storage.getStorageID()), String.valueOf(newQuantity), String.valueOf(Data.getBranchId()),
                                            storage.getStorageID(),
                                            Integer.parseInt(quantity.getText().toString()),
                                            newQuantity,
                                            storage.getQuantity(),
                                            !(extract || close),
                                            (selection > 3 && close)?Branches.getBranchByNameAndLocation(itemsList.get(selection)).getId():storage.getBranchID(),
                                            branchImport,
                                            close?((selection > 3)?-1:selection+1):0);
                                    if(extract) {
                                        for (Storage _storage : Branches.getBranchByID(storage.getBranchID()).getStorage()) {
                                            if (_storage.getState() == Items.getItemTypeID(itemsList.get(selection)) && _storage.getItem() == storage.getItem()) {
                                                newQuantity = _storage.getQuantity() + Integer.parseInt(quantity.getText().toString());
                                                new UpdateStorageFromServer((Pages) context, String.valueOf(_storage.getStorageID()), String.valueOf(newQuantity), String.valueOf(Data.getBranchId()),
                                                        _storage.getStorageID(),
                                                        Integer.parseInt(quantity.getText().toString()),
                                                        newQuantity,
                                                        _storage.getQuantity(),
                                                        true,
                                                        _storage.getBranchID(),
                                                        _storage.getBranchID(),
                                                        0);
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