package cmu18641.bustracker.common.protocols;

public class NetProtocol {

	// parameter string in requests
	public static final String PARAM_BUS_NAME = "bus";
	public static final String PARAM_BUS_DIR = "dir";
	public static final String PARAM_STOP_NAME = "stop";
	
	// string to replace '&' character in URL parameter list
	public static final String AND_CHAR = "_and_";
	
	// index prefixed to values is for keeping order of parameters, as a hack
	//public static final String PREFIX_SEPARATOR = "_";
	
	public static final String ANSWER_ON_ERROR = "error: ";
	
	
	public static String putBackSpecialChars (String str)
	{
		return str.replaceAll(AND_CHAR, "&");
	}
}
