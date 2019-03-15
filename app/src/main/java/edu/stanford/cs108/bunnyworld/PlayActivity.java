package edu.stanford.cs108.bunnyworld;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Play mode for BunnyWorld
 */
public class PlayActivity extends AppCompatActivity {

    // Commented out this first line and replaced with a Hashmap<String, Page> to match agreed upon structures
    //static HashMap<String, ArrayList<Shape>> fullShapeList; // Contains key of string of page names linked to an ArrayList of shapes.
    static HashMap<String, Page> pageMap; // Maps unique string ID to page objects
    private Page currentPage;
    // private String currPage;
    private PlayView playView;
    private HashMap<String, String> displayNameToID; // Maps display name of Page to unique ID of Page
    private Page starterPage; // Tracks user-selected starter page

    // ArrayList<Shape> inventory = new ArrayList<Shape>();
    // Inventory inventory;

    // actions for script parsing
    private static final String GOTO = "goto";
    private static final String PLAY = "play";
    private static final String HIDE = "hide";
    private static final String SHOW = "show";

    // triggers for script parsing
    private static final String CLICK = "click";
    private static final String ENTER = "enter";
    private static final String DROP = "drop";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play); // goes to play activity
        playView = findViewById(R.id.play_view); // initializes play view
        loadGame();
        checkForEntryScript();
    }

    /** Loads a game from the database. Allows user to select which game to load.
     *  Replaces pageMap with saved pageMap. Moves into play activity and changes the
     *  page.
     */
    private void loadGame() {
        final Database db = Database.getInstance(getApplicationContext()); // Initializes database.
        setContentView( R.layout.database_load ); // Opens database view.
        ListView listView = findViewById( R.id.list_view );
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // Occurs when user selects a save game.
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String product = ((TextView) view).getText().toString(); // Obtains game user chose.
                setPages(db.loadGame(product)); // Loads the hashmap from the database into the hashmap stored in this file
                displayNameToID = new HashMap<String, String>();

                setContentView(R.layout.activity_play); // Goes into play activity.
                playView = findViewById(R.id.play_view);

                if (pageMap != null) {
                    String startingPage = null;
                    for (String key : getPages().keySet()) {

                        Page currentPage = getPages().get(key);

                        String pageName = currentPage.getDisplayName();
                        // System.out.println("is hiding");

                        displayNameToID.put(key,pageName);
                        if (currentPage.getStarterPageStatus() == true) {
                            startingPage = key;
                            starterPage = currentPage;
                        }
                    }
                    playView.changeCurrentPage(pageMap.get(startingPage));
                }
                Toast successToast = Toast.makeText(getApplicationContext(),"Loading successful.",Toast.LENGTH_SHORT); // Informs user of successful load.
                successToast.show();
            }
        });
        // Enables multiple games
        String[] gameList = db.returnGameList().toArray( new String[0] ); // Shows list of games.
        if (gameList != null) {
            ArrayAdapter<String> itemsAdapter =
                    new ArrayAdapter<String>( PlayActivity.this, android.R.layout.test_list_item, gameList );
            listView = (ListView) findViewById( R.id.list_view );
            if (listView != null) {
                listView.setAdapter( itemsAdapter );
            }
        }
    }

    /**
     * Helper method to switch between pages
     * @param pageName the page to switch to
     */
    private void switchPages(String pageName) {
        // System.out.println("switching");
        // currPage = pageName;
        currentPage = pageMap.get(pageName);
        playView.changeCurrentPage(currentPage);
        // Page nextPage = pageMap.get(pageName);
        // playView.changeCurrentPage(nextPage);

    }

    /** Helper method to get pages
     * @return pages
     */
    private HashMap<String, Page> getPages() {
        return pageMap;
    }

    /** Helper setter method for pages.
     */
    private void setPages(HashMap<String,Page> newPages) {
        pageMap = newPages;
        // System.out.println("pages");
    }

    /**
     * Executes click scripts for Shape
     * If Shape has multiple click scripts, only the first one is executed per spec requirements
     * @param thisShape the Shape to be executed upon
     */
    public void executeClickScripts(Shape thisShape) {
        // Executes only if Shape is not hidden/unplayable
        if (!thisShape.isHidden()) {
            // not case-sensitive
            String script = thisShape.getScript().toLowerCase();

            // Accounts for null script, in which case script will not execute
            if (!script.equals("")) {
                // splits block of script into clauses
                String[] clauses = script.split(";");

                // uses loop to execute each clause
                for (int i = 0; i < clauses.length; i++) {

                    // splits each clause into tokens based on whitespace delimiter
                    String[] tokens = clauses[i].split("\\s+");
                    // System.out.println("tokens" + tokens);

                    // index of first start action
                    int actionStart = 2;

                    if (tokens[1].equals(CLICK)) {
                        // add click condition, then call helper method to parse triggers
                        executeTriggers(tokens, actionStart);
                        // only calls first click condition
                        break;
                    }
                }
            }
        }
    }

    /**
     * Executes enter scripts for Shape
     * @param thisShape the Shape to be executed upon
     */
    public void executeEnterScripts(Shape thisShape) {
        // Executes only if Shape is not hidden/unplayable
        if (!thisShape.isHidden()) {
            // not case-sensitive
            String script = thisShape.getScript().toLowerCase();

            // Accounts for null script, in which case script will not execute
            if (!script.equals("")) {
                // splits block of script into clauses
                String[] clauses = script.split(";");

                // uses loop to execute each clause
                for (int i = 0; i < clauses.length; i++) {

                    // splits each clause into tokens based on whitespace delimiter
                    String[] tokens = clauses[i].split("\\s+");

                    // index of first start action
                    int actionStart = 2;

                    if (tokens[1].equals(ENTER)) {
                        // add click condition, then call helper method to parse triggers
                        executeTriggers(tokens, actionStart);
                    }
                }
            }
        }
    }

    /**
     * Executes on drop scripts for Shape
     * @param thisShape the Shape to be executed upon, in this case the bottom Shape
     */
    public void executeDropScripts(Shape thisShape) {
        // Executes only if Shape is not hidden/unplayable
        if (!thisShape.isHidden()) {
            // not case-sensitive
            String script = thisShape.getScript().toLowerCase();

            // Accounts for null script, in which case script will not execute
            if (!script.equals("")) {
                // splits block of script into clauses
                String[] clauses = script.split(";");

                // uses loop to execute each clause
                for (int i = 0; i < clauses.length; i++) {

                    // splits each clause into tokens based on whitespace delimiter
                    String[] tokens = clauses[i].split("\\s+");

                    // index of first start action
                    // THREE instead of 2 because on drop is two words
                    int actionStart = 3;

                    if (tokens[1].equals(DROP)) {
                        // add click condition, then call helper method to parse triggers
                        executeTriggers(tokens, actionStart);
                    }
                }
            }
        }
    }

    /**
     * Executes appropriate command given provided trigger word
     * Pre-condition: Script is valid and called correctly
     * @param tokens the individual words of a clause in a script
     * @param actionStart the starting index of triggers (after actions are read)
     */
    private void executeTriggers(String[] tokens, int actionStart) {
        for (int j = actionStart; j < tokens.length; j+=2) {
            String command = tokens[j];
            if (command.equals(GOTO)) {
                // System.out.println("switching");
                switchPages(tokens[j+1]);
            } else if (command.equals(PLAY)) {
                playSound(tokens[j+1]);
            } else if (command.equals(HIDE)) {
                // System.out.println("hiding");
                hideShape(tokens[j+1]);
            } else if (command.equals(SHOW)) {
                showShape(tokens[j+1]);
            } else {
                return;
            }
        }
    }

    /**
     * Plays sound given result of parsed script
     * @param soundName the name of the sound to be played
     */
    public void playSound (String soundName) {
        if (soundName.equals("carrotcarrotcarrot")) {
            MediaPlayer mp = MediaPlayer.create(this,R.raw.carrotcarrotcarrot);
            mp.start();
        }
        else if (soundName.equals("evillaugh")) {
            MediaPlayer mp = MediaPlayer.create(this,R.raw.evillaugh);
            mp.start();
        }
        else if (soundName.equals("fire")) {
            MediaPlayer mp = MediaPlayer.create(this,R.raw.fire);
            mp.start();
        }
        else if (soundName.equals("hooray")) {
            MediaPlayer mp = MediaPlayer.create(this,R.raw.hooray);
            mp.start();
        }
        else if (soundName.equals("munch")) {
            MediaPlayer mp = MediaPlayer.create(this,R.raw.munch);
            mp.start();
        }
        else if (soundName.equals("munching")) {
            MediaPlayer mp = MediaPlayer.create(this,R.raw.munching);
            mp.start();
        }
        else if (soundName.equals("scream")) {
            MediaPlayer mp = MediaPlayer.create(this, R.raw.scream);
            mp.start();
        }
        else if (soundName.equals("woof")) {
            MediaPlayer mp = MediaPlayer.create(this,R.raw.woof);
            mp.start();
        }
    }

    /**
     * Sets Shape to be hidden, or not executable
     * @param shapeName the name of the Shape to be hidden
     */
    private void hideShape (String shapeName) {
        // refer to toasts (checks in PlayView for isHidden() etc)
        // if it's hidden, it's not playable
        // System.out.println("is hiding");

        ArrayList<Shape> shapes = currentPage.getList();

        // check to see if it exists, then sets
        for (int i = 0; i < shapes.size(); i++) {
            if (shapes.get(i).getShapeName().equals(shapeName)) {
                Shape current = shapes.get(i);
                current.setHidden(true);
                // redraw page
                // System.out.println("is hiding");
                playView.renderBitmaps(currentPage);
                break;
            }
        }
    }

    /**
     * Sets Shape to be not hidden, or executable
     * @param shapeName the name of the Shape to be shown
     */
    private void showShape (String shapeName) {
        ArrayList<Shape> shapes = currentPage.getList();
        for (int i = 0; i < shapes.size(); i++) {
            if (shapes.get(i).getShapeName().equals(shapeName)) {
                Shape current = shapes.get(i);
                current.setHidden(false);
                // redraw page
                playView.renderBitmaps(currentPage);
                break;
            }
        }
    }

    /**
     * Checks for "on enter" scripts that should be executed at the beginning
     * of the game.
     */
    public void checkForEntryScript() {
        if(currentPage == null) return;
        ArrayList<Shape> shapes = currentPage.getShapes();
        for (int i = 0; i < shapes.size(); i++) {
            executeEnterScripts(shapes.get(i));
        }
    }

    /**
     * Sets current page
     */
    public void setCurrentPage(Page page) {
        currentPage = page;
    }
}