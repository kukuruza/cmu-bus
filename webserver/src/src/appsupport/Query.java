package src.appsupport;


import java.io.*; 
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import cmu18641.bustracker.common.*;

import com.google.gson.Gson;


@WebServlet("/query")
public class Query extends HttpServlet {
	private static final long serialVersionUID = -443200206040603721L;

	// table names
    private static final String TABLE_BUS = "buses";
    private static final String TABLE_STOP = "stops";
    private static final String TABLE_ROUTE = "routes";
    private static final String TABLE_SCHEDULE = "schedules";
    
    // common column names
    private static final String TIME_STAMP = "timestamp";
    
    // BUS table column names
    private static final String BUS_ID = "busid";
    private static final String BUS_NAME = "busname";
    private static final String BUS_DIR = "busdir";
    
    // STOP table column names
    private static final String STOP_ID = "stopid";
    private static final String STOP_NAME = "stopname";
    private static final String STOP_STREET1 = "stopstreet1";
    private static final String STOP_STREET2 = "stopstreet2";
    private static final String STOP_GPSLAT = "stopgpslat";
    private static final String STOP_GPSLONG = "stopgpslong";
    
    // ROUTE table column names
    private static final String ROUTE_ID = "routeid";
    
    // SCHEDULE table column names
    private static final String SCHEDULE_ID = "scheduleid";
    private static final String SCHEDULE_DAY = "scheduleday";
    private static final String SCHEDULE_TIME = "scheduletime";
	
    
    
	@Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
              throws ServletException, IOException {
		
        PrintWriter out = response.getWriter();
        String s = getName();
        if (s == null)
        	out.println("null \n");
        else
        	out.println(s);
                
        //BaseSchedule schedule = new BaseSchedule();
        //BaseStop stop = new BaseStop ("testStop", "a", "b", 123.4, -234.5);
        //schedule.setStop (stop);
        //schedule.addItem (new BaseScheduleItem(new BaseBus("61c", "downtown"), 
        //		                               1000*60*60*2));
	}
  
	public String getName() {
	    //LOG.info("getting name : " + this.name);
		Connection conn = null;
	    try {
	    	//InitialContext ctx = new InitialContext();
	    	//BasicDataSource ds = (BasicDataSource)ctx.lookup("java:comp/env/jdbc/sched");
	    	
	    	BasicDataSource ds = new BasicDataSource();
	    	ds.setDriverClassName("org.sqlite.JDBC");
	    	ds.setUrl("jdbc:sqlite://Users/evg/Desktop/CMU/course-Java-phones/cmu-bus/webserver/dbs/sched.db");
	    	conn = ds.getConnection();
	    	System.out.println("Successfully opened the database");
	    	  
	        Statement stat = conn.createStatement();

	    	
	    	
	    	String stopName = "Squirrel Hill - FORBES AVE & SHADY AVE";
	    	String busName = "61A";
			int currentDay = 0;
			
			// 1. get stopId from stop table
			// 2. get busid from bus table
			// 3. select a route id from route table where route_busid = busid and route_stopId = stopid
			// 4. select times from schedule table where schedule route_id = route_id, and day = current day
			String selectQuery = "SELECT  * FROM " + TABLE_BUS + " tb, " + TABLE_STOP + " ts, " + TABLE_ROUTE + 
					" tr, " + TABLE_SCHEDULE + " tsc WHERE tb." + BUS_NAME + " = '" + busName + "' AND ts." + 
					STOP_NAME + " = '" + stopName + "' AND ts." + STOP_ID + " = tr." + STOP_ID + " AND tb." + 
					BUS_ID + " = tr." + BUS_ID + " AND tr." + ROUTE_ID + " = tsc." + ROUTE_ID + " AND tsc." + 
					SCHEDULE_DAY + " = '" + currentDay + "'";
	    	ResultSet rs = stat.executeQuery(selectQuery);

	    	String str = "";
	    	while(rs.next())
	    	{
	    	    // now walk each column in the array...
	    	    Object o = rs.getObject("scheduletime");
	    	    String sVal = o.toString();
	    	    str = str + " " + sVal; 
	    	}

	    	  
	    	stat.close();
	    	  
	        return str;
	    } catch (SQLException se) {
	    	se.printStackTrace();
	    	return "SQLException";
	        //LOG.info(se.toString());
//	    } catch (NamingException ne) {
//	    	ne.printStackTrace();
//	    	return "NamingException";
	        //LOG.info(ne.toString());
	    } finally {
	    	try {
				conn.close();
			} catch (SQLException e) {
				System.err.println("Could not close db connection. " +
						           "You may have to restart the server,");
				e.printStackTrace();
			}
	    	
	    }
	}
        
}

