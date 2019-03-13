package edu.stanford.cs108.bunnyworld;

import android.graphics.Bitmap;

public class ShapeResource {

    private String shapeName;
    private Bitmap bitmap;

    // stores the x, y position of where these should go in the pop-up screen
    private float x;
    private float y;

    public ShapeResource(Bitmap bm, int index) {
        bitmap = bm;

        // index mod operations used to determine which row and col this resource belongs on
        x = index % 3;
        y = index % 2;
    }

    public Bitmap getBitmap() { return bitmap; }

    public void setX(float x) { this.x = x; }

    public void setY(float y) { this.y = y; }

    public float getX() { return x; }

    public float getY() { return y; }
}
