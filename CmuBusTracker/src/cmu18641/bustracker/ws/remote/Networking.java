package cmu18641.bustracker.ws.remote;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import cmu18641.bustracker.exceptions.TrackerException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
 
public class Networking {
	private static final String TAG = "Networking"; 
	
	// Set the timeout in milliseconds until a connection is established.
	// The default value is zero, that means the timeout is not used. 
	private final static int TimeoutConnection = 1000;
	// Set the default socket timeout (SO_TIMEOUT) 
	// in milliseconds which is the timeout for waiting for data.
	private final static int TimeoutSocket = 2000;


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
	
	public static String askServer (String serverUrlString) throws TrackerException
	{
    	HttpGet get = new HttpGet (serverUrlString);
    	HttpParams httpParameters = new BasicHttpParams();
    	HttpConnectionParams.setConnectionTimeout(httpParameters, TimeoutConnection);
    	HttpConnectionParams.setSoTimeout(httpParameters, TimeoutSocket);


	    HttpClient client = new DefaultHttpClient (httpParameters);
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
	
	
	public static void requestFile (String serverUrlString, String fileSavePath) 
			throws TrackerException
	{
    	HttpGet get = new HttpGet (serverUrlString);
    	HttpParams httpParameters = new BasicHttpParams();
    	HttpConnectionParams.setConnectionTimeout(httpParameters, TimeoutConnection);
    	HttpConnectionParams.setSoTimeout(httpParameters, TimeoutSocket);


	    HttpClient client = new DefaultHttpClient (httpParameters);
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
	    
        InputStream input = null;
        OutputStream output = null;
        byte[] buffer = new byte[1024];

        try {
			Log.d (TAG, "start handleResponse");
			
            Log.i (TAG, "Downloading file...");
            input = response.getEntity().getContent();
            output = new FileOutputStream (fileSavePath);
            int bytes = 0;
            for (int length; (length = input.read(buffer)) > 0;) {
                output.write(buffer, 0, length);
                bytes += length;
            }
            Log.i (TAG, "File successfully downloaded, copied " + 
                         Integer.toString(bytes) + " bytes");

		} catch (HttpResponseException e) {
			Log.e (TAG, "got HttpResponseException");
			throw new TrackerException (TrackerException.BAD_REMOTE_RESULT, TAG, 
                    "HttpResponseException extracting response body");
		} catch (IOException e) {
			Log.e (TAG, "got IOException parsing http response");
			throw new TrackerException (TrackerException.BAD_REMOTE_RESULT, TAG, 
                    "IOException extracting response body");
		} finally {
            if (output != null) try { output.close(); } catch (IOException logOrIgnore) {}
            if (input != null) try { input.close(); } catch (IOException logOrIgnore) {}
        }
	}

}



