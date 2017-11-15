package com.example.demo;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

public class CounterVisit extends HttpServlet {

    private int hitCount;

    public void init()
    {
        // Reset hit counter.
        hitCount = 0;
    }

    public void  service (HttpServletRequest req, HttpServletResponse res)
          throws ServletException , IOException {
          Date date = new Date();
          long time = date.getTime();
          time = time / 1000;
          String remote = req.getRemoteAddr();
  }


}
