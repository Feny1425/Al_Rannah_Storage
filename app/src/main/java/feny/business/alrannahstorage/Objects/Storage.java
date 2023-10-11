package feny.business.alrannahstorage.Objects;

import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import feny.business.alrannahstorage.activities.AdminActivity;
import feny.business.alrannahstorage.activities.LoginActivity;
import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.data.PushPullData;
import feny.business.alrannahstorage.models.Item;
import feny.business.alrannahstorage.models.ItemType;

public class Storage {
    private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<Item> oldItems = new ArrayList<>();


    public Storage() {
    }

    public void addItem(Item item){
        items.add(item);
        oldItems.add(item);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            addHistory(item);
        }
    }
    public void editItem(int position, Item item){
        items.set(position,item);
    }
    public void editItem(int position, String name,String unit, int quantity, ItemType type){
        Item item = items.get(position);
        item.setName(name);
        item.setUnit(unit);
        item.setQuantity(quantity);
        item.setType(type);
        items.set(position, item);
    }
    public void increaseItem(int position, int add){
        items.get(position).increaseQuantity(add);
    }

    public void decreaseItem(int position, int subtract){
        if(items.get(position).getQuantity()>0) {
            items.get(position).increaseQuantity(-subtract);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void checkChanges(){
        for(int i = 0 ; i < items.size();i++){
            if(items.get(i).getQuantity() != oldItems.get(i).getQuantity()){
                if (items.get(i).getQuantity() - oldItems.get(i).getQuantity() > 0){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        addHistory(items.get(i), true, (items.get(i).getQuantity() - oldItems.get(i).getQuantity()));
                    }
                }
                else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    addHistory(items.get(i), false, -(items.get(i).getQuantity() - oldItems.get(i).getQuantity()));
                }
                setOldItems();
            }
        }
    }

    public void setOldItems(){
        oldItems = items;
    }


    public ArrayList<Item> getItems() {
        return items;
    }
    ArrayList<String> history = new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addHistory(@NonNull Item item, Boolean add, int quantity){
        String data;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        data = "History : " + dtf.format(now);
        data = data + " \n" +(add?"add ":"decrease ") +quantity + " " + item.getName() + "\n new quantity: " + item.getQuantity() + " " + item.getUnit();
        history.add(data);

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addHistory(@NonNull Item item){
        String data;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        data =dtf.format(now) + ":";
        data = data + " \n new item added;name:"+item.getName()+";unit:"+item.getUnit()+";quantity:"+item.getQuantity();
        history.add(data);

    }

    public ArrayList<String> getHistory() {
        return history;
    }
}
