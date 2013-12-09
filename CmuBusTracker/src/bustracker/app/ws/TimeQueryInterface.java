package bustracker.app.ws;

import java.util.ArrayList;

import bustracker.app.entities.Bus;
import bustracker.app.entities.Schedule;
import bustracker.app.entities.Stop;
import bustracker.app.exceptions.*;

import android.content.Context;



/*
 * This interface is implemented by RomoteQuery and LocalQuery classes
 */

public interface TimeQueryInterface {

	Schedule getSchedule (Context costext, Stop stop, ArrayList<Bus> buses) 
			throws TrackerException;
}
