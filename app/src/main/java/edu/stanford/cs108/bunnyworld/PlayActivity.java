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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayActivity extends AppCompatActivity {

    // Commented out this first line and replaced with a Hashmap<String, Page> to match agreed upon structures
    //static HashMap<String, ArrayList<Shape>> fullShapeList; // Contains key of string of page names linked to an ArrayList of shapes.
    static HashMap<String, Page> pageMap; // Maps string keys to page objects
    Page currentPage;
    String currPage;
    PlayView playView;
    private HashMap<String, String> displayNameToID; // Maps display name of Page to unique ID of Page
    private Page starterPage; // Tracks user-selected starter page

    ArrayList<Shape> inventory = new ArrayList<Shape>();

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
        setContentView(R.layout.activity_play);
        loadGame();
        playView = findViewById(R.id.play_view);
    }

    private void loadGame() {
        // initializes database
        final Database db = Database.getInstance(getApplicationContext());

        // we can now do stuff with the view
        playView = findViewById(R.id.play_view);

        setContentView( R.layout.database_load );
        ListView listView = findViewById( R.id.list_view );

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // gets the name of the gamestate we're loading
                String product = ((TextView) view).getText().toString();
                Toast pageNameToast = Toast.makeText(getApplicationContext(),product,Toast.LENGTH_SHORT);
                pageNameToast.show();

                // loads the hashmap from the database into the hashmap stored in this file
                setPages(db.loadGame(product));

                // sets the firstpage & ID to the firstpage & ID listed in the databse
                String firstPage = db.returnFirstPage( product );
                Page page = getPages().get(firstPage);

                if (playView != null && page != null) {
                    playView.changeCurrentPage( page );
                }
                currentPage = getPages().get("page1");
                displayNameToID = new HashMap<String, String>();
                String startPage = null;
                starterPage = currentPage;
                for (String key : getPages().keySet()) {
                    Page cPage = getPages().get(key);
                    String pageName = cPage.getDisplayName();
                    displayNameToID.put(key,pageName);
                    if (cPage.getStarterPageStatus() == true) {
                        startPage = key;
                        starterPage = cPage;
                    }
                }
                playView = findViewById(R.id.play_view);
                if (pageMap != null) {
                    playView.changeCurrentPage(starterPage);
                }
            }
        });

        String[] gameList = db.returnGameList().toArray( new String[0] );
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
        currPage = pageName;
        Page nextPage = pageMap.get(pageName);
        playView.changeCurrentPage(nextPage);
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
        System.out.println("pages");
    }

    // Imports save data once user decides to play.
    public void grabDatabase(String saveName) {
        Database thisDatabase = Database.getInstance(getApplicationContext()); // Gets context.
        //pageMap = thisDatabase.loadGame(saveName);
    }

    public void executeScript(String script) {
        // not case-sensitive
        script = script.toLowerCase();

        // splits block of script into clauses
        String[] clauses = script.split(";");

        // uses loop to execute each clause
        for (int i = 0; i < clauses.length; i++) {

            // splits each clause into tokens based on whitespace delimiter
            String[] tokens = clauses[i].split("\\s+");

            // index of first start action
            int actionStart = 2;

            if (tokens[1].equals(CLICK)) {

            } else if (tokens[1].equals(ENTER)) {

            } else if (tokens[1].equals(DROP)) {
                String shape = tokens[2];
                actionStart = 3;
            }

            // parse triggers
            for (int j = actionStart; j < tokens.length; j+=2) {
                String command = tokens[j];
                if (command.equals(GOTO)) {
                    switchPage(tokens[j+1]);
                } else if (command.equals(PLAY)) {
                    playSound(tokens[j+1]);
                } else if (command.equals(HIDE)) {
                    hideShape(tokens[j+1]);
                } else if (command.equals(SHOW)) {
                    showShape(tokens[j+1]);
                } else {
                    return;
                }
            }
        }
    }

    private void switchPage (String pageName) {

    }

    private void playSound (String soundName) {

    }

    private void hideShape (String shapeName) {

    }

    private void showShape (String shapeName) {

    }

}