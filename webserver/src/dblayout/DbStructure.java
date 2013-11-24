package dblayout;

public class DbStructure {
	
    public static final int WEEK_WEEKDAY = 0;
    public static final int WEEK_SATURDAY = 1;
    public static final int WEEK_SUNDAY_HOLIDAY = 2;
    
	

	// table names
    protected static final String TABLE_BUS = "buses";
    protected static final String TABLE_STOP = "stops";
    protected static final String TABLE_ROUTE = "routes";
    protected static final String TABLE_SCHEDULE = "schedules";
    
    // common column names
    protected static final String TIME_STAMP = "timestamp";
    
    // BUS table column names
    protected static final String BUS_ID = "busid";
    protected static final String BUS_NAME = "busname";
    protected static final String BUS_DIR = "busdir";
    
    // STOP table column names
    protected static final String STOP_ID = "stopid";
    protected static final String STOP_NAME = "stopname";
    protected static final String STOP_STREET1 = "stopstreet1";
    protected static final String STOP_STREET2 = "stopstreet2";
    protected static final String STOP_GPSLAT = "stopgpslat";
    protected static final String STOP_GPSLONG = "stopgpslong";
    
    // ROUTE table column names
    protected static final String ROUTE_ID = "routeid";
    
    // SCHEDULE table column names
    protected static final String SCHEDULE_ID = "scheduleid";
    protected static final String SCHEDULE_DAY = "scheduleday";
    protected static final String SCHEDULE_TIME = "scheduletime";
	

    public static String scheduleRequestString (String stopName, String busName, int weekDay)
    {
		// 1. get stopId from stop table
		// 2. get busid from bus table
		// 3. select a route id from route table where route_busid = busid and route_stopId = stopid
		// 4. select times from schedule table where schedule route_id = route_id, and day = current day
		return  "SELECT  * FROM " + 
		        TABLE_BUS + " tb, " + 
		        TABLE_STOP + " ts, " + 
		        TABLE_ROUTE + " tr, " + 
		        TABLE_SCHEDULE + " tsc " + 
		        "WHERE " + 
		        "tb." + BUS_NAME + " = '" + busName + "' AND " + 
		        "ts." + STOP_NAME + " = '" + stopName + "' AND " + 
		        "ts." + STOP_ID + " = tr." + STOP_ID + " AND " + 
		        "tb." + BUS_ID + " = tr." + BUS_ID + " AND " + 
		        "tr." + ROUTE_ID + " = tsc." + ROUTE_ID + " AND " + 
		        "tsc." + SCHEDULE_DAY + " = '" + weekDay + "'";
    }
}
