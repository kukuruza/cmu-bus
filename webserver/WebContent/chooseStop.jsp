<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="dbhelpers.DatabaseConnector" %>
<%@ page import="cmu18641.bustracker.common.entities.BaseStop" %>
<%@ page import="dbhelpers.DbTime" %>



<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>CMU Bus | choose stop</title>
</head>

<jsp:useBean id="allStopsBean" class="beans.AllStopsBean" scope="session"/>
<jsp:setProperty name="allStopsBean" property="*"/>

<body>

        <form action="chooseStop.jsp" method="get" >
        Show only stops on this street: 
	    <input type=text name=street>
	    <input type=submit value="Update list">
	</form>

    <p>
    <form action="chooseBuses.jsp" method="get" >
        Please pick a bus stop and press on: 
        <input type=submit value="choose stop">


 <% 
    // get stops
    // using bean to carry all the stops to avoid reloading
    ArrayList<BaseStop> stops;
    //if (allStopsBean.getStops() == null)
    //{
    	// get from database
    	String street = (String)request.getParameter("street");
        DatabaseConnector connector = new DatabaseConnector();
    	if (street == null)
            stops = connector.getAllStops();
    	else
            stops = connector.getStopsByStreet(street);
        allStopsBean.setStops(stops);
    //}
    //else
    	// get pre-loaded
    	//stops = allStopsBean.getStops();
  %>


 <% if (stops == null) { %>
	    So sorry - an error occurred
 <% } else {
 %>	    
	    <!-- Table header -->
	    <table border="0">
	    <tr>
	        <td style="padding-right:30px"></td>
	        <td style="padding-right:30px"><b> name </b></td>
	        <td style="padding-right:30px"><b> street 1 </b></td>
	        <td>                           <b> street 2 </b></td>
	    </tr>
	    
	    <!-- Stops themselves -->
 <%     int i = 0;
        for (BaseStop stop : stops) 
	    {
 %>         <tr>
            <td style="padding-right:10px">
                <input type=radio name=stop value="<%= i+1 %>" />
            </td>
            <td style="padding-right:30px"><%= stop.getName() %></td>
            <td style="padding-right:30px"><%= stop.street1 %></td>
            <td style="padding-right:30px"><%= stop.street2 %></td>
            </tr>
 <%         ++i;
        } 
 %>     </table>
        
 <% } %> <!-- end of the 'if' that checks result for error -->

     </form>
  


</body>
</html>