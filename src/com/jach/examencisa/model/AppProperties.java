package com.jach.examencisa.model;

import android.provider.BaseColumns;

public class AppProperties implements BaseColumns {
	
	public enum DefaultValues{
		LAST_QUESTION("last_question", "0"), 
		RANDOM_ORDER("random_order", "0");
		
		private final String key;
		private final String defaultValue;
		private DefaultValues(String key, String defaultValue) {
			this.key = key;
			this.defaultValue = defaultValue;
		}
		public String getKey() {
			return key;
		}
		public String getDefaultValue() {
			return defaultValue;
		}
	};
	
	public static final String TABLE_NAME = "APP_PROPERTIES";

	public static final String COL_KEY = "prop_key";
	public static final String COL_VALUE = "prop_value";
	
	private String key;
    private String value;
    
	public AppProperties(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
        
}
