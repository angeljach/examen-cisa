package com.jach.examencisa.model;

import android.provider.BaseColumns;

public class ExamStatistics implements BaseColumns {
	
	public static final String TABLE_NAME = "EXAM_STATISTICS";

	public static final String COL_LAST_QUESTION_DATE = "last_question_date";
	public static final String COL_WAS_CORRECT = "was_correct";
	
	private int idQuestion;
	private String lastQuestionDate;
    private int wasCorrect;
    
	public ExamStatistics(int idQuestion, String lastQuestionDate, int wasCorrect) {
		super();
		this.idQuestion = idQuestion;
		this.lastQuestionDate = lastQuestionDate;
		this.wasCorrect = wasCorrect;
	}

	public int getIdQuestion() {
		return idQuestion;
	}
	
	public void setIdQuestion(int idQuestion) {
		this.idQuestion = idQuestion;
	}
	
	public String getLastQuestionDate() {
		return lastQuestionDate;
	}

	public void setLastQuestionDate(String lastQuestionDate) {
		this.lastQuestionDate = lastQuestionDate;
	}

	public int getWasCorrect() {
		return wasCorrect;
	}

	public void setWasCorrect(int wasCorrect) {
		this.wasCorrect = wasCorrect;
	}

}
