package feny.business.alrannahstorage.data;

import java.util.Random;

import feny.business.alrannahstorage.models.custom.Pages;

public final class Data {
    public final static String[] ITEM_TYPES = {"Food","Reusable","NonReusable"};
    public static final String URLBASE = "https://www.eny.sa/api/database/al_rannah/";
    private final static String COMMERCIAL  = "4031059413";
    private final static String ADMIN_PERMSSION = "3432";
    private static int BRANCH_ID = 0;

    public static String[] getCLOSE() {
        return CLOSE;
    }

    private static String[] CLOSE = {"مباع","صدقة","إعاشة","تالف"};

    public static class ClosedOperations{
        public static int EXPIRE = -2;
        public static int EXPORT = -1;
        public static int BUY = 0;
        public static int SELL = 1;
        public static int IMPORT = 2;
        public static int SPOILED = 3;
        public static int CHARITY = 4;
        public static int RATION = 5;
        public static int EXCHANGE_TO = 6;
        public static int EXCHANGE_FROM = 7;
    }
    public static class Places{
        public static int BUY = 1;
        public static int RECIPE = 2;
        public static int NON_FOOD = 3;
        public static int FOOD = 4;
        public static int CLOSE = 5;
        public static int IMPORT = 6;
    }
    public static int getBranchId() {
        return BRANCH_ID;
    }

    public static void setBranchId(int branchId) {
        BRANCH_ID = branchId;
    }

    public static String getUSER() {
        return USER;
    }

    public static void setUSER(String USER) {
        Data.USER = USER;
    }

    private static String USER = "admin";
    public final static String PERMISSION = "PERMISSION";
    public final static String SHARED_PREFERENCES = "SP";
    public static String BASE_URL(String php){
        return URLBASE+php+".php";
    }

    public static String getUserPermission() {
        return USER_PERMISSION;
    }

    public static void setUserPermission(String userPermission) {
        USER_PERMISSION = userPermission;
    }

    private static String USER_PERMISSION = "0";

    public static String getCOMMERCIAL() {
        return COMMERCIAL;
    }
    public static String getAdminPermssion() {
        return ADMIN_PERMSSION;
    }


    public static final String YES = "نعم";
    public static final String CANCEL = "إلغاء";

    public static String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890@!#_/()*&";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();

    }

    private static Pages CONTEXT = new Pages() {
        @Override
        public void refresh() {
            super.refresh();
        }
    };

    public static void setCONTEXT(Pages CONTEXT) {
        Data.CONTEXT = CONTEXT;
    }

    public static Pages getCONTEXT() {
        return CONTEXT;
    }

    public static int countLinesInString(String text) {
        String[] lines = text.split("\\r?\\n"); // Split by newline characters (UNIX and Windows line endings)
        return lines.length;
    }
}