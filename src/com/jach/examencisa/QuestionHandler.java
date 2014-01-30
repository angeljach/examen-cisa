package com.jach.examencisa;

import java.util.List;
import java.util.Random;

import android.content.Context;

import com.jach.examencisa.util.ExamCisaConstants;
import com.jach.examencisa.vo.AnswerVO;
import com.jach.examencisa.vo.QuestionVO;
import com.jach.examencisa.xml.XmlHelper;

public class QuestionHandler {

	private final XmlHelper xmlHelper;
	private final Context fContext;
	
	public QuestionHandler(Context context) {
        fContext = context;
        xmlHelper = new XmlHelper(fContext.getResources());
    } 

	public QuestionVO questionById(int idQuestion) {
		return xmlHelper.getQuestion(idQuestion);
	}
    
    public List<AnswerVO> answerFromQuestion(int idQuestion) {
    	return xmlHelper.getAnswers(idQuestion);
    }
    
}
