package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DB";
    private static Database instance;
    public static synchronized Database getInstance(Context context) {
        if (instance == null) {
            instance = new Database(context.getApplicationContext());
        }
        return instance;
    }
    private Database(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    private int numPages;
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createPageOne = "CREATE TABLE " + "Page1" + "(" +
                "imgName TEXT PRIMARY KEY, " +
                "text TEXT, " +
                "hidden INTEGER, " +
                "moveable INTEGER, " +
                "x REAL, " +
                "y REAL, " +
                "width REAL, " +
                "height REAL)";
        String createInventory = "CREATE TABLE " + "Inventory" + "(" +
                "imgName TEXT PRIMARY KEY, " +
                "text TEXT, " +
                "hidden INTEGER, " +
                "moveable INTEGER, " +
                "x REAL, " +
                "y REAL, " +
                "width REAL, " +
                "height REAL)";
        db.execSQL(createPageOne);
        db.execSQL(createInventory);
        numPages = 1;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void createNewPage(SQLiteDatabase db) {
        String tableName = "Page" + Integer.toString(numPages);
        String createNewPage = "CREATE TABLE " + "PageOne" + "(" +
                "imgName TEXT PRIMARY KEY, " +
                "text TEXT, " +
                "hidden INTEGER, " +
                "moveable INTEGER, " +
                "x REAL, " +
                "y REAL, " +
                "width REAL, " +
                "height REAL)";
        db.execSQL(createNewPage);
        numPages = numPages + 1;
    }

    public void addNewObject(SQLiteDatabase db, Shape shape, Integer page) {

    }
}
