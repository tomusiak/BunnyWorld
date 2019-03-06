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

    // use unique ID when looking up in hashmaps and when adding to script
    private int numShapes;
    private int numPages;  // number of pages created so far, accumulative
    private HashMap<String, Page> pages; // map the unique page IDs to Page objects
    private HashMap<String, String> displayNameToID;
    private String currPage;
    private String currScript;
    EditorView editorView;

    // add copy and paste functionality

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Page Options Spinner
        final Spinner pageSpinner = findViewById(R.id.pageSpinner);
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
                        addPage();
                        Toast addPageToast = Toast.makeText(getApplicationContext(),currPage + " Added",Toast.LENGTH_SHORT);
                        addPageToast.show();
                        break;
                    case 2:
                        renamePageDialog();
                        break;
                    case 3:
                        deletePageDialog();
                        break;
                    case 4:
                        goToNewPageDialog();
                        break;
                }
                pageSpinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Shape Options Spinner
        final Spinner shapeSpinner = findViewById(R.id.shapeSpinner);
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
                        deleteShape();
                        break;
                }
                shapeSpinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Scrip Options Spinner
        final Spinner scriptSpinner = findViewById(R.id.scriptSpinner);
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
                        handleScript();
                        break;
                    case 2:
                        break;
                }
                scriptSpinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        showCustomDialog();

    }

    private void handleScript() {
        currScript = "";
        scriptTriggersDialog();
    }

    private void scriptTriggersDialog() {
        final String[] scriptTriggers = new String[]{"On Click", "On Enter", "On Drop"};
        AlertDialog.Builder triggersPrompt = new AlertDialog.Builder(this);
        triggersPrompt.setTitle("Select Script Trigger: ");
        triggersPrompt.setItems(scriptTriggers, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selection) {
                currScript += scriptTriggers[selection] + " ";
                scriptActionsDialog();
            }
        });
        triggersPrompt.show();
    }

    private void scriptActionsDialog() {
        final String[] scriptActions = new String[]{"Go To", "Play", "Hide", "Show"};
        AlertDialog.Builder actionsPrompt = new AlertDialog.Builder(this);
        actionsPrompt.setTitle("Select Script Action: ");
        actionsPrompt.setItems(scriptActions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selection) {
                currScript += scriptActions[selection] + " ";
                addMoreDialog();
                switch(selection) {
                    case 0:
                        scriptGoToDialog();
                        break;
                    case 1:
                        scriptPlayDialog();
                        break;
                    case 2:
                        scriptShapeNameDialog();
                        break;
                    case 3:
                        scriptShapeNameDialog();
                        break;
                }
            }
        });
        actionsPrompt.show();
    }

    private void scriptGoToDialog() {
        ArrayList<String> names = new ArrayList<>();
        // builds arraylist of page display names for user to select
        for (String uniquePageID: pages.keySet()) {
            names.add(pages.get(uniquePageID).getDisplayName());
        }
        final String[] pageNames = names.toArray(new String[pages.size()]);
        AlertDialog.Builder goToPrompt = new AlertDialog.Builder(this);
        goToPrompt.setTitle("Select Name of Page: ");
        goToPrompt.setItems(pageNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selection) {
                String newPageName = pageNames[selection];
                // from the selected display name, pull the original page object
                String uniqueID = displayNameToID.get(newPageName);
                currScript += uniqueID + " ";
            }
        });
        goToPrompt.show();
    }

    private void scriptPlayDialog() {
        final String[] scriptSounds = new String[]{"CarrotCarrotCarrot", "EvilLaugh", "Fire", "Hooray", "Munch", "Munching", "Woof"};
        AlertDialog.Builder playPrompt = new AlertDialog.Builder(this);
        playPrompt.setTitle("Select Script Trigger: ");
        playPrompt.setItems(scriptSounds, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selection) {
                currScript += scriptSounds[selection] + " ";
            }
        });
        playPrompt.show();
    }

    private void scriptShapeNameDialog() {
        AlertDialog.Builder shapeNamePrompt = new AlertDialog.Builder(this);
        shapeNamePrompt.setTitle("Input Name of Shape: ");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        shapeNamePrompt.setView(input);
        shapeNamePrompt.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currScript += input.getText().toString() + " ";
            }
        });
        shapeNamePrompt.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        shapeNamePrompt.show();
    }

    private void addPage() {
        numPages++;
        String uniquePageID = "page" + numPages; // create unique identifier for page
        String pageName = uniquePageID + "";     // modifiable default page name
        currPage = uniquePageID;
        displayNameToID.put(pageName, uniquePageID);

        Page newPage = new Page(pageName, uniquePageID);
        pages.put(uniquePageID, newPage);
    }

    private void addMoreDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Add More Actions to Script Trigger?");
        dialog.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        scriptActionsDialog();
                    }
                });
        dialog.setNegativeButton(
                "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        currScript = currScript.substring(0, currScript.length() - 1);
                        currScript += ";";
                        dialog.cancel();
                    }
                });
        dialog.show();
    }

    private void addShape() {
        numShapes++;
        String shapeName = "shape" + numShapes;
        Toast addToast = Toast.makeText(getApplicationContext(),shapeName + " Added",Toast.LENGTH_SHORT);
        addToast.show();

        // example of building a shape from a shape image name
        String shapeImgName = "carrot";
        Shape shape = new Shape(numShapes, shapeImgName, "",
                0, 0, 50, 50);

        //editorView.renderShape(shape);
        //editorView.drawPage();

    }

    private void deleteShape() {

    }

    private void deletePageDialog() {
        ArrayList<String> names = new ArrayList<>();
        for (String page: pages.keySet()) {
            names.add(page);
        }
        final String[] pageNames = names.toArray(new String[pages.size()]);
        AlertDialog.Builder deleteShapePrompt = new AlertDialog.Builder(this);
        deleteShapePrompt.setTitle("Page To Delete: ");
        deleteShapePrompt.setItems(pageNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selection) {
                String pageName = pageNames[selection];
                String uniqueID = displayNameToID.get(pageName);
                pages.remove(uniqueID);
                // update custom view to reflect this
                // figure out starter page
                Toast pageNameToast = Toast.makeText(getApplicationContext(), pageName + " Deleted",Toast.LENGTH_SHORT);
                pageNameToast.show();
            }
        });
        deleteShapePrompt.show();
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
        //ArrayList<Shape> page = pages.remove(currPage);
        //pages.put(newName, page);
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
        editorView = findViewById(R.id.editorView);
        numShapes = 0;
        numPages = 0;
        pages = new HashMap<>();
        displayNameToID = new HashMap<>();
        addPage();
        Toast addToast = Toast.makeText(getApplicationContext(),currPage + " Added",Toast.LENGTH_SHORT);
        addToast.show();
    }

    // deal with this once Allan adds database method that returns names of games
    private void loadExistingGame(){
        Database db = Database.getInstance(getApplicationContext());
        //db.loadGame()
    }

    // change this to list the pages so the user can see options
    private void goToNewPageDialog() {
        System.out.println("cool");
        /*ArrayList<String> names = new ArrayList<>();
        // builds arraylist of page display names for user to select
        for (String uniquePageID: pages.keySet()) {
            names.add(pages.get(uniquePageID).getDisplayName());
        }
        final String[] pageNames = names.toArray(new String[pages.size()]);
        AlertDialog.Builder newPagePrompt = new AlertDialog.Builder(this);
        newPagePrompt.setTitle("Input Name Of Page To Go To: ");
        newPagePrompt.setItems(pageNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selection) {
                String newPageName = pageNames[selection];
                Toast pageNameToast = Toast.makeText(getApplicationContext(),newPageName,Toast.LENGTH_SHORT);
                pageNameToast.show();

                // from the selected display name, pull the original page object
                String uniqueID = displayNameToID.get(newPageName);
                switchPages(uniqueID);
            }
        });
        newPagePrompt.show();*/
    }

    private void switchPages(String pageName) {
        currPage = pageName;
        Page newPage = pages.get(currPage);
        //editorView.changeCurrentPage(newPage);
    }
    // Saves current game state into the database.
    public void saveGame(String saveName, HashMap<String, ArrayList<Shape>> shapeMap) {
        Database db = Database.getInstance(getApplicationContext());
        db.saveGame(saveName, shapeMap);
    }
}