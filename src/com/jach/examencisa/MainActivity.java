package com.jach.examencisa;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.jach.examencisa.db.DatabaseHelper;
import com.jach.examencisa.model.ExamStatistics;
import com.jach.examencisa.util.ExamCisaConstants;
import com.jach.examencisa.vo.AnswerVO;
import com.jach.examencisa.vo.QuestionVO;

public class MainActivity extends Activity {

	private ScrollView mainScrollView;
	private RadioGroup radioGroup;
	private TextView textStatQuestion;
	private TextView textQuestion;
	private TextView textAnswExpl;
	private Button btnNewQuestion;
	
	private QuestionHandler qh;
	private DatabaseHelper dbh;
	
	private int lastQuestion;
	private static boolean isRandomOrder;
	
	private final static int COLOR_ANSW_CORRECT = Color.rgb(15,160,41);
	private final static int COLOR_ANSW_WRONG = Color.RED;
	private static final String TAG = "MainActivity";
	 
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
    	// http://rocketships.ca/blog/how-to-dynamically-add-radio-buttons/
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		qh = new QuestionHandler(this);
		dbh = new DatabaseHelper(this);
		
		String lq = dbh.getPropertyLastQuestion();
		String ro = dbh.getPropertyRandomOrder();
		
		lastQuestion = Integer.parseInt(lq);
		Log.i(TAG, "lastQuestion=" + lastQuestion);
		isRandomOrder = ro.equals("1") ? true : false;
		Log.i(TAG, "isRandomOrder=" + (isRandomOrder ? "T" : "F"));
				
		mainScrollView = (ScrollView)findViewById(R.id.principal_scroll_view);
		textStatQuestion = (TextView) findViewById(R.id.text_stat_question);
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
        
    public boolean onOptionsItemSelected(MenuItem item) {
    	/* 
    	 * http://mobile.tutsplus.com/tutorials/android/android-sdk-implement-an-options-menu/
    	 * This sample code starts new Activity screens for each item chosen.
    	 * If you opt to do this, you will need to add an Activity class for each option
    	 * in your application Java code as well as in the Manifest file.
    	 * 
    	 * startActivity(new Intent(this, About.class));
    	 */
    	 
    	switch (item.getItemId()) {
			case R.id.action_order:
				changeOrderSequence();
				this.init();
				return true;
			case R.id.action_goto_question:
				promptPageNumber();
				return true;
			default:
				Toast.makeText(MainActivity.this, "other", Toast.LENGTH_SHORT).show();
				return super.onOptionsItemSelected(item);
	    }
    }
    
    private void changeOrderSequence() {
    	//---|| Change randomOrder and update the value on DB.
    	dbh.setPropertyRandomOrder((isRandomOrder=!isRandomOrder) ? "1" : "0");
    	Log.i(TAG, "Cambio en el orden de las preguntas a: ".concat(isRandomOrder ? "Aleatorio" : "Ordenado"));
    }
    
    private void promptPageNumber() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Saltar a página");
		alert.setMessage("# Página");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				String errorMsg = "Debes introducir un n�mero en el rango de [1, " + ExamCisaConstants.MAX_NUMBER_QUESTIONS + "]";
				try {
					int idQuestion = Integer.parseInt(value);
					if (idQuestion < 1 || idQuestion > ExamCisaConstants.MAX_NUMBER_QUESTIONS) {
						Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
					} else {
						//---|| Goto question.
						init(idQuestion);
					}
				} catch(NumberFormatException ex) {
					Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
				}
				
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});

		alert.show();
    }
    
    public void init(int idQuestion) {
    	//---|| Get the new question and update the lastQuestion on DB.
    	QuestionVO q = qh.questionById(idQuestion);
    	
    	//Reset the counter when lasQuestion is the last question and the order is consecutive.
    	if ((lastQuestion == ExamCisaConstants.MAX_NUMBER_QUESTIONS) && !isRandomOrder) {
    		lastQuestion = 0;
    	}
    	lastQuestion = q.getId();
    	dbh.setPropertyLastQuestion(Integer.toString(lastQuestion));
    	List<AnswerVO> lstAnswers = qh.answerFromQuestion(lastQuestion);
	
    	//---|| Get statistics from last question.
    	ExamStatistics exs = dbh.getExamStatisticsFromQuestion(q.getId());
    	
        // Return to the top of the page.
        mainScrollView.fullScroll(View.FOCUS_UP);
        
        textStatQuestion.setText(exs.getLastQuestionDate());
        textStatQuestion.setTextColor((exs.getWasCorrect() == 1) 
        		? COLOR_ANSW_CORRECT : COLOR_ANSW_WRONG);
        
        textQuestion.setText(Html.fromHtml(String.format(
        		"<b>#%d/%d</b> (%s)<br/><br/>%s<br/>",
        		q.getId(), ExamCisaConstants.MAX_NUMBER_QUESTIONS, 
        		isRandomOrder ? "Aleatorio" : "Secuencial",
        		q.getQuestion())));
		textAnswExpl.setText(Html.fromHtml("<br/>".concat(q.getExplanation())));
		textAnswExpl.setVisibility(View.INVISIBLE);
		
		btnNewQuestion.setVisibility(View.INVISIBLE);
		
		addRadioButtons(lstAnswers);
    }
    
    public void init() {
    	init(isRandomOrder 
    			? (new Random().nextInt(ExamCisaConstants.MAX_NUMBER_QUESTIONS)+1) 
				: (++lastQuestion));    	
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
			radioButton.setText(Html.fromHtml(answer.getAnswer()));
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
				
				//---|| Inserting statistic (last_question_date and was_correct) to DB.
				dbh.setExamStatistics(new ExamStatistics(
						lastQuestion, 
						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), 
						isCorrect ? 1 : 0));
				
				String toastMsg = (isCorrect) ? "CORRECTO" : "INCORRECTO";
				Toast.makeText(MainActivity.this, toastMsg, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	
	private void addListenerOnNewQuestionButton(Button newQuestionButton) {
		newQuestionButton.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View arg0) { init(); }
		});
	}
	
}
