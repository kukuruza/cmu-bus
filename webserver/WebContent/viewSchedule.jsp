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

<jsp:useBean id="route" class="beans.RouteBean" scope="session"/>
<jsp:setProperty name="route" property="*"/>

<body>
 <% // load form data
    String stopName = route.getStopName();
    ArrayList<String> busNames = route.getBusNames();
    ArrayList<String> busDirs = route.getBusDirs();
    
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
	    <b> Stop: <%= stopName %> </b>

	    <!-- Table header -->
	    <table border="0">
	    <tr>
	        <td style="padding-right:30px"><b> bus </b></td>
	        <td style="padding-right:30px"><b> direction </b></td>
	        <td>                           <b> time </b></td>
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