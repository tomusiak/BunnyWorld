package edu.stanford.cs108.bunnyworld;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayActivity extends AppCompatActivity {

    // Commented out this first line and replaced with a Hashmap<String, Page> to match agreed upon structures
    //static HashMap<String, ArrayList<Shape>> fullShapeList; // Contains key of string of page names linked to an ArrayList of shapes.
    static HashMap<String, Page> pageMap; // Maps string keys to page objects
    String currPage;
    PlayView playView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        loadGame();
        playView = findViewById(R.id.play_view);
    }

    private void loadGame() {
        String saveGame = ""; // User input save name goes here.
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Play a game you made or the standard experience?");
        dialog.setPositiveButton(
                "Standard Experience",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        dialog.setNegativeButton(
                "Load Game",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                grabDatabase(saveGame); // Dumps save information into the HashMap containing pages and shapes.
        dialog.show();

    }

    /**
     * Helper method to switch between pages
     * @param pageName the page to switch to
     */
    private void switchPages(String pageName) {
        currPage = pageName;
        Page nextPage = pageMap.get(pageName);
        playView.changeCurrentPage(nextPage);
    }

    // Imports save data once user decides to play.
    public void grabDatabase(String saveName) {
        Database thisDatabase = Database.getInstance(getApplicationContext()); // Gets context.
        //pageMap = thisDatabase.loadGame(saveName);
    }
}