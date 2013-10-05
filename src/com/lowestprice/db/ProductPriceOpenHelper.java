package com.lowestprice.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.lowestprice.ProductPrice;

import java.util.ArrayList;
import java.util.List;

public class ProductPriceOpenHelper extends SQLiteOpenHelper implements LowestPriceOpenHelper {

    private static final String TABLE_NAME = "PRODUCT_PRICE";
    private static final String DB_NAME = DATABASE_NAME + "_" + TABLE_NAME;
    private static final String CREATE_TABLE =
        "CREATE TABLE " + TABLE_NAME + " (" +
            ProductPrice.BARCODE + "                TEXT, " +       //0
            ProductPrice.BARCODE_TYPE + "           TEXT, " +       //1
            ProductPrice.PRODUCT_NAME + "           TEXT, " +       //2
            ProductPrice.PRODUCT_TYPE + "           TEXT, " +       //3
            ProductPrice.PRICE + "                  REAL, " +       //4
            ProductPrice.QUANTITY + "               INTEGER, " +    //5
            ProductPrice.SESSION_ID + "             INTEGER " +     //6
            ");";
    private static final String DROP_TABLE =
        "DROP TABLE IF EXISTS " + TABLE_NAME + ");";
    private static final String COUNT_ALL =
        "SELECT count(*) FROM " + TABLE_NAME + ");";

    public ProductPriceOpenHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public long save(SQLiteDatabase db, ProductPrice product) {
        return db.insert(TABLE_NAME, null, product.getContentValues());
    }

    public List<ProductPrice> getAll(SQLiteDatabase db) {
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        int count = cursor.getCount();
        List<ProductPrice> products = null;
        if (cursor.moveToFirst()) {
            products = new ArrayList<ProductPrice>(count);
            do {
                products.add(getProductPrice(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return products;
    }

    public void deleteAll(SQLiteDatabase db) {
        db.delete(TABLE_NAME, null, null);
    }

    private ProductPrice getProductPrice(Cursor cursor) {
        return new ProductPrice(cursor.getString(0), cursor.getString(1),
            cursor.getString(2), cursor.getString(3), cursor.getDouble(4),
            cursor.getInt(5), cursor.getInt(6));
    }
}
