package cmu18641.bustracker.dblayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import cmu18641.bustracker.entities.Bus;
import cmu18641.bustracker.entities.Stop;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.text.format.Time;
import android.util.Log;

/*
 * LocalDatabaseConnector.java
 * 
 * Provides implementation to query local SQLite database
 */

public class LocalDatabaseConnector extends SQLiteAssetHelper{
	
    private static final String LOG = LocalDatabaseConnector.class.getName();
    
    private static final String DATABASE_NAME = "busTracker";
    private static final int DATABASE_VERSION = 1;
	private SQLiteDatabase database;

	// constructor
	public LocalDatabaseConnector(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// open the database connection
	private void open() throws SQLException {
		database = getWritableDatabase();
	}

	// close the database connection
	public void close() {
		//if(database != null && database.isOpen()) { 
		//	database.close();
		//}
	}
	
	// inserts a new bus in BUS table
	public long insertBus(Bus bus, long[] stopIds) { 
		ContentValues newBus = new ContentValues();
		newBus.put("name", bus.getName()); 
		newBus.put("direction", bus.getDirection()); 
		newBus.put("timestamp", getDateTime()); 
		
		open(); 
		long busId = database.insert("buses", null, newBus); 
		
		for (long stopId : stopIds) {
            insertRoute(busId, stopId);
        }
		
		return busId;
	}
	
	// selects a bus from BUS table
	public Bus selectBus(Long busId) { 
		
		//sql command to select an item given an id
		String selectQuery = "SELECT  * FROM buses WHERE bus_id = " + busId;
		
		Log.i(LOG, selectQuery);
	 
		open();
	    Cursor busCursor = database.rawQuery(selectQuery, null);
	    
		if (busCursor != null)
	        busCursor.moveToFirst();
	    
		//extract the bus information from the database query
	    String busName = busCursor.getString(busCursor.getColumnIndex("name")); 
	    String busDir = busCursor.getString(busCursor.getColumnIndex("direction"));
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
	    
	    Log.i(LOG, selectQuery);
	    
	    open();
	    Cursor busCursor = database.rawQuery(selectQuery, null);
	    
	    // looping through all rows and adding to list
	    if (busCursor.moveToFirst()) {
	        do {
	        	String busName = busCursor.getString(busCursor.getColumnIndex("name")); 
	    	    String busDir = busCursor.getString(busCursor.getColumnIndex("direction"));
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
	
	// selects all buses associated with a specific stop
	public ArrayList<Bus> selectAllBusesByStop(Stop stop) { 
		ArrayList<Bus> allBusesByStop = new ArrayList<Bus>();
		String stopName = stop.getName(); 
			
		String selectQuery = "SELECT  * FROM buses tb, stops ts, routes tr WHERE ts.name = '" 
				+ stopName + "' AND ts.stop_id = tr.stop_id AND tb.bus_id = tr.bus_id";
		 
		Log.i(LOG, selectQuery);
		 
		open();
		Cursor busCursor = database.rawQuery(selectQuery, null);
		    
		// looping through all rows and adding to list
		if (busCursor.moveToFirst()) {
		     do {
		        String busName = busCursor.getString(busCursor.getColumnIndex("name")); 
		    	String busDir = busCursor.getString(busCursor.getColumnIndex("direction"));
		    	Bus bus = new Bus(busName, busDir);
		            
		    	// Adding bus to list
		        allBusesByStop.add(bus);
		    } while (busCursor.moveToNext());
		}
			
		//close the cursor
		busCursor.close();
		    
		//return all the buses
		return allBusesByStop; 
	}
	
	// updates a bus in BUS table
	public int updateBus(Bus bus, Long busId) { 
		ContentValues updateBus = new ContentValues();
		updateBus.put("name", bus.getName()); 
		updateBus.put("direction", bus.getDirection());
		
		open(); 
		int updateId = database.update("buses", updateBus, "bus_id = ?",  new String[] { String.valueOf(busId) });
		
		return updateId; 
	}
	
	// deletes a bus in BUS table
	public int deleteBus(Long busId) { 
		open(); 
		int deleteId = database.delete("buses", "bus_id = ?",  new String[] { String.valueOf(busId) });
		
		return deleteId; 
	}
	
	// inserts a new stop in STOP table
	public long insertStop(Stop stop, long[] busIds) { 
		ContentValues newStop = new ContentValues();
		newStop.put("name", stop.getName()); 
		newStop.put("street1", stop.getStreet1()); 
		newStop.put("street2", stop.getStreet2());
		newStop.put("latitude", stop.getLocation().getLatitude());
		newStop.put("longitude", stop.getLocation().getLongitude());
		newStop.put("timestamp", getDateTime());
			
		open(); 
		long stopId = database.insert("stops", null, newStop); 
			
		for(long busId : busIds) {
	        insertRoute(stopId, busId);
	    }
			
		return stopId;
	}
		
	// selects a stop from STOP table
	public Stop selectStop(Long stopId) { 

		//sql command to select an item given an id
		String selectQuery = "SELECT  * FROM stops WHERE stop_id = " + stopId;

		Log.i(LOG, selectQuery);

		open();
		Cursor stopCursor = database.rawQuery(selectQuery, null);

		if (stopCursor != null)
			stopCursor.moveToFirst();

		//extract the stop information from the database query
		String stopName = stopCursor.getString(stopCursor.getColumnIndex("name")); 
		String stopStreet1 = stopCursor.getString(stopCursor.getColumnIndex("street1"));
		String stopStreet2 = stopCursor.getString(stopCursor.getColumnIndex("street2"));
		double stopGpsLat = stopCursor.getDouble(stopCursor.getColumnIndex("latitude"));
		double stopGpsLong = stopCursor.getDouble(stopCursor.getColumnIndex("longitude"));
		Location stopLocation = new Location(stopName);
		stopLocation.setLatitude(stopGpsLat);
		stopLocation.setLongitude(stopGpsLong);
		Stop stop = new Stop(stopName, stopStreet1, stopStreet2, stopLocation); 

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

		Log.i(LOG, selectQuery);

		open();
		Cursor stopCursor = database.rawQuery(selectQuery, null);
		close();

		// looping through all rows and adding to list
		if (stopCursor.moveToFirst()) {
			do {
				String stopName = stopCursor.getString(stopCursor.getColumnIndex("name")); 
				String stopStreet1 = stopCursor.getString(stopCursor.getColumnIndex("street1"));
				String stopStreet2 = stopCursor.getString(stopCursor.getColumnIndex("street2"));
				double stopGpsLat = stopCursor.getDouble(stopCursor.getColumnIndex("latitude"));
				double stopGpsLong = stopCursor.getDouble(stopCursor.getColumnIndex("longitude"));
				Location stopLocation = new Location(stopName);
				stopLocation.setLatitude(stopGpsLat);
				stopLocation.setLongitude(stopGpsLong);
				Stop stop = new Stop(stopName, stopStreet1, stopStreet2, stopLocation);

				// Adding stops to list
				allStops.add(stop);
			} while (stopCursor.moveToNext());
		}

		//close the cursor
		stopCursor.close();

		//return all the stops
		return allStops; 
	}

	// selects all buses associated with a specific stop
	public ArrayList<Stop> selectAllStopsByStreet(String street) { 
		ArrayList<Stop> allStopsByStreet = new ArrayList<Stop>();

		String selectQuery = "SELECT  * FROM stops ts WHERE ts.street1 = '" 
				+ street + "' OR ts.street2 = '" + street + "'";

		Log.i(LOG, selectQuery);

		open();
		Cursor stopCursor = database.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (stopCursor.moveToFirst()) {
			do {
				String stopName = stopCursor.getString(stopCursor.getColumnIndex("name")); 
				String stopStreet1 = stopCursor.getString(stopCursor.getColumnIndex("street1"));
				String stopStreet2 = stopCursor.getString(stopCursor.getColumnIndex("street2"));
				double stopGpsLat = stopCursor.getDouble(stopCursor.getColumnIndex("latitude"));
				double stopGpsLong = stopCursor.getDouble(stopCursor.getColumnIndex("longitude"));
				Location stopLocation = new Location(stopName);
				stopLocation.setLatitude(stopGpsLat);
				stopLocation.setLongitude(stopGpsLong);
				Stop stop = new Stop(stopName, stopStreet1, stopStreet2, stopLocation);

				// Adding stops to list
				allStopsByStreet.add(stop);
			} while (stopCursor.moveToNext());
		}

		//close the cursor
		stopCursor.close();

		//return all the stops
		return allStopsByStreet;  
	}

	// updates a stop in STOP table
	public int updateStop(Stop stop, Long stopId) { 
		ContentValues updateStop = new ContentValues();
		updateStop.put("name", stop.getName()); 
		updateStop.put("street1", stop.getStreet1()); 
		updateStop.put("street2", stop.getStreet2());
		updateStop.put("latitude", stop.getLocation().getLatitude());
		updateStop.put("longitude", stop.getLocation().getLongitude());

		open(); 
		int updateId = database.update("stops", updateStop, "stop_id = ?",  new String[] { String.valueOf(stopId) });

		return updateId; 
	}

	// deletes a stop in STOP table
	public int deleteStop(Long stopId) { 
		open(); 
		int deleteId = database.delete("stops", "stop_id = ?",  new String[] { String.valueOf(stopId) });

		return deleteId; 
	}

	// inserts a new route in ROUTE table
	public long insertRoute(Long busId, Long stopId) { 
		ContentValues newRoute = new ContentValues();
		newRoute.put("bus_id", busId); 
		newRoute.put("stop_id", stopId); 
		newRoute.put("timestamp", getDateTime());

		open(); 
		long insertId = database.insert("routes", null, newRoute); 

		return insertId;
	}

	// updates a route in ROUTE table
	public int updateRoute(Long busId, Long stopId, Long routeId) { 
		ContentValues updateRoute = new ContentValues();
		updateRoute.put("bus_id", busId); 
		updateRoute.put("stop_id", stopId);

		open(); 
		int updateId = database.update("routes", updateRoute, "route_id = ?",  new String[] { String.valueOf(routeId) });

		return updateId; 
	}

	// deletes a route in ROUTE table
	public int deleteRoute(Long routeId) { 
		open(); 
		int deleteId = database.delete("routes", "route_id = ?",  new String[] { String.valueOf(routeId) });

		return deleteId; 
	}

	// inserts a new schedule in SCHEDULE table
	public long insertSchedule(Long routeId, Long scheduleDay, Long scheduleTime) { 
		ContentValues newSchedule = new ContentValues();
		newSchedule.put("route_id", routeId); 
		newSchedule.put("weekday", scheduleDay); 
		newSchedule.put("time", scheduleTime); 
		newSchedule.put("timestamp", getDateTime());

		open(); 
		long insertId = database.insert("schedule", null, newSchedule); 

		return insertId;
	}

	//given a stop, bus, and the current day of the week, return all the associated times from schedule table
	public ArrayList<Time> selectScheduleTimes(Stop stop, Bus bus, int currentDay) {
		ArrayList<Time> times = new ArrayList<Time>();

		String stopName = stop.getName(); 
		String busName = bus.getName(); 

		// 1. get stopId from stop table
		// 2. get busid from bus table
		// 3. select a route id from route table where route_busid = busid and route_stopId = stopid
		// 4. select times from schedule table where schedule route_id = route_id, and day = current day
		String selectQuery = "SELECT  * FROM buses tb, stops ts, routes tr, schedule tsc WHERE tb.name = '" 
				+ busName + "' AND ts.name = '" + stopName + 
				"' AND ts.stop_id = tr.stop_id AND tb.bus_id = tr.bus_id" + 
				" AND tr.route_id = tsc.route_id AND tsc.weekday = '" +
				currentDay + "'";

		Log.i(LOG, selectQuery);

		open();
		Cursor timesCursor = database.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (timesCursor.moveToFirst()) {
			do {
				Time time = new Time();
				time.minute = timesCursor.getInt(timesCursor.getColumnIndex("time")); 

				// Adding times to list
				times.add(time);
			} while (timesCursor.moveToNext());
		}

		//close the cursor
		timesCursor.close();

		return times; 
	}


	// updates a schedule in SCHEDULE table
	public int updateschedule(Long routeId, Long scheduleDay, Long scheduleTime, Long scheduleId) { 
		ContentValues updateSchedule = new ContentValues();
		updateSchedule.put("route_id", routeId); 
		updateSchedule.put("weekday", scheduleDay);
		updateSchedule.put("time", scheduleTime);

		open(); 
		int updateId = database.update("schedule", updateSchedule, "schedule_id = ?",  new String[] { String.valueOf(scheduleId) });
		close(); 

		return updateId; 
	}

	// deletes a schedule in SCHEDULE table
	public int deleteSchedule(Long scheduleId) { 
		open(); 
		int deleteId = database.delete("schedule", "schedule_id = ?",  new String[] { String.valueOf(scheduleId) });
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
	