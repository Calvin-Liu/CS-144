This example contains a simple utility class to simplify opening database
connections in Java applications, such as the one you will write to build
your Lucene index. 

To build and run the sample code, use the "run" ant target inside
the directory with build.xml by typing "ant run".

I used the lucene index on the Name of the Item, the Category it is in and the description because when a user is searching for something, the keywords are usually listed in these fields. Indexes should be created for other attributes that are more specific than this. 

I used the StandardAnalyze method to search for the keywords handled by the Lucene Index.

*******
For some reason my project2 is missing 1 tuple in the query that is supposed to return 8365, I don't know why I spoke to the TAs who could not figure it out and the commands I put were ran by another student and they got 8365 as well. It used to work and then I did not change my code and ran project2 again and it changed. It is impossible for 1 query item to be messed up from the 19,000 of them. 
