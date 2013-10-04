package com.lowestprice;

import android.content.ContentValues;
import android.location.Location;
import com.lowestprice.api.Place;

import java.io.Serializable;

public class Session implements Serializable {
    public static final String DATE = "DATE";
    public static final String USER_NAME = "USER_NAME";
    public static final String LOCATION_LONGITUDE = "LOCATION_LONGITUDE";
    public static final String LOCATION_LATITUDE = "LOCATION_LATITUDE";
    public static final String LOCATION_ACCURACY = "LOCATION_ACCURACY";
    public static final String LOCATION_PROVIDER = "LOCATION_PROVIDER";
    public static final String PLACE_NAME = "PLACE_NAME";
    public static final String PLACE_ADDRESS = "PLACE_ADDRESS";
    public static final String PLACE_PHONE = "PLACE_PHONE";
    public static final String PLACE_ID = "PLACE_ID";
    public static final String PLACE_REFERENCE = "PLACE_REFERENCE";
    private final long date;
    private final String userName;
    private final Location location;
    private final Place place;
    private final ContentValues contentValues;

    public Session(long date, String userName, Location location,
                   Place place) {
        this.date = date;
        this.place = place;
        this.location = location;
        this.userName = userName;
        contentValues = new ContentValues();
        contentValues.put(DATE, date);
        contentValues.put(USER_NAME, userName);
        if (place == null || place.name == null) {
            contentValues.put(LOCATION_LONGITUDE, location.getLongitude());
            contentValues.put(LOCATION_LATITUDE, location.getLatitude());
            contentValues.put(LOCATION_ACCURACY, location.getAccuracy());
            contentValues.put(LOCATION_PROVIDER, location.getProvider());
        } else {
            contentValues.put(LOCATION_LONGITUDE, place.geometry.location.lng);
            contentValues.put(LOCATION_LATITUDE, place.geometry.location.lat);
            contentValues.put(LOCATION_ACCURACY, 0);
            contentValues.put(LOCATION_PROVIDER, "Google Place Api");
            contentValues.put(PLACE_NAME, place.name);
            contentValues.put(PLACE_ADDRESS, place.getAddress());
            contentValues.put(PLACE_PHONE, place.formatted_phone_number);
            contentValues.put(PLACE_ID, place.id);
            contentValues.put(PLACE_REFERENCE, place.reference);
        }
    }

    public ContentValues getContentValues() {
        return contentValues;
    }

    public long getDate() {
        return date;
    }

    public Place getPlace() {
        return place;
    }

    public Location getLocation() {
        return location;
    }

    public String getUserName() {
        return userName;
    }
}
