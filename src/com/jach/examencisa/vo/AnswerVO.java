package com.jach.examencisa.vo;

public class AnswerVO {

	private String answer;
    private int sequence;
    private boolean isCorrect;
	
    public AnswerVO(String answer, int sequence, boolean isCorrect) {
		super();
		this.answer = answer;
		this.sequence = sequence;
		this.isCorrect = isCorrect;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public boolean isCorrect() {
		return isCorrect;
	}

	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}
    
}
