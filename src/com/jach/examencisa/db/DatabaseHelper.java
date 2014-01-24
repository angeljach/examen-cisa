package com.jach.examencisa.db;

import java.io.IOException;
import java.util.Random;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.jach.examencisa.R;
import com.jach.examencisa.model.ExamStatistics;
import com.jach.examencisa.vo.QuestionVO;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "cisa";
	private static final String TAG = "DatabaseHelper";
    private final Context fContext;

    private static final int MAX_NUMBER_QUESTIONS = 1100;
    
    private static final String SQL_CREATE_TABLE_EXAMSTAT = "CREATE TABLE " + ExamStatistics.TABLE_NAME + " (" +
    		ExamStatistics._ID + " INTEGER PRIMARY KEY" +
    		"," + ExamStatistics.COL_LAST_QUESTION_DATE + " TEXT" +
    		"," + ExamStatistics.COL_WAS_CORRECT + " INTEGER" +
    		");";
     
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        fContext = context;
    } 
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_EXAMSTAT);
 
        // Add default records
        ContentValues values = new ContentValues();
        db.beginTransaction();
        for (int i=0 ; i<MAX_NUMBER_QUESTIONS ; i++) {
        	values.put(ExamStatistics._ID, i+1);
        	values.put(ExamStatistics.COL_LAST_QUESTION_DATE, "");
        	values.put(ExamStatistics.COL_WAS_CORRECT, 0);
        	
        	db.insert(ExamStatistics.TABLE_NAME, null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }
 
    /* Update database to latest version */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Crude update, make sure to implement a correct one when needed.
                     
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + ExamStatistics.TABLE_NAME);
        onCreate(db);
    }
    /*
    public String getAnimal(String color) {
    	SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query("animals", new String[] { "title",
				"color" }, "color=?",
				new String[] { color }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		
		String title = cursor.getString(0);
		//String color = cursor.getString(1);
		return title;
    }*/
	
    
    public QuestionVO randomQuestion() {
    	Random r = new Random();
    	int randomNumer = r.nextInt(MAX_NUMBER_QUESTIONS)+1;
    	Log.i(TAG, "Pregunta aleatoria a buscar: " + randomNumer);
    	
    	////Get XML resource file
        Resources res = fContext.getResources();
         
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
                	if (xmlId.equals(Integer.toString(randomNumer))) {
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
                		//TODO La información de lastQuestionDate y WasCorrect las debo obtener de la BD
                		QuestionVO question = new QuestionVO(randomNumer, q, exp, cat, null, false);
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
    
    /*
    public Question randomQuestion() {
    	// http://developer.android.com/training/basics/data-storage/databases.html#ReadDbRow

    	Random r = new Random();
    	SQLiteDatabase db = this.getReadableDatabase();
    	
    	String[] projection = {
    			Question._ID,
    			Question.COL_QUESTION,
    			Question.COL_EXPLANATION,
    			Question.COL_CATEGORY,
    			Question.COL_LAST_QUESTION_DATE,
    			Question.COL_WAS_CORRECT
    	};
    	//// Define 'where' part of query.
    	//String selection = FeedEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
    	//// Specify arguments in placeholder order.
    	//String[] selectionArgs = { String.valueOf(rowId) };
    	//String sortOrder = Question.COL_LAST_QUESTION_DATE.concat(" DESC");

    	String selection = Question._ID.concat("=?");
    	String[] selectionArgs = { String.valueOf(r.nextInt(MAX_NUMBER_QUESTIONS)+1)};
    	
    	Cursor c = db.query(
				Question.TABLE_NAME,  			// The table to query
    		    projection,                     // The columns to return
    		    selection,                      // The columns for the WHERE clause
    		    selectionArgs,                  // The values for the WHERE clause
    		    null,                           // don't group the rows
    		    null,                           // don't filter by row groups
    		    null                            // The sort order
    		    );

    	c.moveToFirst();
    	
    	Date date;
    	try {
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(c.getString(c.getColumnIndex(Question.COL_LAST_QUESTION_DATE)));
		} catch (ParseException e) {
			date = null;
		}
    	
    	Question q = new Question(
    			c.getInt(c.getColumnIndex(Question._ID)),
    			c.getString(c.getColumnIndex(Question.COL_QUESTION)),
    			c.getString(c.getColumnIndex(Question.COL_EXPLANATION)),
    			c.getString(c.getColumnIndex(Question.COL_CATEGORY)),
    			date,
    			(c.getInt(c.getColumnIndex(Question.COL_WAS_CORRECT)) == 0 ? false : true)
    			);
    	return q;
    }
    */
    
}
