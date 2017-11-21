package com.example.client_vaadin;

import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;

public class ClientAddressServlet extends HttpServlet {
    public String doPost(VaadinRequest request)
            throws ServletException, IOException {
        VaadinRequest httpServletRequest = (VaadinRequest) request;
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
        System.out.println(clintHost);
     return clientIP;
    }

    protected void doGet(VaadinRequest request, VaadinResponse response)
            throws ServletException, IOException {
        doPost(request);
    }
}
