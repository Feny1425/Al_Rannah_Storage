package feny.business.alrannahstorage.data;

import feny.business.alrannahstorage.models.Item;
import feny.business.alrannahstorage.models.ItemType;

public final class Data {
    public final static String[] ITEM_TYPES = {"Food","Reusable","NonReusable"};
    public static final String URLBASE = "https://www.eny.sa/api/database/";
    private final static String COMMERCIAL  = "4031059413";
    private final static int ADMIN_PERMSSION = 3432;
    public final static String PERMISSION = "PERMISSION";
    public final static String SHARED_PREFERENCES = "SP";

    public static int getUserPermission() {
        return USER_PERMISSION;
    }

    public static void setUserPermission(int userPermission) {
        USER_PERMISSION = userPermission;
    }

    private static int USER_PERMISSION = 0;

    public static String getCOMMERCIAL() {
        return COMMERCIAL;
    }
    public static int getAdminPermssion() {
        return ADMIN_PERMSSION;
    }

    //region items
    public static final Items.Foods Tomato = new Items.Foods("Tomato",0,"كيلو");
    //endregion items


    public static final String YES = "نعم";
    public static final String CANCEL = "إلغاء";



    public static final class Items{
        public static final class Foods extends Item {

            public Foods(String name, int quantity, String unit) {
                super(name, quantity, unit, ItemType.getFood());
            }
        }
        public static final class Reusable extends Item {
            public Reusable(String name, int quantity, String unit) {
                super(name, quantity, unit, ItemType.getReusable());
            }
        }
        public static final class NonReusable extends Item {
            public NonReusable(String name, int quantity, String unit) {
                super(name, quantity, unit, ItemType.getNonReusable());
            }
        }
    }
}
