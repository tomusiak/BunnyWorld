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
    resource objects that can be added to the pages. It also translates button commands into Shape script.
 */
public class EditorActivity extends AppCompatActivity {

    private int numShapes; // Tracks unique ID of shape when looking up in HashMap and adding to script
    private int numPages;  // Tracks cumulative number of pages created so far
    private HashMap<String, Page> pages; // Maps unique Page IDs to Page objects
    private HashMap<String, String> displayNameToID; // Maps display name of Page to unique ID of Page
    private String currPage;
    private Page currentPage;
    private String currScript;
    private EditorView editorView;

    /* TODO: Add copy and paste functionality */

    /**
     * Sets up spinners and their respective functions upon creation of page
     * @param savedInstanceState current saved state to be initialized
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Initializes Spinner for page options
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
                // Do nothing
            }
        });

        // Initializes spinner for shape options
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
                // Do nothing
            }
        });

        // Initialize spinner for script options
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
                // Do nothing
            }
        });
        showCustomDialog();
    }

    /**
     * Generates script given clicked commands
     * Pre-condition: User has gone through the other spinners and clicked sufficient options to generate a valid script
     */
    private void handleScript() {
        // Initializes empty String to add new script
        currScript = "";
        scriptTriggersDialog();
        // TODO: in Shape, setScript(getScript() + currScript)
    }

