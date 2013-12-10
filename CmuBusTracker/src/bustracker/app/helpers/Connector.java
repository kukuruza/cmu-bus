package bustracker.app.helpers;

import bustracker.app.ws.GlobalManager;

/*
 * Connector.java
 * 
 * Instantiate a single instance of global manager to expose 
 * global manager to all activities. 
 * 
 */

public class Connector {
	 public static GlobalManager globalManager = new GlobalManager(); 
}
