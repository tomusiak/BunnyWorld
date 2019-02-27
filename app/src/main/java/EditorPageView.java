import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import java.util.ArrayList;

/*
    Custom View class for an editor page.
 */
public class EditorPageView {

    ArrayList<Shape> shapes;
    String PageName;

    /* Constructor for custom bunny world canvas */
    public EditorPageView(Context context, AttributeSet attrs, String name) {
        PageName = name;

    }

    /* adds a new shape to the page */
    public void createShape() {

    }

    /* draw all current shapes onto the canvas */
    public void drawShapes(Canvas canvas) {
        for(Shape s : shapes) {
            s.draw(canvas);
        }
    }

    /* Returns shape object at specified xy coordinate */
    public Shape shapeAtXY(double x, double y){
        for(int i = shapes.size() - 1; i >= 0; i--) {
            Shape s = shapes.get(i);
            if(x <= s.getX() && x >= s.getX() + s.getWidth() &&
                    y >= s.getY() && y <= s.getY() + s.getHeight()) {
                return s;
            }
        }

        return null; // no shape is here at x, y
    }
}
