package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/** Creates and manages the database for the game. Effectively works by creating a SQLite database
 * and keeping all values in a massive table that holds all page and shape information. Uses
 * SQLiteOpenHelper.
 */
class Database extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "bunny.db";
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

    /** Upon creation, makes a table that contains all page and shape data.
     */
    public void onCreate(SQLiteDatabase db) {
        String createShapeDatabase = "CREATE TABLE ShapeDatabase " + "(" +
                "shapeName TEXT," +
                "image TEXT," +
                "text TEXT," +
                "x REAL," +
                "y REAL," +
                "height REAL," +
                "width REAL," +
                "moveable INTEGER," +
                "hidden INTEGER," +
                "fontSize INTEGER," +
                "starterPage INTEGER," +
                "script TEXT," +
                "pageName TEXT," +
                "PAGEID TEXT," +
                "SAVE TEXT," +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT"+ ");";
        db.execSQL(createShapeDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ShapeDatabase");
        onCreate(db);
    }

    /** Saves the game by taking the HashMap of pages and parsing each page. Once within a page, all of the shapes and their data are
     * dumped into the database with accompanying page information. Before the game is saved, saveGame ensures that it does not create a
     * duplicate by deleting any values with the original save name.
     */
    public void saveGame(String saveName, HashMap<String, Page> pages) {
        SQLiteDatabase db = this.getWritableDatabase();
        String checkQuery = "SELECT * FROM ShapeDatabase WHERE SAVE = " + "'" + saveName + "'";
        Cursor cursor = db.rawQuery(checkQuery,null);
        if (cursor.getCount() != 0) { // Deletes save in case it already exists.
            deleteSave(saveName);
        }
        cursor.close();
        if (pages != null) {
            for (Map.Entry<String, Page> entry : pages.entrySet()) { // Source: https://stackoverflow.com/questions/1066589/iterate-through-a-hashmap (Only this one line)
                Page currentPage = entry.getValue();
                String pageName = currentPage.getDisplayName();
                String pageID = currentPage.getPageID();
                int starterPage = currentPage.getStarterPageStatus() ? 1 : 0;
                ArrayList<Shape> shapeList = currentPage.getShapes();
                for (int i = 0; i < shapeList.size(); i++) {
                    Shape currentShape = shapeList.get( i );
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
                            height + "," +
                            width + "," +
                            moveable + "," +
                            hidden + "," +
                            fontSize + "," +
                            starterPage + ", '" +
                            script + "', '" +
                            pageName + "', '" +
                            pageID + "', '" +
                            saveName + "', NULL)";
                    db.execSQL( insertStr );
                }
            }
        }
    }

    /** Loads the game by parsing the database row-by-row. A new HashMap is opened at the beginning. Each
     * row of the database is then scanned. If the saveFile name matches, the pageID is checked. If the pageID
     * already exists in the HashMap, a new shape is created, its parameters are set according to the table, and it
     * is added to the corresponding page. If the pageID does not exist, a new page is made.
     */
    public HashMap<String, Page> loadGame(String saveFile) {
        int shapeCounter = 0; // Keeps track of which shape is added in sequence. This is highly unlikely to match the original.
        SQLiteDatabase db = this.getWritableDatabase();
        HashMap<String, Page> fullShapeList = new HashMap<> ();
        String saveQuery = "SELECT * FROM ShapeDatabase";
        Cursor cursor = db.rawQuery(saveQuery,null);
        while (cursor.moveToNext()) {
            boolean starterPage = cursor.getInt(10) != 0;
            String pageName = cursor.getString(12);
            String pageID = cursor.getString(13);
            String thisSave = cursor.getString(14);
            Page newPage = null;
            if (fullShapeList.get(pageID) == null && thisSave.equals(saveFile)) {
                newPage = new Page(pageName, pageID);
                newPage.setStarterPageStatus( starterPage );
                newPage.changeDisplayName( pageName );
                fullShapeList.put(pageID,newPage);
            } else if (thisSave.equals(saveFile) && fullShapeList.get(pageID) != null) {
                newPage = fullShapeList.get(pageID);
            }
            if (thisSave.equals(saveFile)) {
                String shapeName = cursor.getString(0);
                String imageName = cursor.getString(1);
                String text = cursor.getString(2);
                double x = cursor.getDouble(3);
                double y = cursor.getDouble(4);
                double height = cursor.getDouble(5);
                double width = cursor.getDouble(6);
                boolean moveable = cursor.getInt(7) != 0;
                boolean hidden = cursor.getInt(8) != 0;
                int fontSize = cursor.getInt(9);
                String script = cursor.getString(11);
                Shape newShape = new Shape(shapeCounter, imageName, text, x, y, width, height);
                newShape.setMoveable(moveable);
                newShape.setHidden(hidden);
                newShape.setFontSize(fontSize);
                newShape.setScript(script);
                newShape.setShapeName(shapeName);
                if (newPage != null) {
                    newPage.addShape(newShape);
                }
                shapeCounter = shapeCounter + 1;

            }
        }
        cursor.close();
        return fullShapeList;
    }

    /** Deletes the save by removing every row in which the saveName is the SAVE.
     */
    public void deleteSave (String saveName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteSave = "DELETE FROM ShapeDatabase WHERE SAVE = '" + saveName + "'";
        db.execSQL( deleteSave );
    }

    /** Autosaves by deleting the old autoSave and then creating a new autoSave save.
     */
    public void autoSave(HashMap<String, Page> pages) {
        saveGame("Auto Save", pages);
    }

    /** Updates the game name through a SQL command.
     */
    public void updateGameName(String oldName, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateSave = "UPDATE ShapeDatabase SET SAVE = " + "'" + newName + "'" + " WHERE SAVE = " + "'" + oldName + "'";
        db.execSQL(updateSave);
    }

    /** Returns ArrayList with the name of each game. To do this, performs a SQL query where it
     * selects all distinct saves from the database. A cursor then iterates through each value and
     * adds it to the ArrayList.
     */
    public ArrayList<String> returnGameList() {
        ArrayList<String> gameList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String returnGameList = "SELECT DISTINCT SAVE FROM ShapeDatabase";
        Cursor cursor = db.rawQuery(returnGameList,null);
        while (cursor.moveToNext()) {
            gameList.add(cursor.getString(0));
        }
        cursor.close();
        Collections.reverse(gameList);
        return gameList;
    }

    /** Drops database table.
     */
    public void clear() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS ShapeDatabase");
        onCreate(db);
    }

    public String returnFirstPage(String save) {
        SQLiteDatabase db = this.getWritableDatabase();
        String firstPage = null;
        String returnGameList = "SELECT DISTINCT PAGEID FROM ShapeDatabase WHERE SAVE = " + "'" + save + "'";
        Cursor cursor = db.rawQuery(returnGameList,null);
        cursor.moveToNext();
        if (cursor.getString(0) != null) {
           firstPage = cursor.getString( 0 );
        }
        cursor.close();
        return firstPage;
    }

    public int getShapeCount(String save) {
        int count = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String returnGameList = "SELECT DISTINCT ShapeName FROM ShapeDatabase WHERE SAVE = " + "'" + save + "'";
        Cursor cursor = db.rawQuery(returnGameList,null);
        while (cursor.moveToNext()) {
            count++;
        }
        return count;
    }

    public int getPageCount(String save) {
        int count = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String returnGameList = "SELECT DISTINCT PAGEID FROM ShapeDatabase WHERE SAVE = " + "'" + save + "'";
        Cursor cursor = db.rawQuery(returnGameList,null);
        while (cursor.moveToNext()) {
            count++;
        }
        return count;
    }
}
