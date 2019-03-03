package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.util.AttributeSet;
import android.view.*;
import android.app.*;

public class InventoryView extends View {

    private static Boolean isEditorMode;

    public InventoryView(Context context, AttributeSet attrs) { super(context, attrs); }

    private void init() {
        Activity activityContext = (Activity) getContext();
        isEditorMode = activityContext instanceof EditorActivity;
    }

    private void handleActivity() {
        if (isEditorMode) {
            // handle editor activity (shapes remain constant and immutable across pages
            // and throughout the entire game
                 // perhaps have preset "boxes" that contain the shapes
        } else {
            // handle play activity (shapes remain constant, however the user is able to
            // drag shapes from the screen and they will move from inventory to page)
                  // inventory is free-flowing (user can drag shape from page to inventory
                  // and it will be positioned wherever the user leaves it)
        }
    }
}
