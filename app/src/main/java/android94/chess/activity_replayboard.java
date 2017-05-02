package android94.chess;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class activity_replayboard extends AppCompatActivity {

    public static Piece[][] replayBoard;
    public static int[][] turnRecorder;
    public static int turnCounter;
    public static boolean gameOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replayboard);

        //TODO set turnRecorder

        gameOver = false;
        replayBoard = Gameplay.createNewBoard();

        // place pieces in their initial positions
        ((ImageButton)findViewById(R.id.a1)).setImageResource(R.drawable.wrook);
        ((ImageButton)findViewById(R.id.b1)).setImageResource(R.drawable.wknight);
        ((ImageButton)findViewById(R.id.c1)).setImageResource(R.drawable.wbishop);
        ((ImageButton)findViewById(R.id.d1)).setImageResource(R.drawable.wqueen);
        ((ImageButton)findViewById(R.id.e1)).setImageResource(R.drawable.wking);
        ((ImageButton)findViewById(R.id.f1)).setImageResource(R.drawable.wbishop);
        ((ImageButton)findViewById(R.id.g1)).setImageResource(R.drawable.wknight);
        ((ImageButton)findViewById(R.id.h1)).setImageResource(R.drawable.wrook);
        ((ImageButton)findViewById(R.id.a2)).setImageResource(R.drawable.wpawn);
        ((ImageButton)findViewById(R.id.b2)).setImageResource(R.drawable.wpawn);
        ((ImageButton)findViewById(R.id.c2)).setImageResource(R.drawable.wpawn);
        ((ImageButton)findViewById(R.id.d2)).setImageResource(R.drawable.wpawn);
        ((ImageButton)findViewById(R.id.e2)).setImageResource(R.drawable.wpawn);
        ((ImageButton)findViewById(R.id.f2)).setImageResource(R.drawable.wpawn);
        ((ImageButton)findViewById(R.id.g2)).setImageResource(R.drawable.wpawn);
        ((ImageButton)findViewById(R.id.h2)).setImageResource(R.drawable.wpawn);

        ((ImageButton)findViewById(R.id.a8)).setImageResource(R.drawable.brook);
        ((ImageButton)findViewById(R.id.b8)).setImageResource(R.drawable.bknight);
        ((ImageButton)findViewById(R.id.c8)).setImageResource(R.drawable.bbishop);
        ((ImageButton)findViewById(R.id.d8)).setImageResource(R.drawable.bqueen);
        ((ImageButton)findViewById(R.id.e8)).setImageResource(R.drawable.bking);
        ((ImageButton)findViewById(R.id.f8)).setImageResource(R.drawable.bbishop);
        ((ImageButton)findViewById(R.id.g8)).setImageResource(R.drawable.bknight);
        ((ImageButton)findViewById(R.id.h8)).setImageResource(R.drawable.brook);
        ((ImageButton)findViewById(R.id.a7)).setImageResource(R.drawable.bpawn);
        ((ImageButton)findViewById(R.id.b7)).setImageResource(R.drawable.bpawn);
        ((ImageButton)findViewById(R.id.c7)).setImageResource(R.drawable.bpawn);
        ((ImageButton)findViewById(R.id.d7)).setImageResource(R.drawable.bpawn);
        ((ImageButton)findViewById(R.id.e7)).setImageResource(R.drawable.bpawn);
        ((ImageButton)findViewById(R.id.f7)).setImageResource(R.drawable.bpawn);
        ((ImageButton)findViewById(R.id.g7)).setImageResource(R.drawable.bpawn);
        ((ImageButton)findViewById(R.id.h7)).setImageResource(R.drawable.bpawn);

    }

    public void nextButton (View view) {

        if (!gameOver) {
            if (turnRecorder[0][turnCounter] == -1) {
                gameOver = true;
            }
            else if (turnRecorder[0][turnCounter] == -2) {
                Context context = view.getContext();
                CharSequence text = "Player White Wins.";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
            else if (turnRecorder[0][turnCounter] == -3) {
                Context context = view.getContext();
                CharSequence text = "Player Black Wins.";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
            else if (turnRecorder[0][turnCounter] == -4) {
                Context context = view.getContext();
                CharSequence text = "Draw.";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
            else {
                Movement.movePiece(turnRecorder[0][turnCounter], turnRecorder[1][turnCounter], turnRecorder[2][turnCounter], turnRecorder[3][turnCounter], replayBoard);
                activity_play.changeDrawableAlt(replayBoard);
                turnCounter++;
            }
        }
        else {
            Context context = view.getContext();
            CharSequence text = "Game is over.";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }
}
