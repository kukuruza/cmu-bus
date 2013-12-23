package bustracker.android.dblayout;

import bustracker.common.dblayout.DbStructure;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/*
 * LocalDatabaseOpenHelper.java
 * 
 * Provides utilties to create database schemas
 */

public class LocalDatabaseOpenHelper extends SQLiteOpenHelper {
	
    // table create Statements
    private static final String CREATE_TABLE_BUS = "CREATE TABLE " +
            DbStructure.TABLE_BUS + "(" + 
    		DbStructure.BUS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
            DbStructure.BUS_NAME + " TEXT," + 
            DbStructure.BUS_DIR + " TEXT," + 
            DbStructure.TIME_STAMP + " DATETIME" + ")";
 
    private static final String CREATE_TABLE_STOP = "CREATE TABLE " + 
    		DbStructure.TABLE_STOP + "(" + 
    		DbStructure.STOP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
    		DbStructure.STOP_NAME + " TEXT," +
    		DbStructure.STOP_STREET1 + " TEXT," + 
    		DbStructure.STOP_STREET2 + " TEXT," + 
    		DbStructure.STOP_GPSLAT + " REAL," + 
    		DbStructure.STOP_GPSLONG + " REAL,"+ 
    		DbStructure.TIME_STAMP + " DATETIME" + ")";
 
    private static final String CREATE_TABLE_ROUTE = "CREATE TABLE " +
    		DbStructure.TABLE_ROUTE + "(" + 
    		DbStructure.ROUTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
    		DbStructure.BUS_ID + " INTEGER," + 
    		DbStructure.STOP_ID + " INTEGER," + 
    		DbStructure.TIME_STAMP + " DATETIME" + ")";
    
    private static final String CREATE_TABLE_SCHEDULE = "CREATE TABLE " +
    		DbStructure.TABLE_SCHEDULE + "(" + 
    		DbStructure.SCHEDULE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
    		DbStructure.ROUTE_ID + " INTEGER," + 
    		DbStructure.SCHEDULE_DAY + " INTEGER," +
    		DbStructure.SCHEDULE_TIME + " INTEGER," + 
    		DbStructure.TIME_STAMP + " DATETIME" + ")";
 
	public LocalDatabaseOpenHelper(Context context, String name, 
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	// creates the contacts table when the database is created
	@Override
	public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BUS);
        db.execSQL(CREATE_TABLE_STOP);
        db.execSQL(CREATE_TABLE_ROUTE);
		db.execSQL(CREATE_TABLE_SCHEDULE);
	}

	// drop old schema and re-populate tables
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { 
        db.execSQL("DROP TABLE IF EXISTS " + DbStructure.TABLE_BUS);
        db.execSQL("DROP TABLE IF EXISTS " + DbStructure.TABLE_STOP);
        db.execSQL("DROP TABLE IF EXISTS " + DbStructure.TABLE_ROUTE);
        db.execSQL("DROP TABLE IF EXISTS " + DbStructure.TABLE_SCHEDULE);
 
        // create new tables
        onCreate(db);	
	}
		
} // end class LocalDatabaseOpenHelper

