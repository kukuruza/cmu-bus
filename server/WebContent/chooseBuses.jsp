<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="bustracker.server.dbhelpers.DatabaseConnector" %>
<%@ page import="bustracker.common.entities.BaseStop" %>
<%@ page import="bustracker.common.entities.BaseBus" %>
<%@ page import="bustracker.common.dblayout.DbTime" %>



<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
  <title>CMU Bus | choose bus</title>
  <link href="css/styles.css" rel="stylesheet" type="text/css">
</head>

<jsp:useBean id="routeBean" class="bustracker.server.beans.RouteBean" scope="session"/>
<jsp:setProperty name="routeBean" property="*"/>


<body>

    <!-- Go and choose stop -->
    
    <form name="chooseStopForm" action="chooseStop.jsp" method="get" >
		<div class=input-line>
            <input type="submit" value="change bus stop" class=styled-button>
		</div>
    </form>

	
	<form action="viewSchedule.jsp" method="get" >
	
	 <% String stopName = (String)request.getParameter("stop");

	    DatabaseConnector connector = new DatabaseConnector();
	    ArrayList<BaseBus> buses = connector.getBusesForStop(stopName);
	    
	    // start filling in routeBean
	    routeBean.setStopName(stopName);
	    ArrayList<String> busNames = new ArrayList<String>();
	    ArrayList<String> busDirs = new ArrayList<String>();

	 %> <h3 class="output-big-line"><%= stopName %></h3>
	 
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
	        	busNames.add(bus.getName());
	        	busDirs.add(bus.getDirection());
             %> <tr>
	            <td style="padding-right:10px">
	                <input type=checkbox name=bus value="<%= i+1 %>" />
	            </td>
	            <td style="padding-right:30px"><%= bus.getName() %></td>
	            <td style="padding-right:30px"><%= bus.getDirection() %></td>
	            </tr>
             <% ++i;
	        }
	        routeBean.setBusNames(busNames);
	        routeBean.setBusDirs(busDirs);
         %> </table>
       
	    <p>
	    <div class=input-line>
	        <input type="submit" value="get schedule" class=styled-button>
	    </div>
    </form>
 
 <% } %> <!-- check for error in buses -->

</body>
</html>