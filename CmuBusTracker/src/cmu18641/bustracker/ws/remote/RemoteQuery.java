package cmu18641.bustracker.ws.remote;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import com.google.gson.Gson;

import cmu18641.bustracker.entities.*;
import cmu18641.bustracker.exceptions.TrackerException;
import android.text.format.Time;


public class RemoteQuery implements TimeQueryInterface {

	private String formRequestString (String url, Stop stop, ArrayList<Bus> buses) 
	{
	    if (!url.endsWith("?"))
	        url += "?";

	    List<NameValuePair> params = new LinkedList<NameValuePair>();

	    params.add(new BasicNameValuePair("stop", stop.getName()));
        
	    while (buses.iterator().hasNext())
	    	params.add(new BasicNameValuePair("buses[]", buses.iterator().next().getName()));

	    String paramString = URLEncodedUtils.format(params, "utf-8");

	    url += paramString;
	    return url;
	}


	@ Override
	public Schedule getSchedule(Stop stop, ArrayList<Bus> buses) throws TrackerException
	{
		// TODO: to be moved to configs
		String url = "https://www.googleapis.com/customsearch/v1?key=AIzaSyBmSXUzVZBKQv9FJkTpZXn0dObKgEQOIFU&cx=014099860786446192319:t5mr0xnusiy&q=AndroidDev&alt=json&searchType=image";
		
		// compose a request
		String requestUrl = formRequestString (url, stop, buses);
		
		// get answer from server
		String responseString = Networking.askServer (requestUrl);
		
		// parse json string into TimesMessage object
		Gson gson = new Gson();
		TimesMessage timesMessage = gson.fromJson (responseString, TimesMessage.class); 
		
		// parse this object into ArrayList<Time>
		return null; //timesMessage.getTimes();
	}
}
