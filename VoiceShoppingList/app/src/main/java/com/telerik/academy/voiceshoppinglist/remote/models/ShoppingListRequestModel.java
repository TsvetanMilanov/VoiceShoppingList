package com.telerik.academy.voiceshoppinglist.remote.models;

import com.telerik.academy.voiceshoppinglist.data.models.Product;
import com.telerik.academy.voiceshoppinglist.data.models.ShoppingList;

import java.util.ArrayList;

public class ShoppingListRequestModel {
    public String name;
    public String createdOn;
    public ArrayList<Product> products;
    public long sqliteId;
}
