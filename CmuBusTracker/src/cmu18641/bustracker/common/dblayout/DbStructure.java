package cmu18641.bustracker.common.dblayout;

import java.util.ArrayList;


public class DbStructure {
	
	// weekDay
    public static final int WEEK_WEEKDAY = 0;
    public static final int WEEK_SATURDAY = 1;
    public static final int WEEK_SUNDAY_HOLIDAY = 2;
    
	

	// table names
    public static final String TABLE_BUS = "buses";
    public static final String TABLE_STOP = "stops";
    public static final String TABLE_ROUTE = "routes";
    public static final String TABLE_SCHEDULE = "schedules";
    
    // common column names
    public static final String TIME_STAMP = "timestamp";
    
    // BUS table column names
    public static final String BUS_ID = "busid";
    public static final String BUS_NAME = "busname";
    public static final String BUS_DIR = "busdir";
    
    // STOP table column names
    public static final String STOP_ID = "stopid";
    public static final String STOP_NAME = "stopname";
    public static final String STOP_STREET1 = "stopstreet1";
    public static final String STOP_STREET2 = "stopstreet2";
    public static final String STOP_GPSLAT = "stopgpslat";
    public static final String STOP_GPSLONG = "stopgpslong";
    
    // ROUTE table column names
    public static final String ROUTE_ID = "routeid";
    
    // SCHEDULE table column names
    public static final String SCHEDULE_ID = "scheduleid";
    public static final String SCHEDULE_DAY = "scheduleday";
    public static final String SCHEDULE_TIME = "scheduletime";
	
    
    // get all stops from the stops table
    public static String allStopsRequestString()
    {
	    return  "SELECT * FROM " + TABLE_STOP;
    }
    
    // get all stops from the table where one of the streets matches
    public static String stopsByStreetRequestString (String street)
    {
	    return "SELECT * " + 
               "FROM " + TABLE_STOP + " ts " + 
               "WHERE " + 
               "ts." + STOP_STREET1 + " = '" + street + "' OR " +
               "ts." + STOP_STREET2 + " = '" + street + "'";
    }


    // get all buses for a single stop
    public static String busesRequestString(String stopName)
    {
		return  "SELECT " + 
        BUS_NAME + ", " + 
        BUS_DIR + " " + 
        "FROM " + 
        TABLE_BUS + " tb, " + 
        TABLE_STOP + " ts, " + 
        TABLE_ROUTE + " tr " + 
        "WHERE " + 
        "ts." + STOP_NAME + " = '" + stopName + "' AND " + 
        "ts." + STOP_ID + " = tr." + STOP_ID + " AND " + 
        "tb." + BUS_ID + " = tr." + BUS_ID;
    }
    
    

