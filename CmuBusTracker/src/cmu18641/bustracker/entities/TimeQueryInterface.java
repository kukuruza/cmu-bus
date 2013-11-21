package cmu18641.bustracker.entities;

import java.util.ArrayList;

import cmu18641.bustracker.exceptions.*;


public interface TimeQueryInterface {

	Schedule getSchedule (Stop stop, ArrayList<Bus> buses) 
			throws TrackerException;
}
