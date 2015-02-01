Team: Calvin Liu
ID: 804182525

Relations:
	After discussion and learning about functional dependencies, we can
set up the dependencies and compute the closure in reverse to know what the 
keys are to avoid duplicates upon making different tables and following the 
dependencies.
	
	Item(
		ItemID, //KEY
		UserID,
		Name,
		Buy_Price,
		Description,
		First_Bid,
		Started,
		Ends,
		Currently,
		Location,
		Country,
		Latitude,
		Longitude
	)
	
	Bid(
		BidID, //KEY
		UserID,
		ItemID,
		Time,
		Amount
	)
	 
	User(
		UserID, //KEY 
		Location, 
		Country, 
		Rating
	)
	
	Categories(
		ItemID,
		Category
	)

	ItemID -> Name, Buy_Price, Description, First_Bid, Started, Ends, Currently, Location, Country
	BidID -> UserID, ItemID, Time, Amount
	UserID -> Location, Country, Rating

	Note: Number_Of_Bids is not needed with this


	For these relations, I used BCNF form instead of 4NF because we dont have multivalued dependencies where one attribute yields two different values for different attributes
