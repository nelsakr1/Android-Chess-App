package android94.chess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class activity_main extends AppCompatActivity {

    public static int[][] positions = new int[2][2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void play (View view) {
        Intent intent = new Intent(this, activity_play.class);
        startActivity(intent);
    }

    public void record (View view) {
        Intent intent = new Intent(this, activity_play.class);
        startActivity(intent);
    }

    public void replay (View view) {
        Intent intent = new Intent(this, activity_play.class);
        startActivity(intent);
    }

    public void instructions (View view) {
        Intent intent = new Intent(this, activity_play.class);
        startActivity(intent);
    }


}
