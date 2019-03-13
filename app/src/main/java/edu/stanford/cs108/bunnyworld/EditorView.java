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
 * TODO: document your custom view class.
 */
public class EditorView extends View {

    //private Canvas canvas; // storing the canvas is broken, use onDraw method instead

    private int TRANSPARENT = Color.WHITE;
    Page currentPage;
    ArrayList<Shape> starters = new ArrayList<Shape>();
    Shape selected;


    // Touch activity float variables
    float x1, y1;   // x and y coordinate of initial press to the screen
    float x2, y2;   // x and y coordinate of when user lifts finger
    float top, left, bottom, right;
    float xDelta, yDelta;

    BitmapDrawable carrotDrawable, carrot2Drawable, deathDrawable, duckDrawable, fireDrawable, mysticDrawable;

    // myPaint is a placeHolder
    Paint myPaint = new Paint();
    Paint selectPaint = new Paint();


    public EditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        drawStarters();

        // myPaint is a placeholder
        myPaint.setColor(Color.rgb(255,0,0));
        myPaint.setStyle(Paint.Style.FILL);

        selectPaint.setColor(Color.rgb(0,0,255));
        selectPaint.setStyle(Paint.Style.STROKE);
    }

    private void init() {
        selected = null;


        //canvasWidth = canvas.getWidth();
        //canvasHeight = canvas.getHeight();

        // set up all our starters
        carrotDrawable =
                (BitmapDrawable) getResources().getDrawable(R.drawable.carrot);
        carrot2Drawable =
                (BitmapDrawable) getResources().getDrawable(R.drawable.carrot2);
        deathDrawable =
                (BitmapDrawable) getResources().getDrawable(R.drawable.death);
        duckDrawable =
                (BitmapDrawable) getResources().getDrawable(R.drawable.duck);
        fireDrawable =
                (BitmapDrawable) getResources().getDrawable(R.drawable.fire);
        mysticDrawable =
                (BitmapDrawable) getResources().getDrawable(R.drawable.mystic);
    }

    // TODO: Canvas width/height operations currently don't work. Pretty
    // TODO: sure that this code is actually to be implemented in the pop-up
    public void drawStarters() {


        // this is commented out due to the reason that canvasWidth & canvasHeight
        // are not able to be assigned in the init() method for the reason that canvas
        // is only accessible in the onDraw method

//        Bitmap carrotBitmap = carrotDrawable.getBitmap();
//        Bitmap carrot2Bitmap = carrot2Drawable.getBitmap();
//        Bitmap deathBitmap = deathDrawable.getBitmap();
//        Bitmap duckBitmap= duckDrawable.getBitmap();
//        Bitmap fireBitmap = fireDrawable.getBitmap();
//        Bitmap mysticBitmap = mysticDrawable.getBitmap();
//
//        float left1 = (float)0.0625*canvasWidth;
//        float left2 = (float)0.375*canvasWidth;
//        float left3 = (float)0.6875*canvasWidth;
//
//        double shapeHeight = 0.25*canvasWidth;
//        double heightSpacer = (0.25*canvasHeight - 2*shapeHeight)/3;
//        double inventoryStart = 0.75*canvasHeight;
//
//        float height1 = (float)(inventoryStart+heightSpacer);
//        float height2 = (float)(height1+shapeHeight+heightSpacer);
//
//        canvas.drawBitmap(carrotBitmap,left1,height1,null);
//        canvas.drawBitmap(carrot2Bitmap,left2,height1,null);
//        canvas.drawBitmap(deathBitmap,left3,height1,null);
//        canvas.drawBitmap(duckBitmap,left1,height2,null);
//        canvas.drawBitmap(fireBitmap,left2,height2,null);
//        canvas.drawBitmap(mysticBitmap,left3,height2,null);
    }

    /**
     * Update the current working page displayed in the editor view to
     * be the new one passed in
     * @param page
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

        // render the bitmap for each shape
        for(int i = 0; i < shapes.size(); i++) {
            Shape currentShape = shapes.get(i);
            String imageID = currentShape.getImageName();

            int bitmapID = getResources().getIdentifier(imageID, "drawable", getContext().getPackageName());
            BitmapDrawable drawableBM =
                    (BitmapDrawable) getResources().getDrawable(bitmapID);
            currentShape.setBitmap(drawableBM.getBitmap());
            renderShape( currentShape );

        }

        invalidate();
    }

    /**
     * Renders the current page in question on the canvas by calling the
     * page's render function.
     */
    public void drawPage(Canvas canvas) {

        //clearCanvas(canvas);
        if(currentPage != null) currentPage.render(canvas);
    }


    /**
     * Deletes the currently selected shape if one exists & returns true
     * upon success. Also will force the canvas to reflect this update.
     * @return true/false bases upon success of deletion
     */
    public boolean deleteShape() {

        // try to delete to a shape
        if(currentPage != null && currentPage.getSelected() != null) {
            currentPage.removeShape(currentPage.getSelected());
            invalidate();
            return true;
        }
        return false;   // no shape to delete
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

        // update the shape's internal bitmap and set its attributes
        shape.setBitmap(myBitmap);
        shape.setWidth(bm.getWidth());
        shape.setHeight(bm.getHeight());

        invalidate();
    }

    /**
     * TODO: Don't think this needs to be used
     * clears the Canvas object.
     * @param canvas to be cleared
     */
    private void clearCanvas(Canvas canvas) {
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
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
                if(currentPage != null) currentPage.selectShape(selected);

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

            case MotionEvent.ACTION_MOVE:
                xDelta = event.getX();
                yDelta = event.getY();

                if(currentPage != null && currentPage.getSelected() != null) {
                    currentPage.getSelected().move(xDelta, yDelta);
                }

                invalidate();   // forces canvas update
        }
        return true;
    }

}
