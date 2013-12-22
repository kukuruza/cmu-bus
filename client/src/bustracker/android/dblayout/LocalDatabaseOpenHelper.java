package bustracker.android.dblayout;

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
	
	// table names
    private static final String TABLE_BUS = "buses";
    private static final String TABLE_STOP = "stops";
    private static final String TABLE_ROUTE = "routes";
    private static final String TABLE_SCHEDULE = "schedules";
    
    // common column names
    private static final String TIME_STAMP = "timestamp";
    
    // BUS table column names
    private static final String BUS_ID = "busid";
    private static final String BUS_NAME = "busname";
    private static final String BUS_DIR = "busdir";
    
    // STOP table column names
    private static final String STOP_ID = "stopid";
    private static final String STOP_NAME = "stopname";
    private static final String STOP_STREET1 = "stopstreet1";
    private static final String STOP_STREET2 = "stopstreet2";
    private static final String STOP_GPSLAT = "stopgpslat";
    private static final String STOP_GPSLONG = "stopgpslong";
    private static final String STOP_DISTANCE = "stopdistance"; 
    
    // ROUTE table column names
    private static final String ROUTE_ID = "routeid";
    
    // SCHEDULE table column names
    private static final String SCHEDULE_ID = "scheduleid";
    private static final String SCHEDULE_DAY = "scheduleday";
    private static final String SCHEDULE_TIME = "scheduletime";
    
    // table create Statements
    private static final String CREATE_TABLE_BUS = "CREATE TABLE "
            + TABLE_BUS + "(" + BUS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + BUS_NAME
            + " TEXT," + BUS_DIR + " TEXT," + TIME_STAMP + " DATETIME" + ")";
 
    private static final String CREATE_TABLE_STOP = "CREATE TABLE " + TABLE_STOP
            + "(" + STOP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + STOP_NAME + " TEXT,"
            + STOP_STREET1 + " TEXT," + STOP_STREET2 + " TEXT," + STOP_GPSLAT 
            + " REAL," + STOP_GPSLONG + " REAL,"+ STOP_DISTANCE + " REAL,"+ TIME_STAMP + " DATETIME" + ")";
 
    private static final String CREATE_TABLE_ROUTE = "CREATE TABLE "
            + TABLE_ROUTE + "(" + ROUTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + BUS_ID + " INTEGER," + STOP_ID + " INTEGER," + TIME_STAMP
            + " DATETIME" + ")";
    
    private static final String CREATE_TABLE_SCHEDULE = "CREATE TABLE "
            + TABLE_SCHEDULE + "(" + SCHEDULE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ROUTE_ID + " INTEGER," + SCHEDULE_DAY + " INTEGER," 
            + SCHEDULE_TIME + " INTEGER," + TIME_STAMP + " DATETIME" + ")";
 
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE);
 
        // create new tables
        onCreate(db);	
	}
		
} // end class LocalDatabaseOpenHelper

