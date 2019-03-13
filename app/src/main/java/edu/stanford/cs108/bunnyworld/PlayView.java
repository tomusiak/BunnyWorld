package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Creates custom view for PlayActivity
 */
public class PlayView extends View {

    Shape selected;

    public PlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Initializes starter images
     */
    private void init() {
        selected = null;
    }

    /**
     * Parses and executes script on shape
     * @param shape
     */
    public void executeScript(Shape shape) {
        // Insert script parsing currently in mainactivity
    }

    /**
     * Renders the current page in question on the canvas by calling the
     * page's render function.
     */
    public void drawPage(Canvas canvas) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

    }

    @Override
    protected void onDraw(Canvas canvas) {

    }


}
