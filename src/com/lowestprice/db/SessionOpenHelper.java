package com.lowestprice.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import com.lowestprice.Session;
import com.lowestprice.api.Place;

import java.util.ArrayList;
import java.util.List;

public class SessionOpenHelper extends SQLiteOpenHelper implements LowestPriceOpenHelper {

    private static final String TABLE_NAME = "SESSION";
    private static final String DB_NAME = DATABASE_NAME + "_" + TABLE_NAME;
    private static final String CREATE_TABLE =
        "CREATE TABLE " + TABLE_NAME + " (" +
            Session.DATE + "                    INTEGER, " +                //0
            Session.USER_NAME + "               TEXT, " +                   //1
            Session.LOCATION_LONGITUDE + "      REAL, " +                   //2
            Session.LOCATION_LATITUDE + "       REAL, " +                   //3
            Session.LOCATION_ACCURACY + "       REAL, " +                   //4
            Session.LOCATION_PROVIDER + "       REAL, " +                   //5
            Session.PLACE_NAME + "              TEXT, " +                   //6
            Session.PLACE_ADDRESS + "           TEXT, " +                   //7
            Session.PLACE_PHONE + "             TEXT, " +                   //8
            Session.PLACE_ID + "                TEXT, " +                   //9
            Session.PLACE_REFERENCE + "         TEXT " +                    //10
            ");";
    private static final String DROP_TABLE =
        "DROP TABLE IF EXISTS " + TABLE_NAME + ");";
    private static final String COUNT_ALL =
        "SELECT count(*) FROM " + TABLE_NAME + ");";
    private static final String ORDER_BY = Session.DATE + " desc ";
    private static final String LIMIT = "1";

    public SessionOpenHelper(Context context) {
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

    public boolean hasUnsavedSession(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery(COUNT_ALL, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    public void save(SQLiteDatabase db, Session session) {
        db.insert(TABLE_NAME, null, session.getContentValues());
    }

    public Session getLast(SQLiteDatabase db) {
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, ORDER_BY, LIMIT);
        Session session = null;
        if (cursor.moveToFirst()) session = getSession(cursor);
        cursor.close();
        return session;
    }

    public List<Session> getAll(SQLiteDatabase db) {
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        int count = cursor.getCount();
        List<Session> sessions = null;
        if (cursor.moveToFirst()) {
            sessions = new ArrayList<Session>(count);
            do {
                sessions.add(getSession(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return sessions;
    }

    public void deleteAll(SQLiteDatabase db) {
        db.delete(TABLE_NAME, null, null);
    }

    private Session getSession(Cursor cursor) {
        Location location = new Location(cursor.getString(5));
        location.setLongitude(cursor.getDouble(2));
        location.setLatitude(cursor.getDouble(3));
        location.setAccuracy(cursor.getFloat(4));
        Place place = new Place(cursor.getString(6), cursor.getString(7),
            cursor.getString(8), cursor.getString(9), cursor.getString(10));
        return new Session(cursor.getLong(0), cursor.getString(1),
            location, place);
    }
}