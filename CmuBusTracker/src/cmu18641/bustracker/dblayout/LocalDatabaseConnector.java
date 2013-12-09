package cmu18641.bustracker.dblayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import cmu18641.bustracker.common.dblayout.DbConnectorInterface;
import cmu18641.bustracker.common.dblayout.DbStructure;
import cmu18641.bustracker.common.dblayout.DbTime;
import cmu18641.bustracker.entities.Bus;
import cmu18641.bustracker.entities.Schedule;
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

public class LocalDatabaseConnector extends SQLiteAssetHelper implements DbConnectorInterface {
	
    private static final String LOG = LocalDatabaseConnector.class.getName();
    
    public static final String DATABASE_NAME = "busTracker";
    private static int DATABASE_VERSION = 5;
	private SQLiteDatabase database;
	
	

	// constructor
	public LocalDatabaseConnector(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		setForcedUpgradeVersion(DATABASE_VERSION);
	}

	// open the database connection
	private void open() throws SQLException {
		database = getWritableDatabase();
	}

	// close the database connection
	public void close() {
		if(database != null && database.isOpen()) { 
			database.close();
		}
	}
	
	// change db version on upgrade
	public static void incrementDbVersion() { 
		DATABASE_VERSION++; 
	}
	
	// inserts a new bus in BUS table
	public long insertBus(Bus bus, long[] stopIds) { 
		ContentValues newBus = new ContentValues();
		newBus.put(DbStructure.BUS_NAME, bus.getName()); 
		newBus.put(DbStructure.BUS_DIR, bus.getDirection()); 
		newBus.put(DbStructure.TIME_STAMP, getDateTime()); 
		
		open(); 
		long busId = database.insert(DbStructure.TABLE_BUS, null, newBus); 
		
		for (long stopId : stopIds) {
            insertRoute(busId, stopId);
        }
		
		close(); 
		return busId;
	}
	
	// selects a bus from BUS table
	public Bus selectBus(Long busId) { 
		
		String selectQuery = "SELECT  * FROM " + DbStructure.TABLE_BUS + " WHERE " + DbStructure.BUS_ID + " = " + busId;
		
		Log.i(LOG, selectQuery);
	 
		open();
	    Cursor busCursor = database.rawQuery(selectQuery, null);
	    
		if (busCursor != null)
	        busCursor.moveToFirst();
	    
		//extract the bus information from the database query
	    String busName = busCursor.getString(busCursor.getColumnIndex(DbStructure.BUS_NAME)); 
	    String busDir = busCursor.getString(busCursor.getColumnIndex(DbStructure.BUS_DIR));
	    Bus bus = new Bus(busName, busDir); 
	    
	    //close the cursor
	    busCursor.close();
	    close();
	    
	    //return the bus
	    return bus;
		
	}
	
	// selects all buses from BUS table
	public ArrayList<Bus> selectAllBuses() { 
		ArrayList<Bus> allBuses = new ArrayList<Bus>();
	    String selectQuery = "SELECT  * FROM " + DbStructure.TABLE_BUS;
	    
	    Log.i(LOG, selectQuery);
	    
	    open();
	    Cursor busCursor = database.rawQuery(selectQuery, null);
	    
	    // looping through all rows and adding to list
	    if (busCursor.moveToFirst()) {
	        do {
	        	String busName = busCursor.getString(busCursor.getColumnIndex(DbStructure.BUS_NAME)); 
	    	    String busDir = busCursor.getString(busCursor.getColumnIndex(DbStructure.BUS_DIR));
	    	    Bus bus = new Bus(busName, busDir);
	            
	    	    // Adding bus to list
	            allBuses.add(bus);
	        } while (busCursor.moveToNext());
	    }
		
	    //close the cursor
	    busCursor.close();
	    close();
	    
		//return all the buses
		return allBuses; 
	}
	
