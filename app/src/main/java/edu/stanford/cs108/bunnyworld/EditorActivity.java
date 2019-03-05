package edu.stanford.cs108.bunnyworld;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.app.*;
import android.content.*;
import android.widget.*;
import android.text.*;

/*
    edu.stanford.cs108.bunnyworld.EditorActivity Class is the class that encapsulates an instance
    of the BunnyWorld editor. It is responsible for managing all the
    pages that the user may wish to create, and display the possible
    resource objects that can be added to the pages.
 */
public class EditorActivity extends AppCompatActivity {

    private int numShapes;
    private int numPages;
    private HashMap<String, ArrayList<Shape>> pages;
    private String currPage;

    // add copy and paste functionality

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Page Options Spinner
        Spinner pageSpinner = findViewById(R.id.pageSpinner);
        String[] pageOptions = new String[]{"Page Options:", "Create Page", "Name Page", "Delete Page", "Open Page"};
        ArrayAdapter<String> pageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, pageOptions);
        pageSpinner.setAdapter(pageAdapter);
        pageSpinner.setSelection(0);
        pageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        break;
                    case 1:
                        Toast addPageToast = Toast.makeText(getApplicationContext(),currPage + " Added",Toast.LENGTH_LONG);
                        addPageToast.show();
                        addPage();
                        break;
                    case 2:
                        renamePageDialog();
                        break;
                    case 3:
                        Toast toast = Toast.makeText(getApplicationContext(),"Page Deleted",Toast.LENGTH_SHORT);
                        toast.show();
                        deletePage();
                        break;
                    case 4:
                        goToNewPageDialog();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Shape Options Spinner
        Spinner shapeSpinner = findViewById(R.id.shapeSpinner);
        String[] shapeOptions = new String[]{"Shape Options:", "Add Shape", "Name Shape", "Edit Shape", "Delete Shape"};
        ArrayAdapter<String> shapeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, shapeOptions);
        shapeSpinner.setAdapter(shapeAdapter);
        shapeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        break;
                    case 1:
                        addShape();
                        break;
                    case 2:
                        renameShapeDialog();
                        break;
                    case 3:
                        break;
                    case 4:
                        Toast deleteToast = Toast.makeText(getApplicationContext(),"Shape Deleted",Toast.LENGTH_SHORT);
                        deleteToast.show();
                        deleteShape();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Scrip Options Spinenr
        Spinner scriptSpinner = findViewById(R.id.scriptSpinner);
        String[] scriptOptions = new String[]{"Script Options:", "Create Script", "Show Script"};
        ArrayAdapter<String> scriptAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, scriptOptions);
        scriptSpinner.setAdapter(scriptAdapter);
        scriptSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        showCustomDialog();

    }

    private void addPage() {
        ArrayList<Shape> newPage = new ArrayList<>();
        numPages++;
        String pageName = "page" + numPages;
        currPage = pageName;
        pages.put(pageName, newPage);
    }

    private void deletePage() {
        pages.remove(currPage);
        // update custom view to display another page
        // figure out starter page
    }

    private void addShape() {
        numShapes++;
        String shapeName = "shape" + numShapes;
        Toast addToast = Toast.makeText(getApplicationContext(),shapeName + " Added",Toast.LENGTH_SHORT);
        addToast.show();
    }

    private void deleteShape() {
    }

    // prompts the user to input a new name for the page
    private void renamePageDialog() {

        AlertDialog.Builder renamePagePrompt = new AlertDialog.Builder(this);
        renamePagePrompt.setTitle("Input New Name For Page: ");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        renamePagePrompt.setView(input);
        renamePagePrompt.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newPageName = input.getText().toString();
                renamePage(newPageName);
            }
        });
        renamePagePrompt.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        renamePagePrompt.show();

    }

    private void renamePage(String newName) {
        ArrayList<Shape> page = pages.remove(currPage);
        pages.put(newName, page);
    }

    // prompts the user to input a new name for the shape
    private void renameShapeDialog() {

        AlertDialog.Builder renameShapePrompt = new AlertDialog.Builder(this);
        renameShapePrompt.setTitle("Input New Name For Shape: ");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        renameShapePrompt.setView(input);
        renameShapePrompt.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newPageName = input.getText().toString();
                renameShape(newPageName);
            }
        });
        renameShapePrompt.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        renameShapePrompt.show();
    }

    private void renameShape(String newName) {

    }

    // prompts the user to either load a new game or load an existing game
    private void showCustomDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Create a new game or open an existing game?");
        dialog.setPositiveButton(
                "Create New Game",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        loadNewGame();
                    }
                });
        dialog.setNegativeButton(
                "Open Existing Game",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        loadExistingGame();
                    }
                });
        dialog.show();
    }

    private void loadNewGame() {
        numShapes = 0;
        numPages = 0;
        pages = new HashMap<>();
        addPage();
        Toast addToast = Toast.makeText(getApplicationContext(),currPage + " Added",Toast.LENGTH_SHORT);
        addToast.show();
    }

    private void loadExistingGame(){

    }

    // change this to list the pages so the user can see options
    private void goToNewPageDialog() {
       /* ArrayList<String> names = new ArrayList<>();
        for (String page: pages.keySet()) {
            names.add(page);
        }
        final String[] pageNames = names.toArray(new String[pages.size()]);
        AlertDialog.Builder newPagePrompt = new AlertDialog.Builder(this);
        newPagePrompt.setTitle("Input Name Of Page To Go To: ");
        newPagePrompt.setItems(pageNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selection) {
                String newPageName = pageNames[selection];
                Toast pageNameToast = Toast.makeText(getApplicationContext(),newPageName,Toast.LENGTH_LONG);
                pageNameToast.show();
                switchPages(newPageName);
            }
        });

        AlertDialog dialog = newPagePrompt.create();
        dialog.show();*/
    }

    private void switchPages(String newPage) {
        currPage = newPage;
    }

    // Saves current game state into the database.
    public void saveGame(String saveName, HashMap<String, ArrayList<Shape>> shapeMap) {
        Database db = Database.getInstance(getApplicationContext());
        db.saveGame(saveName, shapeMap);
    }
}
