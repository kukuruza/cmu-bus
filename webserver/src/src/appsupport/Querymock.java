package src.appsupport;


import java.io.*; 
import java.sql.Time;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

import cmu18641.bustracker.common.*;

import com.google.gson.Gson;


@WebServlet("/querymock")
public class Querymock extends HttpServlet {
	private static final long serialVersionUID = -6483477226845513011L;

	@Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
      throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        
        BaseSchedule schedule = new BaseSchedule();
        BaseStop stop = new BaseStop ("testStop", "a", "b", 123, -234);
        schedule.setStop (stop);
        schedule.addItem (new BaseScheduleItem(new BaseBus("61c", "downtown"), 
        		                               new Time(0)));
        
        Gson gson = new Gson();
        String json = gson.toJson(schedule); 
        out.println(json);
    }
}

