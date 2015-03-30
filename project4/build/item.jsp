<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="edu.ucla.cs.cs144.SearchResult" %>
<%@ page import="calvin.examples.*" %>

<html>
	<head>
		<title>Item Search</title>
	</head>

	<body onload="initialize()">
		<%
			String itemIDString = (String)request.getAttribute("itemXML");
			out.println("<h2>Your search results for <i>'" + request.getParameter("id") + "'</i></h2>");
			out.println("<form action=/eBay/item method=\"get\">" +
				"ID:" + 
				"<input type=\"text\" name=\"id\" id=\"id\">" +
				"<input type=\"submit\">" +
				"</form>");
			if(itemIDString == null || itemIDString == "")
			{
				out.println("<h5>We're sorry, there are no items with the following query <i>'" + request.getParameter("id") + "'</i></h5>");
			}
			else
			{
				String idOfItem = (String)request.getAttribute("idOfItem");
				String nameOfItem = (String)request.getAttribute("nameOfItem");
				String categoryOfItem = (String)request.getAttribute("categoryOfItem");
				String currentlyOfItem = (String)request.getAttribute("currentlyOfItem");
				String buyPriceOfItem = (String)request.getAttribute("buyPriceOfItem");
				String firstBidOfItem = (String)request.getAttribute("firstBidOfItem");
				String numberOfBidsOfItem = (String)request.getAttribute("numberOfBidsOfItem");
				String countryOfItem = (String)request.getAttribute("countryOfItem");
				String startedOfItem = (String)request.getAttribute("startedOfItem");
				String endsOfItem = (String)request.getAttribute("endsOfItem");
				String descriptionOfItem = (String)request.getAttribute("descriptionOfItem");

				Category[] tempCategory = (Category[])request.getAttribute("categoriesOfItem");


				String locationOfItem = (String)request.getAttribute("locationOfItem");
				String latitudeOfItem = (String)request.getAttribute("latitudeOfItem");
				String longitudeOfItem = (String)request.getAttribute("longitudeOfItem");
				String sellerIdOfItem = (String)request.getAttribute("sellerIdOfItem");
				String ratingOfSeller = (String)request.getAttribute("ratingOfSeller");

				Bid[] tempBids = (Bid[])request.getAttribute("bidsOfItem");

				String bidderID = (String)request.getAttribute("bidderID");
				String bidderRating = (String)request.getAttribute("bidderRating");
				String bidderLocation = (String)request.getAttribute("bidderLocation");
				String bidderLatitude = (String)request.getAttribute("bidderLatitude");
				String bidderLongitude = (String)request.getAttribute("bidderLongitude");


				out.println("<table style=" + "\"width:100%\"" + ">");
				out.println("<tr><td>ID: " + idOfItem + "</td></tr>");
				out.println("<tr><td>Name: " + nameOfItem + "</td></tr>");

				for(int i = 0; i < tempCategory.length; i++)
				{
					out.println("<tr><td>Category: " + tempCategory[i].getvalue() + "</td></tr>");
				}

				out.println("<tr><td>Currently: " + currentlyOfItem + "</td></tr>");
		%>
		<%
				if(buyPriceOfItem != null)
				{
					out.println("<tr><td>Buy Price: " + buyPriceOfItem + "</td></tr>");

					out.println("<tr><td><form action=\"http://"+ request.getServerName() + ":" + request.getServerPort() + "/eBay/creditCard.jsp\" method=\"get\">");
					out.println("<input type=\"submit\" value=\"Buy Now\"" + "\">");
					out.println("</td></tr></form>");
				}
		%>
		<%
				out.println("<tr><td>First Bid: " + firstBidOfItem + "</td></tr>");
				out.println("<tr><td>Number of Bids: " + numberOfBidsOfItem + "</td></tr>");

				for(int h = 0; h < tempBids.length; h++)
				{
					out.println("<tr><td>Bidder ID: " + tempBids[h].getBidder().getUserID() + "</td></tr>");
					out.println("<tr><td>Bidder Rating: " + tempBids[h].getBidder().getRating() + "</td></tr>");
					out.println("<tr><td>Bidder Location: " + tempBids[h].getBidder().getLocation().getvalue() + "</td></tr>");
					if(tempBids[h].getBidder().getLocation().getLatitude() != null)
						out.println("<tr><td>Bidder Latitude: " + tempBids[h].getBidder().getLocation().getLatitude() + "</td></tr>");
					if(tempBids[h].getBidder().getLocation().getLongitude() != null)
						out.println("<tr><td>Bidder Latitude: " + tempBids[h].getBidder().getLocation().getLongitude() + "</td></tr>");
					out.println("<tr><td>Bidder Country: " + tempBids[h].getBidder().getCountry() + "</td></tr>");
					out.println("<tr><td>Made a bid at: " + tempBids[h].getTime() + "</td></tr>");
					out.println("<tr><td>Bid Amount: " + tempBids[h].getAmount() + "</td></tr>");
				}

				if(locationOfItem != null)
					out.println("<tr><td>Location: " + locationOfItem + "</td></tr>");
				if(latitudeOfItem != null)
					out.println("<tr><td>Latitude: " + latitudeOfItem + "</td></tr>");
				if(longitudeOfItem != null)
					out.println("<tr><td>Longitude: " + longitudeOfItem + "</td></tr>");

				out.println("<tr><td>Country: " + countryOfItem + "</td></tr>");
				out.println("<tr><td>Started: " + startedOfItem + "</td></tr>");
				out.println("<tr><td>Ends: " + endsOfItem + "</td></tr>");

				if(sellerIdOfItem != null)
					out.println("<tr><td>Seller ID: " + sellerIdOfItem + "</td></tr>");
				if(ratingOfSeller != null)
					out.println("<tr><td>Seller Rating: " + ratingOfSeller + "</td></tr>");

				out.println("<tr><td>Description: " + descriptionOfItem + "</td></tr>");

				out.println("</table");



			}
		%>
		
		<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
		<style type="text/css"> 
		  html { height: 100% } 
		  body { height: 100%; margin: 0px; padding: 0px } 
		  #map_canvas { height: 100% } 
		</style> 
		<script type="text/javascript" 
		    src="http://maps.google.com/maps/api/js?sensor=false"> 
		</script> 
		<c:set name="latitudeOfItem" value=latitudeOfItem />
		<c:set name="longitudeOfItem" value= longitudeOfItem />
		<script type="text/javascript"> 
		  function initialize() 
		  { 
		  	var lat = "${latitudeOfItem}";
		  	var lng = "${longitudeOfItem}";
		  	if(lat == null && lng == null)
		  	{
		  		    	var latlng = new google.maps.LatLng(lat,lng);
		  		    	var myOptions = 
		  		    	{ 
		  		      		zoom: 1, // default is 8  
		  		      		center: latlng, 
		  		      		mapTypeId: google.maps.MapTypeId.ROADMAP 
		  		    	}; 
		  		    	var map = new google.maps.Map(document.getElementById("map_canvas"),
		  		        	myOptions); 
		  	}
		  	else
		  	{
		    	var latlng = new google.maps.LatLng(lat,lng);
		    	var myOptions = 
		    	{ 
		      		zoom: 14, // default is 8  
		      		center: latlng, 
		      		mapTypeId: google.maps.MapTypeId.ROADMAP 
		    	}; 
		    	var map = new google.maps.Map(document.getElementById("map_canvas"),
		        	myOptions); 
				var marker = new google.maps.Marker({
					position: latlng,
					map: map,
					title: 'Hello World!'
				});
			}
		  } 
		</script> 
	 
		<div id="map_canvas" style="width:50%; height:50%"></div> 
		
		
	</body>
</html>
