package cmu18641.bustracker.entities;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import cmu18641.bustracker.exceptions.TrackerException;


//Notes:
//- The success status and return value policy:
//    1) functions return what they are supposed to return, not boolean success status,
//    2) in case of failure functions throw exceptions
//- GPS coordinates are managed by Location class in Android
//

public interface RouteQueryInterface {

	ArrayList<Bus>    getBusesByStop (Context context, Stop stop) throws TrackerException;
	
	ArrayList<Stop>   getStopsByCurrentLocation (Context context, Location here) throws TrackerException;
	
	ArrayList<Stop>   getStopByAddress (Context context, String street) throws TrackerException;
	//ArrayList<Stop>   getStopByAddress (String street1, String street2) throws TrackerException;

}
