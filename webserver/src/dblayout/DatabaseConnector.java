package dblayout;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import cmu18641.bustracker.common.entities.BaseBus;
import cmu18641.bustracker.common.entities.BaseSchedule;
import cmu18641.bustracker.common.entities.BaseScheduleItem;


public class DatabaseConnector {

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
			return dbUrl.substring(firstSlash + 1);
			
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
	    	System.out.println("Successfully opened the database");
	    	
	        _stat = _conn.createStatement();
	        System.out.println("Sucessfully created statement");

	        return true;
	        
		} catch (NamingException ne) {
			// TODO Auto-generated catch block
			ne.printStackTrace();
	    	return false;
	    } catch (SQLException se) {
	    	se.printStackTrace();
	    	// TODO: implement custom exception
	    	return false;
	    }
	}
	
	
	public BaseSchedule getSchedule 
	       (String stopName, ArrayList<String> busesNames, 
			ArrayList<String> busesDirs, int weekDay)
	{
		String selectQuery = DbStructure.scheduleRequestString
				(stopName, busesNames, busesDirs, weekDay);
		System.out.println(selectQuery);
		
		if (!connect()) return null; 
		
    	ResultSet rs;
		try {
			rs = _stat.executeQuery(selectQuery);
		} catch (SQLException e) {
			// TODO: insert custom exception
			e.printStackTrace();
			return null;
		}
		
		BaseSchedule schedule = new BaseSchedule ();
		schedule.setStop(stopName);
		
    	try {
			while(rs.next())
			{
			    String timeVal = rs.getObject("scheduletime").toString();
			    String busnameVal = rs.getObject("busname").toString();
			    String busdirVal = rs.getObject("busdir").toString();
			    BaseBus bus = new BaseBus(busnameVal, busdirVal);
			    schedule.addItem(new BaseScheduleItem(bus, Integer.parseInt(timeVal)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    	
		// TODO: if the result is empty check that bus and stop are valid
		
    	closeConnection();
    	
		return schedule;
	}
	
	
	
	public boolean closeConnection() 
	{
    	try {
    		_stat.close();
			_conn.close();
			return true;
		} catch (SQLException e) {
			System.err.println("Could not close db connection. " +
					           "You may have to restart the server,");
			e.printStackTrace();
			return false;
		}
	}
	
}
