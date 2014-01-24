package com.jach.examencisa;

import com.jach.examencisa.db.DatabaseHelper;
import com.jach.examencisa.model.Question;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        
        
        DatabaseHelper dbh = new DatabaseHelper(this);
        Question q = dbh.randomQuestion();
        
        String message = q.getQuestion();

        // Create the text view
        TextView textView = new TextView(this);
        textView.setTextSize(15);
        textView.setText(message);

        // Set the text view as the activity layout
        setContentView(textView);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
