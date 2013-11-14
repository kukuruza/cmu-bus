package cmu18641.bustracker.ws.remote;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import cmu18641.bustracker.exceptions.TrackerException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
 
public class Networking {

	public static boolean isNetworkAvailable (Context context) {
	    ConnectivityManager cm = (ConnectivityManager) 
	    		                 context.getSystemService (Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        return false;
    } 
	
	public static String askServer (String urlString) throws TrackerException
	{
    	HttpGet get = new HttpGet (urlString);

	    HttpClient client = new DefaultHttpClient();
	    HttpResponse response = null;
	    try {
			response = client.execute(get);
		} catch (ClientProtocolException e) {
			throw new TrackerException (TrackerException.BAD_REMOTE_RESULT, "Networking.askServer", 
                     "ClientProtocolException sending request");
		} catch (IOException e) {
			throw new TrackerException (TrackerException.BAD_REMOTE_RESULT, "Networking.askServer", 
                     "IOException sending request");
		}
	    
	    String responseString = null;
		try {
			responseString = new BasicResponseHandler().handleResponse(response);
		} catch (HttpResponseException e) {
			throw new TrackerException (TrackerException.BAD_REMOTE_RESULT, "Networking.askServer", 
                    "HttpResponseException extracting response body");
		} catch (IOException e) {
			throw new TrackerException (TrackerException.BAD_REMOTE_RESULT, "Networking.askServer", 
                    "IOException extracting response body");
		}
	    return responseString;
	}

}



