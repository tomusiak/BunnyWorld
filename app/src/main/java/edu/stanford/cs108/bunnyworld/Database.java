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



        // CARROT 1
        Shape carrot1 = new Shape(1, "carrot", "", 20, 20, 250, 250);
        String carrot1shapeName = carrot1.getShapeName();
        String carrot1imageName = carrot1.getImageName();
        String carrot1text = carrot1.getText();
        double carrot1x = carrot1.getX();
        double carrot1y = carrot1.getY();
        double carrot1height = carrot1.getHeight();
        double carrot1width = carrot1.getWidth();
        int carrot1moveable = carrot1.getMoveableStatus() ? 1 : 0;
        int carrot1hidden = carrot1.isHidden() ? 1 : 0;
        int carrot1fontSize = 48;
        String carrot1script = "";
        int carrot1starterPage = 1;
        String carrot1pageName = "page3";
        String carrot1pageID = "page3";
        String carrot1saveName = "Sample Game";
        String carrot1Str = "INSERT INTO ShapeDatabase VALUES "
                + "('" + carrot1shapeName + "', '" +
                carrot1imageName + "', '" +
                carrot1text + "'," +
                carrot1x + "," +
                carrot1y + "," +
                carrot1height + "," +
                carrot1width + "," +
                carrot1moveable + "," +
                carrot1hidden + "," +
                carrot1fontSize + "," +
                carrot1starterPage + ", '" +
                carrot1script + "', '" +
                carrot1pageName + "', '" +
                carrot1pageID + "', '" +
                carrot1saveName + "', NULL)";
        db.execSQL(carrot1Str);

        // CARROT 2
        Shape carrot2 = new Shape(2, "carrot", "", 20, 20, 250, 250);
        String carrot2shapeName = carrot2.getShapeName();
        String carrot2imageName = carrot2.getImageName();
        String carrot2text = carrot2.getText();
        double carrot2x = carrot2.getX();
        double carrot2y = carrot2.getY();
        double carrot2height = carrot2.getHeight();
        double carrot2width = carrot2.getWidth();
        int carrot2moveable = carrot2.getMoveableStatus() ? 1 : 0;
        int carrot2hidden = carrot2.isHidden() ? 1 : 0;
        int carrot2fontSize = 48;
        String carrot2script = "";
        int carrot2starterPage = 1;
        String carrot2pageName = "page5";
        String carrot2pageID = "page5";
        String carrot2saveName = "Sample Game";
        String carrot2Str = "INSERT INTO ShapeDatabase VALUES "
                + "('" + carrot2shapeName + "', '" +
                carrot2imageName + "', '" +
                carrot2text + "'," +
                carrot2x + "," +
                carrot2y + "," +
                carrot2height + "," +
                carrot2width + "," +
                carrot2moveable + "," +
                carrot2hidden + "," +
                carrot2fontSize + "," +
                carrot2starterPage + ", '" +
                carrot2script + "', '" +
                carrot2pageName + "', '" +
                carrot2pageID + "', '" +
                carrot2saveName + "', NULL)";
        db.execSQL(carrot2Str);

        // CARROT 3
        Shape carrot3 = new Shape(3, "carrot", "", 20, 20, 250, 250);
        String carrot3shapeName = carrot3.getShapeName();
        String carrot3imageName = carrot3.getImageName();
        String carrot3text = carrot3.getText();
        double carrot3x = carrot3.getX();
        double carrot3y = carrot3.getY();
        double carrot3height = carrot3.getHeight();
        double carrot3width = carrot3.getWidth();
        int carrot3moveable = carrot3.getMoveableStatus() ? 1 : 0;
        int carrot3hidden = carrot3.isHidden() ? 1 : 0;
        int carrot3fontSize = 48;
        String carrot3script = "";
        int carrot3starterPage = 1;
        String carrot3pageName = "page5";
        String carrot3pageID = "page5";
        String carrot3saveName = "Sample Game";
        String carrot3Str = "INSERT INTO ShapeDatabase VALUES "
                + "('" + carrot3shapeName + "', '" +
                carrot3imageName + "', '" +
                carrot3text + "'," +
                carrot3x + "," +
                carrot3y + "," +
                carrot3height + "," +
                carrot3width + "," +
                carrot3moveable + "," +
                carrot3hidden + "," +
                carrot3fontSize + "," +
                carrot3starterPage + ", '" +
                carrot3script + "', '" +
                carrot3pageName + "', '" +
                carrot3pageID + "', '" +
                carrot3saveName + "', NULL)";
        db.execSQL(carrot3Str);

        // CARROT 4
        Shape carrot4 = new Shape(4, "carrot", "", 20, 20, 250, 250);
        String carrot4shapeName = carrot4.getShapeName();
        String carrot4imageName = carrot4.getImageName();
        String carrot4text = carrot4.getText();
        double carrot4x = carrot4.getX();
        double carrot4y = carrot4.getY();
        double carrot4height = carrot4.getHeight();
        double carrot4width = carrot4.getWidth();
        int carrot4moveable = carrot4.getMoveableStatus() ? 1 : 0;
        int carrot4hidden = carrot4.isHidden() ? 1 : 0;
        int carrot4fontSize = 48;
        String carrot4script = "";
        int carrot4starterPage = 1;
        String carrot4pageName = "page5";
        String carrot4pageID = "page5";
        String carrot4saveName = "Sample Game";
        String carrot4Str = "INSERT INTO ShapeDatabase VALUES "
                + "('" + carrot4shapeName + "', '" +
                carrot4imageName + "', '" +
                carrot4text + "'," +
                carrot4x + "," +
                carrot4y + "," +
                carrot4height + "," +
                carrot4width + "," +
                carrot4moveable + "," +
                carrot4hidden + "," +
                carrot4fontSize + "," +
                carrot4starterPage + ", '" +
                carrot4script + "', '" +
                carrot4pageName + "', '" +
                carrot4pageID + "', '" +
                carrot4saveName + "', NULL)";
        db.execSQL(carrot4Str);

        // FIRE
        Shape fire = new Shape(5, "fire", "", 20, 20, 250, 250);
        String fireshapeName = fire.getShapeName();
        String fireimageName = fire.getImageName();
        String firetext = fire.getText();
        double firex = fire.getX();
        double firey = fire.getY();
        double fireheight = fire.getHeight();
        double firewidth = fire.getWidth();
        int firemoveable = fire.getMoveableStatus() ? 1 : 0;
        int firehidden = fire.isHidden() ? 1 : 0;
        int firefontSize = 48;
        String firescript = "";
        int firestarterPage = 1;
        String firepageName = "page3";
        String firepageID = "page3";
        String firesaveName = "Sample Game";
        String fireStr = "INSERT INTO ShapeDatabase VALUES "
                + "('" + fireshapeName + "', '" +
                fireimageName + "', '" +
                firetext + "'," +
                firex + "," +
                firey + "," +
                fireheight + "," +
                firewidth + "," +
                firemoveable + "," +
                firehidden + "," +
                firefontSize + "," +
                firestarterPage + ", '" +
                firescript + "', '" +
                firepageName + "', '" +
                firepageID + "', '" +
                firesaveName + "', NULL)";
        db.execSQL(fireStr);

        // BUNNY 1
        Shape bunny1 = new Shape(6, "mystic", "", 450, 450, 250, 250);
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
        String bunny1script = "on click hide carrot1 play munch;on enter show door2";
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

        Shape bunny2 = new Shape(7, "mystic", "", 450, 450, 250, 250);
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
        String bunny2script = "on enter play evillaugh;on drop carrot1 hide carrot1 play carrot-eating " +
                "hide dath-bunny show exit; on click play evillaugh;";
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

        Shape gray1 = new Shape(8, "grayshape", "", 150, 900, 200, 200);
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

        Shape door2 = new Shape(9, "grayshape", "", 450, 900, 200, 200);
        String door2shapeName = door2.getShapeName();
        String door2imageName = door2.getImageName();
        String door2text = door2.getText();
        double door2x = door2.getX();
        double door2y = door2.getY();
        double door2height = door2.getHeight();
        double door2width = door2.getWidth();
        int door2moveable = door2.getMoveableStatus() ? 1 : 0;
        int door2hidden = 1;
        int door2fontSize = door2.getFontSize();
        String door2script = "on click goto page3";
        int door2starterPage = 1;
        String door2pageName = "page1";
        String door2pageID = "page1";
        String door2saveName = "Sample Game";
        String door2Str = "INSERT INTO ShapeDatabase VALUES "
                + "('" + door2shapeName + "', '" +
                door2imageName + "', '" +
                door2text + "'," +
                door2x + "," +
                door2y + "," +
                door2height + "," +
                door2width + "," +
                door2moveable + "," +
                door2hidden + "," +
                door2fontSize + "," +
                door2starterPage + ", '" +
                door2script + "', '" +
                door2pageName + "', '" +
                door2pageID + "', '" +
                door2saveName + "', NULL)";
        db.execSQL(door2Str);

        Shape gray3 = new Shape(10, "grayshape", "", 750, 900, 200, 200);
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

        Shape gray4 = new Shape(11, "grayshape", "", 150, 900, 200, 200);
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

        Shape gray5 = new Shape(12, "grayshape", "", 450, 900, 200, 200);
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

        Shape exit = new Shape(13, "grayshape", "", 750, 700, 200, 200);
        String exitshapeName = exit.getShapeName();
        String exitimageName = exit.getImageName();
        String exittext = exit.getText();
        double exitx = exit.getX();
        double exity = exit.getY();
        double exitheight = exit.getHeight();
        double exitwidth = exit.getWidth();
        int exitmoveable = exit.getMoveableStatus() ? 1 : 0;
        int exithidden = 1;
        int exitfontSize = exit.getFontSize();
        String exitscript = "on click goto page5";
        int exitstarterPage = 1;
        String exitpageName = "page4";
        String exitpageID = "page4";
        String exitsaveName = "Sample Game";
        String exitStr = "INSERT INTO ShapeDatabase VALUES "
                + "('" + exitshapeName + "', '" +
                exitimageName + "', '" +
                exittext + "'," +
                exitx + "," +
                exity + "," +
                exitheight + "," +
                exitwidth + "," +
                exitmoveable + "," +
                exithidden + "," +
                exitfontSize + "," +
                exitstarterPage + ", '" +
                exitscript + "', '" +
                exitpageName + "', '" +
                exitpageID + "', '" +
                exitsaveName + "', NULL)";
        db.execSQL(exitStr);


        // Bunny World
        Shape carrota = new Shape(14, "carrot", "Bunny World", 20, 20, 250, 150);
        String carrotashapeName = carrota.getShapeName();
        String carrotaimageName = carrota.getImageName();
        String carrotatext = "Bunny World";
        double carrotax = carrot1.getX();
        double carrotay = carrot1.getY();
        double carrotaheight = carrota.getHeight();
        double carrotawidth = carrota.getWidth();
        int carrotamoveable = carrota.getMoveableStatus() ? 1 : 0;
        int carrotahidden = carrota.isHidden() ? 1 : 0;
        int carrotafontSize = 48;
        String carrotascript = "";
        int carrotastarterPage = 1;
        String carrotapageName = "page1";
        String carrotapageID = "page1";
        String carrotasaveName = "Sample Game";
        String carrotaStr = "INSERT INTO ShapeDatabase VALUES "
                + "('" + carrotashapeName + "', '" +
                carrotaimageName + "', '" +
                carrotatext + "'," +
                carrotax + "," +
                carrotay + "," +
                carrotaheight + "," +
                carrotawidth + "," +
                carrotamoveable + "," +
                carrotahidden + "," +
                carrotafontSize + "," +
                carrotastarterPage + ", '" +
                carrotascript + "', '" +
                carrotapageName + "', '" +
                carrotapageID + "', '" +
                carrotasaveName + "', NULL)";
        db.execSQL(carrotaStr);

