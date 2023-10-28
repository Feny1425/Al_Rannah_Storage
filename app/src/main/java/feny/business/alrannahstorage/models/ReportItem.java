package feny.business.alrannahstorage.models;

import static feny.business.alrannahstorage.models.ReportItem.MESSAGES.LINE;
import static feny.business.alrannahstorage.models.ReportItem.MESSAGES.NEXT_LINE;
import static feny.business.alrannahstorage.models.ReportItem.MESSAGES.SELECT_MESSAGE;
import static feny.business.alrannahstorage.models.ReportItem.MESSAGES.SMALL_TAP;
import static feny.business.alrannahstorage.models.ReportItem.MESSAGES.TAP;

import android.util.Pair;

import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import feny.business.alrannahstorage.Objects.Branches;
import feny.business.alrannahstorage.Objects.Items;

public class ReportItem {
    private Set<Item> items = new HashSet<>();
    public Pair<int[],int[]> select;

    public ReportItem( Pair<int[],int[]> select) {
        this.select = select;
    }

    public void addItem(int itemID) {
        for(Item item : items){
            if(item.itemID == itemID){
                return;
            }
        }
        items.add(new Item(itemID));
    }

    public Vector<String> getItemsShort() {
        Vector<String> reports = new Vector<>();
        String date="";
        if(select.first[0] != 0){
            date+=("من " + select.first[0] + "/" + select.first[2] + "/" + select.first[3]);
        }
        if(select.second[0] != 0){
            if(!date.equals("")){
                date+="\n";
            }
            date+=("إلى " + select.second[0] + "/" + select.second[2] + "/" + select.second[3]);
        }
        reports.add(date);
        for(Item item : items) {
            reports.addAll(item.getShort());
        }
        return reports;
    }
    public Vector<String> getItemsS() {
        Vector<String> reports = new Vector<>();
        for(Item item : items) {
            reports.addAll(item.getTypes());
        }
        return reports;
    }
    public Set<Item> getItems(){
        return items;
    }

    public class Item {
        int itemID;
        String unit;
        String name;
        private Vector<ItemType> itemTypes = new Vector<>();

        public int getItemID() {
            return itemID;
        }

        public Item(int itemID) {
            this.itemID = itemID;
            feny.business.alrannahstorage.models.Item item = Items.getItemByID(itemID);
            unit = item.getUnit();
            name = item.getName();
        }

        public void addItemType(int itemTypeID, int oldQuant, int newQuant, int Quant, String Date,int closedOperation,int imBranch,int exBranch) {
            String dateNoTime = StringUtils.left(Date, 10);
            String[] dates = dateNoTime.split("-");
            int year = Integer.parseInt(dates[0]);
            int month = Integer.parseInt(dates[1]);
            int day = Integer.parseInt(dates[2]);

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);

            int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

            int fromYear = select.first[0], toYear = select.second[0];
            int fromDay = select.first[1], toDay = select.second[1];


            boolean to = (toYear==0||(year <= toYear && dayOfYear <= toDay));
            boolean from = (fromYear==0||(year >= fromYear && dayOfYear >= fromDay));
            // Check if today is within the desired range or equals
            if ( to && from) {
                // Date is within the specified range or there's no range specified
                // Add the item type to the list
                feny.business.alrannahstorage.models.ItemType itemType = Items.getItemTypesByID(itemTypeID);
                if (itemType.getType().contains(name) || itemType.getId() == 0) {
                    itemTypes.add(new ItemType(itemTypeID, oldQuant, newQuant, Quant, Date, closedOperation, imBranch, exBranch));
                }
            }




        }

        public Vector<String> getTypes() {
            String head = name + " :- \n";
            if(itemTypes.size()>0){
                itemTypes.get(0).setHead("┓ "+head);
            }
            Vector<String> strings = new Vector<>();
            for(ItemType itemType : itemTypes){
                String body = "";
                body += itemType.toString();
                if(itemTypes.indexOf(itemType) != itemTypes.size()-1){
                    body += "|"+"\n"+"|";
                }
                else body+= "\n┚\n";
                strings.add(body);
            }
            return strings;
        }


        public Vector<String> getShort() {
            Vector<Four<Integer,Integer,Integer,Integer>> quantities = new Vector<>();
            if(itemTypes.size() > 0){
                for(ItemType itemType : itemTypes){
                    boolean go = true;
                    if(quantities.size() > 0){
                        for(Four<Integer,Integer,Integer,Integer> four : quantities){
                            if(four.first == itemType.itemTypeID){
                                four.third += itemType.getQuant();
                                four.fourth += itemType.getQuant();
                                go = false;
                            }
                        }
                    }
                    else {
                        quantities.add(new Four<>(itemType.getItemTypeID(), itemType.getOldQuant(), itemType.getQuant(),itemType.getNewQuant()));
                        go = false;
                    }
                    if(go){
                        quantities.add(new Four<>(itemType.getItemTypeID(), itemType.getOldQuant(), itemType.getQuant(),itemType.getNewQuant()));
                    }
                }

            }
            Vector<String> strings =  new Vector<>();
            for(Four<Integer,Integer,Integer,Integer> four : quantities){
                StringBuilder s = new StringBuilder();
                s.append(NEXT_LINE).append(SMALL_TAP).append(Items.getItemByID(itemID).getName()).append(" : ").append(Items.getItemTypesByID(four.first).getType()).append(" :").append(NEXT_LINE);
                s.append(TAP).append(four.third > 0 ? "تم إضافة " : "تم خصم ").append(four.third).append(" ").append(Items.getItemByID(itemID).getUnit()).append(NEXT_LINE);
                s.append(TAP).append("الكمية القديمة ").append(four.second).append(NEXT_LINE);
                s.append(TAP).append("الكمية الجديدة ").append(four.fourth).append(NEXT_LINE);
                s.append(LINE);
                strings.add(s.toString());
            }
            return strings;
        }

