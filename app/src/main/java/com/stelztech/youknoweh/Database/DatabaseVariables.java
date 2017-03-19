package com.stelztech.youknoweh.Database;

import android.provider.BaseColumns;

/**
 * Created by Alexandre on 4/20/2016.
 */
public final class DatabaseVariables {


    public static final String SQL_CREATE_TABLE_WORD = "CREATE TABLE "
            + TableWord.TABLE_NAME + " ("
            + TableWord.COLUMN_NAME_ID_WORD + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TableWord.COLUMN_NAME_QUESTION + " TEXT NOT NULL,"
            + TableWord.COLUMN_NAME_ANSWER + " TEXT NOT NULL,"
            + TableWord.COLUMN_NAME_MORE_INFORMATION + " TEXT" + " );";

    public static final String SQL_CREATE_TABLE_PACKAGE = "CREATE TABLE "
            + TablePackage.TABLE_NAME + " ("
            + TablePackage.COLUMN_NAME_ID_PACKAGE + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TablePackage.COLUMN_NAME_PACKAGE_NAME + " TEXT NOT NULL" + " );";

    public static final String SQL_CREATE_TABLE_WORD_PACKAGE = "CREATE TABLE "
            + TableWordPackage.TABLE_NAME + " ("
            + TableWordPackage.COLUMN_NAME_ID_WORD + " INTEGER,"
            + TableWordPackage.COLUMN_NAME_ID_PACKAGE + " TEXT,"
            + " FOREIGN KEY " + "(" + TableWordPackage.COLUMN_NAME_ID_WORD + ")"
            + " REFERENCES " + TableWord.TABLE_NAME + "(" + TableWord.COLUMN_NAME_ID_WORD + "),"
            + " FOREIGN KEY " + "(" + TableWordPackage.COLUMN_NAME_ID_PACKAGE + ")"
            + " REFERENCES " + TablePackage.TABLE_NAME + "(" + TablePackage.COLUMN_NAME_ID_PACKAGE + ")"
            + " PRIMARY KEY (" + TableWordPackage.COLUMN_NAME_ID_WORD + ", " + TableWordPackage.COLUMN_NAME_ID_PACKAGE + ") );";

    public static final String SQL_DELETE_TABLE_WORD = "DROP TABLE IF EXISTS " + TableWord.TABLE_NAME;
    public static final String SQL_DELETE_TABLE_PACKAGE = "DROP TABLE IF EXISTS " + TablePackage.TABLE_NAME;
    public static final String SQL_DELETE_TABLE_WORD_PACKAGE = "DROP TABLE IF EXISTS " + TableWordPackage.TABLE_NAME;

    public static abstract class TableWord implements BaseColumns {
        public static final String TABLE_NAME = "word";
        public static final String COLUMN_NAME_ID_WORD = "idword";
        public static final String COLUMN_NAME_QUESTION = "question";
        public static final String COLUMN_NAME_ANSWER = "answer";
        public static final String COLUMN_NAME_MORE_INFORMATION = "moreinfo";
    }

    public static abstract class TablePackage implements BaseColumns {
        public static final String TABLE_NAME = "package";
        public static final String COLUMN_NAME_ID_PACKAGE = "idpackage";
        public static final String COLUMN_NAME_PACKAGE_NAME = "packagename";

    }

    public static abstract class TableWordPackage implements BaseColumns {
        public static final String TABLE_NAME = "wordpackage";
        public static final String COLUMN_NAME_ID_WORD = "idword";
        public static final String COLUMN_NAME_ID_PACKAGE = "idpackage";

    }


}
