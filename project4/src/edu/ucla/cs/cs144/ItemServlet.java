package edu.ucla.cs.cs144;

import calvin.examples.*;
import java.util.*;
import javax.xml.bind.*;
import java.io.StringReader;
import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        String itemId = request.getParameter("id");
        if(itemId == null || itemId == "")
        {
            response.sendRedirect("getItem.html");
        }
        else
        {
            AuctionSearchClient temp = new AuctionSearchClient();
    		String itemXML = temp.getXMLDataForItemId(itemId);
            //       if(itemXML != null)
            //           response.getWriter().println("not null");
            //       else
            //           response.getWriter().println("null");
    		request.setAttribute("itemXML", itemXML);
            //response.getWriter().println(itemXML);
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(Item.class);
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

                StringReader reader = new StringReader(itemXML);
                Item item = (Item) unmarshaller.unmarshal(reader);
                request.setAttribute("idOfItem", item.getItemID());
                request.setAttribute("nameOfItem", item.getName());

                List<Category> categoryList = item.getCategory();
                Category[] categoryArray = new Category[categoryList.size()];
                for(int p = 0; p < categoryList.size(); p++)
                {
                    categoryArray[p] = categoryList.get(p);
                }
                request.setAttribute("categoriesOfItem", categoryArray);

                request.setAttribute("currentlyOfItem", item.getCurrently());
                if(item.getBuyPrice() != null)
                {
                    request.setAttribute("buyPriceOfItem", item.getBuyPrice());
                }
                request.setAttribute("firstBidOfItem", item.getFirstBid());
                request.setAttribute("numberOfBidsOfItem", item.getNumberOfBids());

                List<Bid> bidsList = item.getBids().getBid();
                if(bidsList != null)
                {
                    Bid[] bidsArray = new Bid[bidsList.size()];
                    for(int r = 0; r < bidsList.size(); r++)
                    {   
                        bidsArray[r] = bidsList.get(r);
                    }
                    request.setAttribute("bidsOfItem", bidsArray);
                }

                if(item.getLocation() != null)
                {
                    request.setAttribute("locationOfItem", item.getLocation().getvalue());
                    request.setAttribute("latitudeOfItem", item.getLocation().getLatitude());
                    request.setAttribute("longitudeOfItem", item.getLocation().getLongitude());
                }

                request.setAttribute("countryOfItem", item.getCountry());
                request.setAttribute("startedOfItem", item.getStarted());
                request.setAttribute("endsOfItem", item.getEnds());

                request.setAttribute("sellerIdOfItem", item.getSeller().getUserID());
                request.setAttribute("ratingOfSeller", item.getSeller().getRating());

                request.setAttribute("descriptionOfItem", item.getDescription());

                HttpSession session = request.getSession(true);
                session.setAttribute("payNowItemID", item.getItemID());
                session.setAttribute("payNowNameOfItem", item.getName());
                session.setAttribute("payNowBuyPrice", item.getBuyPrice());

                request.getRequestDispatcher("/item.jsp").forward(request, response);

            } catch (JAXBException j) {
                System.out.println("Error in using JAXB");
            }
        }
    }
    public void doPost(HttpServletRequest request,
                     HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);
  }
}
