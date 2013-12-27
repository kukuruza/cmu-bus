<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="bustracker.server.dbhelpers.DatabaseConnector" %>
<%@ page import="bustracker.common.entities.BaseStop" %>
<%@ page import="bustracker.common.dblayout.DbTime" %>



<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
    <title>CMU Bus | choose stop</title>
    <link href="css/styles.css" rel="stylesheet" type="text/css">

    <!-- filter stops -->
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
	<script src="js/sunnywalker-jQuery.FilterTable/jquery.filtertable.min.js"></script>
	<script>
	    $(document).ready(function() {
		    $('table').filterTable();
	    });
	</script>	
</head>

<body>

    <form action="chooseBuses.jsp" method="get" >
        <div  class="input-line w-text">
            Please pick a bus stop and press on: 
            <input type=submit value="choose stop" class=styled-button>
        </div>

 <% 
    // get stops from database
    DatabaseConnector connector = new DatabaseConnector();
    ArrayList<BaseStop> stops = connector.getAllStops();
  %>


 <% if (stops == null) { %>
	    So sorry - an error occurred
 <% } else {
 %>	    
	    <!-- Table header -->
	    <table border="0">
	        <thead>
			    <tr>
		        <th style="padding-right:30px"></th>
		        <th style="padding-right:30px"><b> name </b></th>
		        <th style="padding-right:30px"><b> street 1 </b></th>
		        <th>                           <b> street 2 </b></th>
			    </tr>
			</thead>
			<tbody>
	    
			    <!-- Stops themselves -->
		 <%     int i = 0;
		        for (BaseStop stop : stops) 
			    {
		 %>         <tr>
		            <td style="padding-right:10px">
		                <input type=radio name=stop value="<%= stop.getName() %>" />
		            </td>
		            <td style="padding-right:30px"><%= stop.getName() %></td>
		            <td style="padding-right:30px"><%= stop.getStreet1() %></td>
		            <td style="padding-right:30px"><%= stop.getStreet2() %></td>
		            </tr>
		 <%         ++i;
		        }
 %>      </tbody>
     </table>
        
 <% } %> <!-- end of the 'if' that checks result for error -->

     </form>
  

</body>
</html>