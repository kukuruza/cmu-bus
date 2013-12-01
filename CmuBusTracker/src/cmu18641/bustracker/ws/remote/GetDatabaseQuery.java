package cmu18641.bustracker.ws.remote;

import android.content.Context;
import android.util.Log;
import cmu18641.bustracker.R;
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

	public void downloadDb (Context context, String pathToSave) 
			throws TrackerException
	{
		String serverUrl = context.getResources().getString(R.string.server_timequery_url);

		try {
			Networking.requestFile (serverUrl, pathToSave); 
		} catch (TrackerException e) {
			Log.e (TAG, "Could not load database");
			throw e;
		} catch (Exception e) {
			Log.wtf (TAG, "Exception, could not load database \n");
			e.printStackTrace();
			throw new TrackerException(0, "Exception, could not load database", TAG);
		}

	}
	
}
