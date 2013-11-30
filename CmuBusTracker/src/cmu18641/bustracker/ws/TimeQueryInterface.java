package cmu18641.bustracker.ws;

import java.util.ArrayList;

import android.content.Context;

import cmu18641.bustracker.entities.Bus;
import cmu18641.bustracker.entities.Schedule;
import cmu18641.bustracker.entities.Stop;
import cmu18641.bustracker.exceptions.*;


public interface TimeQueryInterface {

	Schedule getSchedule (Context costext, Stop stop, ArrayList<Bus> buses) 
			throws TrackerException;
}
