<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="edu.ucla.cs.cs144.SearchResult" %>
<%@ page import="calvin.examples.*" %>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.DateFormat"%>

<html>
	<head>
		<title>
			Confirmation Page
		</title>
		<h1>Thank you for your purchase!</h1>
	</head>
	<body>
		<h3>You have successfully purchased:</h3>
		<%
			String idOfItem = (String)session.getAttribute("payNowItemID");
			String nameOfItem = (String)session.getAttribute("payNowNameOfItem");
			String buyPriceOfItem = (String)session.getAttribute("payNowBuyPrice");
			String creditCardNumber;
			String date = new Date().toString();

			out.println("<strong>ItemID: " + idOfItem + "</strong><br>");
			out.println("<strong>Item Name: " + nameOfItem + "</strong><br>");
			out.println("<strong>Buy Price: " + buyPriceOfItem + "</strong><br>");
			out.println("<strong>Credit Card Number: " + "</strong>");
			out.println("<i>" + request.getParameter("cardInfo") + "</i>");
			out.println("<div><strong>Successfully purchased at:  " + date + "</strong></div>");
		%>
	</body>
</html>