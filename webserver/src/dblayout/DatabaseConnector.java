package dblayout;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import cmu18641.bustracker.common.entities.BaseBus;
import cmu18641.bustracker.common.entities.BaseSchedule;
import cmu18641.bustracker.common.entities.BaseScheduleItem;


public class DatabaseConnector {

	private Connection _conn;
	private Statement _stat;

    //private DbStructure dbStructure;
    
	
	// get the connection to our database
	private boolean connect()
	{
	    try {
	    	//InitialContext ctx = new InitialContext();
	    	//BasicDataSource ds = (BasicDataSource)ctx.lookup("java:comp/env/jdbc/sched");
	    	
	    	BasicDataSource ds = new BasicDataSource();
	    	ds.setDriverClassName("org.sqlite.JDBC");
	    	// FIXME: use web.xml
	    	ds.setUrl("jdbc:sqlite://Users/evg/Desktop/CMU/course-Java-phones/cmu-bus/webserver/dbs/sched.db");
	    	_conn = ds.getConnection();
	    	System.out.println("Successfully opened the database");
	    	
	        _stat = _conn.createStatement();
	        System.out.println("Sucessfully created statement");

	        return true;
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
