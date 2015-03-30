package edu.ucla.cs.cs144;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProxyServlet extends HttpServlet implements Servlet {
       
    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        response.setContentType("text/xml");
        String q = request.getParameter("q");
        q = q.trim();
        URL url = new URL("http://www.google.com/complete/search?output=toolbar&q=" + URLEncoder.encode(q, "UTF-8"));
        HttpURLConnection httpconnection = (HttpURLConnection) url.openConnection();
        httpconnection.connect();

        BufferedReader buffR = new BufferedReader(new InputStreamReader(httpconnection.getInputStream()));

        String fillThisString = "";
        String temp;
        while( (temp = buffR.readLine()) != null )
        {
        	fillThisString += temp;
        }

        buffR.close();
        httpconnection.disconnect();
        response.getWriter().println(fillThisString);
    }
}