//        // page 1
//        Shape carrota = new Shape(14, "carrot", "Bunny World", 20, 20, 250, 150);
//        String carrotashapeName = carrota.getShapeName();
//        String carrotaimageName = carrota.getImageName();
//        String carrotatext = "Bunny World";
//        double carrotax = carrot1.getX();
//        double carrotay = carrot1.getY();
//        double carrotaheight = carrota.getHeight();
//        double carrotawidth = carrota.getWidth();
//        int carrotamoveable = carrota.getMoveableStatus() ? 1 : 0;
//        int carrotahidden = carrota.isHidden() ? 1 : 0;
//        int carrotafontSize = 48;
//        String carrotascript = "";
//        int carrotastarterPage = 1;
//        String carrotapageName = "page1";
//        String carrotapageID = "page1";
//        String carrotasaveName = "Sample Game";
//        String carrotaStr = "INSERT INTO ShapeDatabase VALUES "
//                + "('" + carrotashapeName + "', '" +
//                carrotaimageName + "', '" +
//                carrotatext + "'," +
//                carrotax + "," +
//                carrotay + "," +
//                carrotaheight + "," +
//                carrotawidth + "," +
//                carrotamoveable + "," +
//                carrotahidden + "," +
//                carrotafontSize + "," +
//                carrotastarterPage + ", '" +
//                carrotascript + "', '" +
//                carrotapageName + "', '" +
//                carrotapageID + "', '" +
//                carrotasaveName + "', NULL)";
//        db.execSQL(carrotaStr);
        
        Shape win = new Shape(15, "carrot", "You Win! Yay!", 100, 900, 150, 150);
        String winshapeName = win.getShapeName();
        String winimageName = win.getImageName();
        String wintext = "You Win! Yay!";
        double winx = win.getX();
        double winy = win.getY();
        double winheight = win.getHeight();
        double winwidth = win.getWidth();
        int winmoveable = win.getMoveableStatus() ? 1 : 0;
        int winhidden = win.isHidden() ? 1 : 0;
        int winfontSize = 24;
        String winscript = "on enter play victory";
        int winstarterPage = 1;
        String winpageName = "page5";
        String winpageID = "page5";
        String winsaveName = "Sample Game";
        String winStr = "INSERT INTO ShapeDatabase VALUES "
                + "('" + winshapeName + "', '" +
                winimageName + "', '" +
                wintext + "'," +
                winx + "," +
                winy + "," +
                winheight + "," +
                winwidth + "," +
                winmoveable + "," +
                winhidden + "," +
                winfontSize + "," +
                winstarterPage + ", '" +
                winscript + "', '" +
                winpageName + "', '" +
                winpageID + "', '" +
                winsaveName + "', NULL)";
        db.execSQL(winStr);
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
                newShape.setText(text);
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
