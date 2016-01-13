package com.telerik.academy.voiceshoppinglist.data.models;

import java.io.Serializable;

public class Product implements Serializable {
    private Long _ID;
    private String name;
    private Double price;
    private Double quantity;
    private Long shoppingListId;
    private Boolean isChecked;

    public Product(String name, Double price, Double quantity, Long shoppingListId, Boolean isChecked) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.shoppingListId = shoppingListId;
        this.isChecked = isChecked;
    }

    public Product(Long _ID, String name, Double price, Double quantity, Long shoppingListId, Boolean isChecked) {
        this(name, price, quantity, shoppingListId, isChecked);
        this._ID = _ID;
    }

    public Long get_ID() {
        return _ID;
    }

    public void set_ID(Long _ID) {
        this._ID = _ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Long getShoppingListId() {
        return shoppingListId;
    }

    public Boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }

    public void setShoppingListId(Long shoppingListId) {
        this.shoppingListId = shoppingListId;
    }
}
