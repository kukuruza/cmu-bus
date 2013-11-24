package src.servlets;


import java.io.*; 
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmu18641.bustracker.common.*;

import com.google.gson.Gson;

import dblayout.DatabaseConnector;
import dblayout.DbStructure;




@WebServlet("/query")
public class QueryServlet extends HttpServlet {
	
	private static final long serialVersionUID = -443200206040603721L;

    private static final Logger logger = LoggerFactory.getLogger(QueryServlet.class);

    
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
	
	
	// get time in format of the DB - minutes since midnight
	private int getMinutes ()
	{
		Calendar calendar = Calendar.getInstance();
		int min = calendar.get(Calendar.MINUTE);
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		return hours * 60 + min;
	}
	
	
	@Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
              throws ServletException, IOException {
		
        Map<String, String[]> parameterMap = request.getParameterMap();

        if ((parameterMap != null) && !parameterMap.isEmpty())
        {
            Map.Entry<String, String[]> entry;
            int index;
            Iterator<Map.Entry<String, String[]>> iterator = parameterMap.entrySet().iterator();
            String str = new String();
            String[] values;

            while (iterator.hasNext())
            {
                entry = iterator.next();
                values = entry.getValue();

                if (values != null)
                {
                    for (index = 0; index < values.length; ++index)
                    {
                    	str = str + ", ";
                        str = str + values[index];
                    }
                }
                else
                {
                    str = str + "[none]";
                }

                logger.info("Key: {}; Value(s): {}", entry.getKey(), str);
            }
        }
        else
        {
            logger.info("parameterMap is empty.");
        }

		
		int weekDay = getWeekDay();
		// FIXME: when weekday schedule is in DB
		weekDay = 0;
		
		// TODO: filter on time
		//int minutes = getMinutes ();
		
        PrintWriter out = response.getWriter();

        
        String stopName = "Squirrel Hill - FORBES AVE & MURRAY AVE";
		ArrayList<BaseBus> busList = new ArrayList<BaseBus>();
		//busList.add(new BaseBus ("61A", "Braddock"));
		busList.add(new BaseBus ("61A", "Downtown"));
		
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

