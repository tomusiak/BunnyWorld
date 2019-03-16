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
        populateSampleGame();
    }

    private void populateSampleGame() {

        SQLiteDatabase db = this.getWritableDatabase();
        Shape carrot1 = new Shape(1, "carrot", "", 20, 20, 250, 250);
        String shapeName = carrot1.getShapeName();
        String imageName = carrot1.getImageName();
        String text = carrot1.getText();
        double x = carrot1.getX();
        double y = carrot1.getY();
        double height = carrot1.getHeight();
        double width = carrot1.getWidth();
        int moveable = carrot1.getMoveableStatus() ? 1 : 0;
        int hidden = carrot1.isHidden() ? 1 : 0;
        int fontSize = 48;
        String script = "";
        int starterPage = 1;
        String pageName = "page1";
        String pageID = "page1";
        String saveName = "Sample Game";
        String carrot1Str = "INSERT INTO ShapeDatabase VALUES "
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
        db.execSQL(carrot1Str);

        Shape bunny1 = new Shape(1, "mystic", "", 450, 450, 250, 250);
        String bunny1shapeName = bunny1.getShapeName();
        String bunny1imageName = bunny1.getImageName();
        String bunny1text = bunny1.getText();
        double bunny1x = bunny1.getX();
        double bunny1y = bunny1.getY();
        double bunny1height = bunny1.getHeight();
        double bunny1width = bunny1.getWidth();
        int bunny1moveable = bunny1.getMoveableStatus() ? 1 : 0;
        int bunny1hidden = bunny1.isHidden() ? 1 : 0;
        int bunny1fontSize = bunny1.getFontSize();
        String bunny1script = bunny1.getScript();
        int bunny1starterPage = 0;
        String bunny1pageName = "page2";
        String bunny1pageID = "page2";
        String bunny1saveName = "Sample Game";
        String bunny1Str = "INSERT INTO ShapeDatabase VALUES "
                + "('" + bunny1shapeName + "', '" +
                bunny1imageName + "', '" +
                bunny1text + "'," +
                bunny1x + "," +
                bunny1y + "," +
                bunny1height + "," +
                bunny1width + "," +
                bunny1moveable + "," +
                bunny1hidden + "," +
                bunny1fontSize + "," +
                bunny1starterPage + ", '" +
                bunny1script + "', '" +
                bunny1pageName + "', '" +
                bunny1pageID + "', '" +
                bunny1saveName + "', NULL)";
        db.execSQL(bunny1Str);

        Shape bunny2 = new Shape(1, "mystic", "", 450, 450, 250, 250);
        String bunny2shapeName = bunny2.getShapeName();
        String bunny2imageName = bunny2.getImageName();
        String bunny2text = bunny2.getText();
        double bunny2x = bunny2.getX();
        double bunny2y = bunny2.getY();
        double bunny2height = bunny2.getHeight();
        double bunny2width = bunny2.getWidth();
        int bunny2moveable = bunny2.getMoveableStatus() ? 1 : 0;
        int bunny2hidden = bunny2.isHidden() ? 1 : 0;
        int bunny2fontSize = bunny2.getFontSize();
        String bunny2script = bunny2.getScript();
        int bunny2starterPage = 0;
        String bunny2pageName = "page4";
        String bunny2pageID = "page4";
        String bunny2saveName = "Sample Game";
        String bunny2Str = "INSERT INTO ShapeDatabase VALUES "
                + "('" + bunny2shapeName + "', '" +
                bunny2imageName + "', '" +
                bunny2text + "'," +
                bunny2x + "," +
                bunny2y + "," +
                bunny2height + "," +
                bunny2width + "," +
                bunny2moveable + "," +
                bunny2hidden + "," +
                bunny2fontSize + "," +
                bunny2starterPage + ", '" +
                bunny2script + "', '" +
                bunny2pageName + "', '" +
                bunny2pageID + "', '" +
                bunny2saveName + "', NULL)";
        db.execSQL(bunny2Str);

        Shape gray1 = new Shape(1, "grayshape", "", 150, 900, 200, 200);
        String gray1shapeName = gray1.getShapeName();
        String gray1imageName = gray1.getImageName();
        String gray1text = gray1.getText();
        double gray1x = gray1.getX();
        double gray1y = gray1.getY();
        double gray1height = gray1.getHeight();
        double gray1width = gray1.getWidth();
        int gray1moveable = gray1.getMoveableStatus() ? 1 : 0;
        int gray1hidden = gray1.isHidden() ? 1 : 0;
        int gray1fontSize = gray1.getFontSize();
        String gray1script = "on click goto page2";
        int gray1starterPage = 1;
        String gray1pageName = "page1";
        String gray1pageID = "page1";
        String gray1saveName = "Sample Game";
        String gray1Str = "INSERT INTO ShapeDatabase VALUES "
                + "('" + gray1shapeName + "', '" +
                gray1imageName + "', '" +
                gray1text + "'," +
                gray1x + "," +
                gray1y + "," +
                gray1height + "," +
                gray1width + "," +
                gray1moveable + "," +
                gray1hidden + "," +
                gray1fontSize + "," +
                gray1starterPage + ", '" +
                gray1script + "', '" +
                gray1pageName + "', '" +
                gray1pageID + "', '" +
                gray1saveName + "', NULL)";
        db.execSQL(gray1Str);

        Shape gray2 = new Shape(1, "grayshape", "", 450, 900, 200, 200);
        String gray2shapeName = gray2.getShapeName();
        String gray2imageName = gray2.getImageName();
        String gray2text = gray2.getText();
        double gray2x = gray2.getX();
        double gray2y = gray2.getY();
        double gray2height = gray2.getHeight();
        double gray2width = gray2.getWidth();
        int gray2moveable = gray2.getMoveableStatus() ? 1 : 0;
        int gray2hidden = gray2.isHidden() ? 1 : 0;
        int gray2fontSize = gray2.getFontSize();
        String gray2script = "on click goto page3";
        int gray2starterPage = 1;
        String gray2pageName = "page1";
        String gray2pageID = "page1";
        String gray2saveName = "Sample Game";
        String gray2Str = "INSERT INTO ShapeDatabase VALUES "
                + "('" + gray2shapeName + "', '" +
                gray2imageName + "', '" +
                gray2text + "'," +
                gray2x + "," +
                gray2y + "," +
                gray2height + "," +
                gray2width + "," +
                gray2moveable + "," +
                gray2hidden + "," +
                gray2fontSize + "," +
                gray2starterPage + ", '" +
                gray2script + "', '" +
                gray2pageName + "', '" +
                gray2pageID + "', '" +
                gray2saveName + "', NULL)";
        db.execSQL(gray2Str);

        Shape gray3 = new Shape(1, "grayshape", "", 750, 900, 200, 200);
        String gray3shapeName = gray3.getShapeName();
        String gray3imageName = gray3.getImageName();
        String gray3text = gray3.getText();
        double gray3x = gray3.getX();
        double gray3y = gray3.getY();
        double gray3height = gray3.getHeight();
        double gray3width = gray3.getWidth();
        int gray3moveable = gray3.getMoveableStatus() ? 1 : 0;
        int gray3hidden = gray3.isHidden() ? 1 : 0;
        int gray3fontSize = gray3.getFontSize();
        String gray3script = "on click goto page4";
        int gray3starterPage = 1;
        String gray3pageName = "page1";
        String gray3pageID = "page1";
        String gray3saveName = "Sample Game";
        String gray3Str = "INSERT INTO ShapeDatabase VALUES "
                + "('" + gray3shapeName + "', '" +
                gray3imageName + "', '" +
                gray3text + "'," +
                gray3x + "," +
                gray3y + "," +
                gray3height + "," +
                gray3width + "," +
                gray3moveable + "," +
                gray3hidden + "," +
                gray3fontSize + "," +
                gray3starterPage + ", '" +
                gray3script + "', '" +
                gray3pageName + "', '" +
                gray3pageID + "', '" +
                gray3saveName + "', NULL)";
        db.execSQL(gray3Str);

        Shape gray4 = new Shape(1, "grayshape", "", 150, 900, 200, 200);
        String gray4shapeName = gray4.getShapeName();
        String gray4imageName = gray4.getImageName();
        String gray4text = gray4.getText();
        double gray4x = gray4.getX();
        double gray4y = gray4.getY();
        double gray4height = gray4.getHeight();
        double gray4width = gray4.getWidth();
        int gray4moveable = gray4.getMoveableStatus() ? 1 : 0;
        int gray4hidden = gray4.isHidden() ? 1 : 0;
        int gray4fontSize = gray4.getFontSize();
        String gray4script = "on click goto page1";
        int gray4starterPage = 1;
        String gray4pageName = "page2";
        String gray4pageID = "page2";
        String gray4saveName = "Sample Game";
        String gray4Str = "INSERT INTO ShapeDatabase VALUES "
                + "('" + gray4shapeName + "', '" +
                gray4imageName + "', '" +
                gray4text + "'," +
                gray4x + "," +
                gray4y + "," +
                gray4height + "," +
                gray4width + "," +
                gray4moveable + "," +
                gray4hidden + "," +
                gray4fontSize + "," +
                gray4starterPage + ", '" +
                gray4script + "', '" +
                gray4pageName + "', '" +
                gray4pageID + "', '" +
                gray4saveName + "', NULL)";
        db.execSQL(gray4Str);

        Shape gray5 = new Shape(1, "grayshape", "", 450, 900, 200, 200);
        String gray5shapeName = gray5.getShapeName();
        String gray5imageName = gray5.getImageName();
        String gray5text = gray5.getText();
        double gray5x = gray5.getX();
        double gray5y = gray5.getY();
        double gray5height = gray5.getHeight();
        double gray5width = gray5.getWidth();
        int gray5moveable = gray5.getMoveableStatus() ? 1 : 0;
        int gray5hidden = gray5.isHidden() ? 1 : 0;
        int gray5fontSize = gray5.getFontSize();
        String gray5script = "on click goto page2";
        int gray5starterPage = 0;
        String gray5pageName = "page3";
        String gray5pageID = "page3";
        String gray5saveName = "Sample Game";
        String gray5Str = "INSERT INTO ShapeDatabase VALUES "
                + "('" + gray5shapeName + "', '" +
                gray5imageName + "', '" +
                gray5text + "'," +
                gray5x + "," +
                gray5y + "," +
                gray5height + "," +
                gray5width + "," +
                gray5moveable + "," +
                gray5hidden + "," +
                gray5fontSize + "," +
                gray5starterPage + ", '" +
                gray5script + "', '" +
                gray5pageName + "', '" +
                gray5pageID + "', '" +
                gray5saveName + "', NULL)";
        db.execSQL(gray5Str);

        Shape gray6 = new Shape(1, "grayshape", "", 750, 700, 200, 200);
        String gray6shapeName = gray6.getShapeName();
        String gray6imageName = gray6.getImageName();
        String gray6text = gray6.getText();
        double gray6x = gray6.getX();
        double gray6y = gray6.getY();
        double gray6height = gray6.getHeight();
        double gray6width = gray6.getWidth();
        int gray6moveable = gray6.getMoveableStatus() ? 1 : 0;
        int gray6hidden = gray6.isHidden() ? 1 : 0;
        int gray6fontSize = gray6.getFontSize();
        String gray6script = "on click goto page5";
        int gray6starterPage = 1;
        String gray6pageName = "page4";
        String gray6pageID = "page4";
        String gray6saveName = "Sample Game";
        String gray6Str = "INSERT INTO ShapeDatabase VALUES "
                + "('" + gray6shapeName + "', '" +
                gray6imageName + "', '" +
                gray6text + "'," +
                gray6x + "," +
                gray6y + "," +
                gray6height + "," +
                gray6width + "," +
                gray6moveable + "," +
                gray6hidden + "," +
                gray6fontSize + "," +
                gray6starterPage + ", '" +
                gray6script + "', '" +
                gray6pageName + "', '" +
                gray6pageID + "', '" +
                gray6saveName + "', NULL)";
        db.execSQL(gray6Str);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ShapeDatabase");
        onCreate(db);
    }

    /** Saves the game by taking the HashMap of pages and parsing each page. Once within a page, all of the shapes and their data are
     * dumped into the database with accompanying page information. Before the game is saved, saveGame ensures that it does not create a
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
                    int hidden = currentShape.isHidden() ? 1 : 0;
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
