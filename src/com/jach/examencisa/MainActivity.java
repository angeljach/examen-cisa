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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.jach.examencisa.vo.AnswerVO;
import com.jach.examencisa.vo.QuestionVO;

public class MainActivity extends Activity {

	private ScrollView mainScrollView;
	private RadioGroup radioGroup;
	private TextView textQuestion;
	private TextView textAnswExpl;
	private Button btnNewQuestion;
	
	private final QuestionHandler qh = new QuestionHandler(this);
	private final static int COLOR_ANSW_CORRECT = Color.rgb(15,160,41);
	private final static int COLOR_ANSW_WRONG = Color.RED;
	 
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
    	// http://rocketships.ca/blog/how-to-dynamically-add-radio-buttons/
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mainScrollView = (ScrollView)findViewById(R.id.principal_scroll_view);
		textQuestion = (TextView) findViewById(R.id.text_question);
		textAnswExpl = (TextView) findViewById(R.id.text_answer_explanation);
		radioGroup = (RadioGroup) findViewById(R.id.radio_selection_group);
		btnNewQuestion = (Button) findViewById(R.id.btn_new_question);
		
		addListenerOnNewQuestionButton(btnNewQuestion);
		
        init();
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void init() {
    	QuestionVO q = qh.randomQuestion();
        List<AnswerVO> lstAnswers = qh.answerFromQuestion(q.getId());
		
        // Return to the top of the page.
        mainScrollView.fullScroll(View.FOCUS_UP);
        
		textQuestion.setText(Html.fromHtml("Pregunta <b>#".concat(Integer.toString(q.getId()))
				.concat("</b><br/><br/>").concat(q.getQuestion()).replace("<p>", "").replace("</p>", "").concat("<br/>")));
		
		textAnswExpl.setText(Html.fromHtml("<br/>".concat(q.getExplanation())));
		textAnswExpl.setVisibility(View.INVISIBLE);
		
		btnNewQuestion.setVisibility(View.INVISIBLE);
		
		addRadioButtons(lstAnswers);
    }
    
	private void addRadioButtons(List<AnswerVO> lstAnswers) {
		//---|| Initialize the radioGroup.
		if (radioGroup.getChildCount() > 0) {
			radioGroup.removeAllViews();
		}
		
		int idCorrectAnswer = -1;
		for (AnswerVO answer : lstAnswers) {
			if (answer.isCorrect()) {
				idCorrectAnswer = answer.getSequence();
			}
		}
		
		for (AnswerVO answer : lstAnswers) {
			RadioButton radioButton = new RadioButton(this);
			
			// set the values that you would otherwise hardcode in the xml...
			radioButton.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			radioButton.setText(Html.fromHtml(answer.getAnswer().replace("<p>", "").replace("</p>", "")));
			radioButton.setId(answer.getSequence());
			radioButton.setTextColor((answer.getSequence() % 2) == 0 
					? Color.rgb(122, 122, 122) 
					: Color.rgb(0, 0, 0));
			
			addListenerOnRadioButton(radioButton, answer.isCorrect(), idCorrectAnswer);
			
			radioGroup.addView(radioButton, answer.getSequence());
		}
	}
	
	
	private void addListenerOnRadioButton(RadioButton radioButton, final boolean isCorrect, final int idCorrectAnswer) {
		radioButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				textAnswExpl.setTextColor(isCorrect ? COLOR_ANSW_CORRECT : COLOR_ANSW_WRONG);
				textAnswExpl.setVisibility(View.VISIBLE);
				btnNewQuestion.setVisibility(View.VISIBLE);
				
				//Disable all radio buttons.
				for (int i=0 ; i<radioGroup.getChildCount() ; i++) {
					((RadioButton) radioGroup.getChildAt(i)).setTextColor((idCorrectAnswer == i) ? COLOR_ANSW_CORRECT : COLOR_ANSW_WRONG);
					((RadioButton) radioGroup.getChildAt(i)).setEnabled(false);
				}
				
				
				String toastMsg = (isCorrect) ? "CORRECTO" : "INCORRECTO";
				Toast.makeText(MainActivity.this, toastMsg, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void addListenerOnNewQuestionButton(Button newQuestionButton) {
		newQuestionButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) { 
				init();
			}
		});
	}
	
}
