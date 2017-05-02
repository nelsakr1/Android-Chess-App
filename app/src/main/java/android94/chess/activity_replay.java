package android94.chess;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class activity_replay extends AppCompatActivity {

    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay);

        context = this;

        List<String> recordName = Gameplay.readName(this);

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, recordName);
        ListView listView = (ListView) findViewById(R.id.record_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {

                Intent intent = new Intent(context, activity_replayboard.class);

                List<int[][]> record = Gameplay.readRecord(context);
                int[][] gameBeingPlayed = record.get(position);

                Bundle bundle = new Bundle();
                bundle.putSerializable("recordserial", gameBeingPlayed);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

    }

    public void sort (View view) {

        Button button = (Button) this.findViewById(R.id.sort_button);
        button.getText();

        if (button.getText().equals("Sort By Name")) {
            button.setText("Sort By Date");
        }
        else {
            button.setText("Sort By Name");
        }


    }


}
