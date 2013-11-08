package cmu18641.bustracker.exceptions;

public class TrackerException extends Throwable {
	
	private static final long serialVersionUID = 1184740826442588787L;

    private int _errorNum;
    private String _srcTag;
	
    private static final String[] _type = {
        "Internet unavailable",
        "Cannot determine current location",
        
        "There is internet, but the app server is unavailable",
        "Bad query result from the app server",
        "Bad query result from the local database",
    	"Uknown error"
	   };

    public TrackerException (int num, String src, String msg)
    {
	    super (msg);
	    _errorNum = num;
	    _srcTag = src;
	}
	
	public String getMessage()
	{
	    return _type[_errorNum] + " at " + _srcTag + "\n" + super.getMessage();
	}
}




