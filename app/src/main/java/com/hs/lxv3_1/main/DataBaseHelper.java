package com.hs.lxv3_1.main;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hs.lxv3_1.utils.Constant;

/**
 * Created by Holy-Spirit on 2016/2/26.
 */
public class DataBaseHelper extends SQLiteOpenHelper {


    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    public DataBaseHelper(Context context, String name) {
        this(context, name, Constant.VERSION);

    }

    public DataBaseHelper(Context context, String name, int version) {
        this(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        System.out.println("-->>create a database");
        db.execSQL("create table if not exists " + Constant.POS_TAB + "(" +
                Constant.POS_ID + "  INTEGER PRIMARY KEY AUTOINCREMENT," +
                Constant.POS_ADDR + " varchar(40)," +
                Constant.POS_TIME + " datatime," +
                Constant.POS_LAT + " double," +
                Constant.POS_LNG + " double )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("-->>update a database");
    }


    public static boolean deleteDb(Context context) {
        return context.deleteDatabase(Constant.DB_NAME);
    }


    /**
     * 插入数据到数据库
     *
     * @param addrStr
     * @param timeStr
     * @param lat
     * @param lng
     */

    public static void insert(Context context, String addrStr, String timeStr, Double lat,
                              Double lng) {

        DataBaseHelper helper = new DataBaseHelper(context, Constant.DB_NAME);
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(Constant.POS_ADDR, addrStr);
        value.put(Constant.POS_TIME, timeStr);
        value.put(Constant.POS_LAT, lat);
        value.put(Constant.POS_LNG, lng);
        database.insert(Constant.POS_TAB, null, value);
    }


    /**
     * 从数据库查询数据
     *
     * @return
     */

    public static LocBean queryLast(Context context) {

        LocBean result = new LocBean();
        DataBaseHelper helper = new DataBaseHelper(context, Constant.DB_NAME);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.query(Constant.POS_TAB,
                new String[]{Constant.POS_ID, Constant.POS_ADDR, Constant.POS_TIME,
                        Constant.POS_LAT, Constant.POS_LNG}, null, null, null, null, null);

        if (cursor.moveToLast()) {

            String addrStr = cursor.getString(cursor.getColumnIndex(Constant.POS_ADDR));
            result.setAddr(addrStr);
            String timeStr = cursor.getString(cursor.getColumnIndex(Constant.POS_TIME));
            result.setTime(timeStr);
            int id = cursor.getInt(cursor.getColumnIndex(Constant.POS_ID));
            result.setId(id);
            double lat = cursor.getDouble(cursor.getColumnIndex(Constant.POS_LAT));
            result.setLat(lat);
            double lng = cursor.getDouble(cursor.getColumnIndex(Constant.POS_LNG));
            result.setLng(lng);
        }

        return result;
    }
}