    /**
     * Converts trigger-related button clicks into script text and adds to currScript
     * Calls scriptActionsDialog() method to set script actions after trigger option is saved
     */
    private void scriptTriggersDialog() {
        final String[] scriptTriggers = new String[]{"On Click", "On Enter", "On Drop"};
        AlertDialog.Builder triggersPrompt = new AlertDialog.Builder(this);
        triggersPrompt.setTitle("Select Script Trigger: ");
        triggersPrompt.setItems(scriptTriggers, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selection) {
                currScript += scriptTriggers[selection] + " ";
                // Allows users to set actions after setting triggers
                scriptActionsDialog();
            }
        });
        triggersPrompt.show();
    }

    /**
     * Converts action-related button clicks into script text and adds to currScript
     * Calls respective methods for each action to continue adding to currScript
     */
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

    /**
     * Parses and adds "go to" + Page command to currScript
     */
    private void scriptGoToDialog() {
        // Generates ArrayList of all current page display names for the user to select from
        ArrayList<String> names = new ArrayList<>();
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
                // From the selected display name, pull the original page object
                String uniqueID = displayNameToID.get(newPageName);
                currScript += uniqueID + " ";
            }
        });
        goToPrompt.show();
    }

    /**
     * Parses and adds "play" + SoundName command to currScript
     */
    private void scriptPlayDialog() {
        // Predetermined list of names of sounds
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

    /**
     * Allows for custom input of Shape names by the user
     */
    private void scriptShapeNameDialog() {
        AlertDialog.Builder shapeNamePrompt = new AlertDialog.Builder(this);
        shapeNamePrompt.setTitle("Input Name of Shape: ");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        shapeNamePrompt.setView(input);
        // Saves Shape name
        shapeNamePrompt.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currScript += input.getText().toString() + " ";
            }
        });
        // Enables cancel option
        shapeNamePrompt.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        shapeNamePrompt.show();
    }

    /**
     * Creates new Page and initializes relevant internal variables
     */
    private void addPage() {
        numPages++;
        // Creates internal unique page ID
        String uniquePageID = "page" + numPages;
        // Creates user-modifiable default page name
        String pageName = uniquePageID + "";
        // Sets current page to be the newly created internal unique page ID
        currPage = uniquePageID;
        displayNameToID.put(pageName, uniquePageID);

        Page newPage = new Page(pageName, uniquePageID);
        pages.put(uniquePageID, newPage);

        currentPage = newPage;
        // Updates the current page in the view
        editorView.changeCurrentPage(currentPage);
    }

    /**
     * Enables loop to add more commands to dialog
     * Pre-condition: User has already added some dialog on the first go-around
     */
    private void addMoreDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Add More Actions to Script Trigger?");
        // Enables option to add more commands
        dialog.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        scriptActionsDialog();
                    }
                });
        // Enables option to not add more commands
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

    /**
     * Adds shape to screen and tracks it internally
     */
    private void addShape() {
        makePopUp();

        numShapes++;
        String shapeName = "shape" + numShapes;
        Toast addToast = Toast.makeText(getApplicationContext(),shapeName + " Added",Toast.LENGTH_SHORT);
        addToast.show();

        // Sample: Building a shape from a shape image name
        String shapeImgName = "carrot";
        Shape shape = new Shape(numShapes, shapeImgName, "",
                20, 20, 50, 50);

        currentPage.addShape(shape);
        // System.out.println("Adding shape.");

        editorView.renderShape(shape);
        //editorView.drawPage();
    }

    /**
     * TODO: Makes shape appear on screen
     */
    private void makePopUp() {

    }

    /**
     * TODO: Deletes shape from screen
     */
    private void deleteShape() {

    }

    /**
     * Deletes selected page from internal data structures and external view
     */
    private void deletePageDialog() {
        // Gets names of all pages
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
                // Removes correct page from pages
                String pageName = pageNames[selection];
                String uniqueID = displayNameToID.get(pageName);
                pages.remove(uniqueID);
                // TODO: update custom view to reflect this
                // TODO: figure out starter page
                Toast pageNameToast = Toast.makeText(getApplicationContext(), pageName + " Deleted",Toast.LENGTH_SHORT);
                pageNameToast.show();
            }
        });
        deleteShapePrompt.show();
    }

    /**
     * Takes in user input to rename page in internal structures and external view
     */
    private void renamePageDialog() {
        AlertDialog.Builder renamePagePrompt = new AlertDialog.Builder(this);
        renamePagePrompt.setTitle("Input New Name For Page: ");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        renamePagePrompt.setView(input);
        // Enables add option
        renamePagePrompt.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newPageName = input.getText().toString();
                renamePage(newPageName);
            }
        });
        // Enables cancel option
        renamePagePrompt.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        renamePagePrompt.show();
    }

    /**
     * TODO: Helper method to rename page internally
     * @param newName the new name of the page
     */
    private void renamePage(String newName) {
        //ArrayList<Shape> page = pages.remove(currPage);
        //pages.put(newName, page);
    }

    /**
     * Prompts user to input a new name for the shape
     */
    private void renameShapeDialog() {
        AlertDialog.Builder renameShapePrompt = new AlertDialog.Builder(this);
        renameShapePrompt.setTitle("Input New Name For Shape: ");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        renameShapePrompt.setView(input);
        // Enables saving new name
        renameShapePrompt.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newPageName = input.getText().toString();
                renameShape(newPageName);
            }
        });
        // Cancels save
        renameShapePrompt.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        renameShapePrompt.show();
    }

    /**
     * TODO: Helper method to rename shape internally
     * @param newName the new name of the shape
     */
    private void renameShape(String newName) {

    }

    /**
     * Prompts the user to either load a new game or load an existing game
     */
    private void showCustomDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Create a new game or open an existing game?");
        // Creates new game
        dialog.setPositiveButton(
                "Create New Game",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        loadNewGame();
                    }
                });
        // Opens existing game
        dialog.setNegativeButton(
                "Open Existing Game",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        loadExistingGame();
                    }
                });
        dialog.show();
    }

    /**
     * Helper method to add and load new game
     */
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

    /**
     * TODO: complete once database method that returns names of games is complete
     */
    private void loadExistingGame(){
        Database db = Database.getInstance(getApplicationContext());
        //db.loadGame()
    }

    /**
     * TODO: Change this to list the pages so the user can see options
     * Goes to new page based on user input
     */
    private void goToNewPageDialog() {
        ArrayList<String> names = new ArrayList<>();
        // Builds ArrayList of page display names for user to select
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

                // From the selected display name, pull the original page object
                String uniqueID = displayNameToID.get(newPageName);
                switchPages(uniqueID);
            }
        });
        newPagePrompt.show();
    }

    /**
     * Helper method to switch between pages
     * @param pageName the page to switch to
     */
    private void switchPages(String pageName) {
        currPage = pageName;
        Page newPage = pages.get(currPage);
        //editorView.changeCurrentPage(newPage);
    }

    /**
     * Saves current game state into the database.
     */
    public void saveGame(String saveName, HashMap<String, Page> pageMap) {
        Database db = Database.getInstance(getApplicationContext());
        db.saveGame(saveName, pageMap);
    }
}