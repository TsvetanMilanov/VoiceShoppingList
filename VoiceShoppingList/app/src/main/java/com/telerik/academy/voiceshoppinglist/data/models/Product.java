package com.telerik.academy.voiceshoppinglist.data.models;

public class Product {
    private Long _ID;
    private String name;
    private Double price;
    private Double quantity;
    private Long shoppingListId;

    public Product(String name, Double price, Double quantity, Long shoppingListId) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.shoppingListId = shoppingListId;
    }

    public Product(Long _ID, String name, Double price, Double quantity, Long shoppingListId) {
        this(name, price, quantity, shoppingListId);
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

    public void setShoppingListId(Long shoppingListId) {
        this.shoppingListId = shoppingListId;
    }
}
