package helpers;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import cmu18641.bustracker.activities.SettingsActivity;
import cmu18641.bustracker.entities.Bus;
import cmu18641.bustracker.entities.Stop;
import cmu18641.bustracker.entities.UserChoice;

/*
 * This class manages user favorites: stop + a set of buses
 * Every such favorite item is stored as a json string
 * Due to limitations of API-10, we have to manually manage array of strings, 
 *   and this is done via "|" separator between json items
 */

public class Favorites {
	private static final String TAG = "TAG";
	
	private static final String SplitChar = "-";
    
	// manage storing array of strings as a string in favorites
	private static String addToString (String srcStr, String newStr)
	{
		assert (srcStr != null);
		assert (newStr != null);
		if (srcStr.equals(""))
			return newStr;
		else
    		return new String (srcStr + SplitChar + newStr); 
	}
	
	// manage storing array of strings as a string in favorites
	private static ArrayList<String> parseIntoStrings (String inStr)
	{
		String[] strArr = inStr.split(SplitChar);
		return new ArrayList<String>(  Arrays.asList(strArr) );
	}
	
	// parse json strings as UserChoice objects 
	private static ArrayList<UserChoice> parseIntoObjects (ArrayList<String> strList)
	{
		ArrayList<UserChoice> objectList = new ArrayList<UserChoice>();
		for (String str : strList)  
	    { 
			Gson gson = new Gson();
			objectList.add( gson.fromJson (str, UserChoice.class) );
	    }
		return objectList;
	}
	
	
	public static boolean addToFavorites (Context context, Stop stop)
	{
		try {
			// check that the bus set is not empty
			if (buses.size() == 0)
			{
				Log.i (TAG, "Tried to add favorites with empty bus set");
				Toast.makeText(context, "Could not save to favorite\n " + 
						"This set of buses is empty", Toast.LENGTH_LONG).show();
				return false;
			}
			
			// retrieve favorites
			SharedPreferences settings = context.getSharedPreferences ("preferences", Context.MODE_PRIVATE);
			String favorites_str = settings.getString(SettingsActivity.KEY_FAVORITES, "");
			Log.d (TAG, "Initial favorites string in addToFavorites: " + favorites_str);
			
			// make json string from object
			UserChoice newObj = new UserChoice (stop.getName(), buses);
			Gson gson = new Gson();
			String json = gson.toJson(newObj);
			Log.d (TAG, "Json string of new object in addToFavorites: \n" + json);
			
			// check that all the objects are different from new one
			ArrayList<String> strList = parseIntoStrings (favorites_str);
			ArrayList<UserChoice> objectList = parseIntoObjects (strList);
			Log.d (TAG, "number of objects in preferences: " + objectList.size());
			for (UserChoice oldObj : objectList)
			{
				if (oldObj == null) continue;
				if (newObj.equals(oldObj))
				{
					Log.i (TAG, "Tried to same favorite from existing stop: " + newObj.stopName);
					Toast.makeText(context, "Could not save to favorite\n " + 
											"This stop is already there", Toast.LENGTH_LONG).show();
					return false;
				}
			}
			
			// add to existing json string
			favorites_str = addToString(favorites_str, json);
			Log.d (TAG, "Final string to write in addToFavorites: \n" + favorites_str);
			
			// write settings
			SharedPreferences.Editor editor = settings.edit();
			editor.putString(SettingsActivity.KEY_FAVORITES, favorites_str);
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
	
	public static ArrayList<UserChoice> getFavorites (Context context)
	{
		try {
			// retrieve favorites
			SharedPreferences settings = context.getSharedPreferences ("preferences", 0);
			String favorites_str = settings.getString(SettingsActivity.KEY_FAVORITES, "");
			
			// parse string into objects
			ArrayList<String> strList = parseIntoStrings (favorites_str);
			ArrayList<UserChoice> objectList = parseIntoObjects (strList);
			Log.d (TAG, "Read favorites");
			return objectList;
			
		} catch (Exception e) {
			Log.e (TAG, "Error while reading and parsing favorites: " + e.getMessage());
			Toast.makeText(context, "Could not get your favorites \n" + 
					"Realy sorry, internal error", Toast.LENGTH_LONG).show();
			return null;
		}
	}
}
