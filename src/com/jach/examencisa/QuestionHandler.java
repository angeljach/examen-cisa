package com.jach.examencisa;

import java.util.List;
import java.util.Random;

import android.content.Context;

import com.jach.examencisa.util.ExamCisaConstants;
import com.jach.examencisa.vo.AnswerVO;
import com.jach.examencisa.vo.QuestionVO;
import com.jach.examencisa.xml.XmlHelper;

public class QuestionHandler {

	private final Context fContext;
	
	public QuestionHandler(Context context) {
        fContext = context;
    } 
	
	public QuestionVO randomQuestion() {
    	XmlHelper xmlHelper = new XmlHelper(fContext.getResources());
    	Random r = new Random();
    	return xmlHelper.getQuestion(r.nextInt(ExamCisaConstants.MAX_NUMBER_QUESTIONS)+1);
    }
    
    public List<AnswerVO> answerFromQuestion(int idQuestion) {
    	XmlHelper xmlHelper = new XmlHelper(fContext.getResources());
    	return xmlHelper.getAnswers(idQuestion);
    }
    
}
