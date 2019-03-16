package edu.stanford.cs108.bunnyworld;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class MakeCharacter extends AppCompatActivity implements OnClickListener {

    private Button saveButton;
    private DrawView drawView;
    private Bitmap savedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_character);
        saveButton = findViewById(R.id.save);
        saveButton.setOnClickListener(this);
        drawView = findViewById(R.id.drawView);
    }

    @Override
    public void onClick(View view) {

        if (view.getId()==R.id.save){
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Save your character?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //Define a bitmap with the same size as the view
                    Bitmap b = Bitmap.createBitmap(drawView.getWidth(), drawView.getHeight(),Bitmap.Config.ARGB_8888);
                    //Bind a canvas to it
                    Canvas canvas = new Canvas(b);
                    //Get the view's background
                    Drawable bgDrawable =drawView.getBackground();
                    if (bgDrawable!=null)
                        //has background drawable, then draw it on the canvas
                        bgDrawable.draw(canvas);
                    else
                        //does not have background drawable, then draw white background on the canvas
                        canvas.drawColor(Color.WHITE);
                    // draw the view on the canvas
                    drawView.draw(canvas);
                    //return the bitmap
                    savedImage = b;

                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            saveDialog.show();
        }
    }

    public Bitmap getCharacter () {
        if (savedImage != null) {
            return savedImage;
        }
        return null;
    }
}
