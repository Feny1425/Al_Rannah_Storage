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

import androidx.core.util.Pair;

import java.util.Arrays;
import java.util.Objects;
import java.util.Vector;

import feny.business.alrannahstorage.Objects.Branches;
import feny.business.alrannahstorage.Objects.Items;
import feny.business.alrannahstorage.Objects.Storage;
import feny.business.alrannahstorage.R;
import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.database.UpdateStorageFromServer;
import feny.business.alrannahstorage.models.Branch;
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
    private Vector<String> itemsList;
    private boolean extract,close,Import;
    private Pair<Branch,Pair<Integer,String>> export;

    public AddItemDialog(Context context, int storageID, boolean extract, boolean e2, boolean Import,Pair<Branch,Pair<Integer,String>> export) {
        super(context); this.context=context; this.storageID = storageID;
        this.extract = extract; close = e2; this.Import = Import; this.export = export;
        if(export == null){
            this.export = new Pair<>(new Branch("",-111,"111111111111111111111","12e1weqds","asdqewdas"),new Pair<>(0,""));
        }

    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_dialog);
        dialogTitle = findViewById(R.id.label);

        String[] titels ={
                "إخراج " + Branches.getStorageByID(storageID).getItem().getName() + " من المخزن",
                "إدخال " + Branches.getStorageByID(storageID).getItem().getName() + " إلى المخزن",
                "استيراد " + export.second.first + " " + Branches.getStorageByID(storageID).getStateType().getType() + "\n من " + export.first.getName() + " " + export.first.getLocation()
        };


        dialogTitle.setText((extract||close)?titels[0]:(Import?titels[2]:titels[1]));
        if(Import) dialogTitle.setTextSize(20);
        quantity = findViewById(R.id.quantity);
        items = findViewById(R.id.types);
        if(Import)quantity.setVisibility(View.GONE);
        save = findViewById(R.id.save_dilg);
        if(Import)save.setText("تأكيد");
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
                    if((quantity.getText().toString().equals("") || Integer.parseInt(quantity.getText().toString())==0) && !Import){
                        quantity.setError("لم يتم إدخال كمية");
                    }
                    else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setTitle(extract?"إخراج كمية":"إضافة كمية");
                        alert.setMessage((extract?"متأكد بأنك تريد إخراج\n":"متأكد بأنك تريد أن تضيف\n")+(Import?export.second.first:Integer.parseInt(quantity.getText().toString()))+ " " + Objects.requireNonNull(Branches.getStorageByID(storageID)).getItem().getUnit() + " " + Objects.requireNonNull(Branches.getStorageByID(storageID)).getItem().getName());
                        alert.setPositiveButton(Data.YES, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Storage storage = Objects.requireNonNull(Branches.getStorageByID(storageID));
                                int newQuantity = Import?0:(extract||close)?storage.getQuantity()-Integer.parseInt(quantity.getText().toString()):storage.getQuantity()+Integer.parseInt(quantity.getText().toString());
                                if((extract||close) & newQuantity<0){
                                    quantity.setError("الكمية غير متوفرة في المخزون");
                                }
                                else {
                                    if(Import) {
                                       new UpdateStorageFromServer((Pages) context, String.valueOf(storage.getStorageID()), String.valueOf(export.second.first+storage.getQuantity()), String.valueOf(Data.getBranchId()),
                                                storageID,
                                                export.second.first,
                                                storage.getQuantity()+export.second.first,
                                                storage.getQuantity(),
                                                true,
                                                storage.getBranchID(),
                                                export.first.getId(),
                                                storage.getStateType().getId(),
                                               export.second.second);


                                    }
                                    else {
                                        new UpdateStorageFromServer((Pages) context, String.valueOf(storage.getStorageID()), String.valueOf(newQuantity), String.valueOf(Data.getBranchId()),
                                                storage.getStorageID(),
                                                Integer.parseInt(quantity.getText().toString()),
                                                newQuantity,
                                                storage.getQuantity(),
                                                !(extract || close),
                                                (selection > 3 && close) ? Branches.getBranchByNameAndLocation(itemsList.get(selection)).getId() : storage.getBranchID(),
                                                storage.getBranchID(),
                                                close ? ((selection > 3) ? -1 : selection + 1) : 0);
                                    }
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