package edu.stanford.cs108.bunnyworld;

import android.graphics.Canvas;

/*
    The edu.stanford.cs108.bunnyworld.Shape class represents an object that has been added
    to an editor page view.
 */
public class Shape {
    /* enum for the possible shape types */
    public enum ShapeType {
        IMAGE,
        TEXT,   // takes presedence over image
        RECT,
    }


    String imgName; // stores name of the image it draws
    String text;    // text displayed by image
    Boolean hidden;
    Boolean moveable;   // whether or not shape can be moved by user

    String script;  // stores script that object does

    // screen coordinates
    double x;
    double y;

    double height;
    double width;

    /* Constructor for shape object */
    public Shape() {

    }

    /* can set whether object is visible or not */
    public void setVisibility(Boolean value) {
        hidden = value;
    }

    public void setMoveable(Boolean value) {
        moveable = value;
    }

    /* update the displayed text in the shape */
    public void updateText(String text) {
        this.text = text;
    }

    /* updates image of this shape */
    public void updateImage(String imageName) {
        imgName = imageName;
    }

    /* draws the shape to the canvas, or WorldEditorView */
    public void draw(Canvas canvas) {

    }

    /* move the shape and update its coordinates */
    public void move(Canvas canvas, double newx, double newy) {

    }

    /* get x coordinate of the shape object (upper left corner) */
    public double getX() {
        return x;
    }

    /* get x coordinate of the shape object (upper left corner) */
    public double getY() {
        return y;
    }

    /* get the height of this shape */
    public double getHeight() {
        return height;
    }

    /* get the width of this shape */
    public double getWidth() {
        return width;
    }
}
