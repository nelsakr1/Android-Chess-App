package android94.chess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class activity_main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> name = Gameplay.readName(this);
        List<int[][]> record = Gameplay.readRecord(this);


        File file1 = new File("/data/user/0/android94.chess/files/names");
        File file2 = new File("/data/user/0/android94.chess/files/recordings");

        if (!file1.exists()) {
            try {
                file1.createNewFile();
                List<String> emptyName = new ArrayList<String>();
                Gameplay.writeName(emptyName, this);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        if (!file2.exists()) {
            try {
                file2.createNewFile();
                List<int[][]> emptyRecord = new ArrayList<int[][]>();
                Gameplay.writeRecord(emptyRecord, this);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }

        // have to force it to crash on first install for some reason
        name.size();
        record.size();


    }

    public void play (View view) {
        Intent intent = new Intent(this, activity_play.class);
        startActivity(intent);
    }

    public void replay (View view) {
        Intent intent = new Intent(this, activity_replay.class);
        startActivity(intent);
    }

    public void instructions (View view) {
        Intent intent = new Intent(this, activity_instructions.class);
        startActivity(intent);
    }


}
