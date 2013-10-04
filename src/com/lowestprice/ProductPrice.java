package com.lowestprice;

import android.content.ContentValues;

import java.io.Serializable;

public class ProductPrice implements Serializable {
    private final String barcode;
    private final String barcodeType;
    private final String productName;
    private final String productType;
    private final double price;
    private final int quantity;
    private final long sessionId;
    public static final String BARCODE = "BARCODE";
    public static final String BARCODE_TYPE = "BARCODE_TYPE";
    public static final String PRODUCT_NAME = "PRODUCT_NAME";
    public static final String PRODUCT_TYPE = "PRODUCT_TYPE";
    public static final String PRICE = "PRICE";
    public static final String QUANTITY = "QUANTITY";
    public static final String SESSION_ID = "SESSION_ID";
    private final ContentValues contentValues;

    public ProductPrice(String barcode, String barcodeType, String productName, String productType, double price, int quantity, long sessionId) {
        this.barcode = barcode;
        this.barcodeType = barcodeType;
        this.productName = productName;
        this.productType = productType;
        this.price = price;
        this.quantity = quantity;
        this.sessionId = sessionId;
        contentValues = new ContentValues();
        contentValues.put(BARCODE, barcode);
        contentValues.put(BARCODE_TYPE, barcodeType);
        contentValues.put(PRODUCT_NAME, productName);
        contentValues.put(PRODUCT_TYPE, productType);
        contentValues.put(PRICE, price);
        contentValues.put(QUANTITY, quantity);
        contentValues.put(SESSION_ID, sessionId);
    }

    public String getBarcode() {
        return barcode;
    }

    public String getBarcodeType() {
        return barcodeType;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductType() {
        return productType;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public long getSessionId() {
        return sessionId;
    }

    public ContentValues getContentValues() {
        return contentValues;
    }
}
