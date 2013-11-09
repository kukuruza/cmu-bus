package cmu18641.bustracker.ws.remote;

import java.util.ArrayList;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import cmu18641.bustracker.entities.*;
import cmu18641.bustracker.exceptions.TrackerException;
import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.Time;



public class RemoteQuery { //implements Query {

	public ArrayList<Time> getTimes (Context context, Stop stop, ArrayList<Bus> buses) throws TrackerException 
	{
		RequestQueue queue = Volley.newRequestQueue (context);
		String url = "https://www.googleapis.com/customsearch/v1?key=AIzaSyBmSXUzVZBKQv9FJkTpZXn0dObKgEQOIFU&cx=014099860786446192319:t5mr0xnusiy&q=AndroidDev&alt=json&searchType=image";
		
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
	
			@Override
			public void onResponse(JSONObject response) {
				Gson gson = new Gson();
				ArrayList<Time> = gson.fromJson(response, ArrayList<Time>.class);
				//txtDisplay.setText("Response => "+response.toString());
				//findViewById(R.id.progressBar1).setVisibility(View.GONE);
			}
		}, new Response.ErrorListener() {
	
			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				
			}
		});
		
		queue.add(jsObjRequest);
	}
}
