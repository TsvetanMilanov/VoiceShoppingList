package com.telerik.academy.voiceshoppinglist.data.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ShoppingList implements Serializable {
    private static final String DEFAULT_SQLITE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private Long _ID;
    private String name;
    private Double latitude;
    private Double longitude;
    private String createdOn;

    public ShoppingList(String createdOn, Double latitude, Double longitude, String name) {
        this.createdOn = createdOn;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    public ShoppingList(Date createdOn, Double latitude, Double longitude, String name) {
        this(getDateTime(createdOn), latitude, longitude, name);
    }

    public ShoppingList(Long _ID, String createdOn, Double latitude, Double longitude, String name) {
        this(createdOn, latitude, longitude, name);
        this._ID = _ID;
    }

    public ShoppingList(Long _ID, Date createdOn, Double latitude, Double longitude, String name) {
        this(createdOn, latitude, longitude, name);
        this._ID = _ID;
    }

    public Long get_ID() {
        return _ID;
    }

    public void set_ID(Long _ID) {
        this._ID = _ID;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = this.getDateTime(createdOn);
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private static String getDateTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                DEFAULT_SQLITE_DATE_FORMAT, Locale.getDefault());
        return dateFormat.format(date);
    }
}
