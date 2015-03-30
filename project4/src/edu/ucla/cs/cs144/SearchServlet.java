package edu.ucla.cs.cs144;
import java.io.IOException;
import java.io.*;
import edu.ucla.cs.cs144.AuctionSearchClient;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.NumberFormatException;

public class SearchServlet extends HttpServlet implements Servlet {
       
    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
    	String q = request.getParameter("q");
		if(q == null)
		{
			response.sendRedirect("keywordSearch.html");
		}
		else
		{
	        int numResultsToSkip = 0;
	        int numResultsToReturn = 20;
			try 
			{
				numResultsToSkip = Integer.parseInt(request.getParameter("numResultsToSkip"));
			} catch (NumberFormatException n) {
				/*Check to see if its a number*/
				numResultsToSkip = 0;
			} finally {
				if(numResultsToSkip < 0)
					numResultsToSkip = 0;
			}
			try
			{
				numResultsToReturn = Integer.parseInt(request.getParameter("numResultsToReturn"));
			} catch (NumberFormatException n) {
				numResultsToReturn = 20;
			} finally {
				if(numResultsToReturn < 0)
					numResultsToReturn = 20;
			}

			AuctionSearchClient temp = new AuctionSearchClient();
			SearchResult[] results = temp.basicSearch(q, numResultsToSkip, numResultsToReturn);
			
			numResultsToSkip += 20;
			int numResultsToSkipPrevious = numResultsToSkip - 40;

			request.setAttribute("bResults", results);
			request.setAttribute("query", q);
			request.setAttribute("numResultsToReturn", numResultsToReturn);
			request.setAttribute("numResultsToSkip", numResultsToSkip);
			request.setAttribute("numResultsToSkipPrevious", numResultsToSkipPrevious);
			request.getRequestDispatcher("/search.jsp").forward(request, response);
		}
	}
}
