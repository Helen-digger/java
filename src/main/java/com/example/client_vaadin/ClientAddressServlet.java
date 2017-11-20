package com.example.client_vaadin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ClientAddressServlet extends HttpServlet {
    protected String doPost(HttpServletRequest request)
            throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        //
        // Get client's IP address
        //
        String clientIP = request.getRemoteAddr();

        //
        // Get client's host name
        //
        String clintHost = request.getRemoteHost();

        //response.setContentType("text/plain");
        //PrintWriter out = response.getWriter();
     return clientIP;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
