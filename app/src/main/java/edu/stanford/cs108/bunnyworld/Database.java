package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class Database extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "BunnyDB.db";
    private static Database instance;
    private int numPages;

    public static synchronized Database getInstance(Context context) {
        return instance;
    }

    private Database(Context context, String name) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        String databaseOrganizer = "CREATE TABLE SaveTable " + "(" +
                "name TEXT PRIMARY KEY)";
        String createPageOne = "CREATE TABLE Page1 " + "(" +
                "imgName TEXT PRIMARY KEY, " +
                "save TEXT, " +
                "text TEXT, " +
                "hidden INTEGER, " +
                "moveable INTEGER, " +
                "x REAL, " +
                "y REAL, " +
                "width REAL, " +
                "height REAL)";
        String createInventory = "CREATE TABLE " + "InventoryTable" + "(" +
                "imgName TEXT PRIMARY KEY, " +
                "save TEXT, " +
                "text TEXT, " +
                "hidden INTEGER, " +
                "moveable INTEGER, " +
                "x REAL, " +
                "y REAL, " +
                "width REAL, " +
                "height REAL)";
        db.execSQL(createPageOne);
        db.execSQL(createInventory);
        db.execSQL(databaseOrganizer);
        numPages = 1;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createNewPage() {
        SQLiteDatabase db = this.getWritableDatabase();
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

    private void addNewObject(Shape shape, String page, String save) {
        SQLiteDatabase db = this.getWritableDatabase();
        String shapeName = shape.imgName;
        String shapeText = shape.text;
        int shapeHidden = shape.hidden ? 1 : 0;
        int shapeMoveable = shape.moveable ? 1 : 0;
        double shapeX = shape.getX();
        double shapeY = shape.getY();
        double shapeWidth = shape.getWidth();
        double shapeHeight = shape.getHeight();
        String insertStr = "INSERT INTO " + page + " VALUES "
                + "('" + shapeName + "'," +
                save + "'," +
                shapeText + "'," +
                shapeHidden + "," +
                shapeMoveable + "," +
                shapeX + "," +
                shapeY + "," +
                shapeWidth + "," +
                shapeHeight + ")";
        db.execSQL(insertStr);
    }

    public void saveGame(String saveName, HashMap<String, ArrayList<Shape>> shapeMap) {
        SQLiteDatabase db = this.getWritableDatabase();
        String newSave = "INSERT INTO SaveTable VALUES " + "('" +
                saveName + "')";
        db.execSQL(newSave);
        for (Map.Entry<String, ArrayList<Shape>> entry : shapeMap.entrySet()) {
            String page = entry.getKey();
            ArrayList<Shape> allShapes = entry.getValue();
            for (int i = 0; i < allShapes.size(); i++) {
                addNewObject(allShapes.get(i), page, saveName);
            }
        }
    }

}
