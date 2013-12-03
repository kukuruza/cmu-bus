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
    
	    // get schedule
		DatabaseConnector connector = new DatabaseConnector();
	    BaseSchedule schedule = connector.getSchedule (stopName, busNames, busDirs);
	  %>
	 
	 <% if (schedule == null) { %>
		    Sorry, internal error
	 <% } else {
	        ArrayList<BaseScheduleItem> scheduleItemList = schedule.getScheduleItemList();
	 %>	    
		    <b> Stop: <%= stopName %> </b>
	
	        <!-- Print current time -->
	 <%     DbTime now = new DbTime();
	        now.setTime (DbTime.getCurrentDbTime());
	 %>     <p> Current time: <%= now.toString() %>
	
		    <!-- Table header -->
		    <table border="0">
		    <tr>
		        <td style="padding-right:30px"><b> bus </b></td>
		        <td style="padding-right:30px"><b> direction </b></td>
		        <td style="padding-right:30px"><b> time </b></td>
		        <td>                           <b> minutes left </b>
		    </tr>
		    
		    <!-- Schedule itself -->
	 <%     DbTime dbTime = new DbTime();
	        DbTime left = new DbTime();
	        for (BaseScheduleItem scheduleItem : scheduleItemList) 
		    {
	        	dbTime.setTime((int)(scheduleItem.getTime()));
	        	left.setTime (dbTime.getMinutesTotal() - now.getMinutesTotal());
	 %>         <tr>
	            <td style="padding-right:30px"><%= scheduleItem.getBus().getName() %></td>
	            <td style="padding-right:30px"><%= scheduleItem.getBus().getDirection() %></td>
	            <td style="padding-right:30px"><%= dbTime.toString() %></td>
	            <td>                           <%= left.getMinutesTotal() + " " %> min. </td>
	            </tr>
	 <%     } %>
	        </table>
	        
	 <% } %> <!-- end of the 'if' that checks result for error -->
 <% } %> <!-- end of the 'if' that checks error for loading route -->

</body>
</html>