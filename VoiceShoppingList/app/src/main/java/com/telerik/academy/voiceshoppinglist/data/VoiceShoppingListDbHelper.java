package com.telerik.academy.voiceshoppinglist.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.telerik.academy.voiceshoppinglist.data.contracts.ShoppingListContract;
import com.telerik.academy.voiceshoppinglist.data.models.Product;
import com.telerik.academy.voiceshoppinglist.data.models.ShoppingList;

import java.util.ArrayList;

public class VoiceShoppingListDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "VoiceShoppingList.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String INTEGER_PRIMARY_KEY = " INTEGER PRIMARY KEY,";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_TABLE_SHOPPING_LISTS =
            "CREATE TABLE " + ShoppingListContract.ShoppingListEntry.TABLE_NAME + " (" +
                    ShoppingListContract.ShoppingListEntry._ID + INTEGER_PRIMARY_KEY +
                    ShoppingListContract.ShoppingListEntry.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    ShoppingListContract.ShoppingListEntry.COLUMN_CREATED_ON + TEXT_TYPE + COMMA_SEP +
                    ShoppingListContract.ShoppingListEntry.COLUMN_LATITUDE + REAL_TYPE + COMMA_SEP +
                    ShoppingListContract.ShoppingListEntry.COLUMN_LONGITUDE + REAL_TYPE +
                    " )";
    private static final String SQL_CREATE_TABLE_PRODUCTS =
            "CREATE TABLE " + ShoppingListContract.ProductEntry.TABLE_NAME + " (" +
                    ShoppingListContract.ProductEntry._ID + INTEGER_PRIMARY_KEY +
                    ShoppingListContract.ProductEntry.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    ShoppingListContract.ProductEntry.COLUMN_PRICE + REAL_TYPE + COMMA_SEP +
                    ShoppingListContract.ProductEntry.COLUMN_QUANTITY + REAL_TYPE + COMMA_SEP +
                    ShoppingListContract.ProductEntry.COLUMN_SHOPPING_LIST_ID + INTEGER_TYPE + COMMA_SEP +
                    ShoppingListContract.ProductEntry.COLUMN_IS_CHECKED + INTEGER_TYPE +
                    " )";
    private static final String COLUMN_NAME_NULLABLE = "NULLABLE";

    public VoiceShoppingListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_SHOPPING_LISTS);
        db.execSQL(SQL_CREATE_TABLE_PRODUCTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public long addShoppingList(ShoppingList shoppingList) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ShoppingListContract.ShoppingListEntry.COLUMN_NAME, shoppingList.getName());
        values.put(ShoppingListContract.ShoppingListEntry.COLUMN_CREATED_ON, shoppingList.getCreatedOn());
        values.put(ShoppingListContract.ShoppingListEntry.COLUMN_LATITUDE, shoppingList.getLatitude());
        values.put(ShoppingListContract.ShoppingListEntry.COLUMN_LONGITUDE, shoppingList.getLongitude());

        long newRowId;
        newRowId = db.insert(
                ShoppingListContract.ShoppingListEntry.TABLE_NAME,
                COLUMN_NAME_NULLABLE,
                values);

        db.close();

        return newRowId;
    }

    public ArrayList<ShoppingList> getAllShoppingLists() {
        ArrayList<ShoppingList> allShoppingLists = new ArrayList<>();

        String selectAllQuery =
                "SELECT * FROM " + ShoppingListContract.ShoppingListEntry.TABLE_NAME + ";";

        SQLiteDatabase database = getWritableDatabase();

        Cursor cursor = database.rawQuery(selectAllQuery, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            ShoppingList currentShoppingList = createShoppingListFromCursor(cursor);

            if (currentShoppingList != null) {
                allShoppingLists.add(currentShoppingList);
            }

            cursor.moveToNext();
        }

        cursor.close();
        database.close();
        return allShoppingLists;
    }

    public ShoppingList getShoppingListById(Long id) {
        String selectAllQuery =
                "SELECT * FROM " + ShoppingListContract.ShoppingListEntry.TABLE_NAME +
                        " WHERE " + ShoppingListContract.ShoppingListEntry._ID + " = " + id + ";";

        SQLiteDatabase database = getWritableDatabase();

        Cursor cursor = database.rawQuery(selectAllQuery, null);
        cursor.moveToFirst();

        ShoppingList shoppingList = createShoppingListFromCursor(cursor);

        cursor.close();
        database.close();
        return shoppingList;
    }

    public long addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ShoppingListContract.ProductEntry.COLUMN_NAME, product.getName());
        values.put(ShoppingListContract.ProductEntry.COLUMN_PRICE, product.getPrice());
        values.put(ShoppingListContract.ProductEntry.COLUMN_QUANTITY, product.getQuantity());
        values.put(ShoppingListContract.ProductEntry.COLUMN_SHOPPING_LIST_ID, product.getShoppingListId());
        values.put(ShoppingListContract.ProductEntry.COLUMN_IS_CHECKED, product.getIsChecked() ? 1 : 0);

        long newRowId;
        newRowId = db.insert(
                ShoppingListContract.ProductEntry.TABLE_NAME,
                COLUMN_NAME_NULLABLE,
                values);

        db.close();

        return newRowId;
    }

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> allProducts = new ArrayList<>();

        String selectAllQuery =
                "SELECT * FROM " + ShoppingListContract.ProductEntry.TABLE_NAME + ";";

        SQLiteDatabase database = getWritableDatabase();

        Cursor cursor = database.rawQuery(selectAllQuery, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Product currentProduct = createProductFromCursor(cursor);

            if (currentProduct != null) {
                allProducts.add(currentProduct);
            }

            cursor.moveToNext();
        }

        cursor.close();
        database.close();
        return allProducts;
    }

    public ArrayList<Product> getProductsByShoppingListId(Long id) {
        ArrayList<Product> allProducts = new ArrayList<>();

        String selectAllQuery =
                "SELECT * FROM " + ShoppingListContract.ProductEntry.TABLE_NAME +
                        " WHERE " + ShoppingListContract.ProductEntry.COLUMN_SHOPPING_LIST_ID + " = " + id + ";";

        SQLiteDatabase database = getWritableDatabase();

        Cursor cursor = database.rawQuery(selectAllQuery, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Product currentProduct = createProductFromCursor(cursor);

            if (currentProduct != null) {
                allProducts.add(currentProduct);
            }

            cursor.moveToNext();
        }

        cursor.close();
        database.close();
        return allProducts;
    }

    public Product getProductById(Long id) {
        String selectAllQuery =
                "SELECT * FROM " + ShoppingListContract.ProductEntry.TABLE_NAME +
                        " WHERE " + ShoppingListContract.ProductEntry._ID + " = " + id + ";";

        SQLiteDatabase database = getWritableDatabase();

        Cursor cursor = database.rawQuery(selectAllQuery, null);
        cursor.moveToFirst();

        Product product = createProductFromCursor(cursor);

        cursor.close();
        database.close();
        return product;
    }

    public void deleteAllShoppingLists() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(ShoppingListContract.ShoppingListEntry.TABLE_NAME, null, null);
        db.delete(ShoppingListContract.ProductEntry.TABLE_NAME, null, null);
    }

    private ShoppingList createShoppingListFromCursor(Cursor cursor) {
        Long id = cursor.getLong(cursor.getColumnIndex(ShoppingListContract.ShoppingListEntry._ID));
        Double latitude = cursor.getDouble(cursor.getColumnIndex(ShoppingListContract.ShoppingListEntry.COLUMN_LATITUDE));
        Double longitude = cursor.getDouble(cursor.getColumnIndex(ShoppingListContract.ShoppingListEntry.COLUMN_LONGITUDE));
        String createdOn = cursor.getString(cursor.getColumnIndex(ShoppingListContract.ShoppingListEntry.COLUMN_CREATED_ON));
        String name = cursor.getString(cursor.getColumnIndex(ShoppingListContract.ShoppingListEntry.COLUMN_NAME));

        ShoppingList shoppingList = new ShoppingList(id, createdOn, latitude, longitude, name);

        return shoppingList;
    }

    private Product createProductFromCursor(Cursor cursor) {
        Long id = cursor.getLong(cursor.getColumnIndex(ShoppingListContract.ProductEntry._ID));
        Double price = cursor.getDouble(cursor.getColumnIndex(ShoppingListContract.ProductEntry.COLUMN_PRICE));
        Double quantity = cursor.getDouble(cursor.getColumnIndex(ShoppingListContract.ProductEntry.COLUMN_QUANTITY));
        Long shoppingListId = cursor.getLong(cursor.getColumnIndex(ShoppingListContract.ProductEntry.COLUMN_SHOPPING_LIST_ID));
        String name = cursor.getString(cursor.getColumnIndex(ShoppingListContract.ProductEntry.COLUMN_NAME));
        Boolean isChecked = cursor.getInt(cursor.getColumnIndex(ShoppingListContract.ProductEntry.COLUMN_IS_CHECKED)) == 0 ? false : true;

        Product product = new Product(id, name, price, quantity, shoppingListId, isChecked);

        return product;
    }
}
