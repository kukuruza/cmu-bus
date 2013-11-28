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
import android.util.Log;
 
public class Networking {
	private static final String TAG = "Networking"; 

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
			Log.d (TAG, "start client.execute");
			response = client.execute(get);
		} catch (ClientProtocolException e) {
			Log.e (TAG, "got ClientProtocolException");
			throw new TrackerException (TrackerException.BAD_REMOTE_RESULT, TAG, 
                     "ClientProtocolException sending request");
		} catch (IOException e) {
			Log.e (TAG, "got IOException executing request");
			throw new TrackerException (TrackerException.BAD_REMOTE_RESULT, TAG, 
                     "IOException sending request");
		}
	    
	    String responseString = null;
		try {
			Log.d (TAG, "start handleResponse");
			responseString = new BasicResponseHandler().handleResponse(response);
		} catch (HttpResponseException e) {
			Log.e (TAG, "got HttpResponseException");
			throw new TrackerException (TrackerException.BAD_REMOTE_RESULT, TAG, 
                    "HttpResponseException extracting response body");
		} catch (IOException e) {
			Log.e (TAG, "got IOException parsing http response");
			throw new TrackerException (TrackerException.BAD_REMOTE_RESULT, TAG, 
                    "IOException extracting response body");
		}
	    return responseString;
	}

}



