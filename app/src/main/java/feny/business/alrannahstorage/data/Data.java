package feny.business.alrannahstorage.data;

import feny.business.alrannahstorage.models.Item;
import feny.business.alrannahstorage.models.ItemType;

public final class Data {
    public final static String[] ITEM_TYPES = {"Food","Reusable","NonReusable"};

    public static final class Items{
        public static final class Foods {
            public static final Item Chicken = new Item("Chicken",1,"حبة", new ItemType.Food());
        }
        public static final class Reusable {
            public static final Item Broom = new Item("Broom",1,"حبة", new ItemType.Reusable());
        }
        public static final class NonReusable {
            public static final Item Gloves = new Item("Gloves",1,"كرتون", new ItemType.NonReusable());
        }
    }
}