package src.appsupport;


import java.io.*; 
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

import cmu18641.bustracker.common.*;

import com.google.gson.Gson;

import dblayout.DatabaseConnector;
import dblayout.DbStructure;


@WebServlet("/query")
public class Query extends HttpServlet {
	private static final long serialVersionUID = -443200206040603721L;

	
	// this function decides if it is weekday or saturday or sunday/holiday
	// TODO: implement holiday
	private int getWeekDay()
	{
    	Calendar calendar = Calendar.getInstance();
	    int day = calendar.get(Calendar.DAY_OF_WEEK);
	    
	    if (day == Calendar.SUNDAY)
	    	return DbStructure.WEEK_SUNDAY_HOLIDAY;
	    else if (day == Calendar.SATURDAY)
	    	return DbStructure.WEEK_SATURDAY;
	    else
	    	return DbStructure.WEEK_WEEKDAY;
	}
	
	
	@Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
              throws ServletException, IOException {
		
        PrintWriter out = response.getWriter();
    	String stopName = "Squirrel Hill - FORBES AVE & MURRAY AVE";
    	String busName = "61A";
    	String busDir = "Braddock";
    	
		int weekDay = getWeekDay();
		// FIXME: when weekday schedule is in DB
		weekDay = 0;
		
		ArrayList<BaseBus> busList = new ArrayList<BaseBus>();
		busList.add(new BaseBus (busName, busDir));
		
		DatabaseConnector connector = new DatabaseConnector();
        BaseSchedule schedule = connector.getSchedule (stopName, busList, weekDay);
        if (schedule == null)
            // TODO: output error
        	out.println("null \n");

        Gson gson = new Gson();
        String json = gson.toJson(schedule);  

        out.println(json);
	}
  
}

