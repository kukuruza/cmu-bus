package cmu18641.bustracker.dblayout;

import java.util.ArrayList;
import cmu18641.bustracker.entities.Bus;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/*
 * LocalDatabaseConnector.java
 * 
 * Provides implementation to query local SQLite database
 */

public class LocalDatabaseConnector {
 
    private static final String DATABASE_NAME = "busTracker";
    private static final int DATABASE_VERSION = 1;
	private SQLiteDatabase database;
	private LocalDatabaseOpenHelper databaseOpenHelper;

	// constructor
	public LocalDatabaseConnector(Context context) {
		databaseOpenHelper = 
			new LocalDatabaseOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// open the database connection
	private void open() throws SQLException {
		database = databaseOpenHelper.getWritableDatabase();
	}

	// close the database connection
	private void close() {
		if(database != null && database.isOpen()) { 
			database.close();
		}
	}
	
	// inserts a new bus in BUS table
	public long insertBus(Bus bus) { 
		open(); 
		// db operation
		close(); 
		
		return 0; 
	}
	
	// selects a bus from BUS table
	public Bus selectBus(Long busId) { 
		Bus bus = new Bus(""); 
		open(); 
		// db operation
		close(); 
		
		return bus; 
	}
	
	// selects all buses from BUS table
	public ArrayList<Bus> selectAllBuses() { 
		ArrayList<Bus> allBuses = new ArrayList<Bus>();
		open(); 
		// db operation
		close(); 
		
		return allBuses; 
	}
	
	// updates a bus in BUS table
	public int updateBus(Bus bus, Long busId) { 
		open(); 
		// db operation
		close(); 
		
		return 0; 
	}
	
	// deletes a bus in BUS table
	public int deleteBus(Long busId) { 
		open(); 
		// db operation
		close(); 
		
		return 0; 
	}
	
	/*
	 * tables for STOPS, ROUTES and SCHEDULES have 
	 * same set of methods
	 */
	
} // end LocalDatabaseConnector
	