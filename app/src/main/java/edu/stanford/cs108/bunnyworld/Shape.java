package edu.stanford.cs108.bunnyworld;

import android.graphics.Bitmap;
import android.graphics.Color;

/*
    The edu.stanford.cs108.bunnyworld.Shape class represents an object that has been added
    to an editor page view.
 */
public class Shape {
    private String shapeName; // Name of Shape
    private String shapeID; // Internal ID for Shape
    private String image;
    private String text;

    private boolean isText;
    private int fontSize;
    private int fontColor;

    private double x;
    private double y;
    private double height;
    private double width;

    private boolean moveable;
    private boolean hidden;

    private static final int DEFAULT_FONT_SIZE = 24; // Sets default font size to 24

    // Script, which may contain multiple executable clauses, is stored as a String
    private String script;

    Bitmap bitmap;

    /**
     * Constructor for Shape class
     * @param shapeNumber the number of the Shape being created relative to all Shapes
     * @param imageName name of image, i.e. "bunny.png" - can be empty String
     * @param textName user-inputted text to appear in single line - can be empty String
     * @param xCoord upper left hand x coordinate
     * @param yCoord upper left hand y coordinate
     * @param width width of image or text being displayed
     * @param height height of image or text being displayed
     */
    public Shape(int shapeNumber, String imageName, String textName,
                 double xCoord, double yCoord, double width, double height) {

        // Default format is "shape1", "shape2", etc.
        this.shapeName = "shape" + shapeNumber;
        this.shapeID = this.shapeName; // internal ID and shape name are the same, but user can set shape name to be something else

        this.image = imageName;
        this.text = textName;
        this.x = xCoord;
        this.y = yCoord;
        this.height = height;
        this.width = width;

        // Shapes are moveable and un-hidden as a default
        this.moveable = true;
        this.hidden = false;
        // Default font size is 24
        this.fontSize = DEFAULT_FONT_SIZE;

        // Script will be set once it's read in from interface
        this.script = "";

        // set text defaults
        this.isText = false;
        if(!textName.equals("")) this.isText = true;
        this.fontColor = Color.BLACK;

    }

    public Shape(Shape copyShape) {

        // Default format is "shape1", "shape2", etc.
        this.shapeName = copyShape.getShapeName();
        this.shapeID = this.shapeName; // internal ID and shape name are the same, but user can set shape name to be something else

        this.image = copyShape.getImageName();
        this.text = copyShape.getText();
        this.x = copyShape.getX();
        this.y = copyShape.getY();
        this.height = copyShape.getHeight();
        this.width = copyShape.getWidth();

        this.moveable = copyShape.getMoveableStatus();
        this.hidden = copyShape.isHidden();

        this.fontSize = copyShape.getFontSize();

        this.script = copyShape.getScript();
        }

    /**
     * Accessor method for shape's internal ID
     * @return String the Shape's internal ID
     */
    public String getShapeID() {
        return this.shapeID;
    }

    /**
     * Accessor method for current shape name
     * @return String the name of the shape
     */
    public String getShapeName() {
        return this.shapeName;
    }
    /**
     * Modifier method for shape name
     * @param name the user-set name of the shape
     */
    public void setShapeName(String name) {
        this.shapeName = name;
    }

    /**
     * Accessor method for current image name being displayed
     * @return String the name of the image being displayed, empty String "" if not available
     */
    public String getImageName() {
        return this.image;
    }

    /**
     * Modifier method for current image name being displayed
     * @param name the user-set image name of the shape
     */
    public void setImageName(String name) { this.image = name; }

    /**
     * Accessor method for current text being displayed
     * @return String the name of the image being displayed, empty String "" if not available
     */
    public String getText() {
        return this.text;
    }

    /**
     * Modifier method for text being displayed
     * @param text the user-set text
     */
    public void setText(String text) {
        if(text != null && !text.equals("")) {
            isText = true;
        }
        this.text = text;
    };

    /**
     * update the boolean variable that tracks whether this shape is actually text
     * @param isText
     */
    public void setIsText(boolean isText) { this.isText = isText; }

    /**
     * see if this shape is actually text
     */
    public boolean isText() { return isText; }

    /**
     * Set the font color of this shape
     * @param c color to be selected
     */
    public void setFontColor(int c) { fontColor = c; }

