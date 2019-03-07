package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class PopupView extends View {


    float canvasWidth;
    float canvasHeight;

    BitmapDrawable carrotDrawable, carrot2Drawable, deathDrawable, duckDrawable, fireDrawable, mysticDrawable;

    public PopupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        //drawStarters();


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

        carrotBitmap = Bitmap.createScaledBitmap(
                carrotBitmap, 120, 120, false);

        carrot2Bitmap = Bitmap.createScaledBitmap(
                carrot2Bitmap, 120, 120, false);

        deathBitmap = Bitmap.createScaledBitmap(
                deathBitmap, 120, 120, false);

        duckBitmap = Bitmap.createScaledBitmap(
                duckBitmap, 120, 120, false);

        fireBitmap = Bitmap.createScaledBitmap(
                fireBitmap, 120, 120, false);

        mysticBitmap = Bitmap.createScaledBitmap(
                mysticBitmap, 120, 120, false);

        float left1 = (float)0.0625*canvasWidth;
        float left2 = (float)0.375*canvasWidth;
        float left3 = (float)0.6875*canvasWidth;

        double shapeHeight = 0.25*canvasWidth;
        double heightSpacer = (canvasHeight - 2*shapeHeight)/3;

        float height1 = (float)(heightSpacer);
        float height2 = (float)(height1+shapeHeight+heightSpacer);

        canvas.drawBitmap(carrotBitmap,left1,height1,null);
        canvas.drawBitmap(carrot2Bitmap,left2,height1,null);
        canvas.drawBitmap(deathBitmap,left3,height1,null);
        canvas.drawBitmap(duckBitmap,left1,height2,null);
        canvas.drawBitmap(fireBitmap,left2,height2,null);
        canvas.drawBitmap(mysticBitmap,left3,height2,null);

    }

}

