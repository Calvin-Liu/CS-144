Relations:
	After discussion and learning about functional dependencies, we can
set up the dependencies and compute the closure in reverse to know what the keys are to avoid duplicates upon making different tables and following the dependencies. Keys are marked with a *
	
	Name, ItemID, Category, Buy_Price, Description, First_Bid, Amount, Start, End, Location, Country
	Item(ItemID*, )
	
	Currently, Number_of_Bids, Time, Amount
	Bid()
	
	Location, Country, UserID, Rating 
	User(UserID*, Location, Country, Rating)


For these relations, I used BCNF form instead of 4NF because we dont have multivalued dependencies where one attribute yields two different values for different attributes
