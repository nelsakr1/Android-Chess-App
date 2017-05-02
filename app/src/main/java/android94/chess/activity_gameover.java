package android94.chess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class activity_gameover extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover);

        // set end game condition text
        TextView t = (TextView) findViewById(R.id.gameover_text);
        if (activity_play.stalemate) {
            t.setText("Draw");
        }
        else {
            if (activity_play.whiteWins) {
                t.setText("Player White Wins");
            }
            else {
                t.setText("Player Black Wins");
            }
        }
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
