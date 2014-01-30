package com.jach.examencisa.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jach.examencisa.model.AppProperties;
import com.jach.examencisa.model.ExamStatistics;
import com.jach.examencisa.util.ExamCisaConstants;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 4;
	private static final String DATABASE_NAME = "cisa";
	private static final String TAG = "DatabaseHelper";
    //private final Context fContext;
    
    private static final String SQL_CREATE_TABLE_EXAMSTAT = String.format("CREATE TABLE %s (" +
    		"%s INTEGER PRIMARY KEY, %s TEXT, %s INTEGER);", 
    		ExamStatistics.TABLE_NAME, 
    		ExamStatistics._ID,
    		ExamStatistics.COL_LAST_QUESTION_DATE, 
    		ExamStatistics.COL_WAS_CORRECT);
    
    private static final String SQL_CREATE_TABLE_APPPROP = String.format("CREATE TABLE %s (" +
    		"%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT);", 
    		AppProperties.TABLE_NAME, 
    		AppProperties._ID,
    		AppProperties.COL_KEY, 
    		AppProperties.COL_VALUE);
     
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //fContext = context;
    } 
 
    @Override
    public void onCreate(SQLiteDatabase db) {
    	db.execSQL(SQL_CREATE_TABLE_APPPROP);
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
        
        
        //---|| Add property records
        for (AppProperties.DefaultValues dVal : AppProperties.DefaultValues.values()) {
        	insertValueOnAppPropertiesTable(db, dVal.getKey(), dVal.getDefaultValue());
        }
    }
 
    /* Update database to latest version */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Crude update, make sure to implement a correct one when needed.
                     
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + AppProperties.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ExamStatistics.TABLE_NAME);
        onCreate(db);
    }
    
    private void insertValueOnAppPropertiesTable(SQLiteDatabase db, String key, String value) {
    	Log.i(TAG, String.format("Insert default valuest into [%s] : [%s][%s]", 
    			AppProperties.TABLE_NAME, key, value));
    	ContentValues values = new ContentValues();
    	values.put(AppProperties.COL_KEY, key);
        values.put(AppProperties.COL_VALUE, value);
        db.insert(AppProperties.TABLE_NAME, null, values);
    }
    
    public String getPropertyLastQuestion() {
    	return this.getPropertyValue(AppProperties.DefaultValues.LAST_QUESTION.getKey());
    }

    public void setPropertyLastQuestion(String lastQuestion) {
    	this.setPropertyValue(
    			AppProperties.DefaultValues.LAST_QUESTION.getKey(), 
    			lastQuestion);
    }
    
    public String getPropertyRandomOrder() {
    	return this.getPropertyValue(AppProperties.DefaultValues.RANDOM_ORDER.getKey());
    }
    
    /**
     * 
     * @param randomOrderIntegerValue Should be '0' to FALSE, or '1' to TRUE.
     */
    public void setPropertyRandomOrder(String randomOrderIntegerValue) {
    	this.setPropertyValue(
    			AppProperties.DefaultValues.RANDOM_ORDER.getKey(), 
    			randomOrderIntegerValue);
    }
    
    private String getPropertyValue(String key) {
    	Log.d(TAG, "Getting values on DB from property: " + key);
    	SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(AppProperties.TABLE_NAME, 
				new String[] { AppProperties.COL_VALUE },
				AppProperties.COL_KEY.concat("=?"),
				new String[] { key }, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		} else {
			Log.e(TAG, "Property [" + key + "] not found on DB.");
			return null;
		}
		
		String value = cursor.getString(0);
		Log.i(TAG, "DB property value '" + key + "' = '" + value + "'");
		return value;
    }
    
    private void setPropertyValue(String key, String value) {
    	String sql = "UPDATE " + AppProperties.TABLE_NAME + 
    			" SET " + AppProperties.COL_VALUE + "='" + value + "' " +
				"WHERE " + AppProperties.COL_KEY + "='" + key + "'";
    	Log.i(TAG, sql);
    	
    	
    	SQLiteDatabase db = this.getReadableDatabase();
    	//ContentValues valores = new ContentValues();
    	//valores.put(AppProperties.COL_VALUE, value);
    	//db.update(AppProperties.TABLE_NAME, 
    	//		valores, 
    	//		AppProperties.COL_KEY + "=" + key, 
    	//		null);
    	db.execSQL(sql);
    }
    
    
    
    public ExamStatistics getExamStatisticsFromQuestion(int idQuestion) {
    	Log.d(TAG, "Getting question statistics (last_question_date and was_correct): " + idQuestion);
    	SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(ExamStatistics.TABLE_NAME, 
				new String[] { ExamStatistics.COL_LAST_QUESTION_DATE, ExamStatistics.COL_WAS_CORRECT },
				ExamStatistics._ID.concat("=?"),
				new String[] { Integer.toString(idQuestion) }, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		} else {
			Log.e(TAG, "Statistic not found on DB for question " + idQuestion);
			return null;
		}
		String lastQuestionDate = cursor.getString(0);
		int wasCorrect = cursor.getInt(1);
		Log.i(TAG, String.format("Statistic found (last_question_date='%s', wasCorrect=%d) for question %d", 
				lastQuestionDate, wasCorrect, idQuestion));
		return new ExamStatistics(idQuestion, lastQuestionDate, wasCorrect);

    }
    
    public void setExamStatistics(ExamStatistics exs) {
		String sql = String.format("UPDATE %s SET %s='%s', %s=%d WHERE %s=%d", 
				ExamStatistics.TABLE_NAME,
				ExamStatistics.COL_LAST_QUESTION_DATE, exs.getLastQuestionDate(),
				ExamStatistics.COL_WAS_CORRECT, exs.getWasCorrect(),
				ExamStatistics._ID, exs.getIdQuestion());
    	Log.i(TAG, sql);
    	
    	SQLiteDatabase db = this.getReadableDatabase();
    	db.execSQL(sql);
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
