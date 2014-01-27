package com.jach.examencisa;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jach.examencisa.db.DatabaseHelper;
import com.jach.examencisa.vo.AnswerVO;
import com.jach.examencisa.vo.QuestionVO;

public class MainActivity extends Activity {

	private final static int TEXT_SIZE = 12; 
	RadioGroup radioGroup;
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
    	// http://rocketships.ca/blog/how-to-dynamically-add-radio-buttons/
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		DatabaseHelper dbh = new DatabaseHelper(this);
        QuestionVO q = dbh.randomQuestion();
        List<AnswerVO> lstAnswers = dbh.answerFromQuestion(q.getId());
		
		radioGroup = (RadioGroup) findViewById(R.id.radio_selection_group);
		
		TextView textQuestion = (TextView) findViewById(R.id.text_question);
		textQuestion.setTextSize(TEXT_SIZE);
		textQuestion.setText(Html.fromHtml("Pregunta <b>#".concat(Integer.toString(q.getId())).concat("</b>").concat(q.getQuestion())));
		
		//int numberOfRadioButtons = 7;
		//addRadioButtons(numberOfRadioButtons);
		addRadioButtons(lstAnswers);
	}
    
	private void addRadioButtons(int numButtons) {
		for (int i = 0; i < numButtons; i++) {
			RadioButton radioButton = new RadioButton(this);

			// set the values that you would otherwise hardcode in the xml...
			radioButton.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

			// label the button...
			radioButton.setText("Radio Button #" + i);
			radioButton.setId(i);

			// add it to the group.
			radioGroup.addView(radioButton, i);
		}
	}
	
	private void addRadioButtons(List<AnswerVO> lstAnswers) {
		int i=0;;
		for (AnswerVO answer : lstAnswers) {
			RadioButton radioButton = new RadioButton(this);
			
			// set the values that you would otherwise hardcode in the xml...
			radioButton.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			radioButton.setText(Html.fromHtml(answer.getAnswer().replace("<p>", "").replace("</p>", "")));
			radioButton.setTextSize(TEXT_SIZE);
			radioButton.setId(i);
			radioGroup.addView(radioButton, i);
			i++;
		}
	}
    
    /*
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
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
