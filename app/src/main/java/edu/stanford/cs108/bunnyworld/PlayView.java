package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Creates custom view for PlayActivity
 */
public class PlayView extends View {

    Shape selected;
    boolean inventorySelected; // is the last shape selected from the inventory?
    Page currentPage;
    Inventory inventory;

    private int TRANSPARENT = Color.WHITE;

    float x1, y1;   // x and y coordinate of initial press to the screen
    float x2, y2;   // x and y coordinate of when user lifts finger
    float xDelta, yDelta;

    float inventoryY;
    Paint myPaint = new Paint();
    Paint selectPaint = new Paint();
    Paint linePaint = new Paint();

    public PlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        myPaint.setColor(Color.rgb(255,0,0));
        myPaint.setStyle(Paint.Style.FILL);

        selectPaint.setColor(Color.rgb(0,0,255));
        selectPaint.setStyle(Paint.Style.STROKE);

        inventorySelected = false;

    }

    /**
     * Initializes starter images
     */
    private void init() {
        selected = null;
        inventory = new Inventory();
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

        //((PlayActivity)getContext()).setCurrentPage(currentPage);

        renderBitmaps(page); // render all the bitmaps for the page
        renderBitmaps(inventory); // render the inventory

        ((PlayActivity)getContext()).checkForEntryScript();

        invalidate();
    }

    /**
     * Accessor method for inventory
     * @return current Inventory
     */
    public Inventory getInventory() {
        return inventory;
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
     * Render all of the bitmap images for the current active inventory and
     * save them to the page's shape objects. Each shape object stores
     * its own bitmap inside.
     * @param inventory
     */
    public void renderBitmaps(Inventory inventory) {
        ArrayList<Shape> shapes = inventory.getList();  // get list of shapes from page

        // render the page's background
        if(inventory.hasBackground()) {
            int bitmapDrawableID = getResources().getIdentifier(inventory.getBackgroundName(),
                    "drawable", getContext().getPackageName());
            BitmapDrawable drawableBM =
                    (BitmapDrawable) getResources().getDrawable(bitmapDrawableID);
            Bitmap bm = drawableBM.getBitmap();
            bm = Bitmap.createScaledBitmap(bm, 1100,
                    1100, false);

            inventory.updateBackgroundBitmap(bm);    // set internal bitmap
        }

        // render the bitmap for each shape
        for(int i = 0; i < shapes.size(); i++) {
            Shape currentShape = shapes.get(i);
            if (!currentShape.isHidden()) {
                renderShape(currentShape);
            }
        }
        invalidate();
    }

    /**
     * Renders a single shape's bitmap.
     *
     * Note: must be called manually after a new shape with
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
        if(currentPage != null) currentPage.playRender(canvas);
        if (inventory != null) inventory.playRender(canvas);
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

        if(currentPage == null && inventory == null) return null; // don't do anything if page just loaded

        ArrayList<Shape> shapes = currentPage.getList();

        // search from back to get the images closet to the front
        for(int i = shapes.size() - 1; i >= 0; i--) {
            Shape s = shapes.get(i);
            // if the click is in bounds of this shape, return it
            if(x <= s.getRight() && x >= s.getLeft() &&
                    y >= s.getTop() && y <= s.getBottom()) {
                inventorySelected = false;
                return s;
            }
        }

        ArrayList<Shape> inventoryShapes = inventory.getList();

        for(int i = inventoryShapes.size() - 1; i >= 0; i--) {
            Shape s = inventoryShapes.get(i);
            // if the click is in bounds of this shape, return it
            if(x <= s.getRight() && x >= s.getLeft() &&
                    y >= s.getTop() && y <= s.getBottom()) {
                inventorySelected = true;
                return s;
            }
        }

        inventorySelected = false;
        selected = null;
        return null; // no shape is here
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
                if (selected != null) {
                    ((PlayActivity)this.getContext()).setCurrentPage(currentPage);
                    ((PlayActivity)this.getContext()).executeClickScripts(selected);
                }

                if (currentPage != null && !inventorySelected && selected != null) {
                    currentPage.selectShape(selected);
                }
                if (inventory != null && inventorySelected && selected != null) {
                    inventory.selectShape(selected);
                }
                break;

            // record coordinate where user lifts finger
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();

                currentPage.selectShape(null);
                inventory.selectShape(null);

            case MotionEvent.ACTION_MOVE:
                xDelta = event.getX();
                yDelta = event.getY();

                // only enable moving the page if isMoveable == true
                if (currentPage != null && currentPage.getSelected() != null
                        && currentPage.getSelected().getMoveableStatus()) {
                    currentPage.getSelected().move(xDelta, yDelta);

                    // move from play area to inventory
                    double halfHeight = currentPage.getSelected().getHeight()/2;
                    if (y1 <= inventoryY+halfHeight && yDelta >= inventoryY+halfHeight) {
                        inventory.addShape(currentPage.getSelected());
                        currentPage.removeShape(currentPage.getSelected());
                    }

                    // add something about the drop script
                }

                if (inventory != null && inventory.getSelected() != null) {
                    inventory.getSelected().move(xDelta, yDelta);

                    // move from inventory to play area
                    double halfHeight = inventory.getSelected().getHeight()/2;
                    if (y1 >= inventoryY+halfHeight && yDelta <= inventoryY+halfHeight) {
                        currentPage.addShape(inventory.getSelected());
                        inventory.removeShape(inventory.getSelected());
                        inventorySelected = false;
                    }
                }

                invalidate();   // forces canvas update
        }
        return true;
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

        float width = canvas.getWidth();
        float height = canvas.getHeight();

        inventoryY = (float)0.75*height;

        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(2);

        canvas.drawLine((float)0, (float)0.75*height, (float)width, (float)0.75*height, linePaint);
    }


}
