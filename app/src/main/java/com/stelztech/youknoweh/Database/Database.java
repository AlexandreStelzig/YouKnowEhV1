package com.stelztech.youknoweh.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Alexandre on 4/25/2016.
 */
public class Database extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "YouKnowEh.db";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseVariables.SQL_CREATE_TABLE_WORD);
        db.execSQL(DatabaseVariables.SQL_CREATE_TABLE_PACKAGE);
        db.execSQL(DatabaseVariables.SQL_CREATE_TABLE_WORD_PACKAGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseVariables.SQL_DELETE_TABLE_PACKAGE);
        db.execSQL(DatabaseVariables.SQL_DELETE_TABLE_WORD);
        db.execSQL(DatabaseVariables.SQL_DELETE_TABLE_WORD_PACKAGE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
