package com.example.ajay.library_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajay-5674 on 03/07/17.
 */

public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "libraryInfo1";

    //private static final String TABLE_LIBRARY = "library";
    private static final String TABLE_LIBRARY = "library";

    private static final String KEY_ID = "_id";
    private static final String KEY_NAME = "Name";
    private static final String KEY_AUTH = "Author";
    private static final String KEY_PUB = "Publisher";
    private static final String KEY_NOS = "nos";
    private static final String KEY_TOT = "total";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_LIBRARY + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
                + KEY_AUTH + " TEXT," + KEY_PUB + " TEXT," + KEY_NOS + " INTEGER," + KEY_TOT + " INTEGER " + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP_TABLE_IF_EXISTS " + TABLE_LIBRARY);
// Creating tables again
        onCreate(db);
    }

    public void makeChanges() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "ALTER TABLE " + TABLE_LIBRARY + " ADD COLUMN " + KEY_TOT + " INTEGER";
        db.execSQL(query);
        query = "UPDATE " + TABLE_LIBRARY + " SET " + KEY_TOT + " = " + KEY_NOS;
        db.execSQL(query);
    }


    //Adding a new book
    public void addBook(libraryBook book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(KEY_ID, book.getId());
        values.put(KEY_NAME, book.getName());
        values.put(KEY_AUTH, book.getAuthor());
        values.put(KEY_PUB, book.getPublisher());
        values.put(KEY_NOS, book.getNos());
        values.put(KEY_TOT, book.getNos());

        db.insert(TABLE_LIBRARY, null, values);
        db.close();
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

    public String getColTot() {
        return KEY_TOT;
    }


    public libraryBook getBook(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LIBRARY, new String[]{KEY_ID, KEY_NAME, KEY_AUTH, KEY_PUB, KEY_NOS, KEY_TOT}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        libraryBook book = new libraryBook(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                cursor.getString(2), cursor.getString(3), Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)));
        return book;
    }

    public libraryBook getBookByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LIBRARY, new String[]{KEY_ID, KEY_NAME, KEY_AUTH, KEY_PUB, KEY_NOS}, KEY_NAME + "=?",
                new String[]{name}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        libraryBook book = new libraryBook(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                cursor.getString(2), cursor.getString(3), Integer.parseInt(cursor.getString(4)));
        return book;
    }

    public List<libraryBook> getAllBooks() {
        List<libraryBook> bookList = new ArrayList<libraryBook>();
        String selectQuery = "SELECT * FROM " + TABLE_LIBRARY + " ORDER BY " + KEY_NAME + " ASC";
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
                book.setTotal(book.getNos());
// Adding contact to list
                bookList.add(book);
            } while (cursor.moveToNext());
        }
        return bookList;
    }

    public Cursor getCursor() {
        List<libraryBook> bookList = new ArrayList<libraryBook>();
        String selectQuery = "SELECT * FROM " + TABLE_LIBRARY + " ORDER BY " + KEY_NAME + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public int getBookCount() {
        String countQuery = "SELECT * FROM " + TABLE_LIBRARY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        //cursor.close();
// return count
        return cursor.getCount();
    }

    public int updateBook(libraryBook book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, book.getId());
        values.put(KEY_NAME, book.getName());
        values.put(KEY_AUTH, book.getAuthor());
        values.put(KEY_PUB, book.getPublisher());
        values.put(KEY_NOS, book.getNos());
        values.put(KEY_TOT, book.getTotal());
// updating row
        return db.update(TABLE_LIBRARY, values, KEY_ID + " = ?", new String[]{String.valueOf(book.getId())});
    }

    public void deleteBook(libraryBook book) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LIBRARY, KEY_ID + " = ?", new String[]{String.valueOf(book.getId())});
        db.close();
    }
}

