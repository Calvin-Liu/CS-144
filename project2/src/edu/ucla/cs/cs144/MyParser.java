/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

class MyParser {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
    
    static final String[] typeName = {
    "none",
    "Element",
    "Attr",
    "Text",
    "CDATA",
    "EntityRef",
    "Entity",
    "ProcInstr",
    "Comment",
    "Document",
    "DocType",
    "DocFragment",
    "Notation",
    };
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     * Array 
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or 
     * null if one does not exist. NR means Non-Recursive.
     * To get the attribute
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     * To get the actual text within the tags
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }
    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
        
        /*Store all items in a list to iterate through and parse with tags Item*/
        Element[] itemslist = getElementsByTagNameNR(doc.getDocumentElement(), "Item");
        
        try
        {
            for(int i = 0; i < itemslist.length; i++)
            {
                parseItems(itemslist[i]);
                parseUsers(itemslist[i]);
                parseBids(itemslist[i]);
                parseCategories(itemslist[i]);
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
        /**************************************************************/      
    }
    
    public static BufferedWriter itemsDoc;
    public static BufferedWriter usersDoc;
    public static BufferedWriter bidsDoc;
    public static BufferedWriter categoryDoc;
    
    public static String makeTuples(String[] input)
    {
        String tupleRows = "";
        int k;
        for(k = 0; k < input.length-1; k++)
        {
            tupleRows += input[k] + columnSeparator;
        }
        /*Append the last element without separator*/
        tupleRows += input[k];
        return tupleRows;
    }
    
    public static void load(BufferedWriter output, String... args) throws IOException
    {
        if(args.length == 0)
        {
            System.out.println("Error in loading the file");
            return;
        }
        else
        {
            output.write(makeTuples(args));
            output.newLine();
        }
    }
    
    public static String timestamp(String XMLdate)
    {
        SimpleDateFormat correctFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat wrongFormat = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        try
        {
            Date parsedDate = wrongFormat.parse(XMLdate);
            return correctFormat.format(parsedDate);
        } catch(ParseException p) {
            System.err.println("Error! Could not parse the time!");
            return "blah";
        }
    }
    
    public static void parseItems(Element itemslist) throws IOException
    {
        String itemID = itemslist.getAttribute("ItemID");
        Element seller = getElementByTagNameNR(itemslist, "Seller");
        String userID = seller.getAttribute("UserID");
        String name = getElementTextByTagNameNR(itemslist, "Name");
        String buy_price = strip(getElementTextByTagNameNR(itemslist, "Buy_Price"));
        String description = getElementTextByTagNameNR(itemslist, "Description");
        if(description.length() > 4000)
        {
            description = description.substring(0, 3999);
        }
        String first_bid = strip(getElementTextByTagNameNR(itemslist, "First_Bid"));
        String started = timestamp(getElementTextByTagNameNR(itemslist, "Started")).toString();
        String ends = timestamp(getElementTextByTagNameNR(itemslist, "Ends")).toString();
        String currently = strip(getElementTextByTagNameNR(itemslist, "Currently"));
        
        load(itemsDoc, itemID, userID, name, buy_price, description, first_bid, started, ends, currently);
    }
    
    public static void parseUsers(Element itemslist) throws IOException
    {
        Element sellingPerson = getElementByTagNameNR(itemslist, "Seller");
        String userID = sellingPerson.getAttribute("UserID");
        String location = getElementText(getElementByTagNameNR(itemslist, "Location"));
        if(location == null)
        {
            location = "";
        }
        String country = getElementText(getElementByTagNameNR(itemslist, "Country"));
        if(country == null)
        {
            country = "";
        }
        String rating = sellingPerson.getAttribute("Rating");
        
        Element[] bids = getElementsByTagNameNR(getElementByTagNameNR(itemslist, "Bidder"), "Bid");
        for(int j = 0; j < bids.length; j++)
        {
            Element bidder = getElementByTagNameNR(bids[j], "Bidder");
            String bidderID = bidder.getAttribute("UserID");
            String bidderRating = bidder.getAttribute("Rating");
            String bidderLocation = bidder.getAttribute("Location");
            if(bidderLocation == null)
            {
                bidderLocation = "";
            }
            String bidderCountry = bidder.getAttribute("Country");
            if(bidderCountry == null)
            {
                bidderCountry = "";
            }
            load(usersDoc, bidderID, bidderRating, bidderLocation, bidderCountry);
        }
        
        load(usersDoc, userID, location, country, rating);
    }
    
    private static int bidID = 0;
    public static void parseBids(Element itemslist) throws IOException
    {
        /*bidID already handled*/
        String itemID = itemslist.getAttribute("ItemID");
        Element[] bidss = getElementsByTagNameNR(getElementByTagNameNR(itemslist, "Bids"), "Bid");
        for(int k = 0; k < bidss.length; k++)
        {
            bidID++;
            Element bidder = getElementByTagNameNR(bidss[k], "Bidder");
            String userID = bidder.getAttribute("UserID");
            String wrongTime = getElementTextByTagNameNR(bidss[k], "Time");
            String correctTime = timestamp(wrongTime).toString();
            String amount = strip(getElementTextByTagNameNR(bidss[k], "Amount"));
            
            load(bidsDoc, String.valueOf(bidID), userID, itemID, correctTime, amount);
        }
    }
    
    public static void parseCategories(Element itemslist) throws IOException
    {
        String itemID = itemslist.getAttribute("ItemID");
        Element[] categories = getElementsByTagNameNR(itemslist, "Category");
        for(int i = 0; i < categories.length; i++)
        {
            String category = getElementText(categories[i]);
            
            load(categoryDoc, itemID, category);
        }
    }
    
    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }
    }
}
