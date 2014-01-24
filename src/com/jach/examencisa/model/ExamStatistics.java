package com.jach.examencisa.model;

import java.util.Date;

import android.provider.BaseColumns;

public class ExamStatistics implements BaseColumns {
	
	public static final String TABLE_NAME = "ExamStatistics";

	public static final String COL_LAST_QUESTION_DATE = "last_question_date";
	public static final String COL_WAS_CORRECT = "was_correct";
	
	private Date lastQuestionDate;
    private boolean wasCorrect;
    
	public ExamStatistics(Date lastQuestionDate, boolean wasCorrect) {
		super();
		this.lastQuestionDate = lastQuestionDate;
		this.wasCorrect = wasCorrect;
	}

	public Date getLastQuestionDate() {
		return lastQuestionDate;
	}

	public void setLastQuestionDate(Date lastQuestionDate) {
		this.lastQuestionDate = lastQuestionDate;
	}

	public boolean isWasCorrect() {
		return wasCorrect;
	}

	public void setWasCorrect(boolean wasCorrect) {
		this.wasCorrect = wasCorrect;
	}
	
	

}
