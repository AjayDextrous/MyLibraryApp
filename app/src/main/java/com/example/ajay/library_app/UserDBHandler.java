package com.example.ajay.library_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajay-5674 on 03/07/17.
 */

public class UserDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UserInfo1";
    private static final String TABLE_USERS[] = {"User1", "User2", "User3", "User4"};

    private static final String KEY_ID = "_id";
    private static final String KEY_NAME = "Name";
    private static final String KEY_AUTH = "Author";
    private static final String KEY_PUB = "Publisher";
    private static final String KEY_NOS = "nos";

    public UserDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("UserDB :", " Created");

        for (String user : TABLE_USERS) {
            String CREATE_USER_TABLE = "CREATE TABLE " + user + "("
                    + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                    + KEY_AUTH + " TEXT," + KEY_PUB + " TEXT," + KEY_NOS + " INTEGER" + ")";
            db.execSQL(CREATE_USER_TABLE);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        for (String user : TABLE_USERS) {
            db.execSQL("DROP_TABLE_IF_EXISTS " + user);
// Creating tables again
            onCreate(db);
        }

    }


    //Adding a new book
    public void addBook(libraryBook book, String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, book.getId());
        values.put(KEY_NAME, book.getName());
        values.put(KEY_AUTH, book.getAuthor());
        values.put(KEY_PUB, book.getPublisher());
        values.put(KEY_NOS, book.getNos());

        db.insert(user, null, values);
        db.close();
        Log.d("UserDB :", " Book Added to " + user);
    }

    public String getColID() {
        return KEY_ID;
    }

    public String getColName() {
        return KEY_NAME;
    }

    public String getColAuth() {
        return KEY_AUTH;
    }

    public String getColNos() {
        return KEY_NOS;
    }

    public libraryBook getBook(int id, String user) {
        Log.d("UserDB :", " Getting Book");
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("UserDB :", " DataBase Loaded");
        Cursor cursor = db.query(user, new String[]{KEY_ID, KEY_NAME, KEY_AUTH, KEY_PUB, KEY_NOS}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        Log.d("UserDB :", " Cursor Queried");
        if (cursor.getCount() != 0) {
            Log.d("UserDB :", " cursor is not null");
            cursor.moveToFirst();
            libraryBook book = new libraryBook(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), Integer.parseInt(cursor.getString(4)));
            Log.d("UserDB :", " Fetched book " + book.getName() + " from " + user);
            return book;
        } else {
            Log.d("UserDB :", " Cursor is null");
            return null;

        }


    }

    public libraryBook getBookByName(String name, String user) {
        Log.d("UserDB-" + user + " : ", " Getting Book");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(user, new String[]{KEY_ID, KEY_NAME, KEY_AUTH, KEY_PUB, KEY_NOS}, KEY_NAME + "=?",
                new String[]{name}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        libraryBook book = new libraryBook(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                cursor.getString(2), cursor.getString(3), Integer.parseInt(cursor.getString(4)));
        return book;
    }

    public List<libraryBook> getAllBooks(String user) {
        List<libraryBook> bookList = new ArrayList<libraryBook>();
        String selectQuery = "SELECT * FROM " + user + " ORDER BY " + KEY_NAME + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                libraryBook book = new libraryBook();
                book.setId(Integer.parseInt(cursor.getString(0)));
                book.setName(cursor.getString(1));
                book.setAuthor(cursor.getString(2));
                book.setPublisher(cursor.getString(3));
                book.setNos(Integer.parseInt(cursor.getString(4)));
                bookList.add(book);
            } while (cursor.moveToNext());
        }
        Log.d("UserDB :", " All books from " + user + " Fetched");
        return bookList;
    }

    public Cursor getCursor(String user) {
        String selectQuery = "SELECT * FROM " + user + " ORDER BY " + KEY_NAME + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("UserDB :", " Cursor Fetched from " + user);
        return cursor;

    }

    public int getBookCount(String user) {
        String countQuery = "SELECT * FROM " + user;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        Log.d("UserDB :", " Book Count of " + user + " : " + cursor.getCount());
        return cursor.getCount();
    }

    public int updateBook(libraryBook book, String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        libraryBook userBook = getBook(book.getId(), user);
        if (userBook == null) {
            addBook(book, user);
            Log.d("UserDB :", " Add-Updating " + book.getName() + " count of " + user);
            return 0;
        } else {
            values.put(KEY_ID, book.getId());
            values.put(KEY_NAME, book.getName());
            values.put(KEY_AUTH, book.getAuthor());
            values.put(KEY_PUB, book.getPublisher());
            values.put(KEY_NOS, book.getNos());
            Log.d("UserDB :", " Updating " + book.getName() + " count of " + user);
            return db.update(user, values, KEY_ID + " = ?", new String[]{String.valueOf(book.getId())});

        }

    }

    public void deleteBook(int id, String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(user, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}

