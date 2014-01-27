package com.jach.examencisa;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.widget.TextView;

import com.jach.examencisa.db.DatabaseHelper;
import com.jach.examencisa.vo.AnswerVO;
import com.jach.examencisa.vo.QuestionVO;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        
        
        DatabaseHelper dbh = new DatabaseHelper(this);
        QuestionVO q = dbh.randomQuestion();
        List<AnswerVO> lstAnsw = dbh.answerFromQuestion(q.getId());
        
        String message = Integer.toString(q.getId()).concat(") ")
        		.concat(q.getQuestion()).concat("<\br><\br>")
        		.concat(lstAnsw.get(0).getAnswer()).concat("<\br><\br>")
        		.concat(lstAnsw.get(1).getAnswer()).concat("<\br><\br>")
        		.concat(lstAnsw.get(2).getAnswer()).concat("<\br><\br>")
        		.concat(lstAnsw.get(3).getAnswer()).concat("<\br><\br>");

        // Create the text view
        TextView textView = new TextView(this);
        textView.setTextSize(12);
        textView.setText(Html.fromHtml(message));

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
