package dblayout;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;


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
	
	
	public String getSchedule (String stopName, ArrayList<String> buses, int weekDay)
	{
		String busName = buses.get(0);
		
		String selectQuery = DbStructure.scheduleRequestString(stopName, busName, weekDay);
		
		if (!connect()) return null; 
		
    	ResultSet rs;
		try {
			rs = _stat.executeQuery(selectQuery);
		} catch (SQLException e) {
			// TODO: insert custom exception
			e.printStackTrace();
			return null;
		}
		
    	String str = "";
    	try {
			while(rs.next())
			{
			    // now walk each column in the array...
			    Object o = rs.getObject("scheduletime");
			    String sVal = o.toString();
			    str = str + " " + sVal; 
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	
    	closeConnection();
    	
		return str;
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
