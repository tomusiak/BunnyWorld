package edu.stanford.cs108.bunnyworld;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    AnimationDrawable rocketAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView rocketImage = findViewById(R.id.backgroundim);
        rocketImage.setBackgroundResource(R.drawable.animation);
        rocketAnimation = (AnimationDrawable) rocketImage.getBackground();
        rocketAnimation.start();
    }

    public void makeGame(View view) {
        Intent intent = new Intent(this,EditorActivity.class);
        startActivity(intent);
    }

    public void playGame(View view) {
        Intent intent = new Intent(this,PlayActivity.class);
        startActivity(intent);
    }
}
