package src.servlets;


import java.io.*; 
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import src.dbhelpers.DatabaseConnector;
import src.dbhelpers.DbStructure;
import cmu18641.bustracker.common.entities.BaseSchedule;
import cmu18641.bustracker.common.protocols.*;

import com.google.gson.Gson;




@WebServlet("/query")
public class QueryServlet extends HttpServlet {
	
	private static final long serialVersionUID = -443200206040603721L;

    private static final Logger logger = LoggerFactory.getLogger(QueryServlet.class);

    
    Map<String, String[]> _parameterMap = null;
    
    String _stopName = null;
    ArrayList<String> _busNames = null;
    ArrayList<String> _busDirs = null;
    
    
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
	protected int getMinutes ()
	{
		Calendar calendar = Calendar.getInstance();
		int min = calendar.get(Calendar.MINUTE);
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		return hours * 60 + min;
	}
	
	
	private void logRequestParameters ()
	{
        if ((_parameterMap != null) && !_parameterMap.isEmpty())
        {
            Map.Entry<String, String[]> entry;
            Iterator<Map.Entry<String, String[]>> iterator = _parameterMap.entrySet().iterator();
            String str = new String();
            String[] values;

            while (iterator.hasNext())
            {
            	str = "";
                entry = iterator.next();
                values = entry.getValue();

                if (values != null)
                    for (int index = 0; index < values.length; ++index)
                    	str = str + ", " + values[index];
                else
                    str = str + "[none]";

                logger.info("Key: {}; Value(s): {}", entry.getKey(), str);
            }
        }
        else
            logger.info("parameterMap is empty.");
	}
	
	
	// returns false when should not process any further
	private boolean processParams (PrintWriter out)
	{
        if ((_parameterMap == null) || _parameterMap.isEmpty())
        {
        	String message = "bad request: empty parameter list";
        	logger.info(message);
        	out.println(NetProtocol.ANSWER_ON_ERROR + message);
        	return false;
        }
        
        // stop
        if (!_parameterMap.containsKey(NetProtocol.PARAM_STOP_NAME))
        {
        	String message = "request does not have STOP NAME";
        	logger.info(message);
        	out.println(NetProtocol.ANSWER_ON_ERROR + message);
        	return false;
        }
        String[] stopNameArr = _parameterMap.get(NetProtocol.PARAM_STOP_NAME);
        if (stopNameArr.length > 1)
        {
        	logger.info("request has multiple STOP NAMES");
        }
        _stopName = new String(stopNameArr[0]);
        
        // buses
        if (!_parameterMap.containsKey(NetProtocol.PARAM_BUS_NAME))
        {
        	// this is a valid request
        	logger.info("request has empty BUS NAMES list");
        	return false;
        }
        if (!_parameterMap.containsKey(NetProtocol.PARAM_BUS_DIR))
        {
        	// this is a valid request
        	logger.info("request has empty BUS DIRECTIONS list");
        	return false;
        }
        String[] busNamesArr = _parameterMap.get(NetProtocol.PARAM_BUS_NAME);
        String[] busDirsArr  = _parameterMap.get(NetProtocol.PARAM_BUS_DIR);
        if (busNamesArr.length != busDirsArr.length)
        {
        	String message = "bad request: different lengths of bus names and dirs";
        	logger.info(message);
        	out.println(NetProtocol.ANSWER_ON_ERROR + message);
        	return false;
        }
        
        _busNames = new ArrayList<String>(Arrays.asList(busNamesArr));
        _busDirs  = new ArrayList<String>(Arrays.asList(busDirsArr));
        
        return true;
	}
	
	
	private boolean replaceSpecialCharacters (Map<String, String[]> parameterMap)
	{
        if ((parameterMap != null) && !parameterMap.isEmpty())
        {
            Map.Entry<String, String[]> entry;
            Iterator<Map.Entry<String, String[]>> iterator = parameterMap.entrySet().iterator();
            String[] values;

            while (iterator.hasNext())
            {
                entry = iterator.next();
                values = entry.getValue();
                
                if (values != null)
                    for (int index = 0; index < values.length; ++index)
						try {
							values[index] = URLDecoder.decode(values[index], "utf-8");
						} catch (UnsupportedEncodingException e) {
							return false;
						}
            }
        }
        return true;
	}
	

	@Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
              throws ServletException, IOException 
    {	
        PrintWriter out = response.getWriter();

        _stopName = null;
        _busNames = null;
        _busDirs  = null;
        
        // get parameters from request
        _parameterMap = request.getParameterMap();
        replaceSpecialCharacters(_parameterMap);
        logRequestParameters ();
        if (!processParams (out))
        	return;  // this may be an error or a valid empty request
		
		int weekDay = getWeekDay();
		
		// get schedule from database
		DatabaseConnector connector = new DatabaseConnector();
        BaseSchedule schedule = connector.getSchedule (_stopName, _busNames, _busDirs, weekDay);
        if (schedule == null)
        {
        	out.println(NetProtocol.ANSWER_ON_ERROR);
        	return;
        }

        // send the answer
        Gson gson = new Gson();
        String json = gson.toJson(schedule);  
        out.println(json);
	}
  
}

