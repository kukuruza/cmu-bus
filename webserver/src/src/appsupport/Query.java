package src.appsupport;


import java.io.*; 
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import cmu18641.bustracker.common.*;

import com.google.gson.Gson;

import dblayout.DatabaseConnector;


@WebServlet("/query")
public class Query extends HttpServlet {
	private static final long serialVersionUID = -443200206040603721L;

    //private DbStructure dbStructure;
    
	@Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
              throws ServletException, IOException {
		
        PrintWriter out = response.getWriter();
    	String stopName = "Squirrel Hill - FORBES AVE & SHADY AVE";
    	String busName = "61A";
		int weekDay = 0;
		
		ArrayList<String> busList = new ArrayList<String>();
		busList.add(busName);
		
		DatabaseConnector connector = new DatabaseConnector();
        String s = connector.getSchedule (stopName, busList, weekDay);
        if (s == null)
        	out.println("null \n");
        else
        	out.println(s);
                
	
	}
  
}

