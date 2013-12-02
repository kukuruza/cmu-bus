<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="dbhelpers.DatabaseConnector" %>
<%@ page import="cmu18641.bustracker.common.entities.BaseStop" %>
<%@ page import="cmu18641.bustracker.common.entities.BaseBus" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="dbhelpers.DbTime" %>



<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>CMU Bus | choose bus</title>
</head>

<jsp:useBean id="route" class="beans.RouteBean" scope="session"/>
<jsp:setProperty name="route" property="*"/>

<jsp:useBean id="allStopsBean" class="beans.AllStopsBean" scope="session"/>
<jsp:setProperty name="allStopsBean" property="*"/>


<body>

    <!-- Go and choose stop -->
    
    <form name="chooseStopForm" action="chooseStop.jsp" method="get" >
        <input type="submit" value="choose another bus stop">
    </form>



    
 <% // load what chooseStop.jsp was supposed to write
    String stopNum = (String)request.getParameter("stop");
    ArrayList<BaseStop> stops = allStopsBean.getStops();
    
    if (stops == null || stopNum == null) { %>
        Error: first go and choose a stop using the form
 <% } else if (Integer.parseInt(stopNum)-1 >= stops.size()) { %>
        Error: incorrect stop number. First go and choose a stop usng the form
 <% } else { %>

	    <!-- When everything is ready list buses  -->
	
		<form action="viewSchedule.jsp" method="get" >
		
		 <% BaseStop stop = stops.get(Integer.parseInt(stopNum)-1);
			String stopName = stop.getName();
	
		    DatabaseConnector connector = new DatabaseConnector();
		    ArrayList<BaseBus> buses = connector.getBusesForStop(stop);
	
		 %> <b><%= stopName %></b>
		 
		 <% if (buses == null) { %>
			   <p>Sorry, internal error
	     <% } else { %>
			 
				<!-- Table header -->
			    <table border="0">
			    <tr>
			        <td style="padding-right:30px"></td>
			        <td style="padding-right:30px"><b> bus </b></td>
			        <td>                           <b> direction </b></td>
			    </tr>
			    
			    <!-- Buses itself -->
	         <% int i = 0;
		        for (BaseBus bus : buses) 
			    {
	             %> <tr>
		            <td style="padding-right:10px">
		                <input type=checkbox name=bus value="<%= i+1 %>" />
		            </td>
		            <td style="padding-right:30px"><%= bus.getName() %></td>
		            <td style="padding-right:30px"><%= bus.getDirection() %></td>
		            </tr>
	             <% ++i;
		        } 
	         %> </table>
        
		    <p><input type="submit" value="get schedule">
	    </form>
	 
	 <% } %> <!-- check for error in buses -->
	    
 <% } %> <!-- check for data consistency -->

</body>
</html>