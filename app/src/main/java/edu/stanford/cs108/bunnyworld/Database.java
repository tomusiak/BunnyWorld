package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Database extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "BunnyDB.db";
    private static Database instance;
    private static ArrayList<String> pageList;

    public static synchronized Database getInstance(Context context) {
        if (instance == null) {
            instance = new Database(context.getApplicationContext());
        }
        return instance;
    }

    private Database(Context context) {
        super(context, DATABASE_NAME, null, 1);
        pageList = new ArrayList<String>();
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createNewPage(String page) {
        SQLiteDatabase db = this.getWritableDatabase();
        String createNewPage = "CREATE TABLE " + page + "(" +
                "imgName TEXT PRIMARY KEY, " +
                "save TEXT, " +
                "text TEXT, " +
                "hidden INTEGER, " +
                "moveable INTEGER, " +
                "x REAL, " +
                "y REAL, " +
                "width REAL, " +
                "height REAL)";
        db.execSQL(createNewPage);
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
        if (!pageList.contains(page)) {
            createNewPage(page);
            pageList.add(page);
        }
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

    public HashMap<String, ArrayList<Shape>> loadGame(String saveFile) {
        SQLiteDatabase db = this.getWritableDatabase();
        HashMap<String, ArrayList<Shape>> fullShapeList = new HashMap<String, ArrayList<Shape>>();
        for (int i = 0; i < pageList.size(); i++) {
            String currentPage = pageList.get(i);
            String query = "SELECT * FROM " + currentPage + " WHERE save == " + saveFile;
            Cursor cursor = db.rawQuery(query, null);
            ArrayList<Shape> currentShapeList = new ArrayList<Shape>();
            for (int j = 0; j < cursor.getColumnCount(); j++) {
                String shapeName = cursor.getString(0);
                String shapeText = cursor.getString(2);
                int shapeHidden = cursor.getInt(3);
                int shapeMoveable = cursor.getInt(4);
                double shapeX = cursor.getDouble(5);
                double shapeY = cursor.getDouble(6);
                double shapeWidth = cursor.getDouble(7);
                double shapeHeight =  cursor.getDouble(8);
                Shape newShape = new Shape();
                newShape.imgName = shapeName;
                newShape.text = shapeText;
                newShape.hidden = shapeHidden == 1;
                newShape.moveable = shapeMoveable == 1;
                newShape.x = shapeX;
                newShape.y = shapeY;
                newShape.width = shapeWidth;
                newShape.height = shapeHeight;
                currentShapeList.add(newShape);
                if (!cursor.isLast()) {
                    cursor.moveToNext();
                }
            }
            cursor.close();
            fullShapeList.put(currentPage, currentShapeList);
        }
        return fullShapeList;
    }
}
