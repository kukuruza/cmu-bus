package bustracker.android.ws;

import java.util.ArrayList;

import bustracker.android.entities.Bus;
import bustracker.android.entities.Schedule;
import bustracker.android.entities.Stop;
import bustracker.android.exceptions.*;

import android.content.Context;



/*
 * This interface is implemented by RomoteQuery and LocalQuery classes
 */

public interface TimeQueryInterface {

	Schedule getSchedule (Context costext, Stop stop, ArrayList<Bus> buses) 
			throws TrackerException;
}
