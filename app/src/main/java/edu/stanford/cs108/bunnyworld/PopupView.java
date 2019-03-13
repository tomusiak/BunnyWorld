package edu.stanford.cs108.bunnyworld;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * TODO: document your custom view class.
 */
public class PopupView extends View {

    float[] xpos = {(float)0.0625, (float)0.375, (float)0.6875};
    float[] ypos = {(float)0.25, (float)0.0625};

    ArrayList<ShapeResource> shapeResources; // stores rendered options
    ArrayList<String> resources;

    float canvasWidth;
    float canvasHeight;


    public PopupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {

        shapeResources = new ArrayList<>();
        populateResources();

    }

    /**
     * Called by EditorActivity upon creating a new popup view window to select a shape.
     * This populates the shapeResources arraylist of this class with all the necessary
     * ShapeResource objects.
     *
     */
    public void populateResources() {

        resources = new ArrayList<>();
        resources.add("carrot");
        resources.add("carrot2");
        resources.add("death");
        resources.add("duck");
        resources.add("fire");
        resources.add("mystic");

        // render each of the resource item's bitmaps and store them in ShapeResource Objects
        for(int i = 0; i < resources.size(); i++) {

            int bitmapDrawableID = getResources().getIdentifier(resources.get(i),
                    "drawable", getContext().getPackageName());
            BitmapDrawable drawableBM =
                    (BitmapDrawable) getResources().getDrawable(bitmapDrawableID);

            Bitmap bm = drawableBM.getBitmap();

            ShapeResource resource = new ShapeResource(bm, i, resources.get(i));
            shapeResources.add(resource);
        }
    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
        super.onSizeChanged(xNew, yNew, xOld, yOld);

        canvasWidth = xNew;
        canvasHeight = yNew;
    }

    /**
     * Override onDraw method. This is responsible for rendering the
     * available resource shapes the user can add to their bunny world
     * page in the resource selection pop up window
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int size = (int)(0.25 * canvasWidth);
        double shapeHeight = 0.25 * canvasWidth;
        double heightSpacer = 0.0625 * canvasWidth;

        // draw all the resources onto the pop-up window
        for(ShapeResource s : shapeResources) {
            System.out.println(s.getName());
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(
                    s.getBitmap(), size, size, false);
            float x = xpos[s.getX()] * canvasWidth;
            float y = ((float)heightSpacer + ((float)heightSpacer * s.getY())) + ((float)shapeHeight * s.getY());
            canvas.drawBitmap(scaledBitmap, x, y, null);

        }

//        float left1 = (float)0.0625*canvasWidth;
//        float left2 = (float)0.375*canvasWidth;
//        float left3 = (float)0.6875*canvasWidth;
//
//        double shapeHeight = 0.25*canvasWidth;
//        double heightSpacer = 0.0625*canvasWidth;
//
//        float height1 = (float)(heightSpacer);
//        float height2 = (float)(height1+shapeHeight+heightSpacer);

    }

//    /**
//     * Override onTouch Event. This is responsible for reading the touch
//     * activity to tell where the user clicks and update the x and y coords
//     * accordingly.
//     *
//     * @param event motion event
//     * @return success
//     */
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        switch (event.getAction()) {
//            // record coordinate where user presses down
//            case MotionEvent.ACTION_DOWN:
//                x1 = event.getX();
//                y1 = event.getY();
//
//                Shape selected = shapeAtXY(x1, y1);
//                if(currentPage != null) currentPage.selectShape(selected);
//
//                break;
//            // record coordinate where user lifts finger
//            case MotionEvent.ACTION_UP:
//                x2 = event.getX();
//                y2 = event.getY();
//
//                if (x1 > x2) {
//                    left = x2;
//                    right = x1;
//                } else {
//                    left = x1;
//                    right = x2;
//                }
//
//                if (y1 > y2) {
//                    top = y2;
//                    bottom = y1;
//                } else {
//                    top = y1;
//                    bottom = y2;
//                }
//
//            case MotionEvent.ACTION_MOVE:
//                xDelta = event.getX();
//                yDelta = event.getY();
//
//                if(currentPage != null && currentPage.getSelected() != null) {
//                    currentPage.getSelected().move(xDelta, yDelta);
//                }
//
//                invalidate();   // forces canvas update
//        }
//        return true;
//    }

}
