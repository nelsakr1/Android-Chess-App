package android94.chess;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class activity_record extends AppCompatActivity {

    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        context = this;

        //TODO set turnRecorder
    }

    public void createRecording (View view) {

        List<int[][]> record = null;
        List<String> recordName = null;

        // read/create game recording data
        File file1 = new File(context.getFilesDir(), "record.dat");
        if (file1.exists()) {
            try {
                FileInputStream fis = context.openFileInput("record.dat");
                ObjectInputStream is = new ObjectInputStream(fis);
                record = (List<int[][]>) is.readObject();
                is.close();
                fis.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                file1.createNewFile();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        // add new recording to the list
        record.add(activity_play.turnRecorder);

        // update the data files with new recording
        try {
            FileOutputStream fos = context.openFileOutput("record.dat", context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(record);
            os.close();
            fos.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        // read/create game recording name data
        File file2 = new File(context.getFilesDir(), "recordName.dat");
        if (file2.exists()) {
            try {
                FileInputStream fis = context.openFileInput("recordName.dat");
                ObjectInputStream is = new ObjectInputStream(fis);
                recordName = (List<String>) is.readObject();
                is.close();
                fis.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                file2.createNewFile();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        // add new recording to the list
        TextView textView = (TextView) ((Activity) context).findViewById(R.id.recordingname);
        recordName.add(textView.getText().toString());

        // update the data files with new recording
        try {
            FileOutputStream fos = context.openFileOutput("recordName.dat", context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(recordName);
            os.close();
            fos.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }





        // toast notification confirming save
        Context context = view.getContext();
        CharSequence text = "Game Recorded";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        // return to main menu
        Intent intent = new Intent(this, activity_main.class);
        startActivity(intent);

    }
}
