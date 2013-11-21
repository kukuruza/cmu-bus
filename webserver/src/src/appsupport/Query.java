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

	    	ResultSet rs = stat.executeQuery("SELECT direction FROM buses");
	    	String str = rs.getString(1);
	    	  
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

