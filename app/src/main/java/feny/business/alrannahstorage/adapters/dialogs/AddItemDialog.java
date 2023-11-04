package feny.business.alrannahstorage.adapters.dialogs;

import static feny.business.alrannahstorage.data.Data.ClosedOperations.BUY;
import static feny.business.alrannahstorage.data.Data.ClosedOperations.CHARITY;
import static feny.business.alrannahstorage.data.Data.ClosedOperations.EXCHANGE_FROM;
import static feny.business.alrannahstorage.data.Data.ClosedOperations.EXCHANGE_TO;
import static feny.business.alrannahstorage.data.Data.ClosedOperations.EXPORT;
import static feny.business.alrannahstorage.data.Data.ClosedOperations.IMPORT;
import static feny.business.alrannahstorage.data.Data.ClosedOperations.RATION;
import static feny.business.alrannahstorage.data.Data.ClosedOperations.SELL;
import static feny.business.alrannahstorage.data.Data.ClosedOperations.SPOILED;

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
import feny.business.alrannahstorage.R;
import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.database.UpdateStorage;
import feny.business.alrannahstorage.models.custom.Pages;
import feny.business.alrannahstorage.models.custom.RecipeItem;
import feny.business.alrannahstorage.models.custom.Storage;

public class AddItemDialog extends Dialog {

    private TextView dialogTitle;
    private EditText quantity;
    private Spinner items;
    private Button save, cancel;
    private int selection = -1;
    private Storage storage;
    private Context context;
    private Vector<String> itemsList;

    public AddItemDialog(Context context, Storage storage) {
        super(context);
        this.context = context;
        this.storage = storage;

    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_dialog);
        dialogTitle = findViewById(R.id.label);

        String[] titels = {
                "إخراج " + storage.getName() + " من المخزن",
                "تقفيل ال" + storage.getName(),
                "إدخال " + storage.getName() + " إلى المخزن",
                "استيراد " + storage.getQuantity() + " " + storage.getName() + "\n من " + storage.getOtherStorage().getBranch().getName() + " " + storage.getOtherStorage().getBranch().getLocation(),
                "سيتم طبخ " + "حصة كاملة من الـ" + storage.getName()
        };


        dialogTitle.setText(storage.isImported() ? titels[3] : storage.isClose() ? titels[1] : storage.isExtracted() ? titels[0] : storage.isRecipe() ? titels[4] : titels[2]);

        quantity = findViewById(R.id.quantity);
        items = findViewById(R.id.types);
        save = findViewById(R.id.save_dilg);
        cancel = findViewById(R.id.cancel_dilg);
        itemsList = new Vector<>();
        if (storage.isImported()) {
            dialogTitle.setTextSize(20);
            quantity.setVisibility(View.GONE);
            save.setText("تأكيد");
        }

        if((storage.isClose()||storage.isImported())) {
            items.setVisibility(View.VISIBLE);
        }

