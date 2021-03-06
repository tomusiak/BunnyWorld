package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Creates custom view for EditorActivity
 */
public class EditorView extends View {

    private int TRANSPARENT = Color.WHITE;
    Page currentPage;
    Shape selected;

    // Touch activity float variables
    float x1, y1;   // x and y coordinate of initial press to the screen
    float x2, y2;   // x and y coordinate of when user lifts finger
    float top, left, bottom, right;
    float xDelta, yDelta;

    // myPaint is a placeHolder
    Paint myPaint = new Paint();
    Paint selectPaint = new Paint();

    boolean resizing;

    float inventoryY;

    /**
     * Sets up EditorView
     */
    public EditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

        // myPaint is a placeholder
        myPaint.setColor(Color.rgb(255,0,0));
        myPaint.setStyle(Paint.Style.FILL);

        selectPaint.setColor(Color.rgb(0,0,255));
        selectPaint.setStyle(Paint.Style.STROKE);
    }

    /**
     * Initializes starter images from resources
     */
    private void init() {
        selected = null;
        resizing = false;
    }

    /**
     * Update the current working page displayed in the editor view to
     * be the new one passed in
     * @param page the new page to be passed in
     */
    public void changeCurrentPage(Page page) {
        // set selected to null before changing the page
        if(currentPage != null) currentPage.selectShape(null);
        currentPage = page;
        renderBitmaps(page); // render all the bitmaps for the page
        invalidate();
    }

    /**
     * Render all of the bitmap images for the current active page and
     * save them to the page's shape objects. Each shape object stores
     * its own bitmap inside.
     * @param page
     */
    public void renderBitmaps(Page page) {
        ArrayList<Shape> shapes = page.getList();  // get list of shapes from page

        // render the page's background
        if(page.hasBackground()) {
            int bitmapDrawableID = getResources().getIdentifier(page.getBackgroundName(),
                    "drawable", getContext().getPackageName());
            BitmapDrawable drawableBM =
                    (BitmapDrawable) getResources().getDrawable(bitmapDrawableID);
            Bitmap bm = drawableBM.getBitmap();
            bm = Bitmap.createScaledBitmap(bm, 1100,
                    1100, false);

            page.updateBackgroundBitmap(bm);    // set internal bitmap
        }

        // render the bitmap for each shape
        for(int i = 0; i < shapes.size(); i++) {
            Shape currentShape = shapes.get(i);
            renderShape(currentShape);
        }
        invalidate();
    }

    /**
     * Renders a single shape's bitmap.
     *
     * Note: must be called manuallly after a new shape with
     * an image is created.
     * @param shape to render the bitmap for
     */
    public void renderShape(Shape shape) {
        String imageID = shape.getImageName();

        // create a bitmap and store it inside the current shape
        int bitmapDrawableID = getResources().getIdentifier(imageID, "drawable", getContext().getPackageName());
        BitmapDrawable drawableBM =
                (BitmapDrawable) getResources().getDrawable(bitmapDrawableID);

        // get bitmap from image resource and adjust the transparent pixels
        Bitmap bm = drawableBM.getBitmap();
        int[] pixels = new int[bm.getHeight() * bm.getWidth()];
        bm.getPixels(pixels, 0, bm.getWidth(), 0, 0, bm.getWidth(), bm.getHeight());
        for(int p = 0; p < bm.getHeight() * bm.getWidth(); p++) {
            if(pixels[p] == TRANSPARENT) {
                pixels[p] = Color.alpha(Color.TRANSPARENT);
            }
        }

        // create new bitmap that has transparency
        Bitmap myBitmap = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        myBitmap.setPixels(pixels, 0, bm.getWidth(), 0, 0, bm.getWidth(), bm.getHeight());

        myBitmap = Bitmap.createScaledBitmap(myBitmap, (int)shape.getWidth(),
                (int)shape.getHeight(), false);

        // update the shape's internal bitmap and set its attributes
        shape.setBitmap(myBitmap);
        shape.setWidth(myBitmap.getWidth());
        shape.setHeight(myBitmap.getHeight());


        invalidate();
    }

    /**
     * Renders the current page in question on the canvas by calling the
     * page's render function.
     */
    public void drawPage(Canvas canvas) {
        if(currentPage != null) currentPage.render(canvas);
    }

    /**
     * Deletes the currently selected shape if one exists & returns true
     * upon success. Also will force the canvas to reflect this update.
     * @return true/false bases upon success of deletion
     */
    public boolean deleteShape() {

        // try to delete a shape
        if(currentPage != null && currentPage.getSelected() != null) {
            currentPage.removeShape(currentPage.getSelected());
            invalidate();
            return true;
        }
        return false;   // no shape to delete
    }

    /**
     * Finds a shape that exists at the specified x, y coordinate and returns
     * it. null is returned if no shape is found.
     *
     * @param x coordinate to search for shape at
     * @param y coordinate to search for shape at
     * @return the found shape, or null if no shape is found at x, y
     */
    public Shape shapeAtXY(double x, double y) {

        if(currentPage == null) return null; // don't do anything if page just loaded

        ArrayList<Shape> shapes = currentPage.getList();

        // search from back to get the images closet to the front
        for(int i = shapes.size() - 1; i >= 0; i--) {
            Shape s = shapes.get(i);
            // if the click is in bounds of this shape, return it
            if(x <= s.getRight() && x >= s.getLeft() &&
                    y >= s.getTop() && y <= s.getBottom()) {
                return s;
            }
        }

        return null; // no shape is here
    }

    /**
     * Override onDraw method. This will update the canvas to reflect the
     * most recent updates to the pages and objects.
     *
     * This is only called when the current view is invalidated and forced
     * to update. This only occurs 1. at the beginning and 2. when invalidate()
     * is called.
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPage(canvas);

        float height = canvas.getWidth();
        inventoryY = (float)0.75 * height;
    }

    /**
     * Override onTouch Event. This is responsible for reading the touch
     * activity to tell where the user clicks and update the x and y coords
     * accordingly.
     *
     * @param event motion event
     * @return success
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            // record coordinate where user presses down
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();

                Shape selected = shapeAtXY(x1, y1);

                if(currentPage != null) {
                    if(currentPage.getSelected() != null && clickInBubble(x1, y1)) {
                        resizing = true;
                    } else {
                        currentPage.selectShape(selected);
                        resizing = false;

                        if(currentPage.getSelected() != null) {
                            System.out.println("isText: " + currentPage.getSelected().isText());
                            System.out.println("text: " + currentPage.getSelected().getText());
                        }
                    }
                }

                break;
            // record coordinate where user lifts finger
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();

                if (x1 > x2) {
                    left = x2;
                    right = x1;
                } else {
                    left = x1;
                    right = x2;
                }

                if (y1 > y2) {
                    top = y2;
                    bottom = y1;
                } else {
                    top = y1;
                    bottom = y2;
                }

                resizing = false;
                break;

            case MotionEvent.ACTION_MOVE:
                xDelta = event.getX();
                yDelta = event.getY();

                // if the user is clicking on the resize bubble, resize
                if(resizing && currentPage != null && currentPage.getSelected() != null) {


                    float width = (float)currentPage.getSelected().getX() +
                            (float)(xDelta - currentPage.getSelected().getX());

                    float height = (float)currentPage.getSelected().getY() +
                            (float)(yDelta - currentPage.getSelected().getY());

                    System.out.println("width: " + width);
                    System.out.println("height: " + height);

                    // no negative dimensions
                    if(width > 1 && height > 1) {
                        currentPage.getSelected().setWidth((double)width);
                        currentPage.getSelected().setHeight((double)height);
                    }


                    renderShape(currentPage.getSelected());

                } else if(currentPage != null && currentPage.getSelected() != null
                        && !resizing) {

                    System.out.println("case 5");
                    currentPage.getSelected().move(xDelta, yDelta);
                    renderShape(currentPage.getSelected());
                }

        }

        invalidate(); // forces canvas update
        return true;
    }

    private boolean clickInBubble(float x1, float y1) {
        Shape s = currentPage.getSelected();
        return ((x1 >= s.getRight() - 100) && (x1 <= s.getRight() + 100)
            && (y1 >= s.getBottom() - 100) && (y1 <= s.getBottom() + 100));
    }

}
