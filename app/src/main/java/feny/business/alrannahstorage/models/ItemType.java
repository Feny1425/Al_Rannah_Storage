package feny.business.alrannahstorage.models;

import static feny.business.alrannahstorage.data.Data.ITEM_TYPES;

public abstract class ItemType {
    public static class Food extends ItemType{
        private static String type = ITEM_TYPES[0];

        public static String getType() {
            return type;
        }
    }
    public static class Reusable extends ItemType{
        private static String type = ITEM_TYPES[1];

        public static String getType() {
            return type;
        }
    }
    public static class NonReusable extends ItemType{
        private static String type = ITEM_TYPES[2];

        public static String getType() {
            return type;
        }
    }
}
