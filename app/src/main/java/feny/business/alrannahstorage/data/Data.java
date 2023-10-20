package feny.business.alrannahstorage.data;

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

    public static final int EXPORTED = -1;

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

}
