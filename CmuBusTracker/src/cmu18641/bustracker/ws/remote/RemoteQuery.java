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

import cmu18641.bustracker.common.entities.BaseSchedule;
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
	    	params.add(new BasicNameValuePair("bus", bus.getName()));
            params.add(new BasicNameValuePair("dir", bus.getDirection()));
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
		String responseString;
		try {
			// TODO: to be moved to configs
			String url = "http://10.0.2.2:8080/webserver/query";
			
			// compose a request
			String requestUrl = formRequestString (url, stop, buses);
			
			// get answer from server
			responseString = Networking.askServer (requestUrl);
		} catch (RuntimeException e) {
			Log.e (TAG, "RuntimeException in askServer");
			throw new TrackerException (0, "RuntimeException in askServer", TAG);
		}
			
		BaseSchedule baseSchedule;
		try {
			// parse json string into TimesMessage object
			Gson gson = new Gson();
			baseSchedule = gson.fromJson (responseString, BaseSchedule.class);
			Log.i(TAG, "baseSchedule name is" + (baseSchedule.getStop() == null ? " null" : "fine"));
		} catch (RuntimeException e) {
			Log.e (TAG, "RuntimeException in parsing to Gson");
			throw new TrackerException (0, "RuntimeException in parsing to Gson", TAG);
		}
		
		Schedule schedule;
		try {
			schedule = FromBaseHelper.fromBase(baseSchedule);
		} catch (RuntimeException e) {
			Log.e (TAG, "RuntimeException in FromBaseHelper");
			throw new TrackerException (0, "RuntimeException in FromBaseHelper", TAG);
		}

		return schedule;
	}
}
