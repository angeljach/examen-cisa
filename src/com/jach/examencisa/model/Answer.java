package com.jach.examencisa.model;

import android.provider.BaseColumns;

public class Answer implements BaseColumns {
	
	public static final String TABLE_NAME = "Answer";
	
	public static final String COL_ID_QUESTION = "id_question";
	public static final String COL_ANSWER = "answer";
	public static final String COL_IS_ANSWER = "is_answer";
	public static final String COL_SEQUENCE = "sequence";
	
	private int id;
    private int idQuestion;
    private String answer;
    private boolean isAnswer;
    private int sequence;
	
    public Answer() {
		// TODO Auto-generated constructor stub
	}
    
    public Answer(int id, int idQuestion, String answer, boolean isAnswer,
			int sequence) {
		super();
		this.id = id;
		this.idQuestion = idQuestion;
		this.answer = answer;
		this.isAnswer = isAnswer;
		this.sequence = sequence;
	}



	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdQuestion() {
		return idQuestion;
	}
	public void setIdQuestion(int idQuestion) {
		this.idQuestion = idQuestion;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public boolean isAnswer() {
		return isAnswer;
	}
	public void setAnswer(boolean isAnswer) {
		this.isAnswer = isAnswer;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	
	

}
