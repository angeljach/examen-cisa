package com.jach.examencisa.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.jach.examencisa.R;
import com.jach.examencisa.model.Answer;
import com.jach.examencisa.model.Question;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "cisa";
	private static final String TAG = "DatabaseHelper";
    private final Context fContext;

    private static final int MAX_NUMBER_QUESTIONS = 1100;
    
    private static final String SQL_CREATE_TABLE_QUESTION = "CREATE TABLE " + Question.TABLE_NAME + " (" +
    		Question._ID + " INTEGER PRIMARY KEY" +
    		"," + Question.COL_QUESTION + " TEXT NOT NULL" +
    		"," + Question.COL_EXPLANATION + " TEXT NOT NULL" +
    		"," + Question.COL_CATEGORY + " TEXT NOT NULL" +
    		"," + Question.COL_LAST_QUESTION_DATE + " TEXT" +
    		"," + Question.COL_WAS_CORRECT + " INTEGER" +
    		");";
    private static final String SQL_CREATE_TABLE_ANSWER = "CREATE TABLE " + Answer.TABLE_NAME + " (" +
    		Answer._ID + " INTEGER PRIMARY KEY" +
    		"," + Answer.COL_ID_QUESTION + " INTEGER NOT NULL" +
    		"," + Answer.COL_ANSWER + " TEXT NOT NULL" +
    		"," + Answer.COL_IS_ANSWER + " INTEGER NOT NULL" +
    		"," + Answer.COL_SEQUENCE + " INTEGER NOT NULL" +
    		"," + "FOREIGN KEY(" + Answer.COL_ID_QUESTION + ") REFERENCES " + Question.TABLE_NAME + "(" + Question._ID + ")" +
    		");";
     
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        fContext = context;
    } 
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_QUESTION);
        db.execSQL(SQL_CREATE_TABLE_ANSWER);
 
        //Add default records
        ContentValues values;
        Resources res = fContext.getResources();
        InputStream in;
        BufferedReader reader;
        
        //---|| Questions
        values = new ContentValues();
        in = res.openRawResource(R.raw.question);
        reader = new BufferedReader(new InputStreamReader(in));
        try {
        	String readLine = null;
        	db.beginTransaction();
        	while ((readLine = reader.readLine()) != null) {
        		String[] lineValues = readLine.split("|");
        		
        		values.put(Question._ID, lineValues[0]);
        		values.put(Question.COL_QUESTION, lineValues[1]);
        		values.put(Question.COL_EXPLANATION, lineValues[2]);
        		values.put(Question.COL_CATEGORY, lineValues[3]);
        		
        		db.insert(Question.TABLE_NAME, null, values);
        	}
        	db.endTransaction();
        } catch(IOException ex) {
        	
        } finally {
        	try {
        		in.close();
        	} catch(IOException ex) {
        		
        	}
        	try {
        		reader.close();
        	} catch(IOException ex) {
        		
        	}
        	
        }
        
        
      //---|| Answers
        values = new ContentValues();
        in = res.openRawResource(R.raw.answer);
        reader = new BufferedReader(new InputStreamReader(in));
        try {
        	String readLine = null;
        	db.beginTransaction();
        	while ((readLine = reader.readLine()) != null) {
        		String[] lineValues = readLine.split("|");
        		
        		values.put(Answer.COL_ANSWER, lineValues[0]);
        		values.put(Answer.COL_SEQUENCE, lineValues[1]);
        		values.put(Answer.COL_IS_ANSWER, lineValues[2]);
        		values.put(Answer.COL_ID_QUESTION, lineValues[3]);
        		
        		db.insert(Answer.TABLE_NAME, null, values);
        	}
        	db.endTransaction();
        } catch(IOException ex) {
        	
        } finally {
        	try {
        		in.close();            	        		
        	} catch(IOException ex) {
        		
        	}
        	try {
        		reader.close();
        	} catch(IOException ex) {
        		
        	}
        }
        
        

    }
 
    /* Update database to latest version */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Crude update, make sure to implement a correct one when needed.
                     
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + Answer.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Question.TABLE_NAME);
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
    
}
