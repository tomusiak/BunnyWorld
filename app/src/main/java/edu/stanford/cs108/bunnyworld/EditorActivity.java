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

    ArrayList<PageView> pages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Spinner pageSpinner = (Spinner) findViewById(R.id.pageSpinner);
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
                        Toast addPageToast = Toast.makeText(getApplicationContext(),"[PAGE NAME] Added",Toast.LENGTH_LONG);
                        addPageToast.show();
                        break;
                    case 2:
                        renamePageDialog();
                        break;
                    case 3:
                        Toast toast = Toast.makeText(getApplicationContext(),"Page Deleted",Toast.LENGTH_SHORT);
                        toast.show();
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



        Spinner shapeSpinner = (Spinner) findViewById(R.id.shapeSpinner);
        String[] shapeOptions = new String[]{"Shape Options:", "Add Shape", "Name Shape", "See Shape", "Edit Shape", "Delete Shape"};
        ArrayAdapter<String> shapeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, shapeOptions);
        shapeSpinner.setAdapter(shapeAdapter);

        shapeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        break;
                    case 1:
                        Toast addToast = Toast.makeText(getApplicationContext(),"Shape Added",Toast.LENGTH_SHORT);
                        addToast.show();
                        break;
                    case 2:
                        renameShapeDialog();
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        Toast deleteToast = Toast.makeText(getApplicationContext(),"Shape Deleted",Toast.LENGTH_SHORT);
                        deleteToast.show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Spinner scriptSpinner = (Spinner) findViewById(R.id.scriptSpinner);
        String[] scriptOptions = new String[]{"Script Options:", "Create Script", "Show Script"};
        ArrayAdapter<String> scriptAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, scriptOptions);
        scriptSpinner.setAdapter(scriptAdapter);
        showCustomDialog();

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

    }

    private void renamePageDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Input New Name For Page: ");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        dialog.setView(input);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newPageName = input.getText().toString();
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();

    }

    private void renameShapeDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Input New Name For Shape: ");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        dialog.setView(input);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newPageName = input.getText().toString();
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void showCustomDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Create a new game or open an existing game?");
        dialog.setPositiveButton(
                "Create New Game",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        dialog.setNegativeButton(
                "Open Existing Game",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        dialog.show();
    }

    private void goToNewPageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Input Name Of Page To Go To: ");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newPageName = input.getText().toString();
                Toast pageNameToast = Toast.makeText(getApplicationContext(),"[PAGE NAME]",Toast.LENGTH_LONG);
                pageNameToast.show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    // Saves current game state into the database.
    public void saveGame(String saveName, HashMap<String, ArrayList<Shape>> shapeMap) {
        Database db = Database.getInstance(getApplicationContext());
        db.saveGame(saveName, shapeMap);
    }
}
