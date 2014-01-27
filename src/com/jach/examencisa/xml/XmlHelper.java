package com.jach.examencisa.xml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.jach.examencisa.R;
import com.jach.examencisa.vo.AnswerVO;
import com.jach.examencisa.vo.QuestionVO;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;


public class XmlHelper {
	
	private Resources res;
	
	public XmlHelper(Resources res) {
		this.res = res;
	}
	
	public static final String TAG = XmlHelper.class.getName();
	
	public QuestionVO getQuestion(int idQuestion) {
    	Log.i(TAG, "Buscando pregunta: " + idQuestion);
    	 
        //Open XML file
    	XmlResourceParser xpp = res.getXml(R.xml.question);
    	try {
            //Check for end of document
            int eventType = xpp.getEventType();
            
            //db.beginTransaction();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                //Search for record tags
                if ((eventType == XmlPullParser.START_TAG) && (xpp.getName().equals("question"))){
                    //QUESTION tag found, now get values and insert record
                    
                	
                	String xmlId = xpp.getAttributeValue(null, "id");
                	if (xmlId.equals(Integer.toString(idQuestion))) {
                		eventType = xpp.next();
                		
                		String q = "";
                		String exp = "";
                		String cat = "";
                		
                		while ( ! ((eventType == XmlPullParser.END_TAG) && xpp.getName().equals("question")) ) {
                			if (eventType == XmlPullParser.START_TAG) {
                				if (xpp.getName().equals("q")) {
                					eventType = xpp.next();
                					q = xpp.getText();
                				} else if (xpp.getName().equals("exp")) {
                					eventType = xpp.next();
                					exp = xpp.getText();
                				} else if (xpp.getName().equals("cat")) {
                					eventType = xpp.next();
                					cat = xpp.getText();
                				}
                			}
                			eventType = xpp.next();
                		}
                		//TODO La informaci�n de lastQuestionDate y WasCorrect las debo obtener de la BD
                		QuestionVO question = new QuestionVO(idQuestion, q, exp, cat, null, false);
                		return question;
                	}
                	
                }
                eventType = xpp.next();
            }
            //db.endTransaction();
        }
        //Catch errors
        catch (XmlPullParserException e) {       
            Log.e(TAG, e.getMessage(), e);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
             
        } finally {           
            //Close the xml file
            xpp.close();
        }
    	
    	return null;
    }
	
	public List<AnswerVO> getAnswers(int idQuestion) {
		List<AnswerVO> lstAnswer = new ArrayList<AnswerVO>();
		
		//Open XML file
    	XmlResourceParser xpp = res.getXml(R.xml.answer);
    	try {
            //Check for end of document
            int eventType = xpp.getEventType();
            
            //db.beginTransaction();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                //Search for record tags
                if ((eventType == XmlPullParser.START_TAG) && (xpp.getName().equals("answer"))) {
                    //ANSWER tag found, now get values
                    
                	String xmlId = xpp.getAttributeValue(null, "id");
                	if (xmlId.equals(Integer.toString(idQuestion))) {
                		eventType = xpp.next();
                		
                		while ( ! ((eventType == XmlPullParser.END_TAG) && xpp.getName().equals("answer")) ) {
                			if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("opcion")) {
            					int sequence = Integer.parseInt(xpp.getAttributeValue(null, "seq"));
            					boolean isCorrect = (Integer.parseInt(xpp.getAttributeValue(null, "correct")) == 1);
            					
            					eventType = xpp.next();
            					
            					String answer = xpp.getText();
            					lstAnswer.add(new AnswerVO(answer, sequence, isCorrect));
                			}
                			eventType = xpp.next();
                		}
                		return lstAnswer;
                	}
                }
                eventType = xpp.next();
            }
        }
        //Catch errors
        catch (XmlPullParserException e) {       
            Log.e(TAG, e.getMessage(), e);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
             
        } finally {           
            //Close the xml file
            xpp.close();
        }
    	
    	return lstAnswer;
	}

}