        if(storage.isClose()){
            itemsList.addAll(Arrays.asList(Data.getCLOSE()));
            for (Integer id : Branches.getAllBranchesIDs()){
                    if(Data.getBranchId() != id){
                        itemsList.add(Objects.requireNonNull(Branches.getBranchByID(id)).getName() + " " + Objects.requireNonNull(Branches.getBranchByID(id)).getLocation());
                    }
            }
        }
        if (itemsList.size() == 0) {
            //itemsList.add(Items.getItemTypes().get(1).getType());
        }
        String[] s = new String[itemsList.size()];
        for (int i = 0; i < itemsList.size(); i++) {
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
                int quant = Integer.parseInt(quantity.getText().toString());
                if ((quantity.getText().toString().equals("") || quant == 0)
                        && !storage.isImported()) {
                    quantity.setError("لم يتم إدخال كمية");
                } else {
                    boolean extract = storage.isExtracted() || storage.isRecipe() || storage.isClose();
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle(extract ? "إخراج كمية" : "إضافة كمية");
                    alert.setMessage((storage.isExtracted()||storage.isClose() ? "متأكد بأنك تريد إخراج\n" : storage.isRecipe() ? "متأكد بأنك تريد أن تطبخ\n" : "متأكد بأنك تريد أن تضيف\n") + (storage.isImported() ? storage.getQuantity() : storage.isRecipe()? (quant*storage.getItemType().getRecipe().getQuantity()) :quant) + " " + storage.getUnit() + " " + storage.getName());
                    alert.setPositiveButton(Data.YES, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int newQuantity = storage.isImported() ? 0 : (storage.isExtracted() || storage.isClose()) ? storage.getQuantity() - Integer.parseInt(quantity.getText().toString()) : storage.getQuantity() + Integer.parseInt(quantity.getText().toString());
                            if(storage.isRecipe()){
                                newQuantity = storage.getQuantity()+quant;
                            }
                            if ((storage.isExtracted() || storage.isClose()) & newQuantity < 0) {
                                quantity.setError("الكمية غير متوفرة في المخزون");
                            }  else if((storage.isRecipe()&&!storage.isClose()) && quant>storage.getItemType().getRecipe().HowManyCanBeCooked()) {
                                quantity.setError("الكميات الموجودة في المخزون \n لا يمكنها تغطية عدد الحصص المطلوبة" + "\n" + "رجاء اضغط القِ نظرة على التفاصيل");
                            }
                            else {
                                if (storage.isImported()) {
                                    new UpdateStorage((Pages) context,//context
                                            String.valueOf(Data.getBranchId()),//branch id
                                            storage.getStorageID(),//storage id
                                            storage.getOtherStorage().getQuantity(),//quantity
                                            storage.getQuantity() + storage.getOtherStorage().getQuantity(),//new quantity
                                            storage.getQuantity(),//old quantity
                                            true,//add
                                            storage.getBranchID(),//import branch
                                            storage.getOtherStorage().getBranchID(),//export branch
                                            IMPORT,//closed operation
                                            storage.getSalted());//salted string


                                } else {
                                    int closed;
                                    if (storage.isClose()) {
                                        switch (selection) {
                                            case 0:
                                                closed = SELL;
                                                break;
                                            case 1:
                                                closed = CHARITY;
                                                break;
                                            case 2:
                                                closed = RATION;
                                                break;
                                            case 3:
                                                closed = SPOILED;
                                                break;
                                            default:
                                                closed = EXPORT;
                                        }
                                    } else if (storage.isExtracted() || storage.isRecipe()) {
                                        closed = EXCHANGE_FROM;
                                    } else {
                                        closed = BUY;
                                    }

                                    new UpdateStorage((Pages) context, String.valueOf(storage.getStorageID()),  String.valueOf(Data.getBranchId()),
                                            storage.getStorageID(),
                                            Integer.parseInt(quantity.getText().toString()),
                                            newQuantity,
                                            storage.getQuantity(),
                                            !(storage.isExtracted() || storage.isClose()),
                                            (selection > 3 && storage.isClose()) ? Branches.getBranchByNameAndLocation(itemsList.get(selection)).getId() : storage.getBranchID(),
                                            storage.getBranchID(),
                                            closed);
                                }
                                if (storage.isExtracted()) {
                                    new UpdateStorage((Pages) context, String.valueOf(storage.getStorageID()), String.valueOf(Data.getBranchId()),
                                            storage.getStorageID(),
                                            Integer.parseInt(quantity.getText().toString()),
                                            newQuantity,
                                            storage.getQuantity(),
                                            false,
                                            storage.getBranchID(),
                                            storage.getBranchID(),
                                            EXCHANGE_FROM);
                                } else if (storage.isRecipe()) {
                                    new UpdateStorage((Pages) context, String.valueOf(storage.getStorageID()),  String.valueOf(Data.getBranchId()),
                                            storage.getStorageID(),
                                            quant*storage.getItemType().getRecipe().getQuantity(),
                                            storage.getQuantity()+quant*storage.getItemType().getRecipe().getQuantity(),
                                            storage.getQuantity(),
                                            true,
                                            storage.getBranchID(),
                                            storage.getBranchID(),
                                            EXCHANGE_TO);
                                    for(RecipeItem item : storage.getItemType().getRecipe().getRecipeItems()){
                                        Storage _storage = Branches.getStorageByItemID(item.getItem_id());
                                        new UpdateStorage((Pages) context, String.valueOf(_storage.getStorageID()), String.valueOf(Data.getBranchId()),
                                                _storage.getStorageID(),
                                                quant*item.getQuantity(),
                                                _storage.getQuantity()-(quant* item.getQuantity()),
                                                storage.getQuantity(),
                                                false,
                                                storage.getBranchID(),
                                                storage.getBranchID(),
                                                EXCHANGE_FROM);
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