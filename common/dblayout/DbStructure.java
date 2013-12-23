package bustracker.common.dblayout;

import java.util.ArrayList;
import java.util.Locale;


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
    
    

    public static String scheduleRequestString (String stopName, 
    		ArrayList<String> busNames, ArrayList<String> busDirs, int weekDay)
    {
    	assert (busNames.size() == busDirs.size());
    	
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
    
    
    public static void cleanupStreetQuery( String street )
    {
		// capitilize first letter
		street = street.substring(0, 1).toUpperCase(Locale.US) + street.substring(1);

		//cleanup user street query entry
		street = street.toLowerCase(Locale.US); 
		street = street.replace("and", ""); 
		street = street.replace("street", "");
		street = street.replace("avenue", ""); 
		street = street.replace("road", ""); 
		street = street.trim(); 
    }

}
