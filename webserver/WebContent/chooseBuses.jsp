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

	    <!-- When everything is ready get schedule -->
	
		<form name="getScheduleForm" action="viewSchedule.jsp" method="get" >
		
		 <% BaseStop stop = stops.get(Integer.parseInt(stopNum)-1);
		    String stopName = stop.getName();
		    
		 %> <b><%= stopName %></b> <%
		    
		    ArrayList<String> busNames = new ArrayList<String>();
		    busNames.add("61A");
		    ArrayList<String> busDirs = new ArrayList<String>();
		    busDirs.add("Braddock");
		    
		    // set form data
		    route.setStopName(stopName);
		    route.setBusNames(busNames);
		    route.setBusDirs(busDirs);
		    
		  %>
		  
		    <p><input type="submit" value="get schedule">
	    </form>
	    
 <% } %> <!-- check for data consistency -->

</body>
</html>