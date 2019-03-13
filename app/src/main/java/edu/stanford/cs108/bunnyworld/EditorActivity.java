package edu.stanford.cs108.bunnyworld;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

    /**
     * Sets up spinners and their respective functions upon creation of page
     * @param savedInstanceState current saved state to be initialized
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        initializeResources();

        // Initializes Spinner for page options
        final Spinner pageSpinner = findViewById(R.id.pageSpinner);
        String[] pageOptions = new String[]{"Page Options:", "Create Page", "Rename Page", "Delete Page", "Open Page", "Change Background", "Change Starter Page"};
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
        String[] shapeOptions = new String[]{"Shape Options:", "Add Shape", "Rename Shape", "Edit Shape", "Delete Shape", "Copy Shape", "Paste Shape"};
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
                        showScript();
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
     * Initializes the arraylist of resource file names. This arraylist is then used
     * for the addShape popup view.
     */
    private void initializeResources() {
        resources = new ArrayList<>();
        resources.add("carrot");
        resources.add("carrot2");
        resources.add("death");
        resources.add("duck");
        resources.add("fire");
        resources.add("mystic");

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
     * Displays script for selected shape
     */
    private void showScript() {
        Shape selectedShape = currentPage.getSelected();
        if (selectedShape != null) {
            Dialog shapeScriptDialog = new Dialog(this);
            shapeScriptDialog.setTitle(selectedShape.getScript());
        } else {
            // not able to show script because no shape selected
            Toast addToast = Toast.makeText(getApplicationContext(),"No shape was selected.",Toast.LENGTH_SHORT);
            addToast.show();
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
     * Adds shape to the page and tracks it internally
     */
    private void addShape() {
        String selection = makePopUp();

        numShapes++;
        String shapeName = "shape" + numShapes;
        Toast addToast = Toast.makeText(getApplicationContext(),shapeName + " Added",Toast.LENGTH_SHORT);
        addToast.show();

        // Sample: Building a shape from a shape image name
        String shapeImgName = selection;
        Shape shape = new Shape(numShapes, shapeImgName, "",
                20, 20, 50, 50);

        currentPage.addShape(shape);
        editorView.renderShape(shape);  // renders the bitmaps for the newly added shape
    }

    /**
     * TODO: Makes shape appear on screen
     */
    private String makePopUp() {
        String selection = "carrot";
        AddShapeDialog makeShape = new AddShapeDialog();
        makeShape.show(getSupportFragmentManager(), "make shape");

        PopupView shapesPanel = findViewById(R.id.shapeSelect);
        //if(shapesPanel != null) shapesPanel.populateResources(resources);

        return selection;
    }

    /**
     * Deletes the selected shape on the canvas. Displays helpful toasts based
     * upon success of deletion.
    */
    private void deleteShape() {
        if (editorView.deleteShape()) {
            Toast addToast = Toast.makeText(getApplicationContext(),"Shape Successfully Deleted.",Toast.LENGTH_SHORT);
            addToast.show();
        } else {
            // show toast message if deletion did not work
            Toast addToast = Toast.makeText(getApplicationContext(),"No shape was selected.",Toast.LENGTH_SHORT);
            addToast.show();
        }
    }

    private void copyShape() {
        Shape selectedShape = currentPage.getSelected();
        if (selectedShape == null) {
            Toast shapeSelectedError = Toast.makeText(getApplicationContext(), "No Shape Selected", Toast.LENGTH_SHORT);
            shapeSelectedError.show();
        } else {
            copiedShape = selectedShape;
        }
    }

    private void pasteShape() {
        if (copiedShape == null) {
            Toast shapeCopyError = Toast.makeText(getApplicationContext(), "No Shape Copied", Toast.LENGTH_SHORT);
            shapeCopyError.show();
        } else {
            numShapes++;
            String shapeName = "shape" + numShapes;
            Toast addToast = Toast.makeText(getApplicationContext(),shapeName + " Added",Toast.LENGTH_SHORT);
            addToast.show();

            String selection = "carrot";
            Shape newShape = new Shape(numShapes, selection, "",
                    20, 20, 50, 50);

            currentPage.addShape(newShape);
            editorView.renderShape(newShape);
        }
    }

    /**
     * Deletes selected page from internal data structures and external view
     */
    private void deletePageDialog() {
        // Gets names of all pages
        ArrayList<String> names = new ArrayList<>();
        names.addAll(pages.keySet());
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
                displayNameToID.remove(pageName);
                // TODO: update custom view to reflect this
                // TODO: figure out starter page
                Toast pageNameToast = Toast.makeText(getApplicationContext(), pageName + " Deleted",Toast.LENGTH_SHORT);
                pageNameToast.show();
            }
        });
        deleteShapePrompt.show();
    }

    // prompts the user to select which page (of the created pages) to rename
    private void selectPageToRenameDialog() {
        ArrayList<String> names = new ArrayList<>();
        names.addAll(pages.keySet());
        final String[] pageNames = names.toArray(new String[pages.size()]);
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
        Page page = pages.remove(uniqueID);
        page.changeDisplayName(newName);
        pages.put(newName, page);
    }

    /**
     * Prompts the user to select which page to change the background of -- hands off to
     * backgroundDialog() once a page is selected
     */
    private void changePageBackground() {
        ArrayList<String> names = new ArrayList<>();
        names.addAll(pages.keySet());
        final String[] pageNames = names.toArray(new String[pages.size()]);
        AlertDialog.Builder pageToRenamePrompt = new AlertDialog.Builder(this);
        pageToRenamePrompt.setTitle("Page To Change Background Of: ");
        pageToRenamePrompt.setItems(pageNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selection) {
                String pageName = pageNames[selection];
                String uniqueID = displayNameToID.get(pageName);
                backgroundDialog(uniqueID);
            }
        });
        pageToRenamePrompt.show();
    }

    /**
     * Prompts the user to select which background to set for page -- hands off to changeBackground()
     */
    private void backgroundDialog(final String uniqueID) {
        final String[] backgroundList = new String[]{"Background1", "Background2", "Background3", "Background4", "Background5", "Background6"};
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

        Toast backgroundToast = Toast.makeText(getApplicationContext(), "Background Changed", Toast.LENGTH_SHORT);
        backgroundToast.show();
    }

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
                ArrayList<Shape> shapes = currentPage.getShapes();
                // checks to make sure shape name not already used; if used, prompts user to re-enter name
                for (int i = 0; i < shapes.size(); i++) {
                    if (shapes.get(i).getShapeName().equals(newShapeName)) {
                        Toast shapeRenameError = Toast.makeText(getApplicationContext(), "Shape Name Already Used On This Page", Toast.LENGTH_SHORT);
                        shapeRenameError.show();
                        renameShapeDialog();
                        return;
                    }
                }
                renameShape(newShapeName);
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
        selectedShape.setShapeName(newName);
    }

    /**
     * Dialog that pops up that allows user to change the properties of the selected shape
     */
    private void editShapeDialog() {
        Dialog dialog = new Dialog(this);
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
            Toast addToast = Toast.makeText(getApplicationContext(),"No shape was selected.",Toast.LENGTH_SHORT);
            addToast.show();

        }
    }

    /**
     * Populates the EditText views in the editShapeDialog with the existing properties of the shape
     */
    private void populateEditShapeDialog(Shape shape, Dialog dialog) {
        EditText topInput = dialog.findViewById(R.id.topInput);
        topInput.setText(Double.toString(shape.getTop()));
        EditText bottomInput = dialog.findViewById(R.id.bottomInput);
        bottomInput.setText(Double.toString(shape.getBottom()));
        EditText leftInput = dialog.findViewById(R.id.leftInput);
        leftInput.setText(Double.toString(shape.getLeft()));
        EditText rightInput = dialog.findViewById(R.id.rightInput);
        rightInput.setText(Double.toString(shape.getRight()));
        EditText shapeName = dialog.findViewById(R.id.nameInput);
        shapeName.setText(shape.getShapeName());
        CheckBox moveInput = dialog.findViewById(R.id.moveInput);
        moveInput.setEnabled(shape.getMoveableStatus());
        CheckBox visibleInput = dialog.findViewById(R.id.visibleInput);
        visibleInput.setEnabled(!shape.getHiddenStatus());
    }

    /**
     * TODO: Helper method that, on click, updates shape properties
     * @param view
     */
   public void editShapeProperties(View view) {

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

    /**
     * TODO: complete once database method that returns names of games is complete
     */
    private void loadExistingGame(){
        final Database db = Database.getInstance(getApplicationContext());
        editorView = findViewById(R.id.editorView);
        setContentView( R.layout.database_load );
        ListView listView = findViewById( R.id.list_view );
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String product = ((TextView) view).getText().toString();
                Toast pageNameToast = Toast.makeText(getApplicationContext(),product,Toast.LENGTH_SHORT);
                pageNameToast.show();
                setPages(db.loadGame(product));
                String firstPage = db.returnFirstPage( product );
                Page page = getPages().get(firstPage);
                if (editorView != null && page != null) {
                    editorView.changeCurrentPage( page );
                }
                returnHome();
            }
        });
        String[] gameList = db.returnGameList().toArray( new String[0] );
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
        Page nextPage = pages.get(currPage);
        editorView.changeCurrentPage(nextPage);
    }

    /** Saves current game state into the database. Displays current list of games in a list view.
     */
    public void saveGame(View view) {
        Database db = Database.getInstance(getApplicationContext());
        String[] gameList = db.returnGameList().toArray(new String[0]);
        setContentView( R.layout.database_popup );
        if (gameList != null) {
            ArrayAdapter<String> itemsAdapter =
                    new ArrayAdapter<String>( EditorActivity.this, android.R.layout.test_list_item, gameList );
            ListView listView = (ListView) findViewById(R.id.list_view );
            if (listView != null) {
                listView.setAdapter( itemsAdapter );
            }
        }
    }

    /** Upon clicking the 'save' button, saves the current game into the database and displays it.
     */
    public void confirmSave(View view) {
        Database db = Database.getInstance(getApplicationContext());
        TextView textView = findViewById(R.id.edit_text);
        String text = textView.getText().toString();
        String[] gameList = db.returnGameList().toArray(new String[0]);
        if (gameList.length > 11) { // Ensures list of saves does not run off screen.
            Toast toast = Toast.makeText(this,"Please delete a save before adding more.",Toast.LENGTH_SHORT);
            toast.show();
        } else if (getPages() == null) {
            Toast toast = Toast.makeText(this,"Please add at least one shape before saving.",Toast.LENGTH_SHORT);
            toast.show();
        } else {
            db.saveGame(text,getPages());
            returnHome();
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

    /** Deletes a save from the database.
     */
    public void deleteSave(View view) {
        Database db = Database.getInstance(getApplicationContext());
        TextView textView = findViewById(R.id.edit_text);
        String text = textView.getText().toString();
        db.deleteSave(text);
        String[] gameList = db.returnGameList().toArray(new String[0]);
        if (gameList != null) {
            ArrayAdapter<String> itemsAdapter =
                    new ArrayAdapter<String>( EditorActivity.this, android.R.layout.test_list_item, gameList );
            ListView listView = (ListView) findViewById(R.id.list_view );
            if (listView != null) {
                listView.setAdapter( itemsAdapter );
            }
        }
    }
    /** Loads a chosen save game.
     */

    /** Exits to main menu.
     */
    public void exit(View view) {
        Database db = Database.getInstance(getApplicationContext());
        db.autoSave(getPages()); // Autosaves in case someone did not mean to lose all of their data.\
        returnHome();
    }

    public void returnHome() {
        setContentView(R.layout.activity_editor);
        initializeResources();
        // Initializes Spinner for page options
        final Spinner pageSpinner = findViewById(R.id.pageSpinner);
        String[] pageOptions = new String[]{"Page Options:", "Create Page", "Rename Page", "Delete Page", "Open Page", "Change Background"};
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
        String[] shapeOptions = new String[]{"Shape Options:", "Add Shape", "Rename Shape", "Edit Shape", "Delete Shape"};
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
        editorView = findViewById(R.id.editorView);
        numShapes = 0;
        numPages = 0;
        editorView.changeCurrentPage(pages.get("page1"));
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
}