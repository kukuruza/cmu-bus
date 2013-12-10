package bustracker.server.servlets;


import java.io.*; 

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

import bustracker.common.entities.BaseBus;
import bustracker.common.entities.BaseSchedule;
import bustracker.common.entities.BaseScheduleItem;

import com.google.gson.Gson;


@WebServlet("/querymock")
public class QuerymockServlet extends HttpServlet {
	private static final long serialVersionUID = -6483477226845513011L;

	@Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
      throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        
        BaseSchedule schedule = new BaseSchedule();
        //BaseStop stop = new BaseStop ("testStop", "a", "b", 123, -234);
        schedule.setStop ("testStop");
        schedule.addItem (new BaseScheduleItem(new BaseBus("61c", "downtown"), 
        		                               0));
        schedule.addItem (new BaseScheduleItem(new BaseBus("61c", "squirrel hill"), 
                                               1000*60*60));
        
        Gson gson = new Gson();
        String json = gson.toJson(schedule); 
        out.println(json);
    }
}

