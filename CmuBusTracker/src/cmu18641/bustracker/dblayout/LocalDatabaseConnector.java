package cmu18641.bustracker.dblayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import cmu18641.bustracker.entities.Bus;
import cmu18641.bustracker.entities.Stop;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

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
	public long insertBus(Bus bus, long[] stopIds) { 
		ContentValues newBus = new ContentValues();
		newBus.put("busname", bus.getName()); 
		newBus.put("busdir", bus.getDirection()); 
		newBus.put("timestamp", getDateTime()); 
		
		open(); 
		long busId = database.insert("buses", null, newBus); 
		close(); 
		
		for (long stopId : stopIds) {
            insertRoute(busId, stopId);
        }
		
		return busId;
	}
	
	// selects a bus from BUS table
	public Bus selectBus(Long busId) { 
		
		//sql command to select an item given an id
		String selectQuery = "SELECT  * FROM buses WHERE busid = " + busId;
	 
		open();
	    Cursor busCursor = database.rawQuery(selectQuery, null);
	    close(); 
	    
		if (busCursor != null)
	        busCursor.moveToFirst();
	    
		//extract the bus information from the database query
	    String busName = busCursor.getString(busCursor.getColumnIndex("busname")); 
	    String busDir = busCursor.getString(busCursor.getColumnIndex("busdir"));
	    Bus bus = new Bus(busName, busDir); 
	    
	    //close the cursor
	    busCursor.close();
	    
	    //return the bus
	    return bus;
		
	}
	
	// selects all buses from BUS table
	public ArrayList<Bus> selectAllBuses() { 
		ArrayList<Bus> allBuses = new ArrayList<Bus>();
		
		// sql commad to select all
	    String selectQuery = "SELECT  * FROM buses";
	    
	    open();
	    Cursor busCursor = database.rawQuery(selectQuery, null);
	    close();
	    
	    // looping through all rows and adding to list
	    if (busCursor.moveToFirst()) {
	        do {
	        	String busName = busCursor.getString(busCursor.getColumnIndex("busname")); 
	    	    String busDir = busCursor.getString(busCursor.getColumnIndex("busdir"));
	    	    Bus bus = new Bus(busName, busDir);
	            
	    	    // Adding bus to list
	            allBuses.add(bus);
	        } while (busCursor.moveToNext());
	    }
		
	    //close the cursor
	    busCursor.close();
	    
		//return all the buses
		return allBuses; 
	}
	
	// updates a bus in BUS table
	public int updateBus(Bus bus, Long busId) { 
		ContentValues updateBus = new ContentValues();
		updateBus.put("busname", bus.getName()); 
		updateBus.put("busdir", bus.getDirection());
		
		open(); 
		int updateId = database.update("buses", updateBus, "busid = ?",  new String[] { String.valueOf(busId) });
		close(); 
		
		return updateId; 
	}
	
	// deletes a bus in BUS table
	public int deleteBus(Long busId) { 
		open(); 
		int deleteId = database.delete("buses", "busid = ?",  new String[] { String.valueOf(busId) });
		close(); 
		
		return deleteId; 
	}
	
	// inserts a new stop in STOP table
		public long insertStop(Stop stop, long[] busIds) { 
			ContentValues newStop = new ContentValues();
			newStop.put("stopname", stop.getName()); 
			newStop.put("stopstreet1", stop.getStreet1()); 
			newStop.put("stopstreet2", stop.getStreet2());
			newStop.put("stopgpslat", stop.getLocation().getLatitude());
			newStop.put("stopgpslong", stop.getLocation().getLongitude());
			newStop.put("stopdistance", stop.getDistance()); 
			newStop.put("timestamp", getDateTime());
			
			open(); 
			long stopId = database.insert("stops", null, newStop); 
			close(); 
			
			for (long busId : busIds) {
	            insertRoute(stopId, busId);
	        }
			
			return stopId;
		}
		
		// selects a stop from STOP table
		public Stop selectStop(Long stopId) { 
			
			//sql command to select an item given an id
			String selectQuery = "SELECT  * FROM stops WHERE stopid = " + stopId;
		 
			open();
		    Cursor stopCursor = database.rawQuery(selectQuery, null);
		    close(); 
		    
			if (stopCursor != null)
				stopCursor.moveToFirst();
			
			//extract the stop information from the database query
		    String stopName = stopCursor.getString(stopCursor.getColumnIndex("stopname")); 
		    String stopStreet1 = stopCursor.getString(stopCursor.getColumnIndex("stopstreet1"));
		    String stopStreet2 = stopCursor.getString(stopCursor.getColumnIndex("stopstreet2"));
		    double stopGpsLat = stopCursor.getDouble(stopCursor.getColumnIndex("stopgpslat"));
		    double stopGpsLong = stopCursor.getDouble(stopCursor.getColumnIndex("stopgpslong"));
		    float stopDistance = stopCursor.getFloat(stopCursor.getColumnIndex("stopdistance"));
		    Location stopLocation = new Location(stopName);
		    stopLocation.setLatitude(stopGpsLat);
		    stopLocation.setLongitude(stopGpsLong);
		    Stop stop = new Stop(stopName, stopStreet1, stopStreet2, stopLocation, stopDistance); 
		    
		    //close the cursor
		    stopCursor.close();
		    
		    //return the bus
		    return stop;
			
		}
		
		// selects all stops from STOP table
		public ArrayList<Stop> selectAllStops() { 
			ArrayList<Stop> allStops = new ArrayList<Stop>();
			
			// sql commad to select all
		    String selectQuery = "SELECT  * FROM stops";
		    
		    open();
		    Cursor stopCursor = database.rawQuery(selectQuery, null);
		    close();
		    
		    // looping through all rows and adding to list
		    if (stopCursor.moveToFirst()) {
		        do {
		        	String stopName = stopCursor.getString(stopCursor.getColumnIndex("stopname")); 
				    String stopStreet1 = stopCursor.getString(stopCursor.getColumnIndex("stopstreet1"));
				    String stopStreet2 = stopCursor.getString(stopCursor.getColumnIndex("stopstreet2"));
				    double stopGpsLat = stopCursor.getDouble(stopCursor.getColumnIndex("stopgpslat"));
				    double stopGpsLong = stopCursor.getDouble(stopCursor.getColumnIndex("stopgpslong"));
				    float stopDistance = stopCursor.getFloat(stopCursor.getColumnIndex("stopdistance"));
				    Location stopLocation = new Location(stopName);
				    stopLocation.setLatitude(stopGpsLat);
				    stopLocation.setLongitude(stopGpsLong);
				    Stop stop = new Stop(stopName, stopStreet1, stopStreet2, stopLocation, stopDistance);
		            
		    	    // Adding stops to list
		            allStops.add(stop);
		        } while (stopCursor.moveToNext());
		    }
		    
		    //close the cursor
		    stopCursor.close();
		    
			//return all the stops
			return allStops; 
		}
		
		// updates a stop in STOP table
		public int updateStop(Stop stop, Long stopId) { 
			ContentValues updateStop = new ContentValues();
			updateStop.put("stopname", stop.getName()); 
			updateStop.put("stopstreet1", stop.getStreet1()); 
			updateStop.put("stopstreet2", stop.getStreet2());
			updateStop.put("stopgpslat", stop.getLocation().getLatitude());
			updateStop.put("stopgpslong", stop.getLocation().getLongitude());
			updateStop.put("stopdistance", stop.getDistance());
			
			open(); 
			int updateId = database.update("stops", updateStop, "stopid = ?",  new String[] { String.valueOf(stopId) });
			close(); 
			
			return updateId; 
		}
		
		// deletes a stop in STOP table
		public int deleteStop(Long stopId) { 
			open(); 
			int deleteId = database.delete("stops", "stopid = ?",  new String[] { String.valueOf(stopId) });
			close(); 
			
			return deleteId; 
		}
		
		// inserts a new route in ROUTE table
		public long insertRoute(Long busId, Long stopId) { 
			ContentValues newRoute = new ContentValues();
			newRoute.put("busid", busId); 
			newRoute.put("stopid", stopId); 
			newRoute.put("timestamp", getDateTime());
			
			open(); 
			long insertId = database.insert("routes", null, newRoute); 
			close(); 
			
			return insertId;
		}
		
		
		
		
		// updates a route in ROUTE table
		public int updateRoute(Long busId, Long stopId, Long routeId) { 
			ContentValues updateRoute = new ContentValues();
			updateRoute.put("busid", busId); 
			updateRoute.put("stopid", stopId);
			
			open(); 
			int updateId = database.update("routes", updateRoute, "routeid = ?",  new String[] { String.valueOf(routeId) });
			close(); 
			
			return updateId; 
		}
		
		// deletes a route in ROUTE table
		public int deleteRoute(Long routeId) { 
			open(); 
			int deleteId = database.delete("routes", "routeid = ?",  new String[] { String.valueOf(routeId) });
			close(); 
			
			return deleteId; 
		}

		// inserts a new schedule in SCHEDULE table
		public long insertSchedule(Long routeId, Long scheduleDay, Long scheduleTime) { 
			ContentValues newSchedule = new ContentValues();
			newSchedule.put("routeId", routeId); 
			newSchedule.put("scheduleday", scheduleDay); 
			newSchedule.put("scheduletime", scheduleTime); 
			newSchedule.put("timestamp", getDateTime());
			
			open(); 
			long insertId = database.insert("schedules", null, newSchedule); 
			close(); 
			
			return insertId;
		}
		
		
		// updates a schedule in SCHEDULE table
		public int updateschedule(Long routeId, Long scheduleDay, Long scheduleTime, Long scheduleId) { 
			ContentValues updateSchedule = new ContentValues();
			updateSchedule.put("routeid", routeId); 
			updateSchedule.put("scheduleday", scheduleDay);
			updateSchedule.put("scheduletime", scheduleTime);
			
			open(); 
			int updateId = database.update("schedules", updateSchedule, "scheduleid = ?",  new String[] { String.valueOf(scheduleId) });
			close(); 
			
			return updateId; 
		}
		
		// deletes a schedule in SCHEDULE table
		public int deleteSchedule(Long scheduleId) { 
			open(); 
			int deleteId = database.delete("schedules", "scheduleid = ?",  new String[] { String.valueOf(scheduleId) });
			close(); 
			
			return deleteId; 
		}
		
		
		 /**
	     * get datetime
	     * */
	    private String getDateTime() {
	        SimpleDateFormat dateFormat = new SimpleDateFormat(
	                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
	        Date date = new Date();
	        return dateFormat.format(date);
	    }
	
} // end LocalDatabaseConnector
	