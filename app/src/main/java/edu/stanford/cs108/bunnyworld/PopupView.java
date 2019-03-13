package edu.stanford.cs108.bunnyworld;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * TODO: document your custom view class.
 */
public class PopupView extends View {

    float[] xpos = {(float)0.0625, (float)0.375, (float)0.6875};
    float[] ypos = {(float)0.25, (float)0.0625};

    ArrayList<ShapeResource> shapeResources;
    ArrayList<String> resources;
    float canvasWidth;
    float canvasHeight;

    BitmapDrawable carrotDrawable, carrot2Drawable, deathDrawable, duckDrawable, fireDrawable, mysticDrawable;


    public PopupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {

        shapeResources = new ArrayList<>();
        populateResources();
        // set up all our starters
//        carrotDrawable =
//                (BitmapDrawable) getResources().getDrawable(R.drawable.carrot);
//        carrot2Drawable =
//                (BitmapDrawable) getResources().getDrawable(R.drawable.carrot2);
//        deathDrawable =
//                (BitmapDrawable) getResources().getDrawable(R.drawable.death);
//        duckDrawable =
//                (BitmapDrawable) getResources().getDrawable(R.drawable.duck);
//        fireDrawable =
//                (BitmapDrawable) getResources().getDrawable(R.drawable.fire);
//        mysticDrawable =
//                (BitmapDrawable) getResources().getDrawable(R.drawable.mystic);
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Bitmap carrotBitmap = carrotDrawable.getBitmap();
//        Bitmap carrot2Bitmap = carrot2Drawable.getBitmap();
//        Bitmap deathBitmap = deathDrawable.getBitmap();
//        Bitmap duckBitmap= duckDrawable.getBitmap();
//        Bitmap fireBitmap = fireDrawable.getBitmap();
//        Bitmap mysticBitmap = mysticDrawable.getBitmap();

        int size = (int)(0.25*canvasWidth);
        double shapeHeight = 0.25*canvasWidth;
        double heightSpacer = 0.0625*canvasWidth;

        // draw all the resources onto the pop-up window
        for(ShapeResource s : shapeResources) {
            System.out.println(s.getName());
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(
                    s.getBitmap(), size, size, false);
            float x = xpos[s.getX()] * canvasWidth;
            float y = ((float)heightSpacer + ((float)heightSpacer * s.getY())) + ((float)shapeHeight * s.getY());
            canvas.drawBitmap(scaledBitmap, x, y, null);

        }

//        carrotBitmap = Bitmap.createScaledBitmap(
//                carrotBitmap, size, size, false);
//
//        carrot2Bitmap = Bitmap.createScaledBitmap(
//                carrot2Bitmap, size, size, false);
//
//        deathBitmap = Bitmap.createScaledBitmap(
//                deathBitmap, size, size, false);
//
//        duckBitmap = Bitmap.createScaledBitmap(
//                duckBitmap, size, size, false);
//
//        fireBitmap = Bitmap.createScaledBitmap(
//                fireBitmap, size, size, false);
//
//        mysticBitmap = Bitmap.createScaledBitmap(
//                mysticBitmap, size, size, false);
//
//        float left1 = (float)0.0625*canvasWidth;
//        float left2 = (float)0.375*canvasWidth;
//        float left3 = (float)0.6875*canvasWidth;
//
//        double shapeHeight = 0.25*canvasWidth;
//        double heightSpacer = 0.0625*canvasWidth;
//
//        float height1 = (float)(heightSpacer);
//        float height2 = (float)(height1+shapeHeight+heightSpacer);
//
//        canvas.drawBitmap(carrotBitmap,left1,height1,null);
//        canvas.drawBitmap(carrot2Bitmap,left2,height1,null);
//        canvas.drawBitmap(deathBitmap,left3,height1,null);
//        canvas.drawBitmap(duckBitmap,left1,height2,null);
//        canvas.drawBitmap(fireBitmap,left2,height2,null);
//        canvas.drawBitmap(mysticBitmap,left3,height2,null);

    }

}
