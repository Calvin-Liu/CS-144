package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.lang.*;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {
    
    /** Creates a new instance of Indexer */
    public Indexer() {
    }
    

    IndexWriter indexWriter = null;
    public void rebuildIndexes() 
    {

        Connection conn = null;
        Directory indexDir = null;
        Statement stmt = null;
        Statement catStmt = null;
        ResultSet rs = null;
        ResultSet catRs = null;
        String itemID;
        String name, categories, description, content;

        // create a connection to the database to retrieve Items from MySQL
        try 
        {
            conn = DbManager.getConnection(true);
            PreparedStatement pstmt = conn.prepareStatement("SELECT Category FROM Categories as C WHERE C.ItemID = ?");
            indexDir = FSDirectory.open(new File("/var/lib/lucene/index1/"));
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
            config.setOpenMode( OpenMode.CREATE );
            stmt = conn.createStatement();
            indexWriter = new IndexWriter(indexDir, config);
            rs = stmt.executeQuery("SELECT Name, ItemID, Description FROM Items");
            catStmt = conn.createStatement();
            while(rs.next())
            {
                /*Items Table*/
                itemID = rs.getString("ItemID");
                name = rs.getString("Name");
                description = rs.getString("Description");

                /*Categories Table*/
                /*Match ItemID with ItemID of categories to retrieve Category*/
                pstmt.setString(1, itemID);
                catRs = pstmt.executeQuery();
                categories = "";
                
                while(catRs.next())
                {
                    categories += catRs.getString("Category") + " ";
                }
                content = name + " " + categories + " " + description;
                catRs.close();

                Document doc = new Document();
                doc.add(new TextField("ItemID", itemID, Field.Store.YES));
                doc.add(new TextField("Name", name, Field.Store.YES));
                doc.add(new TextField("content", content, Field.Store.NO));
                indexWriter.addDocument(doc);
            }
            indexWriter.close();
            // close the database connection
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException n) {
            n.printStackTrace();
        }
        /*
         * Add your code here to retrieve Items using the connection
         * and add corresponding entries to your Lucene inverted indexes.
             *
             * You will have to use JDBC API to retrieve MySQL data from Java.
             * Read our tutorial on JDBC if you do not know how to use JDBC.
             *
             * You will also have to use Lucene IndexWriter and Document
             * classes to create an index and populate it with Items data.
             * Read our tutorial on Lucene as well if you don't know how.
             *
             * As part of this development, you may want to add 
             * new methods and create additional Java classes. 
             * If you create new classes, make sure that
             * the classes become part of "edu.ucla.cs.cs144" package
             * and place your class source files at src/edu/ucla/cs/cs144/.
         * 
         */
    }    

    public static void main(String args[]) {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
    }   
}
