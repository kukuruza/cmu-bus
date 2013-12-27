package bustracker.server.dbhelpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bustracker.common.dblayout.DbConnectorInterface;
import bustracker.common.dblayout.DbStructure;
import bustracker.common.dblayout.DbTime;
import bustracker.common.entities.BaseBus;
import bustracker.common.entities.BaseSchedule;
import bustracker.common.entities.BaseScheduleItem;
import bustracker.common.entities.BaseStop;


public class DatabaseConnector implements DbConnectorInterface {

	private static final Logger logger = LoggerFactory.getLogger(DatabaseConnector.class);

	private static final String dbResource = "java:comp/env/jdbc/sched";

	private Connection _conn;
	private Statement _stat;
	
	// takes no parameters, just copies the whole file
	public static String getDbPath ()
	{
		try {
			InitialContext ctx = new InitialContext();
			BasicDataSource ds = (BasicDataSource) ctx.lookup(dbResource);
			String dbUrl = ds.getUrl();
			
			// simple workaround for getting actual file path
			// involves knowledge of how database url is organized 
			int firstSlash = dbUrl.indexOf ('/');
			String path = dbUrl.substring(firstSlash + 1);
			
			return path;
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	// get the connection to our database
	private boolean connect()
	{
	    try {
	    	InitialContext ctx = new InitialContext();
	    	BasicDataSource ds = (BasicDataSource) ctx.lookup(dbResource);
	    	
	    	_conn = ds.getConnection();
	    	logger.info("Successfully opened the database");
	    	
	        _stat = _conn.createStatement();
	        logger.info("Sucessfully created statement");

	        return true;
	        
		} catch (NamingException ne) {
			ne.printStackTrace();
	    	return false;
	    } catch (SQLException se) {
	    	se.printStackTrace();
	    	return false;
	    }
	}
	
	
	public BaseSchedule getSchedule 
	       (String stopName, ArrayList<String> busesNames, ArrayList<String> busesDirs)
	{
	    int weekDay = DbTime.getWeekDay();
		int currentMin = DbTime.getCurrentDbTime();
		
		String selectQueryMain = DbStructure.scheduleRequestString
				(stopName, busesNames, busesDirs, weekDay, currentMin, 24*60);
		logger.info(selectQueryMain);

		BaseSchedule schedule = new BaseSchedule ();
		schedule.setStop(stopName);
		
		if (!connect()) return null; 
		
		// rs - is the usual one, rs2 - is after midnight
    	ResultSet rs;
		try {
			rs = _stat.executeQuery(selectQueryMain);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		logger.info ("current minutes: " + Integer.toString(currentMin));
    	try {
			while(rs.next())
			{
			    String timeStr = rs.getObject("scheduletime").toString();
			    int minutesTotal = Integer.parseInt(timeStr);
			    String busnameVal = rs.getObject("busname").toString();
			    String busdirVal = rs.getObject("busdir").toString();
			    BaseBus bus = new BaseBus(busnameVal, busdirVal);
			    schedule.addItem(new BaseScheduleItem(bus, minutesTotal));
			}
    	} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		String selectQueryAfterMidnight = DbStructure.scheduleRequestString
				(stopName, busesNames, busesDirs, weekDay, 0, DbTime.DeepNight);
		logger.info("after midnight: " + selectQueryAfterMidnight);
		
		try {
			rs = _stat.executeQuery(selectQueryAfterMidnight);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
			
    	try {
			while(rs.next())
			{
			    String timeVal = rs.getObject("scheduletime").toString();
			    int minutesTotal = Integer.parseInt(timeVal) + 24 * 60;
			    System.out.println("minutesTotal: " + minutesTotal);
			    String busnameVal = rs.getObject("busname").toString();
			    String busdirVal = rs.getObject("busdir").toString();
			    BaseBus bus = new BaseBus(busnameVal, busdirVal);
			    schedule.addItem(new BaseScheduleItem(bus, minutesTotal));
			}
    	} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
    	closeConnection();
    	
		return schedule;
	}
	
	
	public ArrayList<BaseStop> getAllStops()
	{
		String selectQuery = DbStructure.allStopsRequestString();
		logger.info("getAllStops: " + selectQuery);
		
		if (!connect()) return null; 
		
		ResultSet rs;
		try {
			rs = _stat.executeQuery(selectQuery);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		ArrayList<BaseStop> stops = new ArrayList<BaseStop>();
		try {
			while(rs.next())
			{
				String name      = rs.getObject(DbStructure.STOP_NAME).toString();
			    String street1   = rs.getObject(DbStructure.STOP_STREET1).toString();
			    String street2   = rs.getObject(DbStructure.STOP_STREET2).toString();
			    double latitude  = Double.parseDouble(rs.getObject(DbStructure.STOP_GPSLAT).toString());
			    double longitude = Double.parseDouble(rs.getObject(DbStructure.STOP_GPSLONG).toString());
			    stops.add(new BaseStop(name, street1, street2, latitude, longitude));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		closeConnection();
		
		return stops;
	}
	
	
	
	public ArrayList<BaseStop> getStopsByStreet (String street)
	{
		if (street == null)
		{
			logger.error ("getStopsByStreet: supplied street is null");
			return null;
		}
		
		// clean up street name
		DbStructure.cleanupStreetQuery(street);
		
		
		String selectQuery = DbStructure.stopsByStreetRequestString (street);
		logger.info("getStopsByStreet with street " + street + ": " + selectQuery);
		
		if (!connect()) return null; 
		
		ResultSet rs;
		try {
			rs = _stat.executeQuery(selectQuery);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		ArrayList<BaseStop> stops = new ArrayList<BaseStop>();
		try {
			while(rs.next())
			{
				String name      = rs.getObject(DbStructure.STOP_NAME).toString();
			    String street1   = rs.getObject(DbStructure.STOP_STREET1).toString();
			    String street2   = rs.getObject(DbStructure.STOP_STREET2).toString();
			    double latitude  = Double.parseDouble(rs.getObject(DbStructure.STOP_GPSLAT).toString());
			    double longitude = Double.parseDouble(rs.getObject(DbStructure.STOP_GPSLONG).toString());
			    stops.add(new BaseStop(name, street1, street2, latitude, longitude));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		closeConnection();
		
		return stops;
	}


	
	public ArrayList<BaseBus> getBusesForStop (String stopName)
	{
		String selectQuery = DbStructure.busesRequestString(stopName);
		logger.info("getBusesForStop: " + selectQuery);
		
		if (!connect()) return null; 
		
		ResultSet rs;
		try {
			rs = _stat.executeQuery(selectQuery);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		ArrayList<BaseBus> buses = new ArrayList<BaseBus>();
		try {
			while(rs.next())
			{
				String name = rs.getObject(DbStructure.BUS_NAME).toString();
			    String dir  = rs.getObject(DbStructure.BUS_DIR).toString();
			    buses.add (new BaseBus(name, dir));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		closeConnection();
		
		return buses;
	}

	
	
	public boolean closeConnection() 
	{
    	try {
    		_stat.close();
			_conn.close();
			return true;
		} catch (SQLException e) {
			logger.error("Could not close db connection. " +
					     "You may have to restart the server,");
			e.printStackTrace();
			return false;
		}
	}
	
}
