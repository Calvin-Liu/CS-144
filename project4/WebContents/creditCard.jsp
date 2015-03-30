<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="edu.ucla.cs.cs144.SearchResult" %>
<%@ page import="calvin.examples.*" %>

<html>
	<head>
		<title>Credit Card Input</title>
		<h1>Please fill in your credit card information</h1>
	</head>
	<body>
		<%
			String idOfItem = (String)session.getAttribute("payNowItemID");
			String nameOfItem = (String)session.getAttribute("payNowNameOfItem");
			String buyPriceOfItem = (String)session.getAttribute("payNowBuyPrice");

			out.println("<strong>ItemID: " + idOfItem + "</strong><br>");
			out.println("<strong>Item Name: " + nameOfItem + "</strong><br>");
			out.println("<strong>Buy Price: " + buyPriceOfItem + "</strong><br>");
			out.println("<strong>Credit Card Number: " + "</strong><br>");
		
			out.println("<form action=\"https://" + request.getServerName() + ":8443/eBay/confirmation.jsp\" method=\"post\">");
			out.println("<input type=\"text\" name=\"cardInfo\">");
			out.println("<input type=\"submit\" name=\"pay\">");
			out.println("</form>");
		%>
	</body>
</html>