package bustracker.entities;

import java.util.ArrayList;
import android.location.Location;
import android.text.format.Time;

import bustracker.exceptions.*;

// Notes:
//   - The success status and return value policy:
//         1) functions return what they are supposed to return, not boolean success status,
//         2) in case of failure functions throw exceptions
//   - GPS coordinates are managed by Location class in Android
//

public interface Query {

	ArrayList<Bus>    getBusesByStop (ArrayList<Stop> stops) throws TrackerException;
	
	ArrayList<Stop>   getStopsByCurrentLocation (Location here) throws TrackerException;
	
	ArrayList<Stop>   getStopByAddress (String street) throws TrackerException;
	ArrayList<Stop>   getStopByAddress (String street1, String street2) throws TrackerException;
	
	ArrayList<Time>   getTimes (Stop stop, ArrayList<Bus> buses) throws TrackerException;
}
