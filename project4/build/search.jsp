<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="edu.ucla.cs.cs144.SearchResult" %>

<html>
	<head>
		<title>Ebay Item Search</title>
	</head>
	<body>
	<!--Table and rows-->
		<%
			out.println("<h2>Your search results for <i>'" + request.getParameter("q") + "'</i></h2>");

			SearchResult[] basicResults = (SearchResult[])request.getAttribute("bResults");

			out.println("<script type=\"text/javascript\" src=\"autosuggest1.js\"></script>" +
			"<link rel=\"stylesheet\" type=\"text/css\" href=\"suggest.css\" />");

			out.println("<script type=\"text/javascript\">" +
            "window.onload = function () {" +
                "var oTextbox = new AutoSuggestControl(document.getElementById(\"suggestion\"));" + 
            "}" +
        		"</script>");

			out.println("<form action=/eBay/search method=\"get\" itemid=\"searchForm\">" + 
				"<input id=\"suggestion\" name=\"q\" type=\"text\">" +
				"<input type=\"hidden\" name=\"numResultsToSkip\" value=\"0\">" +
				"<input type=\"hidden\" name=\"numResultsToReturn\" value=\"20\">" +
				"<input type=submit>" +
			"</form>");

			if(basicResults == null || basicResults.length == 0)
			{
				out.println("<h5>We're sorry, there are no items with the following query <i>'" + request.getParameter("q") + "'</i></h5>");
			}
			else 
			{
				out.println("<table style=" + "\"width:100%\"" + ">");
				for(int k = 0; k < basicResults.length; k++)
				{
					out.println("<tr>");
					out.println("<td>" + 
									"<a href=\"/eBay/item?id=" + basicResults[k].getItemId() + "\">"
									 + basicResults[k].getItemId() + 
									"</a>" +
								"</td>");
					
					out.println("<td>" + basicResults[k].getName() + "</td>");
					out.println("</tr>");
				}
				out.println("</table>");
				
				out.println("<div style=\"float:left;\">" +
								"<h3>" +
									"<a href=\"/eBay/search?q=" + request.getAttribute("query") + "&numResultsToSkip=" + request.getAttribute("numResultsToSkipPrevious") + "&numResultsToReturn=" + request.getAttribute("numResultsToReturn") + "\">" + "Previous</a>" +
								"</h3>" +
							"</div>");

				out.println("<div style=\"float:right;\">" +
								"<h3>" +
									"<a href=\"/eBay/search?q=" + request.getAttribute("query") + "&numResultsToSkip=" + request.getAttribute("numResultsToSkip") + "&numResultsToReturn=" + request.getAttribute("numResultsToReturn") + "\">" + "Next</a>" +
								"</h3>" +
							"</div>");

			}
		%>
	</body>
</html>