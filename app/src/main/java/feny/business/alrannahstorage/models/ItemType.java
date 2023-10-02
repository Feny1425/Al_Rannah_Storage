package feny.business.alrannahstorage.models;

import static feny.business.alrannahstorage.data.Data.ITEM_TYPES;

public abstract class ItemType {
    private static ItemType Food = new Food();
    private static ItemType Reusable = new Reusable();
    private static ItemType NonReusable = new NonReusable();

    public static ItemType getFood() {
        return Food;
    }

    public static ItemType getReusable() {
        return Reusable;
    }

    public static ItemType getNonReusable() {
        return NonReusable;
    }

    private static class Food extends ItemType{
        private static String type = ITEM_TYPES[0];

        public static String getType() {
            return type;
        }
    }
    private static class Reusable extends ItemType{
        private static String type = ITEM_TYPES[1];

        public static String getType() {
            return type;
        }
    }
    private static class NonReusable extends ItemType{
        private static String type = ITEM_TYPES[2];

        public static String getType() {
            return type;
        }
    }
}
