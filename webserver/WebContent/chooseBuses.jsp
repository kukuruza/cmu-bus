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
<body>

    <form name="getScheduleForm" action="viewSchedule.jsp" method="get" >


 <% String stopName = new String("Fifth Ave at Wood St");
    ArrayList<String> busNames = new ArrayList<String>();
    busNames.add("61A");
    ArrayList<String> busDirs = new ArrayList<String>();
    busDirs.add("Braddock");
    
    //int weekDay = TimeHelper.getWeekDay();
    int weekDay = 0;
    
    // get schedule
	DatabaseConnector connector = new DatabaseConnector();
    BaseSchedule schedule = connector.getSchedule (stopName, busNames, busDirs, weekDay);
  %>
  
    <input type="submit" value="get schedule">
    </form>


</body>
</html>