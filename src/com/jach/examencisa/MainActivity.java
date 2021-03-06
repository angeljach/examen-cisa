package com.jach.examencisa;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.InputType;
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
import com.jach.examencisa.settings.SettingsActivity;
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
	private Button btnNextQuestion;
	private Button btnPrevQuestion;
	
	private QuestionHandler qh;
	private DatabaseHelper dbh;
	
	private int lastQuestion;
	
	
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

		//---|| Only if lastQuestion in [1, 1100] else 1
		lastQuestion = Integer.parseInt(lq);
		Log.d(TAG, "lastQuestion=" + lastQuestion);
				
		mainScrollView = (ScrollView)findViewById(R.id.principal_scroll_view);
		textStatQuestion = (TextView) findViewById(R.id.text_stat_question);
		textQuestion = (TextView) findViewById(R.id.text_question);
		textAnswExpl = (TextView) findViewById(R.id.text_answer_explanation);
		radioGroup = (RadioGroup) findViewById(R.id.radio_selection_group);
		btnNewQuestion = (Button) findViewById(R.id.btn_new_question);
		btnNextQuestion = (Button) findViewById(R.id.btn_next_question);
		btnPrevQuestion = (Button) findViewById(R.id.btn_prev_question);
		
		addListenerOnNewQuestionButton(btnNewQuestion);
		addListenerOnNextQuestionButton(btnNextQuestion);
		addListenerOnPrevQuestionButton(btnPrevQuestion);
		
        init();
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
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
    		case R.id.action_settings:
    			// Launch settings activity
    		    Intent i = new Intent(this, SettingsActivity.class);
    		    startActivity(i);
    			return true;
			case R.id.action_goto_question:
				promptPageNumber();
				return true;
			default:
				Toast.makeText(MainActivity.this, "other", Toast.LENGTH_SHORT).show();
				return super.onOptionsItemSelected(item);
	    }
    }
    
    private void promptPageNumber() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle(getString(R.string.title_promt_goto_question));
		alert.setMessage(getString(R.string.title_promt_msg_goto_question));

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_NUMBER);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				String errorMsg = String.format(getString(R.string.msg_error_wrong_question_number), ExamCisaConstants.MAX_NUMBER_QUESTIONS);
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
    	//---|| Get preferences.
    	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
    	boolean isRandomOrder = settings.getBoolean(getString(R.string.pref_randomOrder_key), false);

    	Log.d(TAG, "isRandomOrder=" + (isRandomOrder ? "T" : "F"));
    	
    	if ((idQuestion > ExamCisaConstants.MAX_NUMBER_QUESTIONS) || (idQuestion < 1)) {
    		//Reset the counter.
    		idQuestion = 1;
    	}
    	
    	//---|| Get the new question and update the lastQuestion on DB.
    	QuestionVO q = qh.questionById(idQuestion);
    	
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
    	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
    	boolean isRandomOrder = settings.getBoolean(getString(R.string.pref_randomOrder_key), false);
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
	
	
	private void addListenerOnRadioButton(RadioButton radioButton, 
			final boolean isCorrect, final int idCorrectAnswer) {
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
				
				String toastMsg = (isCorrect) ? getString(R.string.msg_correct_answer) : getString(R.string.msg_wrong_answer);
				Toast.makeText(MainActivity.this, toastMsg, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	
	private void addListenerOnNewQuestionButton(Button newQuestionButton) {
		newQuestionButton.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View arg0) { init(); }
		});
	}
	
	private void addListenerOnNextQuestionButton(Button nextQuestionButton) {
		nextQuestionButton.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View arg0) { init(); }
		});
	}
	
	private void addListenerOnPrevQuestionButton(Button prevQuestionButton) {
		prevQuestionButton.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View arg0) { init(--lastQuestion); }
		});
	}

	/*
	@Override
	protected void onResume() {
		
	}*/
	
}
