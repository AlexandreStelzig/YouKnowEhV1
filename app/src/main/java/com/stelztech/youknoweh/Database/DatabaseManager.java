package com.stelztech.youknoweh.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Alexandre on 4/25/2016.
 */
public class DatabaseManager {

    private static DatabaseManager databaseManager = null;
    private Database database;


    public static DatabaseManager getInstance(Context context) {
        if (databaseManager == null) {
            databaseManager = new DatabaseManager(context);
        }
        return databaseManager;
    }


    DatabaseManager(Context context) {
        database = new Database(context);
    }


    public List<Package> getPackages() {
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseVariables.TablePackage.TABLE_NAME + " ORDER BY " + DatabaseVariables.TablePackage.COLUMN_NAME_PACKAGE_NAME + " COLLATE NOCASE ", null);
        List<Package> packageList = new ArrayList<Package>();

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                String id = cursor.getString(cursor
                        .getColumnIndex(DatabaseVariables.TablePackage.COLUMN_NAME_ID_PACKAGE));
                String name = cursor.getString(cursor
                        .getColumnIndex(DatabaseVariables.TablePackage.COLUMN_NAME_PACKAGE_NAME));
                packageList.add(new Package(id, name));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return packageList;
    }

    public long insertIntoTable_Package(String name) {
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseVariables.TablePackage.COLUMN_NAME_PACKAGE_NAME, name);

        long newRowId;
        newRowId = db.insert(
                DatabaseVariables.TablePackage.TABLE_NAME,
                null,
                values);
        return newRowId;
    }

    public boolean deleteFromTable_Package(String id) {
        SQLiteDatabase db = database.getReadableDatabase();
        deleteFromTable_WordPackage_package(id);
        return db.delete(DatabaseVariables.TablePackage.TABLE_NAME, DatabaseVariables.TablePackage.COLUMN_NAME_ID_PACKAGE + "=" + id, null) > 0;
    }

    public void updateFromTable_Package(String idPackage, String name) {
        SQLiteDatabase db = database.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseVariables.TablePackage.COLUMN_NAME_PACKAGE_NAME, name);
        db.update(DatabaseVariables.TablePackage.TABLE_NAME, values, DatabaseVariables.TablePackage.COLUMN_NAME_ID_PACKAGE + "=" + idPackage, null);

    }

    public List<Word> getWords() {
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseVariables.TableWord.TABLE_NAME
                + " ORDER BY " + DatabaseVariables.TableWord.COLUMN_NAME_QUESTION + " COLLATE NOCASE ", null);
        List<Word> wordList = new ArrayList<Word>();

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                String id = cursor.getString(cursor
                        .getColumnIndex(DatabaseVariables.TableWord.COLUMN_NAME_ID_WORD));
                String question = cursor.getString(cursor
                        .getColumnIndex(DatabaseVariables.TableWord.COLUMN_NAME_QUESTION));
                String answer = cursor.getString(cursor
                        .getColumnIndex(DatabaseVariables.TableWord.COLUMN_NAME_ANSWER));
                String moreInfo = cursor.getString(cursor
                        .getColumnIndex(DatabaseVariables.TableWord.COLUMN_NAME_MORE_INFORMATION));

                wordList.add(new Word(id, question, answer, moreInfo));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return wordList;
    }

    public long insertIntoTable_Word(String question, String answer, String moreInfo) {
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseVariables.TableWord.COLUMN_NAME_QUESTION, question);
        values.put(DatabaseVariables.TableWord.COLUMN_NAME_ANSWER, answer);
        values.put(DatabaseVariables.TableWord.COLUMN_NAME_MORE_INFORMATION, moreInfo);


        long newRowId;
        newRowId = db.insert(
                DatabaseVariables.TableWord.TABLE_NAME,
                null,
                values);
        return newRowId;
    }

    public boolean deleteFromTable_Word(String id) {
        SQLiteDatabase db = database.getReadableDatabase();
        deleteFromTable_WordPackage_word(id);
        return db.delete(DatabaseVariables.TableWord.TABLE_NAME, DatabaseVariables.TableWord.COLUMN_NAME_ID_WORD + "=" + id, null) > 0;
    }

    public Word getWordFromId(String idWord) {
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseVariables.TableWord.TABLE_NAME + " WHERE " + DatabaseVariables.TableWord.COLUMN_NAME_ID_WORD + "=" + idWord, null);
        Word word = null;

        if (cursor.moveToFirst()) {

            String id = cursor.getString(cursor
                    .getColumnIndex(DatabaseVariables.TableWord.COLUMN_NAME_ID_WORD));
            String question = cursor.getString(cursor
                    .getColumnIndex(DatabaseVariables.TableWord.COLUMN_NAME_QUESTION));
            String answer = cursor.getString(cursor
                    .getColumnIndex(DatabaseVariables.TableWord.COLUMN_NAME_ANSWER));
            String moreInfo = cursor.getString(cursor
                    .getColumnIndex(DatabaseVariables.TableWord.COLUMN_NAME_MORE_INFORMATION));

            word = (new Word(id, question, answer, moreInfo));
            cursor.moveToNext();

        }
        cursor.close();
        return word;
    }

    public Package getPackageFromId(String idPackage) {
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseVariables.TablePackage.TABLE_NAME + " WHERE "
                + DatabaseVariables.TablePackage.COLUMN_NAME_ID_PACKAGE + "=" + idPackage, null);
        Package pack = null;

        if (cursor.moveToFirst()) {

            String id = cursor.getString(cursor
                    .getColumnIndex(DatabaseVariables.TablePackage.COLUMN_NAME_ID_PACKAGE));
            String name = cursor.getString(cursor
                    .getColumnIndex(DatabaseVariables.TablePackage.COLUMN_NAME_PACKAGE_NAME));


            pack = (new Package(id, name));
            cursor.moveToNext();

        }
        cursor.close();
        return pack;
    }

    public void updateFromTable_Word(String idWord, String question, String answer, String moreInfo) {
        SQLiteDatabase db = database.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseVariables.TableWord.COLUMN_NAME_QUESTION, question);
        values.put(DatabaseVariables.TableWord.COLUMN_NAME_ANSWER, answer);
        values.put(DatabaseVariables.TableWord.COLUMN_NAME_MORE_INFORMATION, moreInfo);
        db.update(DatabaseVariables.TableWord.TABLE_NAME, values, DatabaseVariables.TableWord.COLUMN_NAME_ID_WORD + "=" + idWord, null);

    }

    public int getNumberOfPackagesForAWord(String idWord) {
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor mCount = db.rawQuery("SELECT count(*) FROM " + DatabaseVariables.TableWordPackage.TABLE_NAME + " WHERE " + DatabaseVariables.TableWord.COLUMN_NAME_ID_WORD + "=" + idWord, null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        mCount.close();

        return count;
    }

    public List<Word> getWordsFromPackage(String idPackage){
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseVariables.TableWordPackage.TABLE_NAME + " WHERE "
                + DatabaseVariables.TableWordPackage.COLUMN_NAME_ID_PACKAGE + "=" + idPackage, null);
        List<Word> wordList = new ArrayList<Word>();


        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                String id = cursor.getString(cursor
                        .getColumnIndex(DatabaseVariables.TableWordPackage.COLUMN_NAME_ID_WORD));
                wordList.add((getWordFromId(id)));
                cursor.moveToNext();
            }
        }

        Collections.sort(wordList, new Comparator<Word>() {
            @Override
            public int compare(Word lhs, Word rhs) {
                return lhs.getQuestion().compareToIgnoreCase(rhs.getQuestion());
            }
        });

        cursor.close();
        return  wordList;
    }

    public long insertIntoTable_WordPackage(String idWord, String idPackage) {
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseVariables.TableWordPackage.COLUMN_NAME_ID_WORD, idWord);
        values.put(DatabaseVariables.TableWordPackage.COLUMN_NAME_ID_PACKAGE, idPackage);


        long newRowId;
        newRowId = db.insert(
                DatabaseVariables.TableWordPackage.TABLE_NAME,
                null,
                values);
        return newRowId;
    }

    public List<WordPackage> getWordPackages() {
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseVariables.TableWordPackage.TABLE_NAME, null);
        List<WordPackage> list = new ArrayList<WordPackage>();

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                String idWord = cursor.getString(cursor
                        .getColumnIndex(DatabaseVariables.TableWordPackage.COLUMN_NAME_ID_WORD));
                String idPackage = cursor.getString(cursor
                        .getColumnIndex(DatabaseVariables.TableWordPackage.COLUMN_NAME_ID_PACKAGE));


                list.add(new WordPackage(idWord, idPackage));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return list;
    }

    public boolean deleteFromTable_WordPackage(String idWord, String idPackage) {
        SQLiteDatabase db = database.getReadableDatabase();
        return db.delete(DatabaseVariables.TableWordPackage.TABLE_NAME, DatabaseVariables.TableWordPackage.COLUMN_NAME_ID_WORD + "=" + idWord +" AND "
                + DatabaseVariables.TableWordPackage.COLUMN_NAME_ID_PACKAGE + "=" + idPackage , null) > 0;
    }
    public boolean deleteFromTable_WordPackage_word(String idWord) {
        SQLiteDatabase db = database.getReadableDatabase();
        return db.delete(DatabaseVariables.TableWordPackage.TABLE_NAME, DatabaseVariables.TableWordPackage.COLUMN_NAME_ID_WORD + "=" + idWord , null) > 0;
    }
    public boolean deleteFromTable_WordPackage_package(String idPackage) {
        SQLiteDatabase db = database.getReadableDatabase();
        return db.delete(DatabaseVariables.TableWordPackage.TABLE_NAME, DatabaseVariables.TableWordPackage.COLUMN_NAME_ID_PACKAGE + "=" + idPackage , null) > 0;
    }

    public List<Package> getPackagesFromWords(String idWord){
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseVariables.TableWordPackage.TABLE_NAME + " WHERE "
                + DatabaseVariables.TableWordPackage.COLUMN_NAME_ID_WORD + "=" + idWord, null);
        List<Package> packageList = new ArrayList<Package>();


        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                String id = cursor.getString(cursor
                        .getColumnIndex(DatabaseVariables.TableWordPackage.COLUMN_NAME_ID_PACKAGE));
                packageList.add((getPackageFromId(id)));
                cursor.moveToNext();
            }
        }

        Collections.sort(packageList, new Comparator<Package>() {
            @Override
            public int compare(Package lhs, Package rhs) {
                return (lhs.getPackageName()).compareToIgnoreCase(rhs.getPackageName());
            }
        });

        cursor.close();
        return  packageList;
    }

}
