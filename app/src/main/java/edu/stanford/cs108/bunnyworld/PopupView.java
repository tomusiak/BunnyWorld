package edu.stanford.cs108.bunnyworld;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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
        canvas.drawBitmap(carrot2Bitmap,left2,height1,null);
        canvas.drawBitmap(deathBitmap,left3,height1,null);
        canvas.drawBitmap(duckBitmap,left1,height2,null);
        canvas.drawBitmap(fireBitmap,left2,height2,null);
        canvas.drawBitmap(mysticBitmap,left3,height2,null);

    }

}
