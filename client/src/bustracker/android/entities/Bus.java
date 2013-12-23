package bustracker.android.entities;

import bustracker.common.entities.BaseBus;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Bus extends BaseBus implements Parcelable {

	public Bus()
	{
		super(); 
	}

	public Bus( String name, String direction )
	{
		super(name, direction); 
	}

	public Bus( Bus another )
	{
		super(another); 
	}
	
	public Bus( BaseBus another )
	{
		super(another); 
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Log.i("BusParcel", "writeToParcel");
	    dest.writeString(getName());
	    dest.writeString(getDirection());
	}
	
	public static final Parcelable.Creator<Bus> CREATOR = new Parcelable.Creator<Bus>() 
    {
		public Bus createFromParcel(Parcel in) {
			return new Bus(in);
		}

		public Bus[] newArray(int size) {
		    return new Bus[size];
		}
	};
	
	public Bus( Parcel source )
	{
		Log.i("BusParcel", "Assemble bus parcel data");
        setName(source.readString()); 
        setDirection(source.readString()); 
    }
	
}
