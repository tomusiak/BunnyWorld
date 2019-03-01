package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
// I did some stuff.
class Database extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    private static final String DATABASE_NAME = "DB";
    private static Database instance;
    private int numPages;

    public static synchronized Database getInstance(Context context) {
        if (instance == null) {
            instance = new Database(context.getApplicationContext());
        }
        return instance;
    }

    private Database(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        String createPageOne = "CREATE TABLE Page1 " + "(" +
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
    public void createNewPage() {
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

    public void addNewObject(Shape shape, Integer page) {
        String currentPage = "Page" + Integer.toString(page);
        String shapeName = shape.imgName;
        String shapeText = shape.text;
        int shapeHidden = shape.hidden ? 1 : 0;
        int shapeMoveable = shape.moveable ? 1 : 0;
        double shapeX = shape.getX();
        double shapeY = shape.getY();
        double shapeWidth = shape.getWidth();
        double shapeHeight = shape.getHeight();
        String insertStr = "INSERT INTO " + currentPage + " VALUES "
                + "('" + shapeName + "'," +
                shapeText + "'," +
                shapeHidden + "," +
                shapeMoveable + "," +
                shapeX + "," +
                shapeY + "," +
                shapeWidth + "," +
                shapeHeight + ")";
        db.execSQL(insertStr);

    }
}
