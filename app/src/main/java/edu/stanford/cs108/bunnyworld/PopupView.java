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

    /*
    number of shapes user can add to their game - should be 6 unless user has created a custom shape,
    in which case this variable will be change in init()
     */
    int numStarters = 6;

    // will be changed to true in init() if user has created a custom shape
    boolean extras = false;

    // user touch ACTION_DOWN coordinates
    float cornerX, cornerY;

    // size of the canvas
    float canvasWidth;
    float canvasHeight;

    String starterID;

    ArrayList<ArrayList<Float>> starterDrawables = new ArrayList<ArrayList<Float>>();

    BitmapDrawable carrotDrawable, carrot2Drawable, deathDrawable, duckDrawable, fireDrawable, mysticDrawable;

    public PopupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {

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

        starterID = null;
    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
        super.onSizeChanged(xNew, yNew, xOld, yOld);

        canvasWidth = xNew;
        canvasHeight = yNew;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap carrotBitmap = carrotDrawable.getBitmap();
        Bitmap carrot2Bitmap = carrot2Drawable.getBitmap();
        Bitmap deathBitmap = deathDrawable.getBitmap();
        Bitmap duckBitmap= duckDrawable.getBitmap();
        Bitmap fireBitmap = fireDrawable.getBitmap();
        Bitmap mysticBitmap = mysticDrawable.getBitmap();

        int size = (int)(0.25*canvasWidth);

        carrotBitmap = Bitmap.createScaledBitmap(
                carrotBitmap, size, size, false);

        carrot2Bitmap = Bitmap.createScaledBitmap(
                carrot2Bitmap, size, size, false);

        deathBitmap = Bitmap.createScaledBitmap(
                deathBitmap, size, size, false);

        duckBitmap = Bitmap.createScaledBitmap(
                duckBitmap, size, size, false);

        fireBitmap = Bitmap.createScaledBitmap(
                fireBitmap, size, size, false);

        mysticBitmap = Bitmap.createScaledBitmap(
                mysticBitmap, size, size, false);

        float left1 = (float)0.0625*canvasWidth;
        float left2 = (float)0.375*canvasWidth;
        float left3 = (float)0.6875*canvasWidth;

        double shapeHeight = 0.25*canvasWidth;
        double heightSpacer = 0.0625*canvasWidth;

        float height1 = (float)(heightSpacer);
        float height2 = (float)(height1+shapeHeight+heightSpacer);

        canvas.drawBitmap(carrotBitmap,left1,height1,null);
        ArrayList<Float> carrot = new ArrayList<Float>();
        carrot.add(left1);
        carrot.add(height1);
        starterDrawables.add(carrot);

        canvas.drawBitmap(carrot2Bitmap,left2,height1,null);
        ArrayList<Float> carrot2 = new ArrayList<Float>();
        carrot.add(left2);
        carrot.add(height1);
        starterDrawables.add(carrot2);

        canvas.drawBitmap(deathBitmap,left3,height1,null);
        ArrayList<Float> death = new ArrayList<Float>();
        carrot.add(left3);
        carrot.add(height1);
        starterDrawables.add(death);

        canvas.drawBitmap(duckBitmap,left1,height2,null);
        ArrayList<Float> duck = new ArrayList<Float>();
        carrot.add(left1);
        carrot.add(height2);
        starterDrawables.add(duck);

        canvas.drawBitmap(fireBitmap,left2,height2,null);
        ArrayList<Float> fire = new ArrayList<Float>();
        carrot.add(left2);
        carrot.add(height2);
        starterDrawables.add(fire);

        canvas.drawBitmap(mysticBitmap,left3,height2,null);
        ArrayList<Float> mystic = new ArrayList<Float>();
        carrot.add(left3);
        carrot.add(height2);
        starterDrawables.add(mystic);


        // something where you load in your custom shapes if they exist
        // potentially change extras to true
        // potentially change your numShapes to numStarters + (# extras)

    }

    /*
    Method to return an ArrayList of ArrayLists representing all of the
    coordinate information for the bitmaps currently on screen.
     */
    public ArrayList<ArrayList<Float>> getStarterCoordinates() {
        return starterDrawables;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                cornerX = event.getX();
                cornerY = event.getY();
                break;
            default:
                return false;
        }

        int shapeTouched = isInShape();
        if (shapeTouched != -1) {
            setShapeID(shapeTouched);
        }

        return true;
    }

    private int isInShape() {
        for (int i = 0; i < numStarters; i++) {
            float left = starterDrawables.get(i).get(0);
            float top = starterDrawables.get(i).get(1);

            if(cornerX <= left + (float)(0.25*canvasWidth) && cornerX >= left &&
                    cornerY >= top && cornerY <= top+(float)(0.25*canvasWidth)) {
                return i;
            }
        }
        return -1;
    }

    private void setShapeID(int shapeTouched) {
        if (shapeTouched == 0) {
            starterID = "carrot";
        }
        else if (shapeTouched == 1) {
            starterID = "carrot2";
        }
        else if (shapeTouched == 2) {
            starterID = "death";
        }
        else if (shapeTouched == 3) {
            starterID = "duck";
        }
        else if (shapeTouched == 4) {
            starterID = "fire";
        }
        else if (shapeTouched == 5) {
            starterID = "mystic";
        }
    }

    public String getShapeID() {
        return starterID;
    }


}
