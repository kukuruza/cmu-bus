<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="dbhelpers.DatabaseConnector" %>
<%@ page import="cmu18641.bustracker.common.entities.BaseSchedule" %>
<%@ page import="cmu18641.bustracker.common.entities.BaseScheduleItem" %>
<%@ page import="dbhelpers.DbTime" %>
    
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>CMU Bus | schedule</title>
</head>

<jsp:useBean id="routeBean" class="beans.RouteBean" scope="session"/>
<jsp:setProperty name="routeBean" property="*"/>

<body>
 <% // load form data
    String stopName = routeBean.getStopName();
    ArrayList<String> busNames = routeBean.getBusNames();
    ArrayList<String> busDirs = routeBean.getBusDirs();
    
    if (stopName == null || busNames == null || busDirs == null) {
      %> Sorry, internal error <%
    } else {
    
	    int weekDay = DbTime.getWeekDay();
	    
	    // get schedule
		DatabaseConnector connector = new DatabaseConnector();
	    BaseSchedule schedule = connector.getSchedule (stopName, busNames, busDirs, weekDay);
	  %>
	 
	 <% if (schedule == null) { %>
		    Sorry, internal error
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
 <% } %> <!-- end of the 'if' that checks error for loading route -->

</body>
</html>