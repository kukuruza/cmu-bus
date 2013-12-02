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
			
			// involes knowledge that database zip file is needed
			//path = path + ".zip";
			
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
	       (String stopName, ArrayList<String> busesNames, 
			ArrayList<String> busesDirs, int weekDay)
	{
		String selectQuery = DbStructure.scheduleRequestString
				(stopName, busesNames, busesDirs, weekDay);
		logger.info(selectQuery);
		
		if (!connect()) return null; 
		
    	ResultSet rs;
		try {
			rs = _stat.executeQuery(selectQuery);
		} catch (SQLException e) {
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
			logger.error("Could not close db connection. " +
					     "You may have to restart the server,");
			e.printStackTrace();
			return false;
		}
	}
	
}
