package cmu18641.bustracker.entities;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class LocationService extends Service implements LocationListener{

	private static final String TAG = "LocationService";
	
    private final Context mContext;
 
    boolean isGPSEnabled = false;  // flag for GPS status
    boolean isNetworkEnabled = false;  // flag for network status
 
    Location currentLocation; 
    double latitude; 
    double longitude; 
 
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 1 meters
    private static final long MIN_TIME_CHANGE_FOR_UPDATES = 500; // 30 seconds
 
    protected LocationManager locationManager;
 
    public LocationService(Context context) {
        this.mContext = context;
        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        initalizeLocationService(); 
    }
	
    private void initalizeLocationService() {
        try {
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
 
            // gps is more accurate
            if(isGPSEnabled) {
            	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            MIN_TIME_CHANGE_FOR_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                
            	currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(currentLocation != null) {
                	latitude = currentLocation.getLatitude();
                    longitude = currentLocation.getLongitude();
                }
            
                Log.i(TAG, "GPS enabled");
            }
            else if(isNetworkEnabled) { 
            	locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_CHANGE_FOR_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            
            	currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            	if(currentLocation != null) {
            		latitude = currentLocation.getLatitude();
            		longitude = currentLocation.getLongitude();
            	}
        
            Log.i(TAG, "Network enabled");
            }
            else { 
            	Log.i(TAG, "No provider enabled"); 
            	showSettingsAlert(); 
            	currentLocation = null; 
            }
            
        } catch(Exception e) { 
        	Log.e("LocationService", "exception", e); 
        	if(!canGetLocation()) { 
        		currentLocation = null; 
        	}
        }
    }
     
    // stop location provider updates
    public void stopUsingLocation(){
        if(locationManager != null){
            locationManager.removeUpdates(LocationService.this);
        }       
    }
     
    // return location
    public Location getLocation() { 
    	return currentLocation; 
    }
    
    // return latitude
    public double getLatitude(){
        if(currentLocation != null){
            latitude = currentLocation.getLatitude();
        }
        return latitude;
    }
     
    // return longitude
    public double getLongitude(){
        if(currentLocation != null){
            longitude = currentLocation.getLongitude();
        }
        return longitude;
    }
     
    // return true if location can be determined
    public boolean canGetLocation() {
        return isGPSEnabled || isNetworkEnabled; 
    }
     
    // notify user if gps not enabled
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
     
        alertDialog.setTitle("GPS settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
  
        // on pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
  
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
  
        alertDialog.show();
    }
 
    @Override
    public void onLocationChanged(Location location) {
   
    }
 
    @Override
    public void onProviderDisabled(String provider) {
    }
 
    @Override
    public void onProviderEnabled(String provider) {
    }
 
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
 
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    
}
