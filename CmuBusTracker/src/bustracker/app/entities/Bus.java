package bustracker.app.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Bus implements Parcelable {

	private String _name;
	private String _direction; 
	
	public Bus (String name, String direction)
	{
		_name = name;
		_direction = direction; 
	}
	public Bus (Bus another)
	{
		_name = another.getName(); 
		_direction = another.getDirection(); 
	}
	
	public String getName()
	{
		return new String (_name);
	}
	
	public String getDirection() 
	{
		return new String (_direction); 
	}
	

	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Log.i("BusParcel", "writeToParcel");
	    dest.writeString(_name);
	    dest.writeString(_direction);
	}
	
	public static final Parcelable.Creator<Bus> CREATOR
    		= new Parcelable.Creator<Bus>() {
		public Bus createFromParcel(Parcel in) {
			return new Bus(in);
		}

		public Bus[] newArray(int size) {
		    return new Bus[size];
		}
	};
	
	public Bus(Parcel source){
		Log.i("BusParcel", "Assemble bus parcel data");
        _name = source.readString(); 
        _direction = source.readString(); 
    }
	
}
