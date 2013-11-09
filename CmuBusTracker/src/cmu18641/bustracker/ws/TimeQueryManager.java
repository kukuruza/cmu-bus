package cmu18641.bustracker.ws;

import java.util.ArrayList;
import cmu18641.bustracker.entities.Bus;
import cmu18641.bustracker.entities.Stop;
import cmu18641.bustracker.entities.TimeQueryInterface;
import cmu18641.bustracker.exceptions.TrackerException;
import android.text.format.Time;

public class TimeQueryManager implements TimeQueryInterface {

	@Override
	public ArrayList<Time> getTimes(Stop stop, ArrayList<Bus> buses)
			throws TrackerException {
		// TODO Auto-generated method stub
		return null;
	}

}
