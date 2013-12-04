package servlets;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dbhelpers.DatabaseConnector;


@WebServlet("/getdatabase")
public class GetDatabaseServlet extends HttpServlet {

	private static final long serialVersionUID = -650288185628123344L;
	private static final Logger logger = LoggerFactory.getLogger(QueryServlet.class);

    private static final int _bufferSize = 8192;

    
	private void copy (InputStream in, OutputStream out) throws IOException 
	{
	    byte[] buffer = new byte[_bufferSize];
	    int read;

	    while ((read = in.read(buffer, 0, _bufferSize)) != -1)
	    	out.write(buffer, 0, read);
	}
	

	@Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
              throws ServletException, IOException 
    {	
		String resourcePath = DatabaseConnector.getDbPath();
		if (resourcePath == null)
			logger.error("Could not retrieve schedule database path");
		else
    		logger.info("Schedule database path is: " + resourcePath);
		
        FileInputStream in = new FileInputStream (resourcePath);
        ServletOutputStream out = response.getOutputStream();

        // write as byte stream
        copy(in, out);
	}
  
}