	// selects all buses associated with a specific stop
	@Override
	public ArrayList<Bus> getBusesForStop (Stop stop) { 
		ArrayList<Bus> allBusesByStop = new ArrayList<Bus>();
			
		String selectQuery = DbStructure.busesRequestString(stop.getName());
		 
		Log.i(LOG, selectQuery);
		 
		open();
		Cursor busCursor = database.rawQuery(selectQuery, null);
		    
		// looping through all rows and adding to list
		if (busCursor.moveToFirst()) {
		     do {
		        String busName = busCursor.getString(busCursor.getColumnIndex(DbStructure.BUS_NAME)); 
		    	String busDir = busCursor.getString(busCursor.getColumnIndex(DbStructure.BUS_DIR));
		    	Bus bus = new Bus(busName, busDir);
		            
		    	// Adding bus to list
		        allBusesByStop.add(bus);
		    } while (busCursor.moveToNext());
		}
			
		//close the cursor
		busCursor.close();
		close();
		
		//return all the buses
		return allBusesByStop; 
	}
	
	// updates a bus in BUS table
	public int updateBus(Bus bus, Long busId) { 
		ContentValues updateBus = new ContentValues();
		updateBus.put(DbStructure.BUS_NAME, bus.getName()); 
		updateBus.put(DbStructure.BUS_DIR, bus.getDirection());
		
		open(); 
		int updateId = database.update(DbStructure.TABLE_BUS, updateBus, DbStructure.BUS_ID + " = ?",  new String[] 
				{ String.valueOf(busId) });
		close();
		return updateId; 
	}
	
	// deletes a bus in BUS table
	public int deleteBus(Long busId) { 
		open(); 
		int deleteId = database.delete(DbStructure.TABLE_BUS, DbStructure.BUS_ID + " = ?",  new String[] 
				{ String.valueOf(busId) });
		close();
		return deleteId; 
	}
	
	// inserts a new stop in STOP table
	public long insertStop(Stop stop, long[] busIds) { 
		ContentValues newStop = new ContentValues();
		newStop.put(DbStructure.STOP_NAME, stop.getName()); 
		newStop.put(DbStructure.STOP_STREET1, stop.getStreet1()); 
		newStop.put(DbStructure.STOP_STREET2, stop.getStreet2());
		newStop.put(DbStructure.STOP_GPSLAT, stop.getLocation().getLatitude());
		newStop.put(DbStructure.STOP_GPSLONG, stop.getLocation().getLongitude());
		newStop.put(DbStructure.TIME_STAMP, getDateTime());
			
		open(); 
		long stopId = database.insert(DbStructure.TABLE_STOP, null, newStop); 
			
		for(long busId : busIds) {
	        insertRoute(stopId, busId);
	    }
		close();	
		return stopId;
	}
		
	// selects a stop from STOP table
	public Stop selectStop(Long stopId) { 

		//sql command to select an item given an id
		String selectQuery = "SELECT  * FROM " + DbStructure.TABLE_STOP + " WHERE " + DbStructure.STOP_ID + " = " + stopId;

		Log.i(LOG, selectQuery);

		open();
		Cursor stopCursor = database.rawQuery(selectQuery, null);

		if (stopCursor != null)
			stopCursor.moveToFirst();

		//extract the stop information from the database query
		String stopName = stopCursor.getString(stopCursor.getColumnIndex(DbStructure.STOP_NAME)); 
		String stopStreet1 = stopCursor.getString(stopCursor.getColumnIndex(DbStructure.STOP_STREET1));
		String stopStreet2 = stopCursor.getString(stopCursor.getColumnIndex(DbStructure.STOP_STREET2));
		double stopGpsLat = stopCursor.getDouble(stopCursor.getColumnIndex(DbStructure.STOP_GPSLAT));
		double stopGpsLong = stopCursor.getDouble(stopCursor.getColumnIndex(DbStructure.STOP_GPSLONG));
		Location stopLocation = new Location(stopName);
		stopLocation.setLatitude(stopGpsLat);
		stopLocation.setLongitude(stopGpsLong);
		Stop stop = new Stop(stopName, stopStreet1, stopStreet2, stopLocation); 

		//close the cursor
		stopCursor.close();
		close();
		
		//return the bus
		return stop;

	}

