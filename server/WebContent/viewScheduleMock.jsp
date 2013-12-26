<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="bustracker.server.dbhelpers.DatabaseConnector" %>
<%@ page import="bustracker.common.entities.BaseSchedule" %>
<%@ page import="com.google.gson.Gson" %>
    
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
  <title>CMU Bus | mock schedule</title>
  <link href="css/styles.css" rel="stylesheet" type="text/css">
</head>

<body>
 <% String stopName = new String("Fifth Ave at Wood St");
    ArrayList<String> busNames = new ArrayList<String>();
    busNames.add("61A");
    ArrayList<String> busDirs = new ArrayList<String>();
    busDirs.add("Braddock");
	DatabaseConnector connector = new DatabaseConnector();
    BaseSchedule schedule = connector.getSchedule (stopName, busNames, busDirs);
  %>
 
 <% if (schedule == null) { %>
	    So sorry - an error occured
 <% } else {
		Gson gson = new Gson();
		String json = gson.toJson(schedule);  
  %> <%= 
		json 
  %> <% 
    } 
  %>

</body>
</html>