package com.example.notix.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.notix.beans.UserRemember;

import java.util.ArrayList;
import java.util.List;

/**
 * Wraps the logic for a SQLite database
 */
public class DataManager extends SQLiteOpenHelper {

    // Database Information
    private static final String DB_NAME = "notix.db";
    // Database version
    private static final int DB_VERSION = 1;
    // Table Name
    public static final String TABLE_NAME = "User";
    // Table columns
    private static final String DNI = "dni";
    private static final String PASS = "pass";

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" +
            DNI + " Text PRIMARY KEY, " +
            PASS + " TEXT NOT NULL" +
            ");";

    private final Context context;

    public DataManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int oldVersion, int newVersion) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sQLiteDatabase);
    }

//------------------------------ selectUser ------------------------------//

    public UserRemember selectUser() {
        UserRemember ret = new UserRemember();
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery(query, null);
        UserRemember userRemember;
        while (cursor.moveToNext()) {

            ret.setDni(cursor.getString(0));
            ret.setPass(cursor.getString(1));

        }
        cursor.close();
        sQLiteDatabase.close();
        return ret;
    }

//------------------------------ selectAll ------------------------------//

    public List<UserRemember> selectAllUsers() {
        List<UserRemember> ret = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery(query, null);
        UserRemember userRemember;
        while (cursor.moveToNext()) {
            userRemember = new UserRemember();
            userRemember.setDni(cursor.getString(0));
            userRemember.setPass(cursor.getString(1));
            ret.add(userRemember);
        }
        cursor.close();
        sQLiteDatabase.close();
        return ret;
    }

//------------------------------ Insert ------------------------------//

    public void insert(UserRemember userRemember) {
        ContentValues values = new ContentValues();
        values.put(DNI, userRemember.getDni());
        values.put(PASS, userRemember.getPass());

        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        sQLiteDatabase.insert(TABLE_NAME, null, values);
        sQLiteDatabase.close();
    }

//------------------------------ Delete ------------------------------//

    public int delete(String userRem) {
        int ret;
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        UserRemember user = new UserRemember();
        user.setDni(userRem);
        ret = sQLiteDatabase.delete(TABLE_NAME, DNI + "=?",
                new String[]{
                        String.valueOf(user.getDni())
                });
        sQLiteDatabase.close();
        return ret;
    }

//------------------------------ ifExist ------------------------------//

    public boolean ifTableExists() {
        boolean ret = false;
        Cursor cursor = null;
        try {
            SQLiteDatabase sQLiteDatabase = this.getReadableDatabase();
            String query = "select DISTINCT tbl_name from sqlite_master where tbl_name = '" +
                    TABLE_NAME + "'";
            cursor = sQLiteDatabase.rawQuery(query, null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    ret = true;
                }
            }
        } catch (Exception e) {
            // Nothing to do here...
        } finally {
            try {
                assert cursor != null;
                cursor.close();
            } catch (NullPointerException e) {
                // Nothing to do here...
            }
        }
        return ret;
    }

//------------------------------ ifExist ------------------------------//

    public boolean isEmpty() {
        boolean ret = true;
        Cursor cursor = null;
        try {
            SQLiteDatabase sQLiteDatabase = this.getReadableDatabase();
            cursor = sQLiteDatabase.rawQuery("SELECT count(*) FROM (select 0 from " +
                    TABLE_NAME + " limit 1)", null);
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            if (count > 0) {
                ret = false;
            }
        } catch (Exception e) {
            // Nothing to do here...
        } finally {
            try {
                assert cursor != null;
                cursor.close();
            } catch (Exception e) {
                // Nothing to do here...
            }
        }
        return ret;
    }
}