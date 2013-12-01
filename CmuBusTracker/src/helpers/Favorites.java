package helpers;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import cmu18641.bustracker.activities.SettingsActivity;
import cmu18641.bustracker.entities.Stop;

/*
 * This class manages user favorites: arraylist of stopNames
 * Favorites are stored as a json string
 */

public class Favorites {
	private static final String TAG = "TAG";
	
	public static boolean addToFavorites (Context context, Stop stop)
	{
		try {
			// new stop
			String newStopName = stop.getName();
			
			// retrieve favorites
			SharedPreferences settings = context.getSharedPreferences ("preferences", Context.MODE_PRIVATE);
			String inJson = settings.getString(SettingsActivity.KEY_FAVORITES, "");
			Log.d (TAG, "Initial favorites string in addToFavorites: " + inJson);
			
			// init gson
			Gson gson = new Gson();
			Type type = new TypeToken<ArrayList<String>>(){}.getType();
			
			// read favorites
			ArrayList<String> favoritesList = new ArrayList<String>();
			if (!inJson.equals(""))
		        favoritesList = gson.fromJson(inJson, type);
			Log.d (TAG, "number of objects in preferences: " + favoritesList.size());
			

			// check that all the objects are different from new one
			for (String stopName : favoritesList)
			{
				if (stopName == null) continue;
				if (stopName.equals(newStopName))
				{
					Log.i (TAG, "Tried to same favorite from existing stop: " + newStopName);
					Toast.makeText(context, "Could not save to favorite\n " + 
											"This stop is already there", Toast.LENGTH_LONG).show();
					return false;
				}
			}
			
			// add to existing json string
			favoritesList.add(newStopName);
			
			// back to gson
			String outJson = gson.toJson(favoritesList);
			
			// write settings
			SharedPreferences.Editor editor = settings.edit();
			editor.putString(SettingsActivity.KEY_FAVORITES, outJson);
			if (!editor.commit())
			{
				Toast.makeText(context, "Could not save to favorite \n" + 
						"Sorry, internal error", Toast.LENGTH_LONG).show();
				Log.e (TAG, "Error adding an object to favorites settings");
				return false;
			}
			
			Toast.makeText(context, "Added to favorites", Toast.LENGTH_LONG).show();
			return true;
		} catch (Exception e) {
			Log.e (TAG, "Error while adding favorites.");
			e.printStackTrace();
			Toast.makeText(context, "Could not save to favorite \n" + 
					"Sorry, internal error", Toast.LENGTH_LONG).show();
			return false;
		}	
	}
	
	public static ArrayList<String> getFavorites (Context context)
	{
		try {
			// retrieve favorites
			SharedPreferences settings = context.getSharedPreferences ("preferences", Context.MODE_PRIVATE);
			String inJson = settings.getString(SettingsActivity.KEY_FAVORITES, "");
			Log.d (TAG, "Initial favorites string in addToFavorites: " + inJson);
			
			// init gson
			Gson gson = new Gson();
			Type type = new TypeToken<ArrayList<String>>(){}.getType();
			
			// read favorites
			ArrayList<String> favoritesList = new ArrayList<String>();
			if (!inJson.equals(""))
		        favoritesList = gson.fromJson(inJson, type);
			Log.d (TAG, "number of objects in preferences: " + favoritesList.size());
			
			return favoritesList;
			
		} catch (Exception e) {
			Log.e (TAG, "Error while reading and parsing favorites: " + e.getMessage());
			Toast.makeText(context, "Could not get your favorites \n" + 
					"Realy sorry, internal error", Toast.LENGTH_LONG).show();
			return null;
		}
	}
}