	// selects all stops from STOP table
	@Override
	public ArrayList<Stop> getAllStops() { 
		ArrayList<Stop> allStops = new ArrayList<Stop>();

		// sql commad to select all
		String selectQuery = DbStructure.allStopsRequestString();

		Log.i(LOG, selectQuery);

		open();
		Cursor stopCursor = database.rawQuery(selectQuery, null);
		
		// looping through all rows and adding to list
		if (stopCursor.moveToFirst()) {
			do {
				String stopName = stopCursor.getString(stopCursor.getColumnIndex(DbStructure.STOP_NAME)); 
				String stopStreet1 = stopCursor.getString(stopCursor.getColumnIndex(DbStructure.STOP_STREET1));
				String stopStreet2 = stopCursor.getString(stopCursor.getColumnIndex(DbStructure.STOP_STREET2));
				double stopGpsLat = stopCursor.getDouble(stopCursor.getColumnIndex(DbStructure.STOP_GPSLAT));
				double stopGpsLong = stopCursor.getDouble(stopCursor.getColumnIndex(DbStructure.STOP_GPSLONG));
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
		close();
		
		//return all the stops
		return allStops; 
	}

	// selects all buses associated with a specific stop
	@Override
	public ArrayList<Stop> getStopsByStreet (String street) { 
		ArrayList<Stop> allStopsByStreet = new ArrayList<Stop>();

		String selectQuery = DbStructure.stopsByStreetRequestString(street);

		Log.i(LOG, selectQuery);

		open();
		Cursor stopCursor = database.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (stopCursor.moveToFirst()) {
			do {
				String stopName = stopCursor.getString(stopCursor.getColumnIndex(DbStructure.STOP_NAME)); 
				String stopStreet1 = stopCursor.getString(stopCursor.getColumnIndex(DbStructure.STOP_STREET1));
				String stopStreet2 = stopCursor.getString(stopCursor.getColumnIndex(DbStructure.STOP_STREET2));
				double stopGpsLat = stopCursor.getDouble(stopCursor.getColumnIndex(DbStructure.STOP_GPSLAT));
				double stopGpsLong = stopCursor.getDouble(stopCursor.getColumnIndex(DbStructure.STOP_GPSLONG));
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
		close();
		
		//return all the stops
		return allStopsByStreet;  
	}

	// updates a stop in STOP table
	public int updateStop(Stop stop, Long stopId) { 
		ContentValues updateStop = new ContentValues();
		updateStop.put(DbStructure.STOP_NAME, stop.getName()); 
		updateStop.put(DbStructure.STOP_STREET1, stop.getStreet1()); 
		updateStop.put(DbStructure.STOP_STREET2, stop.getStreet2());
		updateStop.put(DbStructure.STOP_GPSLAT, stop.getLocation().getLatitude());
		updateStop.put(DbStructure.STOP_GPSLONG, stop.getLocation().getLongitude());

		open(); 
		int updateId = database.update(DbStructure.TABLE_STOP, updateStop, DbStructure.STOP_ID + " = ?",  new String[] 
				{ String.valueOf(stopId) });
		close();
		return updateId; 
	}

	// deletes a stop in STOP table
	public int deleteStop(Long stopId) { 
		open(); 
		int deleteId = database.delete(DbStructure.TABLE_STOP, DbStructure.STOP_ID + " = ?",  new String[] 
				{ String.valueOf(stopId) });
		close();
		return deleteId; 
	}

	// inserts a new route in ROUTE table
	public long insertRoute(Long busId, Long stopId) { 
		ContentValues newRoute = new ContentValues();
		newRoute.put(DbStructure.BUS_ID, busId); 
		newRoute.put(DbStructure.STOP_ID, stopId); 
		newRoute.put(DbStructure.TIME_STAMP, getDateTime());

		open(); 
		long insertId = database.insert(DbStructure.TABLE_ROUTE, null, newRoute); 
		close();
		return insertId;
	}

	// updates a route in ROUTE table
	public int updateRoute(Long busId, Long stopId, Long routeId) { 
		ContentValues updateRoute = new ContentValues();
		updateRoute.put(DbStructure.BUS_ID, busId); 
		updateRoute.put(DbStructure.STOP_ID, stopId);

		open(); 
		int updateId = database.update(DbStructure.TABLE_ROUTE, updateRoute, DbStructure.ROUTE_ID + " = ?",  new String[] 
				{ String.valueOf(routeId) });
		close();
		return updateId; 
	}

	// deletes a route in ROUTE table
	public int deleteRoute(Long routeId) { 
		open(); 
		int deleteId = database.delete(DbStructure.TABLE_ROUTE, DbStructure.ROUTE_ID + " = ?",  new String[] 
				{ String.valueOf(routeId) });
		close();
		return deleteId; 
	}

	// inserts a new schedule in SCHEDULE table
	public long insertSchedule(Long routeId, Long scheduleDay, Long scheduleTime) { 
		ContentValues newSchedule = new ContentValues();
		newSchedule.put (DbStructure.ROUTE_ID, routeId); 
		newSchedule.put (DbStructure.SCHEDULE_DAY, scheduleDay); 
		newSchedule.put (DbStructure.SCHEDULE_TIME, scheduleTime); 
		newSchedule.put (DbStructure.TIME_STAMP, getDateTime());

		open(); 
		long insertId = database.insert (DbStructure.TABLE_SCHEDULE, null, newSchedule); 
		close();
		return insertId;
	}

	//given a stop, bus, and the current day of the week, return all the associated times from schedule table
	public ArrayList<Time> selectScheduleTimes(Stop stop, Bus bus) {
		ArrayList<Time> times = new ArrayList<Time>();

		String stopName = stop.getName(); 
		String busName = bus.getName(); 
		String busDir = bus.getDirection();

		int currentDay = DbTime.getWeekDay();
		
		// 1. get stopId from stop table
		// 2. get busid from bus table
		// 3. select a route id from route table where route_busid = busid and route_stopId = stopid
		// 4. select times from schedule table where schedule route_id = route_id, and day = current day
		String selectQuery = DbStructure.scheduleRequestString(stopName, busName, busDir, currentDay);

		Log.i(LOG, selectQuery);

		open();
		Cursor timesCursor = database.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (timesCursor.moveToFirst()) {
			do {
				// convert time db format to time object
				Time time = new Time();
				int numMinutesSinceMidnight = timesCursor.getInt(timesCursor.getColumnIndex(DbStructure.SCHEDULE_TIME));
				
				int minutes = numMinutesSinceMidnight % 60; 
				int hours = numMinutesSinceMidnight / 60; 
				time.set(0, minutes, hours, 1, 1, Calendar.getInstance().get(Calendar.YEAR));

				// Adding times to list
				times.add(time);
			} while (timesCursor.moveToNext());
		}

		//close the cursor
		timesCursor.close();
		close();
		return times; 
	}


	// updates a schedule in SCHEDULE table
	public int updateschedule(Long routeId, Long scheduleDay, Long scheduleTime, Long scheduleId) { 
		ContentValues updateSchedule = new ContentValues();
		updateSchedule.put(DbStructure.ROUTE_ID, routeId); 
		updateSchedule.put(DbStructure.SCHEDULE_DAY, scheduleDay);
		updateSchedule.put(DbStructure.SCHEDULE_TIME, scheduleTime);

		open(); 
		int updateId = database.update(DbStructure.TABLE_SCHEDULE, updateSchedule, DbStructure.SCHEDULE_ID + " = ?",  new String[] { String.valueOf(scheduleId) });
		close(); 

		return updateId; 
	}

	// deletes a schedule in SCHEDULE table
	public int deleteSchedule(Long scheduleId) { 
		open(); 
		int deleteId = database.delete(DbStructure.TABLE_SCHEDULE, DbStructure.SCHEDULE_ID + " = ?",  new String[] 
				{ String.valueOf(scheduleId) });
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

	
	
	
	
	
	@Override
	public Schedule getSchedule(String stopName, ArrayList<String> busesNames,
			ArrayList<String> busesDirs) {
		// TODO Auto-generated method stub
		return null;
	}


} // end LocalDatabaseConnector
	