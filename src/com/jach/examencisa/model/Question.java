package com.jach.examencisa.model;

import java.util.Date;

import android.provider.BaseColumns;

public class Question implements BaseColumns {
	
	public static final String TABLE_NAME = "Question";
	
	public static final String COL_QUESTION = "question";
	public static final String COL_EXPLANATION = "explanation";
	public static final String COL_CATEGORY = "category";
	public static final String COL_LAST_QUESTION_DATE = "last_question_date";
	public static final String COL_WAS_CORRECT = "was_correct";
	
	private int id;
    private String question;
    private String explanation;
    private String category;
    private Date lastQuestionDate;
    private boolean wasCorrect;
    
    public Question() {
		// TODO Auto-generated constructor stub
	}
    
	public Question(int id, String question, String explanation,
			String category, Date lastQuestionDate, boolean wasCorrect) {
		super();
		this.id = id;
		this.question = question;
		this.explanation = explanation;
		this.category = category;
		this.lastQuestionDate = lastQuestionDate;
		this.wasCorrect = wasCorrect;
	}




	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getExplanation() {
		return explanation;
	}
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
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
