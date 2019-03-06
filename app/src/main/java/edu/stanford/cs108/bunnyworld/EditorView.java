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
import android.view.View;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * TODO: document your custom view class.
 */
public class EditorView extends View {
    //canvas
    private Canvas canvas;

    //ArrayList<Shape> pageState = new ArrayList<Shape>();
    Page currentPage;
    ArrayList<Shape> starters = new ArrayList<Shape>();
    Shape selected;

    float canvasWidth;
    float canvasHeight;

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

    public void drawStarters() {
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
        currentPage = page;
        renderBitmaps(page); // render all the bitmaps for the page
        drawPage(); // update canvas to draw this new page
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

        }
    }

    /**
     * Renders the current page in question on the canvas by calling the
     * page's render function.
     */
    public void drawPage() {

        clearCanvas();
        currentPage.render(canvas);
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

        shape.setBitmap(drawableBM.getBitmap());
    }

    // passes a reference to the canvas
    public Canvas getCanvas() {
        return canvas;
    }

    // clears the canvas
    private void clearCanvas() {
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.canvas = canvas;
        super.onDraw(canvas);
        //drawPage();

    }
}
