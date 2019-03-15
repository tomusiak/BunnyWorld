package edu.stanford.cs108.bunnyworld;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.app.*;
import android.content.*;
import android.widget.*;
import android.text.*;
import android.media.*;

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
    private String currPage; // Tracks user-selected name of current page
    private Page currentPage; // Tracks current page being displayed
    private Page starterPage; // Tracks user-selected starter page
    private String currScript;
    private EditorView editorView;
    private Shape copiedShape;
    private ArrayList<String> resources;    // stores list of addable objects
    private Page undoPageDelete; // stores last deleted page
    private Shape undoShapeDelete; // stores last deleted shape
    private Page undoPageAdd;
    private Shape undoShapeAdd;
    private int currScriptSteps;
    private int numScriptAdds;

    private Dialog editShapeDialog;
    private Dialog addShapeDialog;

    String[] resourceFiles = {"carrot", "carrot2", "death", "duck",
            "fire", "mystic", "textbox", "patrick"};
    int[] imageIds = {R.drawable.carrot,
            R.drawable.carrot2,
            R.drawable.death,
            R.drawable.duck,
            R.drawable.fire,
            R.drawable.mystic,
            R.drawable.textbox,
            R.drawable.patrick,};

    private ArrayList<ShapeResource> shapeResources;

    /**
     * Sets up spinners and their respective functions upon creation of page
     * @param savedInstanceState current saved state to be initialized
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        initializeResources();
        initializeEditor();
        showCustomDialog();
    }

    /**
     * Initializes the arraylist of resource file names. This arraylist is then used
     * for the addShape popup view.
     */
    private void initializeResources() {
        shapeResources = new ArrayList<>();
        for(int i = 0; i < imageIds.length; i++) {
            ShapeResource s = new ShapeResource(resourceFiles[i], imageIds[i]);
            shapeResources.add(s);
        }
        resources = new ArrayList<>();
        resources.add("carrot");
        resources.add("carrot2");
        resources.add("death");
        resources.add("duck");
        resources.add("fire");
        resources.add("mystic");
        resources.add("textbox");
        resources.add("patrick");



    }

    /**
     * Generates script given clicked commands
     * Pre-condition: User has gone through the other spinners and clicked sufficient options to generate a valid script
     */
    public void handleScript(View view) {
        // Initializes empty String to add new script
        currScript = "";
        Shape selectedShape = currentPage.getSelected();
        if (selectedShape == null) {
            Toast shapeErrorToast = Toast.makeText(getApplicationContext(), "No Shape Selected", Toast.LENGTH_SHORT);
            shapeErrorToast.show();
        } else {
            currScriptSteps = 0;
            numScriptAdds = 1;
            scriptTriggersDialog();;
        }
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
                currScriptSteps++;
            }
        });
        goToPrompt.show();
    }

    /**
     * Parses and adds "play" + SoundName command to currScript
     */
    private void scriptPlayDialog() {
        // Predetermined list of names of sounds
        final String[] scriptSounds = new String[]{"CarrotCarrotCarrot", "EvilLaugh", "Fire", "Hooray",
                "Munch", "Munching", "Woof"};
        AlertDialog.Builder playPrompt = new AlertDialog.Builder(this);
        playPrompt.setTitle("Select Script Trigger: ");
        playPrompt.setItems(scriptSounds, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selection) {
                    currScript += scriptSounds[selection] + " ";
                    MediaPlayer mp;
                switch(selection) {
                    case 0:
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.carrotcarrotcarrot);
                        mp.start();
                        break;
                    case 1:
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.evillaugh);
                        mp.start();
                        break;
                    case 2:
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.fire);
                        mp.start();
                        break;
                    case 3:
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.hooray);
                        mp.start();
                        break;
                    case 4:
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.munch);
                        mp.start();
                        break;
                    case 5:
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.munching);
                        mp.start();
                        break;
                    case 6:
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.woof);
                        mp.start();
                        break;
                }
                currScriptSteps++;
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
                String name = input.getText().toString();
                if (!shapeNameExists(name)) {
                    Toast nameErrorToast = Toast.makeText(getApplicationContext(), "Shape Name Does Not Exist", Toast.LENGTH_SHORT);
                    nameErrorToast.show();
                    scriptShapeNameDialog();
                } else {
                    currScript += input.getText().toString() + " ";
                    currScriptSteps++;
                }
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

    private Boolean shapeNameExists(String name) {
        for (Page page : pages.values()) {
            for (Shape shape : page.getList()) {
                if (shape.getShapeName().equals(name)) return true;
            }
        }
        return false;
    }

    /**
     * Displays script for selected shape
     */
    private void showScript() {
        Shape selectedShape = currentPage.getSelected();
        if (selectedShape != null) {
            String scriptText = selectedShape.getScript();
            if (scriptText.isEmpty()) {
                Toast scriptEmptyToast = Toast.makeText(getApplicationContext(), "Shape Does Not Have Script", Toast.LENGTH_SHORT);
                scriptEmptyToast.show();
            } else {
                Dialog shapeScriptDialog = new Dialog(this);
                shapeScriptDialog.setTitle(selectedShape.getScript());
                final TextView script = new TextView(this);
                script.setText(scriptText);
                shapeScriptDialog.setContentView(script);
                shapeScriptDialog.show();
            }
        } else {
            // not able to show script because no shape selected
            Toast showErrorToast = Toast.makeText(getApplicationContext(),"No Shape Selected",Toast.LENGTH_SHORT);
            showErrorToast.show();
        }
    }

    private void deleteScript() {
        Shape selectedShape = currentPage.getSelected();
        if (selectedShape != null) {
            selectedShape.setScript("");
        } else {
            Toast selectErrorToast = Toast.makeText(getApplicationContext(), "No Shape Selected", Toast.LENGTH_SHORT);
            selectErrorToast.show();
        }
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
        if (numPages == 1) {
            newPage.setStarterPageStatus(true);
            starterPage = newPage;
        }
        // Updates the current page in the view
        editorView.changeCurrentPage(currentPage);

        undoShapeDelete = null;
        undoShapeAdd = null;

        Toast addPageToast = Toast.makeText(getApplicationContext(),currPage + " Added",Toast.LENGTH_SHORT);
        addPageToast.show();
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
                        numScriptAdds++;
                        scriptActionsDialog();
                    }
                });
        // Enables option to not add more commands
        dialog.setNegativeButton(
                "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (currScriptSteps == numScriptAdds) {
                            currScript = currScript.substring(0, currScript.length() - 1);
                            currScript += ";";
                            Shape selectedShape = currentPage.getSelected();
                            selectedShape.setScript(selectedShape.getScript() + currScript);
                            dialog.cancel();
                        }
                    }
                });
        dialog.show();
    }

    /**
     * Adds shape to the page and tracks it internally
     */
    private void addShape() {
        makePopUp();
    }

    /**
     * Responsible for building the pop-up menu that presents all the possible shapes
     * to add to the editor view.
     */
    private void makePopUp() {
        Dialog dialog = new Dialog(EditorActivity.this);
        addShapeDialog = dialog;
        dialog.setContentView(R.layout.shapeadder_popup);

        GridView grid = (GridView) dialog.findViewById(R.id.resource_gridview);
        final GridViewAdapter adapter = new GridViewAdapter(EditorActivity.this, shapeResources);
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                    ShapeResource resource = (ShapeResource) adapter.getItem(position);
                    if(resource != null) {
                        addShapeToEditor(resource.getShapeName());
                        addShapeDialog.dismiss();
                    }
                }
            });

        dialog.show();
    }

    /**
     * Called from makePopUp(), which presents the user with a pop-up view showing
     * their options for shapes to add to the screen. This adds the shape to the
     * Page and renders it on screen.
     * @param selection string of the shape to be rendered
     */
    public void addShapeToEditor(String selection) {
        numShapes++;
        String shapeName = "shape" + numShapes;
        Toast addToast = Toast.makeText(getApplicationContext(),shapeName + " Added",Toast.LENGTH_SHORT);
        addToast.show();

        // Sample: Building a shape from a shape image name
        String shapeImgName = selection;
        Shape shape;
        if (shapeImgName.equals("textbox")) {
            shape = new Shape(numShapes, shapeImgName, shapeName,
                    20, 20, 250, 48 );
            shape.setIsText(true);
            shape.setText("Text");
        } else {
            shape = new Shape(numShapes, shapeImgName, "",
                    20, 20, 250, 250);
        }

        currentPage.addShape(shape);
        editorView.renderShape(shape);  // renders the bitmaps for the newly added shape
        undoShapeAdd = shape;
    }

    /**
     * Deletes the selected shape on the canvas. Displays helpful toasts based
     * upon success of deletion.
    */
    private void deleteShape() {
        Shape selectedShape = currentPage.getSelected();
        if (selectedShape != null) {
            undoShapeDelete = selectedShape;
            if (copiedShape != null && copiedShape.equals(selectedShape)) copiedShape = null;
            if (undoShapeAdd != null && undoShapeAdd.equals(selectedShape)) undoShapeAdd = null;
            editorView.deleteShape();

            // EXTENSION: Delete references to that deleted shape in scripts of all other shapes

            // Iterate through all pages and Shapes in those pages
            Iterator it = pages.values().iterator();
            while (it.hasNext()) {
                Page page = (Page) it.next();
                ArrayList<Shape> shapes = page.getList();
                for (int i = 0; i < shapes.size(); i++) {
                    // Breaks script into clauses
                    String script = shapes.get(i).getScript().toLowerCase();
                    String[] clauses = script.split(";");
                    // If clause contains that Shape name, delete clause
                    for (int j = 0; j < clauses.length; j++) {
                        if (clauses[i].contains(selectedShape.getShapeName())) {
                            clauses[i] = "";
                        }
                    }
                    // Reassemble script, not including deleted clauses
                    String newScript = "";
                    for (int k = 0; k < clauses.length; k++) {
                        if (!clauses[k].equals("")) {
                            newScript += clauses[k];
                        }
                    }
                    // Set script of that Shape to that newly re-assembled script
                    shapes.get(i).setScript(newScript);
                }
            }

            Toast addToast = Toast.makeText(getApplicationContext(),"Shape Successfully Deleted",Toast.LENGTH_SHORT);
            addToast.show();
            //numShapes--;
        } else {
            // show toast message if deletion did not work
            Toast addToast = Toast.makeText(getApplicationContext(),"No Shape Selected",Toast.LENGTH_SHORT);
            addToast.show();
        }
    }

    /**
     * Copies selected Shape and associated properties
     */
    private void copyShape() {
        Shape selectedShape = currentPage.getSelected();
        if (selectedShape == null) {
            Toast shapeSelectedError = Toast.makeText(getApplicationContext(), "No Shape Selected", Toast.LENGTH_SHORT);
            shapeSelectedError.show();
        } else {
            copiedShape = selectedShape;
            Toast copiedShapeToat = Toast.makeText(getApplicationContext(), copiedShape.getShapeName() + " copied to clipboard.", Toast.LENGTH_SHORT);
            copiedShapeToat.show();
        }
    }

    /**
     * Pastes the currently copied Shape and associated properties
     * Error if there is no currently copied Shape
     */
    private void pasteShape() {
        if (copiedShape == null) {
            Toast shapeCopyError = Toast.makeText(getApplicationContext(), "No Shape Copied", Toast.LENGTH_SHORT);
            shapeCopyError.show();
        } else {
            String selection = copiedShape.getImageName();
            addShapeToEditor(selection);
        }
    }

    /**
     * Undo functionality to undo Shape deletion
     */
    private void undoShapeDelete() {
        if (undoShapeDelete == null) {
            Toast undoErrorToast = Toast.makeText(getApplicationContext(), "Can Only Undo Most Recent Shape Deletion", Toast.LENGTH_SHORT);
            undoErrorToast.show();
        } else {
            currentPage.addShape(undoShapeDelete);
            editorView.renderShape(undoShapeDelete);  // renders the bitmaps for the newly added shape
            undoShapeDelete = null;
            Toast undoShapeToast = Toast.makeText(getApplicationContext(), "Deleted Shape Added Back to Game", Toast.LENGTH_SHORT);
            undoShapeToast.show();
        }
    }

    /**
     * Undo functionality for most recent shape added to page
     */
    private void undoShapeAdd() {
        if (undoShapeAdd == null) {
            Toast undoErrorToast = Toast.makeText(getApplicationContext(), "Can Only Undo Most Recent Shape Addition", Toast.LENGTH_SHORT);
            undoErrorToast.show();
        } else {
            if (copiedShape != null && copiedShape.equals(undoShapeAdd)) copiedShape = null;
            currentPage.removeShape(undoShapeAdd);
            editorView.renderBitmaps(currentPage);
            numShapes--;
            undoShapeAdd = null;
            Toast undoShapeToast = Toast.makeText(getApplicationContext(), "Shape Addition Undone", Toast.LENGTH_SHORT);
            undoShapeToast.show();
        }
    }

    /**
     * Deletes selected page from internal data structures and external view
     */
    private void deletePageDialog() {
        // Gets names of all pages
        ArrayList<String> names = new ArrayList<>();
        names.addAll(getPages().keySet());
        final String[] pageNames = names.toArray(new String[getPages().size()]);
        AlertDialog.Builder deletePagePrompt = new AlertDialog.Builder(this);
        deletePagePrompt.setTitle("Page To Delete: ");
        deletePagePrompt.setItems(pageNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selection) {
                // Removes correct page from pages
                String pageName = pageNames[selection];
                String uniqueID = displayNameToID.get(pageName);
                Page deletedPage = getPages().get(uniqueID);
                if (deletedPage.equals(starterPage)) {
                    Toast deleteErrorToast = Toast.makeText(getApplicationContext(), "Unable to Delete Starter Page",Toast.LENGTH_SHORT);
                    deleteErrorToast.show();
                } else {
                    undoPageDelete = deletedPage;
                    getPages().remove(uniqueID);
                    displayNameToID.remove(pageName);
                    if (deletedPage.equals(currentPage)) editorView.changeCurrentPage(starterPage);
                    Toast pageNameToast = Toast.makeText(getApplicationContext(), pageName + " Deleted",Toast.LENGTH_SHORT);
                    pageNameToast.show();
                }
            }
        });
        deletePagePrompt.show();
    }

    /**
     * Prompts the user to select which page (of the created pages) to rename
     */
    private void selectPageToRenameDialog() {
        ArrayList<String> names = new ArrayList<>();
        names.addAll(getPages().keySet());
        final String[] pageNames = names.toArray(new String[getPages().size()]);
        AlertDialog.Builder pageToRenamePrompt = new AlertDialog.Builder(this);
        pageToRenamePrompt.setTitle("Page To Rename: ");
        pageToRenamePrompt.setItems(pageNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selection) {
                String pageName = pageNames[selection];
                String uniqueID = displayNameToID.get(pageName);
                renamePageDialog(uniqueID, pageNames);
            }
        });
        pageToRenamePrompt.show();
    }

    /**
     * Takes in user input to rename page in internal structures and external view
     */
    private void renamePageDialog(final String uniqueID, final String[] pageNames) {
        final ArrayList<String> pageList = new ArrayList<>(Arrays.asList(pageNames));
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
                newPageName = newPageName.toLowerCase();
                if (pageList.contains(newPageName)) {
                    Toast pageRenameError = Toast.makeText(getApplicationContext(), "Page Name Already Used", Toast.LENGTH_SHORT);
                    pageRenameError.show();
                    renamePageDialog(uniqueID, pageNames);
                } else {
                    renamePage(newPageName, uniqueID);
                }
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
     * Helper method to rename page internally
     * @param newName the new name of the page
     */
    private void renamePage(String newName, String uniqueID) {
        Page page = getPages().remove(uniqueID);
        page.changeDisplayName(newName);
        getPages().put(newName, page);
    }

    /**
     * Prompts the user to select which page to change the background of -- hands off to
     * backgroundDialog() once a page is selected
     */
    private void changePageBackground() {
        ArrayList<String> names = new ArrayList<>();
        names.addAll(pages.keySet());
        final String[] pageNames = names.toArray(new String[pages.size()]);
        AlertDialog.Builder pageChangePrompt = new AlertDialog.Builder(this);
        pageChangePrompt.setTitle("Page To Change Background Of: ");
        pageChangePrompt.setItems(pageNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selection) {
                String pageName = pageNames[selection];
                String uniqueID = displayNameToID.get(pageName);
                backgroundDialog(uniqueID);
            }
        });
        pageChangePrompt.show();
    }

    /**
     * Prompts the user to select which background to set for page -- hands off to changeBackground()
     */
    private void backgroundDialog(final String uniqueID) {
        final String[] backgroundList = new String[]{"coffeeshop",
                "gates", "moon", "mounteverest",
                "egyptianpyramid", "molly", "nobackground"};
        final String[] backgroundFiles = new String[]{"coffeeshop",
                "gates", "moon", "mounteverest",
                "egyptianpyramid", "molly"};
        AlertDialog.Builder playPrompt = new AlertDialog.Builder(this);
        playPrompt.setTitle("Select Background: ");
        playPrompt.setItems(backgroundList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selection) {
                String selectedBackground = backgroundList[selection];
                changeBackground(uniqueID, selectedBackground);
            }
        });
        playPrompt.show();
    }

    /**
     * Changes the background of the parameterized page associated with the uniqueID
     */
    private void changeBackground(String uniqueID, String background) {

        currentPage.changeBackground(background);
        editorView.renderBitmaps(currentPage);

        Toast backgroundToast = Toast.makeText(getApplicationContext(), "Background Changed", Toast.LENGTH_SHORT);
        backgroundToast.show();
    }

    /**
     * Allows user to select starter page for their game
     */
    private void selectStarterPage() {
        ArrayList<String> names = new ArrayList<>();
        names.addAll(pages.keySet());
        final String[] pageNames = names.toArray(new String[pages.size()]);
        AlertDialog.Builder starterPagePrompt = new AlertDialog.Builder(this);
        starterPagePrompt.setTitle(starterPage.getDisplayName() + " Is Current Starter Page. Select New Starter Page: ");
        starterPagePrompt.setItems(pageNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selection) {
                String pageName = pageNames[selection];
                String uniqueID = displayNameToID.get(pageName);
                // Uses unique internal ID to track page in case name is changed
                Page selectedPage = pages.get(uniqueID);
                for (Page page : pages.values()) {
                    if (page.getStarterPageStatus()) page.setStarterPageStatus(false);
                }
                starterPage = selectedPage;
                selectedPage.setStarterPageStatus(true);
            }
        });
        starterPagePrompt.show();
    }

    /**
     * Undo support for page deletion
     */
    private void undoPageDelete() {
        if (undoPageDelete == null) {
            Toast undoPageError = Toast.makeText(getApplicationContext(), "Can Only Undo Most Recent Page Deletion", Toast.LENGTH_SHORT);
            undoPageError.show();
        } else {
            displayNameToID.put(undoPageDelete.getDisplayName(), undoPageDelete.getPageID());
            pages.put(undoPageDelete.getPageID(), undoPageDelete);
            Toast undoPageToast = Toast.makeText(getApplicationContext(),"Deleted Page Added Back To Game",Toast.LENGTH_SHORT);
            undoPageToast.show();
        }
    }

    /**
     * Prompts user to input a new name for the shape
     */
    private void renameShapeDialog() {
        Shape selectedShape = currentPage.getSelected();
        if (selectedShape == null) {
            Toast shapeSelectedError = Toast.makeText(getApplicationContext(), "No Shape Selected", Toast.LENGTH_SHORT);
            shapeSelectedError.show();
            return;
        }
        AlertDialog.Builder renameShapePrompt = new AlertDialog.Builder(this);
        renameShapePrompt.setTitle("Input New Name For Shape: ");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        renameShapePrompt.setView(input);
        // Enables saving new name
        renameShapePrompt.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newShapeName = input.getText().toString();
                newShapeName = newShapeName.toLowerCase();
                ArrayList<Shape> shapes = currentPage.getShapes();
                // checks to make sure shape name not already used; if used, prompts user to re-enter name
                if (shapeNameExists(newShapeName)) {
                    Toast shapeRenameError = Toast.makeText(getApplicationContext(), "Shape Name Already Exists", Toast.LENGTH_SHORT);
                    shapeRenameError.show();
                } else {
                    renameShape(newShapeName);
                }
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
     * Helper method to rename shape internally
     * @param newName the new name of the shape
     */
    private void renameShape(String newName) {
        Shape selectedShape = currentPage.getSelected();
        String name = newName.toLowerCase();
        selectedShape.setShapeName(name);
    }

    /**
     * Dialog that pops up that allows user to change the properties of the selected shape
     */
    private void editShapeDialog() {
        Dialog dialog = new Dialog(this);
        editShapeDialog = dialog;
        dialog.setContentView(R.layout.shape_properties);
        final Shape selectedShape = currentPage.getSelected();
        // if a shape is selected, dialog with properties pops up; if not, user is alerted of problem
        if(selectedShape != null) {
            populateEditShapeDialog(selectedShape, dialog);
            EditText shapeName = dialog.findViewById(R.id.nameInput);
            shapeName.setText(selectedShape.getShapeName());
            dialog.show();
        } else {
            // show toast message if there is no shape selected
            Toast addToast = Toast.makeText(getApplicationContext(),"No Shape Selected",Toast.LENGTH_SHORT);
            addToast.show();

        }
    }

    /**
     * Populates the EditText views in the editShapeDialog with the existing properties of the shape
     */
    private void populateEditShapeDialog(Shape shape, Dialog dialog) {
        EditText xInput = dialog.findViewById(R.id.xInput);
        xInput.setText(Double.toString(shape.getX()));

        EditText yInput = dialog.findViewById(R.id.yInput);
        yInput.setText(Double.toString(shape.getY()));

        EditText widthInput = dialog.findViewById(R.id.widthInput);
        widthInput.setText(Double.toString(shape.getWidth()));

        EditText heightInput = dialog.findViewById(R.id.heightInput);
        heightInput.setText(Double.toString(shape.getHeight()));

        EditText shapeName = dialog.findViewById(R.id.nameInput);
        shapeName.setText(shape.getShapeName());

        CheckBox moveInput = dialog.findViewById(R.id.moveInput);
        moveInput.setChecked(shape.getMoveableStatus());

        CheckBox visibleInput = dialog.findViewById(R.id.visibleInput);
        visibleInput.setChecked(!shape.isHidden());

        EditText imageNameInput = dialog.findViewById(R.id.imageNameInput);
        imageNameInput.setText(shape.getImageName());

        EditText textInput = dialog.findViewById(R.id.textInput);
        textInput.setText(shape.getText());

        TextView scriptText = dialog.findViewById(R.id.scriptText);
        scriptText.setText(shape.getScript());

    }

    /**
     * Helper method that, on click, updates shape properties
     * @param view
     */
   public void editShapeProperties(View view) {

       Shape shape = currentPage.getSelected();

       EditText xInput = editShapeDialog.findViewById(R.id.xInput);
       double x = Double.parseDouble(xInput.getText().toString());
       shape.setX(x);

       EditText yInput = editShapeDialog.findViewById(R.id.yInput);
       String yText = yInput.getText().toString();
       shape.setY(Double.parseDouble(yText));

       EditText widthInput = editShapeDialog.findViewById(R.id.widthInput);
       int width = (int)Double.parseDouble(widthInput.getText().toString());
       shape.setWidth(width);

       EditText heightInput = editShapeDialog.findViewById(R.id.heightInput);
       int height = (int)Double.parseDouble(heightInput.getText().toString());
       shape.setHeight(height);

       EditText shapeName = editShapeDialog.findViewById(R.id.nameInput);
       String name = shapeName.getText().toString();
       ArrayList<Shape> shapes = currentPage.getShapes();
       if (shapeNameExists(name)) {
           Toast shapeRenameError = Toast.makeText(getApplicationContext(), "Shape Name Already Exists", Toast.LENGTH_SHORT);
           shapeRenameError.show();
       } else {
           name = name.toLowerCase();
           shape.setShapeName(name);
       }

       CheckBox moveInput = editShapeDialog.findViewById(R.id.moveInput);
       boolean isMovable = moveInput.isChecked();
       shape.setMoveable(isMovable);

       CheckBox visibleInput = editShapeDialog.findViewById(R.id.visibleInput);
       boolean isVisible = visibleInput.isChecked();
       shape.setHidden(!isVisible);

       EditText imageNameInput = editShapeDialog.findViewById(R.id.imageNameInput);
       String imageNameText = imageNameInput.getText().toString();
       if (resources.contains(imageNameText)) {
           shape.setImageName(imageNameText);
       } else {
           Toast invalidNameToast = Toast.makeText(getApplicationContext(), "Invalid Image Name", Toast.LENGTH_SHORT);
           invalidNameToast.show();
       }

       EditText textInput = editShapeDialog.findViewById(R.id.textInput);
       String textText = textInput.getText().toString();
       shape.setText(textText);

       // if the text is empty string, we want to mark isText false
       if(textText.equals("")) {
           shape.setIsText(false);
       }

       editorView.renderShape(shape);

       editShapeDialog.dismiss();

   }

    /**
     * Prompts the user to either load a new game or load an existing game
     */
    private void showCustomDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Create a new game or open an existing game?");
        dialog.setPositiveButton("Create New Game", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                loadNewGame();
            }
        });
        dialog.setNegativeButton("Open Existing Game", new DialogInterface.OnClickListener() {
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
        copiedShape = null;
        pages = new HashMap<>();
        displayNameToID = new HashMap<>();
        addPage();
        Toast addToast = Toast.makeText(getApplicationContext(),currPage + " Added",Toast.LENGTH_SHORT);
        addToast.show();
    }

    /** Loads an existing game.
     */
    private void loadExistingGame(){
        final Database db = Database.getInstance(getApplicationContext()); // Loads database.
        editorView = findViewById(R.id.editorView);
        setContentView( R.layout.database_load ); // Shifts into loading view.
        ListView listView = findViewById( R.id.list_view );
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // Activates when user chooses a save.
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String product = ((TextView) view).getText().toString();
                setPages(db.loadGame(product)); // Loads game into pages.
                numShapes = db.getShapeCount(product);
                numPages = db.getPageCount( product );
                displayNameToID = new HashMap<String, String>();
                String startPage = null;
                for (String key : getPages().keySet()) { // Finds which page is the starter page, and then makes that the first page to go to.
                    Page currentPage = getPages().get(key);
                    String pageName = currentPage.getDisplayName();
                    displayNameToID.put(key,pageName);
                    if (currentPage.getStarterPageStatus() == true) {
                        startPage = key;
                        starterPage = currentPage;
                    }
                }
                initializeEditor(); // Makes editor features visible, shifts back into EditorActivity.
                editorView.changeCurrentPage(pages.get(startPage)); // Goes to first page.
            }
        });
        String[] gameList = db.returnGameList().toArray( new String[0] ); // Shows list of saved games.
        if (gameList != null) {
             ArrayAdapter<String> itemsAdapter =
                     new ArrayAdapter<String>( EditorActivity.this, android.R.layout.test_list_item, gameList );
             listView = (ListView) findViewById( R.id.list_view );
             if (listView != null) {
                 listView.setAdapter( itemsAdapter );
             }
        }
    }

    /**
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
        String uniqueID = displayNameToID.get(pageName);
        Page nextPage = pages.get(uniqueID);
        currentPage = nextPage;
        undoShapeAdd = null;
        undoShapeDelete = null;
        editorView.changeCurrentPage(nextPage);
    }

    /** Displays current list of games in a list view.
     */
    public void saveGame(View view) {
        Database db = Database.getInstance(getApplicationContext());
        String[] gameList = db.returnGameList().toArray(new String[0]);
        setContentView( R.layout.database_popup ); // Goes into popup view.
        if (gameList != null) { // Shows list of games.
            ArrayAdapter<String> itemsAdapter =
                    new ArrayAdapter<String>( EditorActivity.this, android.R.layout.test_list_item, gameList );
            ListView listView = (ListView) findViewById(R.id.list_view );
            if (listView != null) {
                listView.setAdapter( itemsAdapter );
            }
        }
    }

    /** Upon clicking the 'save' button, saves the current game into the database and returns.
     */
    public void confirmSave(View view) {
        Database db = Database.getInstance(getApplicationContext());
        TextView textView = findViewById(R.id.edit_text);
        String text = textView.getText().toString();
        String[] gameList = db.returnGameList().toArray(new String[0]);
        if (gameList.length > 11) { // Ensures list of saves does not run off screen.
            Toast toast = Toast.makeText(this,"Please delete a save before adding more.",Toast.LENGTH_SHORT);
            toast.show();
        } else if (numShapes == 0) { // Checks that at least one shape has been added.
            Toast toast = Toast.makeText(this,"Please add at least one shape before saving.",Toast.LENGTH_SHORT);
            toast.show();
        } else {
            db.saveGame(text,getPages()); // Saves shapes into database.
            gameList = db.returnGameList().toArray(new String[0]);
            if (gameList != null) { // Updates appearance of games in list.
                ArrayAdapter<String> itemsAdapter =
                        new ArrayAdapter<String>( EditorActivity.this, android.R.layout.test_list_item, gameList );
                ListView listView = (ListView) findViewById(R.id.list_view );
                if (listView != null) {
                    listView.setAdapter( itemsAdapter );
                }
            }
        }
        if (gameList != null) { // Updates appearance of games in list.
            ArrayAdapter<String> itemsAdapter =
                    new ArrayAdapter<String>( EditorActivity.this, android.R.layout.test_list_item, gameList );
            ListView listView = (ListView) findViewById(R.id.list_view );
            if (listView != null) {
                listView.setAdapter( itemsAdapter );
            }
        }
    }

    /** Empties all records in the database.
     */
    public void clearDatabase(View view) {
        Database db = Database.getInstance(getApplicationContext());
        db.clear(); // Clears database by dropping the main table and re-creating it.
        String[] gameList = db.returnGameList().toArray(new String[0]);
        if (gameList != null) { // If gameList isn't empty, refreshes games list.
            ArrayAdapter<String> itemsAdapter =
                    new ArrayAdapter<String>( EditorActivity.this, android.R.layout.test_list_item, gameList );
            ListView listView = (ListView) findViewById(R.id.list_view );
            if (listView != null) {
                listView.setAdapter( itemsAdapter );
            }
        }
    }

    /** Renames a chosen save to a new value.
     */
    public void renameSave(View view) {
        final Database db = Database.getInstance(getApplicationContext());
        final String[] gameList = db.returnGameList().toArray(new String[0]); // Obtains list of games.
        TextView textView = findViewById(R.id.edit_text);
        final String text = textView.getText().toString();
        AlertDialog.Builder renameSavePrompt = new AlertDialog.Builder(this); // Prompts user for which game they would like to re-name.
        renameSavePrompt.setTitle("Which save would you like to rename? ");
        renameSavePrompt.setItems(gameList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selection) {
                if (Arrays.asList(gameList).contains(text)) { // Ensures no save name duplicates (breaks database).
                    Toast addPageToast = Toast.makeText(getApplicationContext(),"Choose a different name.",Toast.LENGTH_SHORT);
                    addPageToast.show();
                } else {
                    db.updateGameName( gameList[selection], text );
                }
                String[] newGameList = db.returnGameList().toArray(new String[0]);
                if (newGameList != null) {
                    ArrayAdapter<String> itemsAdapter =
                            new ArrayAdapter<String>( EditorActivity.this, android.R.layout.test_list_item, newGameList );
                    ListView listView = (ListView) findViewById(R.id.list_view );
                    if (listView != null) {
                        listView.setAdapter( itemsAdapter );
                    }
                }
            }
        });
        renameSavePrompt.show();
    }

    /** Deletes a save from the database.
     */
    public void deleteSave(View view) {
        final Database db = Database.getInstance(getApplicationContext());
        final String[] gameList = db.returnGameList().toArray(new String[0]);
        AlertDialog.Builder deleteSavePrompt = new AlertDialog.Builder(this);
        deleteSavePrompt.setTitle("Which save would you like to delete? ");
        deleteSavePrompt.setItems(gameList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selection) {
                db.deleteSave(gameList[selection]);
                String[] newGameList = db.returnGameList().toArray(new String[0]);
                if (newGameList != null) {
                    ArrayAdapter<String> itemsAdapter =
                            new ArrayAdapter<String>( EditorActivity.this, android.R.layout.test_list_item, newGameList );
                    ListView listView = (ListView) findViewById(R.id.list_view );
                    if (listView != null) {
                        listView.setAdapter( itemsAdapter );
                    }
                }
            }
        });
        deleteSavePrompt.show();
    }

    /** Exits back to editor activity.
     */
    public void exit(View view) {
        Database db = Database.getInstance(getApplicationContext());
        initializeEditor();
        db.autoSave(getPages()); // Autosaves in case someone did not mean to lose all of their data.\
    }

    /** Resets editor activity.
     */
    public void initializeEditor() {
        setContentView(R.layout.activity_editor);
        initializeResources();
        // Initializes Spinner for page options
        final Spinner pageSpinner = findViewById(R.id.pageSpinner);
        String[] pageOptions = new String[]{"Page Options:", "Create Page", "Rename Page", "Delete Page",
                "Open Page", "Change Background",  "Change Starter Page", "Undo Delete"};
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
                        selectPageToRenameDialog();
                        break;
                    case 3:
                        deletePageDialog();
                        break;
                    case 4:
                        goToNewPageDialog();
                        break;
                    case 5:
                        changePageBackground();
                        break;
                    case 6:
                        selectStarterPage();
                    case 7:
                        undoPageDelete();
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
        String[] shapeOptions = new String[]{"Shape Options:", "Add Shape", "Rename Shape", "Edit Shape",
                "Delete Shape", "Copy Shape", "Paste Shape", "Undo Delete", "Undo Add"};
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
                        editShapeDialog();
                        break;
                    case 4:
                        deleteShape();
                        break;
                    case 5:
                        copyShape();
                        break;
                    case 6:
                        pasteShape();
                        break;
                    case 7:
                        undoShapeDelete();
                        break;
                    case 8:
                        undoShapeAdd();
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
        String[] scriptOptions = new String[]{"Script Options:", "Create Script", "Show Script", "Delete Script"};
        ArrayAdapter<String> scriptAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, scriptOptions);
        scriptSpinner.setAdapter(scriptAdapter);
        scriptSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        break;
                    case 1:
                        handleScript(view);
                        break;
                    case 2:
                        showScript();
                        break;
                    case 3:
                        deleteScript();
                        break;
                }
                scriptSpinner.setSelection(0);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
        editorView = findViewById(R.id.editorView);
        if (pages != null) {
            String startPage = null;
            for (String key : getPages().keySet()) {
                Page currentPage = getPages().get(key);
                String pageName = currentPage.getDisplayName();
                displayNameToID.put(key,pageName);
                if (currentPage.getStarterPageStatus() == true) {
                    startPage = key;
                    starterPage = currentPage;
                }
            }
            editorView.changeCurrentPage(pages.get(startPage));
        }
    }

    /** Helper method to get pages
     * @return pages
     */
    private HashMap<String, Page> getPages() {
        return pages;
    }

    /** Helper setter method for pages.
     */
    private void setPages(HashMap<String,Page> newPages) {
        pages = newPages;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Database db = Database.getInstance(getApplicationContext());
        if (pages != null) {
            db.autoSave( pages );
        }
        Toast pageRenameError = Toast.makeText(getApplicationContext(), "Auto-saved!", Toast.LENGTH_SHORT);
        pageRenameError.show();
    }
}