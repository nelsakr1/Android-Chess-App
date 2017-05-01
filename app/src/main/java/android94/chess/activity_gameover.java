package android94.chess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class activity_gameover extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover);
    }

    public void mainmenu (View view) {

        Intent intent = new Intent(this, activity_main.class);
        startActivity(intent);
    }

    public void record (View view) {

        Intent intent = new Intent(this, activity_record.class);
        startActivity(intent);
    }
}
