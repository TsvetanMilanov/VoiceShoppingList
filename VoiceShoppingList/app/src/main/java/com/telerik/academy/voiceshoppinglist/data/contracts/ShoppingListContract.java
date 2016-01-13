package com.telerik.academy.voiceshoppinglist.data.contracts;

import android.provider.BaseColumns;

public final class ShoppingListContract {
    public static class ShoppingListEntry implements BaseColumns {
        public static final String TABLE_NAME = "shopping_lists";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_CREATED_ON = "created_on";
    }

    public static class ProductEntry implements BaseColumns {
        public static final String TABLE_NAME = "products";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_SHOPPING_LIST_ID = "shopping_list_id";
        public static final String COLUMN_IS_CHECKED = "is_checked";
    }
}
