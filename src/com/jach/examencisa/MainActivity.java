package com.jach.examencisa;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jach.examencisa.db.DatabaseHelper;
import com.jach.examencisa.vo.AnswerVO;
import com.jach.examencisa.vo.QuestionVO;

public class MainActivity extends Activity {

	private RadioGroup radioGroup;
	private TextView textQuestion;
	private TextView textAnswExpl;
	private Button btnNewQuestion;
	
	//private final static int TEXT_SIZE = 12; 
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
    	// http://rocketships.ca/blog/how-to-dynamically-add-radio-buttons/
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		DatabaseHelper dbh = new DatabaseHelper(this);
        QuestionVO q = dbh.randomQuestion();
        List<AnswerVO> lstAnswers = dbh.answerFromQuestion(q.getId());
		
		radioGroup = (RadioGroup) findViewById(R.id.radio_selection_group);
		
		textQuestion = (TextView) findViewById(R.id.text_question);
		//textQuestion.setTextSize(TEXT_SIZE);
		textQuestion.setText(Html.fromHtml("Pregunta <b>#".concat(Integer.toString(q.getId())).concat("</b>").concat(q.getQuestion())));
		
		textAnswExpl = (TextView) findViewById(R.id.text_answer_explanation);
		//textAnswExpl.setTextSize(TEXT_SIZE);
		textAnswExpl.setText(Html.fromHtml(q.getExplanation()));
		textAnswExpl.setVisibility(View.INVISIBLE);
		
		btnNewQuestion = (Button) findViewById(R.id.btn_new_question);
		//btnNewQuestion.setTextSize(TEXT_SIZE);
		btnNewQuestion.setVisibility(View.INVISIBLE);
		
		addRadioButtons(lstAnswers);
	}
    
	private void addRadioButtons(List<AnswerVO> lstAnswers) {
		for (AnswerVO answer : lstAnswers) {
			RadioButton radioButton = new RadioButton(this);
			
			// set the values that you would otherwise hardcode in the xml...
			radioButton.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			radioButton.setText(Html.fromHtml(answer.getAnswer().replace("<p>", "").replace("</p>", "")));
			//radioButton.setTextSize(TEXT_SIZE);
			radioButton.setId(answer.getSequence());
			
			addListenerOnRadioButton(radioButton, answer.isCorrect());
			
			radioGroup.addView(radioButton, answer.getSequence());
		}
	}
	
	
	public void addListenerOnRadioButton(RadioButton radioButton, final boolean isCorrect) {
		radioButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//int selectedId = radioGroup.getCheckedRadioButtonId();
				//RadioButton selectedRadioButton = (RadioButton) findViewById(selectedId);
	 
				textAnswExpl.setTextColor(isCorrect ? Color.rgb(15,160,41) : Color.rgb(180,4,4));
				textAnswExpl.setVisibility(View.VISIBLE);
				btnNewQuestion.setVisibility(View.VISIBLE);
				
				for (int i=0 ; i<radioGroup.getChildCount() ; i++) {
					((RadioButton) radioGroup.getChildAt(i)).setEnabled(false);
				}
				
				
				String toastMsg = (isCorrect) ? "CORRECTO" : "INCORRECTO";
				Toast.makeText(MainActivity.this, toastMsg, Toast.LENGTH_SHORT).show();
			}
		});
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
