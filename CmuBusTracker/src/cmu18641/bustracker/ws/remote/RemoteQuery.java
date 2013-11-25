package cmu18641.bustracker.ws.remote;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import cmu18641.bustracker.common.BaseSchedule;
import cmu18641.bustracker.entities.*;
import cmu18641.bustracker.exceptions.TrackerException;


public class RemoteQuery implements TimeQueryInterface {
	private final String TAG = "RemoteQuery";

	private String formRequestString (String url, Stop stop, ArrayList<Bus> buses) 
	{
	    if (!url.endsWith("?"))
	        url += "?";

	    List<NameValuePair> params = new LinkedList<NameValuePair>();

	    params.add(new BasicNameValuePair("stop", stop.getName()));
        
	    for (Bus bus : buses)
	    {
	    	params.add(new BasicNameValuePair("bus[]", bus.getName()) );
            params.add(new BasicNameValuePair("dir[]", bus.getDirection()));
        }

	    String paramString = URLEncodedUtils.format(params, "utf-8");	    
	    url += paramString;
	    
	    Log.i(TAG, "url=" + url);
	    return url;
	}


	@ Override
	public Schedule getSchedule (Context context, Stop stop, ArrayList<Bus> buses) 
			throws TrackerException
	{
		// TODO: to be moved to configs
		String url = "http://10.0.2.2:8080/webserver/querymock";
		
		// compose a request
		String requestUrl = formRequestString (url, stop, buses);
		
		// get answer from server
		String responseString = "alala";
		try {
			AskServer askServer = new AskServer(); 
			askServer.execute(requestUrl);
			askServer.onPostExecute(responseString);
			Log.i(TAG, "Let's see: " + responseString);
    		if (responseString.equals("alala"))
    			throw new TrackerException(TrackerException.BAD_REMOTE_RESULT, TAG,
					"askServer failed");
		} catch (Exception e) {
			Log.e(TAG, "failed to ask server - " + e.getMessage());
			throw new TrackerException(TrackerException.BAD_REMOTE_RESULT, TAG,
					"askServer failed");
		}
		
		Log.e(TAG, "Here");
		
		// parse json string into TimesMessage object
		Gson gson = new Gson();
		BaseSchedule baseSchedule = gson.fromJson (responseString, BaseSchedule.class); 
		
		Schedule schedule = FromBaseHelper.fromBase(baseSchedule);
		schedule.log(TAG);
		return schedule;
	}
}
