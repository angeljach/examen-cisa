package com.jach.examencisa.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jach.examencisa.model.ExamStatistics;
import com.jach.examencisa.util.ExamCisaConstants;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "cisa";
	private static final String TAG = "DatabaseHelper";
    //private final Context fContext;
    
    private static final String SQL_CREATE_TABLE_EXAMSTAT = "CREATE TABLE " + ExamStatistics.TABLE_NAME + " (" +
    		ExamStatistics._ID + " INTEGER PRIMARY KEY" +
    		"," + ExamStatistics.COL_LAST_QUESTION_DATE + " TEXT" +
    		"," + ExamStatistics.COL_WAS_CORRECT + " INTEGER" +
    		");";
     
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        //fContext = context;
    } 
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_EXAMSTAT);
 
        // Add default records
        ContentValues values = new ContentValues();
        db.beginTransaction();
        for (int i=0 ; i<ExamCisaConstants.MAX_NUMBER_QUESTIONS ; i++) {
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
