package edu.stanford.cs108.bunnyworld;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.content.Context;
import android.widget.Toast;

import static java.security.AccessController.getContext;

/**
 * Main Activity for BunnyWorld
 */
public class MainActivity extends AppCompatActivity {

    AnimationDrawable rocketAnimation;

    /**
     * Loads initial animations upon opening game
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView rocketImage = findViewById(R.id.backgroundim);
        rocketImage.setBackgroundResource(R.drawable.animation);
        rocketAnimation = (AnimationDrawable) rocketImage.getBackground();
        rocketAnimation.start();
    }

    /**
     * Launches editor mode
     * @param view the EditorActivity view
     */
    public void makeGame(View view) {
        Intent intent = new Intent(this,EditorActivity.class);
        startActivity(intent);
    }

    /**
     * Launches play mode
     * @param view the PlayActivity view
     */
    public void playGame(View view) {
        Intent intent = new Intent(this,PlayActivity.class);
        startActivity(intent);
    }

    /**
     * Launches character creator
     * @param view the MakeCharacter view
     */
    public void makeCharacter(View view) {
        Intent intent = new Intent(this,MakeCharacter.class);
        startActivity(intent);
    }
}
