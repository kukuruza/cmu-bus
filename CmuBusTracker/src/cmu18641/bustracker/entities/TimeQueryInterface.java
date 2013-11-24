package cmu18641.bustracker.entities;

import java.util.ArrayList;

import android.content.Context;

import cmu18641.bustracker.exceptions.*;


public interface TimeQueryInterface {

	Schedule getSchedule (Context costext, Stop stop, ArrayList<Bus> buses) 
			throws TrackerException;
}