    public static String scheduleRequestString 
        (String stopName, String busName, String busDir, int weekDay)
    {
		// 1. get stopId from stop table
		// 2. get busid from bus table
		// 3. select a route id from route table where route_busid = busid and route_stopId = stopid
		// 4. select times from schedule table where schedule route_id = route_id, and day = current day
		return  "SELECT " + 
		        BUS_NAME + ", " + 
		        BUS_DIR + ", " + 
		        SCHEDULE_TIME + " " +
		        "FROM " + 
		        TABLE_BUS + " tb, " + 
		        TABLE_STOP + " ts, " + 
		        TABLE_ROUTE + " tr, " + 
		        TABLE_SCHEDULE + " tsc " + 
		        "WHERE " + 
		        "tb." + BUS_NAME + " = '" + busName + "' AND " + 
		        "tb." + BUS_DIR + " = '" + busDir + "' AND " + 
		        "ts." + STOP_NAME + " = '" + stopName + "' AND " + 
		        "ts." + STOP_ID + " = tr." + STOP_ID + " AND " + 
		        "tb." + BUS_ID + " = tr." + BUS_ID + " AND " + 
		        "tr." + ROUTE_ID + " = tsc." + ROUTE_ID + " AND " + 
		        "tsc." + SCHEDULE_DAY + " = '" + weekDay + "'";
    }

    
    public static String scheduleRequestString (String stopName, 
    		ArrayList<String> busNames, ArrayList<String> busDirs, int weekDay)
    {
    	assert (busNames.size() == busDirs.size());
    	
		// 1. get stopId from stop table
		// 2. get busid from bus table
		// 3. select a route id from route table where route_busid = busid and route_stopId = stopid
		// 4. select times from schedule table where schedule route_id = route_id, and day = current day
		String s = "SELECT " + 
		        BUS_NAME + ", " + 
		        BUS_DIR + ", " + 
		        SCHEDULE_TIME + " " +
		        "FROM " + 
		        TABLE_BUS + " tb, " + 
		        TABLE_STOP + " ts, " + 
		        TABLE_ROUTE + " tr, " + 
		        TABLE_SCHEDULE + " tsc " + 
		        "WHERE ";
		        // this code enters multiple buses
		        s = s + "(";
		        for (int i = 0; i != busNames.size(); ++i)
		        {
		        	s = s + 
		            "tb." + BUS_NAME + " = '" + busNames.get(i) + "' AND " + 
		            "tb." + BUS_DIR + " = '" + busDirs.get(i);
		        	if (i == busNames.size()-1) 
		        		s = s + "') AND ";
		        	else
		        		s = s + "' OR ";
		        }
		        s = s +
		        "ts." + STOP_NAME + " = '" + stopName + "' AND " + 
		        "ts." + STOP_ID + " = tr." + STOP_ID + " AND " + 
		        "tb." + BUS_ID + " = tr." + BUS_ID + " AND " + 
		        "tr." + ROUTE_ID + " = tsc." + ROUTE_ID + " AND " + 
		        "tsc." + SCHEDULE_DAY + " = '" + weekDay + "' " +
		        "ORDER BY " + SCHEDULE_TIME;
        return s;
    }
    
    
    public static String scheduleRequestString (String stopName, 
    		ArrayList<String> busNames, ArrayList<String> busDirs, int weekDay, 
    		int minMinutes, int maxMinutes)
    {
    	assert (busNames.size() == busDirs.size());
    	
		// 1. get stopId from stop table
		// 2. get busid from bus table
		// 3. select a route id from route table where route_busid = busid and route_stopId = stopid
		// 4. select times from schedule table where schedule route_id = route_id, and day = current day
		String s = "SELECT " + 
		        BUS_NAME + ", " + 
		        BUS_DIR + ", " + 
		        SCHEDULE_TIME + " " +
		        "FROM " + 
		        TABLE_BUS + " tb, " + 
		        TABLE_STOP + " ts, " + 
		        TABLE_ROUTE + " tr, " + 
		        TABLE_SCHEDULE + " tsc " + 
		        "WHERE ";
		        // this code enters multiple buses
		        s = s + "(";
		        for (int i = 0; i != busNames.size(); ++i)
		        {
		        	s = s + 
		            "tb." + BUS_NAME + " = '" + busNames.get(i) + "' AND " + 
		            "tb." + BUS_DIR + " = '" + busDirs.get(i);
		        	if (i == busNames.size()-1) 
		        		s = s + "') AND ";
		        	else
		        		s = s + "' OR ";
		        }
		        s = s +
		        "ts." + STOP_NAME + " = '" + stopName + "' AND " + 
		        "ts." + STOP_ID + " = tr." + STOP_ID + " AND " + 
		        "tb." + BUS_ID + " = tr." + BUS_ID + " AND " + 
		        "tr." + ROUTE_ID + " = tsc." + ROUTE_ID + " AND " + 
		        "tsc." + SCHEDULE_DAY + " = '" + weekDay + "' AND " +
		        "tsc." + SCHEDULE_TIME + " >= " + Integer.toString(minMinutes) + " AND " +
		        "tsc." + SCHEDULE_TIME + " < " + Integer.toString(maxMinutes) + " " +
		        "ORDER BY " + SCHEDULE_TIME;
        return s;
    }
    
    
     

}
