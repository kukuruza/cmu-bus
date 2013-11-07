package bustracker.entities;

import java.util.ArrayList;

import android.location.Location;
import android.text.format.Time;
import bustracker.exceptions.TrackerException;

public class QueryManager implements Query {

	@Override
	public ArrayList<Bus> getBusesByStop(ArrayList<Stop> stops)
			throws TrackerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Stop> getStopsByCurrentLocation(Location here)
			throws TrackerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Stop> getStopByAddress(String street)
			throws TrackerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Stop> getStopByAddress(String street1, String street2)
			throws TrackerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Time> getTimes(Stop stop, ArrayList<Bus> buses)
			throws TrackerException {
		// TODO Auto-generated method stub
		return null;
	}

}
