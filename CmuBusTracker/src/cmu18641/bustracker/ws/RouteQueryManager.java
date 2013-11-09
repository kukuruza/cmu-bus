package cmu18641.bustracker.ws;

import java.util.ArrayList;
import android.location.Location;
import cmu18641.bustracker.entities.Bus;
import cmu18641.bustracker.entities.RouteQueryInterface;
import cmu18641.bustracker.entities.Stop;
import cmu18641.bustracker.exceptions.TrackerException;


public class RouteQueryManager implements RouteQueryInterface {
	
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

}
