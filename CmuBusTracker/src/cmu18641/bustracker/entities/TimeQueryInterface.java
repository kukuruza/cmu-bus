package cmu18641.bustracker.entities;

import cmu18641.bustracker.exceptions.*;

import java.util.ArrayList;
import android.text.format.Time;


// Notes:
//   - The success status and return value policy:
//         1) functions return what they are supposed to return, not boolean success status,
//         2) in case of failure functions throw exceptions
//   - GPS coordinates are managed by Location class in Android
//

public interface TimeQueryInterface {

	Schedule getSchedule (Stop stop, ArrayList<Bus> buses) throws TrackerException;
}
