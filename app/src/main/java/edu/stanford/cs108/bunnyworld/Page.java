package edu.stanford.cs108.bunnyworld;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.ArrayList;

public class Page {

    private ArrayList<Shape> shapes;
    private String displayName;
    private String pageID;
    private Boolean starterPage;

    private Shape selected;     // holds onto the selected shape
    private Paint borderColor;  // color for selection box
    private Paint transBoxFill;


    /**
     * Constructor for the Page Class. This is the empty
     * constructor used for buiding a brand new page in the editor.
     */
    public Page(String name, String uniquePageID) {
        shapes = new ArrayList<Shape>();
        displayName = name;
        pageID = uniquePageID;
        starterPage = false;
        selected = null;

        // initialize selection color
        borderColor = new Paint();
        borderColor.setStyle(Paint.Style.STROKE);
        borderColor.setStrokeWidth(10);
        borderColor.setColor(Color.rgb(190, 21, 21));

        // initialize transparent box color
        transBoxFill = new Paint();
        transBoxFill.setStyle(Paint.Style.FILL);
        transBoxFill.setColor(Color.TRANSPARENT);

    }

    /**
     * ArrayList constructor the page class. Assembles a new page from
     * an arraylist of shapes.
     */
    public Page(ArrayList<Shape> shapeList) {
        shapes = shapeList;
    }

    /**
     * Accessor method for the page's underlying arraylist of shapes.
     * Used to store the pages into the database.
     */
    public ArrayList<Shape> getList() {
        return shapes;
    }

    /**
     * Modifier for the Page object that allows you to load in a new
     * arraylist of shapes and overwrite the old ones.
     * @param newState
     */
    public void updateList(ArrayList<Shape> newState) {
        shapes = newState;
    }

    /**
     * Renders all shapes in the internal arraylist to the editor view canvas.
     * @param canvas the canvas area that the shapes will be drawn onto
     */
    public void render(Canvas canvas) {
        Paint paint = null;

        // render each shape onto the canvas
        for(int i = 0; i < shapes.size(); i++) {
            Shape currentShape = shapes.get(i);
            // if shape has a valid bitmap image
            if(currentShape.getImageName() != "") {

                // if the shape to be drawn is selected, render a box around it
                if(currentShape == selected) {

                    // use rectangle that is larger than the image as a border
                    RectF shapeBorder = new RectF((float)currentShape.getLeft() - 10,
                            (float)currentShape.getTop() - 10,
                            (float)currentShape.getRight() + 10,
                            (float)currentShape.getBottom() + 10);

                    // draw transparent box and colored selection border
                    canvas.drawRect(shapeBorder, transBoxFill);
                    canvas.drawRect(shapeBorder, borderColor);

                }

                // if this shape is hidden, display it as semi-transparent
                if(currentShape.getHiddenStatus()) {
                    paint = new Paint();
                    paint.setAlpha(70);
                }

                canvas.drawBitmap(currentShape.getBitmap(), (float)currentShape.getX(),
                        (float)currentShape.getY(), paint);

            }



        }
    }

    /**
     * Adds a new shape to the shapes arraylist. Used to add shapes to
     * pages.
     * @param shape to be added
     */
    public void addShape(Shape shape) { shapes.add(shape); }

    /**
     * Removes the specified shape from the arraylist. Used
     * to delete shapes from pages.
     * @param shape to be removed
     */
    public void removeShape(Shape shape) { shapes.remove(shape); }

    /**
     * Removes all shapes from the current page. Used to wipe this page
     * clean.
     */
    public void clearShapes() { shapes.clear(); }

    public ArrayList<Shape> getShapes() {
        return shapes;
    }

    /**
     * Get the unique identifier of this page
     * @return the page's unique ID number
     */
    public String getPageID() { return pageID; }

    public String getDisplayName() { return displayName; }

    /**
     * Updates this page's display name.
     * @param name to change displayName to
     */
    public void changeDisplayName(String name) { displayName = name; }

    /**
     * Set a specific shape on the page as selected
     * @param select shape to be marked as selected
     */
    public void selectShape(Shape select) {
        selected = select;

        // move items to the back of list when they are selected,
        // which moves them to the "front" of the screen
        if(selected != null) {
            shapes.remove(selected);
            shapes.add(selected);
        }

    }

    public Boolean getStarterPageStatus() { return starterPage; }

    public void setStarterPageStatus(Boolean status) { starterPage = status; }

    /**
     * Get the shape that is currently selected on this page
     * @return page that is currently selected, null if none
     */
    public Shape getSelected() { return selected; }

}
