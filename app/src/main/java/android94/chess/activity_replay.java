package android94.chess;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class activity_replay extends AppCompatActivity {

    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay);

        context = this;

        //TODO set turnRecorder

        List<String> recordName = new ArrayList<String>();
        recordName.add("example");

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, recordName);

        ListView listView = (ListView) findViewById(R.id.record_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
            {
                // int value = Integer.parseInt((String) adapter.getItemAtPosition(position));

                Intent intent = new Intent(context, activity_replayboard.class);
                intent.putExtra("listPosition", position);
                startActivity(intent);

            }
        });

    }

    public void byname (View view) {

    }

    public void bydate (View view) {

    }

}
