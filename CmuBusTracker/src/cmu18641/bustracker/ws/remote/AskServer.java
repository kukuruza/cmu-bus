package cmu18641.bustracker.ws.remote;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
 
public class AskServer extends AsyncTask<String, Void, String> {
	private final static String TAG = "AskServer";

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
	
	@Override
	protected String doInBackground(String... params)
	{
		assert (params.length > 0);
		String urlString = params[0];
		
    	HttpGet get = new HttpGet (urlString);

	    HttpClient client = new DefaultHttpClient();
	    HttpResponse response = null;
	    
	    try {
			response = client.execute(get);
		} catch (ClientProtocolException e) {
			Log.e (TAG, "ClientProtocolException sending request" + e.getMessage());
			return null;
		} catch (IOException e) {
			Log.e (TAG, "IOException sending request. " + e.getMessage());
			return null;
		}
		    
	    String responseString = null;
		try {
			responseString = new BasicResponseHandler().handleResponse(response);
		} catch (HttpResponseException e) {
			Log.e (TAG, "HttpResponseException extracting response body" + e.getMessage());
			return null;
		} catch (IOException e) {
			Log.e (TAG, "IOException extracting response body" + e.getMessage());
			return null;
		}
		
		if (responseString == null)
			Log.i (TAG, "responseString is null");
		else
    		Log.i (TAG, responseString);
	    return responseString;
	}

	@Override
	protected void onPostExecute(String result) {
	}
}



