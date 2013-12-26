<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>

<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="bustracker.server.dbhelpers.DatabaseConnector" %>
<%@ page import="bustracker.common.entities.BaseSchedule" %>
<%@ page import="bustracker.common.entities.BaseScheduleItem" %>
<%@ page import="bustracker.common.dblayout.DbTime" %>
    
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
  <title>CMU Bus | schedule</title>
  <link href="css/styles.css" rel="stylesheet" type="text/css">
</head>

<jsp:useBean id="routeBean" class="bustracker.server.beans.RouteBean" scope="session"/>
<jsp:setProperty name="routeBean" property="*"/>

<body>

    <form action="chooseStop.jsp" method="get" >
        <input type=submit value="another query" class=styled-button>
    </form>
    <p>

 <% // load form data
    String stopName = routeBean.getStopName();
    ArrayList<String> allBusNames = routeBean.getBusNames();
    ArrayList<String> allBusDirs = routeBean.getBusDirs();
    
    // get selected buses
    String[] busIdxArr = request.getParameterValues("bus");
    
    if (busIdxArr == null || busIdxArr.length == 0)
      %> No buses were chosen <%
    else if (stopName == null || allBusNames == null || allBusDirs == null) {
      %> Sorry, internal error <%
    } else {
    
        ArrayList<String> busNames = new ArrayList<String>();
        ArrayList<String> busDirs = new ArrayList<String>();
        for (int i = 0; i != busIdxArr.length; ++i)
        {
        	int ind = Integer.parseInt(busIdxArr[i]) - 1;
        	busNames.add(allBusNames.get(ind));
        	busDirs.add (allBusDirs.get(ind));
        }

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
	        	dbTime.setTime(scheduleItem.getTime());
	        	left.setTime (scheduleItem.getTime() - now.getMinutesTotal());
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