    /**
     * Get the font color of this shape
     * @return font color
     */
    public int getFontColor() { return fontColor; }

    /**
     * Modifier method that sets the script of the shape
     * Pre-Condition: Script is in the correct converted format after user clicks relevant buttons
     * @param script the new script of the shape
     */
    public void setScript(String script) {
        this.script = script;
    }
    /**
     * Accessor method that returns the script of the shape
     * @param //script the current script
     */
    public String getScript() {
        return this.script;
    }

    /**
     * Checks to see if script has goto action
     * @return true if present, false if not
     */
    public boolean scriptHasGoto() {
        this.script = this.script.toLowerCase();
        String[] clauses = script.split(";");

        for (int i = 0; i < clauses.length; i++) {
            String[] tokens = clauses[i].split("\\s+");
            for (int j = 2; j < tokens.length; j+=2) {
                if (tokens[j].equals("goto")) {
                    return true;
                }
            }
        }
        return false;

    }

    /**
     * The following are accessor methods for Shape properties (x, y, width, height)
     */
    public double getX() {
        return this.x;
    }
    public double getY() {
        return this.y;
    }
    public double getWidth() {
        return this.width;
    }
    public double getHeight() {
        return this.height;
    }

    /**
     * The following are modifier methods that set Shape properties (x, y, width, height)
     */
    public void setX(double newX) {
        this.x = newX;
    }
    public void setY(double newY) {
        this.y = newY;
    }
    public void setWidth(double newWidth) {
        this.width = newWidth;
    }
    public void setHeight(double newHeight) {
        this.height = newHeight;
    }

    /**
     * Moves Shape to new location by setting new x and y coordinates for location
     * @param newX upper left hand corner x value for new location
     * @param newY upper left hand corner y value for new location
     */
    public void move(double newX, double newY) {

        double xdist = newX - getLeft();
        double ydist = newY - getTop();
        this.x = newX - (width / 2);
        this.y = newY - (height / 2);
//        this.x += xdist;
//        this.y += ydist;
    }

    /**
     * Sets new size of Shape given new width and height
     * @param newWidth new width of Shape
     * @param newHeight new height of Shape
     */
    public void resize(double newWidth, double newHeight) {
        this.width = newWidth;
        this.height = newHeight;
    }

    /** Shape Coordinate accessor methods **/

    /**
     * Get the coordinate that represents the right of the shape's bounding box.
     * @return right coordinate
     */
    public double getRight() { return x + width; }

    /**
     * Get the coordinate that represents the left of the shape's bounding box.
     * @return left coordinate
     */
    public double getLeft() { return x; }

    /**
     * Get the coordinate that represents the top of the shape's bounding box.
     * @return top coordinate
     */
    public double getTop() { return y; }

    /**
     * Get the coordinate that represents the bottom of the shape's bounding box.
     * @return bottom coordinate
     */
    public double getBottom() { return y + height; }

    /**
     * Accessor method for font size
     * @return current font size
     */
    public int getFontSize() {
        return this.fontSize;
    }

    /**
     * Modifier method to set font size (default is 24)
     * @param size the new size to be set
     */
    public void setFontSize(int size) {
        this.fontSize = size;
    }

    /**
     * Allows user to set Shape to be moveable or not
     * @param isMoveable true or false for moveability
     */
    public void setMoveable(boolean isMoveable) {
        this.moveable = isMoveable;
    }

    /**
     * Gets whether Shape is moveable or not, could be used as a check before calling move()
     * @return whether Shape is currently moveable
     */
    public boolean getMoveableStatus() {
        return this.moveable;
    }

    /**
     * Sets shape to be hidden or unhidden
     * @param isHidden true and false for hidden and not hidden
     */
    public void setHidden(boolean isHidden) {
        this.hidden = isHidden;
    }

    /**
     * Returns whether the shape is hidden or not
     * @return true and false for hidden and not hidden
     */
    public boolean isHidden() { return hidden; }

    /**
     * Updates the internal shape to store its current bitmap representation
     *
     * Note: does not need to be saved in the database. This just holds on
     * to the bitmap rendering for when it actually needs to be loaded.
     *
     * @param bm the bitmap
     */
    public void setBitmap(Bitmap bm) { bitmap = bm; }

    /**
     * Gives the bitmap representation of the image
     * @return the stored bitmap image
     */
    public Bitmap getBitmap() { return bitmap; }
}