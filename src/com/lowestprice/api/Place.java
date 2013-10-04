package com.lowestprice.api;

import com.google.api.client.util.Key;

import java.io.Serializable;

/** Implement this class from "Serializable"
* So that you can pass this class Object to another using Intents
* Otherwise you can't pass to another actitivy
* */
public class Place implements Serializable {

    @Key
    public String id;

    @Key
    public String name;

    @Key
    public String reference;

    @Key
    public String icon;

    @Key
    public String vicinity;

    @Key
    public Geometry geometry;

    @Key
    public String formatted_address;

    @Key
    public String formatted_phone_number;

    @Key
    public String[] types;

    public Place(String id, String name, String reference,
                 String formatted_address, String formatted_phone_number) {
        this.id = id;
        this.name = name;
        this.reference = reference;
        this.formatted_address = formatted_address;
        this.formatted_phone_number = formatted_phone_number;
    }

    @Override
    public String toString() {
        return name + " - " + id + " - " + reference;
    }

    public String getAddress() {
        return formatted_address != null ? formatted_address : vicinity;
    }

    public static class Geometry implements Serializable
    {
        @Key
        public Location location;
    }

    public static class Location implements Serializable
    {
        @Key
        public double lat;

        @Key
        public double lng;
    }

}