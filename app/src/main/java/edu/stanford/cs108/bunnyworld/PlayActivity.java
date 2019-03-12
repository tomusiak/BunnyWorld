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

    Page currentPage = new Page;
    static HashMap<String, ArrayList<Shape>> fullShapeList; // Contains key of string of page names linked to an ArrayList of shapes.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        loadGame();
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

    // Imports save data once user decides to play.
    public void grabDatabase(String saveName) {
        Database thisDatabase = Database.getInstance(getApplicationContext()); // Gets context.
        //fullShapeList = thisDatabase.loadGame(saveName);
    }
}