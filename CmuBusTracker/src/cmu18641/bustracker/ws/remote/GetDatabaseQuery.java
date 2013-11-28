package cmu18641.bustracker.ws.remote;

import android.util.Log;
import cmu18641.bustracker.exceptions.TrackerException;

/*
 * UpdateDbQuery
 * 
 * This class is responsible for asking the server 
 *   to send a copy of the current schedule database.
 *   
 * It the database is sent as a byte stream to be saved to a specified file path
 */

public class GetDatabaseQuery {
	private final static String TAG = "DownloadDbQuery";

	public void downloadDb (String pathToSave) throws TrackerException
	{
		String serverUrl = "http://10.0.2.2:8080/webserver/getdatabase";

		try {
			Networking.requestFile (serverUrl, pathToSave); 
		} catch (TrackerException e) {
			Log.e (TAG, "Could not load database");
			throw e;
		}
	}
	
}
