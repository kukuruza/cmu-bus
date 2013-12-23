package bustracker.android.dblayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import bustracker.android.entities.Bus;
import bustracker.android.entities.Stop;
import bustracker.common.dblayout.DbConnectorInterface;
import bustracker.common.dblayout.DbStructure;
import bustracker.common.dblayout.DbTime;
import bustracker.common.entities.BaseBus;
import bustracker.common.entities.BaseSchedule;
import bustracker.common.entities.BaseScheduleItem;
import bustracker.common.entities.BaseStop;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/*
 * LocalDatabaseConnector.java
 * 
 * Provides implementation to query local SQLite database
 */

public class LocalDatabaseConnector extends SQLiteAssetHelper implements DbConnectorInterface {
	
    private static final String TAG = LocalDatabaseConnector.class.getName();
    
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
		
		Log.i(TAG, selectQuery);
	 
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
	    
	    Log.i(TAG, selectQuery);
	    
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
	public ArrayList<BaseBus> getBusesForStop (BaseStop stop) { 
		ArrayList<BaseBus> allBusesByStop = new ArrayList<BaseBus>();
			
		String selectQuery = DbStructure.busesRequestString(stop.getName());
		 
		Log.i(TAG, selectQuery);
		 
		open();
		Cursor busCursor = database.rawQuery(selectQuery, null);
		    
		// looping through all rows and adding to list
		if (busCursor.moveToFirst()) {
		     do {
		        String busName = busCursor.getString(busCursor.getColumnIndex(DbStructure.BUS_NAME)); 
		    	String busDir = busCursor.getString(busCursor.getColumnIndex(DbStructure.BUS_DIR));
		    	BaseBus bus = new BaseBus(busName, busDir);
		            
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
		newStop.put(DbStructure.STOP_GPSLAT, stop.getLatitude());
		newStop.put(DbStructure.STOP_GPSLONG, stop.getLongitude());
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

		Log.i(TAG, selectQuery);

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
		Stop stop = new Stop(stopName, stopStreet1, stopStreet2, stopGpsLat, stopGpsLong); 

		//close the cursor
		stopCursor.close();
		close();
		
		//return the bus
		return stop;

	}

	// selects all stops from STOP table
	@Override
	public ArrayList<BaseStop> getAllStops() { 
		ArrayList<BaseStop> allStops = new ArrayList<BaseStop>();

		// sql commad to select all
		String selectQuery = DbStructure.allStopsRequestString();

		Log.i(TAG, selectQuery);

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
				BaseStop stop = new BaseStop(stopName, stopStreet1, stopStreet2, stopGpsLat, stopGpsLong);

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
	public ArrayList<BaseStop> getStopsByStreet (String street) { 
		ArrayList<BaseStop> allStopsByStreet = new ArrayList<BaseStop>();

		String selectQuery = DbStructure.stopsByStreetRequestString(street);

		Log.i(TAG, selectQuery);

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
				BaseStop stop = new BaseStop(stopName, stopStreet1, stopStreet2, stopGpsLat, stopGpsLong);

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
		updateStop.put(DbStructure.STOP_GPSLAT, stop.getLatitude());
		updateStop.put(DbStructure.STOP_GPSLONG, stop.getLongitude());

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

	
	@Override
	public BaseSchedule getSchedule (String stopName, ArrayList<String> busNames, ArrayList<String> busDirs) 
	{
		int currentDay = DbTime.getWeekDay();
		int currentMinutes = DbTime.getCurrentDbTime();
		
		open();
		Cursor timesCursor = null;
		
		BaseSchedule baseSchedule = new BaseSchedule();
		baseSchedule.setStop(stopName);
		
		
		// before midnight
		
		String selectQuery = DbStructure.scheduleRequestString(stopName, busNames, busDirs, currentDay,
				currentMinutes, 24*60);
		Log.i(TAG, selectQuery);

		timesCursor = database.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (timesCursor.moveToFirst()) {
			do {
				String busName = timesCursor.getString(timesCursor.getColumnIndex(DbStructure.BUS_NAME));
				String busDir = timesCursor.getString(timesCursor.getColumnIndex(DbStructure.BUS_DIR));
				int minutesSinceMidnight = timesCursor.getInt(timesCursor.getColumnIndex(DbStructure.SCHEDULE_TIME));

				baseSchedule.getScheduleItemList().add (new BaseScheduleItem
						                         (new BaseBus(busName, busDir), minutesSinceMidnight));
				
			} while (timesCursor.moveToNext());
		}

		// after midnight
		
		String selectAfterMidnight = DbStructure.scheduleRequestString(stopName, busNames, busDirs, currentDay,
				0, DbTime.DeepNight);
		Log.i(TAG, selectAfterMidnight);

		timesCursor = database.rawQuery(selectAfterMidnight, null);

		// looping through all rows and adding to list
		if (timesCursor.moveToFirst()) {
			do {
				String busName = timesCursor.getString(timesCursor.getColumnIndex(DbStructure.BUS_NAME));
				String busDir = timesCursor.getString(timesCursor.getColumnIndex(DbStructure.BUS_DIR));
				int minutesSinceMidnight = timesCursor.getInt(timesCursor.getColumnIndex(DbStructure.SCHEDULE_TIME))
		                 + 24 * 60;

				baseSchedule.getScheduleItemList().add (new BaseScheduleItem
						                         (new BaseBus(busName, busDir), minutesSinceMidnight));
				
			} while (timesCursor.moveToNext());
		}

		//close the cursor
		timesCursor.close();
		close();
		
		return baseSchedule; 
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

} // end LocalDatabaseConnector
	