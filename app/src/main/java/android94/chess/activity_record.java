package android94.chess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class activity_record extends AppCompatActivity {

    public static int[][] turnRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        // get turnRecorder from bundle
        Bundle bundle = getIntent().getExtras();
        turnRecorder = (int[][]) bundle.getSerializable("recordserial");

    }

    public void createRecording (View view) {

        List<int[][]> record = new ArrayList<>();
        List<String> recordName = new ArrayList<>();

        recordName = Gameplay.readName(this);
        record = Gameplay.readRecord(this);

        TextView textView = (TextView) this.findViewById(R.id.recordingname);
        recordName.add(textView.getText().toString());

        record.add(turnRecorder);

        Gameplay.writeName(recordName, this);
        Gameplay.writeRecord(record, this);

        // toast notification confirming save
        Toast.makeText(view.getContext(), "Game Recorded.", Toast.LENGTH_SHORT).show();

        // return to main menu
        Intent intent = new Intent(this, activity_main.class);
        startActivity(intent);

    }
}
