package cmu18641.bustracker.common.protocols;

public class NetProtocol {

	// parameter string in requests
	public static final String PARAM_BUS_NAME = "bus";
	public static final String PARAM_BUS_DIR = "dir";
	public static final String PARAM_STOP_NAME = "stop";
	
	// replace '&', '?', '=' characters to prepare for URL parameter list
	public static final String AND_CHAR = "_and_";
	public static final String QUESTION_CHAR = "_ques_";
	public static final String EQUAL_CHAR = "_equal_";
	
	
	public static final String ANSWER_ON_ERROR = "error: ";
	
	
	// app replaces special chars before sending to server,
	//   and the server parses them back
	public static String replaceSpecialChars (String str)
	{
		str = str.replaceAll("&", AND_CHAR);
		str = str.replaceAll("?", QUESTION_CHAR);
		str = str.replaceAll("=", EQUAL_CHAR);
		return str;
	}
	public static String putBackSpecialChars (String str)
	{
		str = str.replaceAll(AND_CHAR, "&");
		str = str.replaceAll(QUESTION_CHAR, "?");
		str = str.replaceAll(EQUAL_CHAR, "=");
		return str;
	}
}
