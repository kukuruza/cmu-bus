package bustracker.common.dblayout;

import java.util.ArrayList;

import bustracker.common.entities.BaseBus;
import bustracker.common.entities.BaseSchedule;
import bustracker.common.entities.BaseStop;


public interface DbConnectorInterface {

	public ArrayList<BaseStop> getAllStops();
	
	public ArrayList<BaseStop> getStopsByStreet (String street);
	
	public BaseSchedule getSchedule (String stopName, ArrayList<String> busesNames, ArrayList<String> busesDirs);
	
	public ArrayList<BaseBus> getBusesForStop (BaseStop stop);
	
}
