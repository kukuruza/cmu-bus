package cmu18641.bustracker.ws.local;

import java.util.ArrayList;

import cmu18641.bustracker.entities.*;
import cmu18641.bustracker.exceptions.TrackerException;
import android.location.Location;
import android.text.format.Time;



public class LocalQuery implements Query {

	@Override
	public ArrayList<Bus> getBusesByStop(ArrayList<Stop> stops) throws TrackerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Stop> getStopsByCurrentLocation(Location here) throws TrackerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Stop> getStopByAddress(String street) throws TrackerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Stop> getStopByAddress(String street1, String street2) throws TrackerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Time> getTimes(Stop stop, ArrayList<Bus> buses) throws TrackerException {
		// TODO Auto-generated method stub
		return null;
	}

}
