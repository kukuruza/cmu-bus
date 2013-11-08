package cmu18641.bustracker.entities;

import static org.junit.Assert.*;

import org.junit.Test;

import cmu18641.bustracker.entities.Stop;
import cmu18641.bustracker.entities.StopAdapter;

import android.location.Location;

public class StopAdapterTest {

	@Test
	public void testZeroDistance() {
		Location zero = new Location(new String("dummy"));
		Stop stop = new Stop ("zero", "one", "another", zero);
		StopAdapter adapter = new StopAdapter(stop);
		
		float miles = adapter.getDistance (zero);
		assertTrue (miles == 0);
		fail("Not yet implemented");
	}

}
