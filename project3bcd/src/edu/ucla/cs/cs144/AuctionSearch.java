package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.text.*;
import java.lang.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

public class AuctionSearch implements IAuctionSearch {

	/* 
         * You will probably have to use JDBC to access MySQL data
         * Lucene IndexSearcher class to lookup Lucene index.
         * Read the corresponding tutorial to learn about how to use these.
         *
	 * You may create helper functions or classes to simplify writing these
	 * methods. Make sure that your helper functions are not public,
         * so that they are not exposed to outside of this class.
         *
         * Any new classes that you create should be part of
         * edu.ucla.cs.cs144 package and their source files should be
         * placed at src/edu/ucla/cs/cs144.
         *
         */
	private IndexSearcher searcher = null;
	private QueryParser parser = null;
	
	public SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn) 
	{
		// TODO: Your code here!
		try 
		{	
			searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/var/lib/lucene/index1/"))));
			parser = new QueryParser("content", new StandardAnalyzer());
			Query queryParsed = parser.parse(query);
			TopDocs matchingDocs = searcher.search(queryParsed, numResultsToReturn+numResultsToSkip);
			ScoreDoc[] hits = matchingDocs.scoreDocs;
			int hitsLength = hits.length;

			/*Dynamically allocate array for ID and Name*/
			SearchResult[] results = new SearchResult[hitsLength];
			if(numResultsToSkip > hitsLength)
			{
				return results;
			}
			/*Iterate through each matching doc from ScoreDoc array*/
			for(int i = numResultsToSkip; i < results.length; i++)
			{
				Document doc = searcher.doc(hits[i].doc);
				/*Store the ID and Name in the dynamically allocated array*/ 
				results[i] = new SearchResult(doc.get("ItemID"), doc.get("Name"));
			}
			return results;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return null;
	}

	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {
		// TODO: Your code here!
		ArrayList<SearchResult> tempResults = new ArrayList<SearchResult>();
		String itemID;
		String name;
		ResultSet getRs = null;
		int i = 0;
		String lowerLeft = String.valueOf(region.getLx()) + " " + String.valueOf(region.getLy());
		String upperLeft = String.valueOf(region.getLx()) + " " + String.valueOf(region.getRy());
		String upperRight = String.valueOf(region.getRx()) + " " + String.valueOf(region.getRy());
		String lowerRight = String.valueOf(region.getRx()) + " " + String.valueOf(region.getLy());
		try
		{
			Connection conn = DbManager.getConnection(true);
			Statement stmt = conn.createStatement();
			SearchResult[] basicSReturns = basicSearch(query, 0, 10000); 
			SearchResult[] docsInsidePoly = new SearchResult[basicSReturns.length];
			
			String polygon = "'Polygon((" + lowerLeft + ", " /*LowLeft*/
											+ upperLeft + ", " /*UpLeft*/
											+ upperRight + ", " /*UpRight*/
											+ lowerRight + ", " /*LowRight*/
											+ lowerLeft + "))'";

			//System.out.println(polygon);
			/*Get the itemID from each and then do JDBC*/
	 		for(int k = 0; k < basicSReturns.length; k++)
	 		{
	 			//Each individual doc of (ItemID, Name)
				itemID = basicSReturns[k].getItemId();
				
				String q = "SELECT * " +
							"FROM particles P " +
							"WHERE P.ItemID = '" + itemID + "' AND "+
							"MBRContains(GeomFromText(" + polygon + "), Coordinates);";
				/*Get points from buildSQLIndex particles(ItemID, POINT)*/
				//System.out.println(q);
				getRs = stmt.executeQuery( q );
				if(getRs.next())
				{
					//System.out.println(basicSReturns[k].getName());
					/*ItemID is inside the polygon*/
					tempResults.add(new SearchResult(itemID, basicSReturns[k].getName()));
				}
			}
		} catch (SQLException ex) {
			System.out.println(ex);
		}

		SearchResult[] searchResults = new SearchResult[tempResults.size()];
		
		for(int j = 0; j < tempResults.size(); j++)
		{
			searchResults[j] = tempResults.get(j);
			//System.out.println(searchResults[i].getItemId());
			//System.out.println(searchResults[i].getName());
		}
		return searchResults;

		//return new SearchResult[0];
	}

	public String getXMLDataForItemId(String itemId) {
		// TODO: Your code here!
		try {
			Connection conn = DbManager.getConnection(true);
			Statement stmt = conn.createStatement();

			PreparedStatement pItem = conn.prepareStatement("SELECT * FROM Items WHERE ItemID = ?");
			pItem.setString(1, itemId);
			ResultSet itemRs = pItem.executeQuery();

			/*No itemID*/
			if(!itemRs.next())
			{
				return "";
			}

			/*ItemID*/
			String rootItemID = "<Item ItemID=\"" + itemId + "\">\n";
			
			/*Name*/
			String name = removeTheEscapes(itemRs.getString("Name"));
			name = "\t<Name>" + name + "</Name>\n";

			/*Categories*/
			String category = "";
			PreparedStatement pCate = conn.prepareStatement("SELECT * FROM Categories WHERE ItemID = ?");
			pCate.setString(1, itemId);
			ResultSet catRs = pCate.executeQuery();
			while(catRs.next())
			{
				category += "\t<Category>";
				category += removeTheEscapes(catRs.getString("Category"));
				category += "</Category>\n";
			}

			/*Currently*/
			String currently = String.format("$%.2f", itemRs.getDouble("Currently"));
			currently = "\t<Currently>" + currently + "</Currently>\n";

			/*First Bid*/
			String first_bid = String.format("$%.2f", itemRs.getDouble("First_Bid"));
			first_bid = "\t<First_Bid>" + first_bid + "</First_Bid>\n";

			/*Bids, if no bid <Bids />. If Bid then must get from Bid Table*/
			String bids = "";
			/*For each bid*/
			PreparedStatement pBids = conn.prepareStatement("SELECT * FROM Bids WHERE Bids.ItemID = ?");
			pBids.setString(1, itemId);
			ResultSet bidRs = pBids.executeQuery();
			boolean once = true;
			int counting_bids = 0;
			while(bidRs.next())
			{
				if(once)
				{
					bids += "\t<Bids>\n";
					once = false;
				}
				bids += "\t\t<Bid>\n";
				String bidderID = bidRs.getString("UserID");

				/*Get each user per bid, should be only 1*/
				PreparedStatement pUserBids = conn.prepareStatement("SELECT * FROM Users WHERE UserID = ?");
				pUserBids.setString(1, bidderID);
				ResultSet bidderData = pUserBids.executeQuery();
				while(bidderData.next())
				{
					String bidderRating = bidderData.getString("Rating");
					bidderID = removeTheEscapes(bidderID);
					String bidderLocation = removeTheEscapes(bidderData.getString("Location"));
					String bidderCountry = removeTheEscapes(bidderData.getString("Country"));

					if(bidderLocation == "" && bidderCountry == "")
					{
						bids += "\t\t\t<Bidder Rating=\"" + bidderRating + "\" UserID=\"" + bidderID + "\">\n";
					}
					else
					{
						bids += "\t\t\t<Bidder Rating=\"" + bidderRating + "\" UserID=\"" + bidderID + "\">\n";
						bids += "\t\t\t\t<Location>" + bidderLocation + "</Location>\n";
						bids += "\t\t\t\t<Country" + bidderCountry + "<Country>\n";
						bids += "\t\t\t</Bidder>\n";
					}
					bids += "\t\t\t<Time>" + timestamp(bidRs.getTimestamp("Time").toString()) + "</Time>\n";
					bids += "\t\t\t<Amount>" + String.format("$%.2f", bidRs.getDouble("Amount")) + "</Amount>\n";
					counting_bids++;
				}
				bids += "\t\t<Bid>\n";
				bids += "\t</Bids>\n";
			}
			if(bids == "")
			{
				bids = "\t<Bids />\n";
			}

			/*Location*/
			String location = removeTheEscapes(itemRs.getString("Location"));
			String lat = String.valueOf(itemRs.getDouble("Latitude"));
			String longi = String.valueOf(itemRs.getDouble("Longitude"));
			//System.out.println(lat);
			if(itemRs.getDouble("Latitude") == 0.0 && itemRs.getDouble("Longitude") == 0.0)
			{
				location = "\t<Location>" + location + "</Location>\n"; 
			}
			else
			{
				location = "\t<Location Latitude=\"" + lat + "\" Longitude=\"" + longi + "\">" + location + "</Location>\n";
			}

			/*Number Of Bids*/
			String number_bids;
			number_bids = "\t<Number_Of_Bids>" + counting_bids + "</Number_Of_Bids\n";

			/*Country*/
			String country = removeTheEscapes(itemRs.getString("Country"));
			country = "\t<Country>" + country + "</Country>\n";

			/*Started*/
			String start = timestamp(itemRs.getString("Started")).toString();
			start = "\t<Started>" + start + "</Started>\n";

			/*Ended*/
			String end = timestamp(itemRs.getTimestamp("Ends").toString());
			end = "\t<Ends>" + end + "</Ends>\n";

			/*Seller*/
			String sellerinfo = "";
			String seller = itemRs.getString("UserID");
			PreparedStatement pUser = conn.prepareStatement("SELECT * FROM Users WHERE UserID = ?");
			pUser.setString(1, seller);
			ResultSet userRs = pUser.executeQuery();
			while(userRs.next())
			{
				String rating = "" + userRs.getInt("Rating");
				String userID = removeTheEscapes(userRs.getString("UserID"));
				sellerinfo = "\t<Seller Rating=\"" + rating + "\" " +
									"UserID=\"" + userID + "\" />\n"; 
			}

			/*Description*/
			String description = removeTheEscapes(itemRs.getString("Description"));
			description = "\t<Description>" + description + "</Description>\n";

			String closingRootItem = "</Item>\n";

			return (
				rootItemID + 
				name +
				category +
				currently +
				first_bid +
				number_bids +
				bids +
				location +
				country +
				start +
				end +
				sellerinfo +
				description +
				closingRootItem
			);

		} catch (SQLException s) {
			System.out.println(s);
		}
		return "";
	}

	public String removeTheEscapes(String inputString)
	{
		inputString = inputString.replaceAll("<", "&lt;");
		inputString = inputString.replaceAll(">", "&gt;");
		inputString = inputString.replaceAll("&", "&amp;");
		return inputString;
	}

	public String timestamp(String mysqlTimeStamp)
	{

		String xmlTimeStampStr = "";
		try
		{
			DateFormat formatterDestination = new SimpleDateFormat( "MMM-dd-yy HH:mm:ss" );
			DateFormat formatterOrigin = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
			Date timeStamp = formatterOrigin.parse( mysqlTimeStamp );
			xmlTimeStampStr = formatterDestination.format( timeStamp );
		}
		catch( java.text.ParseException e )
		{
			System.err.println( "Failed parsing timestamp: " + mysqlTimeStamp );
			System.exit( 1 );
		}

		return xmlTimeStampStr;
	}
	
	public String echo(String message) {
		return message;
	}

}
