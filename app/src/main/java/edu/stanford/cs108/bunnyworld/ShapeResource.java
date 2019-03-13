package edu.stanford.cs108.bunnyworld;

import android.graphics.Bitmap;

public class ShapeResource {

    private String shapeName;
    private Bitmap bitmap;

    // stores the x, y position of where these should go in the pop-up screen
    private int x;
    private int y;

    public ShapeResource(Bitmap bm, int index, String name) {
        bitmap = bm;
        shapeName = name;

        // index mod operations used to determine which row and col this resource belongs on
        x = index % 3;
        y = index % 2;

    }

    public Bitmap getBitmap() { return bitmap; }

    public void setX(int x) { this.x = x; }

    public void setY(int y) { this.y = y; }

    public int getX() { return x; }

    public int getY() { return y; }

    public String getName() { return shapeName; }
}