        public class ItemType {
            String head = "";

            public void setHead(String head) {
                this.head = head;
            }

            int itemTypeID;
            int oldQuant;
            int newQuant;
            int Quant;
            int year;
            int month;
            int day;
            String Date;
            int closedOperation;
            int imBranch;
            int exBranch;

            public boolean isAdd() {
                return (oldQuant-newQuant < 0);
            }

            public int getItemTypeID() {
                return itemTypeID;
            }

            public int getOldQuant() {
                return oldQuant;
            }

            public int getNewQuant() {
                return newQuant;
            }

            public int getQuant() {
                return Quant * (isAdd()?1:-1);
            }

            public int getYear() {
                return year;
            }

            public int getMonth() {
                return month;
            }

            public int getDay() {
                return day;
            }

            public String getDate() {
                return Date;
            }

            public int getClosedOperation() {
                return closedOperation;
            }

            public ItemType(int itemTypeID, int oldQuant, int newQuant, int quant, String date, int closedOperation,int imBranch,int exBranch) {
                this.itemTypeID = itemTypeID;
                this.oldQuant = oldQuant;
                this.newQuant = newQuant;
                Quant = quant;
                String dateNoTime = StringUtils.left(date,10);
                String[] dates = dateNoTime.split("-");
                this.year = Integer.parseInt(dates[0]);
                this.month = Integer.parseInt(dates[1]);
                this.day = Integer.parseInt(dates[2]);
                Date = date;
                this.closedOperation = closedOperation;
                this.imBranch = imBranch;
                this.exBranch = exBranch;
            }

            @Override
            public String toString() {

                Branch importBranch = Branches.getBranchByID(imBranch);
                Branch exportBranch = Branches.getBranchByID(exBranch);
                String itemTypeName = "|------ "+Items.getItemTypesByID(itemTypeID).getType()+NEXT_LINE;
                String quantity = "|"+TAP+TAP+"+ "+SELECT_MESSAGE(closedOperation) + Quant + " " + unit+NEXT_LINE +
                        (closedOperation==-1? "|"+TAP+TAP+"+ " + "إلى : " + importBranch.getName() + " " + importBranch.getLocation() +NEXT_LINE :
                        (closedOperation== 2? "|"+TAP+TAP+"+ " + "من : " + exportBranch.getName() + " " + importBranch.getLocation() +NEXT_LINE:""));
                String old ="|"+ TAP+TAP+"+ "+"الكمية القديمة " + oldQuant +" " +unit+NEXT_LINE;
                String New ="|"+ TAP+TAP+"+ "+"الكمية الجديدة " + newQuant+" " +unit+NEXT_LINE;
                String Date ="|"+ TAP+TAP+"+ "+"التاريخ : "+this.Date;
                return head+itemTypeName+quantity+old+New+Date;
            }
        }
    }




    static class MESSAGES{
        public static String NEXT_LINE = "\n";
        public static String SMALL_TAP = "\t\t\t";
        public static String TAP = "\t\t\t\t\t";
        public static String LINE = "_____________________________________________";
        public static String EXPORT = "تم تصدير ";
        public static String BUY = "تم شراء ";
        public static String SELL = "تم بيع ";
        public static String IMPORT = "تم استيراد ";
        public static String SPOILED = "الفاسد ";
        public static String CHARITY = "تم التبرع بـ ";
        public static String RATION = "اعاشة العمال ";
        public static String EXCHANGE_TO = "تم طبخ ";
        public static String EXCHANGE = "تم اخراج ";
        public static String SELECT_MESSAGE(int Operation){
            String op = "";
            switch (Operation){
                case -1:
                    op = EXPORT;
                    break;
                case 0:
                    op = BUY;
                    break;
                case 1:
                    op = SELL;
                    break;
                case 2:
                    op = IMPORT;
                    break;
                case 3:
                    op = SPOILED;
                    break;
                case 4:
                    op = CHARITY;
                    break;
                case 5:
                    op = RATION;
                    break;
                case 6:
                    op = EXCHANGE_TO;
                    break;
                case 7:
                    op = EXCHANGE;
                    break;
            }
            return op;
        }
    }
}