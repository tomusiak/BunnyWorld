package edu.stanford.cs108.bunnyworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private String script;

    // actions
    private static final String GOTO = "goto";
    private static final String PLAY = "play";
    private static final String HIDE = "hide";
    private static final String SHOW = "show";

    // triggers
    private static final String CLICK = "click";
    private static final String ENTER = "enter";
    private static final String DROP = "drop";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /* TODO: build test harness */
    public void executeScript() {
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
