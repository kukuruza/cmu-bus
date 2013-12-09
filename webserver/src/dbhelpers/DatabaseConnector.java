package dbhelpers;

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

import servlets.QueryServlet;
import cmu18641.bustracker.common.entities.BaseBus;
import cmu18641.bustracker.common.entities.BaseSchedule;
import cmu18641.bustracker.common.entities.BaseScheduleItem;
import cmu18641.bustracker.common.entities.BaseStop;


public class DatabaseConnector {

	private static final Logger logger = LoggerFactory.getLogger(QueryServlet.class);

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
		
		String selectQuery = DbStructure.scheduleRequestString
				(stopName, busesNames, busesDirs, weekDay);
		logger.info(selectQuery);

		BaseSchedule schedule = new BaseSchedule ();
		schedule.setStop(stopName);
		
		if (!connect()) return null; 
		
		// rs - is the usual one, rs2 - is after midnight
    	ResultSet rs;
		try {
			rs = _stat.executeQuery(selectQuery);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		int currentMin = DbTime.getCurrentDbTime();
    	try {
			while(rs.next())
			{
			    String timeVal = rs.getObject("scheduletime").toString();
			    // logic is to be removed from here
			    int minutesTotal = Integer.parseInt(timeVal);
			    if (currentMin > minutesTotal)
			    	continue;
			    String busnameVal = rs.getObject("busname").toString();
			    String busdirVal = rs.getObject("busdir").toString();
			    BaseBus bus = new BaseBus(busnameVal, busdirVal);
			    schedule.addItem(new BaseScheduleItem(bus, minutesTotal));
			}
    	} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

    	/*
		final int deepNight = 4*60;
		String selectQueryAfterMidnight = DbStructure.scheduleRequestString
				(stopName, busesNames, busesDirs, weekDay, 0, deepNight);
		logger.info("after midnight: " + selectQueryAfterMidnight);
		
		try {
			rs = _stat.executeQuery(selectQueryAfterMidnight);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
			
    	try {
			System.out.println("-");
			while(rs.next())
			{
				System.out.println("+");
			    String timeVal = rs.getObject("scheduletime").toString();
			    int minutesTotal = Integer.parseInt(timeVal) + 24 * 60;
			    String busnameVal = rs.getObject("busname").toString();
			    String busdirVal = rs.getObject("busdir").toString();
			    BaseBus bus = new BaseBus(busnameVal, busdirVal);
			    schedule.addItem(new BaseScheduleItem(bus, minutesTotal));
			}
    	} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}*/
		
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


	
	public ArrayList<BaseBus> getBusesForStop (BaseStop stop)
	{
		String selectQuery = DbStructure.busesRequestString(stop.getName());
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
