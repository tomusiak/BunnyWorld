package edu.stanford.cs108.bunnyworld;

/*
    The edu.stanford.cs108.bunnyworld.Shape class represents an object that has been added
    to an editor page view.
 */
public class Shape {
    private String shapeName;
    private String image;
    private String text;
    private double x;
    private double y;
    private double height;
    private double width;

    private boolean moveable;
    private boolean hidden;

    private int fontSize;
    private static final int DEFAULT_FONT_SIZE = 24;

    private String script;

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
     * Accessor method for current text being displayed
     * @return String the name of the image being displayed, empty String "" if not available
     */
    public String getText() {
        return this.text;
    }

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
     * @param script the current script
     */
    public String getScript() {
        return this.script;
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
        this.x = newX;
        this.y = newY;
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
    public boolean getHiddenStatus() {
        return this.hidden;
    }
}