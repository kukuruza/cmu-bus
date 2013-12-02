<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="dbhelpers.DatabaseConnector" %>
<%@ page import="cmu18641.bustracker.common.entities.BaseSchedule" %>
<%@ page import="cmu18641.bustracker.common.entities.BaseScheduleItem" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="dbhelpers.DbTime" %>
    
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>CMU Bus | choose buses</title>
</head>
<body>
 <% String stopName = new String("Fifth Ave at Wood St");
    ArrayList<String> busNames = new ArrayList<String>();
    busNames.add("61A");
    ArrayList<String> busDirs = new ArrayList<String>();
    busDirs.add("Braddock");
    
    //int weekDay = TimeHelper.getWeekDay();
    int weekDay = 0;
    
    // get schedule
	DatabaseConnector connector = new DatabaseConnector();
    BaseSchedule schedule = connector.getSchedule (stopName, busNames, busDirs, weekDay);
  %>
 
 <% if (schedule == null) { %>
	    So sorry - an error occurred
 <% } else {
        ArrayList<BaseScheduleItem> scheduleItemList = schedule.getScheduleItemList();
 %>	    
	    <%= stopName %>

	    <!-- Table header -->
	    <table border="0">
	    <tr>
	        <td style="padding-right:30px"> bus </td>
	        <td style="padding-right:30px"> direction </td>
	        <td>                            time </td>
	    </tr>
	    
	    <!-- Schedule itself -->
 <%     DbTime dbTime = new DbTime();
        for (BaseScheduleItem scheduleItem : scheduleItemList) 
	    {
        	dbTime.setTime((int)(scheduleItem.getTime()));
 %>         <tr>
            <td><%= scheduleItem.getBus().getName() %></td>
            <td><%= scheduleItem.getBus().getDirection() %></td>
            <td><%= dbTime.toString() %></td>
            </tr>
 <%     } %>
        </table>
        
 <% } %> <!-- end of the 'if' that checks result for error -->

</body>
</html>