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
    Shape selected;

    float canvasWidth;
    float canvasHeight;

    BitmapDrawable carrotDrawable, carrot2Drawable, deathDrawable, duckDrawable, fireDrawable, mysticDrawable;

    // myPaint is a placeHolder
    Paint myPaint = new Paint();
    Paint selectPaint = new Paint();


    public EditorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // myPaint is a placeholder
        myPaint.setColor(Color.rgb(255,0,0));
        myPaint.setStyle(Paint.Style.FILL);

        selectPaint.setColor(Color.rgb(0,0,255));
        selectPaint.setStyle(Paint.Style.STROKE);
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
