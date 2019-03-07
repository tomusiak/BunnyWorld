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
        String createShapeDatabase = "CREATE TABLE ShapeDatabase " + "(" +
                "shapeName TEXT, " +
                "image TEXT, " +
                "text TEXT, " +
                "x REAL, " +
                "y REAL, " +
                "height REAL, " +
                "width REAL, " +
                "moveable INTEGER, " +
                "hidden INTEGER, " +
                "fontSize INTEGER, " +
                "script TEXT, " +
                "PAGE TEXT PRIMARY KEY, " +
                "SAVE TEXT PRIMARY KEY) ";
        db.execSQL(createShapeDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void saveGame(String saveName, HashMap<String, Page> pages) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (Map.Entry<String, Page> entry : pages.entrySet()) { // Source: https://stackoverflow.com/questions/1066589/iterate-through-a-hashmap (Only this one line)
            String pageName = entry.getKey();
            Page currentPage = entry.getValue();
            ArrayList<Shape> shapeList = currentPage.getShapes();
            for (int i = 0; i < shapeList.size(); i++) {
                Shape currentShape = shapeList.get(i);
                String shapeName = currentShape.getShapeName();
                String imageName = currentShape.getImageName();
                String text = currentShape.getText();
                double x = currentShape.getX();
                double y = currentShape.getY();
                double height = currentShape.getHeight();
                double width = currentShape.getWidth();
                int moveable = currentShape.getMoveableStatus() ? 1 : 0;
                int hidden = currentShape.getHiddenStatus() ? 1 : 0;
                int fontSize = currentShape.getFontSize();
                String script = currentShape.getScript();
                String insertStr = "INSERT INTO ShapeDatabase VALUES "
                    + "('" + shapeName + "', '" +
                    imageName + "', '" +
                    text + "'," +
                    x + "," +
                    y + "," +
                    height+ "," +
                    width + "," +
                    moveable + "," +
                    hidden + "," +
                    fontSize + ", '" +
                    script + "', '" +
                    pageName + "', '" +
                    saveName + "')";
                db.execSQL(insertStr);
            }
        }
    }

    public HashMap<String, ArrayList<Shape>> loadGame(String saveFile) {
        SQLiteDatabase db = this.getWritableDatabase();
        HashMap<String, ArrayList<Shape>> fullShapeList = new HashMap<String, ArrayList<Shape>>();
        for (int i = 0; i < 8; i++) {
            String currentPage = "PLACEHOLDER";
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
                Shape newShape = new Shape(0, shapeName, shapeText, shapeX,
                        shapeY, shapeWidth, shapeHeight);
                newShape.setShapeName(shapeName);
                newShape.setText(shapeText);
                newShape.setHidden(shapeHidden == 1);
                newShape.setMoveable(shapeMoveable == 1);
                newShape.setX(shapeX);
                newShape.setY(shapeY);
                newShape.setWidth(shapeWidth);
                newShape.setHeight(shapeHeight);
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
