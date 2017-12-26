package com.example.salun.androidlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class ChatDatabaseHelper extends SQLiteOpenHelper {

    private final static String ACTIVITY_NAME = "ChatDatabaseHelper";
    public final static String DATABASE_NAME = "Messages.db";
    public final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "Messages";
    public final static String KEY_ID = "_id";
    public final static String KEY_MESSAGE = "message";

    public ChatDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE " + TABLE_NAME
                        + " ("
                        + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + KEY_MESSAGE + " text" +
                        ");"
        );
        Log.i(ACTIVITY_NAME, "Calling onCreate()");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        Log.i(ACTIVITY_NAME, "Calling onUpgrade - Going from old version " + oldVersion + " to new version " + newVersion);
        onCreate(db);

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        Log.i(ACTIVITY_NAME, "Calling onDowngrade - Going from old version " + oldVersion + " to new version " + newVersion);
        onCreate(db);

    }
}
